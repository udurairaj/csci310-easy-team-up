package com.example.easyteamup;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.example.easyteamup.login.LoginActivity;

import org.junit.Rule;
import org.junit.Test;

public class BasicFeatureBlackBoxTests {

    @Rule
    public ActivityScenarioRule<LoginActivity> loginRule =
            new ActivityScenarioRule<LoginActivity>(LoginActivity.class);

    @Test
    public void testLogin() {
        onView(withId(R.id.usernameLoginBox)).perform(typeText("bob"));
        onView(withId(R.id.passwordLoginBox)).perform(typeText("bobbobbob"), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());
        onView(withId(R.id.nameProfileView)).check(matches(withText("billy bobby :)")));
    }
}
