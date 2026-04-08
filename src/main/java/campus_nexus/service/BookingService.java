package campus_nexus.service;

import campus_nexus.entity.Booking;
import campus_nexus.enums.BookingStatus;
import campus_nexus.repository.BookingRepository;
import campus_nexus.repository.ResourceRepository;
import campus_nexus.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ResourceRepository resourceRepository;

    /**
     * Retrieve all bookings from the database
     * @return List of all booking entities
     */
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    /**
     * Create a new booking after validating for scheduling conflicts and existence of entities
     * @param booking Booking details from request
     * @return Saved booking entity
     * @throws RuntimeException if a scheduling conflict or invalid entity ID is detected
     */
    public Booking createBooking(Booking booking) {
        // 1. Verify if the User and Resource actually exist in the database to avoid Foreign Key errors
        if (booking.getUser() == null || !userRepository.existsById(booking.getUser().getId())) {
            throw new RuntimeException("Invalid User ID! User does not exist in the database.");
        }
        if (booking.getResource() == null || !resourceRepository.existsById(booking.getResource().getId())) {
            throw new RuntimeException("Invalid Resource ID! Resource does not exist in the database.");
        }

        // 2. Check for scheduling conflicts for the same resource, date, and time
        List<Booking> conflicts = bookingRepository.findConflictingBookings(
                booking.getResource().getId(),
                booking.getBookingDate(),
                booking.getStartTime(),
                booking.getEndTime()
        );

        // 3. If conflicts exist, prevent booking and throw an exception
        if (!conflicts.isEmpty()) {
            throw new RuntimeException("Time slot conflict detected! This resource is already booked for the selected period.");
        }

        // 4. Set initial status to PENDING and persist to database
        booking.setStatus(BookingStatus.PENDING);
        return bookingRepository.save(booking);
    }

    /**
     * Cancel/Delete an existing booking by ID
     * @param id The ID of the booking to be removed
     */
    public void cancelBooking(Long id) {
        bookingRepository.deleteById(id);
    }
}