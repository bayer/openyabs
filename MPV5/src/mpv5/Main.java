/*
 * Main.java
 */
package mpv5;

import java.util.logging.Level;
import java.util.logging.Logger;
import mpv5.ui.frames.MPV5View;
import mpv5.logging.*;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

import com.l2fprod.common.swing.plaf.LookAndFeelAddons;
import java.io.File;
import java.io.FileWriter;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import mpv5.db.common.ConnectionTypeHandler;
import mpv5.db.common.DatabaseConnection;
import mpv5.globals.Constants;
import mpv5.globals.LocalSettings;
import mpv5.globals.Messages;
import mpv5.ui.dialogs.Popup;
import mpv5.ui.dialogs.Wizard;
import mpv5.ui.dialogs.subcomponents.wizard_DBSettings_1;


import mpv5.usermanagement.User;
import mpv5.utils.files.FileDirectoryHandler;
import org.apache.commons.cli2.*;
import org.apache.commons.cli2.builder.*;
import org.apache.commons.cli2.commandline.Parser;
import org.apache.commons.cli2.util.*;

/**
 * The main class of the application.
 */
public class Main extends SingleFrameApplication {

    private File lockfile = new File(MPPATH + File.separator + "." + Constants.PROG_NAME + Constants.VERSION + "." + "lck");

    /**
     *
     */
    public static void start() {

        try {
            LocalSettings.read();
            LocalSettings.apply();
        } catch (Exception ex) {
            Log.Debug(Main.class, ex.getMessage());
            Log.Debug(Main.class, "Local settings file not readable: " + LocalSettings.getLocalFile());
        }
        launch(Main.class, new String[]{});
    }

    /**
     * At startup create and show the main frame of the application.
     */
    @Override
    protected void startup() {
        if (!firstInstance()) {
            System.exit(1);
        }

        getContext().getLocalStorage().setDirectory(new File(Main.MPPATH));

        if (probeDatabaseConnection()) {
            go();
        } else if (Popup.Y_N_dialog(Messages.NO_DB_CONNECTION)) {
            showDbWiz();
        } else {
            System.exit(1);
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
        LocalSettings.save();
        try {
            if (!MPV5View.getUser().isDefault()) {
                MPV5View.getUser().logout();
            }
        } catch (Exception e) {
        }
        super.shutdown();
    }

    /**
     * Main method launching the application.
     * @param args
     */
    public static void main(String[] args) {

        System.out.print(Messages.START_MESSAGE);

        getOS();
        setEnv();
        parseArgs(args);
        setDerbyLog();
        start();
    }
    public static boolean IS_WINDOWS = false;
    public static String MPPATH = null;
    public static String SETTINGS_FILE = null;
    public static String APP_DIR = null;
    public static String USER_HOME = null;
    public static String DESKTOP = null;

    private static void getOS() {
        if (System.getProperty("os.name").contains("Windows")) {
            IS_WINDOWS = true;
        } else {
            IS_WINDOWS = false;
        }
    }

    public static void setEnv() {

        if (IS_WINDOWS) {

            USER_HOME = System.getenv("USERPROFILE");
            DESKTOP = USER_HOME + File.separator + "Desktop";
            MPPATH = USER_HOME + File.separator + ".mp";
            SETTINGS_FILE = Main.MPPATH + File.separator + "settings" + Constants.RELEASE_VERSION + ".mp";
            APP_DIR = USER_HOME + File.separator + Constants.PROG_NAME;

        } else {

            USER_HOME = System.getProperty("user.home");
            DESKTOP = USER_HOME + File.separator + "Desktop";
            MPPATH = USER_HOME + File.separator + ".mp";
            SETTINGS_FILE = Main.MPPATH + File.separator + "settings" + Constants.RELEASE_VERSION + ".mp";
            APP_DIR = USER_HOME + File.separator + Constants.PROG_NAME;

        }

    }

    private static void parseArgs(String[] args) {

        DefaultOptionBuilder obuilder = new DefaultOptionBuilder();
        ArgumentBuilder abuilder = new ArgumentBuilder();
        GroupBuilder gbuilder = new GroupBuilder();

        Argument option = abuilder.withName("=option").withMinimum(1).withMaximum(1).create();
        Argument filearg = abuilder.withName("=file").withMinimum(1).withMaximum(1).create();
        Argument dirarg = abuilder.withName("=directory").withMinimum(1).withMaximum(1).create();

        Option help = obuilder.withShortName("help").withShortName("h").withDescription("print this message").create();
        Option license = obuilder.withShortName("license").withShortName("li").withDescription("print license").create();
        Option version = obuilder.withShortName("version").withDescription("print the version information and exit").create();
        Option verbose = obuilder.withShortName("verbose").withDescription("be extra verbose").create();
        Option nolf = obuilder.withShortName("nolf").withDescription("use system L&F instead of Tiny L&F").create();
        Option dbtype = obuilder.withShortName("dbdriver").withShortName("r").withDescription("DB Driver: derby (default), mysql, custom").withArgument(option).create();
        Option debug = obuilder.withShortName("debug").withDescription("debug logging").create();
        Option logfile = obuilder.withShortName("logfile").withShortName("l").withDescription("use file for log").withArgument(filearg).create();
        Group options = gbuilder.withName("options").
                withOption(help).
                withOption(version).
                withOption(verbose).
                withOption(debug).
                withOption(license).
                withOption(nolf).
                withOption(logfile).create();

        HelpFormatter hf = new HelpFormatter();
        Parser p = new Parser();
        p.setGroup(options);
        p.setHelpFormatter(hf);
        CommandLine cl = p.parseAndHelp(args);
        if (cl == null) {
            System.err.println("Cannot parse arguments");
        }
        if (cl.hasOption(help)) {
            hf.print();
            System.exit(0);
        }

        if (cl.hasOption(license)) {
//            System.out.print(Messages.GPL);
        }

        if (cl.hasOption(version)) {
            System.out.println("MP Version: " + Constants.VERSION);
            System.exit(0);
        }
        if (cl.hasOption(verbose)) {
            Log.setLogLevel(Log.LOGLEVEL_HIGH);

        }

        if (cl.hasOption(debug)) {
            Log.setLogLevel(Log.LOGLEVEL_DEBUG);

        }

        if (cl.hasOption(logfile)) {
            try {
                LogConsole.setLogFile(((String) cl.getValue(logfile)).split("=")[1]);
            } catch (Exception e) {
                Log.Debug(Main.class, "Fehler beim Schreiben der Logdatei: " + e.getMessage(), true);
            }
        }

        if (cl.hasOption(dbtype)) {
            if (((String) cl.getValue(dbtype)).toLowerCase().endsWith("derby")) {
                LocalSettings.setProperty(LocalSettings.DBDRIVER, ConnectionTypeHandler.DERBY_DRIVER);
            } else if (((String) cl.getValue(dbtype)).toLowerCase().endsWith("mysql")) {
                LocalSettings.setProperty(LocalSettings.DBDRIVER, ConnectionTypeHandler.MYSQL_DRIVER);
            } else if (((String) cl.getValue(dbtype)).toLowerCase().endsWith("custom")) {
                LocalSettings.setProperty(LocalSettings.DBDRIVER, ConnectionTypeHandler.CUSTOM_DRIVER);
            }
        }

        if (cl.hasOption(nolf)) {
            setLaF(null);
        }
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
            if (MPV5View.identifierFrame != null && MPV5View.identifierFrame.isShowing()) {
                MPV5View.identifierFrame.setVisible(false);
                SwingUtilities.updateComponentTreeUI(MPV5View.identifierFrame);
                MPV5View.identifierFrame.setVisible(true);
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

    public static void printEnv() {
        Map<String, String> env = System.getenv();
        for (String envName : env.keySet()) {
            System.out.format("%s=%s%n", envName, env.get(envName));
        }
    }

    public void go() {
        setLaF(null);
        login();
        super.show(new MPV5View(this));
        SwingUtilities.updateComponentTreeUI(MPV5View.identifierFrame);
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

    private boolean firstInstance() {
        try {
            if (lockfile.exists()) {
                Log.Debug(this, "Application already running.");
                return false;
            } else {
                FileWriter x = new FileWriter(lockfile);
                x.write("Locked on " + new Date() + ". Instance[0]");
                x.close();
                lockfile.deleteOnExit();
                Log.Debug(this, "Application will start now.");
                return true;
            }
        } catch (Exception e) {
            Log.Debug(this, "Application encountered some problem. Will try to continue anyway.");
            return true;
        }

    }

    private void showDbWiz() {
        try {
            Log.setLogLevel(Log.LOGLEVEL_DEBUG);
            LogConsole.setLogFile("install.log");
            Log.Debug(this, new Date());
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        Wizard w = new Wizard(true);
        w.addPanel(new wizard_DBSettings_1(w));
        w.addPanel(new wizard_DBSettings_1(w));
        w.showWiz();
    }
}
