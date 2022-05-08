package com.example.userprofilescreen.UserProfile;

import static com.example.userprofilescreen.util.ConstUrl.GET_USERPROFILE_DETAILS;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.userprofilescreen.*;
import com.example.userprofilescreen.More.More_Activity;
import com.example.userprofilescreen.app.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class User_Profile_Activity extends AppCompatActivity {

    private String TAG = User_Profile_Activity.class.getSimpleName();
    private Button profileBackButton, profileEditProfileButton, profileDeleteAccountButton, profileLogOutButton;
    private ImageView userImageView;
    private TextView userNameTextView, userIDTextView, userAboutMeTextView, userStatsTextView;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        profileBackButton = findViewById(R.id.userProfile_BackButton);
        profileEditProfileButton = findViewById(R.id.userProfile_EditProfileButton);
        profileDeleteAccountButton = findViewById(R.id.userProfile_DeleteAccountButton);

        userNameTextView = findViewById(R.id.userProfile_UsernameTextView);
        userIDTextView = findViewById(R.id.userProfile_UserIdTextView);
        userImageView = findViewById(R.id.userProfile_ImageView);
        userAboutMeTextView = findViewById(R.id.userProfile_AboutMeTextView);
        userStatsTextView = findViewById(R.id.userProfile_StatsTextView);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        GenerateUserData();

        profileBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(User_Profile_Activity.this, More_Activity.class));
            }
        });

        profileEditProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(User_Profile_Activity.this, Edit_Profile_Activity.class));
            }
        });

//        profileDeleteAccountButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                deleteUser();
//            }
//        });
    }

    private void deleteUser() {
        // TODO: Get username and password to confirm delete and finally send a delete request
    }

    private  void showProgressDialog() {
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    private void hideProgressDialog() {
        if (progressDialog.isShowing()) {
            progressDialog.hide();
        }
    }

    public void GenerateUserData() {
        showProgressDialog();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, GET_USERPROFILE_DETAILS, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    userStatsTextView.setText("");
                    userAboutMeTextView.setText("");

                    long id = response.getLong("id");
                    String imageByte = response.getString("profilePictureBytes");
                    String userName = response.getString("userName");
                    String aboutMe = response.getString("aboutMe");
//                    String base64Image = imageByte.split(",")[1];
//                    byte[] encodeByte = Base64.decode(base64Image, Base64.DEFAULT);
                    byte[] encodeByte = Base64.decode(imageByte, Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);

                    userImageView.setImageBitmap(bitmap);
                    userNameTextView.setText("Username: "+ userName);
                    userIDTextView.setText("ID: " + String.valueOf(id));
                    userAboutMeTextView.setText(aboutMe);
                    userStatsTextView.append("\nHighscore: "  + response.getLong("highScore") + "\n");
                    userStatsTextView.append("\nTotal Games: " + response.getLong("numGamesPlayed") + "\n");
                    userStatsTextView.append("\nTotal Hours: " + response.getDouble("hoursPlayed") + "\n");
                    userStatsTextView.append("\nLongest Round: " + response.getLong("longestEventSurvived") + "\n");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                hideProgressDialog();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(User_Profile_Activity.this, "Failed to Retrieve User Data", Toast.LENGTH_LONG).show();
                hideProgressDialog();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        AppController.getInstance().addToRequestQueue(jsonObjectRequest, TAG);
    }

}