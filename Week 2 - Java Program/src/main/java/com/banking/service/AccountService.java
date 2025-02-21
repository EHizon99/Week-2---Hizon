package com.banking.service;

import com.banking.BankingSystem;
import com.banking.Main;
import com.banking.model.Account;
import com.banking.model.AccountFactory;
import com.banking.model.AccountType;
import com.banking.exception.*;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


/**
 * This class handles all banking operations like:
 * - Creating new accounts
 * - Depositing money
 * - Withdrawing money
 * - Transferring between accounts
 */
public class AccountService {
    // We need these to work with accounts and save transactions
    private final BankingSystem bankingSystem;

    // Make sure these are set (e.g., from application.properties) BEFORE this class is instantiated
    private static final String URL     = "jdbc:h2:mem:bankingdb;MODE=MySQL;DB_CLOSE_DELAY=-1";
    private static final String USER    = "sa";
    private static final String PASSWORD= "";

    Connection conn;

    {
        try {
            conn = DriverManager.getConnection(URL,USER,PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // When we create AccountService, we need a BankingSystem
    public AccountService(BankingSystem bankingSystem) {
        this.bankingSystem = bankingSystem;

    }

    /**
     * Create a new bank account
     */
    public Account createAccount(AccountType type, String accountId, BigDecimal initialBalance)
            throws BankingException {

        // Validate
        if (initialBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new BankingException("Initial balance cannot be negative");
        }
        if (accountId == null || accountId.isEmpty()) {
            throw new BankingException("Account ID cannot be empty");
        }

        // Create the account
        Account account = AccountFactory.createAccount(type, accountId, initialBalance);

        try (PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO accounts (account_id, account_type, balance) VALUES (?, ?, ?)")) {

            stmt.setString(1, accountId);
            stmt.setString(2, type.name());  // or use type.toString(), etc.
            stmt.setBigDecimal(3, initialBalance);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new BankingException("Failed to create account in DB" );
        }

        // Save it in the banking system
        bankingSystem.addAccount(account);

        return account;
    }

    /**
     * Deposit money into an account
     */
    public void deposit(String accountId, BigDecimal amount) throws BankingException {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BankingException("Deposit amount must be positive");
        }

        try (PreparedStatement stmt = conn.prepareStatement(
                "UPDATE accounts SET balance = balance + ? WHERE account_id = ?")) {
            stmt.setBigDecimal(1, amount);
            stmt.setString(2, accountId);

            int rows = stmt.executeUpdate();
            if (rows == 0) {
                throw new AccountNotFoundException("No account found with ID " + accountId);
            }
        } catch (SQLException e) {
            throw new BankingException("Failed to deposit into DB: " + e.getMessage(), e);
        }

        Account account = findAccount(accountId);
        account.deposit(amount);

        // Save the transaction with the current timestamp
        recordTransaction(accountId, amount, new Timestamp(System.currentTimeMillis()));
    }

    /**
     * Withdraw money from an account
     */
    public void withdraw(String accountId, BigDecimal amount) throws BankingException {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BankingException("Withdrawal amount must be positive");
        }

        try {
            BigDecimal currentBalance = getBalance(accountId);
            if (currentBalance.compareTo(amount) < 0) {
                throw new BankingException("Insufficient balance in account " + accountId);
            }

            try (PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE accounts SET balance = balance - ? WHERE account_id = ?")) {
                stmt.setBigDecimal(1, amount);
                stmt.setString(2, accountId);

                int rows = stmt.executeUpdate();
                if (rows == 0) {
                    throw new AccountNotFoundException("No account found with ID " + accountId);
                }
            }

            Account account = findAccount(accountId);
            account.withdraw(amount);

            // Save the transaction with the current timestamp
            recordTransaction(accountId, amount.negate(), new Timestamp(System.currentTimeMillis()));

        } catch (SQLException e) {
            throw new BankingException("Failed to withdraw from DB: " + e.getMessage(), e);
        }
    }

    /**
     * Transfer money between accounts
     */
    public void transfer(String fromAccountId, String toAccountId, BigDecimal amount) throws BankingException {

        double doubleAmount = amount.doubleValue();

        if ((getBalance(toAccountId).doubleValue())-doubleAmount<0) {
            System.out.println("\nFailed to transfer with invalid amount.");
            Main.mainMenu();
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BankingException("Transfer amount must be positive");
        }

        Timestamp transactionTimestamp = new Timestamp(System.currentTimeMillis());

        try {
            conn.setAutoCommit(false);

            try (PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE accounts SET balance = balance - ? WHERE account_id = ?")) {
                stmt.setBigDecimal(1, amount);
                stmt.setString(2, fromAccountId);
                int rows = stmt.executeUpdate();
                if (rows == 0) {
                    throw new AccountNotFoundException("No account found with ID " + fromAccountId);
                }
            }

            try (PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE accounts SET balance = balance + ? WHERE account_id = ?")) {
                stmt.setBigDecimal(1, amount);
                stmt.setString(2, toAccountId);
                int rows = stmt.executeUpdate();
                if (rows == 0) {
                    throw new AccountNotFoundException("No account found with ID " + toAccountId);
                }
            }

            conn.commit();


            // Find both accounts and update them
            Account fromAccount = findAccount(fromAccountId);
            Account toAccount = findAccount(toAccountId);

            fromAccount.withdraw(amount);
            toAccount.deposit(amount);

            // Save both transactions with the same timestamp
            recordTransaction(fromAccountId, amount.negate(), transactionTimestamp);
            recordTransaction(toAccountId, amount, transactionTimestamp);

        } catch (SQLException e) {

            try {
                conn.rollback();
            } catch (SQLException rollbackEx) {
                // Log rollback failure
            }
            throw new BankingException("Failed to transfer in DB: " + e.getMessage(), e);

        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException ex) {
                // Log auto-commit reset failure
            }
        }
    }

    /**
     * Get the balance of an account
     */
    public BigDecimal getBalance(String accountId) throws AccountNotFoundException {
        BigDecimal balance = BigDecimal.ZERO;

        try (PreparedStatement stmt = conn.prepareStatement(
                "SELECT balance FROM accounts WHERE account_id = ?")) {
            stmt.setString(1, accountId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    balance = rs.getBigDecimal("balance");
                } else {
                    throw new AccountNotFoundException("No account found with ID " + accountId);
                }
            }
        } catch (SQLException e) {
            throw new AccountNotFoundException("Error retrieving balance: " + e.getMessage(), e);
        }

        return balance;
    }
    public String viewAccount (String accountId) throws BankingException {
        String query = "SELECT account_id, account_type, balance FROM accounts WHERE account_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, accountId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return  "Account ID: " + rs.getString("account_id") + "\n" +
                        "Account Type: " + rs.getString("account_type") + "\n" +
                        "Balance: $" + rs.getBigDecimal("balance");
            } else {
                throw new AccountNotFoundException("No account found with ID: " + accountId);
            }
        } catch (SQLException e) {
            throw new BankingException("Failed to retrieve account details: " + e.getMessage(), e);
        }
    }

    /**
     * Get all transactions in the system
     */
    public List<String> getAllTransactions() {
        List<String> transactions = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(
                "SELECT account_id, amount, transaction_date FROM transactions ORDER BY transaction_date ASC");
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String accId = rs.getString("account_id");
                BigDecimal amount = rs.getBigDecimal("amount");
                Timestamp ts = rs.getTimestamp("transaction_date");
                transactions.add("Account: " + accId + ", Amount: " + amount + ", Date: " + ts);
            }
        } catch (SQLException e) {
            // In a real app, handle properly or rethrow
            System.err.println("Failed to fetch all transactions: " + e.getMessage());
        }

        return transactions;
    }

    private Account findAccount(String accountId) throws AccountNotFoundException {
        if (accountId == null || accountId.isEmpty()) {
            throw new AccountNotFoundException("Account ID cannot be empty");
        }
        return bankingSystem.findAccount(accountId);
    }
    /**
     * Helper method to insert a transaction row in the DB.
     */
    public void recordTransaction(String accountId, BigDecimal amount, Timestamp transactionDate) throws BankingException {
        try (PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO transactions (account_id, amount, transaction_date) VALUES (?, ?, ?)")) {
            stmt.setString(1, accountId);
            stmt.setBigDecimal(2, amount);
            stmt.setTimestamp(3, transactionDate);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new BankingException("Failed to record transaction: " + e.getMessage(), e);
        }
    }

}
