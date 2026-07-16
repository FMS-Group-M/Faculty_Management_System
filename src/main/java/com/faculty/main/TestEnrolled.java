package com.faculty.main;

import java.util.List;
import com.faculty.model.Course;
import model.dao.CourseDAO;
import model.dao.StudentDAO;

public class TestEnrolled {
    public static void main(String[] args) {
        CourseDAO dao = new CourseDAO();
        // Lakindu's user_id is 14 (or whatever it is).
        // Let's get his user ID first
        int userId = -1;
        try (java.sql.Connection conn = com.faculty.model.DatabaseConnection.getConnection();
             java.sql.PreparedStatement pstmt = conn.prepareStatement("SELECT id FROM users WHERE username = 'Lakindu'")) {
            try (java.sql.ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    userId = rs.getInt("id");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Lakindu User ID: " + userId);
        
        List<Course> courses = dao.getCoursesByStudentUserId(userId);
        System.out.println("Total courses enrolled: " + courses.size());
        for (Course c : courses) {
            System.out.println(c.getCourseCode() + " Year: " + c.getAcademicYear() + " Sem: " + c.getSemester());
        }
    }
}
