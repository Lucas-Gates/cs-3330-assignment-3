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

	//handling bad inputs 
	@Test
	void ShouldThrowExceptionWhenCalcReceivesNull(){
		assertThrows(NullPointerException.class, () -> testcalc.calculatePlayerDamage(null, testmonster));
		assertThrows(NullPointerException.class, () -> testcalc.calculatePlayerDamage(testplayer,null));
	}
	
	// happypath tests

	//standard player attack, should return true
	@Test 
	public void shouldCalculateStandardPlayerDamage() {
		// player attack = 35 and monster defense = 20, 35-20 = 15
		Player strongPlayer = new Player("laila", 100, 35, 20, new Inventory(50));
		int damage = testcalc.calculatePlayerDamage(strongPlayer, testmonster);
		
		assertEquals(15,damage);
	}

	// standard monster attack
	@Test 
	public void shouldCalculateStandardMonsterDamage() {
		//act 
		int damage = testcalc.calculateMonsterDamage(testmonster, testplayer);
		//assert
		assertEquals(5, damage);
	}
	// boss monster attack 
	@Test
	public void shouldCalculateBossMonsterDamageUsingCurrentAttack() {
		BossMonster boss = new BossMonster("Mega Hotdog", 200, 40, 20, new ArrayList<>(), 15);
		int damage = testcalc.calculateMonsterDamage(boss, testplayer);
		
		assertEquals(20,damage);
	}
	
	// tie 
	@Test
	public void shouldReturnOneWhenAttackEqualsDefense() {
		Player evenPlayer = new Player("laila", 100, 20, 20, new Inventory(50));
        int damage = testcalc.calculatePlayerDamage(evenPlayer, testmonster);
        
        assertEquals(1, damage);
	}
	
	//exact boundry
	@Test
	public void shouldFloorAtOneWhenDefenseExceedsAttack() {
		int damage = testcalc.calculatePlayerDamage(testplayer, testmonster);
		
		assertEquals(1, damage);
	}
	
	//below boundry
    @Test
    void shouldReturnTwoWhenAttackExceedsDefenseByTwo() {
    	Player strongerPlayer = new Player("laila", 100, 22, 20, new Inventory(50));
        int damage = testcalc.calculatePlayerDamage(strongerPlayer, testmonster);
        
        assertEquals(2, damage);
    }
	
	
	
	};
