package campus_nexus.repository;

import campus_nexus.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    /**
     * Check if there are any existing bookings for a specific resource, date, and time range.
     * This query detects overlaps where a new booking's time intersects with an existing one.
     *
     * @param resourceId ID of the resource (hall/equipment)
     * @param date Date of the booking
     * @param startTime Start time of the requested booking
     * @param endTime End time of the requested booking
     * @return List of conflicting bookings
     */
    @Query("SELECT b FROM Booking b WHERE b.resource.id = :resourceId " +
            "AND b.bookingDate = :date " +
            "AND ((b.startTime < :endTime AND b.endTime > :startTime))")
    List<Booking> findConflictingBookings(
            @Param("resourceId") Long resourceId,
            @Param("date") LocalDate date,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );
}