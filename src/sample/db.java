package sample;

import java.sql.*;
/**
 * Created by Test on 24/07/2017.
 */
public class db {

    Connection conn = null;

    public static Connection ConnectDB() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite::resource:pvcontrol.db");
            System.out.println("Connexion OK");

            return conn;


        } catch (ClassNotFoundException notFoundException){
            System.out.println("Driver absent" + notFoundException);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
