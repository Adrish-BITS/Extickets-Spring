package com.project.extickets.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.project.extickets.model.Ticket;
import com.project.extickets.model.TicketWithStatus;
import com.project.extickets.service.AdminDashboardService;
import com.project.extickets.service.EmailService;
import com.project.extickets.service.UploadTicketService;

class AdminDashboardControllerTest {

	@Mock
	private AdminDashboardService adminService;

	@Mock
	private UploadTicketService uploadService;

	@Mock
	private EmailService emailService;

	@InjectMocks
	private AdminDashboardController controller;

	private Ticket sampleTicket;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);

		sampleTicket = new Ticket();
		sampleTicket.setId("1L");
		sampleTicket.setEventName("Rock Concert");
		sampleTicket.setVenue("Stadium");
		sampleTicket.setEventDateTime(LocalDateTime.of(2025, 10, 20, 18, 30));
		sampleTicket.setPrice(1500.0);
	}

	@Test
	void testGetAllTicketsBasedOnStatus() {
		List<TicketWithStatus> tickets = Collections.singletonList(new TicketWithStatus());
		when(adminService.getAllTicketsBasedOnStatus("Approved")).thenReturn(tickets);

		ResponseEntity<List<TicketWithStatus>> response = controller.getAllTicketsBasedOnStatus("Approved");

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(1, response.getBody().size());
		verify(adminService, times(1)).getAllTicketsBasedOnStatus("Approved");
	}

	@Test
    void testChangeStatus_Approved() {
        when(uploadService.getTicketById(1L)).thenReturn(sampleTicket);
        when(adminService.changeStatus(1L, "Approved")).thenReturn(true);

        ResponseEntity<String> response = controller.changeStatus(1L, "Approved");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Ticket status changed successfully!", response.getBody());
        verify(emailService, times(2)).sendEmail(any(), any(), any()); // User + Admin email
        verify(adminService).changeStatus(1L, "Approved");
    }

	@Test
    void testChangeStatus_Rejected() {
        when(uploadService.getTicketById(1L)).thenReturn(sampleTicket);
        when(adminService.changeStatus(1L, "Rejected")).thenReturn(true);

        ResponseEntity<String> response = controller.changeStatus(1L, "Rejected");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(emailService, times(2)).sendEmail(any(), any(), any()); // User + Admin
        verify(adminService).changeStatus(1L, "Rejected");
    }

	@Test
    void testChangeStatus_InvalidStatus() {
        when(uploadService.getTicketById(1L)).thenReturn(sampleTicket);
        when(adminService.changeStatus(1L, "Pending")).thenReturn(true);

        ResponseEntity<String> response = controller.changeStatus(1L, "Pending");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(emailService, never()).sendEmail(any(), any(), any());
        verify(adminService).changeStatus(1L, "Pending");
    }

	@Test
    void testChangeStatus_TicketNotFound() {
        when(uploadService.getTicketById(1L)).thenReturn(sampleTicket);
        when(adminService.changeStatus(1L, "Approved")).thenReturn(false);

        ResponseEntity<String> response = controller.changeStatus(1L, "Approved");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("No such ticket exists", response.getBody());
        verify(emailService, times(2)).sendEmail(any(), any(), any());
    }
}