package com.example.haunted.engine;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.example.haunted.events.InteractionResult;
import com.example.haunted.model.Armor;
import com.example.haunted.model.Direction;
import com.example.haunted.model.Inventory;
import com.example.haunted.model.Item;
import com.example.haunted.model.Key;
import com.example.haunted.model.Player;
import com.example.haunted.model.Potion;
import com.example.haunted.model.Room;
import com.example.haunted.model.Weapon;
import com.example.haunted.rules.QuestTracker;
import com.example.haunted.model.Quest;

public class InteractionEngineTest {
	@Test
	void testConstructor() {
		// Arrange
		QuestTracker questTracker = new QuestTracker();
		
		// Act
		InteractionEngine engine = new InteractionEngine(questTracker);

		// Assert
		assertNotNull(engine);
	}
	
	@Test
	void testItemNull() {
		// Arrange
		QuestTracker questTracker = new QuestTracker();
		InteractionEngine engine = new InteractionEngine(questTracker);
		Player player = new Player("Student Explorer", 50, 7, 2, new Inventory(8));
		Room stairwell = new Room("stairwell", "Maintenance Stairwell", "The flickering stairwell leading into the basement.");
		Quest quest = new Quest("Escape the Basement",
                "Recover the Lost Gradebook and defeat the Final Exam Phantom.");
		
		// Act
		player.setCurrentRoom(stairwell);
		InteractionResult result = engine.pickUpItem(player, quest, null);
		
		// Arrange
		assertFalse(result.isSuccess());
		assertEquals("Item not found in the room.", result.getMessage());
	}
	
	@Test
	void testFullInventory() {
		// Arrange
		QuestTracker questTracker = new QuestTracker();
		InteractionEngine engine = new InteractionEngine(questTracker);
		Player player = new Player("Student Explorer", 50, 7, 2, new Inventory(0));
		Room stairwell = new Room("stairwell", "Maintenance Stairwell", "The flickering stairwell leading into the basement.");
		Quest quest = new Quest("Escape the Basement",
                "Recover the Lost Gradebook and defeat the Final Exam Phantom.");
		Item potion = new Potion("Coffee Potion", "A suspiciously warm cup of coffee.", 15);
		
		// Act
		stairwell.addItem(potion);
		player.setCurrentRoom(stairwell);
		InteractionResult result = engine.pickUpItem(player, quest, potion.getName());
		
		// Assert
		assertFalse(result.isSuccess());
		assertEquals("Inventory is full.", result.getMessage());
	}
	
	@Test
	void testNormalPickUpItem() {
		// Arrange
		QuestTracker questTracker = new QuestTracker();
		InteractionEngine engine = new InteractionEngine(questTracker);
		Player player = new Player("Student Explorer", 50, 7, 2, new Inventory(8));
		Room stairwell = new Room("stairwell", "Maintenance Stairwell", "The flickering stairwell leading into the basement.");
		Quest quest = new Quest("Escape the Basement",
                "Recover the Lost Gradebook and defeat the Final Exam Phantom.");
		Item potion = new Potion("Coffee Potion", "A suspiciously warm cup of coffee.", 15);
		
		// Act
		stairwell.addItem(potion);
		player.setCurrentRoom(stairwell);
		InteractionResult result = engine.pickUpItem(player, quest, potion.getName());
		
		// Assert
		assertTrue(result.isSuccess());
		assertEquals("Picked up Coffee Potion.", result.getMessage());
	}
	
	@Test
	void testEmptyUseItem() {
		// Arrange
		QuestTracker questTracker = new QuestTracker();
		InteractionEngine engine = new InteractionEngine(questTracker);
		Player player = new Player("Student Explorer", 50, 7, 2, new Inventory(8));
		
		// Act
		InteractionResult result = engine.useItem(player, null);
		
		// Assert
		assertFalse(result.isSuccess());
		assertEquals("Item not found in inventory.", result.getMessage());
	}
	
	@Test
	void testNotPotion() {
		// Arrange
		QuestTracker questTracker = new QuestTracker();
		InteractionEngine engine = new InteractionEngine(questTracker);
		Player player = new Player("Student Explorer", 50, 7, 2, new Inventory(8));
		Quest quest = new Quest("Escape the Basement",
                "Recover the Lost Gradebook and defeat the Final Exam Phantom.");
		Room stairwell = new Room("stairwell", "Maintenance Stairwell", "The flickering stairwell leading into the basement.");
		Item key = new Key("Archive Key", "Opens the Exam Archive.");
		
		// Act
		stairwell.addItem(key);
		player.setCurrentRoom(stairwell);
		InteractionResult result = engine.pickUpItem(player, quest, key.getName());
		result = engine.useItem(player, key.getName());
		
		// Assert
		assertFalse(result.isSuccess());
		assertEquals("That item cannot be used.", result.getMessage());
	}
	
	@Test
	void testNormalUseItem() {
		// Arrange
		QuestTracker questTracker = new QuestTracker();
		InteractionEngine engine = new InteractionEngine(questTracker);
		Player player = new Player("Student Explorer", 50, 7, 2, new Inventory(8));
		Quest quest = new Quest("Escape the Basement",
                "Recover the Lost Gradebook and defeat the Final Exam Phantom.");
		Room stairwell = new Room("stairwell", "Maintenance Stairwell", "The flickering stairwell leading into the basement.");
		Item potion = new Potion("Coffee Potion", "A suspiciously warm cup of coffee.", 15);
		
		// Act
		stairwell.addItem(potion);
		player.setCurrentRoom(stairwell);
		InteractionResult result = engine.pickUpItem(player, quest, potion.getName());
		result = engine.useItem(player, potion.getName());
		
		// Assert
		assertTrue(result.isSuccess());
		assertEquals("Used Coffee Potion.", result.getMessage());
	}
	
	@Test
	void testEmptyEquipItem() {
		// Arrange
		QuestTracker questTracker = new QuestTracker();
		InteractionEngine engine = new InteractionEngine(questTracker);
		
		Player player = new Player("Student Explorer", 50, 7, 2, new Inventory(8));
		
		// Act
		InteractionResult result = engine.equipItem(player, null);
		
		// Assert
		assertFalse(result.isSuccess());
		assertEquals("Item not found in inventory.", result.getMessage());
	}
	
	@Test
	void testNotWeapon() {
		// Arrange
		QuestTracker questTracker = new QuestTracker();
		InteractionEngine engine = new InteractionEngine(questTracker);
		Player player = new Player("Student Explorer", 50, 7, 2, new Inventory(8));
		Quest quest = new Quest("Escape the Basement",
                "Recover the Lost Gradebook and defeat the Final Exam Phantom.");
		Room stairwell = new Room("stairwell", "Maintenance Stairwell", "The flickering stairwell leading into the basement.");
		Item key = new Key("Archive Key", "Opens the Exam Archive.");
		
		// Act
		stairwell.addItem(key);
		player.setCurrentRoom(stairwell);
		InteractionResult result = engine.pickUpItem(player, quest, key.getName());
		result = engine.equipItem(player, key.getName());
		
		// Assert
		assertFalse(result.isSuccess());
		assertEquals("That item cannot be equipped.", result.getMessage());
	}
	
	@Test
	void testWeaponEquipItem() {
		// Arrange
		QuestTracker questTracker = new QuestTracker();
		InteractionEngine engine = new InteractionEngine(questTracker);
		Player player = new Player("Student Explorer", 50, 7, 2, new Inventory(8));
		Quest quest = new Quest("Escape the Basement",
                "Recover the Lost Gradebook and defeat the Final Exam Phantom.");
		Room stairwell = new Room("stairwell", "Maintenance Stairwell", "The flickering stairwell leading into the basement.");
		Item weapon = new Weapon("Stapler of Justice", "A heavy-duty stapler with righteous force.", 4);
		
		// Act
		stairwell.addItem(weapon);
		player.setCurrentRoom(stairwell);
		InteractionResult result = engine.pickUpItem(player, quest, weapon.getName());
		result = engine.equipItem(player, weapon.getName());
		
		// Assert
		assertTrue(result.isSuccess());
		assertEquals("Equipped weapon Stapler of Justice.", result.getMessage());
	}
	
	@Test
	void testArmorEquipItem() {
		// Arrange
		QuestTracker questTracker = new QuestTracker();
		InteractionEngine engine = new InteractionEngine(questTracker);
		Player player = new Player("Student Explorer", 50, 7, 2, new Inventory(8));
		Quest quest = new Quest("Escape the Basement",
                "Recover the Lost Gradebook and defeat the Final Exam Phantom.");
		Room stairwell = new Room("stairwell", "Maintenance Stairwell", "The flickering stairwell leading into the basement.");
		Item armor = new Armor("Calculator Shield", "A battered calculator used as a shield.", 3);
		
		// Act
		stairwell.addItem(armor);
		player.setCurrentRoom(stairwell);
		InteractionResult result = engine.pickUpItem(player, quest, armor.getName());
		result = engine.equipItem(player, armor.getName());
		
		// Assert
		assertTrue(result.isSuccess());
		assertEquals("Equipped armor Calculator Shield.", result.getMessage());
	}
	
	@Test
	void testNullRoom() {
		// Arrange
		QuestTracker questTracker = new QuestTracker();
		InteractionEngine engine = new InteractionEngine(questTracker);
		Player player = new Player("Student Explorer", 50, 7, 2, new Inventory(8));
		Room stairwell = new Room("stairwell", "Maintenance Stairwell", "The flickering stairwell leading into the basement.");
		
		// Act
		player.setCurrentRoom(stairwell);
		InteractionResult result = engine.unlockRoom(player, Direction.NORTH);
		
		// Assert
		assertFalse(result.isSuccess());
		assertEquals("There is no room in that direction.", result.getMessage());
	}
	
	@Test
	void testUnlockedRoom() {
		// Arrange
		QuestTracker questTracker = new QuestTracker();
		InteractionEngine engine = new InteractionEngine(questTracker);
		Player player = new Player("Student Explorer", 50, 7, 2, new Inventory(8));
		Room stairwell = new Room("stairwell", "Maintenance Stairwell", "The flickering stairwell leading into the basement.");
		Room lectureHall = new Room("lectureHall", "Abandoned Lecture Hall", "Dusty desks and a broken projector.");
	
		// Act
		stairwell.connect(Direction.EAST, lectureHall);
		player.setCurrentRoom(stairwell);
		InteractionResult result = engine.unlockRoom(player, Direction.EAST);
		
		// Assert
		assertTrue(result.isSuccess());
		assertEquals("The room is already unlocked.", result.getMessage());
	}
	
	@Test
	void testNullKey() {
		// Arrange
		QuestTracker questTracker = new QuestTracker();
		InteractionEngine engine = new InteractionEngine(questTracker);
		Player player = new Player("Student Explorer", 50, 7, 2, new Inventory(8));
		Room stairwell = new Room("stairwell", "Maintenance Stairwell", "The flickering stairwell leading into the basement.");
		Room lectureHall = new Room("lectureHall", "Abandoned Lecture Hall", "Dusty desks and a broken projector.");
	
		// Act
		stairwell.connect(Direction.EAST, lectureHall);
		lectureHall.setLocked(true, null);
		player.setCurrentRoom(stairwell);
		InteractionResult result = engine.unlockRoom(player, Direction.EAST);
		
		// Assert
		assertFalse(result.isSuccess());
		assertEquals("The room cannot be unlocked.", result.getMessage());	
	}
	
	@Test
	void testIncorrectKey() {
		// Arrange
		QuestTracker questTracker = new QuestTracker();
		InteractionEngine engine = new InteractionEngine(questTracker);
		Player player = new Player("Student Explorer", 50, 7, 2, new Inventory(8));
		Room stairwell = new Room("stairwell", "Maintenance Stairwell", "The flickering stairwell leading into the basement.");
		Room lectureHall = new Room("lectureHall", "Abandoned Lecture Hall", "Dusty desks and a broken projector.");
		Item key = new Key("LectureHall Key", "Opens the Abandoned Lecture Hall.");
		
		// Act
		stairwell.addItem(key);
		player.setCurrentRoom(stairwell);
		InteractionResult result = engine.pickUpItem(player, null, key.getName());
		
		stairwell.connect(Direction.EAST, lectureHall);
		lectureHall.setLocked(true, "Other Key");
		result = engine.unlockRoom(player, Direction.EAST);
		
		// Assert
		assertFalse(result.isSuccess());
		assertEquals("You do not have the correct key.", result.getMessage());
	}
	
	@Test
	void testIncorrectItemType() {
		// Arrange
		QuestTracker questTracker = new QuestTracker();
		InteractionEngine engine = new InteractionEngine(questTracker);
		Player player = new Player("Student Explorer", 50, 7, 2, new Inventory(8));
		Room stairwell = new Room("stairwell", "Maintenance Stairwell", "The flickering stairwell leading into the basement.");
		Room lectureHall = new Room("lectureHall", "Abandoned Lecture Hall", "Dusty desks and a broken projector.");
		Item potion = new Potion("Coffee Potion", "A suspiciously warm cup of coffee.", 15);
		
		
		// Act
		stairwell.addItem(potion);
		player.setCurrentRoom(stairwell);
		InteractionResult result = engine.pickUpItem(player, null, potion.getName());
		
		stairwell.connect(Direction.EAST, lectureHall);
		lectureHall.setLocked(true, potion.getName());
		result = engine.unlockRoom(player, Direction.EAST);
		
		// Assert
		assertFalse(result.isSuccess());
		assertEquals("You do not have the correct key.", result.getMessage());
	}
	
	@Test
	void testNormalUnlockRoom() {
		// Arrange
		QuestTracker questTracker = new QuestTracker();
		InteractionEngine engine = new InteractionEngine(questTracker);
		Player player = new Player("Student Explorer", 50, 7, 2, new Inventory(8));
		Room stairwell = new Room("stairwell", "Maintenance Stairwell", "The flickering stairwell leading into the basement.");
		Room lectureHall = new Room("lectureHall", "Abandoned Lecture Hall", "Dusty desks and a broken projector.");
		Item key = new Key("LectureHall Key", "Opens the Abandoned Lecture Hall.");
		
		// Act
		stairwell.addItem(key);
		player.setCurrentRoom(stairwell);
		InteractionResult result = engine.pickUpItem(player, null, key.getName());
		
		stairwell.connect(Direction.EAST, lectureHall);
		lectureHall.setLocked(true, key.getName());
		result = engine.unlockRoom(player, Direction.EAST);
		
		// Assert
		assertTrue(result.isSuccess());
		assertEquals("Unlocked Abandoned Lecture Hall.", result.getMessage());
	}
}