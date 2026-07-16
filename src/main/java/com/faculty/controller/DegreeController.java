package com.faculty.controller;

import com.faculty.model.Degree;
import model.dao.DegreeDAO;
import java.util.List;

public class DegreeController {
    private final DegreeDAO degreeDAO = new DegreeDAO();

    public List<Degree> getAllDegrees() {
        return degreeDAO.getAllDegrees();
    }

    public void saveBatch(List<Degree> addedList, List<Degree> editedList, List<Degree> deletedList) throws Exception {
        for (Degree d : addedList) degreeDAO.addDegree(d);
        for (Degree d : editedList) degreeDAO.updateDegree(d);
        for (Degree d : deletedList) degreeDAO.deleteDegree(d.getId());
    }
}





