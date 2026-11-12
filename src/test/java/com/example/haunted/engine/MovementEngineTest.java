package com.example.haunted.engine;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import org.junit.jupiter.api.Test;

import com.example.haunted.events.InteractionResult;
import com.example.haunted.events.MoveResult;
import com.example.haunted.model.Direction;
import com.example.haunted.model.Inventory;
import com.example.haunted.model.Player;
import com.example.haunted.model.Room;
import com.example.haunted.model.Trap;
import com.example.haunted.model.TrapType;
import com.example.haunted.rules.TrapResolver;

public class MovementEngineTest {
	@Test
	void testConstructor() {
		// Arrange
		TrapResolver trapResolver = new TrapResolver();
		
		// Act
		MovementEngine engine = new MovementEngine(trapResolver);
		
		// Assert
		assertNotNull(engine);
	}
	
	@Test
	void testNullRoom() {
		// Arrange
		// stuff needed for the actual result
		TrapResolver trapResolver = new TrapResolver();
		MovementEngine engine = new MovementEngine(trapResolver);
		
		// objects for tests
		Player player = new Player("Student Explorer", 50, 7, 2, new Inventory(8));
		Room stairwell = new Room("stairwell", "Maintenance Stairwell", "The flickering stairwell leading into the basement.");
		
		// Act
		// sets room
		player.setCurrentRoom(stairwell);
		MoveResult result = engine.move(player, Direction.NORTH);
		
		// Assert
		// returns for null room
		assertFalse(result.isSuccess());
		assertEquals("There is no room in that direction.", result.getMessage());
		assertFalse(result.isTrapTriggered());
		assertEquals(0, result.getTrapDamage());
	}
	
	@Test
	void testLockedRoom() {
		// Arrange
		// stuff needed for the actual result
		TrapResolver trapResolver = new TrapResolver();
		MovementEngine engine = new MovementEngine(trapResolver);
		
		// objects for test
		Player player = new Player("Student Explorer", 50, 7, 2, new Inventory(8));
		Room stairwell = new Room("stairwell", "Maintenance Stairwell", "The flickering stairwell leading into the basement.");
		Room lectureHall = new Room("lectureHall", "Abandoned Lecture Hall", "Dusty desks and a broken projector.");
		
		// Act
		// connects and locks room
		stairwell.connect(Direction.EAST, lectureHall);
		lectureHall.setLocked(true, "Key");
		player.setCurrentRoom(stairwell);
		MoveResult result = engine.move(player, Direction.EAST);
		
		// Assert
		// returns for moving into locked room
		assertFalse(result.isSuccess());
		assertEquals("The room is locked.", result.getMessage());
		assertFalse(result.isTrapTriggered());
		assertEquals(0, result.getTrapDamage());
	}
	
	@Test
	void testMoveNoTrap() {
		// Arrange
		// stuff needed for the actual result
		TrapResolver trapResolver = new TrapResolver();
		MovementEngine engine = new MovementEngine(trapResolver);
		
		// objects for tests
		Player player = new Player("Student Explorer", 50, 7, 2, new Inventory(8));
		Room stairwell = new Room("stairwell", "Maintenance Stairwell", "The flickering stairwell leading into the basement.");
		Room lectureHall = new Room("lectureHall", "Abandoned Lecture Hall", "Dusty desks and a broken projector.");
		
		// Act
		// sets connecting room
		stairwell.connect(Direction.EAST, lectureHall);
		player.setCurrentRoom(stairwell);
		MoveResult result = engine.move(player, Direction.EAST);
		
		// Assert
		// returns for moving with no trap in room
		assertTrue(result.isSuccess());
		assertEquals("Moved to Abandoned Lecture Hall.", result.getMessage());
		assertFalse(result.isTrapTriggered());
		assertEquals(0, result.getTrapDamage());
	}
	
	@Test
	void testMoveTrap() {
		// Arrange
		// stuff needed for the actual result
		TrapResolver trapResolver = new TrapResolver();
		MovementEngine engine = new MovementEngine(trapResolver);
		
		// objects for tests
		Player player = new Player("Student Explorer", 50, 7, 2, new Inventory(8));
		Room stairwell = new Room("stairwell", "Maintenance Stairwell", "The flickering stairwell leading into the basement.");
		Room lectureHall = new Room("lectureHall", "Abandoned Lecture Hall", "Dusty desks and a broken projector.");
		Trap trap = new Trap("Loose Wires Trap", TrapType.ELECTRIC, 8, true, true);
				
		// Act
		// sets up rooms and trap
		lectureHall.setTrap(trap);
		stairwell.connect(Direction.EAST, lectureHall);
		player.setCurrentRoom(stairwell);
		MoveResult result = engine.move(player, Direction.EAST);
		
		// Assert
		// returns for moving into a trap
		assertTrue(result.isSuccess());
		assertEquals("Moved to Abandoned Lecture Hall. "
				 + "Trap 'Loose Wires Trap' triggered for 8 damage.", result.getMessage());
		assertTrue(result.isTrapTriggered());
		assertEquals(8, result.getTrapDamage());
	}
	
	@Test
	void testTriggeredTrap() {
		// Arrange
		// stuff needed for the actual result
		TrapResolver trapResolver = new TrapResolver();
		MovementEngine engine = new MovementEngine(trapResolver);
		
		// objects for tests
		Player player = new Player("Student Explorer", 50, 7, 2, new Inventory(8));
		Room stairwell = new Room("stairwell", "Maintenance Stairwell", "The flickering stairwell leading into the basement.");
		Room lectureHall = new Room("lectureHall", "Abandoned Lecture Hall", "Dusty desks and a broken projector.");
		Trap trap = new Trap("Loose Wires Trap", TrapType.ELECTRIC, 8, true, true);
				
		// Act
		// sets up trap and disarms it
		lectureHall.setTrap(trap);
		stairwell.connect(Direction.EAST, lectureHall);
		player.setCurrentRoom(stairwell);
		trap.disarm();
		MoveResult result = engine.move(player, Direction.EAST);
		
		// Assert
		// returns for moving into room with disarmed trap
		assertTrue(result.isSuccess());
		assertEquals("Moved to Abandoned Lecture Hall.", result.getMessage());
		assertFalse(result.isTrapTriggered());
		assertEquals(0, result.getTrapDamage());
	}
}