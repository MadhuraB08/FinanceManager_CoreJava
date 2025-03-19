//package javacodes;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.Statement;
//import java.util.Scanner;
//
//public class IncomeExpenseTracker {
//
//    // Database connection details
//    private static final String URL = "jdbc:postgresql://localhost:5432/expense_tracker";
//    private static final String USER = "postgres"; // Replace with your PostgreSQL username
//    private static final String PASSWORD = "Test@12345"; // Replace with your PostgreSQL password
//
//    // Method to get a database connection
//    private static Connection getConnection() throws Exception {
//        return DriverManager.getConnection(URL, USER, PASSWORD);
//    }
//
//    // Main method
//    public static void main(String[] args) {
//        Scanner scanner = new Scanner(System.in);
//        while (true) {
//            System.out.println("\n--- Income and Expense Tracker ---");
//            System.out.println("1. Add Transaction");
//            System.out.println("2. View All Transactions");
//            System.out.println("3. Exit");
//            System.out.print("Choose an option: ");
//            int choice = scanner.nextInt();
//            scanner.nextLine(); // Consume newline
//
//            switch (choice) {
//                case 1:
//                    addTransaction(scanner);
//                    break;
//                case 2:
//                    viewAllTransactions();
//                    break;
//                case 3:
//                    System.out.println("Exiting...");
//                    return;
//                default:
//                    System.out.println("Invalid choice. Please try again.");
//            }
//        }
//    }
//
//    // Method to add a transaction
//    private static void addTransaction(Scanner scanner) {
//        try (Connection con = getConnection()) {
//            System.out.print("Enter type (INCOME/EXPENSE): ");
//            String type = scanner.nextLine();
//            System.out.print("Enter amount: ");
//            double amount = scanner.nextDouble();
//            scanner.nextLine(); // Consume newline
//            System.out.print("Enter description: ");
//            String description = scanner.nextLine();
//
//            String query = "INSERT INTO transactions (type, amount, description) VALUES (?, ?, ?)";
//            try (PreparedStatement stmt = con.prepareStatement(query)) {
//                stmt.setString(1, type);
//                stmt.setDouble(2, amount);
//                stmt.setString(3, description);
//                stmt.executeUpdate();
//                System.out.println("Transaction added successfully!");
//            }
//        } catch (Exception e) {
//            System.out.println("Error: " + e.getMessage());
//        }
//    }
//
//    // Method to view all transactions
//    private static void viewAllTransactions() {
//        try (Connection con = getConnection()) {
//            String query = "SELECT * FROM transactions";
//            try (Statement stmt = con.createStatement();
//                 ResultSet rs = stmt.executeQuery(query)) {
//                System.out.println("\n--- All Transactions ---");
//                while (rs.next()) {
//                    System.out.println(
//                        "ID: " + rs.getInt("id") +
//                        ", Type: " + rs.getString("type") +
//                        ", Amount: " + rs.getDouble("amount") +
//                        ", Description: " + rs.getString("description")
//                    );
//                }
//            }
//        } catch (Exception e) {
//            System.out.println("Error: " + e.getMessage());
//        }
//    }
//}




package javacodes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class IncomeExpenseTracker {

    // Database connection details
    private static final String URL = "jdbc:postgresql://localhost:5432/expense_tracker";
    private static final String USER = "postgres"; 
    private static final String PASSWORD = "Test@12345"; 

    // Method to get a database connection
    private static Connection getConnection() throws Exception {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Method to create the table if it doesn't exist
    private static void createTableIfNotExists() {
        try (Connection con = getConnection()) {
            String query = "CREATE TABLE IF NOT EXISTS transactions ("
                         + "id SERIAL PRIMARY KEY, "
                         + "type VARCHAR(10) NOT NULL CHECK (type IN ('INCOME', 'EXPENSE')), "
                         + "amount DECIMAL(10, 2) NOT NULL, "
                         + "description TEXT)";
            try (Statement stmt = con.createStatement()) {
                stmt.executeUpdate(query);
                System.out.println("Table 'transactions' created or already exists.");
            }
        } catch (Exception e) {
            System.out.println("Error creating table: " + e.getMessage());
        }
    }

    // Main method
    public static void main(String[] args) {
        createTableIfNotExists(); // Ensure the table exists
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- Income and Expense Tracker ---");
            System.out.println("1. Add Transaction");
            System.out.println("2. View All Transactions");
            System.out.println("3. Update Transaction");
            System.out.println("4. Delete Transaction");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    addTransaction(scanner);
                    break;
                case 2:
                    viewAllTransactions();
                    break;
                case 3:
                    updateTransaction(scanner);
                    break;
                case 4:
                    deleteTransaction(scanner);
                    break;
                case 5:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // Method to add a transaction
    private static void addTransaction(Scanner scanner) {
        try (Connection con = getConnection()) {
            System.out.print("Enter type (INCOME/EXPENSE): ");
            String type = scanner.nextLine().toUpperCase(); // Convert to uppercase
            System.out.print("Enter amount: ");
            double amount = scanner.nextDouble();
            scanner.nextLine(); // newline
            System.out.print("Enter description: ");
            String description = scanner.nextLine();

            String query = "INSERT INTO transactions (type, amount, description) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = con.prepareStatement(query)) {
                stmt.setString(1, type);
                stmt.setDouble(2, amount);
                stmt.setString(3, description);
                stmt.executeUpdate();
                System.out.println("Transaction added successfully!");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Method to view all transactions
    private static void viewAllTransactions() {
        try (Connection con = getConnection()) {
            String query = "SELECT * FROM transactions";
            try (Statement stmt = con.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {
                System.out.println("\n--- All Transactions ---");
                while (rs.next()) {
                    System.out.println(
                        "ID: " + rs.getInt("id") +
                        ", Type: " + rs.getString("type") +
                        ", Amount: " + rs.getDouble("amount") +
                        ", Description: " + rs.getString("description")
                    );
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Method to update a transaction
    private static void updateTransaction(Scanner scanner) {
        try (Connection con = getConnection()) {
            System.out.print("Enter transaction ID to update: ");
            int id = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            System.out.print("Enter new type (INCOME/EXPENSE): ");
            String type = scanner.nextLine().toUpperCase(); // Convert to uppercase
            System.out.print("Enter new amount: ");
            double amount = scanner.nextDouble();
            scanner.nextLine(); // Consume newline
            System.out.print("Enter new description: ");
            String description = scanner.nextLine();

            String query = "UPDATE transactions SET type = ?, amount = ?, description = ? WHERE id = ?";
            try (PreparedStatement stmt = con.prepareStatement(query)) {
                stmt.setString(1, type);
                stmt.setDouble(2, amount);
                stmt.setString(3, description);
                stmt.setInt(4, id);
                int rowsUpdated = stmt.executeUpdate();
                if (rowsUpdated > 0) {
                    System.out.println("Transaction updated successfully!");
                } else {
                    System.out.println("No transaction found with ID: " + id);
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Method to delete a transaction
    private static void deleteTransaction(Scanner scanner) {
        try (Connection con = getConnection()) {
            System.out.print("Enter transaction ID to delete: ");
            int id = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            String query = "DELETE FROM transactions WHERE id = ?";
            try (PreparedStatement stmt = con.prepareStatement(query)) {
                stmt.setInt(1, id);
                int rowsDeleted = stmt.executeUpdate();
                if (rowsDeleted > 0) {
                    System.out.println("Transaction deleted successfully!");
                } else {
                    System.out.println("No transaction found with ID: " + id);
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}