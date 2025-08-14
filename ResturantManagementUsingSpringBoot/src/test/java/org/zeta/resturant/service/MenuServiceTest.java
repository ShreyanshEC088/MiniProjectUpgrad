package org.zeta.resturant.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.zeta.resturant.dao.MenuItemRepository;
import org.zeta.resturant.exceptions.InvalidQuantityException;
import org.zeta.resturant.exceptions.MenuItemNotFoundException;
import org.zeta.resturant.model.MenuItem;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MenuServiceTest {

    @Mock
    private MenuItemRepository menuRepo;

    @InjectMocks
    private MenuService menuService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllMenuItems_returnsList() {
        List<MenuItem> items = List.of(new MenuItem(), new MenuItem());
        when(menuRepo.findAll()).thenReturn(items);

        List<MenuItem> result = menuService.getAllMenuItems();

        assertEquals(items, result);
        verify(menuRepo).findAll();
    }

    @Test
    void addMenuItem_success() {
        MenuItem item = new MenuItem();
        item.setQuantity(5);
        when(menuRepo.save(item)).thenReturn(item);

        MenuItem result = menuService.addMenuItem(item);

        assertEquals(item, result);
        verify(menuRepo).save(item);
    }

    @Test
    void addMenuItem_invalidQuantity_throwsException() {
        MenuItem item = new MenuItem();
        item.setQuantity(-1);

        assertThrows(InvalidQuantityException.class, () -> menuService.addMenuItem(item));
        verify(menuRepo, never()).save(any());
    }

    @Test
    void updateMenuItem_success() {
        Long id = 1L;
        MenuItem existing = new MenuItem();
        existing.setId(id);
        MenuItem updated = new MenuItem();
        updated.setName("Pizza");
        updated.setDescription("Cheese");
        updated.setPrice(10.0);
        updated.setAvailable(true);
        updated.setQuantity(3);

        when(menuRepo.findById(id)).thenReturn(Optional.of(existing));
        when(menuRepo.save(existing)).thenReturn(existing);

        MenuItem result = menuService.updateMenuItem(id, updated);

        assertEquals("Pizza", existing.getName());
        assertEquals("Cheese", existing.getDescription());
        assertEquals(10.0, existing.getPrice());
        assertTrue(existing.isAvailable());
        assertEquals(3, existing.getQuantity());
        assertEquals(existing, result);
        verify(menuRepo).save(existing);
    }

    @Test
    void updateMenuItem_notFound_throwsException() {
        Long id = 2L;
        MenuItem updated = new MenuItem();
        when(menuRepo.findById(id)).thenReturn(Optional.empty());

        assertThrows(MenuItemNotFoundException.class, () -> menuService.updateMenuItem(id, updated));
        verify(menuRepo, never()).save(any());
    }

    @Test
    void deleteMenuItem_callsRepo() {
        menuService.deleteMenuItem(5L);
        verify(menuRepo).deleteById(5L);
    }
}