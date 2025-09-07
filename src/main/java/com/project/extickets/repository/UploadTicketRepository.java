package com.project.extickets.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.project.extickets.model.Ticket;

@Repository
public class UploadTicketRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public int save(Ticket ticket) {
        String sql = "INSERT INTO tickets (event_name, event_date_time, venue, price, file_path) VALUES (?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql,
                ticket.getEventName(),
                Timestamp.valueOf(ticket.getEventDateTime()),
                ticket.getVenue(),
                ticket.getPrice(),
                ticket.getFilePath());
    }

    public List<Ticket> findAll() {
        String sql = "SELECT * FROM tickets";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Ticket.class));
    }

    public Ticket findById(Long id) {
        String sql = "SELECT * FROM tickets WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Ticket.class), id);
    }
}