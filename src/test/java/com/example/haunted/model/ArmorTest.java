package com.example.haunted.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

class ArmorTest {

    //should contruct correctly (happy path)
    @Test
    void shouldInitializeArmorProperties() {
        Armor armor = new Armor("Cardboard box", "Brown box", 5);
        assertEquals("Cardboard box", armor.getName());
        assertEquals("Brown box", armor.getDescription());
        assertEquals(5, armor.getDefenseBonus());
        assertEquals("Cardboard box", armor.toString());
        assertTrue(armor instanceof Equippable);
    }

    //testing bonuses
    @ParameterizedTest
    @ValueSource(ints = {0, 3, 25})
    void shouldHoldVariousDefenseBonuses(int bonus) {
        Armor armor = new Armor("Wet paper bag", "Not good", bonus);
        assertEquals(bonus, armor.getDefenseBonus());
    }
}