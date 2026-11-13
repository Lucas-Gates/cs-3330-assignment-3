package com.example.haunted.events;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;


class InteractionResultTest {
	private InteractionResult testinteractionresult;
	
	@BeforeEach 
	void setUp() {
		// make a new test interaction result for testing purposes 
		testinteractionresult = new InteractionResult(true, "test message");
	}
	
	//check for null inputs for parameters (allow null inputs)
	@Test
	void ShouldAllowNullInputs() {
		InteractionResult nullMessageResult = new InteractionResult(false, null);
		assertFalse(nullMessageResult.isSuccess());
		assertNull(nullMessageResult.getMessage());
	}
	
	//happy test, ensure all inputs(from parameters) are valid 
	@Test 
	void shouldStoreAllInteractionResults() {
		assertTrue(testinteractionresult.isSuccess());
		assertEquals("test message", testinteractionresult.getMessage());
	}
};