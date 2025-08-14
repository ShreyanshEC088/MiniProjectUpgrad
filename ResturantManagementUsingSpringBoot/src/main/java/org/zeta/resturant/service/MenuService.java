package org.zeta.resturant.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zeta.resturant.dao.MenuItemRepository;
import org.zeta.resturant.exceptions.InvalidQuantityException;
import org.zeta.resturant.exceptions.MenuItemNotFoundException;
import org.zeta.resturant.model.MenuItem;

import java.util.List;

@Service
public class MenuService {
    @Autowired
    private MenuItemRepository menuRepo;

    public List<MenuItem> getAllMenuItems() {
        return menuRepo.findAll();
    }

    public MenuItem addMenuItem(MenuItem menuItem) {
        if(menuItem.getQuantity() < 0) {
            throw new InvalidQuantityException("Quantity cannot be negative");
        }
        return menuRepo.save(menuItem);
    }

    public MenuItem updateMenuItem(Long id, MenuItem updatedItem) {
        return menuRepo.findById(id).map(item -> {
            item.setName(updatedItem.getName());
            item.setDescription(updatedItem.getDescription());
            item.setPrice(updatedItem.getPrice());
            item.setAvailable(updatedItem.isAvailable());
            item.setQuantity(updatedItem.getQuantity());
            return menuRepo.save(item);
        }).orElseThrow(() -> new MenuItemNotFoundException("Menu item with id " + id + " not found"));
    }
    public void deleteMenuItem(Long id) {
        menuRepo.deleteById(id);
    }
}
