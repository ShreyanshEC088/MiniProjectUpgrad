package org.zeta.resturant.util;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sun.net.httpserver.HttpExchange;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class HttpUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    public static void sendJsonResponse(HttpExchange exchange, Object response, int statusCode) throws IOException {
        try {
            String json = objectMapper.writeValueAsString(response);
            System.out.println("Serialized JSON: " + json);  // DEBUG LOG
            byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(statusCode, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        } catch (Exception e) {
            e.printStackTrace(); // ← this will show what failed
            String err = "{\"error\":\"Internal Server Error\"}";
            byte[] errBytes = err.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(500, errBytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(errBytes);
            }
        }
    }


    public static void sendTextResponse(HttpExchange exchange, String message, int statusCode) throws IOException {
        byte[] bytes = message.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "text/plain");
        exchange.sendResponseHeaders(statusCode, bytes.length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}