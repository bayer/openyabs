/*
 *  This file is part of MP.
 *
 *      MP is free software: you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation, either version 3 of the License, or
 *      (at your option) any later version.
 *
 *      MP is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *
 *      You should have received a copy of the GNU General Public License
 *      along with MP.  If not, see <http://www.gnu.org/licenses/>.
 */
package mpv5;

import ag.ion.bion.officelayer.application.IOfficeApplication;
import ag.ion.bion.officelayer.application.OfficeApplicationException;
import ag.ion.noa.NOAException;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import mpv5.db.common.NodataFoundException;
import mpv5.ui.frames.MPView;
import mpv5.logging.*;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

import com.l2fprod.common.swing.plaf.LookAndFeelAddons;
import enoa.connection.NoaConnection;
import java.awt.Font;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import mpv5.db.common.ConnectionTypeHandler;
import mpv5.db.common.Context;
import mpv5.db.common.DatabaseConnection;
import mpv5.db.common.DatabaseObject;
import mpv5.db.common.DatabaseObjectLock;
import mpv5.db.common.QueryHandler;
import mpv5.db.objects.Account;
import mpv5.db.objects.Contact;
import mpv5.globals.Constants;
import mpv5.globals.LocalSettings;
import mpv5.globals.Messages;
import mpv5.pluginhandling.MPPLuginLoader;
import mpv5.ui.dialogs.Popup;
import mpv5.ui.dialogs.SplashScreen;
import mpv5.ui.dialogs.Wizard;
import mpv5.ui.dialogs.subcomponents.wizard_DBSettings_1;


import mpv5.db.objects.User;
import mpv5.handler.SDBObjectGenerator;
import mpv5.handler.SimpleDatabaseObject;
import mpv5.i18n.LanguageManager;
import mpv5.pluginhandling.UserPlugin;
import mpv5.server.MPServer;
import mpv5.ui.dialogs.subcomponents.ControlPanel_Fonts;
import mpv5.ui.dialogs.subcomponents.ControlPanel_Userproperties;
import mpv5.utils.files.FileDirectoryHandler;
import mpv5.utils.files.FileReaderWriter;
import mpv5.utils.text.TypeConversion;
import org.apache.commons.cli2.*;
import org.apache.commons.cli2.builder.*;
import org.apache.commons.cli2.commandline.Parser;
import org.apache.commons.cli2.util.*;

/**
 * The main class of the application.
 */
public class Main extends SingleFrameApplication {

    public static SplashScreen splash;
    private static boolean removeplugs = false;
    /**
     * Is true if the application is running, false if in editor
     */
    public static boolean INSTANTIATED = false;
    private static Integer FORCE_INSTALLER;

    /**
     * Use this method to (re) cache data from the database to avoid uneccessary db queries
     */
    public static void cache() {
        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                MPView.addMessage(Messages.CACHE);
                User.cacheUser();
                MPView.addMessage(Messages.CACHED_OBJECTS + ": " + Context.getUser());
                LanguageManager.getCountriesAsComboBoxModel();
                MPView.addMessage(Messages.CACHED_OBJECTS + ": " + Context.getCountries());
            }
        };
        new Thread(runnable).start();
//        Account.cacheAccounts();//pre cache accounts
//        MPView.addMessage(Messages.CACHED_OBJECTS + ": " + Context.getAccounts());
//        DatabaseObject.cacheObjects();//Is called by User.login() later
    }

    private static void useNetbookOpt() {
        ControlPanel_Fonts.applyFont(new Font("Dialog", Font.PLAIN, 11));
        MPView.setNavBarAnimated(false);
        MPView.setTabPaneScrolled(true);
    }

    /**
     * Add the office instance to close on exit
     * @param officeApplication
     */
    public static void addOfficeApplicationToClose(IOfficeApplication officeApplication) {
        oap.add(officeApplication);
    }
    private static List<IOfficeApplication> oap = new Vector<IOfficeApplication>();
    private File lockfile = new File(MPPATH + File.separator + "." + Constants.PROG_NAME + "." + "lck");

    /**
     * Read in local settings and launch the application
     */
    public static void start() {

        splash.nextStep(Messages.LOCAL_SETTINGS.toString());
        try {
            LocalSettings.read();
            LocalSettings.apply();
        } catch (Exception ex) {
            Log.Debug(Main.class, ex.getMessage());
            Log.Debug(Main.class, "Local settings file not readable: " + LocalSettings.getLocalFile());
        }

        splash.nextStep(Messages.LAUNCH.toString());
        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                launch(Main.class, new String[]{});
            }
        };
        SwingUtilities.invokeLater(runnable);
    }
    /**
     * Indicates whether this is the first start of the application
     */
    public static boolean firstStart;
    /**
     * Sometimes it is nice to have a good goodbye message at hand ;-)<br/><br/>
     * <b>"So Long, and Thanks for All the Fish."</b>
     */
    public static final String GOODBYE_MESSAGE = "So Long, and Thanks for All the Fish.";
    /**
     * Indicates whether the server component shall start
     */
    public static boolean START_SERVER = false;

    /**
     * At startup create and show the main frame of the application.
     */
    @Override
    protected void startup() {
        getContext().getLocalStorage().setDirectory(new File(Main.MPPATH));
        splash.nextStep(Messages.FIRST_INSTANCE.toString());
        if (LocalSettings.getProperty(LocalSettings.DBTYPE).equals("single") && !firstInstance()) {
            System.exit(1);
        }
        splash.nextStep(Messages.DB_CHECK.toString());
        if (FORCE_INSTALLER == null) {
            if (probeDatabaseConnection()) {
                go(false);
            } else if (Popup.Y_N_dialog(splash, Messages.NO_DB_CONNECTION, Messages.ERROR_OCCURED.toString())) {
                splash.dispose();
                showDbWiz(null);
            } else {
                System.exit(1);
            }
        } else {
            showDbWiz(FORCE_INSTALLER);
        }
    }

    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     * @param root
     */
    @Override
    protected void configureWindow(java.awt.Window root) {
    }

    /**
     * A convenient static getter for the application instance.
     * @return the instance of Main
     */
    public static Main getApplication() {
        return Application.getInstance(Main.class);
    }

    @Override
    protected void shutdown() {
        MPView.setWaiting(true);
        MPView.setProgressRunning(true);
        DatabaseObjectLock.releaseAllObjectsFor(MPView.getUser());
        try {
            LocalSettings.save();
            if (!MPView.getUser().isDefault()) {
                MPView.getUser().logout();
            }
            for (int i = 0; i < oap.size(); i++) {
                IOfficeApplication iOfficeApplication = oap.get(i);
                try {
                    iOfficeApplication.getDesktopService().terminate();
                } catch (Exception n){}
            }
            NoaConnection.stopOOOServer();
        } catch (Exception e) {
            Log.Debug(e);
        }
        if (Log.getLoglevel() == Log.LOGLEVEL_DEBUG) {
            Log.Debug(Main.class, QueryHandler.instanceOf().getStatistics());
        }
        Log.Print(GOODBYE_MESSAGE);
        try {
            clearLockFile();
        } catch (Exception e) {
        }
        super.shutdown();
    }

    /**
     * Main method launching the application.
     * @param args
     * @throws Exception 
     */
    public static void main(String[] args) throws Exception {

        INSTANTIATED = true;
        try {
            splash = new SplashScreen(new ImageIcon(Test.class.getResource(mpv5.globals.Constants.SPLASH_IMAGE)));
            splash.init(8);
            Log.Print(Messages.START_MESSAGE);

            splash.nextStep(Messages.INIT.toString());
            getOS();
            setEnv(null);
            parseArgs(args);
            setDerbyLog();
            start();
        } catch (Exception e) {
            throw e;
        }
    }
    /**
     * Inicates if the OS is a Windows version
     */
    public static boolean IS_WINDOWS = false;
    /**
     * The path for db, cache, session files
     */
    public static String MPPATH = null;
    /**
     * The local settings file
     */
    public static String SETTINGS_FILE = null;
    /**
     * The user home directory
     */
    public static String USER_HOME = null;
    /**
     * A shortcut to the user desktop
     */
    public static String DESKTOP = null;

    private static void getOS() {
        if (System.getProperty("os.name").contains("Windows")) {
            IS_WINDOWS = true;
        } else {
            IS_WINDOWS = false;
        }
    }

    /**
     * Set the dirs
     * @param rootDir The root dir or null, defaults to: USER_HOME + File.separator + ".yabs"
     */
    public static void setEnv(String rootDir) {

        if (IS_WINDOWS) {
            USER_HOME = System.getenv("USERPROFILE");
        } else {
            USER_HOME = System.getProperty("user.home");
        }

        DESKTOP = USER_HOME + File.separator + "Desktop";
        if (rootDir == null) {
            MPPATH = USER_HOME + File.separator + ".yabs";
        } else {
            MPPATH = rootDir;
        }
        SETTINGS_FILE = Main.MPPATH + File.separator + "settings" + Constants.RELEASE_VERSION + ".yabs";
    }

    private static void parseArgs(String[] args) {

        DefaultOptionBuilder obuilder = new DefaultOptionBuilder();
        ArgumentBuilder abuilder = new ArgumentBuilder();
        GroupBuilder gbuilder = new GroupBuilder();

        Argument option = abuilder.withName("option").withMinimum(1).withMaximum(1).create();
        Argument filearg = abuilder.withName("file").withMinimum(1).withMaximum(1).create();
        Argument dirarg = abuilder.withName("directory").withMinimum(1).withMaximum(1).create();
        Argument number = abuilder.withName("number").withMinimum(1).withMaximum(1).create();

        Option server = obuilder.withShortName("server").withShortName("serv").withDescription("start built-in server component").create();
        Option showenv = obuilder.withShortName("showenv").withShortName("se").withDescription("show environmental variables").create();
        Option netbook = obuilder.withShortName("netbook").withShortName("net").withDescription("use netbook size optimizations").create();
        Option help = obuilder.withShortName("help").withShortName("h").withDescription("print this message").create();
        Option license = obuilder.withShortName("license").withShortName("li").withDescription("print license").create();
        Option version = obuilder.withShortName("version").withDescription("print the version information and exit").create();
        Option verbose = obuilder.withShortName("verbose").withDescription("be extra verbose").create();
        Option nolf = obuilder.withShortName("nolf").withDescription("use system L&F instead of Tiny L&F").create();
        Option debug = obuilder.withShortName("debug").withDescription("debug logging").create();
        Option removeplugins = obuilder.withShortName("removeplugins").withDescription("remove all plugins which would be loaded").create();
        Option logfile = obuilder.withShortName("logfile").withShortName("l").withDescription("use file for log").withArgument(filearg).create();
        Option mpdir = obuilder.withShortName("appdir").withShortName("dir").withDescription("set the application main dir").withArgument(dirarg).create();
        Option connectionInstance = obuilder.withShortName("connectionInstance").withShortName("conn").withDescription("Use stored connection with this ID").withArgument(number).create();
        Option windowlog = obuilder.withShortName("windowlog").withDescription("Enables logging to the MP Log Console").create();
        Option consolelog = obuilder.withShortName("consolelog").withDescription("Enables logging to STDOUT").create();


        Group options = gbuilder.withName("options").
                withOption(help).
                withOption(version).
                withOption(verbose).
                withOption(debug).
                withOption(license).
                withOption(nolf).
                withOption(netbook).
                withOption(showenv).
                withOption(removeplugins).
                withOption(connectionInstance).
                withOption(logfile).
                withOption(server).
                withOption(windowlog).
                withOption(consolelog).
                withOption(mpdir).
                create();

        HelpFormatter hf = new HelpFormatter();
        Parser p = new Parser();
        p.setGroup(options);
        p.setHelpFormatter(hf);

        CommandLine cl = p.parseAndHelp(args);

        if (cl == null) {
            System.err.println("Cannot parse arguments");
        } else {

            if (cl.hasOption(mpdir)) {
                setEnv(cl.getValue(mpdir).toString());
            }

            if (cl.hasOption(connectionInstance)) {
                try {
                    if (!LocalSettings.hasConnectionID(Integer.valueOf(String.valueOf(cl.getValue(connectionInstance))))) {
                        FORCE_INSTALLER = Integer.valueOf(String.valueOf(cl.getValue(connectionInstance)));
                    }
                    Log.Debug(Main.class, "Switching connection id to: " + Integer.valueOf(String.valueOf(cl.getValue(connectionInstance))));
                    LocalSettings.setConnectionID(Integer.valueOf(String.valueOf(cl.getValue(connectionInstance))));
                } catch (Exception ex) {
                    Log.Debug(ex);
                }
            }

            if (cl.hasOption(help)) {
                hf.print();
                System.exit(0);
            }

            if (cl.hasOption(license)) {
                try {
                    System.out.print(new FileReaderWriter(new File(Main.class.getResource("/mpv5/resources/license/gpl-3").toURI())).read());
                } catch (Exception ex) {
                    Log.Debug(ex);
                }
            }

            if (cl.hasOption(version)) {
                System.out.println("MP Version: " + Constants.VERSION);
            }

            if (cl.hasOption(verbose)) {
                Log.setLogLevel(Log.LOGLEVEL_NORMAL);

            }

            if (cl.hasOption(debug)) {
                Log.setLogLevel(Log.LOGLEVEL_DEBUG);

            }

            if (cl.hasOption(logfile)) {
                try {
                    LogConsole.setLogFile(((String) cl.getValue(logfile)).split("=")[1]);
                } catch (Exception e) {
                    Log.Debug(Main.class, "Fehler beim Schreiben der Logdatei: " + e.getMessage());
                }
            }

            if (cl.hasOption(nolf)) {
                setLaF(null);
            }

            if (cl.hasOption(netbook)) {
                useNetbookOpt();
            }

            if (cl.hasOption(removeplugins)) {
                removeplugs = true;
            }

            if (cl.hasOption(showenv)) {
                printEnv();
            }

            if (cl.hasOption(server)) {
                START_SERVER = true;
            }

            LogConsole.setLogStreams(cl.hasOption(logfile), cl.hasOption(consolelog), cl.hasOption(windowlog));
        }

        Log.Print("\nOptions used:");
        for (int idx = 0; idx < cl.getOptions().size(); idx++) {
            Log.Print(cl.getOptions().get(idx));
        }
        Log.Print("\n");
    }

    /**
     * Redirect derby log file
     */
    public static void setDerbyLog() {
        Properties p = System.getProperties();
        p.put("derby.stream.error.file", FileDirectoryHandler.getTempFile());
    }

    /**
     * 
     * @param lafname 
     */
    public static void setLaF(String lafname) {
        try {
            if (lafname != null) {
                UIManager.setLookAndFeel(lafname);
            } else {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            }
            LookAndFeelAddons.setAddon(LookAndFeelAddons.getBestMatchAddonClassName());
            if (MPView.identifierFrame != null && MPView.identifierFrame.isShowing()) {
                MPView.identifierFrame.setVisible(false);
                SwingUtilities.updateComponentTreeUI(MPView.identifierFrame);
                MPView.identifierFrame.setVisible(true);
            }
        } catch (Exception exe) {
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception ex) {
                Log.Debug(Main.class, ex.getMessage());
            }
            Log.Debug(Main.class, exe.getMessage());
        }
    }

    /**
     *
     */
    public static void printEnv() {
        Properties sysprops = System.getProperties();
        Enumeration propnames = sysprops.propertyNames();

        while (propnames.hasMoreElements()) {
            String propname = (String) propnames.nextElement();
            Log.Debug(Main.class, "System env: " + propname.toUpperCase() + " : " + System.getProperty(propname));
        }
    }

    /**
     *
     * @param firststart
     */
    public void go(boolean firststart) {
        setLaF(null);
        Main.splash.nextStep(Messages.INIT_LOGIN.toString());
        login();
        splash.nextStep(Messages.CACHE.toString());
        cache();
        Main.splash.nextStep(Messages.INIT_GUI.toString());
        super.show(new MPView(this));
        firstStart = firststart;
        if (Main.firstStart) {
            getApplication().getMainFrame().setSize(MPView.initialSize);
        }
        SwingUtilities.updateComponentTreeUI(MPView.identifierFrame);
        Main.splash.nextStep(Messages.INIT_PLUGINS.toString());
        loadPlugins();
        splash.dispose();
        if (START_SERVER) {
            MPServer serv = new MPServer();
            serv.start();
            MPView.identifierView.showServerStatus(serv.isAlive());
        } else {
            MPView.identifierView.showServerStatus(false);
        }
        
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        NoaConnection.getConnection();
                    } catch (Exception n) {
                        Log.Debug(n);
                    }
                }
            };
            new Thread(runnable).start();
    }

    private void loadPlugins() {
        if (!removeplugs) {
            try {
                MPPLuginLoader.queuePlugins(MPView.pluginLoader.getPlugins());
                MPView.pluginLoader.loadPlugins();
            } catch (Exception e) {
                Log.Debug(e);
            }
        } else {
            try {
                ArrayList data = DatabaseObject.getReferencedObjects(MPView.getUser(), Context.getPluginsToUsers());
                for (int i = 0; i < data.size(); i++) {
                    try {
                        ((UserPlugin) data.get(i)).delete();
                    } catch (Exception e) {
                        Log.Debug(e);
                    }
                }
            } catch (NodataFoundException ex) {
                Log.Debug(Main.class, ex.getMessage());
            }
        }
    }

    private void login() {
        if (!LocalSettings.getProperty("lastuser").equals("null") && !LocalSettings.getProperty("lastuserpw").equals("null")) {
            User usern1 = new User();
            Log.Debug(this, "Checking for auto login.. ");
            if (usern1.fetchDataOf(Integer.valueOf(LocalSettings.getProperty("lastuser")))) {
                Log.Debug(this, "Trying to login user: " + usern1);
                User user = mpv5.usermanagement.MPSecurityManager.checkAuthInternal(usern1, LocalSettings.getProperty("lastuserpw"));
                if (user != null) {
                    user.login();
                }
            }
        }
    }

    private boolean probeDatabaseConnection() {
        try {
            DatabaseConnection.instanceOf();
            return true;
        } catch (Exception ex) {
            Log.Debug(this, "Could not connect to database.");
//            Log.Debug(this, ex);
            return false;
        }
    }
    private static final String instanceIdentifier = ". Instance[";

    private boolean firstInstance() {
        try {
            FileReaderWriter x = new FileReaderWriter(lockfile);
            if (lockfile.exists()) {
                String[] xc = x.readLines();
                for (int i = 0; i < xc.length; i++) {
                    String line = xc[i];
                    try {
                        if (line.length() > 0 && line.substring(line.lastIndexOf(instanceIdentifier) + instanceIdentifier.length(), line.lastIndexOf("]")).equals(String.valueOf(LocalSettings.getConnectionID()))) {
                            Log.Debug(this, "Application already running.");
                            return false;
                        }
                    } catch (Exception e) {
                        Log.Debug(this, line);
                        Log.Debug(e);
                    }
                }
                return writeLockFile(x);
            } else {
                return writeLockFile(x);
            }
        } catch (Exception e) {
            Log.Debug(e);
            Log.Debug(this, "Application encountered some problem. Will try to continue anyway.");
            return true;
        }
    }

    private void showDbWiz(Integer forConnId) {
        try {
            Log.setLogLevel(Log.LOGLEVEL_DEBUG);
            LogConsole.setLogFile("install.log");
            Log.Debug(this, new Date());
        } catch (Exception ex) {
            mpv5.logging.Log.Debug(ex);//Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        Wizard w = new Wizard(true);
        w.addPanel(new wizard_DBSettings_1(w, forConnId));
        w.showWiz();
    }

    private boolean writeLockFile(FileReaderWriter x) {
        try {
            x.write0("Locked on " + new Date() + instanceIdentifier + LocalSettings.getConnectionID() + "]");
            Log.Debug(this, "Application will start now: " + lockfile);
            return true;
        } catch (Exception e) {
            Log.Debug(e);
            return false;
        }
    }

    private void clearLockFile() {
        FileReaderWriter x = new FileReaderWriter(lockfile);
        String[] lines = x.readLines();
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            if ((!line.contains(instanceIdentifier)) || (line.length() > 0 && line.substring(line.lastIndexOf(instanceIdentifier) + instanceIdentifier.length(), line.lastIndexOf("]")).equals(String.valueOf(LocalSettings.getConnectionID())))) {
                lines[i] = null;
            }
        }
        x.flush();
        x.write0(lines);
    }
}
