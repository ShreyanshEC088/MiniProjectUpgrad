package org.zeta.resturant.controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.zeta.resturant.dto.BookingRequest;
import org.zeta.resturant.dto.BookingResponse;
import org.zeta.resturant.service.BookingService;
import org.zeta.resturant.service.BookingServiceImpl;
import org.zeta.resturant.util.JsonUtil;
import org.zeta.resturant.util.HttpUtils;
import java.io.IOException;
import java.util.List;

public class BookingController implements HttpHandler{

    private BookingService bookingService = new BookingServiceImpl();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("POST".equals(exchange.getRequestMethod())) {
            BookingRequest request = JsonUtil.parseRequest(exchange.getRequestBody(), BookingRequest.class);
            BookingResponse responseDto = bookingService.bookTable(request);
            HttpUtils.sendJsonResponse(exchange, responseDto, 201);
        } else if ("GET".equals(exchange.getRequestMethod())) {
            List<BookingResponse> bookings = bookingService.getAllBookings();
            HttpUtils.sendJsonResponse(exchange, bookings, 200);
        } else {
            HttpUtils.sendTextResponse(exchange, "Method not allowed", 405);
        }
    }
}
