package com.project.extickets.service;
import com.project.extickets.model.Ticket;
import com.project.extickets.model.TicketWithStatus;
import com.project.extickets.repository.AdminDashboardRepository;
import com.project.extickets.repository.UploadTicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminDashboardServiceTest {

    @Mock
    private UploadTicketRepository ticketRepository;

    @Mock
    private AdminDashboardRepository adminRepository;

    @InjectMocks
    private AdminDashboardService service;

    private Ticket ticket;
    private TicketWithStatus ticketWithStatus;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        ticket = new Ticket();
        ticket.setId("1L");
        ticket.setEventName("Music Fest");

        ticketWithStatus = new TicketWithStatus();
        ticketWithStatus.setId("2L");
        ticketWithStatus.setEventName("Cricket Match");
        ticketWithStatus.setStatus("Approved");
    }

    @Test
    void testSaveTicket() {
        service.saveTicket(ticket);

        verify(ticketRepository, times(1)).save(ticket);
    }

    @Test
    void testGetAllTickets() {
        when(ticketRepository.findAll()).thenReturn(Arrays.asList(ticket));

        List<Ticket> result = service.getAllTickets();

        assertEquals(1, result.size());
        assertEquals("Music Fest", result.get(0).getEventName());
        verify(ticketRepository, times(1)).findAll();
    }

    @Test
    void testGetAllTicketsBasedOnStatus() {
        when(ticketRepository.findByStatus("Approved")).thenReturn(Arrays.asList(ticketWithStatus));

        List<TicketWithStatus> result = service.getAllTicketsBasedOnStatus("Approved");

        assertEquals(1, result.size());
        assertEquals("Cricket Match", result.get(0).getEventName());
        assertEquals("Approved", result.get(0).getStatus());
        verify(ticketRepository, times(1)).findByStatus("Approved");
    }

    @Test
    void testChangeStatus() {
        when(adminRepository.changeStatus(1L, "Approved")).thenReturn(true);

        boolean result = service.changeStatus(1L, "Approved");

        assertTrue(result);
        verify(adminRepository, times(1)).changeStatus(1L, "Approved");
    }
}