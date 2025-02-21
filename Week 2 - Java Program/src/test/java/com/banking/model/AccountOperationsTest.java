package com.banking.model;

import org.junit.Before;
import org.junit.Test;
import java.math.BigDecimal;
import static org.junit.Assert.*;
import com.banking.exception.InsufficientFundsException;

public class AccountOperationsTest {
    private SavingsAccount savingsAccount;
    private CheckingAccount checkingAccount;

    @Before
    public void setUp() {
        savingsAccount = new SavingsAccount("SAV001", new BigDecimal("1000.00"));
        checkingAccount = new CheckingAccount("CHK001", new BigDecimal("500.00"));
    }

    @Test
    public void testAccountCreation() {
        assertEquals("SAV001", savingsAccount.getAccountNumber());
        assertEquals(new BigDecimal("1000.00"), savingsAccount.getBalance());
        assertEquals("CHK001", checkingAccount.getAccountNumber());
        assertEquals(new BigDecimal("500.00"), checkingAccount.getBalance());
    }

    @Test
    public void testDeposit() {
        savingsAccount.deposit(new BigDecimal("500.00"));
        assertEquals(new BigDecimal("1500.00"), savingsAccount.getBalance());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDepositNegativeAmount() {
        savingsAccount.deposit(new BigDecimal("-100.00"));
    }

    @Test
    public void testSuccessfulWithdrawal() {
        savingsAccount.withdraw(new BigDecimal("500.00"));
        assertEquals(new BigDecimal("500.00"), savingsAccount.getBalance());
    }

    @Test(expected = InsufficientFundsException.class)
    public void testWithdrawInsufficientFunds() {
        savingsAccount.withdraw(new BigDecimal("2000.00"));
    }

    @Test
    public void testMonthlyFeeProcessing() {
        // Test savings account interest
        BigDecimal initialBalance = savingsAccount.getBalance();
        savingsAccount.processMonthlyFees();
        assertTrue(savingsAccount.getBalance().compareTo(initialBalance) > 0);

        // Test checking account fees
        BigDecimal checkingInitialBalance = checkingAccount.getBalance();
        checkingAccount.processMonthlyFees();
        assertTrue(checkingAccount.getBalance().compareTo(checkingInitialBalance) < 0);
    }

    @Test
    public void testCheckingAccountTransactionCount() {
        checkingAccount.deposit(new BigDecimal("100.00"));
        checkingAccount.withdraw(new BigDecimal("50.00"));
        assertEquals(2, checkingAccount.getMonthlyTransactions());

        // Process monthly fees should reset transaction count
        checkingAccount.processMonthlyFees();
        assertEquals(0, checkingAccount.getMonthlyTransactions());
    }
}
