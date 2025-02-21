package com.banking.model;

import java.math.BigDecimal;

public class CheckingAccount extends Account {
    private static final BigDecimal MONTHLY_FEE = new BigDecimal("12.00");
    private static final BigDecimal OVERDRAFT_LIMIT = new BigDecimal("-100.00");
    private int monthlyTransactions;

    public CheckingAccount(String accountNumber, BigDecimal balance) {
        super(accountNumber, balance);
        this.monthlyTransactions = 0;
    }

    @Override
    public void processMonthlyFees() {
        // Deduct monthly maintenance fee
        withdraw(MONTHLY_FEE);
        // Reset monthly transaction count
        monthlyTransactions = 0;
    }

    @Override
    protected boolean canWithdraw(BigDecimal amount) {
        // Allow withdrawals up to overdraft limit
        return getBalance().subtract(amount)
                         .compareTo(OVERDRAFT_LIMIT) >= 0;
    }

    @Override
    public void withdraw(BigDecimal amount) {
        super.withdraw(amount);
        monthlyTransactions++;
    }

    @Override
    public void deposit(BigDecimal amount) {
        super.deposit(amount);
        monthlyTransactions++;
    }

    public int getMonthlyTransactions() {
        return monthlyTransactions;
    }

    @Override
    public String toString() {
        return String.format("CheckingAccount[number=%s, balance=%.2f, transactions=%d]",
                           getAccountNumber(),
                           getBalance(),
                           monthlyTransactions);
    }
}
