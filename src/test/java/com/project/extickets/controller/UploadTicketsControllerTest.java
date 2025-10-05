package com.project.extickets.controller;

import com.project.extickets.model.Ticket;
import com.project.extickets.service.EmailService;
import com.project.extickets.service.S3StorageService;
import com.project.extickets.service.UploadTicketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class UploadTicketsControllerTest {

    @Mock
    private UploadTicketService ticketService;

    @Mock
    private EmailService emailService;

    @Mock
    private S3StorageService s3StorageService;

    @InjectMocks
    private UploadTicketsController controller;

    private MultipartFile mockFile;
    private MultipartFile mockImage;
    private Ticket sampleTicket;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockFile = new MockMultipartFile("file", "ticket.pdf", "application/pdf", "dummy".getBytes());
        mockImage = new MockMultipartFile("eventImage", "image.png", "image/png", "image".getBytes());

        sampleTicket = new Ticket();
        sampleTicket.setId("1L");
        sampleTicket.setEventName("Rock Concert");
        sampleTicket.setVenue("Stadium");
        sampleTicket.setEventDateTime(LocalDateTime.of(2025, 11, 20, 18, 30));
        sampleTicket.setPrice(1200.0);
    }

    @Test
    void testUploadTicket_Success() throws Exception {
        when(s3StorageService.uploadFile(mockFile)).thenReturn("http://s3.com/ticket.pdf");
        when(s3StorageService.uploadFile(mockImage)).thenReturn("http://s3.com/image.png");

        ResponseEntity<String> response = controller.uploadTicket(
                "Rock Concert", "2025-11-20T18:30", "Stadium", 1200.0, mockFile, mockImage);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Ticket uploaded successfully!", response.getBody());

        verify(s3StorageService, times(2)).uploadFile(any(MultipartFile.class));
        verify(ticketService, times(1)).saveTicket(any(Ticket.class));
        verify(emailService, times(2)).sendEmail(anyString(), anyString(), anyString()); // user + admin
    }

    @Test
    void testUploadTicket_Failure() throws Exception {
        when(s3StorageService.uploadFile(mockFile)).thenThrow(new RuntimeException("S3 error"));

        ResponseEntity<String> response = controller.uploadTicket(
                "Rock Concert", "2025-11-20T18:30", "Stadium", 1200.0, mockFile, mockImage);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().contains("Upload failed"));

        verify(ticketService, never()).saveTicket(any(Ticket.class));
        verify(emailService, never()).sendEmail(anyString(), anyString(), anyString());
    }

    @Test
    void testGetAllTickets() {
        when(ticketService.getAllTickets()).thenReturn(Collections.singletonList(sampleTicket));

        ResponseEntity<List<Ticket>> response = controller.getAllTickets();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(ticketService, times(1)).getAllTickets();
    }

    @Test
    void testGetTicketById() {
        when(ticketService.getTicketById(1L)).thenReturn(sampleTicket);

        ResponseEntity<Ticket> response = controller.getTicketById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Rock Concert", response.getBody().getEventName());
        verify(ticketService, times(1)).getTicketById(1L);
    }
}