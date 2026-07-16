package com.faculty.view.dashboard;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;

import com.faculty.controller.StudentController;
import com.faculty.model.Student;
import com.faculty.model.User;
import com.faculty.view.components.CardPanel;
import com.faculty.view.components.GradientPanel;
import com.faculty.view.components.ModernIcon;
import com.faculty.view.components.RoundedButton;
import com.faculty.view.components.StyledTable;

public class AdminStudentsPanel extends JPanel {
    private StyledTable table;
    private DefaultTableModel tableModel;
    private StudentController studentController;
    private User currentUser;

    private List<Student> allStudents;
    private List<Student> addedList = new ArrayList<>();
    private List<Student> editedList = new ArrayList<>();
    private List<Student> deletedList = new ArrayList<>();

    public AdminStudentsPanel(User user) {
        this.currentUser = user;
        studentController = new StudentController();
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(245, 247, 250));
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Top Banner
        GradientPanel banner = new GradientPanel(new Color(20, 61, 89), new Color(20, 61, 89), false, 20);
        banner.setLayout(new BorderLayout());
        banner.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        JLabel title = new JLabel("Manage Students");
        title.setFont(new Font("Montserrat", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        
        JLabel subtitle = new JLabel("Manage all students, view their details and academic records.");
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
        
        RoundedButton btnAdd = new RoundedButton("Add Student", 20, new Color(20, 61, 89), Color.WHITE);
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
        
        String[] columns = {"Full Name", "Student ID", "Degree Name", "Email", "Mobile Number"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new StyledTable();
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
                JOptionPane.showMessageDialog(this, "Please select a student to edit.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String studentId = (String) tableModel.getValueAt(row, 1);
            Student s = getStudentById(studentId);
            if (s != null) showAddEditDialog(s, row);
        });
        
        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Please select a student to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String studentId = (String) tableModel.getValueAt(row, 1);
            Student s = getStudentById(studentId);
            if (s != null) {
                if (addedList.contains(s)) {
                    addedList.remove(s);
                } else {
                    deletedList.add(s);
                    editedList.remove(s);
                }
                allStudents.remove(s);
            }
            tableModel.removeRow(row);
        });
        
        btnSave.addActionListener(e -> {
            try {
                studentController.saveBatch(addedList, editedList, deletedList);
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
    
    private void showAddEditDialog(Student student, int row) {
        com.faculty.view.components.RoundedTextField txtFullName = new com.faculty.view.components.RoundedTextField(15, 20);
        txtFullName.setText(student != null ? student.getFullName() : "");
        com.faculty.view.components.RoundedTextField txtStudentId = new com.faculty.view.components.RoundedTextField(15, 20);
        txtStudentId.setText(student != null ? student.getStudentIdStr() : "");
        com.faculty.view.components.RoundedComboBox<String> cmbDegree = new com.faculty.view.components.RoundedComboBox<>(20);
        cmbDegree.setPreferredSize(new Dimension(300, 40));
        com.faculty.controller.DegreeController dc = new com.faculty.controller.DegreeController();
        for (com.faculty.model.Degree d : dc.getAllDegrees()) {
            cmbDegree.addItem(d.getName());
        }
        if (student != null && student.getDegreeName() != null) {
            cmbDegree.setSelectedItem(student.getDegreeName());
        }
        
        com.faculty.view.components.RoundedTextField txtEmail = new com.faculty.view.components.RoundedTextField(15, 20);
        txtEmail.setText(student != null ? student.getEmail() : "");
        com.faculty.view.components.RoundedTextField txtMobile = new com.faculty.view.components.RoundedTextField(15, 20);
        txtMobile.setText(student != null ? student.getMobileNumber() : "");

        String[] labels = {"Full Name:", "Student ID:", "Degree Name:", "Email:", "Mobile Number:"};
        JComponent[] fields = {txtFullName, txtStudentId, cmbDegree, txtEmail, txtMobile};

        com.faculty.view.components.ModernDialog dialog = new com.faculty.view.components.ModernDialog(
            SwingUtilities.getWindowAncestor(this), 
            student == null ? "Add Student" : "Edit Student", 
            labels, fields
        );
        dialog.setVisible(true);
        
        if (dialog.isOk()) {
            try {
                String fullName = txtFullName.getText();
                String studentId = txtStudentId.getText();
                String degreeName = (String) cmbDegree.getSelectedItem();
                String email = txtEmail.getText();
                String mobile = txtMobile.getText();

                validateStudentData(studentId, mobile, degreeName, email);

                int degreeId = 0;
                for (com.faculty.model.Degree d : dc.getAllDegrees()) {
                    if (d.getName().equals(degreeName)) {
                        degreeId = d.getId();
                        break;
                    }
                }

                if (student == null) {
                    Student newStudent = new Student(0, 0, studentId, fullName, degreeId, email, mobile);
                    addedList.add(newStudent);
                    allStudents.add(newStudent);
                    tableModel.addRow(new Object[]{fullName, studentId, degreeName, email, mobile});
                } else {
                    student.setFullName(fullName);
                    student.setDegreeId(degreeId);
                    student.setEmail(email);
                    student.setMobileNumber(mobile);
                    
                    if (!addedList.contains(student) && !editedList.contains(student)) {
                        editedList.add(student);
                    }
                    tableModel.setValueAt(fullName, row, 0);
                    tableModel.setValueAt(studentId, row, 1);
                    tableModel.setValueAt(degreeName, row, 2);
                    tableModel.setValueAt(email, row, 3);
                    tableModel.setValueAt(mobile, row, 4);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private Student getStudentById(String studentIdStr) {
        for (Student s : allStudents) {
            if (s.getStudentIdStr().equals(studentIdStr)) return s;
        }
        return null;
    }
    
    private void loadData() {
        tableModel.setRowCount(0);
        allStudents = studentController.getAllStudents();
        for (Student s : allStudents) {
            tableModel.addRow(new Object[]{
                s.getFullName(), 
                s.getStudentIdStr(), 
                s.getDegreeName() != null ? s.getDegreeName() : String.valueOf(s.getDegreeId()),
                s.getEmail(),
                s.getMobileNumber()
            });
        }
    }
    
    private void validateStudentData(String studentId, String mobile, String degreeName, String email) throws Exception {
        if (!mobile.matches("^07\\d{8}$")) {
            throw new Exception("Mobile number must start with 07 and be exactly 10 digits.");
        }
        if (!email.contains("@") || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new Exception("Invalid email format! Email must contain an '@' symbol.");
        }
        String prefix = "";
        if (degreeName.equals("Information Technology")) prefix = "CT";
        else if (degreeName.equals("Engineering Technology")) prefix = "ET";
        else if (degreeName.equals("Computer Science")) prefix = "CS";
        else if (degreeName.equals("Bio Systems Technology")) prefix = "BT";
        else throw new Exception("Unknown degree selected.");

        if (!studentId.matches("^" + prefix + "/20\\d{2}/\\d{3}$")) {
            throw new Exception("Student ID must match format " + prefix + "/20xx/xxx for " + degreeName);
        }
    }
}





