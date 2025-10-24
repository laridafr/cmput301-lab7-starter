package com.example.androiduitesting;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.equalTo;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ShowActivityTest {

    // Default launch of ShowActivity via Rule (like your MainActivityTest style)
    @Rule
    public ActivityScenarioRule<ShowActivity> scenario =
            new ActivityScenarioRule<>(
                    new Intent(ApplicationProvider.getApplicationContext(), ShowActivity.class)
                            .putExtra("city_name", "Edmonton")
            );


    @Test
    public void testShowActivityAppears() {
        onView(withId(R.id.text_city_name)).check(matches(isDisplayed()));
    }

    @Test
    public void testCityNameIsConsistent() {
        Intent i = new Intent(ApplicationProvider.getApplicationContext(), ShowActivity.class)
                .putExtra("city_name", "Vancouver");

        try (ActivityScenario<ShowActivity> s = ActivityScenario.launch(i)) {
            onView(withId(R.id.text_city_name)).check(matches(withText("Vancouver")));
        } // auto-closes; Rule’s instance remains for other tests
    }

    @Test
    public void testBackButton() {
        // Launch MainActivity just for this test
        try (ActivityScenario<MainActivity> main = ActivityScenario.launch(MainActivity.class)) {

            // Add a city through the UI
            onView(withId(R.id.button_add)).perform(click());
            onView(withId(R.id.editText_name)).perform(replaceText("Tokyo"));
            onView(withId(R.id.button_confirm)).perform(click());

            // Tap the list item to open ShowActivity
            onData(equalTo("Tokyo")).inAdapterView(withId(R.id.city_list)).perform(click());

            // Press custom BACK button in ShowActivity
            onView(withId(R.id.button_back)).perform(click());

            // We should be back on MainActivity
            onView(withId(R.id.city_list)).check(matches(isDisplayed()));
        }
    }
}