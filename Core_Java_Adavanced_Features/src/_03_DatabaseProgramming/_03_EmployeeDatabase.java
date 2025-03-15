package _03_DatabaseProgramming;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class _03_EmployeeDatabase {
    private static final String ENV_PATH = "src/.env";

    public static void main(String[] args) {
        Properties properties = new Properties();
        try (FileInputStream file = new FileInputStream(ENV_PATH)) {
            properties.load(file);
            String dbUrl = properties.getProperty("DB_URL");
            String dbUser = properties.getProperty("DB_USER");
            String dbPassword = properties.getProperty("DB_PASSWORD");

            try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
                 Scanner scanner = new Scanner(System.in)) {
                 
                System.out.println("✅ Connected to Database!");

                boolean exit = false;
                while (!exit) {
                    System.out.println("\n====== Employee Database Menu ======");
                    System.out.println("1. Create Table");
                    System.out.println("2. Insert Employee");
                    System.out.println("3. View Employees");
                    System.out.println("4. Update Employee");
                    System.out.println("5. Delete Employee");
                    System.out.println("6. Exit");
                    System.out.print("Enter your choice: ");
                    int choice = scanner.nextInt();
                    scanner.nextLine(); // Consume newline

                    switch (choice) {
                        case 1 -> createTable(conn);
                        case 2 -> insertEmployee(conn, scanner);
                        case 3 -> viewEmployees(conn);
                        case 4 -> updateEmployee(conn, scanner);
                        case 5 -> deleteEmployee(conn, scanner);
                        case 6 -> {
                            exit = true;
                            System.out.println("Exiting...");
                        }
                        default -> System.out.println("❌ Invalid choice! Please try again.");
                    }
                }

            } catch (SQLException e) {
                System.err.println("❌ Database Error: " + e.getMessage());
            }

        } catch (IOException e) {
            System.err.println("❌ Error loading .env file: " + e.getMessage());
        }
    }

    // 1️⃣ Create Employee Table
    private static void createTable(Connection conn) throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS Employee (
                id INT AUTO_INCREMENT PRIMARY KEY,
                name VARCHAR(100) NOT NULL,
                age INT NOT NULL,
                salary DECIMAL(10,2) NOT NULL
            )
        """;
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
            System.out.println("✅ Employee table created successfully!");
        }
    }

    // 2️⃣ Insert Employee Record
    private static void insertEmployee(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter Employee Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Employee Age: ");
        int age = scanner.nextInt();
        System.out.print("Enter Employee Salary: ");
        double salary = scanner.nextDouble();

        String sql = "INSERT INTO Employee (name, age, salary) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, age);
            pstmt.setDouble(3, salary);
            pstmt.executeUpdate();
            System.out.println("✅ Employee added successfully!");
        }
    }

    // 3️⃣ View Employee Records
    private static void viewEmployees(Connection conn) throws SQLException {
        String sql = "SELECT * FROM Employee";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
             
            System.out.println("\n📌 Employee Records:");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") +
                        ", Name: " + rs.getString("name") +
                        ", Age: " + rs.getInt("age") +
                        ", Salary: $" + rs.getDouble("salary"));
            }
        }
    }

    // 4️⃣ Update Employee Record
    private static void updateEmployee(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter Employee ID to update: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter new Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter new Age: ");
        int age = scanner.nextInt();
        System.out.print("Enter new Salary: ");
        double salary = scanner.nextDouble();

        String sql = "UPDATE Employee SET name = ?, age = ?, salary = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setInt(2, age);
            pstmt.setDouble(3, salary);
            pstmt.setInt(4, id);
            int rows = pstmt.executeUpdate();
            if (rows > 0) System.out.println("✅ Employee updated successfully!");
            else System.out.println("❌ Employee not found.");
        }
    }

    // 5️⃣ Delete Employee Record
    private static void deleteEmployee(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter Employee ID to delete: ");
        int id = scanner.nextInt();

        String sql = "DELETE FROM Employee WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rows = pstmt.executeUpdate();
            if (rows > 0) System.out.println("✅ Employee deleted successfully!");
            else System.out.println("❌ Employee not found.");
        }
    }
}
