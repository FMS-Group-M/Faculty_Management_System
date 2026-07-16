package com.faculty.view.dashboard;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;

import com.faculty.controller.DegreeController;
import com.faculty.model.Degree;
import com.faculty.view.components.CardPanel;
import com.faculty.view.components.GradientPanel;
import com.faculty.view.components.ModernIcon;
import com.faculty.view.components.RoundedButton;
import com.faculty.view.components.StyledTable;

public class AdminDegreesPanel extends JPanel {
    private StyledTable table;
    private DefaultTableModel tableModel;
    private DegreeController degreeController;

    private List<Degree> allDegrees;
    private List<Degree> addedList = new ArrayList<>();
    private List<Degree> editedList = new ArrayList<>();
    private List<Degree> deletedList = new ArrayList<>();

    public AdminDegreesPanel() {
        degreeController = new DegreeController();
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(245, 247, 250));
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Top Banner
        GradientPanel banner = new GradientPanel(new Color(20, 61, 89), new Color(20, 61, 89), false, 20);
        banner.setLayout(new BorderLayout());
        banner.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        JLabel title = new JLabel("Manage Degrees");
        title.setFont(new Font("Montserrat", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        
        JLabel subtitle = new JLabel("View and manage all faculty degrees and durations.");
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
        
        RoundedButton btnAdd = new RoundedButton("Add Degree", 20, new Color(20, 61, 89), Color.WHITE);
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
        
        String[] columns = {"Degree", "Department Name", "No of Students"};
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
                JOptionPane.showMessageDialog(this, "Please select a degree to edit.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Degree d = allDegrees.get(row);
            if (d != null) showAddEditDialog(d, row);
        });
        
        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Please select a degree to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Degree d = allDegrees.get(row);
            if (d != null) {
                if (addedList.contains(d)) {
                    addedList.remove(d);
                } else {
                    deletedList.add(d);
                    editedList.remove(d);
                }
                allDegrees.remove(d);
            }
            tableModel.removeRow(row);
        });
        
        btnSave.addActionListener(e -> {
            try {
                degreeController.saveBatch(addedList, editedList, deletedList);
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
    
    private void showAddEditDialog(Degree degree, int row) {
        com.faculty.view.components.RoundedTextField txtName = new com.faculty.view.components.RoundedTextField(15, 20);
        txtName.setText(degree != null ? degree.getName() : "");
        com.faculty.view.components.RoundedComboBox<String> cmbDepartment = new com.faculty.view.components.RoundedComboBox<>(20);
        cmbDepartment.setPreferredSize(new Dimension(300, 40));
        com.faculty.controller.DepartmentController dc = new com.faculty.controller.DepartmentController();
        for (com.faculty.model.Department dept : dc.getAllDepartments()) {
            cmbDepartment.addItem(dept.getName());
        }
        if (degree != null && degree.getDepartmentName() != null) {
            cmbDepartment.setSelectedItem(degree.getDepartmentName());
        }

        com.faculty.view.components.RoundedTextField txtNoOfStudents = new com.faculty.view.components.RoundedTextField(15, 20);
        txtNoOfStudents.setText(degree != null ? String.valueOf(degree.getNoOfStudents()) : "0");

        String[] labels = {"Degree Name:", "Department Name:", "No. of Students:"};
        JComponent[] fields = {txtName, cmbDepartment, txtNoOfStudents};

        com.faculty.view.components.ModernDialog dialog = new com.faculty.view.components.ModernDialog(
            SwingUtilities.getWindowAncestor(this), 
            degree == null ? "Add Degree" : "Edit Degree", 
            labels, fields
        );
        dialog.setVisible(true);

        if (dialog.isOk()) {
            try {
                String name = txtName.getText();
                String deptName = (String) cmbDepartment.getSelectedItem();
                int departmentId = 1;
                for (com.faculty.model.Department dept : dc.getAllDepartments()) {
                    if (dept.getName().equals(deptName)) {
                        departmentId = dept.getId();
                        break;
                    }
                }
                int noOfStudents = Integer.parseInt(txtNoOfStudents.getText());

                if (degree == null) {
                    Degree newDegree = new Degree(0, name, departmentId, noOfStudents);
                    addedList.add(newDegree);
                    allDegrees.add(newDegree);
                    tableModel.addRow(new Object[]{name, deptName, String.valueOf(noOfStudents)});
                } else {
                    degree.setName(name);
                    degree.setDepartmentId(departmentId);
                    degree.setNoOfStudents(noOfStudents);
                    
                    if (!addedList.contains(degree) && !editedList.contains(degree)) {
                        editedList.add(degree);
                    }
                    tableModel.setValueAt(name, row, 0);
                    tableModel.setValueAt(deptName, row, 1);
                    tableModel.setValueAt(String.valueOf(noOfStudents), row, 2);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private Degree getDegreeById(int id) {
        for (Degree d : allDegrees) {
            if (d.getId() == id) return d;
        }
        return null;
    }
    
    private void loadData() {
        tableModel.setRowCount(0);
        allDegrees = degreeController.getAllDegrees();
        for (Degree d : allDegrees) {
            tableModel.addRow(new Object[]{
                d.getName(), 
                d.getDepartmentName() != null ? d.getDepartmentName() : String.valueOf(d.getDepartmentId()), 
                String.valueOf(d.getNoOfStudents())
            });
        }
    }
}





