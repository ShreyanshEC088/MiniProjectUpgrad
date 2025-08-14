package org.zeta.resturant.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.zeta.resturant.dao.BillRepository;
import org.zeta.resturant.dao.OrderRepository;
import org.zeta.resturant.dao.TableBookingRepository;
import org.zeta.resturant.exceptions.*;
import org.zeta.resturant.model.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BillingServiceTest {

    @Mock
    private BillRepository billRepo;
    @Mock
    private OrderRepository orderRepo;
    @Mock
    private TableBookingRepository bookingRepo;

    @InjectMocks
    private BillingService billingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void generateBill_success() {
        Long orderId = 1L;
        Order order = new Order();
        order.setStatus(Order.OrderStatus.PREPARED);
        OrderItem item = new OrderItem();
        item.setPrice(100.0);
        item.setQuantity(2);
        order.setItems(List.of(item));
        when(orderRepo.findById(orderId)).thenReturn(Optional.of(order));
        Bill savedBill = new Bill();
        when(billRepo.save(any(Bill.class))).thenReturn(savedBill);

        Bill result = billingService.generateBill(orderId);

        assertEquals(savedBill, result);
        verify(billRepo).save(any(Bill.class));
    }

    @Test
    void generateBill_orderNotFound() {
        when(orderRepo.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(OrderNotFoundException.class, () -> billingService.generateBill(1L));
    }

    @Test
    void generateBill_orderNotPrepared() {
        Order order = new Order();
        order.setStatus(Order.OrderStatus.PLACED);
        when(orderRepo.findById(anyLong())).thenReturn(Optional.of(order));
        assertThrows(OrderNotPreparedException.class, () -> billingService.generateBill(1L));
    }

    @Test
    void payBill_success() {
        Long billId = 1L;
        Bill bill = new Bill();
        bill.setOrderId(2L);
        bill.setPaymentStatus(Bill.PaymentStatus.PENDING);

        Order order = new Order();
        order.setTableNumber(5);
        order.setStatus(Order.OrderStatus.PREPARED);

        TableBooking booking = new TableBooking();
        booking.setStatus(TableBooking.BookingStatus.BOOKED);

        when(billRepo.findById(billId)).thenReturn(Optional.of(bill));
        when(orderRepo.findById(bill.getOrderId())).thenReturn(Optional.of(order));
        when(bookingRepo.findByTableNumberAndStatus(eq(order.getTableNumber()), eq(TableBooking.BookingStatus.BOOKED)))
                .thenReturn(List.of(booking));
        when(billRepo.save(any(Bill.class))).thenReturn(bill);

        Bill result = billingService.payBill(billId);

        assertEquals(Bill.PaymentStatus.PAID, result.getPaymentStatus());
        verify(orderRepo).save(order);
        verify(bookingRepo).save(booking);
        verify(billRepo, times(1)).save(bill);
    }

    @Test
    void payBill_billNotFound() {
        when(billRepo.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(BillNotFoundException.class, () -> billingService.payBill(1L));
    }

    @Test
    void payBill_orderNotFound() {
        Bill bill = new Bill();
        bill.setOrderId(2L);
        when(billRepo.findById(anyLong())).thenReturn(Optional.of(bill));
        when(orderRepo.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(OrderNotFoundException.class, () -> billingService.payBill(1L));
    }

    @Test
    void payBill_noActiveBooking() {
        Bill bill = new Bill();
        bill.setOrderId(2L);
        Order order = new Order();
        order.setTableNumber(5);
        when(billRepo.findById(anyLong())).thenReturn(Optional.of(bill));
        when(orderRepo.findById(anyLong())).thenReturn(Optional.of(order));
        when(bookingRepo.findByTableNumberAndStatus(anyInt(), any())).thenReturn(Collections.emptyList());
        assertThrows(ActiveBookingNotFoundException.class, () -> billingService.payBill(1L));
    }
}
