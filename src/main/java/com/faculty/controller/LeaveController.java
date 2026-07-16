package com.faculty.controller;

import com.faculty.model.Leave;
import model.dao.LeaveDAO;
import java.util.List;

public class LeaveController {
    private final LeaveDAO leaveDAO = new LeaveDAO();

    public void requestLeave(Leave leave) throws Exception {
        leaveDAO.requestLeave(leave);
    }
    
    public List<Leave> getLeavesByLecturer(int lecturerId) {
        return leaveDAO.getLeavesByLecturer(lecturerId);
    }

    public void saveBatch(List<Leave> addedList, List<Leave> editedList, List<Leave> deletedList) throws Exception {
        for (Leave l : addedList) leaveDAO.requestLeave(l);
        for (Leave l : editedList) leaveDAO.updateLeave(l);
        for (Leave l : deletedList) leaveDAO.deleteLeave(l.getId());
    }
}





