package com.example.userprofilescreen.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.userprofilescreen.Main_Menu_Activity;
import com.example.userprofilescreen.*;
import com.example.userprofilescreen.app.AppController;
import com.example.userprofilescreen.util.ConstUrl;

public class Reset_Password_Activity extends AppCompatActivity {
    private String TAG = Reset_Password_Activity.class.getSimpleName();
    private EditText pass1, pass2;
    private Button confirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        pass1 = findViewById(R.id.reset_PasswordEditText);
        pass2 = findViewById(R.id.reset_Password2EditText);
        confirmButton = findViewById(R.id.reset_ConfirmButton);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePasswordReq();
            }
        });
    }

    private void changePasswordReq() {
        if (pass1.getText().toString().compareTo(pass2.getText().toString()) == 0) {
            String url = ConstUrl.BASE_URL + "user/updatePassword/" + ConstUrl.USERNAME_ID + "/"
                          + pass1.getText().toString() + "/" + pass2.getText().toString();
            StringRequest stringRequest = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Toast.makeText(Reset_Password_Activity.this, response, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Reset_Password_Activity.this, Main_Menu_Activity.class));
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Reset_Password_Activity.this, "Failed to Handle Your Request", Toast.LENGTH_SHORT).show();
                }
            });

            AppController.getInstance().addToRequestQueue(stringRequest, TAG);
        }
    }
}