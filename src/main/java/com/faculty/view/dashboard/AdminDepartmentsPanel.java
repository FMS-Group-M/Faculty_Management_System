package com.faculty.view.dashboard;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;

import com.faculty.controller.DepartmentController;
import com.faculty.model.Department;
import com.faculty.view.components.CardPanel;
import com.faculty.view.components.GradientPanel;
import com.faculty.view.components.ModernIcon;
import com.faculty.view.components.RoundedButton;
import com.faculty.view.components.StyledTable;

public class AdminDepartmentsPanel extends JPanel {
    private StyledTable table;
    private DefaultTableModel tableModel;
    private DepartmentController departmentController;

    private List<Department> allDepartments;
    private List<Department> addedList = new ArrayList<>();
    private List<Department> editedList = new ArrayList<>();
    private List<Department> deletedList = new ArrayList<>();

    public AdminDepartmentsPanel() {
        departmentController = new DepartmentController();
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(245, 247, 250)); // Very light blue/grey
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Top Banner
        GradientPanel banner = new GradientPanel(new Color(20, 61, 89), new Color(20, 61, 89), false, 20);
        banner.setLayout(new BorderLayout());
        banner.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        JLabel title = new JLabel("Manage Departments");
        title.setFont(new Font("Montserrat", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        
        JLabel subtitle = new JLabel("View and manage all faculty departments and staff.");
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
        
        RoundedButton btnAdd = new RoundedButton("Add Department", 20, new Color(20, 61, 89), Color.WHITE);
        btnAdd.setIcon(new ModernIcon(ModernIcon.IconType.ADD, 16, Color.WHITE));
        btnAdd.setIconTextGap(8);
        
        RoundedButton btnEdit = new RoundedButton("Edit", 20, new Color(230, 240, 250), new Color(0, 100, 200));
        btnEdit.setIcon(new ModernIcon(ModernIcon.IconType.EDIT, 16, new Color(0, 100, 200)));
        btnEdit.setIconTextGap(8);
        
        RoundedButton btnDelete = new RoundedButton("Delete", 20, new Color(255, 230, 230), new Color(220, 50, 50));
        btnDelete.setIcon(new ModernIcon(ModernIcon.IconType.DELETE, 16, new Color(220, 50, 50)));
        btnDelete.setIconTextGap(8);
        
        btnAdd.setPreferredSize(new Dimension(160, 40));
        btnEdit.setPreferredSize(new Dimension(100, 40));
        btnDelete.setPreferredSize(new Dimension(110, 40));
        
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnAdd);
        
        // Main Content Area
        JPanel mainContent = new JPanel(new BorderLayout(0, 10));
        mainContent.setOpaque(false);
        mainContent.add(buttonPanel, BorderLayout.NORTH);
        
        String[] columns = {"Name", "HOD", "No of Staff"};
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
                JOptionPane.showMessageDialog(this, "Please select a department to edit.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Department d = allDepartments.get(row);
            if (d != null) showAddEditDialog(d, row);
        });
        
        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Please select a department to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Department d = allDepartments.get(row);
            if (d != null) {
                if (addedList.contains(d)) {
                    addedList.remove(d);
                } else {
                    deletedList.add(d);
                    editedList.remove(d);
                }
                allDepartments.remove(d);
            }
            tableModel.removeRow(row);
        });
        
        btnSave.addActionListener(e -> {
            try {
                departmentController.saveBatch(addedList, editedList, deletedList);
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
    
    private void showAddEditDialog(Department department, int row) {
        com.faculty.view.components.RoundedTextField txtName = new com.faculty.view.components.RoundedTextField(15, 20);
        txtName.setText(department != null ? department.getName() : "");
        com.faculty.view.components.RoundedTextField txtHod = new com.faculty.view.components.RoundedTextField(15, 20);
        txtHod.setText(department != null ? department.getHodName() : "");
        com.faculty.view.components.RoundedTextField txtNoOfStaff = new com.faculty.view.components.RoundedTextField(15, 20);
        txtNoOfStaff.setText(department != null ? String.valueOf(department.getNoOfStaff()) : "0");

        String[] labels = {"Department Name:", "HOD Name:", "No. of Staff:"};
        JComponent[] fields = {txtName, txtHod, txtNoOfStaff};

        com.faculty.view.components.ModernDialog dialog = new com.faculty.view.components.ModernDialog(
            SwingUtilities.getWindowAncestor(this), 
            department == null ? "Add Department" : "Edit Department", 
            labels, fields
        );
        dialog.setVisible(true);

        if (dialog.isOk()) {
            try {
                String name = txtName.getText();
                String hod = txtHod.getText();
                int noOfStaff = Integer.parseInt(txtNoOfStaff.getText());

                if (department == null) {
                    Department newDept = new Department(0, name, hod, noOfStaff);
                    addedList.add(newDept);
                    allDepartments.add(newDept);
                    tableModel.addRow(new Object[]{name, hod, String.valueOf(noOfStaff)});
                } else {
                    department.setName(name);
                    department.setHodName(hod);
                    department.setNoOfStaff(noOfStaff);
                    
                    if (!addedList.contains(department) && !editedList.contains(department)) {
                        editedList.add(department);
                    }
                    tableModel.setValueAt(name, row, 0);
                    tableModel.setValueAt(hod, row, 1);
                    tableModel.setValueAt(String.valueOf(noOfStaff), row, 2);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private Department getDepartmentById(int id) {
        for (Department d : allDepartments) {
            if (d.getId() == id) return d;
        }
        return null;
    }
    
    private void loadData() {
        tableModel.setRowCount(0);
        allDepartments = departmentController.getAllDepartments();
        for (Department d : allDepartments) {
            tableModel.addRow(new Object[]{
                d.getName(), 
                d.getHodName(), 
                String.valueOf(d.getNoOfStaff())
            });
        }
    }
}





