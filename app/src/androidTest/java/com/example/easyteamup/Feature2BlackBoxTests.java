package com.example.easyteamup;

import static android.view.KeyEvent.KEYCODE_1;
import static android.view.KeyEvent.KEYCODE_6;
import static android.view.KeyEvent.KEYCODE_7;
import static android.view.KeyEvent.KEYCODE_C;
import static android.view.KeyEvent.KEYCODE_ENTER;
import static android.view.KeyEvent.KEYCODE_R;
import static android.view.KeyEvent.KEYCODE_S;
import static android.view.KeyEvent.KEYCODE_U;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.pressBack;
import static androidx.test.espresso.action.ViewActions.pressKey;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasValue;
import static org.hamcrest.Matchers.startsWith;

import android.app.LauncherActivity;
import android.util.Log;
import android.view.KeyEvent;

import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.PickerActions;
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
public class Feature2BlackBoxTests {

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
    }

    @Test
    public void createEventErrorTest() {
        login();

        // EventTable used for cleanup after test completes to allow for rerunning
        EventTable eventTable = new EventTable();

        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_create)).perform(click());
        onView(withId(R.id.saveEventButton)).perform(click());
        onView(withText("Error")).perform(pressBack());
        onView(withText("Error")).perform(pressBack());
        onView(withId(R.id.nameEventView)).check(matches(hasErrorText("Title is required.")));

        onView(withId(R.id.nameEventView)).perform(typeText("2.4 Error Event"), closeSoftKeyboard());
        onView(withId(R.id.saveEventButton)).perform(click());
        onView(withText("Error")).perform(pressBack());
        onView(withText("Error")).perform(pressBack());

        onView(withId(R.id.dateTimeSet)).perform(closeSoftKeyboard(), click());
        onView(withId(R.id.dateDuePicker)).perform(PickerActions.setDate(2022, 5, 1));
        onView(withId(R.id.timeDuePicker)).perform(PickerActions.setTime(10, 30));
        onView(withId(R.id.dateTimeDueSet)).perform(click());

        onView(withId(R.id.locationEventSearch)).perform(click(), pressKey(KEYCODE_R), pressKey(KEYCODE_ENTER));
        onView(withText("Error")).perform(pressBack());
        onView(withText("Error")).perform(pressBack());
        closeSoftKeyboard();
    }

    @Test
    public void createBasicPublicEventTest() {
        login();

        // EventTable used for cleanup after test completes to allow for rerunning
        EventTable eventTable = new EventTable();

        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_create)).perform(click());

        onView(withId(R.id.nameEventView)).perform(typeText("2.4 Basic Public Event"), closeSoftKeyboard());
        onView(withId(R.id.statusPublicSpinner)).perform(click());
        onView(withText("Public")).perform(click());
        onView(withId(R.id.dateTimeSet)).perform(click());

        onView(withId(R.id.dateDuePicker)).perform(PickerActions.setDate(2022, 5, 1));
        onView(withId(R.id.timeDuePicker)).perform(PickerActions.setTime(10, 30));
        onView(withId(R.id.dateTimeDueSet)).perform(click());

        onView(withId(R.id.saveEventButton)).perform(click());

        onView(withId(R.id.ownerDetailsView)).check(matches(withText("billy bobby :)")));
        onView(withId(R.id.statusPublicDetailsView)).check(matches(withText("Status: Public")));
        onView(withId(R.id.invitedUsersDetailsView)).check(matches(withText("none")));
        onView(withId(R.id.participantsDetailsView)).check(matches(withText("none")));


        // TEST FINISHED
        // Manually delete event to allow for test to be rerun
        eventTable.removeEvent("2.4 Basic Public Event");
    }

    @Test
    public void createFullPublicEvent() {
        login();

        // EventTable used for cleanup after test completes to allow for rerunning
        EventTable eventTable = new EventTable();

        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_create)).perform(click());

        onView(withId(R.id.nameEventView)).perform(typeText("2.4 Full Public Event"), closeSoftKeyboard());
        onView(withId(R.id.statusPublicSpinner)).perform(click());
        onView(withText("Public")).perform(click());
        onView(withId(R.id.descriptionEventView)).perform(typeText("This is a public event with all information to test event creation for Project 2.4"), closeSoftKeyboard());
        onView(withId(R.id.locationEventSearch)).perform(click(), pressKey(KEYCODE_U), pressKey(KEYCODE_S), pressKey(KEYCODE_C), pressKey(KEYCODE_ENTER));
        onView(withText("Success")).perform(pressBack());
        closeSoftKeyboard();
        onView(withId(R.id.nameEventView)).perform(click(), closeSoftKeyboard());

        onView(withId(R.id.timeSlotsEventButton)).perform(click());
        onView(withId(R.id.addButton)).perform(click());
        onView(withId(R.id.datePicker)).perform(PickerActions.setDate(2022, 6, 4));
        onView(withId(R.id.timePicker)).perform(PickerActions.setTime(11, 30));
        onView(withId(R.id.timeSlotDurationView)).perform(typeText("90"), closeSoftKeyboard());
        onView(withId(R.id.dateTimeSet)).perform(click());
        onView(withId(R.id.timeSlotsEventButton)).perform(click());
        onData(anything()).inAdapterView(withId(R.id.timeSlotsList)).atPosition(0).check(matches(withText("Sat Jun 04 11:30:00 PDT 2022 (90 min)")));
        onView(withId(R.id.addButton)).perform(click());
        onView(withId(R.id.datePicker)).perform(PickerActions.setDate(2022, 6, 11));
        onView(withId(R.id.timePicker)).perform(PickerActions.setTime(9, 45));
        onView(withId(R.id.timeSlotDurationView)).perform(typeText("45"), closeSoftKeyboard());
        onView(withId(R.id.dateTimeSet)).perform(click());
        onView(withId(R.id.timeSlotsEventButton)).perform(click());
        onData(anything()).inAdapterView(withId(R.id.timeSlotsList)).atPosition(0).check(matches(withText("Sat Jun 04 11:30:00 PDT 2022 (90 min)")));
        onData(anything()).inAdapterView(withId(R.id.timeSlotsList)).atPosition(1).check(matches(withText("Sat Jun 11 09:45:00 PDT 2022 (45 min)")));
        onView(withId(R.id.backTimeSlotViewButton)).perform(click());

        onView(withId(R.id.dateTimeSet)).perform(click());
        onView(withId(R.id.dateDuePicker)).perform(PickerActions.setDate(2022, 5, 1));
        onView(withId(R.id.timeDuePicker)).perform(PickerActions.setTime(10, 30));
        onView(withId(R.id.dateTimeDueSet)).perform(click());
        onView(withId(R.id.dueTimeEventView)).check(matches(withText("Sun May 01 10:30:00 PDT 2022")));

        onView(withId(R.id.inviteEventSearch)).perform(click(), pressKey(KEYCODE_6), pressKey(KEYCODE_ENTER), closeSoftKeyboard());
        onView(withId(R.id.inviteSearchDisplayButton)).check(matches(withText("Joe Mulholland")));
        onView(withId(R.id.inviteSearchDisplayButton)).perform(click(), click());
        onView(withId(R.id.inviteEventSearch)).perform(click(), pressKey(KEYCODE_7), pressKey(KEYCODE_ENTER), closeSoftKeyboard());
        onView(withId(R.id.inviteSearchDisplayButton)).check(matches(withText("Erica De Guzman")));
        onView(withId(R.id.inviteSearchDisplayButton)).perform(click(), click());
        onView(withId(R.id.viewInvitedUsersButton)).perform(click());
        onData(anything()).inAdapterView(withId(R.id.invitedUsersList)).atPosition(0).check(matches(withText("Joe Mulholland (6)")));
        onData(anything()).inAdapterView(withId(R.id.invitedUsersList)).atPosition(1).check(matches(withText("Erica De Guzman (7)")));
        onView(withId(R.id.backInvitedUsersButton)).perform(click());

        onView(withId(R.id.saveEventButton)).perform(click());

        onView(withId(R.id.ownerDetailsView)).check(matches(withText("billy bobby :)")));
        onView(withId(R.id.statusPublicDetailsView)).check(matches(withText("Status: Public")));
        onView(withId(R.id.locationDetailsView)).check(matches(withText("usc")));
        // time options not implemented in feature 1 displaying event details yet so not tested
        // final time not implemented in feature 1 yet so not tested
        onView(withId(R.id.invitedUsersDetailsView)).check(matches(withText("Joe Mulholland, Erica De Guzman")));
        onView(withId(R.id.participantsDetailsView)).check(matches(withText("none")));

        // TEST FINISHED
        // Manually delete event to allow for test to be rerun
        eventTable.removeEvent("2.4 Full Public Event");
    }

    @Test
    public void createBasicPrivateEventTest() {
        login();

        // EventTable used for cleanup after test completes to allow for rerunning
        EventTable eventTable = new EventTable();

        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_create)).perform(click());

        onView(withId(R.id.nameEventView)).perform(typeText("2.4 Basic Private Event"), closeSoftKeyboard());
        onView(withId(R.id.statusPublicSpinner)).perform(click());
        onView(withText("Private")).perform(click());
        onView(withId(R.id.dateTimeSet)).perform(click());

        onView(withId(R.id.dateDuePicker)).perform(PickerActions.setDate(2022, 5, 1));
        onView(withId(R.id.timeDuePicker)).perform(PickerActions.setTime(10, 30));
        onView(withId(R.id.dateTimeDueSet)).perform(click());

        onView(withId(R.id.saveEventButton)).perform(click());

        onView(withId(R.id.ownerDetailsView)).check(matches(withText("billy bobby :)")));
        onView(withId(R.id.statusPublicDetailsView)).check(matches(withText("Status: Private")));
        onView(withId(R.id.invitedUsersDetailsView)).check(matches(withText("none")));
        onView(withId(R.id.participantsDetailsView)).check(matches(withText("none")));



        // TEST FINISHED
        // Manually delete event to allow for test to be rerun
        eventTable.removeEvent("2.4 Basic Private Event");
    }

    @Test
    public void createFullPrivateEvent() {
        login();

        // EventTable used for cleanup after test completes to allow for rerunning
        EventTable eventTable = new EventTable();

        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_create)).perform(click());

        onView(withId(R.id.nameEventView)).perform(typeText("2.4 Full Private Event"), closeSoftKeyboard());
        onView(withId(R.id.statusPublicSpinner)).perform(click());
        onView(withText("Private")).perform(click());
        onView(withId(R.id.descriptionEventView)).perform(typeText("This is a private event with all information to test event creation for Project 2.4"), closeSoftKeyboard());
        onView(withId(R.id.locationEventSearch)).perform(click(), pressKey(KEYCODE_U), pressKey(KEYCODE_S), pressKey(KEYCODE_C), pressKey(KEYCODE_ENTER));
        onView(withId(R.id.locationEventSearch)).perform(click(), pressKey(KEYCODE_U), pressKey(KEYCODE_S), pressKey(KEYCODE_C), pressKey(KEYCODE_ENTER));
        onView(withText("Success")).perform(pressBack());
        closeSoftKeyboard();

        onView(withId(R.id.timeSlotsEventButton)).perform(click());
        onView(withId(R.id.addButton)).perform(click());
        onView(withId(R.id.datePicker)).perform(PickerActions.setDate(2022, 6, 4));
        onView(withId(R.id.timePicker)).perform(PickerActions.setTime(11, 30));
        onView(withId(R.id.timeSlotDurationView)).perform(typeText("90"), closeSoftKeyboard());
        onView(withId(R.id.dateTimeSet)).perform(click());
        onView(withId(R.id.timeSlotsEventButton)).perform(click());
        onData(anything()).inAdapterView(withId(R.id.timeSlotsList)).atPosition(0).check(matches(withText("Sat Jun 04 11:30:00 PDT 2022 (90 min)")));
        onView(withId(R.id.addButton)).perform(click());
        onView(withId(R.id.datePicker)).perform(PickerActions.setDate(2022, 6, 11));
        onView(withId(R.id.timePicker)).perform(PickerActions.setTime(9, 45));
        onView(withId(R.id.timeSlotDurationView)).perform(typeText("45"), closeSoftKeyboard());
        onView(withId(R.id.dateTimeSet)).perform(click());
        onView(withId(R.id.timeSlotsEventButton)).perform(click());
        onData(anything()).inAdapterView(withId(R.id.timeSlotsList)).atPosition(0).check(matches(withText("Sat Jun 04 11:30:00 PDT 2022 (90 min)")));
        onData(anything()).inAdapterView(withId(R.id.timeSlotsList)).atPosition(1).check(matches(withText("Sat Jun 11 09:45:00 PDT 2022 (45 min)")));
        onView(withId(R.id.backTimeSlotViewButton)).perform(click());

        onView(withId(R.id.dateTimeSet)).perform(click());
        onView(withId(R.id.dateDuePicker)).perform(PickerActions.setDate(2022, 5, 1));
        onView(withId(R.id.timeDuePicker)).perform(PickerActions.setTime(10, 30));
        onView(withId(R.id.dateTimeDueSet)).perform(click());
        onView(withId(R.id.dueTimeEventView)).check(matches(withText("Sun May 01 10:30:00 PDT 2022")));

        onView(withId(R.id.inviteEventSearch)).perform(click(), pressKey(KEYCODE_6), pressKey(KEYCODE_ENTER), closeSoftKeyboard());
        onView(withId(R.id.inviteSearchDisplayButton)).check(matches(withText("Joe Mulholland")));
        onView(withId(R.id.inviteSearchDisplayButton)).perform(click(), click());
        onView(withId(R.id.inviteEventSearch)).perform(click(), pressKey(KEYCODE_7), pressKey(KEYCODE_ENTER), closeSoftKeyboard());
        onView(withId(R.id.inviteSearchDisplayButton)).check(matches(withText("Erica De Guzman")));
        onView(withId(R.id.inviteSearchDisplayButton)).perform(click(), click());
        onView(withId(R.id.viewInvitedUsersButton)).perform(click());
        onData(anything()).inAdapterView(withId(R.id.invitedUsersList)).atPosition(0).check(matches(withText("Joe Mulholland (6)")));
        onData(anything()).inAdapterView(withId(R.id.invitedUsersList)).atPosition(1).check(matches(withText("Erica De Guzman (7)")));
        onView(withId(R.id.backInvitedUsersButton)).perform(click());

        onView(withId(R.id.saveEventButton)).perform(click());

        onView(withId(R.id.ownerDetailsView)).check(matches(withText("billy bobby :)")));
        onView(withId(R.id.statusPublicDetailsView)).check(matches(withText("Status: Private")));
        onView(withId(R.id.locationDetailsView)).check(matches(withText("usc")));
        // time options not implemented in feature 1 displaying event details yet so not tested
        // final time not implemented in feature 1 yet so not tested
        onView(withId(R.id.invitedUsersDetailsView)).check(matches(withText("Joe Mulholland, Erica De Guzman")));
        onView(withId(R.id.participantsDetailsView)).check(matches(withText("none")));

        // TEST FINISHED
        // Manually delete event to allow for test to be rerun
        eventTable.removeEvent("2.4 Full Private Event");
    }

    @Test
    public void createAndDeleteTimeSlots() {
        login();

        // EventTable used for cleanup after test completes to allow for rerunning
        EventTable eventTable = new EventTable();

        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_create)).perform(click());

        onView(withId(R.id.nameEventView)).perform(typeText("2.4 Delete TS Test Event"), closeSoftKeyboard());
        onView(withId(R.id.statusPublicSpinner)).perform(click());
        onView(withText("Public")).perform(click());
        onView(withId(R.id.descriptionEventView)).perform(typeText("This is a public event to test deleting time slots during event creation for Project 2.4"), closeSoftKeyboard());

        onView(withId(R.id.timeSlotsEventButton)).perform(click());
        onView(withId(R.id.addButton)).perform(click());
        onView(withId(R.id.datePicker)).perform(PickerActions.setDate(2022, 6, 4));
        onView(withId(R.id.timePicker)).perform(PickerActions.setTime(11, 30));
        onView(withId(R.id.timeSlotDurationView)).perform(typeText("90"), closeSoftKeyboard());
        onView(withId(R.id.dateTimeSet)).perform(click());
        onView(withId(R.id.timeSlotsEventButton)).perform(click());
        onData(anything()).inAdapterView(withId(R.id.timeSlotsList)).atPosition(0).check(matches(withText("Sat Jun 04 11:30:00 PDT 2022 (90 min)")));
        onView(withId(R.id.addButton)).perform(click());
        onView(withId(R.id.datePicker)).perform(PickerActions.setDate(2022, 6, 11));
        onView(withId(R.id.timePicker)).perform(PickerActions.setTime(9, 45));
        onView(withId(R.id.timeSlotDurationView)).perform(typeText("45"), closeSoftKeyboard());
        onView(withId(R.id.dateTimeSet)).perform(click());
        onView(withId(R.id.timeSlotsEventButton)).perform(click());
        onData(anything()).inAdapterView(withId(R.id.timeSlotsList)).atPosition(0).check(matches(withText("Sat Jun 04 11:30:00 PDT 2022 (90 min)")));
        onData(anything()).inAdapterView(withId(R.id.timeSlotsList)).atPosition(1).check(matches(withText("Sat Jun 11 09:45:00 PDT 2022 (45 min)")));
        onView(withId(R.id.addButton)).perform(click());
        onView(withId(R.id.datePicker)).perform(PickerActions.setDate(2022, 6, 15));
        onView(withId(R.id.timePicker)).perform(PickerActions.setTime(13, 00));
        onView(withId(R.id.timeSlotDurationView)).perform(typeText("60"), closeSoftKeyboard());
        onView(withId(R.id.dateTimeSet)).perform(click());
        onView(withId(R.id.timeSlotsEventButton)).perform(click());
        onData(anything()).inAdapterView(withId(R.id.timeSlotsList)).atPosition(0).check(matches(withText("Sat Jun 04 11:30:00 PDT 2022 (90 min)")));
        onData(anything()).inAdapterView(withId(R.id.timeSlotsList)).atPosition(1).check(matches(withText("Sat Jun 11 09:45:00 PDT 2022 (45 min)")));
        onData(anything()).inAdapterView(withId(R.id.timeSlotsList)).atPosition(2).check(matches(withText("Wed Jun 15 13:00:00 PDT 2022 (60 min)")));
        onData(anything()).inAdapterView(withId(R.id.timeSlotsList)).atPosition(1).perform(click());
        onView(withId(R.id.timeSlotsEventButton)).perform(click());
        onData(anything()).inAdapterView(withId(R.id.timeSlotsList)).atPosition(0).check(matches(withText("Sat Jun 04 11:30:00 PDT 2022 (90 min)")));
        onData(anything()).inAdapterView(withId(R.id.timeSlotsList)).atPosition(1).check(matches(withText("Wed Jun 15 13:00:00 PDT 2022 (60 min)")));
        onView(withId(R.id.backTimeSlotViewButton)).perform(click());

        onView(withId(R.id.dateTimeSet)).perform(click());
        onView(withId(R.id.dateDuePicker)).perform(PickerActions.setDate(2022, 5, 1));
        onView(withId(R.id.timeDuePicker)).perform(PickerActions.setTime(10, 30));
        onView(withId(R.id.dateTimeDueSet)).perform(click());
        onView(withId(R.id.dueTimeEventView)).check(matches(withText("Sun May 01 10:30:00 PDT 2022")));

        onView(withId(R.id.saveEventButton)).perform(click());

        onView(withId(R.id.ownerDetailsView)).check(matches(withText("billy bobby :)")));
        onView(withId(R.id.statusPublicDetailsView)).check(matches(withText("Status: Public")));
        // time options not implemented in feature 1 displaying event details yet so not tested
        // final time not implemented in feature 1 yet so not tested
        onView(withId(R.id.invitedUsersDetailsView)).check(matches(withText("none")));
        onView(withId(R.id.participantsDetailsView)).check(matches(withText("none")));

        // TEST FINISHED
        // Manually delete event to allow for test to be rerun
        eventTable.removeEvent("2.4 Delete TS Test Event");
    }

    @Test
    public void createAndViewInvitedUser() {
        login();

        // EventTable used for cleanup after test completes to allow for rerunning
        EventTable eventTable = new EventTable();

        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_create)).perform(click());

        onView(withId(R.id.nameEventView)).perform(typeText("2.4 View Invitees Test Event"), closeSoftKeyboard());
        onView(withId(R.id.statusPublicSpinner)).perform(click());
        onView(withText("Public")).perform(click());
        onView(withId(R.id.descriptionEventView)).perform(typeText("This is a public event to test viewing invitees during event creation for Project 2.4"), closeSoftKeyboard());

        onView(withId(R.id.dateTimeSet)).perform(click());
        onView(withId(R.id.dateDuePicker)).perform(PickerActions.setDate(2022, 5, 1));
        onView(withId(R.id.timeDuePicker)).perform(PickerActions.setTime(10, 30));
        onView(withId(R.id.dateTimeDueSet)).perform(click());
        onView(withId(R.id.dueTimeEventView)).check(matches(withText("Sun May 01 10:30:00 PDT 2022")));

        onView(withId(R.id.inviteEventSearch)).perform(click(), pressKey(KEYCODE_6), pressKey(KEYCODE_ENTER), closeSoftKeyboard());
        onView(withId(R.id.inviteSearchDisplayButton)).check(matches(withText("Joe Mulholland")));
        onView(withId(R.id.inviteSearchDisplayButton)).perform(click(), click());
        onView(withId(R.id.inviteEventSearch)).perform(click(), pressKey(KEYCODE_7), pressKey(KEYCODE_ENTER), closeSoftKeyboard());
        onView(withId(R.id.inviteSearchDisplayButton)).check(matches(withText("Erica De Guzman")));
        onView(withId(R.id.inviteSearchDisplayButton)).perform(click(), click());
        onView(withId(R.id.inviteEventSearch)).perform(click(), pressKey(KEYCODE_1), pressKey(KEYCODE_7), pressKey(KEYCODE_ENTER), closeSoftKeyboard());
        onView(withId(R.id.inviteSearchDisplayButton)).check(matches(withText("Uma Durairaj")));
        onView(withId(R.id.inviteSearchDisplayButton)).perform(click(), click());
        onView(withId(R.id.viewInvitedUsersButton)).perform(click());
        onData(anything()).inAdapterView(withId(R.id.invitedUsersList)).atPosition(0).check(matches(withText("Joe Mulholland (6)")));
        onData(anything()).inAdapterView(withId(R.id.invitedUsersList)).atPosition(1).check(matches(withText("Erica De Guzman (7)")));
        onData(anything()).inAdapterView(withId(R.id.invitedUsersList)).atPosition(2).check(matches(withText("Uma Durairaj (17)")));
        onData(anything()).inAdapterView(withId(R.id.invitedUsersList)).atPosition(0).perform(click());

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Log.d("Test", "Timeout issue.");
        }
        onView(withId(R.id.nameOtherProfileView)).check(matches(withText("Joe Mulholland")));
        onView(withId(R.id.userIDOtherProfileView)).check(matches(withText("6")));
        onView(withId(R.id.usernameOtherProfileView)).check(matches(withText("joe")));
        onView(withId(R.id.emailOtherProfileView)).check(matches(withText("jmulholl@usc.edu")));
        onView(withId(R.id.phoneOtherProfileView)).check(matches(withText("2739573945")));
        onView(withId(R.id.otherInfoOtherProfileView)).check(matches(withText("CS310 Student at USC")));
        onView(withId(R.id.imageOtherProfileButton)).check(matches(withContentDescription("SUCCESS")));
        onView(withId(R.id.backToCreateButton)).perform(click());

        onView(withId(R.id.saveEventButton)).perform(click());

        onView(withId(R.id.ownerDetailsView)).check(matches(withText("billy bobby :)")));
        onView(withId(R.id.statusPublicDetailsView)).check(matches(withText("Status: Public")));
        // time options not implemented in feature 1 displaying event details yet so not tested
        // final time not implemented in feature 1 yet so not tested
        onView(withId(R.id.invitedUsersDetailsView)).check(matches(withText("Joe Mulholland, Erica De Guzman, Uma Durairaj")));
        onView(withId(R.id.participantsDetailsView)).check(matches(withText("none")));

        // TEST FINISHED
        // Manually delete event to allow for test to be rerun
        eventTable.removeEvent("2.4 View Invitees Test Event");
    }

    @Test
    public void createAndUninviteUser() {
        login();

        // EventTable used for cleanup after test completes to allow for rerunning
        EventTable eventTable = new EventTable();

        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_create)).perform(click());

        onView(withId(R.id.nameEventView)).perform(typeText("2.4 Uninvite User Test Event"), closeSoftKeyboard());
        onView(withId(R.id.statusPublicSpinner)).perform(click());
        onView(withText("Public")).perform(click());
        onView(withId(R.id.descriptionEventView)).perform(typeText("This is a public event to test uninviting a user during event creation for Project 2.4"), closeSoftKeyboard());

        onView(withId(R.id.dateTimeSet)).perform(click());
        onView(withId(R.id.dateDuePicker)).perform(PickerActions.setDate(2022, 5, 1));
        onView(withId(R.id.timeDuePicker)).perform(PickerActions.setTime(10, 30));
        onView(withId(R.id.dateTimeDueSet)).perform(click());
        onView(withId(R.id.dueTimeEventView)).check(matches(withText("Sun May 01 10:30:00 PDT 2022")));

        onView(withId(R.id.inviteEventSearch)).perform(click(), pressKey(KEYCODE_6), pressKey(KEYCODE_ENTER), closeSoftKeyboard());
        onView(withId(R.id.inviteSearchDisplayButton)).check(matches(withText("Joe Mulholland")));
        onView(withId(R.id.inviteSearchDisplayButton)).perform(click(), click());
        onView(withId(R.id.inviteEventSearch)).perform(click(), pressKey(KEYCODE_7), pressKey(KEYCODE_ENTER), closeSoftKeyboard());
        onView(withId(R.id.inviteSearchDisplayButton)).check(matches(withText("Erica De Guzman")));
        onView(withId(R.id.inviteSearchDisplayButton)).perform(click(), click());
        onView(withId(R.id.viewInvitedUsersButton)).perform(click());
        onData(anything()).inAdapterView(withId(R.id.invitedUsersList)).atPosition(0).check(matches(withText("Joe Mulholland (6)")));
        onData(anything()).inAdapterView(withId(R.id.invitedUsersList)).atPosition(1).check(matches(withText("Erica De Guzman (7)")));
        onView(withId(R.id.inviteEventSearch)).perform(click(), pressKey(KEYCODE_1), pressKey(KEYCODE_7), pressKey(KEYCODE_ENTER), closeSoftKeyboard());
        onView(withId(R.id.inviteSearchDisplayButton)).check(matches(withText("Uma Durairaj")));
        onView(withId(R.id.inviteSearchDisplayButton)).perform(click(), click());
        onView(withId(R.id.viewInvitedUsersButton)).perform(click());
        onData(anything()).inAdapterView(withId(R.id.invitedUsersList)).atPosition(0).check(matches(withText("Joe Mulholland (6)")));
        onData(anything()).inAdapterView(withId(R.id.invitedUsersList)).atPosition(1).check(matches(withText("Erica De Guzman (7)")));
        onData(anything()).inAdapterView(withId(R.id.invitedUsersList)).atPosition(2).check(matches(withText("Uma Durairaj (17)")));
        onData(anything()).inAdapterView(withId(R.id.invitedUsersList)).atPosition(0).perform(click());

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Log.d("Test", "Timeout issue.");
        }
        onView(withId(R.id.nameOtherProfileView)).check(matches(withText("Joe Mulholland")));
        onView(withId(R.id.userIDOtherProfileView)).check(matches(withText("6")));
        onView(withId(R.id.usernameOtherProfileView)).check(matches(withText("joe")));
        onView(withId(R.id.emailOtherProfileView)).check(matches(withText("jmulholl@usc.edu")));
        onView(withId(R.id.phoneOtherProfileView)).check(matches(withText("2739573945")));
        onView(withId(R.id.otherInfoOtherProfileView)).check(matches(withText("CS310 Student at USC")));
        onView(withId(R.id.imageOtherProfileButton)).check(matches(withContentDescription("SUCCESS")));

        onView(withId(R.id.uninviteUserButton)).perform(click());
        onView(withId(R.id.viewInvitedUsersButton)).perform(click());
        onData(anything()).inAdapterView(withId(R.id.invitedUsersList)).atPosition(0).check(matches(withText("Erica De Guzman (7)")));
        onData(anything()).inAdapterView(withId(R.id.invitedUsersList)).atPosition(1).check(matches(withText("Uma Durairaj (17)")));
        onView(withId(R.id.backToCreateButton)).perform(click());

        onView(withId(R.id.saveEventButton)).perform(click());

        onView(withId(R.id.ownerDetailsView)).check(matches(withText("billy bobby :)")));
        onView(withId(R.id.statusPublicDetailsView)).check(matches(withText("Status: Public")));
        // time options not implemented in feature 1 displaying event details yet so not tested
        // final time not implemented in feature 1 yet so not tested
        onView(withId(R.id.invitedUsersDetailsView)).check(matches(withText("Erica De Guzman, Uma Durairaj")));
        onView(withId(R.id.participantsDetailsView)).check(matches(withText("none")));

        // TEST FINISHED
        // Manually delete event to allow for test to be rerun
        eventTable.removeEvent("2.4 Uninvite User Test Event");
    }
}