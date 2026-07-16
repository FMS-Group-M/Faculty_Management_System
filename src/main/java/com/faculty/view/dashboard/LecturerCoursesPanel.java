package com.faculty.view.dashboard;

import com.faculty.controller.CourseController;
import com.faculty.controller.LecturerController;
import com.faculty.model.Course;
import com.faculty.model.Lecturer;
import com.faculty.model.User;
import com.faculty.view.components.ModernIcon;
import com.faculty.view.components.RoundedButton;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class LecturerCoursesPanel extends JPanel {
    private DefaultTableModel tableModel;
    private JTable table;
    private CourseController courseController;
    private Lecturer currentLecturer;

    private List<Course> allCourses;
    private List<Course> addedList = new ArrayList<>();
    private List<Course> editedList = new ArrayList<>();
    private List<Course> deletedList = new ArrayList<>();

    public LecturerCoursesPanel(User currentUser) {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(245, 247, 250));
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        courseController = new CourseController();
        LecturerController lecturerController = new LecturerController();
        currentLecturer = lecturerController.getLecturerByUserId(currentUser.getId());

        com.faculty.view.components.GradientPanel banner = new com.faculty.view.components.GradientPanel(new Color(20, 61, 89), new Color(20, 61, 89), false, 20);
        banner.setLayout(new BorderLayout());
        banner.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        JLabel title = new JLabel("My Lectures");
        title.setFont(new Font("Montserrat", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        
        JLabel subtitle = new JLabel("View the timetable of courses you are currently assigned to teach.");
        subtitle.setFont(new Font("Montserrat", Font.PLAIN, 14));
        subtitle.setForeground(new Color(220, 235, 255));
        
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);
        titlePanel.add(title);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 5)));
        titlePanel.add(subtitle);
        
        banner.add(titlePanel, BorderLayout.WEST);
        
        JPanel bannerWrapper = new JPanel(new BorderLayout());
        bannerWrapper.setOpaque(false);
        bannerWrapper.add(banner, BorderLayout.CENTER);
        
        // Main Content Area
        JPanel mainContent = new JPanel(new BorderLayout(0, 10));
        mainContent.setOpaque(false);
        mainContent.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        String[] columns = {"Time", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
        Object[][] data = {
            {"08.00", "OOP", "OOP", "OOP", "OOP", "OOP"},
            {"10.00", "OOP", "OOP", "OOP", "OOP", "OOP"},
            {"INTERVAL", "Interval", "Interval", "Interval", "Interval", "Interval"},
            {"01.00", "SE", "OOP", "SE", "SE", "SE"},
            {"03.00", "SE", "OOP", "SE", "SE", "SE"}
        };
        table = new com.faculty.view.components.StyledTable() {
            @Override
            public void paint(Graphics g) {
                super.paint(g);
                if (getRowCount() > 2) {
                    Rectangle rect = getCellRect(2, 0, true);
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                    g2d.setColor(Color.WHITE);
                    g2d.setFont(new Font("Montserrat", Font.BOLD, 16));
                    FontMetrics fm = g2d.getFontMetrics();
                    String text = "Interval";
                    int textWidth = fm.stringWidth(text);
                    int x = (getWidth() - textWidth) / 2;
                    int y = rect.y + ((rect.height - fm.getHeight()) / 2) + fm.getAscent();
                    g2d.drawString(text, x, y);
                    g2d.dispose();
                }
            }
        };
        table.setModel(new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
        
        ((javax.swing.table.DefaultTableCellRenderer)table.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
        javax.swing.table.TableCellRenderer existingRenderer = table.getDefaultRenderer(Object.class);
        table.setDefaultRenderer(Object.class, new javax.swing.table.TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = existingRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (c instanceof JLabel) {
                    ((JLabel)c).setHorizontalAlignment(SwingConstants.CENTER);
                }
                if (row == 2) { 
                    c.setBackground(new Color(20, 61, 89)); 
                    c.setForeground(Color.WHITE);
                    if (c instanceof JLabel) {
                        ((JLabel)c).setText("");
                    }
                    c.setFont(new Font("Montserrat", Font.BOLD, 16));
                } else {
                    c.setForeground(new Color(50, 50, 50));
                    c.setFont(new Font("Montserrat", Font.PLAIN, 14));
                }
                return c;
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        com.faculty.view.components.CardPanel tableContainer = new com.faculty.view.components.CardPanel(20, Color.WHITE);
        tableContainer.setLayout(new BorderLayout());
        tableContainer.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        tableContainer.add(scrollPane, BorderLayout.CENTER);
        
        mainContent.add(tableContainer, BorderLayout.CENTER);

        add(bannerWrapper, BorderLayout.NORTH);
        add(mainContent, BorderLayout.CENTER);
    }
}





