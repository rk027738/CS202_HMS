package roles;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import utils.DatabaseUtils.*;

import static utils.DatabaseUtils.*;


public class Guest {
    private Scanner scanner = new Scanner(System.in);

    public void menu() {
        while (true) {
            System.out.println("\n--- Guest Menu ---");
            System.out.println("1. View Available Rooms");
            System.out.println("2. Add New Booking");
            System.out.println("3. View My Bookings");
            System.out.println("4. Cancel Booking");
            System.out.println("5. Logout");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1 -> viewAvailableRooms();
                case 2 -> addBooking();
                case 3 -> viewMyBookings();
                case 4 -> cancelBooking();
                case 5 -> { return; }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void viewAvailableRooms() {
        String query = "SELECT * FROM Room WHERE RoomStatus = 'Available'";
        try (Connection conn = getConnection();
             ResultSet rs = executeQuery(conn, query)) {
            System.out.println("Available Rooms:");
            while (rs.next()) {
                System.out.printf("Room: %s | Type: %s | Price: %.2f\n",
                        rs.getString("RoomNumber"), rs.getString("RoomType"), rs.getDouble("Price"));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching rooms: " + e.getMessage());
        }
    }

//    private void addBooking() {
//        System.out.print("Enter Room Number to Book: ");
//        int roomNumber = scanner.nextInt();
//        System.out.print("Enter Guest Name: ");
//        String guestName = scanner.next();
//
//        String query = "INSERT INTO Booking (RoomNumber, GuestName) VALUES (?, ?)";
//        try (Connection conn = getConnection()) {
//            int rows = executeUpdate(conn, query, roomNumber, guestName);
//            if (rows > 0) System.out.println("Booking added successfully!");
//        } catch (SQLException e) {
//            System.err.println("Error adding booking: " + e.getMessage());
//        }
//    }

    private void addBooking() {
        System.out.print("Enter Room Number to Book: ");
        String roomNumber = scanner.next();
        System.out.print("Enter Guest/User ID: ");
        int userId = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter Check-in Date (YYYY-MM-DD): ");
        String checkInDate = scanner.nextLine();
        System.out.print("Enter Check-out Date (YYYY-MM-DD): ");
        String checkOutDate = scanner.nextLine();
        System.out.print("Enter Number of Guests: ");
        int numberOfGuests = scanner.nextInt();

        String overlapQuery = "SELECT COUNT(*) AS OverlapCount " +
                "FROM Booking b " +
                "JOIN Room r ON b.RoomId = r.RoomId " +
                "WHERE r.RoomNumber = ? " +
                "  AND b.BookingStatus = 'Confirmed' " +
                "  AND (b.CheckInDate < ? AND b.CheckOutDate > ?)";

        String insertQuery = "INSERT INTO Booking (RoomId, UserId, CheckInDate, CheckOutDate, NumberOfGuests, BookingStatus) " +
                "SELECT RoomId, ?, ?, ?, ?, 'Pending' FROM Room WHERE RoomNumber = ?";

        try (Connection conn = getConnection()) {
            // Check for overlapping bookings
            ResultSet rs = executeQuery(conn, overlapQuery, roomNumber, checkOutDate, checkInDate);
            if (rs.next() && rs.getInt("OverlapCount") > 0) {
                System.out.println("Room is already booked for the requested date range.");
                return;
            }

            // Proceed with booking if no overlaps
            int rows = executeUpdate(conn, insertQuery, userId, checkInDate, checkOutDate, numberOfGuests, roomNumber);
            if (rows > 0) {
                System.out.println("Booking added successfully!");
            } else {
                System.out.println("No matching room found.");
            }
        } catch (SQLException e) {
            System.err.println("Error adding booking: " + e.getMessage());
        }
    }




    private void viewMyBookings() {
        System.out.print("Enter your name: ");
        String name = scanner.next();
        String query = "SELECT * FROM Booking WHERE GuestName = ?";

        try (Connection conn = getConnection();
             ResultSet rs = executeQuery(conn, query, name)) {
            while (rs.next()) {
                System.out.printf("Booking ID: %d | Room: %s | Status: %s\n",
                        rs.getInt("BookingId"), rs.getString("RoomNumber"), rs.getString("Status"));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving bookings: " + e.getMessage());
        }
    }

    private void cancelBooking() {
        System.out.print("Enter Booking ID to Cancel: ");
        int bookingId = scanner.nextInt();
        String query = "DELETE FROM Booking WHERE BookingId = ?";

        try (Connection conn = getConnection()) {
            int rows = executeUpdate(conn, query, bookingId);
            if (rows > 0) System.out.println("Booking cancelled successfully.");
        } catch (SQLException e) {
            System.err.println("Error cancelling booking: " + e.getMessage());
        }
    }
}
