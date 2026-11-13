package com.example.haunted.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class BossMonsterTest {
	private BossMonster testbossmonster;
	
	
	//setting up testing space 
	@BeforeEach
    void setUp() {
		//name, health, attack, defense, loot item list, enragedattackbonus
		testbossmonster = new BossMonster("super hotdog monster", 100, 10, 10, List.of(), 10);
    }
	
	//happy path test to ensure all constructors pass varibles (inherits from monster file)
	@Test
	void shouldStoreAllProperties() {
		assertEquals("super hotdog monster", testbossmonster.getName());
        assertEquals(100, testbossmonster.getHealth());
        assertEquals(10, testbossmonster.getAttack());
        assertEquals(10, testbossmonster.getCurrentAttack());
	}
	// test enrage boundry with a parameterized test csv 
	@ParameterizedTest
    @CsvSource({"49, 10", "50, 20", "90, 20"})
    void shouldApplyEnragedBonusAtOrBelowHalfHealth(int damageTaken, int expectedAttack) {
		testbossmonster.takeDamage(damageTaken);
        assertEquals(expectedAttack, testbossmonster.getCurrentAttack());
    }
	
	// null value handling so program doesnt crash
	@Test
    void shouldThrowExceptionWhenConstructorReceivesNull() {
        assertThrows(NullPointerException.class, () -> new BossMonster(null, 100, 10, 10, List.of(), 10));
        assertThrows(NullPointerException.class, () -> new BossMonster("super hotdog monster", 100, 10, 10, null, 10));
    }
	
};
