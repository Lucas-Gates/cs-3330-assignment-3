package com.example.haunted.rules;
import com.example.haunted.events.InteractionResult;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import  com.example.haunted.model.*;

class TrapResolverTest {
	private Player testplayer;
	private Trap testtrap;
	private TrapResolver testtrapresolver;
	
	//before each 
	@BeforeEach 
	void setUp() {
		testtrapresolver = new TrapResolver();
		//making a new single use trap with 15 damage
	    testtrap = new Trap("Spike Trap", TrapType.STEAM, 15, true, true);
	    
	    //set up a default player with 100 health
	    Inventory testInventory = new Inventory(50);
	    testplayer = new Player("laila", 100, 15, 20, testInventory);
	}
	
	//handling bad input for parameters
	@Test
	void ShouldThrowExceptionWhenCalcReceivesNull(){
		//armed trap with no target should throw exception
		assertThrows(NullPointerException.class, () -> testtrapresolver.resolveTrap(null, testtrap));

	    // null trap returns without crashing program
	    InteractionResult result = testtrapresolver.resolveTrap(testplayer, null);
	    assertFalse(result.isSuccess());
	    }
	
	//happy path tests 
	// single use trap deals damage and disarms itself
	@Test
	void shouldTriggerOneTimeTrapAndDisarmIt() {
		//testtrap should equal true (from setup)
	    InteractionResult result = testtrapresolver.resolveTrap(testplayer, testtrap);
	    assertTrue(result.isSuccess());
	    // remaining health should be 85 (100-15)
	    assertEquals(85, testplayer.getHealth()); 
	    //single use trap must disarm after use
	    assertFalse(testtrap.isArmed());
	}
	
	@Test
	void shouldTriggerReusableTrapAndKeepItArmed() {
		//making a new trap that can be reused ( oneTimeTrigger = false)
		Trap reusabletrap = new Trap("steam trap", TrapType.STEAM, 10, true, false);
	    InteractionResult result = testtrapresolver.resolveTrap(testplayer, reusabletrap);

	    assertTrue(result.isSuccess());
	    assertEquals(90, testplayer.getHealth());
	    // trap should remain armed
	    assertTrue(reusabletrap.isArmed()); 
	}
	
	
	//boundry analysis test
	//already used trap should not deal any damage
	@Test
	void shouldNotTriggerDisarmedTrap() {
	    Trap disarmedTrap = new Trap("one time trap", TrapType.STEAM, 20, false, true);
	    InteractionResult result = testtrapresolver.resolveTrap(testplayer, disarmedTrap);

	    //health should remain at 100
	    assertFalse(result.isSuccess());
	    assertEquals(100, testplayer.getHealth());
	}
	
};
