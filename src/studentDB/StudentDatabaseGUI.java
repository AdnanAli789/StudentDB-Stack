package studentdb;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

public class StudentDatabaseGUI extends JFrame {
    private Stack<Student> studentStack;
    private JTextField idField, nameField, gradeField, attendanceField;
    private JTextArea displayArea;

    // Buttons
    private JButton addBtn, displayBtn, updateBtn, deleteBtn;
    private JButton searchBtn, peekBtn, countBtn, saveBtn, clearBtn;

    public StudentDatabaseGUI() {
        this.studentStack = new Stack<>();
        setTitle("Student Database System (Using Stack)");
        setSize(700, 550); // Slightly increased window size for better accommodation
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ===== Set Look and Feel for Modern Appearance =====
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            // Fallback to default if Nimbus isn't available
        }

        // ===== Colorful Home Background =====
        getContentPane().setBackground(new Color(150, 216, 230)); // Light blue background for the main frame

        // ===== Input Panel =====
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 10, 10)); // Increased gaps for better spacing
        inputPanel.setBackground(new Color(240, 248, 255)); // Light background for input panel
        inputPanel.setBorder(BorderFactory.createTitledBorder("Student Information")); // Add a border for professionalism

        // Create labels with larger, bold font
        Font labelFont = new Font("Arial", Font.BOLD, 18); // Larger and bold font for labels
        JLabel idLabel = new JLabel("ID:");
        idLabel.setFont(labelFont);
        inputPanel.add(idLabel);
        idField = new JTextField();
        idField.setPreferredSize(new Dimension(200, 30)); // Increased size for text fields
        inputPanel.add(idField);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(labelFont);
        inputPanel.add(nameLabel);
        nameField = new JTextField();
        nameField.setPreferredSize(new Dimension(200, 30)); // Increased size for text fields
        inputPanel.add(nameField);

        JLabel gradeLabel = new JLabel("GPA (optional if auto-calc):");
        gradeLabel.setFont(labelFont);
        inputPanel.add(gradeLabel);
        gradeField = new JTextField();
        gradeField.setPreferredSize(new Dimension(200, 30)); // Increased size for text fields
        inputPanel.add(gradeField);

        JLabel attendanceLabel = new JLabel("Attendance:");
        attendanceLabel.setFont(labelFont);
        inputPanel.add(attendanceLabel);
        attendanceField = new JTextField();
        attendanceField.setPreferredSize(new Dimension(200, 30)); // Increased size for text fields
        inputPanel.add(attendanceField);

        // ===== Button Panel =====
        JPanel buttonPanel = new JPanel(new GridLayout(3, 4, 10, 10)); // Increased gaps for better spacing
        buttonPanel.setBackground(new Color(240, 248, 255)); // Match input panel background
        buttonPanel.setBorder(BorderFactory.createTitledBorder("Actions")); // Add a border for professionalism

        addBtn = new JButton("Add Student (Push)");
        displayBtn = new JButton("Display All");
        updateBtn = new JButton("Update");
        deleteBtn = new JButton("Delete (Pop Specific)");
        searchBtn = new JButton("Search");
        peekBtn = new JButton("View Top (Peek)");
        countBtn = new JButton("Count Students");
        saveBtn = new JButton("Save to File");
        clearBtn = new JButton("Clear Display");

        // ===== Customize Buttons: Same Colorful Background for All, Larger Font Size, Smaller Sizes, and Hover Effects =====
        JButton[] buttons = {addBtn, displayBtn, updateBtn, deleteBtn, searchBtn, peekBtn, countBtn, saveBtn, clearBtn};
        Color sameButtonColor = Color.BLUE; // Same color for all buttons (you can change to any color like ORANGE, GREEN, etc.)
        for (JButton btn : buttons) {
            btn.setBackground(sameButtonColor); // Same background color for all
            btn.setForeground(Color.WHITE); // White text for contrast
            btn.setPreferredSize(new Dimension(120, 35)); // Decreased size for attractiveness (smaller buttons)
            btn.setFont(new Font("Arial", Font.BOLD, 16)); // Increased font size for better readability

            // ===== Add Hover Effects =====
            btn.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    btn.setBackground(btn.getBackground().darker()); // Darken on hover for effect
                    btn.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Hand cursor
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    btn.setBackground(btn.getBackground().brighter()); // Revert to original brightness
                }
            });
        }

        // Add buttons to panel
        buttonPanel.add(addBtn);
        buttonPanel.add(displayBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(searchBtn);
        buttonPanel.add(peekBtn);
        buttonPanel.add(countBtn);
        buttonPanel.add(saveBtn);
        buttonPanel.add(clearBtn);

        // ===== Display Area =====
        displayArea = new JTextArea(12, 50);
        displayArea.setEditable(false);
        displayArea.setFont(new Font("Monospaced", Font.PLAIN, 12)); // Monospaced for better alignment
        JScrollPane scrollPane = new JScrollPane(displayArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Output")); // Add a border for professionalism

        // ===== Add to Frame =====
        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);

        // ===== Button Actions =====
        addBtn.addActionListener(e -> addStudent());
        displayBtn.addActionListener(e -> displayStudents());
        updateBtn.addActionListener(e -> updateStudent());
        deleteBtn.addActionListener(e -> deleteStudent());
        searchBtn.addActionListener(e -> searchStudent());
        peekBtn.addActionListener(e -> peekStudent());
        countBtn.addActionListener(e -> countStudents());
        saveBtn.addActionListener(e -> saveToFile());
        clearBtn.addActionListener(e -> displayArea.setText(""));
    }

    // ================= CORE METHODS =================

    private void addStudent() { // Push
        try {
            int id = Integer.parseInt(idField.getText());
            String name = nameField.getText();
            int attendance = Integer.parseInt(attendanceField.getText());

            double grade;
            if (gradeField.getText().isEmpty()) {
                // Auto calculate GPA from 5 subject marks
                double total = 0;
                for (int i = 1; i <= 5; i++) {
                    double marks = Double.parseDouble(JOptionPane.showInputDialog("Enter marks for Subject " + i + " (out of 100):"));
                    total += marks;
                }
                double average = total / 5;
                grade = average / 25.0; // GPA on 4.0 scale
            } else {
                grade = Double.parseDouble(gradeField.getText());
            }

            if (isIdUnique(id)) {
                studentStack.push(new Student(id, name, grade, attendance));
                displayArea.append("✅ Added: " + name + " (GPA: " + String.format("%.2f", grade) + ")\n");
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "⚠️ ID already exists!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid input! Please check values.");
        }
    }

    private void displayStudents() { // Read
        if (studentStack.isEmpty()) {
            displayArea.setText("No students in stack.\n");
            return;
        }
        displayArea.setText("===== Students (Top → Bottom) =====\n");
        Stack<Student> temp = new Stack<>();
        while (!studentStack.isEmpty()) {
            Student s = studentStack.pop();
            displayArea.append(s.toString() + "\n");
            temp.push(s);
        }
        while (!temp.isEmpty()) studentStack.push(temp.pop());
    }

    private void updateStudent() { // Update
        try {
            int id = Integer.parseInt(JOptionPane.showInputDialog("Enter ID to update:"));
            Stack<Student> temp = new Stack<>();
            boolean found = false;

            while (!studentStack.isEmpty()) {
                Student s = studentStack.pop();
                if (s.getId() == id) {
                    s.setName(nameField.getText().isEmpty() ? s.getName() : nameField.getText());
                    if (!gradeField.getText().isEmpty())
                        s.setGrade(Double.parseDouble(gradeField.getText()));
                    if (!attendanceField.getText().isEmpty())
                        s.setAttendance(Integer.parseInt(attendanceField.getText()));
                    found = true;
                }
                temp.push(s);
            }

            while (!temp.isEmpty()) studentStack.push(temp.pop());
            displayArea.append(found ? "✅ Student updated.\n" : "❌ Student not found.\n");
            clearFields();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid input!");
        }
    }

    private void deleteStudent() { // Pop specific
        try {
            int id = Integer.parseInt(JOptionPane.showInputDialog("Enter ID to delete:"));
            Stack<Student> temp = new Stack<>();
            boolean found = false;

            while (!studentStack.isEmpty()) {
                Student s = studentStack.pop();
                if (s.getId() == id) {
                    found = true;
                    continue;
                }
                temp.push(s);
            }

            while (!temp.isEmpty()) studentStack.push(temp.pop());
            displayArea.append(found ? "🗑️ Student deleted.\n" : "❌ Student not found.\n");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid input!");
        }
    }

    private void searchStudent() {
        try {
            int id = Integer.parseInt(JOptionPane.showInputDialog("Enter ID to search:"));
            Stack<Student> temp = new Stack<>();
            boolean found = false;

            while (!studentStack.isEmpty()) {
                Student s = studentStack.pop();
                if (s.getId() == id) {
                    displayArea.append("🔍 Found → " + s.toString() + "\n");
                    found = true;
                }
                temp.push(s);
            }

            while (!temp.isEmpty()) studentStack.push(temp.pop());
            if (!found) JOptionPane.showMessageDialog(this, "Student not found!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid input!");
        }
    }

    private void peekStudent() {
        if (studentStack.isEmpty())
            displayArea.append("Stack is empty.\n");
        else
            displayArea.append("👆 Top Student → " + studentStack.peek().toString() + "\n");
    }

    private void countStudents() {
        displayArea.append("📊 Total Students in Stack: " + studentStack.size() + "\n");
    }

    private void sortByGrade() {
        if (studentStack.isEmpty()) {
            displayArea.setText("No students to sort.\n");
            return;
        }

        java.util.List<Student> list = new java.util.ArrayList<Student>();


        list.sort(Comparator.comparingDouble(Student::getGrade).reversed());

        displayArea.setText("===== Students Sorted by GPA (High → Low) =====\n");
        for (Student s : list)
            displayArea.append(s.toString() + "\n");
    }

    private void saveToFile() {
        try (PrintWriter pw = new PrintWriter(new FileWriter("students.txt"))) {
            for (Student s : studentStack)
                pw.println(s.getId() + "," + s.getName() + "," + s.getGrade() + "," + s.getAttendance());
            JOptionPane.showMessageDialog(this, "💾 Data saved to students.txt!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving file!");
        }
    }

    private void loadFromFile() {
        try (Scanner sc = new Scanner(new File("students.txt"))) {
            studentStack.clear();
            while (sc.hasNextLine()) {
                String[] data = sc.nextLine().split(",");
                int id = Integer.parseInt(data[0]);
                String name = data[1];
                double grade = Double.parseDouble(data[2]);
                int attendance = Integer.parseInt(data[3]);
                studentStack.push(new Student(id, name, grade, attendance));
            }
            JOptionPane.showMessageDialog(this, "📂 Data loaded successfully!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading file!");
        }
    }

    // ================= HELPERS =================
    private boolean isIdUnique(int id) {
        for (Student s : studentStack)
            if (s.getId() == id) return false;
        return true;
    }

    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        gradeField.setText("");
        attendanceField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StudentDatabaseGUI().setVisible(true));
    }
}
