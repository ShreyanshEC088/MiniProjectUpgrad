package org.zeta.resturant.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.zeta.resturant.model.Bill;
import org.zeta.resturant.service.BillingService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BillingControllerTest {

    @Mock
    private BillingService billingService;

    @InjectMocks
    private BillingController billingController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGenerateBill() {
        Long orderId = 1L;
        Bill mockBill = new Bill();
        when(billingService.generateBill(orderId)).thenReturn(mockBill);

        Bill result = billingController.generateBill(orderId);

        assertEquals(mockBill, result);
        verify(billingService).generateBill(orderId);
    }

    @Test
    void testPayBill() {
        Long billId = 2L;
        Bill mockBill = new Bill();
        when(billingService.payBill(billId)).thenReturn(mockBill);

        Bill result = billingController.payBill(billId);

        assertEquals(mockBill, result);
        verify(billingService).payBill(billId);
    }
}
