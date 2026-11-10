package com.example.haunted.rules;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

import  com.example.haunted.model.*;




// making test class
class DamageCalculatorTest{
	//setting up varibles
	private Player testplayer;
	private Monster testmonster;
	private DamageCalculator testcalc;

	// before each test
	@BeforeEach
	void setUp() {
		//making a new calculator
	testcalc = new DamageCalculator();
	// initializing player, a new testinventory needed for its parameters
	Inventory testInventory = new Inventory(50);
	testplayer = new Player("laila" , 100 , 15, 20, testInventory);
	
	// initializing monster, new monsterloot list was needed for param
	List<Item> monsterLoot = new ArrayList<>();
	testmonster = new Monster("hotdog monster" , 100, 25, 20, monsterLoot);
	}
	
	
	@Test
	public void shouldRejectPlayerCalc() {
		//executing
		int testresult = testcalc.calculatePlayerDamage(testplayer, testmonster);
		
		//assert 
		assertEquals(-1, testresult);
		
	}
	
	
	
	};