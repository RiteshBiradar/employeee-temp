package com.itc.employeeleaveattendance.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Database utility class for obtaining JDBC connections.
 * Reads configuration from application.properties.
 */
public final class DBUtil {

    private static final Logger LOGGER = Logger.getLogger(DBUtil.class.getName());

    private static String url;
    private static String username;
    private static String password;

    static {
        try {
            // Load Oracle JDBC driver
            Class.forName("oracle.jdbc.OracleDriver");

            // Load properties
            Properties props = new Properties();
            try (InputStream is = DBUtil.class.getClassLoader()
                    .getResourceAsStream("application.properties")) {
                if (is == null) {
                    throw new RuntimeException("application.properties not found in classpath");
                }
                props.load(is);
            }

            url = props.getProperty("db.url");
            username = props.getProperty("db.username");
            password = props.getProperty("db.password");

            if (url == null || username == null || password == null) {
                throw new RuntimeException("Database configuration is incomplete in application.properties");
            }

            LOGGER.info("Database configuration loaded successfully.");
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Oracle JDBC driver not found", e);
            throw new RuntimeException("Oracle JDBC driver not found. Ensure ojdbc11.jar is in the classpath.", e);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to load application.properties", e);
            throw new RuntimeException("Failed to load database configuration.", e);
        }
    }

    private DBUtil() {
    }

    /**
     * Returns a new database connection.
     * Caller is responsible for closing the connection.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }
}
