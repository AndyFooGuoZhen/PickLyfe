package com.example.userprofilescreen.More;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.userprofilescreen.Main_Menu_Activity;
import com.example.userprofilescreen.R;
import com.example.userprofilescreen.Settings.Settings_Activity;
import com.example.userprofilescreen.UpdateScreen.Update_Activity;
import com.example.userprofilescreen.UserProfile.User_Profile_Activity;
import com.example.userprofilescreen.app.AppController;
import com.example.userprofilescreen.util.ConstUrl;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

public class More_Activity extends AppCompatActivity {

    private String TAG = More_Activity.class.getSimpleName();
    private SeekBar volumeSlider;
    private TextView volumeAmt, profile, settings, aboutUs, FAQ;
    private ImageView logo;
    private MediaPlayer m;
    private AudioManager audioManager;
    private Button playAudioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);
        initBottomNavigationView();
        profile = findViewById(R.id.more_Profile);
        settings = findViewById(R.id.more_Settings);
        aboutUs = findViewById(R.id.more_AboutUs);
        FAQ = findViewById(R.id.more_FAQ);
        logo = findViewById(R.id.more_LogoImage);
        volumeAmt = findViewById(R.id.more_VolumeAmount);
        volumeSlider = findViewById(R.id.more_VolumeSlider);
        playAudioButton = findViewById(R.id.more_VolumeTest);
        m = MediaPlayer.create(this, R.raw.testsong);

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        volumeSlider.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        volumeSlider.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));

        generateSettings();

        volumeSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                adjustVolume(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(More_Activity.this, User_Profile_Activity.class));
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(More_Activity.this, Settings_Activity.class));
            }
        });

        playAudioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playAudio();
            }
        });
    }

    private void adjustVolume(int volume) {
        volumeAmt.setText(String.valueOf(volume));
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
        saveSettingsReq();
    }

    private void playAudio() {
        if (m.isPlaying()) {
            m.pause();
        } else {
            m.start();
        }
    }

    private void saveSettingsReq() {
        String url = ConstUrl.BASE_URL + "setting/volume/" + ConstUrl.USERNAME_ID + "/" + Integer.parseInt(volumeAmt.getText().toString());
        StringRequest stringRequest = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        AppController.getInstance().addToRequestQueue(stringRequest, TAG);
    }

    private void generateSettings() {
        String url = ConstUrl.BASE_URL + "setting/" + ConstUrl.USERNAME_ID;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "Received User's Volume");
                int volume = 0;
                try {
                    volume = response.getInt("volume");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adjustVolume(volume);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(More_Activity.this, "Failed to Handle Request", Toast.LENGTH_SHORT).show();
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjectRequest, TAG);
    }

    private void initBottomNavigationView() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.menu_more);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId()) {

                    case R.id.menu_updates:
                        startActivity(new Intent(getApplicationContext(), Update_Activity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.menu_home:
                        startActivity(new Intent(getApplicationContext(), Main_Menu_Activity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.menu_more:
                        startActivity(new Intent(getApplicationContext(), More_Activity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }
}