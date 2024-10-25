package com.mayab.quality.unittest;

/**
 * Hello world!
 */
import java.sql.*;

class App {
    public static void main(String[] args) {

        String host;
        if (args.length == 0) {
            host = "localhost";
            System.out.println(
                    "You didn't provide the database host as a command line argument. Trying with " + host + "...");
        } else {
            host = args[0];
        }

        String dbURL = "jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS=(PROTOCOL=TCP)(HOST=" + host
                + ")(PORT=1521))(CONNECT_DATA=(SERVER = DEDICATED)(SERVICE_NAME=FREEPDB1)))";
        String strUserID = "SYSTEM";
        String strPassword = "1234567890";

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            System.out.println("Oracle JDBC Driver loaded successfully.");
        } catch (ClassNotFoundException e) {
            System.out.println("Failed to load Oracle JDBC Driver.");
            e.printStackTrace();
            return;
        }

        try (Connection con = DriverManager.getConnection(dbURL, strUserID, strPassword);
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT 1 AS result FROM DUAL")) {

            System.out.println("Connected to the database.");

            while (rs.next()) {
                System.out.println(rs.getInt("result"));
            }

        } catch (Exception e) {
            System.out.println("An error occurred while connecting to the database or executing the query.");
            e.printStackTrace();
        }
    }
}
