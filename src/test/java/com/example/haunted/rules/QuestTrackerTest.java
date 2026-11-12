package com.example.haunted.rules;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import  com.example.haunted.model.*;

class QuesttrackerTest {
	//setting up varibles 
	private Quest testquest;
	private QuestItem questitemtest;
	private Item testitem;
	private QuestTracker testquesttracker;

	
	// before each test environment 
	@BeforeEach
	void setUp() {
		testquesttracker = new QuestTracker();
		// making new quest, questitem, and item
		testquest = new Quest("test quest", " test decription of quest");
		questitemtest = new QuestItem("quest item test" , "decription of quest item");
		//item is protected in its file, declare as key 
		testitem = new Key("test item name", "test description");
		}
	//handling bad inputs 
	@Test
	void ShouldHandleNullInputs(){
		assertDoesNotThrow(() -> testquesttracker.updateQuestForItem(testquest, null));
	    assertDoesNotThrow(() -> testquesttracker.updateQuestForMonster(testquest, null));
	    
	    Item gradebook = new QuestItem("lost gradebook", "decription");
	    assertThrows(NullPointerException.class, () -> testquesttracker.updateQuestForItem(null, gradebook));
	}
	
	//happy path tests
	@Test 
	public void ShouldMarkGradebookRecoveredWhenLostGradebookIsFound() {
		//update the quest with an item, 
		Item gradebook = new QuestItem("lost gradebook", "found");
	    testquesttracker.updateQuestForItem(testquest, gradebook);
		
		// verfiy gradebook can be found with the item
	    assertTrue(testquest.isGradebookRecovered());
		
	}
	
	@Test
	public void shouldMarkPhantomDefeatedWhenFinalExamPhantomDies() {
		Monster phantom = new Monster("Final Exam Phantom", 100, 20, 20, new ArrayList<>());
	    phantom.takeDamage(100);
	    assertFalse(phantom.isAlive());
		
		
	    testquesttracker.updateQuestForMonster(testquest, phantom);
	    assertTrue(testquest.isPhantomDefeated());
		
	}
	
	//boundry analysis tests
	
	//  case sensitivity tests
	@Test
	public void shouldRecognizeLostGradebookIgnoringCase() {
		 Item caseBook = new QuestItem("lOsT grAdeBoOk", "decription");
		 testquesttracker.updateQuestForItem(testquest, caseBook);
		 
		 assertTrue(testquest.isGradebookRecovered());
	}
	
	@Test
	public void shouldRecognizeFinalExamPhantomIgnoringCase() {
		Monster caseMonster = new Monster("cAse MonStEr", 100, 20, 20, new ArrayList<>());
		caseMonster.takeDamage(100);
	    testquesttracker.updateQuestForMonster(testquest, caseMonster);
	    
	    assertTrue(testquest.isPhantomDefeated());
	}
	
	// monster alive vs monster dead boundry 
	@Test
	public void shouldNotMarkPhantomDefeatedWhileMonsterIsStillAlive() {
		Monster aliveMonster = new Monster("Final Exam Phantom", 100, 20, 20, new ArrayList<>());
		aliveMonster.takeDamage(99); // Boundary: 1 HP left!
	    testquesttracker.updateQuestForMonster(testquest, aliveMonster);
	    
	    assertFalse(testquest.isPhantomDefeated());
	}
	// wrong name tests
	@Test 
	public void shouldIgnoreUnrelatedItems() {
		testquesttracker.updateQuestForItem(testquest, testitem);
		
		assertFalse(testquest.isGradebookRecovered());
	}
	@Test 
	public void shouldIgnoreUnrelatedDeadMonsters() {
		Monster differentMonster = new Monster("Hotdog Monster", 100, 20, 20, new ArrayList<>());
		differentMonster.takeDamage(100);
	    testquesttracker.updateQuestForMonster(testquest, differentMonster);
	    
	    assertFalse(testquest.isPhantomDefeated());
		
	}
	
	
	
}


