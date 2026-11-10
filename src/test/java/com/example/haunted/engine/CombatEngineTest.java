//Cody
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
		DamageCalculator damageCalculator = new DamageCalculator();
		QuestTracker questTracker = new QuestTracker();
		
		CombatEngine engine = new CombatEngine(damageCalculator, questTracker);
		
		assertNotNull(engine);
	}
	
	@Test
	void testMonsterNotFound() {
		DamageCalculator damageCalculator = new DamageCalculator();
		QuestTracker questTracker = new QuestTracker();
		CombatEngine engine = new CombatEngine(damageCalculator, questTracker);
		
		Player player = new Player("Student Explorer", 50, 7, 2, new Inventory(8));
		Quest quest = new Quest("Escape the Basement",
                "Recover the Lost Gradebook and defeat the Final Exam Phantom.");
		
		CombatResult result = engine.attack(player, quest, null);
		
		assertFalse(result.isSuccess());
		assertEquals("Monster not found.", result.getMessage());
		assertEquals(0, result.getDamageToMonster());
		assertEquals(0, result.getDamageToPlayer());
		assertFalse(result.isMonsterDefeated());
		assertTrue(result.getDroppedItems().isEmpty());
	}
	
	@Test
	void testPlayerNotAlive() {
		DamageCalculator damageCalculator = new DamageCalculator();
		QuestTracker questTracker = new QuestTracker();
		CombatEngine engine = new CombatEngine(damageCalculator, questTracker);
		
		Player player = new Player("Student Explorer", 0, 7, 2, new Inventory(8));
		Quest quest = new Quest("Escape the Basement",
                "Recover the Lost Gradebook and defeat the Final Exam Phantom.");
		Monster monster = new Monster("Sleep-Deprived TA", 18, 6, 1,
                List.of(new Potion("Coffee Potion", "A backup coffee drop.", 15)));
		
		CombatResult result = engine.attack(player, quest, monster);
		
		assertFalse(result.isSuccess());
		assertEquals("Player is defeated.", result.getMessage());
		assertEquals(0, result.getDamageToMonster());
		assertEquals(0, result.getDamageToPlayer());
		assertFalse(result.isMonsterDefeated());
		assertTrue(result.getDroppedItems().isEmpty());
	}
	
	@Test
	void testMonsterNotAlive() {
		DamageCalculator damageCalculator = new DamageCalculator();
		QuestTracker questTracker = new QuestTracker();
		CombatEngine engine = new CombatEngine(damageCalculator, questTracker);
		
		Player player = new Player("Student Explorer", 50, 7, 2, new Inventory(8));
		Quest quest = new Quest("Escape the Basement",
                "Recover the Lost Gradebook and defeat the Final Exam Phantom.");
		Monster monster = new Monster("Sleep-Deprived TA", 0, 6, 1,
                List.of(new Potion("Coffee Potion", "A backup coffee drop.", 15)));
		
		CombatResult result = engine.attack(player, quest, monster);
		
		assertFalse(result.isSuccess());
		assertEquals("Monster is already defeated.", result.getMessage());
		assertEquals(0, result.getDamageToMonster());
		assertEquals(0, result.getDamageToPlayer());
		assertTrue(result.isMonsterDefeated());
		assertTrue(result.getDroppedItems().isEmpty());
	}
	
	@Test
	void testNormalAttack() {
		DamageCalculator damageCalculator = new DamageCalculator();
		QuestTracker questTracker = new QuestTracker();
		CombatEngine engine = new CombatEngine(damageCalculator, questTracker);
		
		Player player = new Player("Student Explorer", 50, 7, 2, new Inventory(8));
		Quest quest = new Quest("Escape the Basement",
                "Recover the Lost Gradebook and defeat the Final Exam Phantom.");
		Monster monster = new Monster("Sleep-Deprived TA", 18, 6, 1,
                List.of(new Potion("Coffee Potion", "A backup coffee drop.", 15)));
		
		CombatResult result = engine.attack(player, quest, monster);

		assertTrue(result.isSuccess());
		assertEquals("Attacked " + monster.getName() + ".", result.getMessage());
		assertEquals(damageCalculator.calculatePlayerDamage(player, monster), result.getDamageToMonster());
		assertEquals(damageCalculator.calculateMonsterDamage(monster, player), result.getDamageToPlayer());
		assertFalse(result.isMonsterDefeated());
		assertTrue(result.getDroppedItems().isEmpty());
	}
	
	@Test
	void testMonsterDefeated() {
		DamageCalculator damageCalculator = new DamageCalculator();
		QuestTracker questTracker = new QuestTracker();
		CombatEngine engine = new CombatEngine(damageCalculator, questTracker);
		
		Player player = new Player("Student Explorer", 50, 7, 2, new Inventory(8));
		Quest quest = new Quest("Escape the Basement",
                "Recover the Lost Gradebook and defeat the Final Exam Phantom.");
		Monster monster = new Monster("Sleep-Deprived TA", 1, 6, 1,
                List.of(new Potion("Coffee Potion", "A backup coffee drop.", 15)));
		Room stairwell = new Room("stairwell", "Maintenance Stairwell", "The flickering stairwell leading into the basement.");
		player.setCurrentRoom(stairwell);
		
		CombatResult result = engine.attack(player, quest, monster);
		
		assertTrue(result.isSuccess());
		assertEquals("Defeated " + monster.getName() + ".", result.getMessage());
		assertEquals(damageCalculator.calculatePlayerDamage(player, monster), result.getDamageToMonster());
		assertEquals(0, result.getDamageToPlayer());
		assertTrue(result.isMonsterDefeated());
		assertEquals(monster.getLoot(), result.getDroppedItems());
		
		assertEquals(monster.getLoot(), stairwell.getItems());
	}
}	