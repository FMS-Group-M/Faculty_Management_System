package com.faculty.view.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class ModernDialog extends JDialog {
    private boolean isOk = false;
    private int pX, pY;

    public ModernDialog(Window owner, String title, String[] labels, JComponent[] fields) {
        super(owner, title, ModalityType.APPLICATION_MODAL);
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0));

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Allow dragging
        mainPanel.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent me) {
                pX = me.getX();
                pY = me.getY();
            }
        });
        mainPanel.addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent me) {
                setLocation(getLocation().x + me.getX() - pX, getLocation().y + me.getY() - pY);
            }
        });

        // Title
        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("Montserrat", Font.BOLD, 22));
        titleLbl.setForeground(new Color(20, 61, 89));
        
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.add(titleLbl, BorderLayout.WEST);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        mainPanel.add(titlePanel, BorderLayout.NORTH);

        // Form fields
        JPanel formPanel = new JPanel(new GridLayout(labels.length, 2, 15, 15));
        formPanel.setOpaque(false);
        for (int i = 0; i < labels.length; i++) {
            JLabel lbl = new JLabel(labels[i]);
            lbl.setFont(new Font("Montserrat", Font.BOLD, 14));
            lbl.setForeground(new Color(60, 70, 80));
            formPanel.add(lbl);
            
            if (fields[i] != null) {
                formPanel.add(fields[i]);
            } else {
                formPanel.add(new JLabel(""));
            }
        }
        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(25, 0, 0, 0));

        RoundedButton btnCancel = new RoundedButton("Cancel", 20, new Color(240, 242, 245), new Color(100, 110, 120));
        btnCancel.setFont(new Font("Montserrat", Font.BOLD, 14));
        btnCancel.setPreferredSize(new Dimension(100, 40));
        btnCancel.addActionListener(e -> {
            isOk = false;
            dispose();
        });

        RoundedButton btnOk = new RoundedButton("Save", 20, new Color(20, 61, 89), Color.WHITE);
        btnOk.setFont(new Font("Montserrat", Font.BOLD, 14));
        btnOk.setPreferredSize(new Dimension(100, 40));
        btnOk.addActionListener(e -> {
            isOk = true;
            dispose();
        });

        buttonPanel.add(btnCancel);
        buttonPanel.add(btnOk);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Wrapper to add border and rounded corners
        JPanel wrapper = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.setStroke(new BasicStroke(1.5f));
                g2.setColor(new Color(180, 190, 200));
                g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 30, 30);
                g2.dispose();
            }
        };
        wrapper.setOpaque(false);
        wrapper.add(mainPanel, BorderLayout.CENTER);
        
        setContentPane(wrapper);
        pack();
        setLocationRelativeTo(owner);
    }

    public boolean isOk() {
        return isOk;
    }
}





