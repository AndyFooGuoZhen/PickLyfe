package com.example.userprofilescreen.GameSetup;

import static com.example.userprofilescreen.util.ConstUrl.*;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.example.userprofilescreen.GameScreen.GameActivity;
import com.example.userprofilescreen.*;
import com.example.userprofilescreen.app.VolleySingleton;
import org.json.JSONObject;
import java.util.Arrays;

public class GameSetupActivity extends AppCompatActivity {

    private NumberPicker difficultyPicker;
    private int difficultySelected;
    private JSONObject userProfile;
    private Button perksBtn; //button for navigating to perks
    private Button startBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gamesetup_activity);

        perksBtn = findViewById(R.id.perksBtn);
        difficultyPicker = findViewById(R.id.difficultyPicker);
        String []difficultyArr = new String[4];
        difficultyArr[0] = "EASY";
        difficultyArr[1] = "MEDIUM";
        difficultyArr[2] = "HARD";
        difficultyArr[3] = "EXPERT";

        difficultyPicker.setMaxValue(difficultyArr.length - 1 );
        difficultyPicker.setMinValue(0);
        difficultyPicker.setDisplayedValues(difficultyArr);

        //default selection : EASY
        difficultySelected = 1;
        fetchingUserProfile();

        difficultyPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                difficultySelected = Arrays.asList(difficultyArr).indexOf(difficultyArr[i1]) + 1 ;
            }
        });

        startBtn = findViewById(R.id.startBtn);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    difficultySetup();
            }
        });
    }

    private void fetchingUserProfile(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = BASE_URL + "userprofile/" + USERNAME_ID;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                userProfile = response;
                String something = userProfile.toString();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(GameSetupActivity.this, "failed to fetch user", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    private void difficultySetup(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue = VolleySingleton.getInstance(this).getRequestQueue();
        String url = BASE_URL + "game/post/" + difficultySelected;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(GameSetupActivity.this, response, Toast.LENGTH_SHORT).show();

                if(userProfile!=null){
                    Intent intent = new Intent(GameSetupActivity.this, GameActivity.class);
                    startActivity(intent);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(GameSetupActivity.this, "failed to set difficulty", Toast.LENGTH_SHORT).show();
            }
        }){
            public byte[] getBody() {
                return userProfile.toString().getBytes();
            }
            public String getBodyContentType() {
                return "application/json";
            }
        };

        requestQueue.add(stringRequest);


    }





}
