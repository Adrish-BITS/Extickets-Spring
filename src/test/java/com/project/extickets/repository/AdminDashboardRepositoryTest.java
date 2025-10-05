package com.project.extickets.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import com.project.extickets.model.Ticket;
import com.project.extickets.model.TicketWithStatus;

@ExtendWith(MockitoExtension.class)
class AdminDashboardRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private AdminDashboardRepository adminDashboardRepository;

    private Ticket sampleTicket;
    private TicketWithStatus sampleTicketWithStatus;

    @BeforeEach
    void setUp() {
        sampleTicket = new Ticket();
        sampleTicket.setId("1L");
        sampleTicket.setEventName("Music Fest");

        sampleTicketWithStatus = new TicketWithStatus();
        sampleTicketWithStatus.setId("1L");
        sampleTicketWithStatus.setEventName("Music Fest");
        sampleTicketWithStatus.setStatus("APPROVED");
    }

    @Test
    void testFindAll_ShouldReturnListOfTickets() {
        // Arrange
        String expectedSql = "SELECT * FROM tickets";
        when(jdbcTemplate.query(eq(expectedSql), any(BeanPropertyRowMapper.class)))
                .thenReturn(Arrays.asList(sampleTicket));

        // Act
        List<Ticket> result = adminDashboardRepository.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Music Fest", result.get(0).getEventName());
        verify(jdbcTemplate, times(1)).query(eq(expectedSql), any(BeanPropertyRowMapper.class));
    }

    @Test
    void testFindByStatus_ShouldReturnListOfTicketWithStatus() {
        // Arrange
        String status = "APPROVED";
        String expectedSql = "SELECT t.id,\r\n" + 
                "       t.event_name,\r\n" +
                "       t.event_date_time,\r\n" +
                "       t.venue,\r\n" +
                "       t.price,\r\n" +
                "       t.file_path,\r\n" +
                "       t.event_image_path,\r\n" +
                "       t.uploaded_date_time,\r\n" +
                "       tr.status,\r\n" +
                "       tr.updated_at\r\n" +
                "FROM tickets t\r\n" +
                "INNER JOIN transactions tr ON t.id = tr.ticket_id\r\n" +
                "WHERE tr.status = ?;";

        when(jdbcTemplate.query(eq(expectedSql), any(BeanPropertyRowMapper.class), eq(status)))
                .thenReturn(Arrays.asList(sampleTicketWithStatus));

        // Act
        List<TicketWithStatus> result = adminDashboardRepository.findByStatus(status);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("APPROVED", result.get(0).getStatus());
        verify(jdbcTemplate, times(1))
                .query(eq(expectedSql), any(BeanPropertyRowMapper.class), eq(status));
    }

    @Test
    void testChangeStatus_ShouldReturnTrue_WhenUpdateIsSuccessful() {
        // Arrange
        Long ticketId = 1L;
        String status = "REJECTED";
        String expectedSql = "UPDATE transactions SET status = ?, updated_at = CURRENT_TIMESTAMP WHERE ticket_id = ?";
        when(jdbcTemplate.update(expectedSql, status, ticketId)).thenReturn(1);

        // Act
        boolean result = adminDashboardRepository.changeStatus(ticketId, status);

        // Assert
        assertTrue(result);
        verify(jdbcTemplate, times(1)).update(expectedSql, status, ticketId);
    }

    @Test
    void testChangeStatus_ShouldReturnFalse_WhenNoRowsUpdated() {
        // Arrange
        Long ticketId = 99L;
        String status = "APPROVED";
        String expectedSql = "UPDATE transactions SET status = ?, updated_at = CURRENT_TIMESTAMP WHERE ticket_id = ?";
        when(jdbcTemplate.update(expectedSql, status, ticketId)).thenReturn(0);

        // Act
        boolean result = adminDashboardRepository.changeStatus(ticketId, status);

        // Assert
        assertFalse(result);
        verify(jdbcTemplate, times(1)).update(expectedSql, status, ticketId);
    }
}
