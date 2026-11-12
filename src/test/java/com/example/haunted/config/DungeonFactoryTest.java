package com.example.haunted.config;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.example.haunted.engine.GameEngine;

public class DungeonFactoryTest {
	@Test
	void testCreateGame() {
		// Arrange Act
		GameEngine engine = DungeonFactory.createGame();
		
		// Assert
		assertNotNull(engine);
		assertEquals("Student Explorer", engine.getPlayer().getName());
		assertEquals("Maintenance Stairwell", engine.getPlayer().getCurrentRoom().getName());
		assertEquals("Escape the Basement", engine.getQuest().getName());
	}
}