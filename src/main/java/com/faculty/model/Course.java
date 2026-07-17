package com.faculty.model;

public class Course {
    private int id;
    private String courseCode;
    private String courseName;
    private int credits;
    private int lecturerId;
    private int academicYear = 1;
    private int semester = 1;
    private int degreeId;

    // Display fields
    private String lecturerName;
    private String degreeName;
    private String grade;

    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }

    public Course(int id, String courseCode, String courseName, int credits, int lecturerId, int academicYear, int semester, int degreeId) {
        this.id = id;
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.credits = credits;
        this.lecturerId = lecturerId;
        this.academicYear = academicYear;
        this.semester = semester;
        this.degreeId = degreeId;
    }

    // Constructor without degreeId for backward compatibility
    public Course(int id, String courseCode, String courseName, int credits, int lecturerId, int academicYear, int semester) {
        this(id, courseCode, courseName, credits, lecturerId, academicYear, semester, 0);
    }
    
    // Legacy constructor
    public Course(int id, String courseCode, String courseName, int credits, int lecturerId) {
        this(id, courseCode, courseName, credits, lecturerId, 1, 1);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }
    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }
    public int getCredits() { return credits; }
    public void setCredits(int credits) { this.credits = credits; }
    public int getLecturerId() { return lecturerId; }
    public void setLecturerId(int lecturerId) { this.lecturerId = lecturerId; }
    
    public String getLecturerName() { return lecturerName; }
    public void setLecturerName(String lecturerName) { this.lecturerName = lecturerName; }
    
    public int getAcademicYear() { return academicYear; }
    public void setAcademicYear(int academicYear) { this.academicYear = academicYear; }
    public int getSemester() { return semester; }
    public void setSemester(int semester) { this.semester = semester; }

    public int getDegreeId() { return degreeId; }
    public void setDegreeId(int degreeId) { this.degreeId = degreeId; }

    public String getDegreeName() { return degreeName; }
    public void setDegreeName(String degreeName) { this.degreeName = degreeName; }
}
