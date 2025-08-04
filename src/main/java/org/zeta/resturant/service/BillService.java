package org.zeta.resturant.service;

import org.zeta.resturant.dao.BillDao;
import org.zeta.resturant.model.Bill;
import org.zeta.resturant.model.Order;
import org.zeta.resturant.model.OrderItem;

import java.time.LocalDateTime;

public class BillService {

    private final BillDao billDao = new BillDao();

    public Bill generateBillFromOrder(Order order) {
        double totalAmount = 0;
        for (OrderItem item : order.getOrderItems()) {
            totalAmount += item.getMenuItem().getPrice() * item.getQuantity();
        }

        Bill bill = new Bill(0L, order.getId(), totalAmount, false, LocalDateTime.now());
        long billId = billDao.generateBill(bill);
        bill.setId(billId);
        return bill;
    }

    public Bill getBillByOrderId(long orderId) {
        return billDao.getBillByOrderId(orderId);
    }

    public boolean markBillAsPaid(long billId) {
        return billDao.markAsPaid(billId);
    }
}

