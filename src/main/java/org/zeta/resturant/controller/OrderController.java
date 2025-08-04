package org.zeta.resturant.controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.zeta.resturant.dto.OrderRequest;
import org.zeta.resturant.dto.OrderResponse;
import org.zeta.resturant.service.OrderService;
import org.zeta.resturant.service.OrderServiceImpl;
import org.zeta.resturant.util.HttpUtils;
import org.zeta.resturant.util.JsonUtil;
import java.io.IOException;
import java.util.List;

public class OrderController implements HttpHandler {

    private OrderService orderService = new OrderServiceImpl();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("POST".equals(exchange.getRequestMethod())) {
            OrderRequest request = JsonUtil.parseRequest(exchange.getRequestBody(), OrderRequest.class);
            OrderResponse responseDto = orderService.makeOrder(request);
            HttpUtils.sendJsonResponse(exchange, responseDto, 201);
        } else if ("GET".equals(exchange.getRequestMethod())) {
            List<OrderResponse> bookings = orderService.getAllOrders();
            HttpUtils.sendJsonResponse(exchange, bookings, 200);
        } else {
            HttpUtils.sendTextResponse(exchange, "Method not allowed", 405);
        }
    }

}
