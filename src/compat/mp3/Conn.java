/*
 *  This file is part of MP by anti43 /GPL.
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
package compat.mp3;


import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import mp4.logs.Log;
import mp4.panels.misc.SplashScreen;




/**
 *
 * @author anti43
 */
public class Conn implements Strings {

    private static Conn connector;




    /**
     * 
     * @param url 
     * @param create 
     * @return Database connector
     */
    public static Conn instanceOf(String url) throws Exception {
        if (connector == null) {
            connector = new Conn(url);
        }
        return connector;

    }
    private static Statement statement;
    private static java.sql.Connection conn;
    /**
     * Verbindung zur Datenbank
     */
    /**
     * JDBC-Treiber-Name. Muss im Klassenpfad sein.
     */
    private static String DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
    /**
     * Verbindungs-URL. Erstellt beim ersten Aufruf eine neue Datenbank.
     */
    private static String URL = "";
    private static boolean tablesCreated = false;
    private SplashScreen splash;



    /**
     * 
     */
    private Conn(String url) throws Exception {

        try {

            URL = "jdbc:derby:" + url;

        } catch (Exception exception) {
            Popup.notice(SETTINGS_NOT_FOUND + exception.getMessage());
        }



        this.connect();

    }

    /**
     * Verbindung zur Datenbank herstellen. 
     * @return Connection
     */
    private Connection connect() throws Exception {



        // Treiber laden
        try {
            Class.forName(DRIVER).newInstance();
        } catch (Exception ex) {
            ex.printStackTrace();

            Popup.warn(ex.getMessage(), Popup.ERROR);
            Conn.shutdown();
        }

        // Verbindung herstellen
        try {
            conn = DriverManager.getConnection(URL);
            // Ben�tige Ressourcen f�r eine SQL-Anweisung bereitstellen 
            statement = conn.createStatement();
        } catch (SQLException ex) {
            System.out.println("Database Error:" + ex.getMessage());
//            ex.printStackTrace();
            
            Popup.warn(ex.getMessage(), Popup.ERROR);
            
            Conn.shutdown();
//            System.exit(1);
            Log.Debug(ex);

            throw new Exception("Datenbank konnte nicht gestartet werden.");

        }
        return conn;
    }

    /**
     * Verbindung trennen
     */
    public static void shutdown() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                conn = null;

            }


            File f = new File(Constants.DATABASEPATH + File.separator + Constants.DATABASENAME + File.separator + "dbex.lck");
            f.deleteOnExit();
            File fi = new File(Constants.DATABASEPATH + File.separator + Constants.DATABASENAME + File.separator + "db.lck");
            fi.deleteOnExit();
        } catch (SQLException ex) {

            ex.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Query ausf�hren
     * @param query 
     * 
     */
    public static void query(String query) {

        String message = "Database Error:";
        statement = null;
        ResultSet resultSet = null;

        try {

            statement = conn.createStatement();
            statement.execute(query);

            System.out.println(statement.getUpdateCount());


        } catch (SQLException ex) {
            System.out.println(query);
            System.out.println(message + ex.getMessage());
        } finally {

            // Alle Ressourcen wieder freigeben
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException ex) {
                    System.out.println(message + ex.getMessage());
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    System.out.println(message + ex.getMessage());
                }
            }
        }
    }

    /**
     * Alle Tabellennamen anzeigen
     * @param table 
     * 
     */
    public static void showTable(String table) {
        String query = "select * from " + table;
        String message = "Database error:";
        statement = null;
        ResultSet resultSet = null;

        try {
            // Select-Anweisung ausf�hren
            statement = conn.createStatement();
            resultSet = statement.executeQuery(query);
            // Alle Tabllennamen anzeigen
            while (resultSet.next()) {
                System.out.println(resultSet.getString(1));
            }
        } catch (SQLException ex) {
            System.out.println(message + ex.getMessage());
        } finally {

            // Alle Ressourcen wieder freigeben
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException ex) {
                    System.out.println(message + ex.getMessage());
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    System.out.println(message + ex.getMessage());
                }
            }
        }
    }

//    @Override
//    public void finalize(){
//        
//        Out.out("Closing connection..");
////        try {
////            conn.close();
////        } catch (SQLException ex) {
////            Logger.getLogger(Conn.class.getName()).log(Level.SEVERE, null, ex);
////        }
//    }
    public static Connection getConnection(String url) throws Exception {
        if (connector == null) {
            connector = new Conn(url);
        }
        return connector.connect();
    }

    private boolean query(String[] querys) {
        String message = "Database error:";
        statement = null;
        ResultSet resultSet = null;

        try {

            statement = conn.createStatement();
            for (int i = 0; i < querys.length; i++) {

                statement.execute(querys[i]);

            }
        } catch (SQLException ex) {
            System.out.println(message + ex.getMessage());
            return false;
        } finally {

            // Alle Ressourcen wieder freigeben
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException ex) {
                    System.out.println(message + ex.getMessage());
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    System.out.println(message + ex.getMessage());
                }
            }
        }
        return true;
    }

    public static boolean isTablesCreated() {
        return tablesCreated;
    }

    public void setSplash(SplashScreen splash) {
        this.splash = splash;
    }
}
