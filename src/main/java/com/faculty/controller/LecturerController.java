package com.faculty.controller;

import com.faculty.model.Lecturer;
import model.dao.LecturerDAO;
import java.util.List;

public class LecturerController {
    private final LecturerDAO lecturerDAO = new LecturerDAO();

    public List<Lecturer> getAllLecturers() {
        return lecturerDAO.getAllLecturers();
    }

    public Lecturer getLecturerByUserId(int userId) {
        return lecturerDAO.getLecturerByUserId(userId);
    }

    public void updateLecturerProfile(Lecturer lecturer) throws Exception {
        lecturerDAO.updateLecturer(lecturer);
    }

    public void addLecturerProfile(Lecturer lecturer) throws Exception {
        lecturerDAO.addLecturer(lecturer);
    }

    public void saveBatch(List<Lecturer> addedList, List<Lecturer> editedList, List<Lecturer> deletedList) throws Exception {
        for (Lecturer l : addedList) lecturerDAO.addLecturer(l);
        for (Lecturer l : editedList) lecturerDAO.updateLecturer(l);
        for (Lecturer l : deletedList) lecturerDAO.deleteLecturer(l.getId());
    }
}





