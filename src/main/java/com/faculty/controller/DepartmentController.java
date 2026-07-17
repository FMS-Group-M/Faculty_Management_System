package com.faculty.controller;

import com.faculty.model.Department;
import model.dao.DepartmentDAO;
import java.util.List;

public class DepartmentController {
    private final DepartmentDAO departmentDAO = new DepartmentDAO();

    public List<Department> getAllDepartments() {
        return departmentDAO.getAllDepartments();
    }

    public void saveBatch(List<Department> addedList, List<Department> editedList, List<Department> deletedList) throws Exception {
        for (Department d : addedList) departmentDAO.addDepartment(d);
        for (Department d : editedList) departmentDAO.updateDepartment(d);
        for (Department d : deletedList) departmentDAO.deleteDepartment(d.getId());
    }
}

