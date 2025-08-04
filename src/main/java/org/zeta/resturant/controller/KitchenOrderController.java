package org.zeta.resturant.controller;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.zeta.resturant.model.KitchenOrder;
import org.zeta.resturant.service.KitchenOrderService;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.nio.charset.StandardCharsets;

public class KitchenOrderController implements HttpHandler {

    private final KitchenOrderService service = new KitchenOrderService();
    private final Gson gson = new Gson();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();

        if ("/kitchen/orders".equals(path) && "GET".equals(method)) {
            handleGetLiveOrders(exchange);
        } else if ("/kitchen/orders/markPrepared".equals(path) && "POST".equals(method)) {
            handleMarkAsPrepared(exchange);
        } else {
            String response = "Unsupported endpoint";
            exchange.sendResponseHeaders(404, response.getBytes().length);
            exchange.getResponseBody().write(response.getBytes());
            exchange.close();
        }
    }

    private void handleGetLiveOrders(HttpExchange exchange) throws IOException {
        List<KitchenOrder> orders = service.getLiveOrders();
        String response = gson.toJson(orders);
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, response.getBytes(StandardCharsets.UTF_8).length);
        exchange.getResponseBody().write(response.getBytes(StandardCharsets.UTF_8));
        exchange.close();
    }

    private void handleMarkAsPrepared(HttpExchange exchange) throws IOException {
        InputStream is = exchange.getRequestBody();
        String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        Map<String, Object> payload = gson.fromJson(body, Map.class);

        long id = ((Number) payload.get("kitchenOrderId")).longValue();
        boolean success = service.markOrderPrepared(id);

        String response = gson.toJson(Map.of("success", success));
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, response.getBytes(StandardCharsets.UTF_8).length);
        exchange.getResponseBody().write(response.getBytes(StandardCharsets.UTF_8));
        exchange.close();
    }
}
