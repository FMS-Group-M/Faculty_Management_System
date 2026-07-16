package com.faculty.view.components;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxUI;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class RoundedComboBox<E> extends JComboBox<E> {
    private int radius;
    private Color borderColor = new Color(200, 200, 200);

    public RoundedComboBox(int radius) {
        super();
        this.radius = radius;
        setOpaque(false);
        setFont(new Font("Montserrat", Font.PLAIN, 14));
        setBackground(Color.WHITE);
        setForeground(new Color(50, 50, 50));
        
        setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        
        setUI(new BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton button = new JButton("▼");
                button.setFont(new Font("Montserrat", Font.BOLD, 10));
                button.setForeground(new Color(150, 150, 150));
                button.setContentAreaFilled(false);
                button.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
                button.setFocusPainted(false);
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
                return button;
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fill(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, radius, radius));
        super.paintComponent(g);
        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(borderColor);
        g2.setStroke(new BasicStroke(1f));
        g2.draw(new RoundRectangle2D.Float(1, 1, getWidth() - 3, getHeight() - 3, radius, radius));
        g2.dispose();
    }
}





