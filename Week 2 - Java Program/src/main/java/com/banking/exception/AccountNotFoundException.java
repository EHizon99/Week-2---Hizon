package com.banking.exception;

public class AccountNotFoundException extends BankingException {
    private final String accountNumber;

    public AccountNotFoundException(String accountNumber) {
        super(String.format("Account not found: %s", accountNumber));
        this.accountNumber = accountNumber;
    }

    public AccountNotFoundException(String accountNumber, Throwable cause) {
        super(String.format("Account not found: %s", accountNumber), cause);
        this.accountNumber = accountNumber;
    }

    public String getAccountNumber() {
        return accountNumber;
    }
}
