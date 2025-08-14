package org.zeta.resturant.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.zeta.resturant.model.Order;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByStatus(Order.OrderStatus status);
    List<Order> findByTableNumberAndStatusNot(int tableNumber, Order.OrderStatus status);

}
