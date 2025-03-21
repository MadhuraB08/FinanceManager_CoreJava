package javacodes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class IncomeExpenseTracker {

    // Method which creates table if not exist 
    private static void createTableIfNotExists(String dbName) { //argument dbName 
        try (Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + dbName, "postgres", "Test@12345")) {
            String query = "CREATE TABLE IF NOT EXISTS transactions ("
                         + "id SERIAL PRIMARY KEY, "
                         + "type VARCHAR(10) NOT NULL CHECK (type IN ('INCOME', 'EXPENSE')), "
                         + "amount DECIMAL(10, 2) NOT NULL, "
                         + "description TEXT)";
            try (Statement stmt = con.createStatement()) //object of statement 
            {
                stmt.executeUpdate(query);   //executes the query
                System.out.println("Table 'transactions' created or already exists.");
            }
        } catch (Exception e) {
            System.out.println("Error creating table: " + e.getMessage());
        }
    }

    
    
    // Main method
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter database name: ");
        String dbName = scanner.nextLine();
        createTableIfNotExists(dbName); //checking if db exists 

        while (true)   //infinity loop until user selects exit option 
        {
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
                    addTransaction(scanner, dbName);
                    break;
                case 2:
                    viewAllTransactions(dbName);
                    break;
                case 3:
                    updateTransaction(scanner, dbName);
                    break;
                case 4:
                    deleteTransaction(scanner, dbName);
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
    private static void addTransaction(Scanner scanner, String dbName) {
        try (Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + dbName, "postgres", "Test@12345")) {
            System.out.print("Enter type (INCOME/EXPENSE): ");
            String type = scanner.nextLine().toUpperCase(); // converting input into uppercase 
            System.out.print("Enter amount: ");
            double amount = scanner.nextDouble();
            scanner.nextLine(); // used to clear the input buffer and ensure the next scanner.nextLine() works as expected.
            System.out.print("Enter description: ");
            String description = scanner.nextLine();

            String query = "INSERT INTO transactions (type, amount, description) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = con.prepareStatement(query)) //PreparedStatement:Precompiled SQLstatements. 
            {
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
    private static void viewAllTransactions(String dbName) {
        try (Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + dbName, "postgres", "Test@12345"))
        //DriverManager: Manages database drivers and establishes connections
        {
            String query = "SELECT * FROM transactions";
            try (Statement stmt = con.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) //ResultSet: Stores and processes query results.
            {
                System.out.println("\n--- All Transactions ---");
                while (rs.next()) //rs.next(): Moves the cursor to the next row. Returns true if a row exists.
                	//while (rs.next()): loop till rs.next has rows to read next . if row there returns true,no rows= false,end of prgm
                {
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
    private static void updateTransaction(Scanner scanner, String dbName) {
        try (Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + dbName, "postgres", "Test@12345")) {
            System.out.print("Enter transaction ID to update: ");
            int id = scanner.nextInt();
            scanner.nextLine(); // newline
            System.out.print("Enter new type (INCOME/EXPENSE): ");
            String type = scanner.nextLine().toUpperCase(); // Convert to uppercase
            System.out.print("Enter new amount: ");
            double amount = scanner.nextDouble();
            scanner.nextLine(); //  newline
            System.out.print("Enter new description: ");
            String description = scanner.nextLine();

            String query = "UPDATE transactions SET type = ?, amount = ?, description = ? WHERE id = ?";
            try (PreparedStatement stmt = con.prepareStatement(query))  //object of prepared statement 
             {
                stmt.setString(1, type);
                stmt.setDouble(2, amount);
                stmt.setString(3, description);
                stmt.setInt(4, id);
                int rowsUpdated = stmt.executeUpdate(); //returns the number of rows affected.
                if (rowsUpdated > 0)  //checks if rows updated or not 
                {
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
    private static void deleteTransaction(Scanner scanner, String dbName) {
        try (Connection con = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + dbName, "postgres", "Test@12345")) {
            System.out.print("Enter transaction ID to delete: ");
            int id = scanner.nextInt();
            scanner.nextLine(); //newline

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

