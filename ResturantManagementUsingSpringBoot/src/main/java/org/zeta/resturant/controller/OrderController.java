package org.zeta.resturant.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zeta.resturant.model.Order;
import org.zeta.resturant.service.OrderService;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    @PostMapping("/place")
    public Order placeOrder(@RequestBody Order order) {
        logger.info("Placing new order for customer: {}", order.getCustomerName());
        Order placedOrder = orderService.placeOrder(order);
        logger.info("Order placed with id: {}", placedOrder.getId());
        return placedOrder;
    }

    @GetMapping("/live")
    public List<Order> getLiveOrders() {
        logger.info("Fetching live orders");
        List<Order> liveOrders = orderService.getLiveOrders();
        logger.info("Fetched {} live orders", liveOrders.size());
        return liveOrders;
    }

    @PutMapping("/{id}/markPrepared")
    public Order markOrderPrepared(@PathVariable Long id) {
        logger.info("Marking order as prepared with id: {}", id);
        Order preparedOrder = orderService.markPrepared(id);
        logger.info("Order marked as prepared with id: {}", preparedOrder.getId());
        return preparedOrder;
    }

    @GetMapping
    public List<Order> getAllOrders() {
        logger.info("Fetching all orders");
        List<Order> allOrders = orderService.getAllOrders();
        logger.info("Fetched {} orders", allOrders.size());
        return allOrders;
    }
}