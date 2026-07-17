package com.faculty.view.auth;

import com.faculty.view.components.*;
import com.faculty.controller.AuthController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AuthFrame extends JFrame {
    private JPanel rightPanel;
    private CardLayout cardLayout;
    private AuthController controller;

    public AuthFrame() {
        controller = new AuthController(this);
        setTitle("Faculty Management System");
        setSize(850, 550); // Slightly larger for better spacing
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        // Left Panel (Purple)
        JPanel leftPanel = createLeftPanel();
        add(leftPanel, BorderLayout.WEST);

        // Right Panel (White with CardLayout)
        cardLayout = new CardLayout();
        rightPanel = new JPanel(cardLayout);
        
        rightPanel.add(createSignInPanel(), "SignIn");
        rightPanel.add(createSignUpPanel(), "SignUp");
        
        add(rightPanel, BorderLayout.CENTER);
        
        cardLayout.show(rightPanel, "SignIn");
    }

    private JPanel createLeftPanel() {
        GradientPanel panel = new GradientPanel(new Color(20, 61, 89), new Color(20, 61, 89), true); // Deep navy to rich blue gradient
        panel.setPreferredSize(new Dimension(350, 550));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        panel.add(Box.createRigidArea(new Dimension(0, 120)));
        
        JLabel iconLabel = new JLabel("🎓");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 120));
        iconLabel.setForeground(Color.WHITE);
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel titleLabel1 = new JLabel("Faculty Management");
        titleLabel1.setFont(new Font("Montserrat", Font.BOLD, 26));
        titleLabel1.setForeground(Color.WHITE);
        titleLabel1.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel titleLabel2 = new JLabel("System");
        titleLabel2.setFont(new Font("Montserrat", Font.BOLD, 26));
        titleLabel2.setForeground(Color.WHITE);
        titleLabel2.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panel.add(iconLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(titleLabel1);
        panel.add(titleLabel2);
        
        panel.add(Box.createVerticalGlue());
        
        JLabel footer1 = new JLabel("Faculty of Computing & Technology");
        footer1.setFont(new Font("Montserrat", Font.BOLD, 14));
        footer1.setForeground(Color.WHITE);
        footer1.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel footer2 = new JLabel("Manage your academic journey");
        footer2.setFont(new Font("Montserrat", Font.PLAIN, 12));
        footer2.setForeground(new Color(230, 230, 255));
        footer2.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panel.add(footer1);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(footer2);
        panel.add(Box.createRigidArea(new Dimension(0, 40)));
        
        return panel;
    }

    private JPanel createSignInPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Header Tabs
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 0));
        headerPanel.setBackground(Color.WHITE);
        
        JLabel lblSignIn = new JLabel("Sign In");
        lblSignIn.setFont(new Font("Montserrat", Font.BOLD, 22));
        lblSignIn.setForeground(new Color(20, 61, 89));
        lblSignIn.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, new Color(20, 61, 89))); 
        
        JLabel lblSignUp = new JLabel("Sign Up");
        lblSignUp.setFont(new Font("Montserrat", Font.BOLD, 22));
        lblSignUp.setForeground(new Color(180, 180, 180));
        lblSignUp.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblSignUp.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(rightPanel, "SignUp");
            }
        });
        
        headerPanel.add(lblSignIn);
        headerPanel.add(lblSignUp);
        
        gbc.insets = new Insets(0, 0, 30, 0);
        panel.add(headerPanel, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(5, 0, 5, 0);
        panel.add(createLabel("Username"), gbc);
        
        gbc.gridy++;
        RoundedTextField txtUsername = new RoundedTextField(20, 15);
        txtUsername.setPreferredSize(new Dimension(320, 45));
        panel.add(txtUsername, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(15, 0, 5, 0);
        panel.add(createLabel("Password"), gbc);
        
        gbc.gridy++;
        gbc.insets = new Insets(5, 0, 5, 0);
        RoundedPasswordField txtPassword = new RoundedPasswordField(20, 15);
        txtPassword.setPreferredSize(new Dimension(320, 45));
        panel.add(txtPassword, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(15, 0, 5, 0);
        panel.add(createLabel("Role"), gbc);
        
        gbc.gridy++;
        gbc.insets = new Insets(5, 0, 25, 0);
        RoleTogglePanel rolePanel = new RoleTogglePanel();
        rolePanel.setPreferredSize(new Dimension(320, 40));
        panel.add(rolePanel, gbc);

        gbc.gridy++;
        RoundedButton btnLogin = new RoundedButton("Sign In", 15, new Color(20, 61, 89), Color.WHITE);
        btnLogin.setIcon(new ModernIcon(ModernIcon.IconType.LOGOUT, 20, Color.WHITE));
        btnLogin.setIconTextGap(10);
        btnLogin.setPreferredSize(new Dimension(320, 45));
        btnLogin.addActionListener(e -> {
            String username = txtUsername.getText();
            String password = new String(txtPassword.getPassword());
            String role = rolePanel.getSelectedRole();
            controller.handleLogin(username, password, role);
        });
        panel.add(btnLogin, gbc);
        
        return panel;
    }

    private JPanel createSignUpPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 0, 5, 0);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Header Tabs
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 0));
        headerPanel.setBackground(Color.WHITE);
        
        JLabel lblSignIn = new JLabel("Sign In");
        lblSignIn.setFont(new Font("Montserrat", Font.BOLD, 22));
        lblSignIn.setForeground(new Color(180, 180, 180));
        lblSignIn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblSignIn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(rightPanel, "SignIn");
            }
        });
        
        JLabel lblSignUp = new JLabel("Sign Up");
        lblSignUp.setFont(new Font("Montserrat", Font.BOLD, 22));
        lblSignUp.setForeground(new Color(20, 61, 89));
        lblSignUp.setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, new Color(20, 61, 89)));
        
        headerPanel.add(lblSignIn);
        headerPanel.add(lblSignUp);
        
        gbc.insets = new Insets(0, 0, 20, 0);
        panel.add(headerPanel, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(5, 0, 5, 0);
        panel.add(createLabel("Username"), gbc);
        
        gbc.gridy++;
        RoundedTextField txtUsername = new RoundedTextField(20, 15);
        txtUsername.setPreferredSize(new Dimension(320, 40));
        panel.add(txtUsername, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(10, 0, 5, 0);
        panel.add(createLabel("Password"), gbc);
        
        gbc.gridy++;
        gbc.insets = new Insets(5, 0, 5, 0);
        RoundedPasswordField txtPassword = new RoundedPasswordField(20, 15);
        txtPassword.setPreferredSize(new Dimension(320, 40));
        panel.add(txtPassword, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(10, 0, 5, 0);
        panel.add(createLabel("Confirm Password"), gbc);
        
        gbc.gridy++;
        gbc.insets = new Insets(5, 0, 5, 0);
        RoundedPasswordField txtConfirmPassword = new RoundedPasswordField(20, 15);
        txtConfirmPassword.setPreferredSize(new Dimension(320, 40));
        panel.add(txtConfirmPassword, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(10, 0, 5, 0);
        panel.add(createLabel("Role"), gbc);
        
        gbc.gridy++;
        gbc.insets = new Insets(5, 0, 20, 0);
        RoleTogglePanel rolePanel = new RoleTogglePanel();
        rolePanel.setPreferredSize(new Dimension(320, 40));
        panel.add(rolePanel, gbc);

        gbc.gridy++;
        RoundedButton btnRegister = new RoundedButton("Sign Up", 15, new Color(20, 61, 89), Color.WHITE);
        btnRegister.setIcon(new ModernIcon(ModernIcon.IconType.ADD, 20, Color.WHITE));
        btnRegister.setIconTextGap(10);
        btnRegister.setPreferredSize(new Dimension(320, 45));
        btnRegister.addActionListener(e -> {
            String username = txtUsername.getText();
            String password = new String(txtPassword.getPassword());
            String confirmPassword = new String(txtConfirmPassword.getPassword());
            String role = rolePanel.getSelectedRole();
            
            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this, "Passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (role.equals("Student") && !username.matches("^(CT|ET|CS|BT)/20\\d{2}/\\d{3}$")) {
                JOptionPane.showMessageDialog(this, "Student username must be a valid Index (e.g. CT/2022/042, ET/2023/010).", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            controller.handleRegistration(username, password, role);
        });
        panel.add(btnRegister, gbc);
        
        return panel;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Montserrat", Font.BOLD, 13));
        label.setForeground(new Color(100, 100, 100)); // Softer label color
        return label;
    }
}





