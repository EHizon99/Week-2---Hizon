package com.banking.db;

import org.h2.tools.Server;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Properties;


public class DatabaseConnection {
    private static Properties loadProperties() {
        Properties props = new Properties();
        try (InputStream input =
                     DatabaseConnection.class.getClassLoader()
                             .getResourceAsStream("application.properties")) {

            if (input == null) {
                throw new RuntimeException(
                        "Cannot find application.properties");
            }
            props.load(input);
            return props;
        } catch (IOException e) {
            throw new RuntimeException(
                    "Error loading properties", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        Properties props = loadProperties();
        return DriverManager.getConnection(
                props.getProperty("db.url"),
                props.getProperty("db.username"),
                props.getProperty("db.password")
        );


    }

    // Method to start the H2 Web Server
    public static void startH2Server() {
        try {
            Server webServer = Server.createWebServer("-web", "-webAllowOthers", "-webPort", "8082").start();
            System.out.println("✅ H2 Web Console started at: http://localhost:8082");
        } catch (SQLException e) {
            System.out.println("❌ Failed to start H2 Web Console: " + e.getMessage());
        }
    }

    public static void ExecuteSQL() {
        String url = "jdbc:h2:mem:bankingdb;MODE=MySQL;DB_CLOSE_DELAY=-1";
        String user = "sa";
        String password = "";
        String schemaFilePath = "src/main/resources/schema.sql";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {

            // Load schema.sql from the correct path
            Path path = Paths.get(schemaFilePath);
            if (!Files.exists(path)) {
                throw new FileNotFoundException("Error: schema.sql file not found at " + path.toAbsolutePath());
            }

            String sql = new String(Files.readAllBytes(path));
            stmt.execute(sql);
            System.out.println("✅Schema executed successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}



