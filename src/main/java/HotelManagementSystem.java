import roles.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class HotelManagementSystem {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n--- Hotel Management System ---");
            System.out.println("1. Guest");
            System.out.println("2. Receptionist");
            System.out.println("3. Administrator");
            System.out.println("4. Housekeeping");
            System.out.println("5. Exit");
            System.out.print("Select your role: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1 -> new Guest().menu();
                case 2 -> new Receptionist().menu();
                case 3 -> new Administrator().menu();
                case 4 -> new Housekeeping().menu();
                case 5 -> {
                    System.out.println("Exiting system. Goodbye!");
                    System.exit(0);
                }
                default -> System.out.println("Invalid option. Try again!");
            }
        }
    }
}
