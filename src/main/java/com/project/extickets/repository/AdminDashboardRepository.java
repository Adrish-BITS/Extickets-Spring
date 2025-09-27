package com.project.extickets.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.project.extickets.model.Ticket;
import com.project.extickets.model.TicketWithStatus;

@Repository
public class AdminDashboardRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<Ticket> findAll() {
		String sql = "SELECT * FROM tickets";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Ticket.class));
	}

	public List<TicketWithStatus> findByStatus(String status) {
		String sql = "SELECT t.id,\r\n" + "       t.event_name,\r\n" + "       t.event_date_time,\r\n"
				+ "       t.venue,\r\n" + "       t.price,\r\n" + "       t.file_path,\r\n"
				+ "       t.event_image_path,\r\n" + "       t.uploaded_date_time,\r\n" + "       tr.status,\r\n"
				+ "       tr.updated_at\r\n" + "FROM tickets t\r\n"
				+ "INNER JOIN transactions tr ON t.id = tr.ticket_id\r\n" + "WHERE tr.status = ?;";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(TicketWithStatus.class), status);
	}

	public boolean changeStatus(Long ticketId,String status) {
		String sql = "UPDATE transactions " + "SET status = ?, updated_at = CURRENT_TIMESTAMP " + "WHERE ticket_id = ?";
		return jdbcTemplate.update(sql, status, ticketId)>0;
	}

}