package com.project.extickets.model;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class TransactionTest {

    @Test
    void testGettersAndSetters() {
        // Arrange
        Transaction transaction = new Transaction();

        Long transactionId = 10L;
        Long ticketId = 5L;
        String status = "approved";
        LocalDateTime updatedAt = LocalDateTime.of(2025, 10, 4, 14, 30);

        // Act
        transaction.setTransactionId(transactionId);
        transaction.setTicketId(ticketId);
        transaction.setStatus(status);
        transaction.setUpdatedAt(updatedAt);

        // Assert
        assertEquals(transactionId, transaction.getTransactionId());
        assertEquals(ticketId, transaction.getTicketId());
        assertEquals(status, transaction.getStatus());
        assertEquals(updatedAt, transaction.getUpdatedAt());
    }

    @Test
    void testDefaultValuesShouldBeNull() {
        Transaction transaction = new Transaction();

        assertNull(transaction.getTransactionId());
        assertNull(transaction.getTicketId());
        assertNull(transaction.getStatus());
        assertNull(transaction.getUpdatedAt());
    }
}
