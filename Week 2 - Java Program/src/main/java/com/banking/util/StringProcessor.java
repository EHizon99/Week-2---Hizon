package com.banking.util;

import java.util.StringTokenizer;
import java.text.NumberFormat;
import java.math.BigDecimal;

public class StringProcessor {
    public void processAccountData(String data) {
        // StringTokenizer for parsing
        StringTokenizer tokenizer = new StringTokenizer(data, ",");
        String name = tokenizer.nextToken().trim();
        String accountType = tokenizer.nextToken().trim();
        String balanceStr = tokenizer.nextToken().trim();

        // StringBuilder for efficiency
        StringBuilder report = new StringBuilder()
            .append("Account Holder: ").append(name).append("\n")
            .append("Account Type: ").append(accountType).append("\n")
            .append("Balance: ").append(formatCurrency(balanceStr));

        // String formatting
        System.out.printf("Created account for %s with %.2f balance%n",
                         name, new BigDecimal(balanceStr));
    }

    private String formatCurrency(String amount) {
        BigDecimal value = new BigDecimal(amount);
        return NumberFormat.getCurrencyInstance()
                          .format(value);
    }
}
