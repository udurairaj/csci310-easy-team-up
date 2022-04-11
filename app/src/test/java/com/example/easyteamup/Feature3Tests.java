package com.example.easyteamup;

import com.google.firebase.database.FirebaseDatabase;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Mockito.*;

public class Feature3Tests {
    @Test
    public void testAddEvent() {
        EventTable table = new EventTable(true);
        Event event = new Event(999, "My Event", true);
       Assert.assertNotEquals(table.getEvent(table.addEvent(event, true)), null);
    }
}
