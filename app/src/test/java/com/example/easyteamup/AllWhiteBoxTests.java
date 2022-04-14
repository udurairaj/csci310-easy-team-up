package com.example.easyteamup;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class AllWhiteBoxTests {

    private UserTable userTable = new UserTable(true);
    private EventTable eventTable = new EventTable(true);

    @Test
    public void userTableContainsTest() {
        User userTest = new User("Test", "test@gmail.com", "usertotest", "testing123");
        Assert.assertEquals(userTable.contains("usertotest"), null);

        userTable.addUser(userTest, true);
        Assert.assertNotEquals(userTable.contains("usertotest"), null);
    }

    @Test
    public void editProfileTest() {
        User userTest = new User("Test", "test@gmail.com", "usertotest", "testing123");
        Assert.assertEquals(userTable.contains("usertotest"), null);
        int ID = userTable.addUser(userTest, true);
        Assert.assertNotEquals(userTable.contains("usertotest"), null);
        User user = userTable.getUser(ID);
        Assert.assertEquals("User name stored in database correctly", user.getName(), "Test");
        Assert.assertEquals("User email stored in database correctly", user.getEmail(), "test@gmail.com");
        Assert.assertEquals("User username stored in database correctly", user.getUsername(), "usertotest");
        Assert.assertEquals("User stored in database with correct ID", user.getUserID(), ID);
        Assert.assertEquals("User password stored in database correctly", user.getPassword(), "testing123");

        userTest.setName("Test Edit");
        userTest.setUsername("usertotestedit");
        userTest.setEmail("testedit@gmail.com");
        userTest.setPhone("1234567890");
        userTest.setProfilePic("image");
        userTest.setOtherInfo("Test student at USC");
        userTable.editUser(userTest, true);
        user = userTable.getUser(ID);
        Assert.assertEquals("User name stored in database correctly", user.getName(), "Test Edit");
        Assert.assertEquals("User email stored in database correctly", user.getEmail(), "testedit@gmail.com");
        Assert.assertEquals("User username stored in database correctly", user.getUsername(), "usertotestedit");
        Assert.assertEquals("User stored in database with correct ID", user.getUserID(), ID);
        Assert.assertEquals("User password stored in database correctly", user.getPassword(), "testing123");
        Assert.assertEquals("User phone number stored in database correctly", user.getPhone(), "1234567890");
        Assert.assertEquals("User profile pic stored in database correctly", user.getProfilePic(), "image");
        Assert.assertEquals("User other info stored in database correctly", user.getOtherInfo(), "Test student at USC");
    }

    @Test
    public void testSignUp()
    {
        UserTable ut = new UserTable(true);
        User user = new User("steph", "steph@rcbc.edu", "steph", "steph123");
        Assert.assertNotEquals(ut.getUser(ut.addUser(user, true)), null);
    }

    @Test
    public void testBadUser()
    {
        UserTable ut = new UserTable(true);
        User user2 = new User();
        Assert.assertNull(ut.getUser(ut.addUser(user2, true)).getName());
    }

    @Test
    public void testLocation()
    {
        Location location = new Location("USC", 34.0224f, 118.2851f);
        Assert.assertEquals(location.getName(), "USC");
        Assert.assertEquals(location.getLatitude(), 34.0224, .001);
        Assert.assertEquals(location.getLongitude(), 118.2851, .001);
    }

    @Test
    public void testBadLocation()
    {
        Location location = new Location();
        location.setName("UCLA");
        location.setLatitude(34.0f);
        location.setLongitude(119.0f);
        Assert.assertNotEquals(location.getName(), "USC");
        Assert.assertNotEquals(location.getLatitude(), 34.0224, .001);
        Assert.assertNotEquals(location.getLongitude(), 118.2851, .001);
    }

    @Test
    public void testTimeSlot()
    {
        TimeSlot slot = new TimeSlot("12:03:2001:11:05");
        Assert.assertEquals(slot.getMonth(), 12);
        Assert.assertEquals(slot.getDay(), 03);
        Assert.assertEquals(slot.getYear(), 2001);
        Assert.assertEquals(slot.getHour(), 11);
        Assert.assertEquals(slot.getMinute(), 05);
    }

    @Test
    public void testBadTimeSlot()
    {
        TimeSlot slot = new TimeSlot("07:08:2000:01:30");
        Assert.assertNotEquals(slot.getMonth(), 12);
        Assert.assertNotEquals(slot.getDay(), 03);
        Assert.assertNotEquals(slot.getYear(), 2001);
        Assert.assertNotEquals(slot.getHour(), 11);
        Assert.assertNotEquals(slot.getMinute(), 05);
    }

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
        int ID = eventTable.addEvent(event, true);

        int day = 20;
        int month = 6;
        int year = 2022;
        int hour = 11;
        int min = 15;
        int dur = 90;
        String date = String.format("%02d", month) + "/" + String.format("%02d", day) + "/" + year;
        String time = String.format("%02d", hour) + ":" + String.format("%02d", min);
        String datetime = date + " " + time;
        TimeSlot timeslot1 = new TimeSlot(datetime, dur);

        day = 25;
        month = 6;
        year = 2022;
        hour = 8;
        min = 30;
        dur = 30;
        date = String.format("%02d", month) + "/" + String.format("%02d", day) + "/" + year;
        time = String.format("%02d", hour) + ":" + String.format("%02d", min);
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
