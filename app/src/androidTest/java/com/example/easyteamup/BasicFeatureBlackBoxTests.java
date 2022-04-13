package com.example.easyteamup;

import static android.view.KeyEvent.KEYCODE_TAB;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.openLinkWithUri;
import static androidx.test.espresso.action.ViewActions.pressKey;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import androidx.activity.result.ActivityResult;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.NavigationViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.easyteamup.login.LoginActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class BasicFeatureBlackBoxTests {

    @Rule
    public ActivityScenarioRule<LoginActivity> loginRule =
            new ActivityScenarioRule<LoginActivity>(LoginActivity.class);

    @Test
    public void login() {
        onView(withId(R.id.usernameLoginBox)).perform(typeText("bob"));
        onView(withId(R.id.passwordLoginBox)).perform(typeText("bobbobbob"), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());

        onView(withId(R.id.nameProfileView)).check(matches(withText("billy bobby :)")));
        onView(withId(R.id.userIDProfileView)).check(matches(withText("5")));
        onView(withId(R.id.usernameProfileView)).check(matches(withText("bob")));
        onView(withId(R.id.emailProfileView)).check(matches(withText("bobbobbob@gmail.com")));
        onView(withId(R.id.phoneProfileView)).check(matches(withText("1111111111")));
        onView(withId(R.id.otherInfoProfileView)).check(matches(withText("cool viterbi dog")));
    }

    @Test
    public void displayTest() {
        login();
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_userEventDisplay)).perform(click());
        onView(withId(R.id.radioButton2)).perform(click());
    }

    @Test
    public void viewAndEditProfileTest() {
        // Log in to Mary's account
        onView(withId(R.id.usernameLoginBox)).perform(typeText("mary"));
        onView(withId(R.id.passwordLoginBox)).perform(typeText("marymary"), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());

        // UserTable used for cleanup after test completes to allow for rerunning
        UserTable userTable = new UserTable();

        // Check that Mary's profile loads correctly
        onView(withId(R.id.nameProfileView)).check(matches(withText("Mary")));
        onView(withId(R.id.userIDProfileView)).check(matches(withText("8")));
        onView(withId(R.id.usernameProfileView)).check(matches(withText("mary")));
        onView(withId(R.id.emailProfileView)).check(matches(withText("mary@google.com")));
        onView(withId(R.id.phoneProfileView)).check(matches(withText("1237894567")));
        onView(withId(R.id.otherInfoProfileView)).check(matches(withText("Mary Mary quite contrary")));

        // Click edit profile button and switch to edit profile page
        onView(withId(R.id.editProfileButton)).perform(click());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Log.d("Test", "Timeout issue.");
        }

        // Check that edit profile page autofills with Mary's information
        onView(withId(R.id.nameEditProfileView)).check(matches(withText("Mary")));
        onView(withId(R.id.usernameEditProfileView)).check(matches(withText("mary")));
        onView(withId(R.id.emailEditProfileView)).check(matches(withText("mary@google.com")));
        onView(withId(R.id.phoneEditProfileView)).check(matches(withText("1237894567")));
        onView(withId(R.id.otherInfoEditProfileView)).check(matches(withText("Mary Mary quite contrary")));
        onView(withId(R.id.imageEditButton)).check(matches(withContentDescription("SUCCESS")));

        // Edit name and delete - expect error and stay on page
        onView(withId(R.id.nameEditProfileView)).perform(click());
        onView(withId(R.id.nameEditProfileView)).perform(clearText(), closeSoftKeyboard());
        onView(withId(R.id.confirmProfileChangesButton)).perform(click());
        onView(withId(R.id.nameEditProfileView)).check(matches(hasErrorText("Name is required.")));
        // Edit name with valid name
        onView(withId(R.id.nameEditProfileView)).perform(click());
        onView(withId(R.id.nameEditProfileView)).perform(clearText());
        onView(withId(R.id.nameEditProfileView)).perform(typeText("Mary Mary"), closeSoftKeyboard());

        // Edit username and delete - expect error and stay on page
        onView(withId(R.id.usernameEditProfileView)).perform(click());
        onView(withId(R.id.usernameEditProfileView)).perform(clearText(), closeSoftKeyboard());
        onView(withId(R.id.confirmProfileChangesButton)).perform(click());
        onView(withId(R.id.usernameEditProfileView)).check(matches(hasErrorText("Username is required")));
        // Edit username with taken username - expect error and stay on page
        onView(withId(R.id.usernameEditProfileView)).perform(click());
        onView(withId(R.id.usernameEditProfileView)).perform(clearText());
        onView(withId(R.id.usernameEditProfileView)).perform(typeText("ericadg"), closeSoftKeyboard());
        onView(withId(R.id.confirmProfileChangesButton)).perform(click());
        onView(withId(R.id.usernameEditProfileView)).check(matches(hasErrorText("Username is taken. Try again.")));
        // Edit username with valid username
        onView(withId(R.id.usernameEditProfileView)).perform(click());
        onView(withId(R.id.usernameEditProfileView)).perform(clearText());
        onView(withId(R.id.usernameEditProfileView)).perform(typeText("mmary"), closeSoftKeyboard());

        // Edit email and delete - expect error and stay on page
        onView(withId(R.id.emailEditProfileView)).perform(click());
        onView(withId(R.id.emailEditProfileView)).perform(clearText(), closeSoftKeyboard());
        onView(withId(R.id.confirmProfileChangesButton)).perform(click());
        onView(withId(R.id.emailEditProfileView)).check(matches(hasErrorText("Email is required")));
        // Edit email with valid email
        onView(withId(R.id.emailEditProfileView)).perform(click());
        onView(withId(R.id.emailEditProfileView)).perform(clearText());
        onView(withId(R.id.emailEditProfileView)).perform(typeText("marymary@google.com"), closeSoftKeyboard());

        // Edit phone with valid number
        onView(withId(R.id.phoneEditProfileView)).perform(click());
        onView(withId(R.id.phoneEditProfileView)).perform(clearText());
        onView(withId(R.id.phoneEditProfileView)).perform(typeText("1029384756"), closeSoftKeyboard());

        // Edit about me with valid description
        onView(withId(R.id.otherInfoEditProfileView)).perform(click());
        onView(withId(R.id.otherInfoEditProfileView)).perform(closeSoftKeyboard(), clearText());
        onView(withId(R.id.phoneEditProfileView)).perform(pressKey(KEYCODE_TAB));
        onView(withId(R.id.otherInfoEditProfileView)).perform(typeText("Mary Mary quite contrary, how does your garden grow?"), closeSoftKeyboard());

        // Save changes made to profile successfully
        onView(withId(R.id.confirmProfileChangesButton)).perform(click());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Log.d("Test", "Timeout issue.");
        }

        // Check that Mary's edited profile loads correctly
        onView(withId(R.id.nameProfileView)).check(matches(withText("Mary Mary")));
        onView(withId(R.id.userIDProfileView)).check(matches(withText("8")));
        onView(withId(R.id.usernameProfileView)).check(matches(withText("mmary")));
        onView(withId(R.id.emailProfileView)).check(matches(withText("marymary@google.com")));
        onView(withId(R.id.phoneProfileView)).check(matches(withText("1029384756")));
        onView(withId(R.id.otherInfoProfileView)).check(matches(withText("Mary Mary quite contrary, how does your garden grow?")));
        onView(withId(R.id.imageButton)).check(matches(withContentDescription("SUCCESS")));

        // FINISHED TESTING
        // Manually reset Mary's profile to original information (so test can be rerun)
        User mary = userTable.getUser(8);
        mary.setName("Mary");
        mary.setUsername("mary");
        mary.setEmail("mary@google.com");
        mary.setPhone("1237894567");
        mary.setOtherInfo("Mary Mary quite contrary");
        userTable.editUser(mary);
    }
}
