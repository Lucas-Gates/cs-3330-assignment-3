package com.example.haunted.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

class PotionTest {
    //potion should contruct correctly
    @Test
    void shouldReturnPotionProperties() {
        Potion potion = new Potion("Bug Juice", "Fruit drink", 200);
        assertEquals("Bug Juice", potion.getName());
        assertEquals("Fruit drink", potion.getDescription());
        assertEquals(200, potion.getHealingAmount());
        assertEquals("Bug Juice", potion.toString());
    }

    //testing various healing values
    @ParameterizedTest
    @CsvSource({"50, 20, 15, 35", "50, 45, 10, 50", "50, 10, 0, 10"})
    void shouldUsePotionToHealPlayerCorrectly(int maxHealth, int startHealth, int healAmount, int expectedHealth) {
        Player player = new Player("Alex", maxHealth, 6, 2, new Inventory(4));
        player.takeDamage(maxHealth - startHealth);
        Potion potion = new Potion("Fanta", "Vitamin C", healAmount);
        potion.use(player);
        assertEquals(expectedHealth, player.getHealth());
    }
}