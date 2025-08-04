package org.zeta.resturant.service;

import org.zeta.resturant.dao.BookingDao;
import org.zeta.resturant.dto.OrderRequest;
import org.zeta.resturant.dto.OrderResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.zeta.resturant.dao.OrderDao;
import org.zeta.resturant.dto.*;
import org.zeta.resturant.enums.BookingStatus;
import org.zeta.resturant.enums.OrderStatus;
import org.zeta.resturant.model.*;
import java.time.LocalDateTime;

public class OrderServiceImpl implements OrderService {

    private final KitchenOrderNotifier kitchenNotifier = new KitchenOrderNotifier();
    private OrderDao orderDao = new OrderDao();
    private BookingDao bookingDao = new BookingDao();

    @Override
    public OrderResponse makeOrder(OrderRequest request) {
        Table table = bookingDao.getTableByNumber(request.getTableNumber());
        if (table == null) {
            return new OrderResponse(-1L, "FAILED", null, request.getTableNumber(), 0,
                    request.getCustomerName(), request.getCustomerPhone(), null,
                    Collections.emptyList());
        }

        if (!table.isAvailable()) {
            return new OrderResponse(-1L, "FAILED", null, table.getTableNumber(), table.getCapacity(),
                    request.getCustomerName(), request.getCustomerPhone(), null,
                    Collections.emptyList());
        }

        Booking booking = new Booking(
                0L,
                request.getCustomerName(),
                request.getCustomerPhone(),
                LocalDateTime.now(),
                request.getTableNumber(),
                BookingStatus.CONFIRMED,
                table
        );

        Booking savedBooking = bookingDao.save(booking);
        booking.setId(savedBooking.getId());
        List<OrderItem> items = new ArrayList<>();
        List<OrderItemResponse> itemResponses = new ArrayList<>();

        for (OrderItemRequest itemReq : request.getItems()) {
            MenuItem menuItem = new MenuItem(
                    itemReq.getMenuItemId(),
                    itemReq.getName(),
                    itemReq.getDescription(),
                    itemReq.getPrice(),
                    true
            );

            items.add(new OrderItem(0L, null, menuItem, itemReq.getQuantity()));
            itemResponses.add(new OrderItemResponse(
                    menuItem.getId(),
                    menuItem.getName(),
                    menuItem.getDescription(),
                    menuItem.getPrice(),
                    itemReq.getQuantity()
            ));
        }

        Order order = new Order(0L, table, booking, LocalDateTime.now(), OrderStatus.PLACED, items);
        long orderId = orderDao.saveOrder(order);
        order.setId(orderId);
        for (OrderItem item : order.getOrderItems()) {
            item.setOrder(order);
        }
        kitchenNotifier.notifyKitchen(order);
        bookingDao.markTableAsUnavailable(table.getTableNumber());

        OrderDao.markTableAsUnavailable(table.getId());

        return new OrderResponse(
                orderId,
                order.getStatus().name(),
                order.getCreatedAt(),
                table.getTableNumber(),
                table.getCapacity(),
                booking.getCustomerName(),
                booking.getPhoneNumber(),
                booking.getBookingTime(),
                itemResponses
        );
    }

    @Override
    public List<OrderResponse> getAllOrders() {
        List<Order> orders = orderDao.getAllOrders();
        List<OrderResponse> responses = new ArrayList<>();

        for (Order order : orders) {
            List<OrderItemResponse> itemResponses = new ArrayList<>();

            for (OrderItem item : order.getOrderItems()) {
                MenuItem menu = item.getMenuItem();
                itemResponses.add(new OrderItemResponse(
                        menu.getId(),
                        menu.getName(),
                        menu.getDescription(),
                        menu.getPrice(),
                        item.getQuantity()
                ));
            }
            Table table = order.getTable();
            Booking booking = order.getBooking();
            OrderResponse response = new OrderResponse(
                    order.getId(),
                    order.getStatus().name(),
                    order.getCreatedAt(),
                    table.getTableNumber(),
                    table.getCapacity(),
                    booking.getCustomerName(),
                    booking.getPhoneNumber(),
                    booking.getBookingTime(),
                    itemResponses
            );
            responses.add(response);
        }
        return responses;
    }
}
