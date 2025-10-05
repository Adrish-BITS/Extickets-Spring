package com.project.extickets.model;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class TicketTest {

    @Test
    void testGettersAndSetters() {
        // Arrange
        Ticket ticket = new Ticket();
        String id = "T123";
        String eventName = "Spring Concert";
        LocalDateTime eventDateTime = LocalDateTime.of(2025, 5, 10, 20, 0);
        String venue = "City Hall";
        Double price = 150.0;
        String filePath = "/tickets/T123.pdf";
        String eventImagePath = "/images/concert.jpg";
        String uploadedDateTime = "2025-05-01T10:00:00";

        // Act
        ticket.setId(id);
        ticket.setEventName(eventName);
        ticket.setEventDateTime(eventDateTime);
        ticket.setVenue(venue);
        ticket.setPrice(price);
        ticket.setFilePath(filePath);
        ticket.setEventImagePath(eventImagePath);
        ticket.setUploadedDateTime(uploadedDateTime);

        // Assert
        assertEquals(id, ticket.getId());
        assertEquals(eventName, ticket.getEventName());
        assertEquals(eventDateTime, ticket.getEventDateTime());
        assertEquals(venue, ticket.getVenue());
        assertEquals(price, ticket.getPrice());
        assertEquals(filePath, ticket.getFilePath());
        assertEquals(eventImagePath, ticket.getEventImagePath());
        assertEquals(uploadedDateTime, ticket.getUploadedDateTime());
    }

    @Test
    void testDefaultValuesShouldBeNull() {
        Ticket ticket = new Ticket();

        assertNull(ticket.getId());
        assertNull(ticket.getEventName());
        assertNull(ticket.getEventDateTime());
        assertNull(ticket.getVenue());
        assertNull(ticket.getPrice());
        assertNull(ticket.getFilePath());
        assertNull(ticket.getEventImagePath());
        assertNull(ticket.getUploadedDateTime());
    }
}
