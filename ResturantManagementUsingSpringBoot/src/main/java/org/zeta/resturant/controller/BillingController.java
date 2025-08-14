package org.zeta.resturant.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zeta.resturant.model.Bill;
import org.zeta.resturant.service.BillingService;

@RestController
@RequestMapping("/api/bills")
public class BillingController {

    private static final Logger logger = LoggerFactory.getLogger(BillingController.class);

    @Autowired
    private BillingService billingService;

    @PostMapping("/generate/{orderId}")
    public Bill generateBill(@PathVariable Long orderId) {
        logger.info("Generating bill for orderId: {}", orderId);
        Bill bill = billingService.generateBill(orderId);
        logger.info("Bill generated with id: {}", bill.getId());
        return bill;
    }

    @PostMapping("/pay/{billId}")
    public Bill payBill(@PathVariable Long billId) {
        logger.info("Paying bill with id: {}", billId);
        Bill bill = billingService.payBill(billId);
        logger.info("Bill paid with id: {}", bill.getId());
        return bill;
    }
}