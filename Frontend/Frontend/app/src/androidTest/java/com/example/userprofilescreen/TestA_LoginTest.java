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


@RunWith(AndroidJUnit4ClassRunner.class)
@LargeTest
public class TestA_LoginTest {
    private static final int DELAY_MS = 500;

    @Rule
    public ActivityTestRule<Login_Activity> activityRule = new ActivityTestRule<>(Login_Activity.class);

    @Test
    public void loginSuccess() {
        String username = "name1";
        String password = "password1";
        String resultString = "Welcome name1!";
        onView(withId(R.id.login_UsernameEditText)).perform(typeText(username), closeSoftKeyboard());
        onView(withId(R.id.login_PasswordEditText)).perform(typeText(password), closeSoftKeyboard());
        onView(withId(R.id.login_LoginButton)).perform(click());

        try {
            Thread.sleep(DELAY_MS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.main_WelcomeText)).check(matches(withText(endsWith(resultString))));
    }

//    @Test
//    public void loginFailed() {
//
//        String username = "wrongname";
//        String password = "wrongpassword1";
//        String resultString = "Login failure, username or email does not exist. Please try again!";
//        onView(withId(R.id.login_UsernameEditText)).perform(typeText(username), closeSoftKeyboard());
//        onView(withId(R.id.login_PasswordEditText)).perform(typeText(password), closeSoftKeyboard());
//        onView(withId(R.id.login_LoginButton)).perform(click());
//
//        try {
//            Thread.sleep(DELAY_MS * 4);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        // Doesnt work anymore with target SDK of >= 30, Currently no alternatives
////        onView(withText(endsWith(resultString))).inRoot(withDecorView(not(activityRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
//    }

    @Test
    public void forgetPassword() {
        Intents.init();
        ViewInteraction forgetBtn = onView(withId(R.id.login_ForgetPasswordTextView));
        forgetBtn.perform(click());
        intended(hasComponent(Forgot_Password_Activity.class.getName()));
        Intents.release();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void signUp() {
        Intents.init();
        onView(withId(R.id.login_SignUpTextView)).perform(clickClickableSpan("Sign-up"));
        intended(hasComponent(Register_Activity.class.getName()));
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
