package com.example.haunted.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class QuestItemTest {

    //this should construct correctly, happy path
    @Test
    void shouldInitializeQuestItemProperties() {
        QuestItem item = new QuestItem("Red pen", "Gives bad grades");
        assertEquals("Red pen", item.getName());
        assertEquals("Gives bad grades", item.getDescription());
        assertEquals("Red pen", item.toString());
    }

    //shouldn't let there be a partial quest item
    @Test
    void shouldThrowExceptionWhenFieldsAreNull() {
        assertThrows(NullPointerException.class, () -> new QuestItem(null, "Notes"));
        assertThrows(NullPointerException.class, () -> new QuestItem("Notebook", null));
    }
}