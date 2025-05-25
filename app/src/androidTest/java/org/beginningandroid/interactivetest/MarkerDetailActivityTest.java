package org.beginningandroid.interactivetest;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

import androidx.test.core.app.ActivityScenario;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class MarkerDetailActivityTest {
    @Test
    public void testMarkerDetailShown(){
        ActivityScenario.launch(MarkerDetailActivity.class);
        onView(withId(R.id.textView)).check(matches(isDisplayed()));
    }

}