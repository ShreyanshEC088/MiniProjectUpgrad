package org.zeta.resturant.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zeta.resturant.dao.BillRepository;
import org.zeta.resturant.dao.OrderRepository;
import org.zeta.resturant.dao.TableBookingRepository;
import org.zeta.resturant.exceptions.ActiveBookingNotFoundException;
import org.zeta.resturant.exceptions.BillNotFoundException;
import org.zeta.resturant.exceptions.OrderNotFoundException;
import org.zeta.resturant.exceptions.OrderNotPreparedException;
import org.zeta.resturant.model.Bill;
import org.zeta.resturant.model.Order;
import org.zeta.resturant.model.TableBooking;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BillingService {

    @Autowired
    private BillRepository billRepo;

    @Autowired
    private OrderRepository orderRepo;

    @Autowired
    private TableBookingRepository bookingRepo;

    public Bill generateBill(Long orderId) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));
        if (order.getStatus() != Order.OrderStatus.PREPARED) {
            throw new OrderNotPreparedException("Order is not prepared yet");
        }
        double amount = order.getItems().stream()
                .mapToDouble(i -> i.getPrice() * i.getQuantity())
                .sum();
        Bill bill = new Bill();
        bill.setOrderId(orderId);
        bill.setAmount(amount);
        bill.setPaymentStatus(Bill.PaymentStatus.PENDING);
        bill.setPaymentTime(null);
        return billRepo.save(bill);
    }

    public Bill payBill(Long billId) {
        return billRepo.findById(billId).map(bill -> {
            bill.setPaymentStatus(Bill.PaymentStatus.PAID);
            bill.setPaymentTime(LocalDateTime.now());
            Order order = orderRepo.findById(bill.getOrderId())
                    .orElseThrow(() -> new OrderNotFoundException("Order not found"));

            order.setStatus(Order.OrderStatus.PAID);
            orderRepo.save(order);
            List<TableBooking> activeBookings = bookingRepo.findByTableNumberAndStatus(
                    order.getTableNumber(), TableBooking.BookingStatus.BOOKED
            );
            if (activeBookings.isEmpty()) {
                throw new ActiveBookingNotFoundException(
                        "No active booking found for table " + order.getTableNumber()
                );
            }
            for (TableBooking booking : activeBookings) {
                booking.setStatus(TableBooking.BookingStatus.COMPLETED);
                bookingRepo.save(booking);
            }
            return billRepo.save(bill);
        }).orElseThrow(() -> new BillNotFoundException("Bill not found"));
    }
}
