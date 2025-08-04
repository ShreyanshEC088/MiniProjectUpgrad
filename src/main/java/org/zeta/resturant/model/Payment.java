package org.zeta.resturant.model;

import org.zeta.resturant.enums.PaymentMethod;
import java.time.LocalDateTime;

public class Payment {

    private long id;
    private Bill bill;
    private PaymentMethod paymentMethod;
    private LocalDateTime paidAt;

    public Payment(long id, Bill bill, PaymentMethod paymentMethod, LocalDateTime paidAt) {
        this.id = id;
        this.bill = bill;
        this.paymentMethod = paymentMethod;
        this.paidAt = paidAt;
    }
}
