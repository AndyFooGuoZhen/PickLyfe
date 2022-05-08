package com.example.userprofilescreen;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.util.Log;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.Toast;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.rule.ActivityTestRule;
import androidx.test.filters.LargeTest;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;

import com.example.userprofilescreen.GameScreen.GameActivity;
import com.example.userprofilescreen.GameSetup.GameSetupActivity;
import com.example.userprofilescreen.Login.Forgot_Password_Activity;
import com.example.userprofilescreen.Login.Login_Activity;

import java.util.Arrays;


@RunWith(AndroidJUnit4ClassRunner.class)
@LargeTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestM_ForgotPasswordTest {
    private static final int DELAY_MS = 500;

    @Rule
    public ActivityTestRule<Forgot_Password_Activity> activityTestRule = new ActivityTestRule<>(Forgot_Password_Activity.class);

    @Test
    public void T1_successEmail() {
        Intents.init();
        String email = "user1@email.com";
        onView(withId(R.id.forgot_EmailEditText)).perform(typeText(email), closeSoftKeyboard());
        onView(withId(R.id.forgot_SendEmailButton)).perform(click());

        try {
            Thread.sleep(DELAY_MS * 5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        intended(hasComponent(Login_Activity.class.getName()));
        Intents.release();
    }

    @Test
    public void T2_failedEmail() {
        String email = "notanemail";
        onView(withId(R.id.forgot_EmailEditText)).perform(typeText(email), closeSoftKeyboard());
        onView(withId(R.id.forgot_SendEmailButton)).perform(click());
        onView(withId(R.id.forgot_EmailEditText)).check(matches(withText("")));
    }
}
