package com.example.userprofilescreen;

import androidx.test.rule.ActivityTestRule;


import com.example.userprofilescreen.Login.Register_Activity;

import org.junit.Rule;
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

@RunWith(AndroidJUnit4ClassRunner.class)
@LargeTest
public class TestB_RegisterTest {
    private static final int DELAY_MS = 500;

    @Rule
    public ActivityTestRule<Register_Activity> activityRule = new ActivityTestRule<>(Register_Activity.class);

//    @Test
//    public void registerAccount() {
//        Intents.init();
//        String username = "ThrowawayTest";
//        String email = "throwaway@gmail.com";
//        String password = "testing123";
//
//        onView(withId(R.id.register_UsernameEditText)).perform(typeText(username), closeSoftKeyboard());
//        onView(withId(R.id.register_EmailEditText)).perform(typeText(email), closeSoftKeyboard());
//        onView(withId(R.id.register_PasswordEditText)).perform(typeText(password), closeSoftKeyboard());
//        onView(withId(R.id.register_ConfirmPasswordEditText)).perform(typeText(password), closeSoftKeyboard());
//        onView(withId(R.id.register_RegisterButton)).perform(click());
//
//        try {
//            Thread.sleep(DELAY_MS);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        intended(hasComponent(Login_Activity.class.getName()));
//        Intents.release();
//    }

    @Test
    public void hasAccount() {
        Intents.init();
        onView(withId(R.id.register_LoginTextView)).perform(clickClickableSpan("Log-in"));
        intended(hasComponent(Login_Activity.class.getName()));
        Intents.release();
    }

    public static ViewAction clickClickableSpan(final CharSequence textToClick) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return Matchers.instanceOf(TextView.class);
            }

            @Override
            public String getDescription() {
                return "clicking on a ClickableSpan";
            }

            @Override
            public void perform(UiController uiController, View view) {
                TextView textView = (TextView) view;
                SpannableString spannableString = (SpannableString) textView.getText();

                if (spannableString.length() == 0) {
                    // TextView is empty, nothing to do
                    throw new NoMatchingViewException.Builder()
                            .includeViewHierarchy(true)
                            .withRootView(textView)
                            .build();
                }

                // Get the links inside the TextView and check if we find textToClick
                ClickableSpan[] spans = spannableString.getSpans(0, spannableString.length(), ClickableSpan.class);
                if (spans.length > 0) {
                    ClickableSpan spanCandidate;
                    for (ClickableSpan span : spans) {
                        spanCandidate = span;
                        int start = spannableString.getSpanStart(spanCandidate);
                        int end = spannableString.getSpanEnd(spanCandidate);
                        CharSequence sequence = spannableString.subSequence(start, end);
                        if (textToClick.toString().contains(sequence.toString())) {
                            span.onClick(textView);
                            return;
                        }
                    }
                }

                // textToClick not found in TextView
                throw new NoMatchingViewException.Builder()
                        .includeViewHierarchy(true)
                        .withRootView(textView)
                        .build();

            }
        };
    }
}
