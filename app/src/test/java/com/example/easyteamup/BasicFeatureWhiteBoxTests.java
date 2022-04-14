package com.example.easyteamup;

import org.junit.Assert;
import org.junit.Test;

public class BasicFeatureWhiteBoxTests {

    private UserTable userTable = new UserTable(true);

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
        userTable.editUser(userTest, true);
        user = userTable.getUser(ID);
        Assert.assertEquals("User name stored in database correctly", user.getName(), "Test Edit");
        Assert.assertEquals("User email stored in database correctly", user.getEmail(), "testedit@gmail.com");
        Assert.assertEquals("User username stored in database correctly", user.getUsername(), "usertotestedit");
        Assert.assertEquals("User stored in database with correct ID", user.getUserID(), ID);
        Assert.assertEquals("User password stored in database correctly", user.getPassword(), "testing123");
        Assert.assertEquals("User phone number stored in database correctly", user.getPhone(), "1234567890");
        Assert.assertEquals("User profile pic stored in database correctly", user.getProfilePic(), "image");
    }
}
