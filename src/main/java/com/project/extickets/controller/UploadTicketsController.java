package com.project.extickets.controller;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

import com.project.extickets.enums.AdminEmailTemplates;
import com.project.extickets.enums.UserEmailTemplates;
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
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy hh:mm a");
		String userHtmlBody = UserEmailTemplates.TICKET_UPLOADED.getTemplate().replace("${eventName}", ticket.getEventName())
				.replace("${venue}", ticket.getVenue())
				.replace("${eventDateTime}", ticket.getEventDateTime().format(formatter).toString())
				.replace("${price}", ticket.getPrice().toString()).replace("${status}", "Review");
		//TODO Add user email id here
		emailService.sendEmail("backuponeplus345@gmail.com", "[ExTickets] Tickets Uploaded successfully", userHtmlBody);
		
		String adminHtmlBody = AdminEmailTemplates.USER_UPLOADED.getTemplate()
				.replace("${eventName}", ticket.getEventName())
				.replace("${venue}", ticket.getVenue())
				.replace("${eventDateTime}", ticket.getEventDateTime().format(formatter).toString())
				.replace("${price}", ticket.getPrice().toString()).replace("${status}", "Review");
		//TODO Add admin email id here
		emailService.sendEmail("backuponeplus345@gmail.com", "[ExTickets] New Ticket Uploaded - Review Required", adminHtmlBody);
		
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
