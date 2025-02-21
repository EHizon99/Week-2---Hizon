package com.banking.model;

import java.math.BigDecimal;

public class AccountFactory {
    public static Account createAccount(AccountType type, String number, BigDecimal balance) {
        switch (type) {
            case SAVINGS:
                return new SavingsAccount(number, balance);
            case CHECKING:
                return new CheckingAccount(number, balance);
            default:
                throw new IllegalArgumentException("Unknown account type: " + type);
        }
    }

    public static Account createSavingsAccount(String number, BigDecimal balance, BigDecimal interestRate) {
        return new SavingsAccount(number, balance, interestRate);
    }

    private AccountFactory() {
        // Private constructor to prevent instantiation
        // This class only provides static factory methods
    }
}
