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
public class MapActivityTest {
    @Test
    public void MapLoads(){
        ActivityScenario.launch(MapActivity.class);
        onView(withId(R.id.map)).check(matches(isDisplayed()));

    }
  
}