package com.pawth.config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
    private static final Properties CONFIG = loadConfig();

    private static Properties loadConfig() {
        Path path = Path.of("config", "database.properties");
        Properties properties = new Properties();
        try (InputStream input = Files.newInputStream(path)) {
            properties.load(input);
        } catch (IOException e) {
            throw new IllegalStateException(
                    "Cannot read "
                            + path.toAbsolutePath()
                            + ". Run PAWTH with the project folder as its working directory.",
                    e);
        }
        return properties;
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                CONFIG.getProperty("url"),
                CONFIG.getProperty("user"),
                CONFIG.getProperty("password"));
    }
}
