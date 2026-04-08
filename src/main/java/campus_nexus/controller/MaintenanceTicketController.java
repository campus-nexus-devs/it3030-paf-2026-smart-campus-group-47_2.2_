package campus_nexus.controller;

import campus_nexus.entity.MaintenanceTicket;
import campus_nexus.enums.TicketStatus;
import campus_nexus.service.MaintenanceTicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for managing Maintenance Tickets.
 * Provides endpoints for reporting issues with 3 images and updating ticket progress.
 */
@RestController
@RequestMapping("/api/tickets")
@CrossOrigin(origins = "*")
public class MaintenanceTicketController {

    @Autowired
    private MaintenanceTicketService ticketService;

    /**
     * Create a new maintenance ticket.
     * Includes support for priority level and 3 image references.
     */
    @PostMapping
    public MaintenanceTicket createTicket(@RequestBody MaintenanceTicket ticket) {
        return ticketService.createTicket(ticket);
    }

    /**
     * Retrieve all maintenance tickets for the dashboard.
     */
    @GetMapping
    public List<MaintenanceTicket> getAllTickets() {
        return ticketService.getAllTickets();
    }

    /**
     * Update the status of a specific ticket (e.g., from OPEN to RESOLVED).
     * URL: PATCH http://localhost:8080/api/tickets/{id}/status?status=RESOLVED
     */
    @PatchMapping("/{id}/status")
    public MaintenanceTicket updateTicketStatus(
            @PathVariable Long id,
            @RequestParam TicketStatus status) {
        return ticketService.updateTicketStatus(id, status);
    }

    /**
     * Delete a ticket from the system.
     * Useful for clearing accidental reports.
     */
    @DeleteMapping("/{id}")
    public String deleteTicket(@PathVariable Long id) {
        ticketService.deleteTicket(id);
        return "Ticket deleted successfully!";
    }
}