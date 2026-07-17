package com.faculty.controller;

import com.faculty.model.Student;
import model.dao.StudentDAO;
import java.util.List;

public class StudentController {
    private final StudentDAO studentDAO = new StudentDAO();

    public List<Student> getAllStudents() {
        return studentDAO.getAllStudents();
    }

    public Student getStudentByUserId(int userId) {
        return studentDAO.getStudentByUserId(userId);
    }

    public void updateStudentProfile(Student student) throws Exception {
        studentDAO.updateStudent(student);
    }

    public void addStudentProfile(Student student) throws Exception {
        studentDAO.addStudent(student);
    }

    public void saveBatch(List<Student> addedList, List<Student> editedList, List<Student> deletedList) throws Exception {
        for (Student s : addedList) studentDAO.addStudent(s);
        for (Student s : editedList) studentDAO.updateStudent(s);
        for (Student s : deletedList) studentDAO.deleteStudent(s.getStudentIdStr());
    }
}





