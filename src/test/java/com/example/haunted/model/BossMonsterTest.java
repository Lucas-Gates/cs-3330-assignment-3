package com.example.haunted.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class BossMonsterTest {
	private BossMonster testbossmonster;
	
	@BeforeEach
    void setUp() {
		List<Item> loot = new ArrayList();
         BossMonster testbossmonster = new BossMonster("super hotdog monster", 100, 10, 10, loot, 10);
    }
	
}
