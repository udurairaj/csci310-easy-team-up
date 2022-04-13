package com.example.easyteamup;

import org.junit.Assert;
import org.junit.Test;

public class BasicFeatureWhiteBoxTests {
    @Test
    public void userTableContainsTest() {
        UserTable userTable = new UserTable(true);
        User userTest = new User("Test", "test@gmail.com", "usertotest", "testing123");
        Assert.assertEquals(userTable.contains("usertotest"), null);

        userTable.addUser(userTest, true);
        Assert.assertNotEquals(userTable.contains("usertotest"), null);
    }
}
