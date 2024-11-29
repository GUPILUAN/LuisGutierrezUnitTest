package com.mayab.quality.unittest.config;

import java.sql.*;

import io.github.cdimascio.dotenv.Dotenv;

public class OracleConnection {

    private String host;
    private Connection connection;

    Dotenv dotenv = Dotenv.load();

    String databaseUrl = dotenv.get("DATABASE_ORACLE_URL");
    String username = dotenv.get("DATABASE_ORACLE_USER");
    String password = dotenv.get("DATABASE_ORACLE_PASSWORD");

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
