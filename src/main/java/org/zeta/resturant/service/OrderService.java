package org.zeta.resturant.service;

import org.zeta.resturant.dto.OrderRequest;
import org.zeta.resturant.dto.OrderResponse;

import java.util.List;

public interface OrderService {

    OrderResponse makeOrder(OrderRequest request);
    List<OrderResponse> getAllOrders();
}
