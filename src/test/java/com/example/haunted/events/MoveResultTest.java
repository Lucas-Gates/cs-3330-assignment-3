package com.example.haunted.events;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class MoveResultTest{
	private MoveResult testMoveResult;
	
	
	//setting up test space by initializing 
	@BeforeEach
	void setUp() {
		testMoveResult = new MoveResult(true,"testmessage",true,10);
	}

	//allowing null inputs so the program doesnt crash
	@Test 
	void ShouldAllowNullInputs() {
		MoveResult nullMoveResult = new MoveResult(true, null, true, 10);
		assertTrue(nullMoveResult.isSuccess());
		assertNull(nullMoveResult.getMessage());
	}
	
	// ensuring all getters are initialized and can be stored
	@Test
	void shouldStoreAllMoveResults() {
		assertTrue(testMoveResult.isSuccess());
		assertEquals("testmessage", testMoveResult.getMessage());
		assertTrue(testMoveResult.isTrapTriggered());
		assertEquals(10, testMoveResult.getTrapDamage());
	}
};