package com.project.extickets.model;

import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.extickets.enums.AdminEmailTemplates;
import com.project.extickets.enums.UserEmailTemplates;
import com.project.extickets.service.AdminDashboardService;
import com.project.extickets.service.EmailService;
import com.project.extickets.service.UploadTicketService;

@RestController
@RequestMapping("/api/admin/tickets")
public class AdminDashboardController {

	@Autowired
	private AdminDashboardService adminService;
	
	@Autowired
	private UploadTicketService uploadService;
	
	@Autowired
	private EmailService emailService;

	@GetMapping("/status/{status}")
	public ResponseEntity<List<TicketWithStatus>> getAllTicketsBasedOnStatus(@PathVariable String status) {
		return ResponseEntity.ok(adminService.getAllTicketsBasedOnStatus(status));
	}

	@PostMapping("/ticket/{id}/changeStatus/{newStatus}")
	public ResponseEntity<String> changeStatus(@PathVariable Long id, @PathVariable String newStatus) {

		Ticket ticket = uploadService.getTicketById(id);
		if (newStatus.equalsIgnoreCase("Approved")) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy hh:mm a");
			String userHtmlBody = UserEmailTemplates.TICKET_APPROVED.getTemplate()
					.replace("${eventName}", ticket.getEventName()).replace("${venue}", ticket.getVenue())
					.replace("${eventDateTime}", ticket.getEventDateTime().format(formatter).toString())
					.replace("${price}", ticket.getPrice().toString()).replace("${status}", "Review");
			// TODO Add user email id here
			emailService.sendEmail("backuponeplus345@gmail.com", "[ExTickets] Yayy!!! Your ticket is approved and live now",
					userHtmlBody);
			
			String adminHtmlBody = AdminEmailTemplates.ADMIN_APPROVED.getTemplate()
					.replace("${eventName}", ticket.getEventName())
					.replace("${venue}", ticket.getVenue())
					.replace("${eventDateTime}", ticket.getEventDateTime().format(formatter).toString())
					.replace("${price}", ticket.getPrice().toString()).replace("${status}", "Review");
			//TODO Add admin email id here
			emailService.sendEmail("backuponeplus345@gmail.com", "[ExTickets] You Approved a Ticket Successfully", adminHtmlBody);
		}
		else if(newStatus.equalsIgnoreCase("Rejected")) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy hh:mm a");
			String userHtmlBody = UserEmailTemplates.TICKET_REJECTED.getTemplate()
					.replace("${eventName}", ticket.getEventName()).replace("${venue}", ticket.getVenue())
					.replace("${eventDateTime}", ticket.getEventDateTime().format(formatter).toString())
					.replace("${price}", ticket.getPrice().toString()).replace("${status}", "Review");
			// TODO Add user email id here
			emailService.sendEmail("backuponeplus345@gmail.com", "[ExTickets] Oops!!! Your ticket is rejected",
					userHtmlBody);
			
			String adminHtmlBody = AdminEmailTemplates.ADMIN_REJECTED.getTemplate()
					.replace("${eventName}", ticket.getEventName())
					.replace("${venue}", ticket.getVenue())
					.replace("${eventDateTime}", ticket.getEventDateTime().format(formatter).toString())
					.replace("${price}", ticket.getPrice().toString()).replace("${status}", "Review");
			//TODO Add admin email id here
			emailService.sendEmail("backuponeplus345@gmail.com", "[ExTickets] You Approved a Ticket Successfully", adminHtmlBody);
		}
		
		return adminService.changeStatus(id, newStatus) ? ResponseEntity.ok("Ticket status changed successfully!")
				: ResponseEntity.status(HttpStatus.NOT_FOUND).body("No such ticket exists");
	}

}
