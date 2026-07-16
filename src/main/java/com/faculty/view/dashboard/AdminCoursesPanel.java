package com.faculty.view.dashboard;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;

import com.faculty.controller.CourseController;
import com.faculty.model.Course;
import com.faculty.view.components.CardPanel;
import com.faculty.view.components.GradientPanel;
import com.faculty.view.components.ModernIcon;
import com.faculty.view.components.RoundedButton;
import com.faculty.view.components.StyledTable;

public class AdminCoursesPanel extends JPanel {
    private StyledTable table;
    private DefaultTableModel tableModel;
    private CourseController courseController;

    private List<Course> allCourses;
    private List<Course> addedList = new ArrayList<>();
    private List<Course> editedList = new ArrayList<>();
    private List<Course> deletedList = new ArrayList<>();

    public AdminCoursesPanel() {
        courseController = new CourseController();
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(245, 247, 250));
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Top Banner
        GradientPanel banner = new GradientPanel(new Color(20, 61, 89), new Color(20, 61, 89), false, 20);
        banner.setLayout(new BorderLayout());
        banner.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        JLabel title = new JLabel("Manage Courses");
        title.setFont(new Font("Montserrat", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        
        JLabel subtitle = new JLabel("View and manage all faculty courses, credits, and assigned lecturers.");
        subtitle.setFont(new Font("Montserrat", Font.PLAIN, 14));
        subtitle.setForeground(new Color(220, 235, 255));
        
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);
        titlePanel.add(title);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 5)));
        titlePanel.add(subtitle);
        
        banner.add(titlePanel, BorderLayout.WEST);
        
        JPanel bannerWrapper = new JPanel(new BorderLayout());
        bannerWrapper.setOpaque(false);
        bannerWrapper.add(banner, BorderLayout.CENTER);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));
        
        RoundedButton btnAdd = new RoundedButton("Add Course", 20, new Color(20, 61, 89), Color.WHITE);
        btnAdd.setIcon(new ModernIcon(ModernIcon.IconType.ADD, 16, Color.WHITE));
        btnAdd.setIconTextGap(8);
        
        RoundedButton btnEdit = new RoundedButton("Edit", 20, new Color(230, 240, 250), new Color(0, 100, 200));
        btnEdit.setIcon(new ModernIcon(ModernIcon.IconType.EDIT, 16, new Color(0, 100, 200)));
        btnEdit.setIconTextGap(8);
        
        RoundedButton btnDelete = new RoundedButton("Delete", 20, new Color(255, 230, 230), new Color(220, 50, 50));
        btnDelete.setIcon(new ModernIcon(ModernIcon.IconType.DELETE, 16, new Color(220, 50, 50)));
        btnDelete.setIconTextGap(8);
        
        btnAdd.setPreferredSize(new Dimension(140, 40));
        btnEdit.setPreferredSize(new Dimension(100, 40));
        btnDelete.setPreferredSize(new Dimension(110, 40));
        
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnAdd);
        
        // Main Content Area
        JPanel mainContent = new JPanel(new BorderLayout(0, 10));
        mainContent.setOpaque(false);
        mainContent.add(buttonPanel, BorderLayout.NORTH);
        
        String[] columns = {"Course code", "Course name", "Credits", "Lecturer Name", "Degree", "Year", "Semester"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new com.faculty.view.components.StyledTable();
        table.setModel(tableModel);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        CardPanel tableContainer = new CardPanel(20, Color.WHITE);
        tableContainer.setLayout(new BorderLayout());
        tableContainer.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        tableContainer.add(scrollPane, BorderLayout.CENTER);
        
        mainContent.add(tableContainer, BorderLayout.CENTER);
        
        // Save Button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 10));
        bottomPanel.setOpaque(false);
        RoundedButton btnSave = new RoundedButton("Save changes", 20, new Color(20, 61, 89), Color.WHITE);
        btnSave.setIcon(new ModernIcon(ModernIcon.IconType.SAVE, 18, Color.WHITE));
        btnSave.setIconTextGap(10);
        btnSave.setPreferredSize(new Dimension(200, 45));
        bottomPanel.add(btnSave);
        mainContent.add(bottomPanel, BorderLayout.SOUTH);
        
        add(bannerWrapper, BorderLayout.NORTH);
        add(mainContent, BorderLayout.CENTER);
        
        // ACTION LISTENERS
        btnAdd.addActionListener(e -> showAddEditDialog(null, -1));
        
        btnEdit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Please select a course to edit.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String courseCode = (String) tableModel.getValueAt(row, 0);
            Course c = getCourseById(courseCode);
            if (c != null) showAddEditDialog(c, row);
        });
        
        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Please select a course to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String courseCode = (String) tableModel.getValueAt(row, 0);
            Course c = getCourseById(courseCode);
            if (c != null) {
                if (addedList.contains(c)) {
                    addedList.remove(c);
                } else {
                    deletedList.add(c);
                    editedList.remove(c);
                }
                allCourses.remove(c);
            }
            tableModel.removeRow(row);
        });
        
        btnSave.addActionListener(e -> {
            try {
                courseController.saveBatch(addedList, editedList, deletedList);
                JOptionPane.showMessageDialog(this, "Changes saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                addedList.clear();
                editedList.clear();
                deletedList.clear();
                loadData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error saving changes: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        loadData();
    }
    
    private void showAddEditDialog(Course course, int row) {
        com.faculty.view.components.RoundedTextField txtCourseCode = new com.faculty.view.components.RoundedTextField(15, 20);
        txtCourseCode.setText(course != null ? course.getCourseCode() : "");
        com.faculty.view.components.RoundedTextField txtCourseName = new com.faculty.view.components.RoundedTextField(15, 20);
        txtCourseName.setText(course != null ? course.getCourseName() : "");
        com.faculty.view.components.RoundedTextField txtCredits = new com.faculty.view.components.RoundedTextField(15, 20);
        txtCredits.setText(course != null ? String.valueOf(course.getCredits()) : "");
        com.faculty.view.components.RoundedTextField txtLecturerName = new com.faculty.view.components.RoundedTextField(15, 20);
        txtLecturerName.setText(course != null && course.getLecturerName() != null ? course.getLecturerName() : "");
        com.faculty.controller.LecturerController lc = new com.faculty.controller.LecturerController();

        com.faculty.view.components.RoundedComboBox<String> cmbYear = new com.faculty.view.components.RoundedComboBox<>(20);
        cmbYear.setPreferredSize(new Dimension(300, 40));
        cmbYear.addItem("1"); cmbYear.addItem("2"); cmbYear.addItem("3"); cmbYear.addItem("4");
        if (course != null) cmbYear.setSelectedItem(String.valueOf(course.getAcademicYear()));

        com.faculty.view.components.RoundedComboBox<String> cmbSemester = new com.faculty.view.components.RoundedComboBox<>(20);
        cmbSemester.setPreferredSize(new Dimension(300, 40));
        cmbSemester.addItem("1"); cmbSemester.addItem("2");
        if (course != null) cmbSemester.setSelectedItem(String.valueOf(course.getSemester()));

        com.faculty.controller.DegreeController degCtrl = new com.faculty.controller.DegreeController();
        com.faculty.view.components.RoundedComboBox<String> cmbDegree = new com.faculty.view.components.RoundedComboBox<>(20);
        cmbDegree.setPreferredSize(new Dimension(300, 40));
        for (com.faculty.model.Degree d : degCtrl.getAllDegrees()) {
            cmbDegree.addItem(d.getName());
        }
        if (course != null && course.getDegreeName() != null) cmbDegree.setSelectedItem(course.getDegreeName());

        String[] labels = {"Course code:", "Course name:", "Credits:", "Lecturer Name:", "Degree:", "Academic Year:", "Semester:"};
        JComponent[] fields = {txtCourseCode, txtCourseName, txtCredits, txtLecturerName, cmbDegree, cmbYear, cmbSemester};

        com.faculty.view.components.ModernDialog dialog = new com.faculty.view.components.ModernDialog(
            SwingUtilities.getWindowAncestor(this), 
            course == null ? "Add Course" : "Edit Course", 
            labels, fields
        );
        dialog.setVisible(true);

        if (dialog.isOk()) {
            try {
                String code = txtCourseCode.getText();
                String courseName = txtCourseName.getText();
                int credits = Integer.parseInt(txtCredits.getText());
                String lecturerName = txtLecturerName.getText().trim();
                int year = Integer.parseInt((String) cmbYear.getSelectedItem());
                int semester = Integer.parseInt((String) cmbSemester.getSelectedItem());

                int lecturerId = 0;
                if (!lecturerName.isEmpty()) {
                    for (com.faculty.model.Lecturer l : lc.getAllLecturers()) {
                        if (l.getFullName().equalsIgnoreCase(lecturerName)) {
                            lecturerId = l.getId();
                            break;
                        }
                    }
                }

                int degreeId = 0;
                String degreeName = (String) cmbDegree.getSelectedItem();
                if (degreeName != null) {
                    for (com.faculty.model.Degree d : degCtrl.getAllDegrees()) {
                        if (d.getName().equals(degreeName)) {
                            degreeId = d.getId();
                            break;
                        }
                    }
                }

                if (course == null) {
                    Course newCourse = new Course(0, code, courseName, credits, lecturerId, year, semester, degreeId);
                    newCourse.setLecturerName(lecturerName);
                    newCourse.setDegreeName(degreeName);
                    addedList.add(newCourse);
                    allCourses.add(newCourse);
                    tableModel.addRow(new Object[]{code, courseName, credits, lecturerName, degreeName, year, semester});
                } else {
                    course.setCourseCode(code);
                    course.setCourseName(courseName);
                    course.setCredits(credits);
                    course.setLecturerId(lecturerId);
                    course.setAcademicYear(year);
                    course.setSemester(semester);
                    course.setDegreeId(degreeId);
                    course.setDegreeName(degreeName);
                    
                    if (!addedList.contains(course) && !editedList.contains(course)) {
                        editedList.add(course);
                    }
                    tableModel.setValueAt(code, row, 0);
                    tableModel.setValueAt(courseName, row, 1);
                    tableModel.setValueAt(credits, row, 2);
                    tableModel.setValueAt(lecturerName, row, 3);
                    tableModel.setValueAt(degreeName, row, 4);
                    tableModel.setValueAt(year, row, 5);
                    tableModel.setValueAt(semester, row, 6);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private Course getCourseById(String courseCode) {
        for (Course c : allCourses) {
            if (c.getCourseCode().equals(courseCode)) return c;
        }
        return null;
    }
    
    private void loadData() {
        tableModel.setRowCount(0);
        allCourses = courseController.getAllCourses();
        for (Course c : allCourses) {
            tableModel.addRow(new Object[]{
                c.getCourseCode(), 
                c.getCourseName(), 
                c.getCredits(), 
                c.getLecturerName() != null ? c.getLecturerName() : String.valueOf(c.getLecturerId()),
                c.getDegreeName() != null ? c.getDegreeName() : "N/A",
                c.getAcademicYear(),
                c.getSemester()
            });
        }
    }
}





