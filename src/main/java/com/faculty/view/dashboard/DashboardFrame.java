package com.faculty.view.dashboard;

import com.faculty.model.User;
import com.faculty.view.components.RoundedButton;
import com.faculty.view.components.GradientPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class DashboardFrame extends JFrame {
    private User currentUser;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private JPanel sidebar;
    private List<JButton> navButtons = new ArrayList<>();

    public DashboardFrame(User user) {
        this.currentUser = user;
        setTitle("Faculty Management System - " + user.getRole() + " Dashboard");
        setSize(1050, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Content Area
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(new Color(248, 249, 250)); // Off-white for content area
        
        initializeViews();

        // Sidebar
        sidebar = createSidebar();
        add(sidebar, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);
        
        // Show first default view based on role
        if (currentUser.getRole().equals("Admin")) {
            cardLayout.show(contentPanel, "Students");
            if (!navButtons.isEmpty()) setActiveButton(navButtons.get(0));
        } else {
            cardLayout.show(contentPanel, "Profile");
            if (!navButtons.isEmpty()) setActiveButton(navButtons.get(0));
        }
    }

    private JPanel createSidebar() {
        GradientPanel panel = new GradientPanel(new Color(20, 61, 89), new Color(20, 61, 89), false);
        panel.setPreferredSize(new Dimension(300, 650));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 0, 20, 0)); // No horizontal padding, elements align center

        // Profile Badge (Image 2 Match)
        JPanel profilePanel = new JPanel();
        profilePanel.setLayout(new BoxLayout(profilePanel, BoxLayout.X_AXIS));
        profilePanel.setOpaque(false);
        profilePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Avatar icon
        JLabel avatar = new JLabel(new com.faculty.view.components.ModernIcon(com.faculty.view.components.ModernIcon.IconType.PROFILE, 50, Color.WHITE));
        avatar.setAlignmentY(Component.CENTER_ALIGNMENT);
        
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        textPanel.setAlignmentY(Component.CENTER_ALIGNMENT);
        
        String displayName = currentUser.getUsername();
        JLabel nameLbl = new JLabel(displayName);
        nameLbl.setFont(new Font("Montserrat", Font.BOLD, 18));
        nameLbl.setForeground(Color.WHITE);
        
        // Green Role Badge
        RoundedButton roleBadge = new RoundedButton("  " + currentUser.getRole().toUpperCase() + "  ", 15, new Color(28, 190, 117), Color.WHITE);
        roleBadge.setFont(new Font("Montserrat", Font.BOLD, 10));
        roleBadge.setFocusable(false);
        roleBadge.setRolloverEnabled(false);
        roleBadge.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        roleBadge.setMaximumSize(new Dimension(110, 20));
        
        nameLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        roleBadge.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        textPanel.add(nameLbl);
        textPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        textPanel.add(roleBadge);
        
        profilePanel.add(avatar);
        profilePanel.add(Box.createRigidArea(new Dimension(15, 0)));
        profilePanel.add(textPanel);
        
        panel.add(profilePanel);
        panel.add(Box.createRigidArea(new Dimension(0, 50)));

        // Navigation Buttons based on Role
        if (currentUser.getRole().equals("Admin")) {
            addNavButton(panel, "Students", "Students", com.faculty.view.components.ModernIcon.IconType.STUDENTS);
            addNavButton(panel, "Lecturers", "Lecturers", com.faculty.view.components.ModernIcon.IconType.LECTURERS);
            addNavButton(panel, "Courses", "Courses", com.faculty.view.components.ModernIcon.IconType.COURSES);
            addNavButton(panel, "Departments", "Departments", com.faculty.view.components.ModernIcon.IconType.DEPARTMENTS);
            addNavButton(panel, "Degrees", "Degrees", com.faculty.view.components.ModernIcon.IconType.DEGREES);
        } else if (currentUser.getRole().equals("Lecturer")) {
            addNavButton(panel, "My Profile", "Profile", com.faculty.view.components.ModernIcon.IconType.PROFILE);
            addNavButton(panel, "My Lectures", "Courses", com.faculty.view.components.ModernIcon.IconType.COURSES);
            addNavButton(panel, "Leave Requests", "Leave", com.faculty.view.components.ModernIcon.IconType.TIMETABLE);
        } else {
            // Student
            addNavButton(panel, "Profile Details", "Profile", com.faculty.view.components.ModernIcon.IconType.PROFILE);
            addNavButton(panel, "Time table", "Timetable", com.faculty.view.components.ModernIcon.IconType.TIMETABLE);
            addNavButton(panel, "Courses Enrolled", "Courses", com.faculty.view.components.ModernIcon.IconType.COURSES);
        }

        panel.add(Box.createVerticalGlue());

        // Logout Button
        RoundedButton btnLogout = new RoundedButton("Logout", 15, new Color(255, 255, 255, 50), Color.WHITE);
        btnLogout.setIcon(new com.faculty.view.components.ModernIcon(com.faculty.view.components.ModernIcon.IconType.LOGOUT, 18, Color.WHITE));
        btnLogout.setIconTextGap(10);
        btnLogout.setMaximumSize(new Dimension(240, 45));
        btnLogout.setFont(new Font("Montserrat", Font.BOLD, 15));
        btnLogout.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogout.setHorizontalAlignment(SwingConstants.CENTER);
        btnLogout.addActionListener(e -> {
            this.dispose();
            SwingUtilities.invokeLater(() -> {
                new com.faculty.view.auth.AuthFrame().setVisible(true);
            });
        });
        
        panel.add(btnLogout);
        panel.add(Box.createRigidArea(new Dimension(0, 20))); // bottom padding
        return panel;
    }

    private void addNavButton(JPanel sidebar, String text, String cardName, com.faculty.view.components.ModernIcon.IconType iconType) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getBackground().equals(Color.WHITE)) {
                    g2.setColor(Color.WHITE);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                } else if (getModel().isRollover()) {
                    g2.setColor(new Color(255, 255, 255, 40));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                }
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setContentAreaFilled(false);
        btn.setMaximumSize(new Dimension(240, 45));
        btn.setBackground(new Color(20, 61, 89)); // Primary dark blue
        btn.setForeground(new Color(230, 230, 255)); // Light text
        btn.setFont(new Font("Montserrat", Font.BOLD, 15));
        
        // Add icon
        com.faculty.view.components.ModernIcon icon = new com.faculty.view.components.ModernIcon(iconType, 20, new Color(230, 230, 255));
        btn.setIcon(icon);
        btn.setIconTextGap(15);
        btn.putClientProperty("btnIcon", icon); // Store to change color on hover
        
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 20));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setOpaque(false);
        
        btn.addActionListener((ActionEvent e) -> {
            cardLayout.show(contentPanel, cardName);
            setActiveButton(btn);
        });
        
        navButtons.add(btn);
        sidebar.add(btn);
        sidebar.add(Box.createRigidArea(new Dimension(0, 5)));
    }
    
    private void setActiveButton(JButton activeBtn) {
        for (JButton btn : navButtons) {
            com.faculty.view.components.ModernIcon icon = (com.faculty.view.components.ModernIcon) btn.getClientProperty("btnIcon");
            if (btn == activeBtn) {
                btn.setBackground(Color.WHITE);
                btn.setForeground(new Color(20, 61, 89));
                icon.setColor(new Color(20, 61, 89));
            } else {
                btn.setBackground(new Color(20, 61, 89));
                btn.setForeground(new Color(230, 230, 255));
                icon.setColor(new Color(230, 230, 255));
            }
            btn.repaint();
        }
    }

    private void initializeViews() {
        if (currentUser.getRole().equals("Admin")) {
            contentPanel.add(new AdminDegreesPanel(), "Degrees");
            contentPanel.add(new AdminDepartmentsPanel(), "Departments");
            contentPanel.add(new AdminCoursesPanel(), "Courses");
            contentPanel.add(new AdminLecturersPanel(), "Lecturers");
            contentPanel.add(new AdminStudentsPanel(currentUser), "Students");
        } else if (currentUser.getRole().equals("Lecturer")) {
            contentPanel.add(new LecturerProfilePanel(currentUser), "Profile");
            contentPanel.add(new LecturerCoursesPanel(currentUser), "Courses");
            contentPanel.add(new LecturerLeavePanel(currentUser), "Leave");
        } else {
            contentPanel.add(new StudentProfilePanel(currentUser), "Profile");
            contentPanel.add(new StudentTimetablePanel(currentUser), "Timetable");
            contentPanel.add(new StudentCoursesPanel(currentUser), "Courses");
        }
    }
}