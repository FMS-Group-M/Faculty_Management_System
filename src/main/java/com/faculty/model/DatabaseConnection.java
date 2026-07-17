package com.faculty.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3307/fms_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // Default XAMPP password is empty
    
    private DatabaseConnection() {}

    public static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        runMigrations(conn);
        return conn;
    }

    private static boolean migrationsRun = false;

    private static synchronized void runMigrations(Connection conn) {
        if (migrationsRun) return;
        try (java.sql.Statement stmt = conn.createStatement()) {
            try {
                stmt.execute("ALTER TABLE courses ADD COLUMN academic_year INT DEFAULT 1");
            } catch (SQLException e) { /* Ignore */ }
            try {
                stmt.execute("ALTER TABLE courses ADD COLUMN semester INT DEFAULT 1");
            } catch (SQLException e) { /* Ignore */ }
            try {
                stmt.execute("CREATE TABLE IF NOT EXISTS leaves (" +
                             "id INT AUTO_INCREMENT PRIMARY KEY," +
                             "lecturer_id INT," +
                             "leave_date DATE NOT NULL," +
                             "reason VARCHAR(255) NOT NULL," +
                             "status ENUM('Pending', 'Approved', 'Rejected') DEFAULT 'Pending'," +
                             "FOREIGN KEY (lecturer_id) REFERENCES lecturers(id) ON DELETE CASCADE" +
                             ")");
            } catch (SQLException e) { e.printStackTrace(); }
        } catch (Exception e) {
            e.printStackTrace();
        }
        migrationsRun = true;
    }
}