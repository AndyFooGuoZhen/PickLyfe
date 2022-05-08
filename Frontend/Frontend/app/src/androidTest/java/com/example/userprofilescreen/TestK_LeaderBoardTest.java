package com.example.userprofilescreen;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.rule.ActivityTestRule;

import com.example.userprofilescreen.Chat.GlobalChatActivity;
import com.example.userprofilescreen.LeaderBoard.LeaderBoardActivity;

import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

@RunWith(AndroidJUnit4ClassRunner.class)
@LargeTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestK_LeaderBoardTest {

    @Rule
    public ActivityTestRule<LeaderBoardActivity> activityRule = new ActivityTestRule<>(LeaderBoardActivity.class);

    @Test
    public void testLeaderBoardOrder(){
        String highestScoredPlayer ="name1";

        onView(withRecyclerView(R.id.leaderBoardRecyclerView)
                .atPositionOnView(0, R.id.userNameTxt))
                .check(matches(withText(highestScoredPlayer)));
    }

    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
        return new RecyclerViewMatcher(recyclerViewId);
    }
}
