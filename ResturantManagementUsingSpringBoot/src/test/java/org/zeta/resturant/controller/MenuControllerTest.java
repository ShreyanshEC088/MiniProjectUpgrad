package org.zeta.resturant.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.zeta.resturant.model.MenuItem;
import org.zeta.resturant.service.MenuService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MenuControllerTest {

    @Mock
    private MenuService menuService;

    @InjectMocks
    private MenuController menuController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetMenu() {
        List<MenuItem> menu = Arrays.asList(new MenuItem(), new MenuItem());
        when(menuService.getAllMenuItems()).thenReturn(menu);

        List<MenuItem> result = menuController.getMenu();

        assertEquals(menu, result);
        verify(menuService).getAllMenuItems();
    }

    @Test
    void testAddMenuItem() {
        MenuItem item = new MenuItem();
        MenuItem savedItem = new MenuItem();
        when(menuService.addMenuItem(item)).thenReturn(savedItem);

        MenuItem result = menuController.addMenuItem(item);

        assertEquals(savedItem, result);
        verify(menuService).addMenuItem(item);
    }

    @Test
    void testUpdateMenuItem() {
        Long id = 1L;
        MenuItem item = new MenuItem();
        MenuItem updatedItem = new MenuItem();
        when(menuService.updateMenuItem(id, item)).thenReturn(updatedItem);

        MenuItem result = menuController.updateMenuItem(id, item);

        assertEquals(updatedItem, result);
        verify(menuService).updateMenuItem(id, item);
    }

    @Test
    void testDeleteMenuItem() {
        Long id = 1L;

        ResponseEntity<?> response = menuController.deleteMenuItem(id);

        assertEquals(ResponseEntity.ok().build(), response);
        verify(menuService).deleteMenuItem(id);
    }
}
