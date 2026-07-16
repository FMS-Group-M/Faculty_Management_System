package com.faculty.view.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.geom.RoundRectangle2D;

public class RoundedPasswordField extends JPasswordField {
    private int radius;
    private Color unfocusedBorderColor = new Color(200, 200, 200);
    private Color focusedBorderColor = new Color(20, 61, 89);
    private Color currentBorderColor = unfocusedBorderColor;
    private Timer animationTimer;
    private boolean isFocused = false;

    public RoundedPasswordField(int columns, int radius) {
        super(columns);
        this.radius = radius;
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        setFont(new Font("Montserrat", Font.PLAIN, 14));
        setForeground(new Color(50, 50, 50));
        setCaretColor(focusedBorderColor);
        setEchoChar('\u2022'); // Standard circle/bullet password character
        
        animationTimer = new Timer(15, e -> animateBorder());
        
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                isFocused = true;
                animationTimer.start();
            }

            @Override
            public void focusLost(FocusEvent e) {
                isFocused = false;
                animationTimer.start();
            }
        });
    }
    
    private void animateBorder() {
        Color targetColor = isFocused ? focusedBorderColor : unfocusedBorderColor;
        
        float step = 0.15f;
        int r = (int) (currentBorderColor.getRed() + (targetColor.getRed() - currentBorderColor.getRed()) * step);
        int g = (int) (currentBorderColor.getGreen() + (targetColor.getGreen() - currentBorderColor.getGreen()) * step);
        int b = (int) (currentBorderColor.getBlue() + (targetColor.getBlue() - currentBorderColor.getBlue()) * step);
        
        currentBorderColor = new Color(r, g, b);
        repaint();
        
        if (Math.abs(currentBorderColor.getRed() - targetColor.getRed()) <= 2 &&
            Math.abs(currentBorderColor.getGreen() - targetColor.getGreen()) <= 2 &&
            Math.abs(currentBorderColor.getBlue() - targetColor.getBlue()) <= 2) {
            currentBorderColor = targetColor;
            animationTimer.stop();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2.setColor(Color.WHITE);
        g2.fill(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, radius, radius));
        
        super.paintComponent(g);
        g2.dispose();
    }
    
    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(currentBorderColor);
        g2.setStroke(new BasicStroke(isFocused ? 2f : 1f));
        g2.draw(new RoundRectangle2D.Float(1, 1, getWidth() - 3, getHeight() - 3, radius, radius));
        g2.dispose();
    }
}





