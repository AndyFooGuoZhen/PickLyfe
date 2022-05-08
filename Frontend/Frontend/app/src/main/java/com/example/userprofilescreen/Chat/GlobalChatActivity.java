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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.userprofilescreen.R;
import com.example.userprofilescreen.app.VolleySingleton;
import com.example.userprofilescreen.util.ConstUrl;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class GlobalChatActivity extends AppCompatActivity {

    private String userName;
    private WebSocketClient cc;
    private RecyclerView chatRecyclerView;
    private ArrayList<Chat> chatList;
    private chatsCardAdapter cCardAdapter ;
    private EditText msgTxt;
    private Button sendBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);
        msgTxt = findViewById(R.id.msgEditTxt);
        sendBtn = findViewById(R.id.sendBtn);

        chatList=new ArrayList<Chat>();
        getUserDetails();

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    cc.send(msgTxt.getText().toString());
                } catch (Exception e) {
                    Log.d("ExceptionSendMessage:", e.getMessage().toString());
                }
            }
        });

    }

    //Get userName of user
    private void getUserDetails(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue = VolleySingleton.getInstance(this).getRequestQueue();
        String url = BASE_URL + "userprofile/" + ConstUrl.USERNAME_ID;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    userName =response.getString("userName");
                    InitializeSocket(userName);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(GlobalChatActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    private void InitializeSocket(String userName){
        Draft[] drafts = {
                new Draft_6455()
        };

        //Remember to change link here to localhost / remote
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

    private void InitializeCardView(String userName, String message){

        if(!message.equals("") && message.length()>0){
//            message = message.replace("\n", "");

          if(message.endsWith("\n")){
              message = message.substring(0, (message.length() - 1)); //removes last \n
          }



            String []individualMessages = message.split("\\n");
            for(int i=0; i<individualMessages.length; i++)
            {
                String[] parts = individualMessages[i].split("\\:");
                if(parts[0].startsWith("[DM] ") || parts[0].startsWith("@")){
                    continue;
                }
                chatList.add(new Chat(parts[0], parts[1]));
            }

            chatRecyclerView = findViewById(R.id.chatRecyclerView);
            chatRecyclerView.setLayoutManager(new LinearLayoutManager(GlobalChatActivity.this));
            cCardAdapter = new chatsCardAdapter(GlobalChatActivity.this, chatList, userName);
            chatRecyclerView.setAdapter(cCardAdapter);
        }



    }
}
