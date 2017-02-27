package com.fff.wifimonitor.activity;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.fff.wifimonitor.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void mainActivityTest() {
        ViewInteraction linearLayout = onView(
                allOf(withId(R.id.item_func_ap_switch), isDisplayed()));
        linearLayout.perform(click());

        ViewInteraction switch_ = onView(
                allOf(withId(R.id.switch_ap_auto_switch), isDisplayed()));
        switch_.perform(click());

    }

    @Test
    public void wifiInfoUiTest() {
        ViewInteraction wifiLinearLayout = onView(
                allOf(withId(R.id.item_func_wifi_info), isDisplayed()));
        wifiLinearLayout.perform(click());
        onView(with).check(matches(isDisplayed()));
    }

}
