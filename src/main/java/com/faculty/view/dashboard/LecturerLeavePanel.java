package com.faculty.view.dashboard;

import com.faculty.controller.LeaveController;
import com.faculty.controller.LecturerController;
import com.faculty.model.Leave;
import com.faculty.model.Lecturer;
import com.faculty.model.User;
import com.faculty.view.components.ModernIcon;
import com.faculty.view.components.RoundedButton;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class LecturerLeavePanel extends JPanel {
    private DefaultTableModel tableModel;
    private JTable table;
    private LeaveController leaveController;
    private Lecturer currentLecturer;

    private List<Leave> allLeaves;
    private List<Leave> addedList = new ArrayList<>();
    private List<Leave> editedList = new ArrayList<>();
    private List<Leave> deletedList = new ArrayList<>();

    public LecturerLeavePanel(User currentUser) {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(245, 247, 250));
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        leaveController = new LeaveController();
        LecturerController lecturerController = new LecturerController();
        currentLecturer = lecturerController.getLecturerByUserId(currentUser.getId());

        com.faculty.view.components.GradientPanel banner = new com.faculty.view.components.GradientPanel(new Color(20, 61, 89), new Color(20, 61, 89), false, 20);
        banner.setLayout(new BorderLayout());
        banner.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        JLabel title = new JLabel("Leave Requests");
        title.setFont(new Font("Montserrat", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        
        JLabel subtitle = new JLabel("Manage and request leaves.");
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
        
        RoundedButton btnAdd = new RoundedButton("Add Request", 20, new Color(20, 61, 89), Color.WHITE);
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
        
        String[] columns = {"ID", "Date", "Reason", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(tableModel);
        table.setRowHeight(40);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setFont(new Font("Montserrat", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(245, 247, 250));
        table.getTableHeader().setForeground(new Color(20, 61, 89));
        table.setFont(new Font("Montserrat", Font.PLAIN, 14));
        table.setSelectionBackground(new Color(230, 240, 250));
        table.setSelectionForeground(new Color(20, 61, 89));
        
        // Hide ID column
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(0).setWidth(0);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        com.faculty.view.components.CardPanel tableContainer = new com.faculty.view.components.CardPanel(20, Color.WHITE);
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

        loadData();

        btnAdd.addActionListener(e -> {
            if (currentLecturer == null) {
                JOptionPane.showMessageDialog(this, "Please set up your profile first.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            showAddEditDialog(null, -1);
        });

        btnEdit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Please select a leave request to edit.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int id = (int) tableModel.getValueAt(row, 0);
            Leave leave = getLeaveById(id);
            if (leave != null) showAddEditDialog(leave, row);
        });

        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Please select a leave request to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int id = (int) tableModel.getValueAt(row, 0);
            Leave leave = getLeaveById(id);
            if (leave != null) {
                if (addedList.contains(leave)) {
                    addedList.remove(leave);
                } else {
                    deletedList.add(leave);
                    editedList.remove(leave);
                }
                allLeaves.remove(leave);
            }
            tableModel.removeRow(row);
        });

        btnSave.addActionListener(e -> {
            try {
                leaveController.saveBatch(addedList, editedList, deletedList);
                JOptionPane.showMessageDialog(this, "Changes saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                addedList.clear();
                editedList.clear();
                deletedList.clear();
                loadData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error saving changes: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void loadData() {
        tableModel.setRowCount(0);
        if (currentLecturer == null) return;
        
        allLeaves = leaveController.getLeavesByLecturer(currentLecturer.getId());
        for (Leave l : allLeaves) {
            tableModel.addRow(new Object[]{
                l.getId(),
                l.getLeaveDate().toString(),
                l.getReason(),
                l.getStatus()
            });
        }
    }

    private Leave getLeaveById(int id) {
        for (Leave l : allLeaves) {
            if (l.getId() == id) return l;
        }
        return null;
    }

    private void showAddEditDialog(Leave leave, int row) {
        com.github.lgooddatepicker.components.DatePickerSettings dateSettings = new com.github.lgooddatepicker.components.DatePickerSettings();
        dateSettings.setFormatForDatesCommonEra("yyyy-MM-dd");
        dateSettings.setFormatForDatesBeforeCommonEra("uuuu-MM-dd");
        dateSettings.setFontValidDate(new Font("Montserrat", Font.PLAIN, 14));
        dateSettings.setFontCalendarDateLabels(new Font("Montserrat", Font.PLAIN, 14));
        dateSettings.setFontCalendarWeekdayLabels(new Font("Montserrat", Font.BOLD, 13));
        dateSettings.setColor(com.github.lgooddatepicker.components.DatePickerSettings.DateArea.CalendarBackgroundNormalDates, Color.WHITE);
        dateSettings.setColor(com.github.lgooddatepicker.components.DatePickerSettings.DateArea.BackgroundOverallCalendarPanel, Color.WHITE);
        dateSettings.setColor(com.github.lgooddatepicker.components.DatePickerSettings.DateArea.BackgroundMonthAndYearNavigationButtons, new Color(245, 247, 250));
        dateSettings.setColor(com.github.lgooddatepicker.components.DatePickerSettings.DateArea.TextMonthAndYearNavigationButtons, new Color(20, 61, 89));

        com.github.lgooddatepicker.components.DatePicker datePicker = new com.github.lgooddatepicker.components.DatePicker(dateSettings);
        datePicker.setPreferredSize(new Dimension(300, 40));
        
        JButton calendarBtn = datePicker.getComponentToggleCalendarButton();
        calendarBtn.setText("");
        calendarBtn.setIcon(new com.faculty.view.components.ModernIcon(com.faculty.view.components.ModernIcon.IconType.TIMETABLE, 18, new Color(20, 61, 89)));
        calendarBtn.setBackground(Color.WHITE);
        calendarBtn.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        calendarBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        datePicker.getComponentDateTextField().setFont(new Font("Montserrat", Font.PLAIN, 14));
        datePicker.getComponentDateTextField().setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        if (leave != null) {
            datePicker.setDate(leave.getLeaveDate().toLocalDate());
        } else {
            datePicker.setDateToToday();
        }

        com.faculty.view.components.RoundedTextField txtReason = new com.faculty.view.components.RoundedTextField(15, 20);
        txtReason.setText(leave != null ? leave.getReason() : "");

        String[] labels = {"Leave Date:", "Reason:"};
        JComponent[] fields = {datePicker, txtReason};

        com.faculty.view.components.ModernDialog dialog = new com.faculty.view.components.ModernDialog(
                SwingUtilities.getWindowAncestor(this), 
                leave == null ? "Add Request" : "Edit Request", 
                labels, fields);
        dialog.setVisible(true);

        if (dialog.isOk()) {
            try {
                java.time.LocalDate localDate = datePicker.getDate();
                if (localDate == null) {
                    throw new Exception("Date cannot be empty.");
                }
                if (localDate.isBefore(java.time.LocalDate.now())) {
                    throw new Exception("Leave date cannot be in the past!");
                }
                java.sql.Date date = java.sql.Date.valueOf(localDate);
                String reason = txtReason.getText();
                
                if (leave == null) {
                    Leave newLeave = new Leave(0, currentLecturer.getId(), date, reason, "Pending");
                    addedList.add(newLeave);
                    allLeaves.add(newLeave);
                    tableModel.addRow(new Object[]{newLeave.getId(), date.toString(), reason, "Pending"});
                } else {
                    leave.setLeaveDate(date);
                    leave.setReason(reason);

                    if (!addedList.contains(leave) && !editedList.contains(leave)) {
                        editedList.add(leave);
                    }
                    tableModel.setValueAt(date.toString(), row, 1);
                    tableModel.setValueAt(reason, row, 2);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}





