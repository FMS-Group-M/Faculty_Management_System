package com.faculty.main;

import com.faculty.model.DatabaseConnection;
import java.sql.Connection;
import java.sql.Statement;

public class UpdateDB {
    public static void main(String[] args) {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            try {
                stmt.executeUpdate("ALTER TABLE courses ADD COLUMN academic_year INT DEFAULT 1");
                System.out.println("Added academic_year column.");
            } catch (Exception e) {
                System.out.println("academic_year may already exist.");
            }
            try {
                stmt.executeUpdate("ALTER TABLE courses ADD COLUMN semester INT DEFAULT 1");
                System.out.println("Added semester column.");
            } catch (Exception e) {
                System.out.println("semester may already exist.");
            }
            try {
                stmt.executeUpdate("ALTER TABLE courses ADD COLUMN degree_id INT");
                stmt.executeUpdate("ALTER TABLE courses ADD CONSTRAINT fk_courses_degree FOREIGN KEY (degree_id) REFERENCES degrees(id) ON DELETE SET NULL ON UPDATE CASCADE");
                System.out.println("Added degree_id column and foreign key.");
            } catch (Exception e) {
                System.out.println("degree_id may already exist or foreign key failed: " + e.getMessage());
            }
            System.out.println("Database update complete.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}





