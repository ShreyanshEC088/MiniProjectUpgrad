package org.zeta.resturant.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.zeta.resturant.dao.MenuItemRepository;
import org.zeta.resturant.dao.OrderRepository;
import org.zeta.resturant.dao.TableBookingRepository;
import org.zeta.resturant.exceptions.InsufficientStockException;
import org.zeta.resturant.exceptions.MenuItemNotFoundException;
import org.zeta.resturant.exceptions.OrderNotFoundException;
import org.zeta.resturant.model.MenuItem;
import org.zeta.resturant.model.Order;
import org.zeta.resturant.model.OrderItem;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Mock
    private OrderRepository orderRepo;
    @Mock
    private MenuItemRepository menuRepo;
    @Mock
    private TableBookingRepository bookingRepo;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void placeOrder_success() {
        Order order = new Order();
        OrderItem item = new OrderItem();
        item.setMenuItemId(1L);
        item.setQuantity(2);
        order.setItems(List.of(item));

        MenuItem menuItem = new MenuItem();
        menuItem.setId(1L);
        menuItem.setName("Pizza");
        menuItem.setQuantity(5);
        menuItem.setPrice(10.0);
        menuItem.setAvailable(true);

        when(menuRepo.findById(1L)).thenReturn(Optional.of(menuItem));
        when(menuRepo.save(any(MenuItem.class))).thenReturn(menuItem);
        when(orderRepo.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Order result = orderService.placeOrder(order);

        assertEquals(Order.OrderStatus.PLACED, result.getStatus());
        assertEquals("Pizza", result.getItems().get(0).getItemName());
        assertEquals(10.0, result.getItems().get(0).getPrice());
        verify(menuRepo).save(menuItem);
        verify(orderRepo).save(order);
    }

    @Test
    void placeOrder_menuItemNotFound() {
        Order order = new Order();
        OrderItem item = new OrderItem();
        item.setMenuItemId(2L);
        item.setQuantity(1);
        order.setItems(List.of(item));

        when(menuRepo.findById(2L)).thenReturn(Optional.empty());

        assertThrows(MenuItemNotFoundException.class, () -> orderService.placeOrder(order));
        verify(menuRepo, never()).save(any());
        verify(orderRepo, never()).save(any());
    }

    @Test
    void placeOrder_insufficientStock() {
        Order order = new Order();
        OrderItem item = new OrderItem();
        item.setMenuItemId(3L);
        item.setQuantity(10);
        order.setItems(List.of(item));

        MenuItem menuItem = new MenuItem();
        menuItem.setId(3L);
        menuItem.setName("Burger");
        menuItem.setQuantity(5);

        when(menuRepo.findById(3L)).thenReturn(Optional.of(menuItem));

        assertThrows(InsufficientStockException.class, () -> orderService.placeOrder(order));
        verify(menuRepo, never()).save(any());
        verify(orderRepo, never()).save(any());
    }

    @Test
    void placeOrder_setsAvailableFalseWhenStockZero() {
        Order order = new Order();
        OrderItem item = new OrderItem();
        item.setMenuItemId(4L);
        item.setQuantity(2);
        order.setItems(List.of(item));

        MenuItem menuItem = new MenuItem();
        menuItem.setId(4L);
        menuItem.setName("Pasta");
        menuItem.setQuantity(2);
        menuItem.setAvailable(true);

        when(menuRepo.findById(4L)).thenReturn(Optional.of(menuItem));
        when(menuRepo.save(any(MenuItem.class))).thenReturn(menuItem);
        when(orderRepo.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Order result = orderService.placeOrder(order);

        assertFalse(menuItem.isAvailable());
        verify(menuRepo).save(menuItem);
        verify(orderRepo).save(order);
    }

    @Test
    void getLiveOrders_returnsList() {
        List<Order> orders = List.of(new Order(), new Order());
        when(orderRepo.findByStatus(Order.OrderStatus.PLACED)).thenReturn(orders);

        List<Order> result = orderService.getLiveOrders();

        assertEquals(orders, result);
        verify(orderRepo).findByStatus(Order.OrderStatus.PLACED);
    }

    @Test
    void markPrepared_success() {
        Long orderId = 10L;
        Order order = new Order();
        order.setStatus(Order.OrderStatus.PLACED);

        when(orderRepo.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepo.save(order)).thenReturn(order);

        Order result = orderService.markPrepared(orderId);

        assertEquals(Order.OrderStatus.PREPARED, result.getStatus());
        verify(orderRepo).save(order);
    }

    @Test
    void markPrepared_notFound() {
        when(orderRepo.findById(20L)).thenReturn(Optional.empty());
        assertThrows(OrderNotFoundException.class, () -> orderService.markPrepared(20L));
        verify(orderRepo, never()).save(any());
    }

    @Test
    void getAllOrders_returnsList() {
        List<Order> orders = List.of(new Order(), new Order());
        when(orderRepo.findAll()).thenReturn(orders);

        List<Order> result = orderService.getAllOrders();

        assertEquals(orders, result);
        verify(orderRepo).findAll();
    }
}