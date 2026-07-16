package com.faculty.view.components;

import javax.swing.*;
import java.awt.*;

public class StatCard extends CardPanel {
    private JLabel lblValue;
    private JLabel lblTitle;
    private ModernIcon icon;

    public StatCard(String title, String value, ModernIcon.IconType iconType, Color iconColor, Color iconBg) {
        super(20, Color.WHITE);
        setLayout(new BorderLayout(15, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        // Icon Circle
        JPanel iconPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(iconBg);
                g2.fillOval(0, 0, getWidth() - 1, getHeight() - 1);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        iconPanel.setOpaque(false);
        iconPanel.setPreferredSize(new Dimension(50, 50));
        iconPanel.setLayout(new GridBagLayout()); // Center icon
        
        icon = new ModernIcon(iconType, 24, iconColor);
        JLabel iconLabel = new JLabel(icon);
        iconPanel.add(iconLabel);
        
        // Text Area
        JPanel textPanel = new JPanel();
        textPanel.setOpaque(false);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        
        lblValue = new JLabel(value);
        lblValue.setFont(new Font("Montserrat", Font.BOLD, 24));
        lblValue.setForeground(new Color(40, 40, 40));
        
        lblTitle = new JLabel(title.toUpperCase());
        lblTitle.setFont(new Font("Montserrat", Font.BOLD, 11));
        lblTitle.setForeground(new Color(130, 130, 130));
        
        textPanel.add(lblValue);
        textPanel.add(Box.createRigidArea(new Dimension(0, 2)));
        textPanel.add(lblTitle);
        
        add(iconPanel, BorderLayout.WEST);
        add(textPanel, BorderLayout.CENTER);
        
        setPreferredSize(new Dimension(220, 90));
    }
}





