package org.beginningandroid.interactivetest;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class CameraActivityTest {
    @Test
    public void testCameraLaunchPermission() {
        ActivityScenario.launch(CameraActivity.class);

        onView(withId(R.id.previewView)).check(matches(isDisplayed()));
    }
}
