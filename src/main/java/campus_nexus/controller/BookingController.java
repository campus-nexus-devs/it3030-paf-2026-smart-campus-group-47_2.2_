package campus_nexus.controller;

import campus_nexus.entity.Booking;
import campus_nexus.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "*") // Allow frontend to access this API
public class BookingController {

    @Autowired
    private BookingService bookingService;

    /**
     * Get all bookings
     * URL: GET http://localhost:8080/api/bookings
     */
    @GetMapping
    public List<Booking> getAllBookings() {
        return bookingService.getAllBookings();
    }

    /**
     * Create a new booking
     * URL: POST http://localhost:8080/api/bookings
     */
    @PostMapping
    public Booking createBooking(@RequestBody Booking booking) {
        return bookingService.createBooking(booking);
    }

    /**
     * Delete a booking
     * URL: DELETE http://localhost:8080/api/bookings/{id}
     */
    @DeleteMapping("/{id}")
    public String cancelBooking(@PathVariable Long id) {
        bookingService.cancelBooking(id);
        return "Booking cancelled successfully!";
    }
}