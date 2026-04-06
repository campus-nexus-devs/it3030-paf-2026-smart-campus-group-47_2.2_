package campus_nexus.repository;

import campus_nexus.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    // මෙතන තමයි පස්සේ අපි conflict check කරන logic එක ලියන්නේ
}