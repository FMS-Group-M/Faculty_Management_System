package com.faculty.view.components;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class StyledTable extends JTable {
    private int hoveredRow = -1;
    
    public StyledTable() {
        super();
        setupStyles();
    }

    private void setupStyles() {
        setRowHeight(45);
        setShowGrid(false);
        setShowHorizontalLines(true);
        setGridColor(new Color(240, 240, 240)); 
        setFont(new Font("Montserrat", Font.PLAIN, 14));
        setForeground(new Color(50, 50, 50));
        setSelectionBackground(new Color(235, 220, 255));
        setSelectionForeground(new Color(50, 50, 50));
        setIntercellSpacing(new Dimension(0, 0));
        setBorder(BorderFactory.createEmptyBorder());
        
        JTableHeader header = getTableHeader();
        header.setPreferredSize(new Dimension(100, 50));
        header.setFont(new Font("Montserrat", Font.BOLD, 15));
        header.setBackground(Color.WHITE);
        header.setForeground(new Color(20, 61, 89)); // Purple header text
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(230, 230, 230)));
        
        // Custom Renderer for alternating colors and hover effect
        DefaultTableCellRenderer customRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(JLabel.CENTER);
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10)); // Add padding
                
                if (!isSelected) {
                    if (row == hoveredRow) {
                        c.setBackground(new Color(248, 245, 255));
                    } else if (row % 2 == 0) {
                        c.setBackground(Color.WHITE);
                    } else {
                        c.setBackground(new Color(252, 252, 252));
                    }
                }
                return c;
            }
        };
        
        setDefaultRenderer(Object.class, customRenderer);
        setDefaultRenderer(String.class, customRenderer);
        setDefaultRenderer(Integer.class, customRenderer);
        
        // Custom Header Renderer
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(JLabel.CENTER);
                c.setBackground(Color.WHITE);
                c.setFont(new Font("Montserrat", Font.BOLD, 14));
                c.setForeground(new Color(20, 61, 89));
                return c;
            }
        };
        header.setDefaultRenderer(headerRenderer);
        
        // Add hover listener
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int row = rowAtPoint(e.getPoint());
                if (row != hoveredRow) {
                    hoveredRow = row;
                    repaint();
                }
            }
        });
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                hoveredRow = -1;
                repaint();
            }
        });
    }
}





