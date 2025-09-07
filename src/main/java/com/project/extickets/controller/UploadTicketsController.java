package com.project.extickets.controller;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.project.extickets.model.Ticket;
import com.project.extickets.service.UploadTicketService;


@RestController
@RequestMapping("/api/tickets")
public class UploadTicketsController {
	
	@Autowired
    private UploadTicketService ticketService;

	@PostMapping("/upload")
    public ResponseEntity<String> uploadTicket(
            @RequestParam String eventName,
            @RequestParam String eventDateTime, // format: "2025-11-20T18:30"
            @RequestParam String venue,
            @RequestParam Double price,
            @RequestParam("file") MultipartFile file) throws IOException {

        // Save file locally
        String uploadDir = "uploads/";
        File dir = new File(uploadDir);
        if (!dir.exists()) dir.mkdirs();

        String filePath = uploadDir + file.getOriginalFilename();
        file.transferTo(new File(filePath));

        // Create Ticket
        Ticket ticket = new Ticket();
        ticket.setEventName(eventName);
        ticket.setEventDateTime(LocalDateTime.parse(eventDateTime));
        ticket.setVenue(venue);
        ticket.setPrice(price);
        ticket.setFilePath(filePath);

        ticketService.saveTicket(ticket);

        return ResponseEntity.ok("Ticket uploaded successfully!");
    }

    @GetMapping
    public ResponseEntity<List<Ticket>> getAllTickets() {
        return ResponseEntity.ok(ticketService.getAllTickets());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ticket> getTicketById(@PathVariable Long id) {
        return ResponseEntity.ok(ticketService.getTicketById(id));
    }

}
