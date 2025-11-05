package studentdb;

public class Student {
    private int id;
    private String name;
    private double grade;  // e.g., GPA or average grade
    private int attendance;  // e.g., days attended

    // Constructor
    public Student(int id, String name, double grade, int attendance) {
        this.id = id;
        this.name = name;
        this.grade = grade;
        this.attendance = attendance;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getGrade() { return grade; }
    public void setGrade(double grade) { this.grade = grade; }
    public int getAttendance() { return attendance; }
    public void setAttendance(int attendance) { this.attendance = attendance; }

    // ToString for display
    @Override
    public String toString() {
        return "ID: " + id + ", Name: " + name + ", Grade: " + grade + ", Attendance: " + attendance;
    }
}