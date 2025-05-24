package org.beginningandroid.interactivetest;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import androidx.test.espresso.intent.Intents;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {
    @Before
    public void setup(){
        Intents.init();
    }
    @After
    public void cleanup(){
        Intents.release();
    }
    @Test
    public void testLoginNavigatesToMapActivitypass() {
        ActivityScenario.launch(LoginActivity.class);
        onView(withId(R.id.editTextUsername)).perform(typeText("bruger"), closeSoftKeyboard());
        onView(withId(R.id.buttonLogin)).perform(click());
        intended(hasComponent(MapActivity.class.getName()));
    }
    @Test
        public void testLoginNavigatesToMapActivityfail () {
        ActivityScenario.launch(LoginActivity.class);
        onView(withId(R.id.editTextUsername)).perform(typeText(""), closeSoftKeyboard());
        onView(withId(R.id.buttonLogin)).perform(click());
        intended(hasComponent(MapActivity.class.getName()));
    }
 }