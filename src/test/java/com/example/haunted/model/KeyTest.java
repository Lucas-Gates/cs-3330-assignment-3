package com.example.haunted.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class KeyTest {

    //key should construct correctly; happy path test
    @Test
    void shouldInitializeKeyProperties() {
        Key key = new Key("Key of B flat", "Unlocks a room");
        assertEquals("Key of B flat", key.getName());
        assertEquals("Unlocks a room", key.getDescription());
        assertEquals("Key of B flat", key.toString());
    }

    //can't have a partially or fully null key
    @Test
    void shouldThrowExceptionWhenFieldsAreNull() {
        assertThrows(NullPointerException.class, () -> new Key(null, "Key made of gold"));
        assertThrows(NullPointerException.class, () -> new Key("Gold key", null));
    }
}