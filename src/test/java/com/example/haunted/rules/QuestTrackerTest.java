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
	void ShouldThrowExceptionWhenQuestTrackerReceivesNull(){
		assertThrows(IllegalArgumentException.class, () -> testquesttracker.updateQuestForItem(null, testitem));
		assertThrows(IllegalArgumentException.class, () -> testquesttracker.updateQuestForItem(testquest,null));
	}
	
	//happy path tests
	@Test 
	public void ShouldMarkGradebookRecoveredWhenLostGradebookIsFound() {
		
	}
	
	@Test
	public void shouldMarkPhantomDefeatedWhenFinalExamPhantomDies() {
		
	}
	
	//boundry analysis tests
	
	//  case sensitivity tests
	@Test
	public void shouldRecognizeLostGradebookIgnoringCase() {
		
	}
	
	@Test
	public void shouldRecognizeFinalExamPhantomIgnoringCase() {
		
	}
	
	// monster alive vs monster dead boundry 
	@Test
	public void shouldNotMarkPhantomDefeatedWhileMonsterIsStillAlive() {
		
	}
	// wrong name tests
	@Test 
	public void shouldIgnoreUnrelatedItems() {
		
	}
	@Test 
	public void shouldIgnoreUnrelatedDeadMonsters() {
		
	}
	
	
	
}


