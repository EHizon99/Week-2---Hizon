package com.banking.exception;

import java.math.BigDecimal;

public class InsufficientFundsException extends BankingException {
    private final String accountNumber;
    private final BigDecimal requestedAmount;
    private final BigDecimal availableBalance;

    public InsufficientFundsException(String accountNumber, BigDecimal requestedAmount, BigDecimal availableBalance) {
        super(String.format("Insufficient funds in account %s: requested %.2f, available %.2f",
                          accountNumber, requestedAmount, availableBalance));
        this.accountNumber = accountNumber;
        this.requestedAmount = requestedAmount;
        this.availableBalance = availableBalance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public BigDecimal getRequestedAmount() {
        return requestedAmount;
    }

    public BigDecimal getAvailableBalance() {
        return availableBalance;
    }
}
