package org.zeta.resturant.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zeta.resturant.exceptions.InsufficientStockException;
import org.zeta.resturant.dao.MenuItemRepository;
import org.zeta.resturant.dao.OrderRepository;
import org.zeta.resturant.dao.TableBookingRepository;
import org.zeta.resturant.exceptions.MenuItemNotFoundException;
import org.zeta.resturant.exceptions.OrderNotFoundException;
import org.zeta.resturant.model.MenuItem;
import org.zeta.resturant.model.Order;
import org.zeta.resturant.model.OrderItem;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepo;

    @Autowired
    private MenuItemRepository menuRepo;

    @Autowired
    private TableBookingRepository bookingRepo;

    public Order placeOrder(Order order) {
        order.setStatus(Order.OrderStatus.PLACED);

        List<OrderItem> validatedItems = order.getItems().stream().map(item -> {
            MenuItem menuItem = menuRepo.findById(item.getMenuItemId())
                    .orElseThrow(() -> new MenuItemNotFoundException(
                            "Menu item not found: " + item.getMenuItemId()));

            if (menuItem.getQuantity() < item.getQuantity()) {
                throw new InsufficientStockException(
                        "Not enough stock for " + menuItem.getName());
            }
            menuItem.setQuantity(menuItem.getQuantity() - item.getQuantity());
            if (menuItem.getQuantity() == 0) {
                menuItem.setAvailable(false);
            }
            menuRepo.save(menuItem);

            item.setItemName(menuItem.getName());
            item.setPrice(menuItem.getPrice());
            return item;
        }).collect(Collectors.toList());

        order.setItems(validatedItems);
        return orderRepo.save(order);
    }

    public List<Order> getLiveOrders() {
        return orderRepo.findByStatus(Order.OrderStatus.PLACED);
    }

    public Order markPrepared(Long orderId) {
        return orderRepo.findById(orderId).map(order -> {
            order.setStatus(Order.OrderStatus.PREPARED);
            return orderRepo.save(order);
        }).orElseThrow(() -> new OrderNotFoundException(
                "Order not found with id: " + orderId));
    }

    public List<Order> getAllOrders() {
        return orderRepo.findAll();
    }
}
