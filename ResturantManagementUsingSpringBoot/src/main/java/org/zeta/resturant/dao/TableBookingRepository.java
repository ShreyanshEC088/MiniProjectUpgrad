package org.zeta.resturant.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.zeta.resturant.model.TableBooking;

import java.util.List;

@Repository
public interface TableBookingRepository extends JpaRepository<TableBooking, Long> {
    List<TableBooking> findByTableNumberAndStatus(int tableNumber, TableBooking.BookingStatus status);
}
