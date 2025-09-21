package com.project.extickets.controller;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.project.extickets.model.Ticket;
import com.project.extickets.service.EmailService;
import com.project.extickets.service.UploadTicketService;

@RestController
@RequestMapping("/api/tickets")
public class UploadTicketsController {

	@Autowired
	private UploadTicketService ticketService;
	@Autowired
	private EmailService emailService;

	@PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<String> uploadTicket(@RequestParam("eventName") String eventName,
			@RequestParam("eventDateTime") String eventDateTime, @RequestParam("venue") String venue,
			@RequestParam("price") Double price, @RequestParam("file") MultipartFile file,
			@RequestParam("eventImage") MultipartFile eventImage) throws IOException {

		String fileUploadDir = System.getProperty("user.home") + "/extickets/server/uploads/";
		File dir = new File(fileUploadDir);
		if (!dir.exists())
			dir.mkdirs();
		String fileUploadPath = fileUploadDir + file.getOriginalFilename();
		file.transferTo(new File(fileUploadPath));

		String eventImageUploadDir = System.getProperty("user.home") + "/extickets/server/eventImages/";
		File dir1 = new File(eventImageUploadDir);
		if (!dir1.exists())
			dir1.mkdirs();
		String eventImageUploadPath = eventImageUploadDir + eventImage.getOriginalFilename();
		eventImage.transferTo(new File(eventImageUploadPath));

		Ticket ticket = new Ticket();
		ticket.setEventName(eventName);
		ticket.setEventDateTime(LocalDateTime.parse(eventDateTime));
		ticket.setVenue(venue);
		ticket.setPrice(price);
		ticket.setFilePath(fileUploadPath);
		ticket.setEventImagePath(eventImageUploadPath);
		ticketService.saveTicket(ticket);
		String htmlBody = """
				<html>
				  <body style="font-family: Arial, sans-serif; background-color:#f9f9f9; padding:20px;">
				    <div style="max-width:600px; margin:auto; background:#ffffff; border-radius:10px; padding:20px; box-shadow:0px 2px 6px rgba(0,0,0,0.1);">
				      <h2 style="color:#2E86C1;">ExTickets - Ticket Upload Confirmation</h2>
				      <p>Dear User,</p>
				      <p>Your ticket <b>has been uploaded successfully</b> and is currently under review.</p>
				      <p>You will be notified once the admin approves or rejects your ticket.</p>
				      <hr style="border:0; border-top:1px solid #ddd;">
				      <p style="font-size:12px; color:#888;">This is an automated email from ExTickets. Please do not reply.</p>
				    </div>
				  </body>
				</html>
				""";
		emailService.sendEmail("backuponeplus@gmail.com", "Tickets Uploaded successfully", htmlBody);
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
