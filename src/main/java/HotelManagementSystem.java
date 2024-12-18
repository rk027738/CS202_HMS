import utils.DatabaseUtils;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class HotelManagementSystem {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n--- Welcome to the Hotel Management System ---");
            System.out.print("Enter Username: ");
            String username = scanner.nextLine(); // Use nextLine to capture the entire line

            System.out.print("Enter Password: ");
            String password = scanner.nextLine(); // Use nextLine to capture the password

            String query = "SELECT UserType FROM User WHERE Name = ? AND Password = ?";

            try (Connection conn = DatabaseUtils.getConnection();
                 ResultSet rs = DatabaseUtils.executeQuery(conn, query, username, password)) {

                if (rs.next()) {
                    String userType = rs.getString("UserType");
                    System.out.println("Login successful! Welcome, " + username + " (" + userType + ").");
                    navigateToRoleMenu(userType);
                } else {
                    System.out.println("Invalid username or password. Please try again.");
                }

            } catch (SQLException e) {
                System.err.println("Database error: " + e.getMessage());
            }
        }
    }


    private static void navigateToRoleMenu(String userType) {
        switch (userType) {
            case "Guest" -> new roles.Guest().menu();
            case "Receptionist" -> new roles.Receptionist().menu();
            case "Admin" -> new roles.Administrator().menu();
            case "Housekeeper" -> new roles.Housekeeping().menu();
            default -> System.out.println("Unknown role: " + userType);
        }
    }
}
