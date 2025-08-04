package org.zeta.resturant.service;

import org.zeta.resturant.dao.AdminDao;
import org.zeta.resturant.model.SalesReport;

import java.time.LocalDate;

public class AdminService {
    private final AdminDao adminDao = new AdminDao();

    public boolean updateMenuPrice(long menuItemId, double newPrice) {
        return adminDao.updateMenuItemPrice(menuItemId, newPrice);
    }

    public SalesReport getSalesReport(LocalDate date) {
        return adminDao.getDailySalesReport(date);
    }
}

