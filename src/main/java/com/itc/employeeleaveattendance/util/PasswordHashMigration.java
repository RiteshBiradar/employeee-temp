package com.itc.employeeleaveattendance.util;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Simple migration tool to hash existing plaintext passwords in the employees table.
 * Run this from the project root using the application DB config.
 * WARNING: This updates the database in-place. Backup your DB before running.
 */
public class PasswordHashMigration {

    private static final Logger LOGGER = Logger.getLogger(PasswordHashMigration.class.getName());

    public static void main(String[] args) {
        LOGGER.info("Starting password hashing migration...");
        try (Connection conn = DBUtil.getConnection()) {
            conn.setAutoCommit(false);
            String selectSql = "SELECT emp_id, password FROM employees";
            try (PreparedStatement ps = conn.prepareStatement(selectSql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
                 ResultSet rs = ps.executeQuery()) {
                int updated = 0;
                while (rs.next()) {
                    String pwd = rs.getString("password");
                    if (pwd == null || pwd.isBlank()) continue;
                    // Skip already-bcrypt hashed values
                    if (pwd.startsWith("$2a$") || pwd.startsWith("$2b$") || pwd.startsWith("$2y$")) continue;
                    String hash = BCrypt.hashpw(pwd, BCrypt.gensalt());
                    rs.updateString("password", hash);
                    rs.updateRow();
                    updated++;
                }
                conn.commit();
                LOGGER.info("Password hashing migration completed. Updated rows: " + updated);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error during migration", e);
        }
    }
}
