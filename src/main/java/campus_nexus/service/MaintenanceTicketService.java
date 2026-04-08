package campus_nexus.service;

import campus_nexus.entity.MaintenanceTicket;
import campus_nexus.enums.TicketStatus;
import campus_nexus.repository.MaintenanceTicketRepository;
import campus_nexus.repository.ResourceRepository;
import campus_nexus.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for Maintenance Tickets.
 * Handles business logic for reporting issues, updating status, and deletion.
 */
@Service
public class MaintenanceTicketService {

    @Autowired
    private MaintenanceTicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ResourceRepository resourceRepository;

    /**
     * Create a new maintenance ticket after validating user and resource existence.
     * @param ticket The ticket details sent from the controller
     * @return The saved MaintenanceTicket object
     */
    public MaintenanceTicket createTicket(MaintenanceTicket ticket) {
        // Validation: Ensure reporting user exists in the database
        if (ticket.getUser() == null || !userRepository.existsById(ticket.getUser().getId())) {
            throw new RuntimeException("Invalid User ID! Reporting user must exist in the database.");
        }

        // Validation: Ensure the resource being reported exists
        if (ticket.getResource() == null || !resourceRepository.existsById(ticket.getResource().getId())) {
            throw new RuntimeException("Invalid Resource ID! Resource does not exist.");
        }

        return ticketRepository.save(ticket);
    }

    /**
     * Retrieve all reported tickets from the database.
     * @return List of maintenance tickets
     */
    public List<MaintenanceTicket> getAllTickets() {
        return ticketRepository.findAll();
    }

    /**
     * Update the status of an existing ticket.
     * @param id Ticket ID
     * @param status New status (e.g., RESOLVED)
     * @return Updated ticket object
     */
    public MaintenanceTicket updateTicketStatus(Long id, TicketStatus status) {
        MaintenanceTicket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found with ID: " + id));

        ticket.setStatus(status);
        return ticketRepository.save(ticket);
    }

    /**
     * Delete a ticket by its ID.
     * @param id Ticket ID to be deleted
     */
    public void deleteTicket(Long id) {
        if (!ticketRepository.existsById(id)) {
            throw new RuntimeException("Cannot delete! Ticket not found with ID: " + id);
        }
        ticketRepository.deleteById(id);
    }
}