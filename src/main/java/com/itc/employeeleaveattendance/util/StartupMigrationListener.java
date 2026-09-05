package com.itc.employeeleaveattendance.util;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Runs `PasswordHashMigration` on application startup when the JVM property
 * `runPasswordMigration=true` is present. This allows running the migration
 * within the app's runtime without external tooling.
 */
public class StartupMigrationListener implements ServletContextListener {

    private static final Logger LOGGER = Logger.getLogger(StartupMigrationListener.class.getName());

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        String run = System.getProperty("runPasswordMigration");
        if ("true".equalsIgnoreCase(run)) {
            LOGGER.info("runPasswordMigration=true detected — running PasswordHashMigration...");
            try {
                PasswordHashMigration.main(new String[]{});
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "PasswordHashMigration failed", e);
            }
        } else {
            LOGGER.info("runPasswordMigration not set — skipping password migration.");
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // no-op
    }
}
