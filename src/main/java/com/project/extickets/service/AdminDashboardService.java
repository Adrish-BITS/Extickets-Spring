package com.project.extickets.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.extickets.model.Ticket;
import com.project.extickets.model.TicketWithStatus;
import com.project.extickets.repository.AdminDashboardRepository;
import com.project.extickets.repository.UploadTicketRepository;

@Service
public class AdminDashboardService {

	@Autowired
	private UploadTicketRepository ticketRepository;
	
	@Autowired
	private AdminDashboardRepository adminRepository;

	public void saveTicket(Ticket ticket) {
		ticketRepository.save(ticket);
	}

	public List<Ticket> getAllTickets() {
		return ticketRepository.findAll();
	}

	public List<TicketWithStatus> getAllTicketsBasedOnStatus(String status) {
		return ticketRepository.findByStatus(status);
	}
	
	public boolean changeStatus(String ticketId, String status) {
		return adminRepository.changeStatus(ticketId, status);
	}

	
}