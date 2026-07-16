package com.faculty.view.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

public class RoundedButton extends JButton {
    private int radius;
    private Color hoverBackgroundColor;
    private Color normalBackgroundColor;
    private Color pressedBackgroundColor;
    private Color currentBgColor;
    
    private Timer animationTimer;
    private float animationProgress = 0f;
    private boolean isHovered = false;
    private boolean isPressed = false;

    public RoundedButton(String text, int radius, Color bgColor, Color fgColor) {
        super(text);
        this.radius = radius;
        this.normalBackgroundColor = bgColor;
        this.hoverBackgroundColor = lighten(bgColor, 0.15f);
        this.pressedBackgroundColor = darken(bgColor, 0.1f);
        this.currentBgColor = bgColor;
        
        setForeground(fgColor);
        super.setBackground(bgColor);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
        setBorderPainted(false);
        setFont(new Font("Montserrat", Font.BOLD, 14));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        animationTimer = new Timer(15, e -> animateColor());

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                isHovered = true;
                animationTimer.start();
            }

            @Override
            public void mouseExited(MouseEvent evt) {
                isHovered = false;
                isPressed = false;
                animationTimer.start();
            }
            
            @Override
            public void mousePressed(MouseEvent evt) {
                if (SwingUtilities.isLeftMouseButton(evt)) {
                    isPressed = true;
                    animationTimer.start();
                }
            }
            
            @Override
            public void mouseReleased(MouseEvent evt) {
                if (SwingUtilities.isLeftMouseButton(evt)) {
                    isPressed = false;
                    animationTimer.start();
                }
            }
        });
    }

    private void animateColor() {
        Color targetColor = isPressed ? pressedBackgroundColor : (isHovered ? hoverBackgroundColor : normalBackgroundColor);
        
        float step = 0.15f;
        int r = (int) (currentBgColor.getRed() + (targetColor.getRed() - currentBgColor.getRed()) * step);
        int g = (int) (currentBgColor.getGreen() + (targetColor.getGreen() - currentBgColor.getGreen()) * step);
        int b = (int) (currentBgColor.getBlue() + (targetColor.getBlue() - currentBgColor.getBlue()) * step);
        
        currentBgColor = new Color(r, g, b);
        repaint();
        
        if (Math.abs(currentBgColor.getRed() - targetColor.getRed()) <= 2 &&
            Math.abs(currentBgColor.getGreen() - targetColor.getGreen()) <= 2 &&
            Math.abs(currentBgColor.getBlue() - targetColor.getBlue()) <= 2) {
            currentBgColor = targetColor;
            animationTimer.stop();
        }
    }
    
    private Color lighten(Color color, float fraction) {
        int red = (int) Math.min(255, color.getRed() + 255 * fraction);
        int green = (int) Math.min(255, color.getGreen() + 255 * fraction);
        int blue = (int) Math.min(255, color.getBlue() + 255 * fraction);
        return new Color(red, green, blue, color.getAlpha());
    }

    private Color darken(Color color, float fraction) {
        int red = (int) Math.max(0, color.getRed() - 255 * fraction);
        int green = (int) Math.max(0, color.getGreen() - 255 * fraction);
        int blue = (int) Math.max(0, color.getBlue() - 255 * fraction);
        return new Color(red, green, blue, color.getAlpha());
    }

    @Override
    public void setBackground(Color bg) {
        super.setBackground(bg);
        this.normalBackgroundColor = bg;
        if (bg != null) {
            this.hoverBackgroundColor = lighten(bg, 0.15f);
            this.pressedBackgroundColor = darken(bg, 0.1f);
            this.currentBgColor = bg;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g2.setColor(currentBgColor);
        g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), radius, radius));
        
        // Slightly shift text down if pressed
        if (isPressed) {
            g2.translate(0, 1);
        }
        
        super.paintComponent(g2);
        g2.dispose();
    }
}





