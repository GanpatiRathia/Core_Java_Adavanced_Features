package _03_DatabaseProgramming;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/*
/src
│── _03_DatabaseProgramming/
│   ├── _02_DBConnectionProperties.java
│── .env
*/

public class _02_DBConnectionProperties {
    private static final String ENV_PATH = "src/.env";

    public static void main(String[] args) {
        Properties properties = new Properties();

        try (FileInputStream file = new FileInputStream(ENV_PATH)) {
            // Load .env file
            properties.load(file);

            // Read values
            String dbHost = properties.getProperty("DB_URL");
            String dbUser = properties.getProperty("DB_USER");
            String dbPassword = properties.getProperty("DB_PASSWORD");

            // Establish database connection
            try (Connection conn = DriverManager.getConnection(dbHost, dbUser, dbPassword)) {
                System.out.println("✅ Database connected securely!");
                System.out.println("🔗 Connection: " + conn);
            } catch (SQLException e) {
                System.err.println("❌ Database connection failed!");
                e.printStackTrace();
            }

        } catch (IOException e) {
            System.err.println("❌ Error loading .env file: " + e.getMessage());
        }
    }
}
