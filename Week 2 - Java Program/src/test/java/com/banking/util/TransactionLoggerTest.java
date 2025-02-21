package com.banking.util;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import static org.junit.Assert.*;

public class TransactionLoggerTest {
    private TransactionLogger logger;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private static final Path TRANSACTIONS_PATH = Path.of("transactions.txt");

    @Before
    public void setUp() throws Exception {
        System.setOut(new PrintStream(outContent));
        Files.deleteIfExists(TRANSACTIONS_PATH);
    }

    @After
    public void tearDown() throws Exception {
        System.setOut(originalOut);
        Files.deleteIfExists(TRANSACTIONS_PATH);
    }

    @Test
    public void testSaveTransaction() throws Exception {
        // Save a transaction
        logger.saveTransaction("ACC001", new BigDecimal("500.00"));

        // Verify file exists
        assertTrue(Files.exists(TRANSACTIONS_PATH));

        // Verify content
        String content = Files.readString(TRANSACTIONS_PATH);
        assertTrue(content.contains("ACC001"));
        assertTrue(content.contains("500.00"));
    }

    @Test
    public void testGetAllTransactionsWithNoFile() {
        // Ensure no file exists
        assertFalse(Files.exists(TRANSACTIONS_PATH));

        // Get transactions
        List<String> transactions = logger.getAllTransactions();

        // Verify empty list
        assertTrue(transactions.isEmpty());
    }

    @Test
    public void testGetAllTransactionsWithMultipleEntries() throws Exception {
        // Save multiple transactions
        logger.saveTransaction("ACC001", new BigDecimal("500.00"));
        logger.saveTransaction("ACC002", new BigDecimal("-200.00"));

        // Get transactions
        List<String> transactions = logger.getAllTransactions();

        // Verify list contains both transactions
        assertEquals(2, transactions.size());
        assertTrue(transactions.stream().anyMatch(t -> t.contains("ACC001") && t.contains("$500.00")));
        assertTrue(transactions.stream().anyMatch(t -> t.contains("ACC002") && t.contains("$-200.00")));
    }

    @Test
    public void testGetTransactionsForAccount() throws Exception {
        // Save multiple transactions for different accounts
        logger.saveTransaction("ACC001", new BigDecimal("500.00"));
        logger.saveTransaction("ACC002", new BigDecimal("-200.00"));
        logger.saveTransaction("ACC001", new BigDecimal("300.00"));

        // Get transactions for ACC001
        List<String> transactions = logger.getTransactionsForAccount("ACC001");

        // Verify only ACC001 transactions are returned
        assertEquals(2, transactions.size());
        assertTrue(transactions.stream().allMatch(t -> t.contains("ACC001")));
        assertTrue(transactions.stream().noneMatch(t -> t.contains("ACC002")));
    }

    @Test
    public void testTransactionFormatting() throws Exception {
        BigDecimal amount = new BigDecimal("1234.56");
        logger.saveTransaction("TEST123", amount);

        String content = Files.readString(TRANSACTIONS_PATH);

        // Split the content into parts
        String[] parts = content.split(",");

        // Verify we have all parts
        assertEquals("Transaction record should have 3 parts", 3, parts.length);

        // Verify timestamp format (just check basic structure)
        String timestamp = parts[0];
        assertTrue("Timestamp should contain date and time",
            timestamp.contains("-") && timestamp.contains("T") && timestamp.contains(":"));

        // Verify account number
        assertEquals("TEST123", parts[1]);

        // Verify amount (trim newline)
        assertEquals("1234.56", parts[2].trim());

        // Verify account number and amount format
        assertTrue(content.contains("TEST123"));
        assertTrue(content.contains("1234.56"));
    }
}
