package com.example.easyteamup;

import com.google.firebase.database.FirebaseDatabase;

import org.junit.Assert;
import org.junit.Test;

public class Feature1WhiteBoxTests {
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


}
