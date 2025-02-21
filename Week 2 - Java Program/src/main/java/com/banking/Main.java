package com.banking;


import com.banking.model.*;
import com.banking.service.AccountService;
import com.banking.exception.*;

import java.math.BigDecimal;
import java.util.Scanner;

import static com.banking.db.DatabaseConnection.ExecuteSQL;
import static com.banking.db.DatabaseConnection.startH2Server;


/**
 * A simple banking application that shows basic account operations.
 * This program demonstrates:
 * 1. How to create bank accounts
 * 2. How to deposit money
 * 3. How to withdraw money
 * 4. How to transfer between accounts
 * 5. How to handle errors
 */
public class Main {
    public static void main(String[] args) {

        // Start H2 Web Console
        startH2Server();

        // Execute SQL
        ExecuteSQL();

        //Execute Main menu
        mainMenu();

    }

    public static void mainMenu() {

        Scanner scanner = new Scanner(System.in);
        BankingSystem bankingSystem = new BankingSystem();
        AccountService accountService = new AccountService(bankingSystem);
        while (true) {
            System.out.println("\n=== Banking System Menu ===");
            System.out.println("1. Create Account");
            System.out.println("2. View Account");
            System.out.println("3. Deposit");
            System.out.println("4. Withdraw");
            System.out.println("5. Transfer");
            System.out.println("6. View Transactions");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");

            int choice = 0;
            try {
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
            } catch (Exception e) {
                System.out.println("Invalid Input!!!");
                mainMenu();
            }

            try {
                switch (choice) {
                    case 1: // Create Account
                        System.out.print("Enter Account ID: ");
                        String accountId = scanner.nextLine();
                        System.out.print("Enter Account Type (SAVINGS/CHECKING): ");
                        String accountTypeStr = scanner.nextLine().toUpperCase();
                        System.out.print("Enter Initial Balance: ");
                        BigDecimal initialBalance = null;
                        try {
                            initialBalance = scanner.nextBigDecimal();
                            scanner.nextLine(); // Consume newline
                        } catch (Exception e) {
                            System.out.println("Invalid Input!!!");
                            mainMenu();
                        }

                        AccountType accountType = AccountType.valueOf(accountTypeStr);
                        accountService.createAccount(accountType, accountId, initialBalance);
                        System.out.println("✅ Account created successfully!");
                        break;

                    case 2: // View Account
                        System.out.print("Enter Account ID: ");
                        String viewAccountId = scanner.nextLine();
                        String accountDetails = accountService.viewAccount(viewAccountId);
                        System.out.println("\n=== Account Details ===");
                        System.out.println(accountDetails);
                        break;

                    case 3: // Deposit
                        System.out.print("Enter Account ID: ");
                        String depositAccountId = scanner.nextLine();
                        System.out.print("Enter Deposit Amount: ");
                        BigDecimal depositAmount = null;
                        try {
                            depositAmount = scanner.nextBigDecimal();
                            scanner.nextLine(); // Consume newline
                        } catch (Exception e) {
                            System.out.println("Invalid Input!!!");
                            mainMenu();
                        }

                        accountService.deposit(depositAccountId, depositAmount);
                        System.out.println("✅ Deposit successful!");
                        break;

                    case 4: // Withdraw
                        System.out.print("Enter Account ID: ");
                        String withdrawAccountId = scanner.nextLine();
                        System.out.print("Enter Withdrawal Amount: ");
                        BigDecimal withdrawAmount = null;
                        try {
                            withdrawAmount = scanner.nextBigDecimal();
                            scanner.nextLine(); // Consume newline
                        } catch (Exception e) {
                            System.out.println("Invalid Input!!!");
                            mainMenu();
                        }
                        accountService.withdraw(withdrawAccountId, withdrawAmount);
                        System.out.println("✅ Withdrawal successful!");
                        break;

                    case 5: // Transfer
                        System.out.print("Enter Sender Account ID: ");
                        String fromAccountId = scanner.nextLine();
                        System.out.print("Enter Receiver Account ID: ");
                        String toAccountId = scanner.nextLine();
                        System.out.print("Enter Transfer Amount: ");
                        BigDecimal transferAmount = null;
                        try {
                            transferAmount = scanner.nextBigDecimal();
                            scanner.nextLine(); // Consume newline
                        } catch (Exception e) {
                            System.out.println("Invalid Input!!!");
                            mainMenu();
                        }

                        accountService.transfer(fromAccountId, toAccountId, transferAmount);
                        System.out.println("✅ Transfer successful!");
                        break;

                    case 6: // View Transactions
                        System.out.println("\n=== Transactions ===");
                        accountService.getAllTransactions().forEach(System.out::println);
                        break;

                    case 7: // Exit
                        System.out.println("\nThank you for your time!");
                        System.exit(0);
                        break;

                    default:
                        System.out.println("❌ Invalid choice. Please try again.");
                }
            } catch (BankingException e) {
                System.out.println("⚠️ Error: " + e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println("⚠️ Invalid account type. Use SAVINGS or CHECKING.");
            }
        }





    }

}
























