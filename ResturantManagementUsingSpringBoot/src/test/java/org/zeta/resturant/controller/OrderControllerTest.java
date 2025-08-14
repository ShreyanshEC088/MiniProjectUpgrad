package org.zeta.resturant.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.zeta.resturant.model.Order;
import org.zeta.resturant.service.OrderService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testPlaceOrder() {
        Order order = new Order();
        Order savedOrder = new Order();
        when(orderService.placeOrder(order)).thenReturn(savedOrder);

        Order result = orderController.placeOrder(order);

        assertEquals(savedOrder, result);
        verify(orderService).placeOrder(order);
    }

    @Test
    void testGetLiveOrders() {
        List<Order> liveOrders = Arrays.asList(new Order(), new Order());
        when(orderService.getLiveOrders()).thenReturn(liveOrders);

        List<Order> result = orderController.getLiveOrders();

        assertEquals(liveOrders, result);
        verify(orderService).getLiveOrders();
    }

    @Test
    void testMarkOrderPrepared() {
        Long id = 1L;
        Order preparedOrder = new Order();
        when(orderService.markPrepared(id)).thenReturn(preparedOrder);

        Order result = orderController.markOrderPrepared(id);

        assertEquals(preparedOrder, result);
        verify(orderService).markPrepared(id);
    }

    @Test
    void testGetAllOrders() {
        List<Order> allOrders = Arrays.asList(new Order(), new Order());
        when(orderService.getAllOrders()).thenReturn(allOrders);

        List<Order> result = orderController.getAllOrders();

        assertEquals(allOrders, result);
        verify(orderService).getAllOrders();
    }
}
