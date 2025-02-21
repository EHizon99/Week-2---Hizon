package com.banking.model;

public enum AccountType {
    SAVINGS("SAV"),
    CHECKING("CHK");

    private final String code;

    AccountType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
