package com.faculty.view.dashboard;

import javax.swing.*;
import java.awt.*;
import com.faculty.controller.LecturerController;
import com.faculty.model.Lecturer;
import com.faculty.model.User;
import com.faculty.view.components.RoundedButton;
import com.faculty.view.components.RoundedTextField;

public class LecturerProfilePanel extends JPanel {
    private RoundedTextField txtFullName;
    private com.faculty.view.components.RoundedComboBox<String> cmbDepartment;
    private RoundedTextField txtEmail;
    private RoundedTextField txtMobile;
    private Lecturer currentLecturer;
    private boolean isEditing = false;
    
    public LecturerProfilePanel(User currentUser) {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(245, 247, 250));
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        LecturerController controller = new LecturerController();
        currentLecturer = controller.getLecturerByUserId(currentUser.getId());

        com.faculty.view.components.GradientPanel banner = new com.faculty.view.components.GradientPanel(new Color(20, 61, 89), new Color(20, 61, 89), false, 20);
        banner.setLayout(new BorderLayout());
        banner.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        JLabel title = new JLabel("My Profile");
        title.setFont(new Font("Montserrat", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        
        JLabel subtitle = new JLabel("View and update your personal details and contact information.");
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
        
        add(bannerWrapper, BorderLayout.NORTH);

        com.faculty.view.components.CardPanel formContainer = new com.faculty.view.components.CardPanel(20, Color.WHITE);
        formContainer.setLayout(new BorderLayout());
        formContainer.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 10, 5, 10);
        
        txtFullName = addField(formPanel, gbc, "Full Name", 0, currentLecturer != null ? currentLecturer.getFullName() : "");
        cmbDepartment = addComboBoxField(formPanel, gbc, "Department", 1, currentLecturer != null ? currentLecturer.getDepartmentName() : "");
        txtEmail = addField(formPanel, gbc, "Email", 2, currentLecturer != null ? currentLecturer.getEmail() : "");
        txtMobile = addField(formPanel, gbc, "Mobile Number", 3, currentLecturer != null ? currentLecturer.getMobileNumber() : "");
        
        setFieldsEditable(false);

        formContainer.add(formPanel, BorderLayout.CENTER);
        
        JPanel centerWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 30));
        centerWrapper.setOpaque(false);
        centerWrapper.add(formContainer);
        
        JScrollPane scrollPane = new JScrollPane(centerWrapper);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);
        
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        bottomPanel.setOpaque(false);
        
        RoundedButton btnEdit = new RoundedButton("Edit", 20, new Color(230, 240, 250), new Color(0, 100, 200));
        btnEdit.setIcon(new com.faculty.view.components.ModernIcon(com.faculty.view.components.ModernIcon.IconType.EDIT, 16, new Color(0, 100, 200)));
        btnEdit.setIconTextGap(10);
        btnEdit.setPreferredSize(new Dimension(120, 45));

        RoundedButton btnSave = new RoundedButton("Save changes", 20, new Color(20, 61, 89), Color.WHITE);
        btnSave.setIcon(new com.faculty.view.components.ModernIcon(com.faculty.view.components.ModernIcon.IconType.SAVE, 18, Color.WHITE));
        btnSave.setIconTextGap(10);
        btnSave.setPreferredSize(new Dimension(200, 45));
        
        btnEdit.addActionListener(e -> {
            isEditing = !isEditing;
            setFieldsEditable(isEditing);
            btnEdit.setText(isEditing ? "Cancel" : "Edit");
            if (!isEditing && currentLecturer != null) {
                txtFullName.setText(currentLecturer.getFullName());
                txtEmail.setText(currentLecturer.getEmail());
                txtMobile.setText(currentLecturer.getMobileNumber());
                cmbDepartment.setSelectedItem(currentLecturer.getDepartmentName() != null ? currentLecturer.getDepartmentName() : "");
            }
        });
        btnSave.addActionListener(e -> {
            try {
                String emailInput = txtEmail.getText().trim();
                if (!emailInput.contains("@")) {
                    throw new Exception("Invalid email format! Email must contain an '@' symbol.");
                }

                String deptInput = (String) cmbDepartment.getSelectedItem();
                
                int deptId = 0;
                if (deptInput != null && !deptInput.isEmpty()) {
                    com.faculty.controller.DepartmentController dc = new com.faculty.controller.DepartmentController();
                    for (com.faculty.model.Department d : dc.getAllDepartments()) {
                        if (d.getName().equalsIgnoreCase(deptInput)) {
                            deptId = d.getId();
                            break;
                        }
                    }
                }

                if (currentLecturer == null) {
                    currentLecturer = new Lecturer(0, currentUser.getId(), txtFullName.getText(), deptId, txtEmail.getText(), txtMobile.getText());
                    controller.addLecturerProfile(currentLecturer);
                } else {
                    currentLecturer.setFullName(txtFullName.getText());
                    currentLecturer.setDepartmentId(deptId);
                    currentLecturer.setEmail(txtEmail.getText());
                    currentLecturer.setMobileNumber(txtMobile.getText());
                    controller.updateLecturerProfile(currentLecturer);
                }
                JOptionPane.showMessageDialog(this, "Profile updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                isEditing = false;
                setFieldsEditable(false);
                btnEdit.setText("Edit");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error updating profile: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        bottomPanel.add(btnEdit);
        bottomPanel.add(btnSave);
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    private void setFieldsEditable(boolean editable) {
        txtFullName.setEditable(editable);
        cmbDepartment.setEnabled(editable);
        txtEmail.setEditable(editable);
        txtMobile.setEditable(editable);
    }
    
    private RoundedTextField addField(JPanel formPanel, GridBagConstraints gbc, String label, int row, String value) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Montserrat", Font.BOLD, 15));
        lbl.setForeground(new Color(80, 80, 80));
        
        RoundedTextField txt = new RoundedTextField(20, 15);
        txt.setText(value);
        txt.setPreferredSize(new Dimension(300, 40));
        
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        formPanel.add(lbl, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.weightx = 0.7;
        formPanel.add(txt, gbc);
        return txt;
    }

    private com.faculty.view.components.RoundedComboBox<String> addComboBoxField(JPanel formPanel, GridBagConstraints gbc, String label, int row, String selectedItem) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Montserrat", Font.BOLD, 15));
        lbl.setForeground(new Color(80, 80, 80));
        
        com.faculty.view.components.RoundedComboBox<String> cmb = new com.faculty.view.components.RoundedComboBox<>(20);
        cmb.setPreferredSize(new Dimension(300, 40));
        
        com.faculty.controller.DepartmentController dc = new com.faculty.controller.DepartmentController();
        for (com.faculty.model.Department d : dc.getAllDepartments()) {
            cmb.addItem(d.getName());
        }
        if (selectedItem != null && !selectedItem.isEmpty()) {
            cmb.setSelectedItem(selectedItem);
        }
        
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        formPanel.add(lbl, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.weightx = 0.7;
        formPanel.add(cmb, gbc);
        return cmb;
    }
}





