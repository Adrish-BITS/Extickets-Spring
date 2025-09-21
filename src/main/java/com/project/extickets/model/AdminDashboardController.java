package com.project.extickets.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.extickets.service.AdminDashboardService;

@RestController
@RequestMapping("/api/admin/tickets")
public class AdminDashboardController {

	@Autowired
	private AdminDashboardService adminservice;

	@GetMapping("/status/{status}")
	public ResponseEntity<List<TicketWithStatus>> getAllTicketsBasedOnStatus(@PathVariable String status) {
		return ResponseEntity.ok(adminservice.getAllTicketsBasedOnStatus(status));
	}

	@PostMapping("/ticket/{id}/changeStatus/{newStatus}")
	public ResponseEntity<String> changeStatus(@PathVariable String id, @PathVariable String newStatus) {
		return adminservice.changeStatus(id, newStatus) ? ResponseEntity.ok("Ticket status changed successfully!")
				: ResponseEntity.status(HttpStatus.NOT_FOUND).body("No such ticket exists");
	}

}
