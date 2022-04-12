package com.example.easyteamup;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.pressKey;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasValue;

import android.app.LauncherActivity;
import android.view.KeyEvent;

import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.internal.util.Checks;

import com.example.easyteamup.login.LoginActivity;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

@RunWith(AndroidJUnit4.class)
public class Feature3BlackBoxTests {

    @Rule
    public ActivityScenarioRule<LoginActivity> loginRule =
            new ActivityScenarioRule<LoginActivity>(LoginActivity.class);


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

        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_userEventDisplay)).perform(click());
    }

    @Test
    public void acceptInviteTest() {
        EventTable eventTable = new EventTable();
        UserTable userTable = new UserTable();

        login();
        onView(withId(R.id.radioButton2)).perform(click());
        onData(anything()).inAdapterView(withId(R.id.list)).atPosition(0).perform(click());

        Event chosenEvent = eventTable.getEvent(eventTable.getInvitedEventIDs().get(0));
        User loggedIn = userTable.getUser(5);

        onView(withId(R.id.acceptInviteButton)).check(matches(isDisplayed()));
        onView(withId(R.id.rejectInviteButton)).check(matches(isDisplayed()));

        onView(withId(R.id.acceptInviteButton)).perform(click());

        onView(withId(R.id.radioButton3)).perform(click());
        onView(withText(chosenEvent.getEventName())).check(matches(isDisplayed()));

        onView(withText(chosenEvent.getEventName())).perform(click());
        onView(withId(R.id.participantsDetailsView)).check(matches(withText(containsString(loggedIn.getName()))));

        chosenEvent.removeParticipant(loggedIn);
        chosenEvent.invite(loggedIn);
        eventTable.editEvent(chosenEvent);
    }

    @Test
    public void rejectInviteTest() {
        EventTable eventTable = new EventTable();
        UserTable userTable = new UserTable();

        login();
        onView(withId(R.id.radioButton2)).perform(click());
        onData(anything()).inAdapterView(withId(R.id.list)).atPosition(0).perform(click());

        Event chosenEvent = eventTable.getEvent(eventTable.getInvitedEventIDs().get(0));
        User loggedIn = userTable.getUser(5);

        onView(withId(R.id.acceptInviteButton)).check(matches(isDisplayed()));
        onView(withId(R.id.rejectInviteButton)).check(matches(isDisplayed()));

        onView(withId(R.id.rejectInviteButton)).perform(click());

        onView(withId(R.id.radioButton3)).perform(click());
        onView(withText(chosenEvent.getEventName())).check(doesNotExist());

        chosenEvent.invite(loggedIn);
        eventTable.editEvent(chosenEvent);
    }

    @Test
    public void withdrawTest() {
        EventTable eventTable = new EventTable();
        UserTable userTable = new UserTable();

        login();
        onView(withId(R.id.radioButton3)).perform(click());
        onData(anything()).inAdapterView(withId(R.id.list)).atPosition(0).perform(click());

        Event chosenEvent = eventTable.getEvent(eventTable.getInvitedEventIDs().get(0));
        User loggedIn = userTable.getUser(5);

        onView(withId(R.id.withdrawButton)).check(matches(isDisplayed()));

        onView(withId(R.id.withdrawButton)).perform(click());

        onView(withId(R.id.radioButton3)).perform(click());
        onView(withText(chosenEvent.getEventName())).check(doesNotExist());

        chosenEvent.addParticipant(loggedIn);
        eventTable.editEvent(chosenEvent);
    }

    @Test
    public void editEventTest() {
        EventTable eventTable = new EventTable();
        UserTable userTable = new UserTable();

        // Log in and go to user event display page
        login();
        // Click on first event in list
        onData(anything()).inAdapterView(withId(R.id.list)).atPosition(0).perform(click());

        // Get event that was clicked on and current user from tables
        Event chosenEvent = eventTable.getEvent(eventTable.getInvitedEventIDs().get(0));
        User loggedIn = userTable.getUser(5);

        // Check that edit event button is visible
        onView(withId(R.id.editEventButton)).check(matches(isDisplayed()));

        // Click edit event button
        onView(withId(R.id.editEventButton)).perform(click());

        // Save old data to restore after test is over
        String oldName = chosenEvent.getEventName();
        Boolean oldStatus = chosenEvent.getStatusPublic();
        String oldInfo = chosenEvent.getDescription();
        Location oldLocation = chosenEvent.getLocation();
        ArrayList<TimeSlot> oldTimeSlots = chosenEvent.getTimeOptions();
        TimeSlot oldDueTime = chosenEvent.getDueTime();
        ArrayList<Integer> oldInvitees = chosenEvent.getInvitees();

        // Click name text field, clear text, type new name
        onView(withId(R.id.nameEventView)).perform(click());
        onView(withId(R.id.nameEventView)).perform(clearText());
        onView(withId(R.id.nameEventView)).perform(typeText("test name"), closeSoftKeyboard());

        // Click status dropdown and change to public
        onView(withId(R.id.statusPublicSpinner)).perform(click());
        onView(withText("Public")).perform(click());

        // Click description field, clear text, type new description
        onView(withId(R.id.descriptionEventView)).perform(click());
        onView(withId(R.id.descriptionEventView)).perform(clearText());
        onView(withId(R.id.descriptionEventView)).perform(typeText("testing editing"), closeSoftKeyboard());

        // Click location search, change location to USC
        onView(withId(R.id.locationEventSearch)).perform(click());
//        onView(withId(R.id.locationEventSearch)).perform(clearText());
//        onView(withId(R.id.locationEventSearch)).perform(typeText("USC"), pressKey(KeyEvent.KEYCODE_ENTER), closeSoftKeyboard());
    }
}
