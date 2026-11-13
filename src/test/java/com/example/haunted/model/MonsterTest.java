package com.example.haunted.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class MonsterTest {
	//initilzing varibles first
	private Monster testmonster;
    private Item testkey;
    
    // initialize test conditions
    @BeforeEach
    void setUp() {
    	  testkey = new Key("skeleton key", "opens doors");
          testmonster = new Monster("hotdog monster", 100, 10, 10, List.of(testkey));
    }
	
	
    //ensuring monster can construct
    @Test
    void shouldReturnMonsterProperties() {
    	assertEquals("hotdog monster", testmonster.getName());
        assertEquals(100, testmonster.getHealth());
        assertEquals(100, testmonster.getMaxHealth());
        assertEquals(10, testmonster.getAttack());
        assertEquals(10, testmonster.getDefense());
        assertTrue(testmonster.isAlive());
        assertEquals(1, testmonster.getLoot().size());
        assertSame(testkey, testmonster.getLoot().get(0));
    }
    
    // failure conditions when null input is recieved
    @Test
    void shouldThrowExceptionWhenReceivesNull() {
    	assertThrows(NullPointerException.class, () -> new Monster(null, 100, 10, 10, List.of()));
    	assertThrows(NullPointerException.class, () -> new Monster("hotdog monster", 100, 10, 10, null));
        }

    
    //ensuring loot list cant be modified 
    @Test
    void shouldReturnUnmodifiableLootList() {
        assertEquals(1, testmonster.getLoot().size());
        assertSame(testkey, testmonster.getLoot().get(0));
        assertThrows(UnsupportedOperationException.class, () -> testmonster.getLoot().add(testkey));
    }
    
    // boundry analysis tests using parametarized tests 
    @ParameterizedTest
    @CsvSource({"30, 70, true", "-20, 100, true", "100, 0, false", "150, 0, false"})
    void shouldTakeDamageAndTrackAliveState(int damage, int expectedHealth, boolean expectedAlive) {
    	testmonster.takeDamage(damage);
        assertEquals(expectedHealth, testmonster.getHealth());
        assertEquals(expectedAlive, testmonster.isAlive());
    }


    


}