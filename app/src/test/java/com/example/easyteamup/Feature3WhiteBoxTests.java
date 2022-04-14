package com.example.easyteamup;

import com.google.firebase.database.FirebaseDatabase;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Mockito.*;

import java.util.ArrayList;

public class Feature3WhiteBoxTests {
    @Test
    public void testAddParticipant() {
        User currentUser = new User();
        currentUser.setUserID(0);

        Event eventToTest = new Event();
        eventToTest.addParticipant(currentUser);

        Assert.assertEquals(eventToTest.getParticipants().size(), 1);
        Assert.assertTrue(eventToTest.getParticipants().contains(0));
    }

    @Test
    public void testWithdraw() {
        User currentUser = new User();
        currentUser.setUserID(0);

        Event eventToTest = new Event();
        eventToTest.addParticipant(currentUser);

        eventToTest.removeParticipant(currentUser);
        Assert.assertEquals(eventToTest.getParticipants().size(), 0);
        Assert.assertFalse(eventToTest.getParticipants().contains(0));
    }

    @Test
    public void testRejectInvite() {
        User currentUser = new User();
        currentUser.setUserID(0);

        Event eventToTest = new Event();
        eventToTest.invite(currentUser);
        eventToTest.removeInvitee(currentUser);

        Assert.assertEquals(eventToTest.getInvitees().size(), 0);
        Assert.assertFalse(eventToTest.getInvitees().contains(0));
    }

    @Test
    public void testGetAllEvents() {
        Event eventTest1 = new Event();
        eventTest1.setEventID(0);

        Event eventTest2 = new Event();
        eventTest2.setEventID(1);

        Event eventTest3 = new Event();
        eventTest3.setEventID(2);

        EventTable eventTable = new EventTable(true);
        eventTable.addEvent(eventTest1, true);
        eventTable.addEvent(eventTest2, true);
        eventTable.addEvent(eventTest3, true);

        ArrayList<Event> allEvents = eventTable.getAllEvents();
        Assert.assertEquals(allEvents.size(), 3);
        Assert.assertTrue(allEvents.contains(eventTest1));
        Assert.assertTrue(allEvents.contains(eventTest2));
        Assert.assertTrue(allEvents.contains(eventTest3));
    }
}
