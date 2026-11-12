package com.example.haunted.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

class WeaponTest {

    //should construct correctly (happy path tests)
    @Test
    void shouldInitializeWeaponProperties() {
        Weapon weapon = new Weapon("Ketchup packet", "Mcdonald's brand", 4);
        assertEquals("Ketchup packet", weapon.getName());
        assertEquals("Mcdonald's brand", weapon.getDescription());
        assertEquals(4, weapon.getAttackBonus());
        assertEquals("Ketchup packet", weapon.toString());
        assertTrue(weapon instanceof Equippable);
    }

    //testing some different bonuieses 
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 10, 50})
    void shouldHoldVariousAttackBonuses(int bonus) {
        Weapon weapon = new Weapon("Can of soda", "Cream soda", bonus);
        assertEquals(bonus, weapon.getAttackBonus());
    }
}