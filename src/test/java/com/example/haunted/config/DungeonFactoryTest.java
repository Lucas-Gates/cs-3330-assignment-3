package com.example.haunted.config;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.example.haunted.engine.GameEngine;

public class DungeonFactoryTest {
	@Test
	void testCreateGame() {
		// Arrange Act
		// create the things were gonna test
		GameEngine engine = DungeonFactory.createGame();
		
		// Assert
		// test that some basic things got correctly placed
		assertNotNull(engine);
		assertEquals("Student Explorer", engine.getPlayer().getName());
		assertEquals("Maintenance Stairwell", engine.getPlayer().getCurrentRoom().getName());
		assertEquals("Escape the Basement", engine.getQuest().getName());
	}
}