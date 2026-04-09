package campus_nexus.repository;

import campus_nexus.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for AuditLog entity.
 * Provides standard CRUD operations to persist system activity logs.
 */
@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    // Standard JpaRepository methods are sufficient for basic logging
}