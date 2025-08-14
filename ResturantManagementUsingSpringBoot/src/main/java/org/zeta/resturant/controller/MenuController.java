package org.zeta.resturant.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zeta.resturant.model.MenuItem;
import org.zeta.resturant.service.MenuService;
import java.util.List;

@RestController
@RequestMapping("/api/menu")
public class MenuController {

    private static final Logger logger = LoggerFactory.getLogger(MenuController.class);

    @Autowired
    private MenuService menuService;

    @GetMapping
    public List<MenuItem> getMenu() {
        logger.info("Fetching all menu items");
        List<MenuItem> items = menuService.getAllMenuItems();
        logger.info("Fetched {} menu items", items.size());
        return items;
    }

    @PostMapping
    public MenuItem addMenuItem(@RequestBody MenuItem item) {
        logger.info("Adding new menu item: {}", item.getName());
        MenuItem added = menuService.addMenuItem(item);
        logger.info("Menu item added with id: {}", added.getId());
        return added;
    }

    @PutMapping("/{id}")
    public MenuItem updateMenuItem(@PathVariable Long id, @RequestBody MenuItem item) {
        logger.info("Updating menu item with id: {}", id);
        MenuItem updated = menuService.updateMenuItem(id, item);
        logger.info("Menu item updated with id: {}", updated.getId());
        return updated;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMenuItem(@PathVariable Long id) {
        logger.info("Deleting menu item with id: {}", id);
        menuService.deleteMenuItem(id);
        logger.info("Menu item deleted with id: {}", id);
        return ResponseEntity.ok().build();
    }
}