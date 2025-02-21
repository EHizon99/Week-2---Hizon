# Week 1: Basic Banking System

A simple banking system implementation to learn fundamental Java concepts.

## Overview

This project implements basic banking operations:
- Account creation
- Deposits
- Withdrawals
- Transfers
- Transaction logging

## Prerequisites

- Java 11 or higher
- Maven 3.6+

## Getting Started

1. Build the project:
   ```bash
   mvn clean compile
   ```

2. Run the application:
   ```bash
   mvn exec:java -Dexec.mainClass="com.banking.Main"
   ```

## Project Structure

```
week1-solutions/
├── src/main/java/com/banking/
│   ├── model/                    # Account implementations
│   │   ├── Account.java         # Base account class
│   │   ├── AccountFactory.java  # Account creation factory
│   │   ├── AccountType.java     # Account type enums
│   │   ├── CheckingAccount.java # Checking account implementation
│   │   ├── SavingsAccount.java  # Savings account implementation
│   │   └── Transferable.java    # Transfer capability interface
│   ├── exception/               # Error handling
│   │   ├── AccountNotFoundException.java     # Account lookup errors
│   │   ├── BankingException.java            # Base exception class
│   │   └── InsufficientFundsException.java  # Balance errors
│   ├── service/                 # Business logic
│   │   └── AccountService.java  # Account operations handler
│   ├── util/                    # Helper utilities
│   │   ├── StringProcessor.java    # String manipulation utils
│   │   └── TransactionLogger.java  # Transaction history logger
│   ├── BankingSystem.java      # Core banking logic
│   └── Main.java               # Demo application
└── pom.xml                     # Project configuration
```

## Core Features

1. Account Types
   - Savings Account
   - Checking Account

2. Basic Operations
   - Create accounts
   - Process deposits
   - Handle withdrawals
   - Execute transfers

3. Safety Features
   - Insufficient funds protection
   - Invalid account validation
   - Negative amount prevention

4. Transaction Tracking
   - Transaction history storage
   - Account-specific history
   - Complete transaction log

## Usage Examples

1. Creating Accounts
   ```java
   // Create a savings account with $1000
   Account savings = accountService.createAccount(
       AccountType.SAVINGS,
       "SAV001",
       new BigDecimal("1000.00")
   );
   ```

2. Making Deposits
   ```java
   // Deposit $500
   accountService.deposit("SAV001", new BigDecimal("500.00"));
   ```

3. Processing Withdrawals
   ```java
   // Withdraw $300
   accountService.withdraw("CHK001", new BigDecimal("300.00"));
   ```

4. Executing Transfers
   ```java
   // Transfer $200 between accounts
   accountService.transfer("CHK001", "SAV001", new BigDecimal("200.00"));
   ```

## Testing

Run the test suite:
```bash
# All tests
mvn test

# Specific test class
mvn test -Dtest=AccountServiceTest

# Specific test method
mvn test -Dtest=AccountServiceTest#testSuccessfulTransfer
```

## Troubleshooting

1. Environment Verification
   ```bash
   # Verify Java version
   java -version

   # Verify Maven version
   mvn -version
   ```

2. Common Issues and Solutions

   a. Java Version Mismatch
   - Problem: Incorrect Java version
   - Solution: Install Java 11 or higher

   b. Maven Build Failures
   - Problem: Build errors
   - Solution:
     ```bash
     mvn clean
     mvn dependency:purge-local-repository
     mvn clean install
     ```

   c. Runtime Errors
   - Problem: ClassNotFoundException
   - Solution: Ensure proper compilation
     ```bash
     mvn clean compile
     ```

## Key Concepts

1. Java Fundamentals
   - Classes and Objects
   - Methods and Properties
   - Exception Handling
   - File I/O Operations

2. Development Practices
   - Code Organization
   - Error Handling
   - Clean Code Principles
   - Unit Testing

## Future Enhancements

1. Planned Improvements
   - Database Integration
   - User Interface
   - Additional Account Types
   - Enhanced Transaction Features

2. Learning Path
   - Week 2: Database Integration
   - Week 3: Advanced Features
   - Final Project: Complete Banking System

## Support

1. Setup Issues
   - Verify Java installation
   - Confirm Maven setup
   - Check project directory structure

2. Build Problems
   - Clean project: `mvn clean`
   - Update dependencies: `mvn dependency:update`
   - Rebuild: `mvn clean install`

3. Runtime Issues
   - Verify Java version
   - Check classpath
   - Review error logs


## Version Control

A .gitignore file is included to exclude:
- Compiled files (target/, *.class)
- IDE settings (.idea/, .vscode/)
- Database files (*.db)
- Local config (application.properties)
- Log files (*.log, transactions.txt)

This helps keep your repository clean when using version control.
