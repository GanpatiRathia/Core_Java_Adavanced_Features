package _03_DatabaseProgramming;

import java.sql.*;

public class _01_SecureDatabaseConnection {
	public static void main(String[] args) {
        // Load environment variables
		String url = "Your Database Host";
        String user = "Your Database User";
        String password = "Your Database Password";
        
        System.out.println(url);
        System.out.println(user);
//        System.out.println(password);

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Database connected securely! \nConnection : " + conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
