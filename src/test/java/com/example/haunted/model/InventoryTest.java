package com.example.haunted.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

class InventoryTest {
    private Inventory inventory;

    //set up the inventory each time
    @BeforeEach
    void setUp() {
        inventory = new Inventory(2);
    }

    //initial state test
    @Test
    void shouldReturnInitialCapacityAndEmptyState() {
        assertEquals(2, inventory.getCapacity());
        assertFalse(inventory.isFull());
        assertEquals(0, inventory.getItems().size());
    }

    //the full function should work
    @Test
    void shouldEnforceCapacityLimits() {
        Key key1 = new Key("Key A", "First");
        Key key2 = new Key("Key B", "Second");
        Key key3 = new Key("Key C", "Third");
        assertTrue(inventory.addItem(key1));
        assertFalse(inventory.isFull());
        assertTrue(inventory.addItem(key2));
        assertTrue(inventory.isFull());
        assertFalse(inventory.addItem(key3));
        assertEquals(2, inventory.getItems().size());
    }

    //case should not be an issue
    @ParameterizedTest
    @ValueSource(strings = {"Energy Drink", "energy drink", "ENERGY DRINK", "EnErGy DrInK"})
    void shouldFindAndContainItemCaseInsensitively(String searchName) {
        Potion potion = new Potion("Energy Drink", "Restores energy", 15);
        inventory.addItem(potion);
        assertTrue(inventory.contains(searchName));
        Optional<Item> result = inventory.findItem(searchName);
        assertTrue(result.isPresent());
        assertSame(potion, result.get());
    }

    //if something isn't in the inv, isEmpty should work
    @Test
    void shouldReturnEmptyWhenItemNotFound() {
        assertFalse(inventory.contains("Notepad"));
        assertTrue(inventory.findItem("Notepad").isEmpty());
    }

    //should remove item
    @Test
    void shouldRemoveItemAndReturnRemovedInstance() {
        Potion icedTea = new Potion("Iced Tea", "Super sweet tea", 1000);
        inventory.addItem(icedTea);
        assertNull(inventory.removeItem("Coffee"));
        assertEquals(1, inventory.getItems().size());
        Item removed = inventory.removeItem("iCeD tEa");
        assertSame(icedTea, removed);
        assertEquals(0, inventory.getItems().size());
        assertFalse(inventory.contains("Iced Tea"));
        assertFalse(inventory.isFull());
    }

    //respect key items
    @Test
    void shouldReturnUnmodifiableItemsList() {
        List<Item> items = inventory.getItems();
        assertThrows(UnsupportedOperationException.class, () -> items.add(new Key("Key", "Desc")));
    }
}