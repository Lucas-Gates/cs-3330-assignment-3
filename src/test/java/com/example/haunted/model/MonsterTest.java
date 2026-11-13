package com.example.haunted.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class MonsterTest {
	//initilzing varibles first
	private Monster testmonster;
    private Item testkey;
    
    // initialize test conditions
    @BeforeEach
    void setUp() {
    	 Item testkey = new Key("skeleton key", "opens doors");
         Monster testmonster = new Monster("hotdog monster", 100, 10, 10, List.of(testkey));
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
        assertEquals(1, testMonster.getLoot().size());
        assertSame(testkey, testMonster.getLoot().get(0));
        assertThrows(UnsupportedOperationException.class, () -> testMonster.getLoot().add(key));
    }

    


}