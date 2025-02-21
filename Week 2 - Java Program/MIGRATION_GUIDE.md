# Converting to H2 Database: A Step-by-Step Guide

This guide will help you convert the file-based banking system to use H2 database.

## Step 1: Add H2 Database Support

1. Add H2 dependency to pom.xml:
   ```xml
   <dependency>
       <groupId>com.h2database</groupId>
       <artifactId>h2</artifactId>
       <version>2.1.214</version>
   </dependency>
   ```

2. Create application.properties in src/main/resources:
   ```properties
   db.url=jdbc:h2:mem:bankingdb;MODE=MySQL
   db.username=sa
   db.password=
   ```

## Step 2: Create Database Tables

Create schema.sql in src/main/resources:

Think about:
- What tables do you need?
- What columns should each table have?
- How are tables related?

Hint: Look at the data being stored in transactions.txt and the Account class.

Exercise:
1. Create an accounts table that stores:
   - Account ID
   - Account Type
   - Balance
2. Create a transactions table that stores:
   - Transaction details
   - Link to account

## Step 3: Database Connection

Create a DatabaseConnection class:
1. Load properties from application.properties
2. Establish database connection
3. Handle connection errors

Key points to implement:
- Singleton pattern for connection management
- Properties loading
- Connection creation
- Error handling

## Step 4: Update TransactionLogger

The current TransactionLogger uses files. Convert it to use the database:

1. Review current implementation:
   - How are transactions stored?
   - What data is being saved?
   - How is data retrieved?

2. Convert to database operations:
   - Replace file operations with SQL
   - Use PreparedStatement for safety
   - Handle database errors

Exercise:
1. Implement logTransaction method using INSERT
2. Implement readTransactionHistory using SELECT

## Step 5: Update AccountService

Modify AccountService to use database:

1. Review current operations:
   - Account creation
   - Deposits/withdrawals
   - Transfers
   - Balance checks

2. Convert each operation:
   - Use transactions for safety
   - Maintain ACID properties
   - Handle errors properly

Exercise:
1. Implement createAccount using database
2. Add proper transaction management
3. Handle rollbacks on errors

## Step 6: Testing

Create tests to verify:
1. Account operations work with database
2. Transactions are properly saved
3. Error cases are handled
4. Data consistency is maintained

Key areas to test:
- Database connection
- Account CRUD operations
- Transaction logging
- Error scenarios

## Common Challenges

1. Transaction Management
   - Problem: Ensuring data consistency
   - Hint: Look into Connection.setAutoCommit()

2. Error Handling
   - Problem: Database errors vs application errors
   - Hint: Create specific exception types

3. Resource Management
   - Problem: Closing connections properly
   - Hint: Use try-with-resources

## Tips for Success

1. Database Operations
   - Always use PreparedStatement
   - Handle resources properly
   - Use transactions for related operations

2. Testing
   - Test database operations thoroughly
   - Verify data consistency
   - Check error scenarios

3. Code Organization
   - Keep database logic separate
   - Use proper exception handling
   - Maintain clean code practices

## Verification Steps

After implementation, verify:
1. Accounts can be created
2. Money can be deposited/withdrawn
3. Transfers work correctly
4. Transaction history is accurate
5. Errors are handled properly

## Learning Resources

1. H2 Database
   - H2 Documentation: http://h2database.com/html/main.html
   - Tutorial: http://www.h2database.com/html/tutorial.html

2. JDBC
   - Oracle JDBC Tutorial
   - JDBC Best Practices

3. SQL
   - Basic SQL Commands
   - Transaction Management
