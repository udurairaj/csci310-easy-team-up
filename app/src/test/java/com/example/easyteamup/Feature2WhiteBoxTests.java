package com.example.easyteamup;

import org.junit.Assert;
import org.junit.Test;

public class Feature2WhiteBoxTests {
    @Test
    public void testAddEvent() {
        EventTable table = new EventTable(true);
        Event event = new Event(999, "My Event", true);
        Assert.assertNotEquals(table.getEvent(table.addEvent(event, true)), null);
        // remove
    }

}
