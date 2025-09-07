package com.project.extickets.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.extickets.model.Ticket;
import com.project.extickets.repository.UploadTicketRepository;

@Service
public class UploadTicketService {

    @Autowired
    private UploadTicketRepository ticketRepository;

    public void saveTicket(Ticket ticket) {
        ticketRepository.save(ticket);
    }

    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    public Ticket getTicketById(Long id) {
        return ticketRepository.findById(id);
    }
}