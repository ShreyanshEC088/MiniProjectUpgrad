package org.zeta.resturant.service;

import org.zeta.resturant.dao.KitchenOrderDao;
import org.zeta.resturant.model.KitchenOrder;

import java.util.List;

public class KitchenOrderService {
    private final KitchenOrderDao dao = new KitchenOrderDao();

    public List<KitchenOrder> getLiveOrders() {
        return dao.getLiveKitchenOrders();
    }

    public boolean markOrderPrepared(long kitchenOrderId) {
        return dao.markAsPrepared(kitchenOrderId);
    }
}

