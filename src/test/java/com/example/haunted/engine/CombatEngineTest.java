package com.example.haunted.engine;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import org.junit.jupiter.api.Test;

import com.example.haunted.events.CombatResult;
import com.example.haunted.model.Inventory;
import com.example.haunted.model.Item;
import com.example.haunted.model.Monster;
import com.example.haunted.model.Player;
import com.example.haunted.model.Potion;
import com.example.haunted.model.Room;
import com.example.haunted.model.Quest;
import com.example.haunted.rules.DamageCalculator;
import com.example.haunted.rules.QuestTracker;

public class CombatEngineTest {
	@Test
	void testConstructor() {
		// Arrange
		// objects for combat engine
		DamageCalculator damageCalculator = new DamageCalculator();
		QuestTracker questTracker = new QuestTracker();
		
		// Act
		CombatEngine engine = new CombatEngine(damageCalculator, questTracker);
		
		// Assert
		assertNotNull(engine);
	}
	
	@Test
	void testMonsterNotFound() {
		// Arrange
		// objects for combat engine
		DamageCalculator damageCalculator = new DamageCalculator();
		QuestTracker questTracker = new QuestTracker();
		CombatEngine engine = new CombatEngine(damageCalculator, questTracker);
		
		// objects for combat result
		Player player = new Player("Student Explorer", 50, 7, 2, new Inventory(8));
		Quest quest = new Quest("Escape the Basement",
                "Recover the Lost Gradebook and defeat the Final Exam Phantom.");
		
		// Act
		CombatResult result = engine.attack(player, quest, null);
		
		// Assert
		// returns for if null monster
		assertFalse(result.isSuccess());
		assertEquals("Monster not found.", result.getMessage());
		assertEquals(0, result.getDamageToMonster());
		assertEquals(0, result.getDamageToPlayer());
		assertFalse(result.isMonsterDefeated());
		assertTrue(result.getDroppedItems().isEmpty());
	}
	
	@Test
	void testPlayerNotAlive() {
		// Arrange
		// objects for combat engine
		DamageCalculator damageCalculator = new DamageCalculator();
		QuestTracker questTracker = new QuestTracker();
		CombatEngine engine = new CombatEngine(damageCalculator, questTracker);
		
		// objects for combat result
		Player player = new Player("Student Explorer", 0, 7, 2, new Inventory(8));
		Quest quest = new Quest("Escape the Basement",
                "Recover the Lost Gradebook and defeat the Final Exam Phantom.");
		Item potion = new Potion("Coffee Potion", "A backup coffee drop.", 15);
		Monster monster = new Monster("Sleep-Deprived TA", 18, 6, 1,
                List.of(potion));
		
		// Act
		CombatResult result = engine.attack(player, quest, monster);
		
		// Assert
		// returns for if player has no health
		assertFalse(result.isSuccess());
		assertEquals("Player is defeated.", result.getMessage());
		assertEquals(0, result.getDamageToMonster());
		assertEquals(0, result.getDamageToPlayer());
		assertFalse(result.isMonsterDefeated());
		assertTrue(result.getDroppedItems().isEmpty());
	}
	
	@Test
	void testMonsterNotAlive() {
		// Arrange
		// objects for combat engine
		DamageCalculator damageCalculator = new DamageCalculator();
		QuestTracker questTracker = new QuestTracker();
		CombatEngine engine = new CombatEngine(damageCalculator, questTracker);
		
		// objects for combat result
		Player player = new Player("Student Explorer", 50, 7, 2, new Inventory(8));
		Quest quest = new Quest("Escape the Basement",
                "Recover the Lost Gradebook and defeat the Final Exam Phantom.");
		Item potion = new Potion("Coffee Potion", "A backup coffee drop.", 15);
		Monster monster = new Monster("Sleep-Deprived TA", 0, 6, 1,
                List.of(potion));
		
		// Act
		CombatResult result = engine.attack(player, quest, monster);
		
		// Assert
		// returns for if monster has no health
		assertFalse(result.isSuccess());
		assertEquals("Monster is already defeated.", result.getMessage());
		assertEquals(0, result.getDamageToMonster());
		assertEquals(0, result.getDamageToPlayer());
		assertTrue(result.isMonsterDefeated());
		assertTrue(result.getDroppedItems().isEmpty());
	}
	
	@Test
	void testNormalAttack() {
		// Arrange
		// object to make combat engine
		DamageCalculator damageCalculator = new DamageCalculator();
		QuestTracker questTracker = new QuestTracker();
		CombatEngine engine = new CombatEngine(damageCalculator, questTracker);
		
		// objects for combat result
		Player player = new Player("Student Explorer", 50, 7, 2, new Inventory(8));
		Quest quest = new Quest("Escape the Basement",
                "Recover the Lost Gradebook and defeat the Final Exam Phantom.");
		Item potion = new Potion("Coffee Potion", "A backup coffee drop.", 15);
		Monster monster = new Monster("Sleep-Deprived TA", 18, 6, 1,
                List.of(potion));
		
		// Act
		CombatResult result = engine.attack(player, quest, monster);

		// Assert
		// check result when monster is only damaged normally
		assertTrue(result.isSuccess());
		assertEquals("Attacked " + monster.getName() + ".", result.getMessage());
		assertEquals(damageCalculator.calculatePlayerDamage(player, monster), result.getDamageToMonster());
		assertEquals(damageCalculator.calculateMonsterDamage(monster, player), result.getDamageToPlayer());
		assertFalse(result.isMonsterDefeated());
		assertTrue(result.getDroppedItems().isEmpty());
	}
	
	@Test
	void testMonsterDefeated() {
		// Arrange
		// object to make combat engine
		DamageCalculator damageCalculator = new DamageCalculator();
		QuestTracker questTracker = new QuestTracker();
		CombatEngine engine = new CombatEngine(damageCalculator, questTracker);
		
		// objects for Combat
		Player player = new Player("Student Explorer", 50, 7, 2, new Inventory(8));
		Quest quest = new Quest("Escape the Basement",
                "Recover the Lost Gradebook and defeat the Final Exam Phantom.");
		Item potion = new Potion("Coffee Potion", "A backup coffee drop.", 15);
		Monster monster = new Monster("Sleep-Deprived TA", 1, 6, 1,
                List.of(potion));
		Room stairwell = new Room("stairwell", "Maintenance Stairwell", "The flickering stairwell leading into the basement.");
		
		// Act
		player.setCurrentRoom(stairwell);
		CombatResult result = engine.attack(player, quest, monster);
		
		// Assert
		// check all the returns for when monster is killed
		assertTrue(result.isSuccess());
		assertEquals("Defeated " + monster.getName() + ".", result.getMessage());
		assertEquals(damageCalculator.calculatePlayerDamage(player, monster), result.getDamageToMonster());
		assertEquals(0, result.getDamageToPlayer());
		assertTrue(result.isMonsterDefeated());
		assertEquals(monster.getLoot(), result.getDroppedItems());
		
		// check item got put into the room
		assertEquals(monster.getLoot(), stairwell.getItems());
	}
}	