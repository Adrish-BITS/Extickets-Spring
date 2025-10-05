package com.project.extickets.model;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class TicketWithStatusTest {

    @Test
    void testGettersAndSettersIncludingInheritedFields() {
        // Arrange
        TicketWithStatus ticket = new TicketWithStatus();

        String id = "T001";
        String eventName = "Rock Festival";
        LocalDateTime eventDateTime = LocalDateTime.of(2025, 8, 15, 19, 30);
        String venue = "Open Air Stadium";
        Double price = 250.0;
        String filePath = "/uploads/tickets/T001.pdf";
        String eventImagePath = "/images/rockfest.jpg";
        String uploadedDateTime = "2025-08-01T09:00:00";

        String status = "approved";
        LocalDateTime updatedAt = LocalDateTime.of(2025, 8, 10, 12, 0);

        // Act
        ticket.setId(id);
        ticket.setEventName(eventName);
        ticket.setEventDateTime(eventDateTime);
        ticket.setVenue(venue);
        ticket.setPrice(price);
        ticket.setFilePath(filePath);
        ticket.setEventImagePath(eventImagePath);
        ticket.setUploadedDateTime(uploadedDateTime);
        ticket.setStatus(status);
        ticket.setUpdatedAt(updatedAt);

        // Assert
        assertEquals(id, ticket.getId());
        assertEquals(eventName, ticket.getEventName());
        assertEquals(eventDateTime, ticket.getEventDateTime());
        assertEquals(venue, ticket.getVenue());
        assertEquals(price, ticket.getPrice());
        assertEquals(filePath, ticket.getFilePath());
        assertEquals(eventImagePath, ticket.getEventImagePath());
        assertEquals(uploadedDateTime, ticket.getUploadedDateTime());
        assertEquals(status, ticket.getStatus());
        assertEquals(updatedAt, ticket.getUpdatedAt());
    }

    @Test
    void testDefaultValuesShouldBeNull() {
        TicketWithStatus ticket = new TicketWithStatus();

        assertNull(ticket.getId());
        assertNull(ticket.getEventName());
        assertNull(ticket.getEventDateTime());
        assertNull(ticket.getVenue());
        assertNull(ticket.getPrice());
        assertNull(ticket.getFilePath());
        assertNull(ticket.getEventImagePath());
        assertNull(ticket.getUploadedDateTime());
        assertNull(ticket.getStatus());
        assertNull(ticket.getUpdatedAt());
    }
}
