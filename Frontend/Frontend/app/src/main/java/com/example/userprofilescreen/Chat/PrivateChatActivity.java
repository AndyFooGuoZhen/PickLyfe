package com.example.userprofilescreen.Chat;

import static com.example.userprofilescreen.util.ConstUrl.BASE_URL;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.userprofilescreen.EndGame.EndActivity;
import com.example.userprofilescreen.R;
import com.example.userprofilescreen.app.VolleySingleton;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class PrivateChatActivity extends AppCompatActivity {

    private WebSocketClient cc;
    private RecyclerView chatRecyclerView;
    private ArrayList<Chat> chatList;
    private chatsCardAdapter cCardAdapter ;
    private EditText msgTxt;
    private Button sendBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.privatechat_activity);
        msgTxt = findViewById(R.id.msgEditTxt);
        sendBtn = findViewById(R.id.sendBtn);
        chatList=new ArrayList<Chat>();

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    cc.send("@" + ConstPrivateChatDetails.friends_Name + " "+ msgTxt.getText().toString());
                } catch (Exception e) {
                    Log.d("ExceptionSendMessage:", e.getMessage().toString());
                }
            }
        });

        retrieveDMHistory();
        InitializeSocket(ConstPrivateChatDetails.user_Name);

    }

    private void retrieveDMHistory(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue = VolleySingleton.getInstance(this).getRequestQueue();
        String url = BASE_URL + "chat/getDirectMessageHistory/" + ConstPrivateChatDetails.user_Name +"/" + ConstPrivateChatDetails.friends_Name;


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //if private dm history is not empty, perform filtering
                if(response!="" && response.length()!=0){

                    if (response.endsWith("\n")) {
                        response = response.substring(0, (response.length() - 1)); //removes last \n
                    }

                    if (response.startsWith("[DM] ")) {
                        response = response.replace("[DM] ", "");
                    }

                    String[] individualDMMessages = response.split("\\n");

                    for (int i = 0; i < individualDMMessages.length; i++) {
                        String[] parts = individualDMMessages[i].split("\\:");
                        parts[1] = parts[1].replaceAll("@\\p{L}+\\p{Nd}+", ""); //removes @name from retrieved messages
                        chatList.add(new Chat(parts[0], parts[1]));

                    }

                    chatRecyclerView = findViewById(R.id.chatRecyclerView);
                    chatRecyclerView.setLayoutManager(new LinearLayoutManager(PrivateChatActivity.this));
                    cCardAdapter = new chatsCardAdapter(PrivateChatActivity.this, chatList, ConstPrivateChatDetails.user_Name);
                    chatRecyclerView.setAdapter(cCardAdapter);

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PrivateChatActivity.this, "Failed to fetch private Dm history game", Toast.LENGTH_LONG).show();
            }
        });

        requestQueue.add(stringRequest);
    }

    private void InitializeSocket(String userName){
        Draft[] drafts = {
                new Draft_6455()
        };

        String websocketUrl = "ws://10.0.2.2:8080/chat/" + userName;

        try {
            Log.d("Socket:", "Trying socket");
            cc = new WebSocketClient(new URI(websocketUrl), (Draft) drafts[0]) {

                @Override
                public void onMessage(String message) {
                    Log.d("", "run() returned: " + message);

                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            InitializeCardView(userName, message);
                        }
                    });

                }

                @Override
                public void onOpen(ServerHandshake handshake) {
                    Log.d("OPEN", "run() returned: " + "is connecting");
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    Log.d("CLOSE", "onClose() returned: " + reason);
                }

                @Override
                public void onError(Exception e) {
                    Log.d("Exception:", e.toString());
                }
            };
        }catch (URISyntaxException e) {
            Log.d("Exception:", e.getMessage().toString());
            e.printStackTrace();
        }
        cc.connect();
    }

    private void InitializeCardView(String userName, String message) {

        if (!message.equals("") && message.length() > 0) {

            if (message.endsWith("\n")) {
                message = message.substring(0, (message.length() - 1)); //removes last \n
            }

            String[] individualMessages = message.split("\\n");
            for (int i = 0; i < individualMessages.length; i++) {
                String[] parts = individualMessages[i].split("\\:");

                if(!parts[0].startsWith("@") && !parts[0].startsWith("[DM] " + ConstPrivateChatDetails.user_Name) && !parts[0].startsWith("[DM] " + ConstPrivateChatDetails.friends_Name)){
                    continue;
                }

                if (parts[0].startsWith("[DM] ")) {
                    parts[0] = parts[0].replace("[DM] ", "");
                }
                parts[1] = parts[1].replaceAll("@\\p{L}+\\p{Nd}+", ""); //removes @name from retrieved messages
                chatList.add(new Chat(parts[0], parts[1]));
            }

            chatRecyclerView = findViewById(R.id.chatRecyclerView);
            chatRecyclerView.setLayoutManager(new LinearLayoutManager(PrivateChatActivity.this));
            cCardAdapter = new chatsCardAdapter(PrivateChatActivity.this, chatList, userName);
            chatRecyclerView.setAdapter(cCardAdapter);
        }

    }

}
