package com.gamecollection;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class Database {
    private static final Properties PROPERTIES = new Properties();

    static {
        try (InputStream input = Database.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (input == null) {
                throw new IllegalStateException("Could not find db.properties in src/main/resources.");
            }
            PROPERTIES.load(input);
        } catch (IOException e) {
            throw new ExceptionInInitializerError("Unable to load database settings: " + e.getMessage());
        }
    }

    private Database() {}

    public static Connection getConnection() throws SQLException {
        String url = System.getProperty("db.url", PROPERTIES.getProperty("db.url"));
        String username = System.getProperty("db.username", PROPERTIES.getProperty("db.username"));
        String password = System.getProperty("db.password", PROPERTIES.getProperty("db.password"));
        if (password == null || password.isBlank() || password.equals("CHANGE_ME")) {
            throw new SQLException("Set your MySQL password in src/main/resources/db.properties first.");
        }
        return DriverManager.getConnection(url, username, password);
    }

    public static void testConnection() throws SQLException {
        try (Connection ignored = getConnection()) {
            // Opening the connection is enough to test connectivity.
        }
    }
}
