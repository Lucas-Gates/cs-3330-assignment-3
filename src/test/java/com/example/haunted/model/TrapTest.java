package com.example.haunted.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import static org.junit.jupiter.api.Assertions.*;

class TrapTest {

    //i would hope this contructs correctly
    @Test
    void shouldInitializeTrapAndDisarm() {
        Trap trap = new Trap("Power socket that is not properly installed", TrapType.ELECTRIC, 70, true, true);
        assertEquals("Power socket that is not properly installed", trap.getName());
        assertEquals(TrapType.ELECTRIC, trap.getType());
        assertEquals(70, trap.getDamage());
        assertTrue(trap.isArmed());
        assertTrue(trap.isOneTimeTrigger());
        trap.disarm();
        assertFalse(trap.isArmed());
    }

    //testing both trap types
    @ParameterizedTest
    @EnumSource(TrapType.class)
    void shouldSupportAllTrapTypes(TrapType type) {
        Trap trap = new Trap("Rug burn trap", type, 5, false, false);
        assertEquals(type, trap.getType());
        assertFalse(trap.isArmed());
        assertFalse(trap.isOneTimeTrigger());
    }

    //shouldn't allow partial traps
    @Test
    void shouldThrowExceptionWhenFieldsAreNull() {
        assertThrows(NullPointerException.class, () -> new Trap(null, TrapType.ELECTRIC, 5, true, true));
        assertThrows(NullPointerException.class, () -> new Trap("Big trap", null, 5, true, true));
    }
}