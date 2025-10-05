package com.project.extickets.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
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
class UploadTicketRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private UploadTicketRepository uploadTicketRepository;

    private Ticket sampleTicket;
    private TicketWithStatus sampleTicketWithStatus;

    @BeforeEach
    void setUp() {
        sampleTicket = new Ticket();
        sampleTicket.setId("1L");
        sampleTicket.setEventName("Tech Expo");
        sampleTicket.setEventDateTime(LocalDateTime.of(2025, 10, 4, 18, 0));
        sampleTicket.setVenue("Convention Center");
        sampleTicket.setPrice(100.0);
        sampleTicket.setFilePath("/uploads/tickets/1.pdf");
        sampleTicket.setEventImagePath("/images/event1.jpg");

        sampleTicketWithStatus = new TicketWithStatus();
        sampleTicketWithStatus.setId("1L");
        sampleTicketWithStatus.setEventName("Tech Expo");
        sampleTicketWithStatus.setStatus("in-review");
    }

    @Test
    void testSave_ShouldInsertTicketAndTransaction() {
        // Arrange
        String insertTicketsSql = "INSERT INTO tickets (event_name, event_date_time, venue, price, file_path, event_image_path) VALUES (?, ?, ?, ?, ?, ?)";
        String getLastInsertIdSql = "SELECT LAST_INSERT_ID()";
        String insertTransactionSql = "INSERT INTO transactions (ticket_id, status) VALUES (?, ?)";

        when(jdbcTemplate.queryForObject(getLastInsertIdSql, Long.class)).thenReturn(1L);

        // Act
        int result = uploadTicketRepository.save(sampleTicket);

        // Assert
        verify(jdbcTemplate, times(1)).update(eq(insertTicketsSql),
                eq(sampleTicket.getEventName()),
                any(Timestamp.class),
                eq(sampleTicket.getVenue()),
                eq(sampleTicket.getPrice()),
                eq(sampleTicket.getFilePath()),
                eq(sampleTicket.getEventImagePath()));

        verify(jdbcTemplate, times(1)).queryForObject(getLastInsertIdSql, Long.class);
        verify(jdbcTemplate, times(1)).update(insertTransactionSql, 1L, "in-review");

        assertEquals(0, result, "save() should always return 0");
    }

    @Test
    void testFindAll_ShouldReturnListOfTickets() {
        // Arrange
        String sql = "SELECT * FROM tickets";
        when(jdbcTemplate.query(eq(sql), any(BeanPropertyRowMapper.class)))
                .thenReturn(Arrays.asList(sampleTicket));

        // Act
        List<Ticket> result = uploadTicketRepository.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Tech Expo", result.get(0).getEventName());
        verify(jdbcTemplate, times(1)).query(eq(sql), any(BeanPropertyRowMapper.class));
    }

    @Test
    void testFindByStatus_ShouldReturnListOfTicketWithStatus() {
        // Arrange
        String status = "in-review";
        String expectedSql = "SELECT t.id,\r\n"
                + "       t.event_name,\r\n"
                + "       t.event_date_time,\r\n"
                + "       t.venue,\r\n"
                + "       t.price,\r\n"
                + "       t.file_path,\r\n"
                + "       t.event_image_path,\r\n"
                + "       t.uploaded_date_time,\r\n"
                + "       tr.status,\r\n"
                + "       tr.updated_at\r\n"
                + "FROM tickets t\r\n"
                + "INNER JOIN transactions tr ON t.id = tr.ticket_id\r\n"
                + "WHERE tr.status = ?;";

        when(jdbcTemplate.query(eq(expectedSql), any(BeanPropertyRowMapper.class), eq(status)))
                .thenReturn(Arrays.asList(sampleTicketWithStatus));

        // Act
        List<TicketWithStatus> result = uploadTicketRepository.findByStatus(status);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("in-review", result.get(0).getStatus());
        verify(jdbcTemplate, times(1))
                .query(eq(expectedSql), any(BeanPropertyRowMapper.class), eq(status));
    }

    @Test
    void testFindById_ShouldReturnTicket() {
        // Arrange
        Long ticketId = 1L;
        String sql = "SELECT * FROM tickets WHERE id = ?";
        when(jdbcTemplate.queryForObject(eq(sql), any(BeanPropertyRowMapper.class), eq(ticketId)))
                .thenReturn(sampleTicket);

        // Act
        Ticket result = uploadTicketRepository.findById(ticketId);

        // Assert
        assertNotNull(result);
//        assertEquals(1, result.getId());
        assertEquals("Tech Expo", result.getEventName());
        verify(jdbcTemplate, times(1))
                .queryForObject(eq(sql), any(BeanPropertyRowMapper.class), eq(ticketId));
    }
}
