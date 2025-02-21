package com.banking.model;

import java.math.BigDecimal;

public interface Transferable {
    boolean transfer(Account destination, BigDecimal amount);

    // Default method
    default boolean canTransfer() {
        return true;
    }
}
