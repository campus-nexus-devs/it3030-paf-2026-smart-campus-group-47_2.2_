package campus_nexus.service;

import campus_nexus.entity.Booking;
import campus_nexus.enums.BookingStatus;
import campus_nexus.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    /**
     * Retrieve all bookings from the database
     */
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    /**
     * Create a new booking request
     * Note: Conflict checking logic will be added here later
     */
    public Booking createBooking(Booking booking) {
        // Set initial status to PENDING
        booking.setStatus(BookingStatus.PENDING);
        return bookingRepository.save(booking);
    }

    /**
     * Delete a booking by its ID
     */
    public void cancelBooking(Long id) {
        bookingRepository.deleteById(id);
    }
}