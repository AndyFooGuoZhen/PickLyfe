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


import android.content.Intent;
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
import com.example.userprofilescreen.UserProfile.Edit_Profile_Activity;
import com.example.userprofilescreen.UserProfile.User_Profile_Activity;

@RunWith(AndroidJUnit4ClassRunner.class)
@LargeTest
public class TestD_UserProfileTest {
    private static final int DELAY_MS = 500;

    @Rule
    public ActivityTestRule<User_Profile_Activity> activityRule = new ActivityTestRule<>(User_Profile_Activity.class);

    @Test
    public void detailsDisplayed() {
        String resultName = "Username: name1";
        String resultID = "ID: 1";

        try {
            Thread.sleep(DELAY_MS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.userProfile_UsernameTextView)).check(matches(withText(resultName)));
        onView(withId(R.id.userProfile_UserIdTextView)).check(matches(withText(resultID)));
    }

    @Test
    public void editProfile() {
        Intents.init();
        onView(withId(R.id.userProfile_EditProfileButton)).perform(click());

        try {
            Thread.sleep(DELAY_MS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        intended(hasComponent(Edit_Profile_Activity.class.getName()));
        Intents.release();
    }

    @Test
    public void backButton() {
        Intents.init();
        onView(withId(R.id.userProfile_BackButton)).perform(click());

        try {
            Thread.sleep(DELAY_MS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        intended(hasComponent(More_Activity.class.getName()));
        Intents.release();
    }
}
