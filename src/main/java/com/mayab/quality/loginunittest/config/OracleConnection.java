package com.mayab.quality.loginunittest.config;

import java.sql.*;

public class OracleConnection {

    private String host;
    private Connection connection;

    public OracleConnection(String[] args) {
        this.host = args.length == 0 ? "localhost" : args[0];
        if (args.length == 0)
            System.out.println(
                    "You didn't provide the database host as a command line argument. Trying with " + this.host
                            + "...");
    }

    public Connection connect() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            System.out.println("Oracle JDBC Driver loaded successfully.");
        } catch (ClassNotFoundException e) {
            System.out.println("Failed to load Oracle JDBC Driver.");
            e.printStackTrace();
            return null;
        }
        try {
            String dbURL = "jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS=(PROTOCOL=TCP)(HOST=" + this.host
                    + ")(PORT=1521))(CONNECT_DATA=(SERVER = DEDICATED)(SERVICE_NAME=FREEPDB1)))";
            String strUserID = "SYSTEM";
            String strPassword = "1234567890";
            this.connection = DriverManager.getConnection(dbURL, strUserID, strPassword);

        } catch (SQLException e) {
            System.out.println("Failed to connect to Oracle database.");
            e.printStackTrace();
            this.connection = null;
        }
        return this.connection;
    }
}
