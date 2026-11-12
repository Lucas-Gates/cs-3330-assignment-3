package com.example.haunted.engine;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import org.junit.jupiter.api.Test;

import com.example.haunted.events.CombatResult;
import com.example.haunted.events.InteractionResult;
import com.example.haunted.events.MoveResult;
import com.example.haunted.model.Armor;
import com.example.haunted.model.Direction;
import com.example.haunted.model.Inventory;
import com.example.haunted.model.Item;
import com.example.haunted.model.Key;
import com.example.haunted.model.Monster;
import com.example.haunted.model.Player;
import com.example.haunted.model.Potion;
import com.example.haunted.model.Quest;
import com.example.haunted.model.Room;
import com.example.haunted.rules.DamageCalculator;
import com.example.haunted.rules.QuestTracker;
import com.example.haunted.rules.TrapResolver;

public class GameEngineTest {
	@Test
	void testConstructor() {
		// Arrange
		QuestTracker questTracker = new QuestTracker();
		MovementEngine movementEngine = new MovementEngine(new TrapResolver());
        CombatEngine combatEngine = new CombatEngine(new DamageCalculator(), questTracker);
        InteractionEngine interactionEngine = new InteractionEngine(questTracker);
        
        Player player = new Player("Student Explorer", 50, 7, 2, new Inventory(8));
		Quest quest = new Quest("Escape the Basement",
                "Recover the Lost Gradebook and defeat the Final Exam Phantom.");
		
        
		// Act
		GameEngine gameEngine = new GameEngine(player, quest, movementEngine,
									combatEngine, interactionEngine);
        
		// Assert
		assertNotNull(gameEngine);
	}
	
	GameEngine createTestGame(Player player, Quest quest) {
		QuestTracker questTracker = new QuestTracker();
		MovementEngine movementEngine = new MovementEngine(new TrapResolver());
        CombatEngine combatEngine = new CombatEngine(new DamageCalculator(), questTracker);
        InteractionEngine interactionEngine = new InteractionEngine(questTracker);
		
		GameEngine engine = new GameEngine(player, quest, movementEngine,
				combatEngine, interactionEngine);
		
		return engine;
	}
	
	@Test
	void testMove() {
		// Arrange
		Player player = new Player("Student Explorer", 50, 7, 2, new Inventory(8));
		GameEngine engine = createTestGame(player, null);
		
		Room stairwell = new Room("stairwell", "Maintenance Stairwell", "The flickering stairwell leading into the basement.");
        Room lectureHall = new Room("lectureHall", "Abandoned Lecture Hall", "Dusty desks and a broken projector.");

		// Act
		stairwell.connect(Direction.EAST, lectureHall);
		player.setCurrentRoom(stairwell);
		MoveResult result = engine.move(Direction.EAST);
		
		// Assert
		assertTrue(result.isSuccess());
		assertEquals("Moved to Abandoned Lecture Hall.", result.getMessage());
		assertFalse(result.isTrapTriggered());
		assertEquals(0, result.getTrapDamage());
	}
	
	@Test
	void testPickUpItem() {
		// Arrange
        Player player = new Player("Student Explorer", 50, 7, 2, new Inventory(8));
		GameEngine engine = createTestGame(player, null);
        
		Room stairwell = new Room("stairwell", "Maintenance Stairwell", "The flickering stairwell leading into the basement.");
		Item potion = new Potion("Coffee Potion", "A suspiciously warm cup of coffee.", 15);
		
		// Act
		stairwell.addItem(potion);
		player.setCurrentRoom(stairwell);
		InteractionResult result = engine.pickUpItem(potion.getName());
		
		// Assert
		assertTrue(result.isSuccess());
		assertEquals("Picked up Coffee Potion.", result.getMessage());
	}
	
	@Test
	void testEquipItem() {
		// Arrange
        Player player = new Player("Student Explorer", 50, 7, 2, new Inventory(8));
		GameEngine engine = createTestGame(player, null);
        
		Room stairwell = new Room("stairwell", "Maintenance Stairwell", "The flickering stairwell leading into the basement.");
		Item armor = new Armor("Calculator Shield", "A battered calculator used as a shield.", 3);
		
		// Act
		stairwell.addItem(armor);
		player.setCurrentRoom(stairwell);
		InteractionResult result = engine.pickUpItem(armor.getName());
		result = engine.equipItem(armor.getName());
		
		// Assert
		assertTrue(result.isSuccess());
		assertEquals("Equipped armor Calculator Shield.", result.getMessage());
	}
	
	@Test
	void testUseItem() {
		// Arrange
        Player player = new Player("Student Explorer", 50, 7, 2, new Inventory(8));
        GameEngine engine = createTestGame(player, null);
        
		Room stairwell = new Room("stairwell", "Maintenance Stairwell", "The flickering stairwell leading into the basement.");	
		Item potion = new Potion("Coffee Potion", "A suspiciously warm cup of coffee.", 15);
		
		// Act
		stairwell.addItem(potion);
		player.setCurrentRoom(stairwell);
		InteractionResult result = engine.pickUpItem(potion.getName());
		result = engine.useItem(potion.getName());
		
		// Assert
		assertTrue(result.isSuccess());
		assertEquals("Used Coffee Potion.", result.getMessage());	
	}
	
	@Test
	void testUnlockRoom() {
		// Arrange
        Player player = new Player("Student Explorer", 50, 7, 2, new Inventory(8));
        GameEngine engine = createTestGame(player, null);
        
        Room stairwell = new Room("stairwell", "Maintenance Stairwell", "The flickering stairwell leading into the basement.");	
        Room lectureHall = new Room("lectureHall", "Abandoned Lecture Hall", "Dusty desks and a broken projector.");
        Item key = new Key("Archive Key", "Opens the Exam Archive.");
		
		// Act
		stairwell.addItem(key);
		stairwell.connect(Direction.EAST, lectureHall);
		lectureHall.setLocked(true, key.getName());
		player.setCurrentRoom(stairwell);
		InteractionResult result = engine.pickUpItem(key.getName());
		result = engine.unlockRoom(Direction.EAST);
		
		// Assert
		assertTrue(result.isSuccess());
		assertEquals("Unlocked Abandoned Lecture Hall.", result.getMessage());
	}
	
	@Test
	void testAttack() {
	    // Arrange
	    Player player = new Player("Student Explorer", 50, 7, 2, new Inventory(8));
	    GameEngine engine = createTestGame(player, null);
	    Item potion = new Potion("Coffee Potion", "A backup coffee drop.", 15);
	    Room stairwell = new Room("stairwell", "Maintenance Stairwell",
	            "The flickering stairwell leading into the basement.");
	    Monster monster = new Monster("Sleep-Deprived TA", 18, 6, 1,
	            List.of(potion));

	    // Act
	    stairwell.addMonster(monster);
	    player.setCurrentRoom(stairwell);

	    CombatResult result = engine.attack(monster.getName());

	    // Assert
	    assertTrue(result.isSuccess());
	    assertEquals("Attacked Sleep-Deprived TA.", result.getMessage());
	    assertEquals(6, result.getDamageToMonster());
	    assertEquals(4, result.getDamageToPlayer());
	    assertFalse(result.isMonsterDefeated());
	    assertTrue(result.getDroppedItems().isEmpty());
	}
	
	@Test
	void testGetPlayer() {
		// Arrange
		Player player = new Player("Student Explorer", 50, 7, 2, new Inventory(8));
        GameEngine engine = createTestGame(player, null);
        
        // Act
        Player p2 = engine.getPlayer();
        
        // Assert
        assertSame(player, p2);
	}
	
	@Test
	void testGetQuest() {
		// Arrange
		Player player = new Player("Student Explorer", 50, 7, 2, new Inventory(8));
		Quest quest = new Quest("Escape the Basement", "Recover the Lost Gradebook and defeat the Final Exam Phantom.");
        GameEngine engine = createTestGame(player, quest);
        
        // Act
        Quest q2 = engine.getQuest();
        
        // Assert
        assertSame(quest, q2);
	}
	
	@Test
	void testGetCurrentRoom() {
		// Arrange
		Player player = new Player("Student Explorer", 50, 7, 2, new Inventory(8));
        GameEngine engine = createTestGame(player, null);
        Room stairwell = new Room("stairwell", "Maintenance Stairwell", "The flickering stairwell leading into the basement.");	

        // Act
        player.setCurrentRoom(stairwell);
        Room room = engine.getCurrentRoom();
        
        // Assert
        assertSame(stairwell, room);
	}
	
	@Test
	void testGameIsNotOver() {
		// Arrange
		Player player = new Player("Student Explorer", 50, 7, 2, new Inventory(8));
        GameEngine engine = createTestGame(player, null);
        
        // Act
        boolean game = engine.isGameOver();
        
        // Assert
        assertFalse(game);
	}
	
	@Test
	void testGameIsOver() {
		// Arrange
		Player player = new Player("Student Explorer", 0, 7, 2, new Inventory(8));
        GameEngine engine = createTestGame(player, null);
        
        // Act
        boolean game = engine.isGameOver();
        
        // Assert
        assertTrue(game);
	}
	
	@Test
	void testGameNotWonQuestNotComplete() {
		// Arrange
		Player player = new Player("Student Explorer", 50, 7, 2, new Inventory(8));
		Quest quest = new Quest("Escape the Basement", "Recover the Lost Gradebook and defeat the Final Exam Phantom.");
		GameEngine engine = createTestGame(player, quest);
		
		// Act
		boolean won = engine.isGameWon();
		
		// Assert
		assertFalse(won);
	}
	
	@Test
	void testGameNotWonPlayerDead() {
		// Arrange
		Player player = new Player("Student Explorer", 0, 7, 2, new Inventory(8));
		Quest quest = new Quest("Escape the Basement", "Recover the Lost Gradebook and defeat the Final Exam Phantom.");
		GameEngine engine = createTestGame(player, quest);
		
		// Act
		quest.markGradebookRecovered();
		quest.markPhantomDefeated();
		quest.updateStatus();
		boolean won = engine.isGameWon();
		
		// Assert
		assertFalse(won);
	}
	
	@Test
	void testGameIsWon() {
		// Arrange
		Player player = new Player("Student Explorer", 50, 7, 2, new Inventory(8));
		Quest quest = new Quest("Escape the Basement", "Recover the Lost Gradebook and defeat the Final Exam Phantom.");
		GameEngine engine = createTestGame(player, quest);
		
		// Act
		quest.markGradebookRecovered();
		quest.markPhantomDefeated();
		quest.updateStatus();
		boolean won = engine.isGameWon();
		
		// Assert
		assertTrue(won);
	}
}