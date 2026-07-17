package com.faculty.model;

import java.sql.Date;

public class Leave {
    private int id;
    private int lecturerId;
    private Date leaveDate;
    private String reason;
    private String status;

    public Leave(int id, int lecturerId, Date leaveDate, String reason, String status) {
        this.id = id;
        this.lecturerId = lecturerId;
        this.leaveDate = leaveDate;
        this.reason = reason;
        this.status = status;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getLecturerId() { return lecturerId; }
    public void setLecturerId(int lecturerId) { this.lecturerId = lecturerId; }
    
    public Date getLeaveDate() { return leaveDate; }
    public void setLeaveDate(Date leaveDate) { this.leaveDate = leaveDate; }
    
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}





