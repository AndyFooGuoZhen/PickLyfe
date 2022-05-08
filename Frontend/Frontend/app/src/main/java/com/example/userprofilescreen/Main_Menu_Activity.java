package com.example.userprofilescreen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.userprofilescreen.Chat.GlobalChatActivity;
import com.example.userprofilescreen.Chat.PrivateORGlobalChatActivity;
import com.example.userprofilescreen.Friends.FriendActivity;
import com.example.userprofilescreen.GameSetup.GameSetupActivity;
import com.example.userprofilescreen.LeaderBoard.LeaderBoardActivity;
import com.example.userprofilescreen.More.More_Activity;
import com.example.userprofilescreen.PerkScreen.Perk_Activity;
import com.example.userprofilescreen.UpdateScreen.Update_Activity;
import com.example.userprofilescreen.util.ConstUrl;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Main_Menu_Activity extends AppCompatActivity {

    private TextView welcomeText;
    private ImageView perkScreenIcon;

    private Button  leaderboardButton, friendButton, newGameButton, chatButton;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        welcomeText = findViewById(R.id.main_WelcomeText);
        perkScreenIcon = findViewById(R.id.main_PerksButton);
        leaderboardButton = findViewById(R.id.main_LeaderboardButton);
        friendButton = findViewById(R.id.main_FriendButton);
        newGameButton = findViewById(R.id.main_NewGameButton);
        chatButton = findViewById(R.id.main_chatButton);

        welcomeText.setText("Welcome " + ConstUrl.USERNAME + "!");
        initBottomNavigationView();

        perkScreenIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Main_Menu_Activity.this, Perk_Activity.class));
            }
        });

        leaderboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Main_Menu_Activity.this, LeaderBoardActivity.class));
            }
        });

        friendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Main_Menu_Activity.this, FriendActivity.class));
            }
        });

        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Main_Menu_Activity.this, GameSetupActivity.class));

            }
        });


        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Main_Menu_Activity.this, PrivateORGlobalChatActivity.class));


            }
        });}

            private void initBottomNavigationView() {
                BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
                bottomNavigationView.setSelectedItemId(R.id.menu_home);
                bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                        switch (item.getItemId()) {

                            case R.id.menu_updates:
                                startActivity(new Intent(getApplicationContext(), Update_Activity.class));
                                overridePendingTransition(0, 0);
                                return true;
                            case R.id.menu_home:
                                startActivity(new Intent(getApplicationContext(), Main_Menu_Activity.class));
                                overridePendingTransition(0, 0);
                                return true;
                            case R.id.menu_more:
                                startActivity(new Intent(getApplicationContext(), More_Activity.class));
                                overridePendingTransition(0, 0);
                                return true;
                        }

                        return false;

                    }
                });
            }

}