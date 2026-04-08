package campus_nexus.service;

import campus_nexus.entity.Booking;
import campus_nexus.enums.BookingStatus;
import campus_nexus.repository.BookingRepository;
import campus_nexus.repository.ResourceRepository;
import campus_nexus.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * BookingService handles the business logic for room/equipment reservations.
 * Implements industry-standard workflow: PENDING -> APPROVED/REJECTED.
 */
@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ResourceRepository resourceRepository;

    /**
     * Retrieve all bookings from the database for the admin dashboard.
     * @return List of all booking entities
     */
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    /**
     * Create a new booking request.
     * Initial status is set to PENDING and conflicts are checked before saving.
     * * @param booking Booking details from request
     * @return Saved booking entity with PENDING status
     */
    public Booking createBooking(Booking booking) {
        // 1. Validating Entity existence to prevent data integrity issues
        if (booking.getUser() == null || !userRepository.existsById(booking.getUser().getId())) {
            throw new RuntimeException("Invalid User ID! The reporting user does not exist.");
        }
        if (booking.getResource() == null || !resourceRepository.existsById(booking.getResource().getId())) {
            throw new RuntimeException("Invalid Resource ID! The requested resource does not exist.");
        }

        // 2. Conflict Prevention Logic
        List<Booking> conflicts = bookingRepository.findConflictingBookings(
                booking.getResource().getId(),
                booking.getBookingDate(),
                booking.getStartTime(),
                booking.getEndTime()
        );

        if (!conflicts.isEmpty()) {
            throw new RuntimeException("Time slot conflict detected! This resource is already booked for the selected period.");
        }

        // 3. Setting initial workflow status
        booking.setStatus(BookingStatus.PENDING);
        return bookingRepository.save(booking);
    }

    /**
     * Professional Admin Action: Approve or Reject a booking request with a reason.
     * * @param id Booking ID to update
     * @param status The new status (APPROVED/REJECTED)
     * @param reason Optional reason (highly recommended for Rejections)
     * @return Updated booking entity
     */
    public Booking updateBookingStatus(Long id, BookingStatus status, String reason) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found with ID: " + id));

        booking.setStatus(status);
        booking.setRejectionReason(reason);

        return bookingRepository.save(booking);
    }

    /**
     * Cancel/Delete a booking from the system.
     * @param id The ID of the booking to be removed
     */
    public void cancelBooking(Long id) {
        if (!bookingRepository.existsById(id)) {
            throw new RuntimeException("Cannot cancel! Booking not found with ID: " + id);
        }
        bookingRepository.deleteById(id);
    }
}