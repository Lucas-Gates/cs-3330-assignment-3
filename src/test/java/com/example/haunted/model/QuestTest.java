package com.example.haunted.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class QuestTest {
    private Quest quest;

    //setting up the first quest before each test
    @BeforeEach
    void setUp() {
        quest = new Quest("First quest", "Win the game");
    }

    //it should construct correctly, happy path test
    @Test
    void shouldInitializeWithNotStartedStatus() {
        assertEquals("First quest", quest.getName());
        assertEquals("Win the game", quest.getDescription());
        assertEquals(QuestStatus.NOT_STARTED, quest.getStatus());
        assertFalse(quest.isGradebookRecovered());
        assertFalse(quest.isPhantomDefeated());
        assertFalse(quest.isComplete());
    }

    //should make progress when intended
    @Test
    void shouldProgressWhenGradebookRecoveredFirst() {
        quest.markGradebookRecovered();
        assertTrue(quest.isGradebookRecovered());
        assertFalse(quest.isPhantomDefeated());
        assertEquals(QuestStatus.IN_PROGRESS, quest.getStatus());
        assertFalse(quest.isComplete());
        quest.markPhantomDefeated();
        assertTrue(quest.isPhantomDefeated());
        assertEquals(QuestStatus.COMPLETED, quest.getStatus());
        assertTrue(quest.isComplete());
    }

    //again, should make progress when intended
    @Test
    void shouldProgressWhenPhantomDefeatedFirst() {
        quest.markPhantomDefeated();
        assertTrue(quest.isPhantomDefeated());
        assertFalse(quest.isGradebookRecovered());
        assertEquals(QuestStatus.IN_PROGRESS, quest.getStatus());
        assertFalse(quest.isComplete());
        quest.markGradebookRecovered();
        assertTrue(quest.isGradebookRecovered());
        assertEquals(QuestStatus.COMPLETED, quest.getStatus());
        assertTrue(quest.isComplete());
    }

    //i would hope you can't change statuses randomly 
    @Test
    void shouldNotChangeStatusWhenNoObjectivesMet() {
        quest.updateStatus();
        assertEquals(QuestStatus.NOT_STARTED, quest.getStatus());
    }

    //don't allow partial quests
    @Test
    void shouldThrowExceptionWhenConstructorReceivesNull() {
        assertThrows(NullPointerException.class, () -> new Quest(null, "Last Description"));
        assertThrows(NullPointerException.class, () -> new Quest("Last Quest", null));
    }
}