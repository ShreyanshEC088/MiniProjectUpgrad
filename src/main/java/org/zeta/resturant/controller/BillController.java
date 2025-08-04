package org.zeta.resturant.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.zeta.resturant.dao.OrderDao;
import org.zeta.resturant.model.Bill;
import org.zeta.resturant.model.Order;
import org.zeta.resturant.service.BillService;
import org.zeta.resturant.util.HttpUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class BillController implements HttpHandler {

    private final BillService billService = new BillService();
    private final OrderDao orderDao = new OrderDao();
    private final ObjectMapper objectMapper = new ObjectMapper(); // local mapper for parsing input JSON

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();

        if ("/bill/generate".equals(path) && "POST".equalsIgnoreCase(method)) {
            handleBillGenerate(exchange);
        } else if ("/bill/pay".equals(path) && "POST".equalsIgnoreCase(method)) {
            handleBillPayment(exchange);
        } else {
            HttpUtils.sendTextResponse(exchange, "Invalid endpoint or method", 404);
        }
    }

    private void handleBillGenerate(HttpExchange exchange) throws IOException {
        try (InputStream is = exchange.getRequestBody()) {
            Map<String, Object> payload = objectMapper.readValue(is, Map.class);
            long orderId = ((Number) payload.get("orderId")).longValue();

            Order order = orderDao.getOrderById(orderId);
            if (order == null) {
                HttpUtils.sendTextResponse(exchange, "Order not found", 404);
                return;
            }

            Bill bill = billService.generateBillFromOrder(order);
            HttpUtils.sendJsonResponse(exchange, bill, 200);

        } catch (Exception e) {
            e.printStackTrace();
            HttpUtils.sendTextResponse(exchange, "Internal server error", 500);
        }
    }

    private void handleBillPayment(HttpExchange exchange) throws IOException {
        try (InputStream is = exchange.getRequestBody()) {
            Map<String, Object> payload = objectMapper.readValue(is, Map.class);
            long billId = ((Number) payload.get("billId")).longValue();

            boolean success = billService.markBillAsPaid(billId);
            HttpUtils.sendJsonResponse(exchange, Map.of("success", success), 200);

        } catch (Exception e) {
            e.printStackTrace();
            HttpUtils.sendTextResponse(exchange, "Internal server error", 500);
        }
    }
}