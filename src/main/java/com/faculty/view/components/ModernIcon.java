package com.faculty.view.components;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

public class ModernIcon implements Icon {
    
    public enum IconType {
        ADD, EDIT, DELETE, SAVE, LOGOUT, PROFILE, TIMETABLE, COURSES, DEGREES, DEPARTMENTS, LECTURERS, STUDENTS
    }
    
    private IconType type;
    private int width;
    private int height;
    private Color color;

    public ModernIcon(IconType type, int size, Color color) {
        this.type = type;
        this.width = size;
        this.height = size;
        this.color = color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g2.translate(x, y);
        g2.setColor(color);
        
        float strokeWidth = Math.max(1.5f, width / 12f);
        g2.setStroke(new BasicStroke(strokeWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        
        int padding = width / 6;
        int w = width - padding * 2;
        int h = height - padding * 2;
        int cx = width / 2;
        int cy = height / 2;
        
        switch (type) {
            case ADD:
                g2.drawLine(cx, padding, cx, height - padding);
                g2.drawLine(padding, cy, width - padding, cy);
                break;
                
            case EDIT:
                // Pencil shape
                Path2D pencil = new Path2D.Float();
                pencil.moveTo(width - padding, padding + w*0.2f);
                pencil.lineTo(padding + w*0.2f, height - padding);
                pencil.lineTo(padding, height - padding);
                pencil.lineTo(padding, height - padding - h*0.2f);
                pencil.lineTo(width - padding - w*0.2f, padding);
                pencil.closePath();
                g2.draw(pencil);
                g2.drawLine((int)(width - padding - w*0.2f), padding, width - padding, (int)(padding + w*0.2f));
                break;
                
            case DELETE:
                // Trash can
                g2.drawRect(padding + w/5, padding + h/4, w - (w/5)*2, h - h/4);
                g2.drawLine(padding, padding + h/4, width - padding, padding + h/4); // lid
                g2.drawLine(cx - w/6, padding, cx + w/6, padding); // handle
                g2.drawLine(cx, padding, cx, padding + h/4);
                break;
                
            case SAVE:
                // Floppy disk
                Path2D disk = new Path2D.Float();
                disk.moveTo(padding, padding);
                disk.lineTo(width - padding - w/4, padding);
                disk.lineTo(width - padding, padding + h/4);
                disk.lineTo(width - padding, height - padding);
                disk.lineTo(padding, height - padding);
                disk.closePath();
                g2.draw(disk);
                g2.drawRect(cx - w/4, height - padding - h/2, w/2, h/2); // bottom label
                break;
                
            case LOGOUT:
                // Door with arrow
                g2.drawRect(padding, padding, w/2, h);
                g2.drawLine(cx, cy, width - padding, cy); // arrow line
                g2.drawLine(width - padding - w/4, cy - h/4, width - padding, cy); // arrow head top
                g2.drawLine(width - padding - w/4, cy + h/4, width - padding, cy); // arrow head bot
                break;
                
            case PROFILE:
            case STUDENTS:
                // Person
                g2.drawOval(cx - w/4, padding, w/2, h/2 - padding/2);
                Path2D shoulders = new Path2D.Float();
                shoulders.moveTo(padding, height - padding);
                shoulders.curveTo(padding, cy + padding, width - padding, cy + padding, width - padding, height - padding);
                g2.draw(shoulders);
                break;
                
            case TIMETABLE:
                // Calendar
                g2.drawRect(padding, padding + h/5, w, h - h/5);
                g2.drawLine(padding, padding + h/2, width - padding, padding + h/2); // header line
                g2.drawLine(cx - w/4, padding, cx - w/4, padding + h/3); // ring 1
                g2.drawLine(cx + w/4, padding, cx + w/4, padding + h/3); // ring 2
                break;
                
            case COURSES:
                // Book
                g2.drawRect(padding, padding, w, h);
                g2.drawLine(cx, padding, cx, height - padding); // spine
                g2.drawLine(padding + w/4, padding + h/4, cx - w/8, padding + h/4); // lines
                g2.drawLine(cx + w/8, padding + h/4, width - padding - w/4, padding + h/4);
                break;
                
            case DEGREES:
                // Graduation cap
                Path2D cap = new Path2D.Float();
                cap.moveTo(cx, padding);
                cap.lineTo(width - padding, cy - padding);
                cap.lineTo(cx, height - padding*2);
                cap.lineTo(padding, cy - padding);
                cap.closePath();
                g2.draw(cap);
                g2.drawLine(width - padding, cy - padding, width - padding, height - padding); // tassel
                break;
                
            case DEPARTMENTS:
                // Building
                g2.drawRect(padding, cy, w, h/2);
                Path2D roof = new Path2D.Float();
                roof.moveTo(padding - w/8, cy);
                roof.lineTo(cx, padding);
                roof.lineTo(width - padding + w/8, cy);
                roof.closePath();
                g2.draw(roof);
                g2.drawRect(cx - w/8, cy + h/4, w/4, h/4); // door
                break;
                
            case LECTURERS:
                // Teacher at podium (simplistic)
                g2.drawOval(cx - w/6, padding, w/3, h/3); // head
                g2.drawLine(cx, padding + h/3, cx, cy + padding); // body
                g2.drawRect(padding, cy + padding, w, h/3); // podium
                break;
        }
        
        g2.dispose();
    }

    @Override
    public int getIconWidth() {
        return width;
    }

    @Override
    public int getIconHeight() {
        return height;
    }
}
