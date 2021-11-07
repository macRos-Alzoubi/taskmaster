package com.example.taskmaster;

import android.content.Context;
import android.view.View;

import static androidx.test.espresso.Espresso.onView;

import static androidx.test.espresso.matcher.ViewMatchers.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;

import static androidx.test.espresso.action.ViewActions.*;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule
            = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.taskmaster", appContext.getPackageName());
    }

    @Test
    public void mainUI(){
        onView(withId(R.id.main_view_lable)).check(matches(isDisplayed()));
        onView(withId(R.id.button_add_task)).check(matches(isDisplayed()));
        onView(withId(R.id.main_text_username)).check(matches(isDisplayed()));
    }

    @Test
    public void displayTaskName(){
        onView(withId(R.id.task_recyleView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.text_detail_task_title)).check(matches(withText("wash the car")));
    }

    @Test
    public void setUsername(){
        onView(withId(R.id.button_settings))
                .perform(click());
        onView(withId(R.id.text_username))
                .perform(typeText("macRosX"), closeSoftKeyboard());
        onView(withId(R.id.button_save)).perform(click());
        Espresso.pressBack();
        onView(withId(R.id.main_text_username)).check(matches(withText("macRosX")));
    }

}