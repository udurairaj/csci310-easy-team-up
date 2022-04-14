package com.example.easyteamup;

import static androidx.test.espresso.Espresso.*;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

import android.support.test.uiautomator.UiObjectNotFoundException;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.easyteamup.login.LoginActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Random;

@RunWith(AndroidJUnit4.class)
public class Feature1BlackBoxTests {

    @Rule
    public ActivityScenarioRule<LoginActivity> loginRule =
            new ActivityScenarioRule<LoginActivity>(LoginActivity.class);

    @Test
    public void signupValid()
    {
        UserTable ut = new UserTable();

//        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
//        StringBuilder salt = new StringBuilder();
//        Random rnd = new Random();
//        while (salt.length() < 10) { // length of the random string.
//            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
//            salt.append(SALTCHARS.charAt(index));
//        }
//        String username = salt.toString();


        onView(withId(R.id.usernameSignupBox)).perform(typeText("mikey123"), closeSoftKeyboard());
        onView(withId(R.id.passwordSignupBox)).perform(typeText("nljkjkjl"), closeSoftKeyboard());
        onView(withId(R.id.nameSignupBox)).perform(typeText("michael"), closeSoftKeyboard());
        onView(withId(R.id.emailSignupBox)).perform(typeText("mikey123@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.signupButton)).perform(click());
        ut.removeUser("mikey123");
        //Espresso.pressBack();


    }

    @Test
    public void signupUserAlreadyExists()
    {
        onView(withId(R.id.usernameSignupBox)).perform(typeText("bob"), closeSoftKeyboard());
        onView(withId(R.id.passwordSignupBox)).perform(typeText("bobbobbob"), closeSoftKeyboard());
        onView(withId(R.id.nameSignupBox)).perform(typeText("bobbobbob"), closeSoftKeyboard());
        onView(withId(R.id.emailSignupBox)).perform(typeText("bobbobbob"), closeSoftKeyboard());
        onView(withId(R.id.signupButton)).perform(click());
        Espresso.pressBack();
    }

    @Test
    public void signupNoPassword()
    {
//        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
//        StringBuilder salt = new StringBuilder();
//        Random rnd = new Random();
//        while (salt.length() < 10) { // length of the random string.
//            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
//            salt.append(SALTCHARS.charAt(index));
//        }
//        String username = salt.toString();


        onView(withId(R.id.usernameSignupBox)).perform(typeText("thiswontwork"), closeSoftKeyboard());
        onView(withId(R.id.nameSignupBox)).perform(typeText("michael"), closeSoftKeyboard());
        onView(withId(R.id.emailSignupBox)).perform(typeText("whouseshotmail@hotmail.com"), closeSoftKeyboard());
        onView(withId(R.id.signupButton)).perform(click());
        //Espresso.pressBack();
    }


    @Test
    public void mapView() throws UiObjectNotFoundException {
        onView(withId(R.id.usernameLoginBox)).perform(typeText("joe"));
        onView(withId(R.id.passwordLoginBox)).perform(typeText("password"), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.mapsFragment)).perform(click());

//        UiDevice device=UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
//        UiObject marker = device.findObject(new UiSelector().descriptionMatches("marker 1"));
//        marker.click();
    }

    @Test
    public void listView()
    {
        onView(withId(R.id.usernameLoginBox)).perform(typeText("joe"));
        onView(withId(R.id.passwordLoginBox)).perform(typeText("password"), closeSoftKeyboard());
        onView(withId(R.id.loginButton)).perform(click());
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.listFragment)).perform(click());
        DataInteraction materialTextView = onData(anything())
                .inAdapterView(allOf(withId(R.id.list),
                        childAtPosition(
                                withClassName(is("android.widget.LinearLayout")),
                                3)))
                .atPosition(2);
        materialTextView.perform(click());
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }




//    public void displayTest() throws UiObjectNotFoundException {
//        signupInvalid();
//        signupValid();
//        mapView();
//        listView();
//    }


}
