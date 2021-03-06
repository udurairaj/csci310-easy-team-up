package com.example.easyteamup;

import android.util.Log;

import org.junit.Assert;
import org.junit.Test;

import java.sql.Time;
import java.util.ArrayList;

public class Feature2WhiteBoxTests {

    private EventTable eventTable = new EventTable(true);
    private UserTable userTable = new UserTable(true);

    @Test
    public void testAddBasicPublicEvent() {
        Event event = new Event(999, "My Public Event", true);
        int ID  = eventTable.addEvent(event, true);
        Event eventInDB = eventTable.getEvent(ID);
        Assert.assertNotEquals("Event exists in database", eventInDB, null);
        Assert.assertEquals("Event name is saved in database correctly", eventInDB.getEventName(), "My Public Event");
        Assert.assertEquals("Event status is saved in database correctly", eventInDB.getStatusPublic(), true);
        Assert.assertEquals("Event owner is saved in database correctly", eventInDB.getOwner(), 999);
        // Remove event at end so test can be rerun
        eventTable.removeEvent(ID, true);
    }

    @Test
    public void testAddFullPublicEvent() {
        Event event = new Event(999, "My Full Public Event Test", true);
        int ID  = eventTable.addEvent(event, true);
        event.setDescription("My white-box test for creating a full public event");

        Location USC = new Location("USC", (float)34.0223503112793, (float)-118.28511810302734);
        event.setLocation(USC);

        int day = 20;
        int month = 6;
        int year = 2022;
        int hour = 11;
        int min = 15;
        int dur = 90;
        String date = String.format("%02d" , month) + "/" + String.format("%02d", day) + "/" + year;
        String time = String.format("%02d" , hour) + ":" + String.format("%02d" , min);
        String datetime = date + " " + time;
        TimeSlot timeslot1 = new TimeSlot(datetime, dur);
        day = 25;
        month = 6;
        year = 2022;
        hour = 8;
        min = 30;
        dur = 30;
        date = String.format("%02d" , month) + "/" + String.format("%02d", day) + "/" + year;
        time = String.format("%02d" , hour) + ":" + String.format("%02d" , min);
        datetime = date + " " + time;
        TimeSlot timeslot2 = new TimeSlot(datetime, dur);
        ArrayList<TimeSlot> timeslots = new ArrayList<>();
        timeslots.add(timeslot1);
        timeslots.add(timeslot2);
        event.setTimeOptions(timeslots);

        day = 25;
        month = 5;
        year = 2022;
        hour = 12;
        min = 00;
        date = String.format("%02d" , month) + "/" + String.format("%02d", day) + "/" + year;
        time = String.format("%02d" , hour) + ":" + String.format("%02d" , min);
        datetime = date + " " + time;
        TimeSlot dueTime = new TimeSlot(datetime);
        event.setDueTime(dueTime);

        ArrayList<Integer> invitees = new ArrayList<>();
        invitees.add(6);
        invitees.add(7);
        event.setInvitees(invitees);

        eventTable.editEvent(event, true);
        Event eventInDB = eventTable.getEvent(ID);
        Assert.assertNotEquals("Event exists in database", eventInDB, null);
        Assert.assertEquals("Event name is saved in database correctly",
                eventInDB.getEventName(), "My Full Public Event Test");
        Assert.assertEquals("Event status is saved in database correctly",
                eventInDB.getStatusPublic(), true);
        Assert.assertEquals("Event owner is saved in database correctly",
                eventInDB.getOwner(), 999);
        Assert.assertEquals("Event description is saved in database correctly",
                eventInDB.getDescription(), "My white-box test for creating a full public event");
        Assert.assertEquals("Event location name is saved in database correctly",
                eventInDB.getLocation().getName(), "USC");
        Assert.assertEquals("Event location latitude is saved in database correctly",
                (float)eventInDB.getLocation().getLatitude(), (float)34.0223503112793, 0.0f);
        Assert.assertEquals("Event location longitude is saved in database correctly",
                (float)eventInDB.getLocation().getLongitude(), (float)-118.28511810302734, 0.0f);
        Assert.assertEquals("Event timeslots saved in database correctly", eventInDB.getTimeOptions(), timeslots);
        Assert.assertEquals("Event due time is saved in database correctly", eventInDB.getDueTime(), dueTime);
        Assert.assertEquals("Event invitees saved in database correctly", eventInDB.getInvitees(), invitees);
        // Remove event at end so test can be rerun
        eventTable.removeEvent(ID, true);
    }

    @Test
    public void testInviteUsersToPrivateEvent() {
        Event event = new Event(999, "My Private Event", false);
        int ID  = eventTable.addEvent(event, true);
        Event eventInDB = eventTable.getEvent(ID);
        Assert.assertNotEquals("Event exists in database", eventInDB, null);
        Assert.assertEquals("Event name is saved in database correctly", eventInDB.getEventName(), "My Private Event");
        Assert.assertEquals("Event status is saved in database correctly", eventInDB.getStatusPublic(), false);
        Assert.assertEquals("Event owner is saved in database correctly", eventInDB.getOwner(), 999);

        User userTest1 = new User("Test1", "test1@gmail.com", "usertotest1", "testing123");
        Assert.assertEquals(userTable.contains("usertotest1"), null);
        Integer ID1 = userTable.addUser(userTest1, true);
        Assert.assertNotEquals(userTable.contains("usertotest1"), null);
        User user1 = userTable.getUser(ID1);

        User userTest2 = new User("Test2", "test2@gmail.com", "usertotest2", "testing123");
        Assert.assertEquals(userTable.contains("usertotest2"), null);
        Integer ID2 = userTable.addUser(userTest2, true);
        Assert.assertNotEquals(userTable.contains("usertotest2"), null);
        User user2 = userTable.getUser(ID2);

        Assert.assertEquals("No invitees yet", eventInDB.getInvitees().size(), 0);
        eventInDB.invite(user1);
        eventTable.editEvent(eventInDB, true);
        eventInDB = eventTable.getEvent(ID);
        Assert.assertEquals("Invitees size 1", eventInDB.getInvitees().size(), 1);
        Assert.assertEquals("Test1 user invitee", eventInDB.getInvitees().get(0), ID1);
        eventInDB.invite(user2);
        eventTable.editEvent(eventInDB, true);
        eventInDB = eventTable.getEvent(ID);
        Assert.assertEquals("Test1 user invitee", eventInDB.getInvitees().get(0), ID1);
        Assert.assertEquals("Test2 user invitee", eventInDB.getInvitees().get(1), ID2);
        Assert.assertEquals("Invitees size 2", eventInDB.getInvitees().size(), 2);

        // Remove event at end so test can be rerun
        eventTable.removeEvent(ID, true);
    }

    @Test
    public void testUnInviteUsersFromPrivateEvent() {
        Event event = new Event(999, "My Private Event", false);
        int ID  = eventTable.addEvent(event, true);
        Event eventInDB = eventTable.getEvent(ID);
        Assert.assertNotEquals("Event exists in database", eventInDB, null);
        Assert.assertEquals("Event name is saved in database correctly", eventInDB.getEventName(), "My Private Event");
        Assert.assertEquals("Event status is saved in database correctly", eventInDB.getStatusPublic(), false);
        Assert.assertEquals("Event owner is saved in database correctly", eventInDB.getOwner(), 999);

        User userTest1 = new User("Test1", "test1@gmail.com", "usertotest1", "testing123");
        Assert.assertEquals(userTable.contains("usertotest1"), null);
        Integer ID1 = userTable.addUser(userTest1, true);
        Assert.assertNotEquals(userTable.contains("usertotest1"), null);
        User user1 = userTable.getUser(ID1);

        User userTest2 = new User("Test2", "test2@gmail.com", "usertotest2", "testing123");
        Assert.assertEquals(userTable.contains("usertotest2"), null);
        Integer ID2 = userTable.addUser(userTest2, true);
        Assert.assertNotEquals(userTable.contains("usertotest2"), null);
        User user2 = userTable.getUser(ID2);

        eventTable.editEvent(eventInDB, true);
        eventInDB = eventTable.getEvent(ID);
        Assert.assertEquals("No invitees yet", eventInDB.getInvitees().size(), 0);
        eventInDB.invite(user1);
        eventTable.editEvent(eventInDB, true);
        eventInDB = eventTable.getEvent(ID);
        Assert.assertEquals("Invitees size 1", eventInDB.getInvitees().size(), 1);
        Assert.assertEquals("Test1 user invitee", eventInDB.getInvitees().get(0), ID1);
        eventInDB.invite(user2);
        eventTable.editEvent(eventInDB, true);
        eventInDB = eventTable.getEvent(ID);
        Assert.assertEquals("Test1 user invitee", eventInDB.getInvitees().get(0), ID1);
        Assert.assertEquals("Test2 user invitee", eventInDB.getInvitees().get(1), ID2);
        Assert.assertEquals("Invitees size 2", eventInDB.getInvitees().size(), 2);

        eventInDB.removeInvitee(user1);
        eventTable.editEvent(eventInDB, true);
        eventInDB = eventTable.getEvent(ID);
        Assert.assertEquals("Invitees size 1", eventInDB.getInvitees().size(), 1);
        Assert.assertEquals("Test2 user invitee", eventInDB.getInvitees().get(0), ID2);

        // Remove event at end so test can be rerun
        eventTable.removeEvent(ID, true);
    }

    @Test
    public void testRemoveTimeSlotsFromPublicEvent() {
        Event event = new Event(999, "My Public Event Test", true);
        int ID  = eventTable.addEvent(event, true);

        int day = 20;
        int month = 6;
        int year = 2022;
        int hour = 11;
        int min = 15;
        int dur = 90;
        String date = String.format("%02d" , month) + "/" + String.format("%02d", day) + "/" + year;
        String time = String.format("%02d" , hour) + ":" + String.format("%02d" , min);
        String datetime = date + " " + time;
        TimeSlot timeslot1 = new TimeSlot(datetime, dur);

        day = 25;
        month = 6;
        year = 2022;
        hour = 8;
        min = 30;
        dur = 30;
        date = String.format("%02d" , month) + "/" + String.format("%02d", day) + "/" + year;
        time = String.format("%02d" , hour) + ":" + String.format("%02d" , min);
        datetime = date + " " + time;
        TimeSlot timeslot2 = new TimeSlot(datetime, dur);

        ArrayList<TimeSlot> timeslots = new ArrayList<>();
        timeslots.add(timeslot1);
        timeslots.add(timeslot2);
        event.setTimeOptions(timeslots);

        eventTable.editEvent(event, true);
        Event eventInDB = eventTable.getEvent(ID);

        Assert.assertEquals("Event time slots stored correctly", eventInDB.getTimeOptions().size(), 2);
        Assert.assertEquals("Event time slot 1 stored correctly", eventInDB.getTimeOptions().get(0), timeslot1);
        Assert.assertEquals("Event time slot 2 stored correctly", eventInDB.getTimeOptions().get(1), timeslot2);

        timeslots.remove(0);
        event.setTimeOptions(timeslots);

        eventTable.editEvent(event, true);
        eventInDB = eventTable.getEvent(ID);
        Assert.assertEquals("Event time slots edited correctly", eventInDB.getTimeOptions().size(), 1);
        Assert.assertNotEquals("Event time slot 1 removed correctly", eventInDB.getTimeOptions().get(0), timeslot1);
        Assert.assertEquals("Event time slot 2 stored correctly", eventInDB.getTimeOptions().get(0), timeslot2);

        eventTable.editEvent(event, true);
        eventInDB = eventTable.getEvent(ID);

        Assert.assertNotEquals("Event exists in database", eventInDB, null);
        Assert.assertEquals("Event name is saved in database correctly",
                eventInDB.getEventName(), "My Public Event Test");
        Assert.assertEquals("Event status is saved in database correctly",
                eventInDB.getStatusPublic(), true);
        Assert.assertEquals("Event owner is saved in database correctly",
                eventInDB.getOwner(), 999);
        Assert.assertEquals("Event timeslots saved in database correctly", eventInDB.getTimeOptions(), timeslots);
        // Remove event at end so test can be rerun
        eventTable.removeEvent(ID, true);
    }

}
