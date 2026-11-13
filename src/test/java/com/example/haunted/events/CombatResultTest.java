package com.example.haunted.events;
import com.example.haunted.model.*;
import org.junit.jupiter.api.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class CombatResultTest {
	//setting up the varibles
	private CombatResult testCombatResult;
	private Item testItem;
	
	@BeforeEach
	void setUp() {
		testItem = new Key("skeleton key", "opens all doors");
		List<Item> loot = new ArrayList<>();
		loot.add(testItem);

		// pass all 6 constructor parametrers: (success, message, damageToMonster, damageToPlayer, monsterDefeated, droppedItems)
		testCombatResult = new CombatResult(true, "Monster defeated", 25, 5, true, loot);
		
	}
	
	//null tests 
	@Test 
	void shouldThrowExceptionWhenConstructorReceivesNull() {
		// checking for a null array list
		assertThrows(NullPointerException.class, () -> new CombatResult(true, "test message", 25, 25, false, null));
	}
	
	//happy path tests 
	//verify the 6 parameters passed though testCombatResult are valid
	@Test 
	void shouldStoreAllCombatResults() {
		assertTrue(testCombatResult.isSuccess());
		assertEquals("Monster defeated", testCombatResult.getMessage());
		assertEquals(25, testCombatResult.getDamageToMonster());
		assertEquals(5,testCombatResult.getDamageToPlayer());
		assertTrue(testCombatResult.isMonsterDefeated());
		assertEquals(1, testCombatResult.getDroppedItems().size());
		assertSame(testItem, testCombatResult.getDroppedItems().get(0));
	}
	
	// boundry analysist tests 
	
	//ensuring the dropped item list doesnt change when combat result is created 
	@Test 
	void ShouldCopyDroppedItemsList() {
		List<Item> items = testCombatResult.getDroppedItems();
		assertThrows(UnsupportedOperationException.class, () -> items.add(new Key("extra key", "decription")));
	}
};