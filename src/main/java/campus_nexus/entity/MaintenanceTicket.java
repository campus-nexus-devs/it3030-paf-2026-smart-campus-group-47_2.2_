package campus_nexus.entity;

import campus_nexus.enums.PriorityLevel;
import campus_nexus.enums.TicketStatus;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * MaintenanceTicket entity represents a reported issue for a specific resource.
 * Includes support for 3 image references and priority categorization.
 */
@Entity
@Data
@Table(name = "maintenance_tickets")
public class MaintenanceTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "resource_id", nullable = false)
    private Resource resource;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PriorityLevel priority;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TicketStatus status;

    // Fields for storing image URLs/Paths (Max 3 as requested)
    private String imageUrl1;
    private String imageUrl2;
    private String imageUrl3;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = TicketStatus.OPEN;
        }
    }
}