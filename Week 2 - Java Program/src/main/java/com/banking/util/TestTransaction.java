package com.banking.util;

//public class TestTransaction {
//
//    package com.banking.util;
//
//import com.banking.db.DatabaseConnection;
//
//import java.math.BigDecimal;
//import java.sql.*;
//import java.time.LocalDateTime;
//

//public class TestTransactionLogger {
//
//    // Save transaction to the H2 database
//    public static void saveTransaction(String accountNumber, BigDecimal amount) {
//        String sql = "INSERT INTO transactions (account_id, amount, transaction_date) VALUES (?, ?, ?)";
//
//        try (Connection conn = DatabaseConnection.getConnection() ;
//             PreparedStatement stmt = conn.prepareStatement(sql)) {
//
//            stmt.setString(1, accountNumber);
//            stmt.setBigDecimal(2, amount);
//            stmt.setObject(3, LocalDateTime.now());
//
//            stmt.executeUpdate();
//            System.out.println(" Transaction saved successfully!");
//
//        } catch (SQLException e) {
//            System.err.println(" Could not save transaction: " + e.getMessage());
//        }
//    }
//
//    // Show all transactions from the database
//    public static void showAllTransactions() {
//        String sql = "SELECT account_id, amount, transaction_date FROM transactions";
//
//        try (Connection conn = DatabaseConnection.getConnection();
//             PreparedStatement stmt = conn.prepareStatement(sql);
//             ResultSet rs = stmt.executeQuery()) {
//
//            if (!rs.isBeforeFirst()) {
//                System.out.println("No transactions found.");
//                return;
//            }
//
//            System.out.println("\n Transaction History:");
//            System.out.println("-----------------------");
//            while (rs.next()) {
//                System.out.printf(" %s |  Account: %s |  Amount: $%.2f%n",
//                        rs.getTimestamp("transaction_date"),
//                        rs.getString("account_id"),
//                        rs.getBigDecimal("amount"));
//            }
//
//        } catch (SQLException e) {
//            System.err.println(" Could not retrieve transactions: " + e.getMessage());
//        }
//    }
//
//    // Clear all transactions from the database
//    public static void clearTransactions() {
//        String sql = "DELETE FROM transactions";
//
//        try (Connection conn = DatabaseConnection.getConnection();
//             PreparedStatement stmt = conn.prepareStatement(sql)) {
//
//            int rowsDeleted = stmt.executeUpdate();
//            System.out.println(" Deleted " + rowsDeleted + " transactions.");
//
//        } catch (SQLException e) {
//            System.err.println(" Could not clear transactions: " + e.getMessage());
//        }
//    }
//}

