package com.faculty.view.dashboard;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;

import com.faculty.controller.LecturerController;
import com.faculty.model.Lecturer;
import com.faculty.view.components.CardPanel;
import com.faculty.view.components.GradientPanel;
import com.faculty.view.components.ModernIcon;
import com.faculty.view.components.RoundedButton;
import com.faculty.view.components.StyledTable;

public class AdminLecturersPanel extends JPanel {
    private StyledTable table;
    private DefaultTableModel tableModel;
    private LecturerController lecturerController;

    private List<Lecturer> allLecturers;
    private List<Lecturer> addedList = new ArrayList<>();
    private List<Lecturer> editedList = new ArrayList<>();
    private List<Lecturer> deletedList = new ArrayList<>();

    public AdminLecturersPanel() {
        lecturerController = new LecturerController();
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(245, 247, 250));
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Top Banner
        GradientPanel banner = new GradientPanel(new Color(20, 61, 89), new Color(20, 61, 89), false, 20);
        banner.setLayout(new BorderLayout());
        banner.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        JLabel title = new JLabel("Manage Lecturers");
        title.setFont(new Font("Montserrat", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        
        JLabel subtitle = new JLabel("View and manage all faculty lecturers and course assignments.");
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
        
        RoundedButton btnAdd = new RoundedButton("Add Lecturer", 20, new Color(20, 61, 89), Color.WHITE);
        btnAdd.setIcon(new ModernIcon(ModernIcon.IconType.ADD, 16, Color.WHITE));
        btnAdd.setIconTextGap(8);
        
        RoundedButton btnEdit = new RoundedButton("Edit", 20, new Color(230, 240, 250), new Color(0, 100, 200));
        btnEdit.setIcon(new ModernIcon(ModernIcon.IconType.EDIT, 16, new Color(0, 100, 200)));
        btnEdit.setIconTextGap(8);
        
        RoundedButton btnDelete = new RoundedButton("Delete", 20, new Color(255, 230, 230), new Color(220, 50, 50));
        btnDelete.setIcon(new ModernIcon(ModernIcon.IconType.DELETE, 16, new Color(220, 50, 50)));
        btnDelete.setIconTextGap(8);
        
        btnAdd.setPreferredSize(new Dimension(150, 40));
        btnEdit.setPreferredSize(new Dimension(100, 40));
        btnDelete.setPreferredSize(new Dimension(110, 40));
        
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnAdd);
        
        // Main Content Area
        JPanel mainContent = new JPanel(new BorderLayout(0, 10));
        mainContent.setOpaque(false);
        mainContent.add(buttonPanel, BorderLayout.NORTH);
        
        String[] columns = {"Full Name", "Department Name", "Email", "Mobile Number"};
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
                JOptionPane.showMessageDialog(this, "Please select a lecturer to edit.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String email = (String) tableModel.getValueAt(row, 2);
            Lecturer l = getLecturerByEmail(email);
            if (l != null) showAddEditDialog(l, row);
        });
        
        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Please select a lecturer to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String email = (String) tableModel.getValueAt(row, 2);
            Lecturer l = getLecturerByEmail(email);
            if (l != null) {
                if (addedList.contains(l)) {
                    addedList.remove(l);
                } else {
                    deletedList.add(l);
                    editedList.remove(l);
                }
                allLecturers.remove(l);
            }
            tableModel.removeRow(row);
        });
        
        btnSave.addActionListener(e -> {
            try {
                lecturerController.saveBatch(addedList, editedList, deletedList);
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
    
    private void showAddEditDialog(Lecturer lecturer, int row) {
        com.faculty.view.components.RoundedTextField txtFullName = new com.faculty.view.components.RoundedTextField(15, 20);
        txtFullName.setText(lecturer != null ? lecturer.getFullName() : "");
        com.faculty.view.components.RoundedComboBox<String> cmbDepartment = new com.faculty.view.components.RoundedComboBox<>(20);
        cmbDepartment.setPreferredSize(new Dimension(300, 40));
        com.faculty.controller.DepartmentController dc = new com.faculty.controller.DepartmentController();
        for (com.faculty.model.Department d : dc.getAllDepartments()) {
            cmbDepartment.addItem(d.getName());
        }
        if (lecturer != null && lecturer.getDepartmentName() != null) {
            cmbDepartment.setSelectedItem(lecturer.getDepartmentName());
        }
        com.faculty.view.components.RoundedTextField txtEmail = new com.faculty.view.components.RoundedTextField(15, 20);
        txtEmail.setText(lecturer != null ? lecturer.getEmail() : "");
        com.faculty.view.components.RoundedTextField txtMobile = new com.faculty.view.components.RoundedTextField(15, 20);
        txtMobile.setText(lecturer != null ? lecturer.getMobileNumber() : "");

        String[] labels = {"Full Name:", "Department Name:", "Email:", "Mobile Number:"};
        JComponent[] fields = {txtFullName, cmbDepartment, txtEmail, txtMobile};

        com.faculty.view.components.ModernDialog dialog = new com.faculty.view.components.ModernDialog(
            SwingUtilities.getWindowAncestor(this), 
            lecturer == null ? "Add Lecturer" : "Edit Lecturer", 
            labels, fields
        );
        dialog.setVisible(true);

        if (dialog.isOk()) {
            try {
                String fullName = txtFullName.getText();
                String deptName = (String) cmbDepartment.getSelectedItem();
                int departmentId = 0;
                for (com.faculty.model.Department d : dc.getAllDepartments()) {
                    if (d.getName().equals(deptName)) {
                        departmentId = d.getId();
                        break;
                    }
                }
                String email = txtEmail.getText();
                String mobile = txtMobile.getText();

                if (!mobile.matches("^07\\d{8}$")) {
                    throw new Exception("Mobile number must start with 07 and be exactly 10 digits.");
                }
                
                if (!email.contains("@") || !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                    throw new Exception("Invalid email format! Email must contain an '@' symbol.");
                }

                if (lecturer == null) {
                    Lecturer newLecturer = new Lecturer(0, 0, fullName, departmentId, email, mobile);
                    newLecturer.setCoursesTeaching("");
                    addedList.add(newLecturer);
                    allLecturers.add(newLecturer);
                    tableModel.addRow(new Object[]{fullName, deptName, email, mobile});
                } else {
                    lecturer.setFullName(fullName);
                    lecturer.setDepartmentId(departmentId);
                    lecturer.setEmail(email);
                    lecturer.setMobileNumber(mobile);
                    
                    if (!addedList.contains(lecturer) && !editedList.contains(lecturer)) {
                        editedList.add(lecturer);
                    }
                    tableModel.setValueAt(fullName, row, 0);
                    tableModel.setValueAt(deptName, row, 1);
                    tableModel.setValueAt(email, row, 2);
                    tableModel.setValueAt(mobile, row, 3);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private Lecturer getLecturerByEmail(String email) {
        for (Lecturer l : allLecturers) {
            if (l.getEmail().equals(email)) return l;
        }
        return null;
    }
    
    private void loadData() {
        tableModel.setRowCount(0);
        allLecturers = lecturerController.getAllLecturers();
        for (Lecturer l : allLecturers) {
            tableModel.addRow(new Object[]{
                l.getFullName(), 
                l.getDepartmentName() != null ? l.getDepartmentName() : String.valueOf(l.getDepartmentId()), 
                l.getEmail(),
                l.getMobileNumber()
            });
        }
    }
}





