package com.example.easyteamup;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.util.Log;

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

        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_userEventDisplay)).perform(click());
    }

    @Test
    public void displayTest() {
        login();
        onView(withId(R.id.radioButton2)).perform(click());
    }
}
