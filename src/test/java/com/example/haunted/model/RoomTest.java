package com.example.haunted.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

class RoomTest {
    private Room room;
    @BeforeEach
    void setUp() {
        room = new Room("study_lounge", "Quiet Study Lounge", "Couches and whiteboards.");
    }

    @Test
    void shouldStoreBasicProperties() {
        assertEquals("study_lounge", room.getId());
        assertEquals("Quiet Study Lounge", room.getName());
        assertEquals("Couches and whiteboards.", room.getDescription());
        assertFalse(room.isLocked());
        assertNull(room.getRequiredKeyName());
        assertNull(room.getTrap());
        assertTrue(room.getItems().isEmpty());
        assertTrue(room.getMonsters().isEmpty());
    }

    @Test
    void shouldThrowExceptionWhenConstructorReceivesNull() {
        assertThrows(NullPointerException.class, () -> new Room(null, "Room A", "Desc"));
        assertThrows(NullPointerException.class, () -> new Room("id_a", null, "Desc"));
        assertThrows(NullPointerException.class, () -> new Room("id_a", "Room A", null));
    }

    @ParameterizedTest
    @EnumSource(Direction.class)
    void shouldConnectExitsInAllDirections(Direction dir) {
        Room adjacent = new Room("hallway", "Main Hallway", "Long corridor.");
        room.connect(dir, adjacent);
        assertSame(adjacent, room.getExit(dir));
        Map<Direction, Room> exits = room.getExits();
        assertTrue(exits.containsKey(dir));
        assertSame(adjacent, exits.get(dir));
    }

    @Test
    void shouldThrowExceptionWhenConnectingNullDirectionOrRoom() {
        Room other = new Room("other", "Other", "Desc");
        assertThrows(NullPointerException.class, () -> room.connect(null, other));
        assertThrows(NullPointerException.class, () -> room.connect(Direction.NORTH, null));
    }

    @Test
    void shouldReturnUnmodifiableExitsMap() {
        Map<Direction, Room> exits = room.getExits();
        Room extra = new Room("extra", "Extra", "Desc");
        assertThrows(UnsupportedOperationException.class, () -> exits.put(Direction.NORTH, extra));
    }

    @Test
    void shouldManageItemsWithCaseInsensitiveLookupAndRemoval() {
        Key key = new Key("Desk Key", "Small silver key");
        room.addItem(key);
        assertEquals(1, room.getItems().size());
        assertThrows(UnsupportedOperationException.class, () -> room.getItems().add(new Key("Key 2", "Desc")));
        Optional<Item> foundUpper = room.findItem("DESK KEY");
        Optional<Item> foundLower = room.findItem("desk key");
        Optional<Item> missing = room.findItem("Pencil");
        assertTrue(foundUpper.isPresent());
        assertSame(key, foundUpper.get());
        assertTrue(foundLower.isPresent());
        assertSame(key, foundLower.get());
        assertTrue(missing.isEmpty());
        assertNull(room.removeItemByName("Missing Item"));
        Item removed = room.removeItemByName("dEsK kEy");
        assertSame(key, removed);
        assertEquals(0, room.getItems().size());
        assertTrue(room.findItem("Desk Key").isEmpty());
    }

    @Test
    void shouldThrowExceptionWhenAddingNullItem() {
        assertThrows(NullPointerException.class, () -> room.addItem(null));
    }

    @Test
    void shouldTrackMonstersAndLivingState() {
        assertFalse(room.hasLivingMonsters());
        Monster ghost = new Monster("Basement Ghost", 20, 5, 2, List.of());
        room.addMonster(ghost);
        assertEquals(1, room.getMonsters().size());
        assertThrows(UnsupportedOperationException.class, () -> room.getMonsters().add(ghost));
        assertTrue(room.hasLivingMonsters());
        Optional<Monster> found = room.findMonster("bAsEmEnT gHoSt");
        assertTrue(found.isPresent());
        assertSame(ghost, found.get());
        assertTrue(room.findMonster("Zombie").isEmpty());
        ghost.takeDamage(20);
        assertFalse(ghost.isAlive());
        assertFalse(room.hasLivingMonsters());
    }

    @Test
    void shouldThrowExceptionWhenAddingNullMonster() {
        assertThrows(NullPointerException.class, () -> room.addMonster(null));
    }

    @Test
    void shouldHandleRoomLockingAndUnlockingWithKey() {
        assertTrue(room.unlock("Any Key"));
        room.setLocked(true, "Office Key");
        assertTrue(room.isLocked());
        assertEquals("Office Key", room.getRequiredKeyName());
        assertFalse(room.unlock("Wrong Key"));
        assertFalse(room.unlock(null));
        assertTrue(room.isLocked());
        assertTrue(room.unlock("office key"));
        assertFalse(room.isLocked());
        room.setLocked(true, null);
        assertFalse(room.unlock("Office Key"));
    }

    @Test
    void shouldSetAndGetTrap() {
        Trap trap = new Trap("Spike Trap", TrapType.STEAM, 8, true, true);
        room.setTrap(trap);
        assertSame(trap, room.getTrap());
        room.setTrap(null);
        assertNull(room.getTrap());
    }
}