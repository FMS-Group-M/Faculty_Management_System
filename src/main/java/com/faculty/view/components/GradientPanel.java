package com.faculty.view.components;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;

public class GradientPanel extends JPanel {
    
    private Color startColor;
    private Color endColor;
    private boolean isDiagonal;
    private int cornerRadius = 0; // default 0 for sharp corners

    public GradientPanel(Color startColor, Color endColor, boolean isDiagonal) {
        this.startColor = startColor;
        this.endColor = endColor;
        this.isDiagonal = isDiagonal;
        setOpaque(false); // Make sure the gradient shows through
    }
    
    public GradientPanel(Color startColor, Color endColor, boolean isDiagonal, int cornerRadius) {
        this(startColor, endColor, isDiagonal);
        this.cornerRadius = cornerRadius;
    }

    public void setColors(Color startColor, Color endColor) {
        this.startColor = startColor;
        this.endColor = endColor;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int w = getWidth();
        int h = getHeight();
        
        Point2D start = new Point2D.Float(0, 0);
        Point2D end;
        
        if (isDiagonal) {
            end = new Point2D.Float(w, h);
        } else {
            end = new Point2D.Float(0, h);
        }
        
        LinearGradientPaint paint = new LinearGradientPaint(
            start, end, 
            new float[]{0.0f, 1.0f}, 
            new Color[]{startColor, endColor}
        );
        
        g2.setPaint(paint);
        
        if (cornerRadius > 0) {
            g2.fillRoundRect(0, 0, w, h, cornerRadius, cornerRadius);
        } else {
            g2.fillRect(0, 0, w, h);
        }
        
        g2.dispose();
    }
}





