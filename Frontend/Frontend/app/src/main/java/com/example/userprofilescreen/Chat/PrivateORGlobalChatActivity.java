package com.example.userprofilescreen.Chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;



import com.example.userprofilescreen.R;



public class PrivateORGlobalChatActivity extends AppCompatActivity {

    private Button globalChatBtn;
    private Button privateChatBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.private_or_globalchat_activity);
        globalChatBtn = findViewById(R.id.globalChatBtn);
        privateChatBtn = findViewById(R.id.privateChatBtn);

        globalChatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PrivateORGlobalChatActivity.this, GlobalChatActivity.class));
            }
        });

        privateChatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PrivateORGlobalChatActivity.this, SelectFriendsToChatActivity.class));

            }
        });




    }
}
