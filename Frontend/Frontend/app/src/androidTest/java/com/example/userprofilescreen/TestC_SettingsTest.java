package com.example.userprofilescreen;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.not;
import static org.hamcrest.CoreMatchers.endsWith;


import android.text.SpannableString;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.intent.Intents;
import androidx.test.filters.LargeTest;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;


import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import androidx.test.rule.ActivityTestRule;

import com.example.userprofilescreen.Login.Forgot_Password_Activity;
import com.example.userprofilescreen.Login.Login_Activity;
import com.example.userprofilescreen.Login.Register_Activity;
import com.example.userprofilescreen.More.More_Activity;
import com.example.userprofilescreen.Settings.Settings_Activity;
import com.example.userprofilescreen.UserProfile.User_Profile_Activity;

@RunWith(AndroidJUnit4ClassRunner.class)
@LargeTest
public class TestC_SettingsTest {
    private static final int DELAY_MS = 500;

    @Rule
    public ActivityTestRule<More_Activity> activityRule = new ActivityTestRule<>(More_Activity.class);

    @Test
    public void volumeAmount() {
        try {
            Thread.sleep(DELAY_MS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.more_VolumeAmount)).check(matches(withText("0")));
    }

    @Test
    public void profilePageButton() {
        Intents.init();
        onView(withId(R.id.more_Profile)).perform(click());

        try {
            Thread.sleep(DELAY_MS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        intended(hasComponent(User_Profile_Activity.class.getName()));
        Intents.release();
    }

    @Test
    public void settingsPageButton() {
        Intents.init();
        onView(withId(R.id.more_Settings)).perform(click());

        try {
            Thread.sleep(DELAY_MS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        intended(hasComponent(Settings_Activity.class.getName()));
        Intents.release();
    }

}
