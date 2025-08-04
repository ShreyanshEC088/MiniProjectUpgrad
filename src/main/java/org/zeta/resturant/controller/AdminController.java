package org.zeta.resturant.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.zeta.resturant.model.SalesReport;
import org.zeta.resturant.service.AdminService;
import org.zeta.resturant.util.HttpUtils;
import org.zeta.resturant.util.QueryParser;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

public class AdminController implements HttpHandler {

    private final AdminService adminService = new AdminService();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();

        if (path.equals("/admin/update-price") && method.equalsIgnoreCase("POST")) {
            handleUpdatePrice(exchange);
        } else if (path.equals("/admin/sales-report") && method.equalsIgnoreCase("GET")) {
            handleSalesReport(exchange);
        } else {
            HttpUtils.sendTextResponse(exchange, "Not Found", 404);
        }
    }

    private void handleUpdatePrice(HttpExchange exchange) throws IOException {
        Map<String, Object> payload = new ObjectMapper().readValue(exchange.getRequestBody(), Map.class);
        long itemId = ((Number) payload.get("itemId")).longValue();
        double newPrice = ((Number) payload.get("newPrice")).doubleValue();

        boolean updated = adminService.updateMenuPrice(itemId, newPrice);
        HttpUtils.sendJsonResponse(exchange, Map.of("success", updated), 200);
    }

    private void handleSalesReport(HttpExchange exchange) throws IOException {
        Map<String, String> params = QueryParser.parse(exchange.getRequestURI().getQuery());
        LocalDate date = LocalDate.parse(params.getOrDefault("date", LocalDate.now().toString()));
        SalesReport report = adminService.getSalesReport(date);
        HttpUtils.sendJsonResponse(exchange, report, 200);
    }
}