package org.zeta.resturant;
import com.sun.net.httpserver.HttpServer;
import org.zeta.resturant.controller.AdminController;
import org.zeta.resturant.controller.BillController;
import org.zeta.resturant.controller.BookingController;
import org.zeta.resturant.controller.KitchenOrderController;
import org.zeta.resturant.controller.OrderController;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) throws IOException {
        int port = 8000;
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/bookings", new BookingController());
        server.createContext("/createOrder", new OrderController());
        server.createContext("/kitchen/orders", new KitchenOrderController());
        server.createContext("/kitchen/orders/markPrepared", new KitchenOrderController());
        server.createContext("/bill/generate", new BillController());
        server.createContext("/bill/pay", new BillController());
        server.createContext("/admin/update-price", new AdminController());
        server.createContext("/admin/sales-report", new AdminController());
        server.createContext("/health", exchange -> {
            String response = "Server is up and running!";
            exchange.sendResponseHeaders(200, response.length());
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        });
        server.setExecutor(null);
        server.start();
        System.out.println("✅ Server started on http://localhost:" + port);
    }
}