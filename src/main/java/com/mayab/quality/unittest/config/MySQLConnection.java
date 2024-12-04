package com.mayab.quality.unittest.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import io.github.cdimascio.dotenv.Dotenv;

public class MySQLConnection {

    private Connection connection;
    Dotenv dotenv = Dotenv.load();

    String databaseUrl = dotenv.get("DATABASE_MYSQL_URL");
    String username = dotenv.get("DATABASE_MYSQL_USER");
    String password = dotenv.get("DATABASE_MYSQL_PASSWORD");

    public MySQLConnection() {

    }

    public Connection connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("MySQL JDBC Driver loaded successfully.");
        } catch (ClassNotFoundException e) {
            System.out.println("Failed to load MySQL JDBC Driver.");
            e.printStackTrace();
            return null;
        }

        try {

            String dbURL = databaseUrl;
            String strUserID = username;
            String strPassword = password;

            this.connection = DriverManager.getConnection(dbURL, strUserID, strPassword);
            System.out.println("Connected to MySQL database successfully.");

        } catch (SQLException e) {
            System.out.println("Failed to connect to MySQL database.");
            e.printStackTrace();
            this.connection = null;
        }

        return this.connection;
    }

}
