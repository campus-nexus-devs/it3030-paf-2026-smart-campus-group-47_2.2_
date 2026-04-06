package campus_nexus.entity;

import campus_nexus.enums.ResourceType;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "resources")
public class Resource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private ResourceType type;

    private Integer capacity;
    private String location;
}