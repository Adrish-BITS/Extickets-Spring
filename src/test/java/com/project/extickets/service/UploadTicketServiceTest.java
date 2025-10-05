package com.project.extickets.service;

import com.project.extickets.model.Ticket;
import com.project.extickets.model.TicketWithStatus;
import com.project.extickets.repository.UploadTicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UploadTicketServiceTest {

    @Mock
    private UploadTicketRepository ticketRepository;

    @InjectMocks
    private UploadTicketService uploadTicketService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveTicket() {
        Ticket ticket = new Ticket();
        uploadTicketService.saveTicket(ticket);

        verify(ticketRepository, times(1)).save(ticket);
    }

    @Test
    void testGetAllTickets() {
        Ticket ticket1 = new Ticket();
        Ticket ticket2 = new Ticket();
        when(ticketRepository.findAll()).thenReturn(Arrays.asList(ticket1, ticket2));

        List<Ticket> result = uploadTicketService.getAllTickets();

        assertEquals(2, result.size());
        assertSame(ticket1, result.get(0));
        assertSame(ticket2, result.get(1));
        verify(ticketRepository, times(1)).findAll();
    }

    @Test
    void testGetAllTicketsBasedOnStatus() {
        TicketWithStatus t1 = new TicketWithStatus();
        TicketWithStatus t2 = new TicketWithStatus();

        when(ticketRepository.findByStatus("APPROVED")).thenReturn(Arrays.asList(t1, t2));

        List<TicketWithStatus> result = uploadTicketService.getAllTicketsBasedOnStatus("APPROVED");

        assertEquals(2, result.size());
        verify(ticketRepository, times(1)).findByStatus("APPROVED");
    }

    @Test
    void testGetTicketById() {
        Ticket ticket = new Ticket();
        when(ticketRepository.findById(1L)).thenReturn(ticket);

        Ticket result = uploadTicketService.getTicketById(1L);

        assertNotNull(result);
        assertSame(ticket, result);
        verify(ticketRepository, times(1)).findById(1L);
    }

    @Test
    void testGetTicketById_NotFound() {
        when(ticketRepository.findById(999L)).thenReturn(null);

        Ticket result = uploadTicketService.getTicketById(999L);

        assertNull(result);
        verify(ticketRepository, times(1)).findById(999L);
    }
}
