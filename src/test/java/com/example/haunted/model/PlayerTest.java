package com.example.haunted.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PlayerTest {
    private Inventory inventory;
    private Player player;

    //set up a new player object with an inventory before each test
    @BeforeEach
    public void setUp() {
        inventory = new Inventory(5);
        player = new Player("Big Ed", 50, 7, 2, inventory);
    }

    //player should definently be constructed correctly (happy path test)
    @Test
    void shouldInitializePlayerProperties() {
        assertEquals("Big Ed", player.getName());
        assertEquals(50, player.getMaxHealth());
        assertEquals(50, player.getHealth());
        assertEquals(7, player.getBaseAttack());
        assertEquals(2, player.getBaseDefense());
        assertSame(inventory, player.getInventory());
        assertNull(player.getEquippedWeapon());
        assertNull(player.getEquippedArmor());
        assertNull(player.getCurrentRoom());
        assertTrue(player.isAlive());
    }

    //can't have a player without a name or inventory
    @Test
    void shouldThrowExceptionWhenConstructorReceivesNull() {
        assertThrows(NullPointerException.class, () -> new Player(null, 50, 7, 2, inventory));
        assertThrows(NullPointerException.class, () -> new Player("Big Ed", 50, 7, 2, null));
    }

    //should set up and store the room correctly
    @Test
    void shouldSetAndGetCurrentRoom() {
        Room room = new Room("lib", "Ellis Library", "Starting point");
        player.setCurrentRoom(room);
        assertSame(room, player.getCurrentRoom());
        assertThrows(NullPointerException.class, () -> player.setCurrentRoom(null));
    }

    //testing various health values for the player to take damage from
    @ParameterizedTest
    @CsvSource({"15, 35, true", "50, 0, false", "80, 0, false", "0, 50, true", "-10, 50, true"})
    void shouldReduceHealthAndClampAtZero(int damage, int expectedHealth, boolean expectedAlive) {
        player.takeDamage(damage);
        assertEquals(expectedHealth, player.getHealth());
        assertEquals(expectedAlive, player.isAlive());
    }

    //testing various health values for the player to heal from
    @ParameterizedTest
    @CsvSource({"30, 10, 40", "30, 20, 50", "30, 50, 50", "30, 0, 30", "30, -15, 30"})
    void shouldHealAndClampAtMaxHealth(int startingHealth, int healAmount, int expectedHealth) {
        player.takeDamage(50 - startingHealth);
        player.heal(healAmount);
        assertEquals(expectedHealth, player.getHealth());
    }

    //attack power calcs (calculations) should work
    @Test
    void shouldCalculateAttackPowerWithAndWithoutWeapon() {
        assertEquals(7, player.getAttackPower());
        Weapon plank = new Weapon("Wooden plank", "2 by 4", 40);
        player.equipWeapon(plank);
        assertSame(plank, player.getEquippedWeapon());
        assertEquals(47, player.getAttackPower());
        assertThrows(NullPointerException.class, () -> player.equipWeapon(null));
    }

    //defense power calcs (calculations) should work
    @Test
    void shouldCalculateDefensePowerWithAndWithoutArmor() {
        assertEquals(2, player.getDefensePower());
        Armor hat = new Armor("Tinfoil hat", "Protects from 5G supposedly", 1);
        player.equipArmor(hat);
        assertSame(hat, player.getEquippedArmor());
        assertEquals(3, player.getDefensePower());
        assertThrows(NullPointerException.class, () -> player.equipArmor(null));
    }
}