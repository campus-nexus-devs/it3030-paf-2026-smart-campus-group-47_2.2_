package campus_nexus.repository;

import campus_nexus.entity.MaintenanceTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaintenanceTicketRepository extends JpaRepository<MaintenanceTicket, Long> {
    // Custom query to find tickets by status (e.g., only OPEN tickets)
    List<MaintenanceTicket> findByStatus(String status);
}