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
import com.example.userprofilescreen.*;
import com.example.userprofilescreen.app.AppController;
import com.example.userprofilescreen.util.ConstUrl;

public class Forgot_Password_Activity extends AppCompatActivity {

    private String TAG = Forgot_Password_Activity.class.getSimpleName();
    private Button sendEmail;
    private EditText userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        sendEmail = findViewById(R.id.forgot_SendEmailButton);
        userEmail = findViewById(R.id.forgot_EmailEditText);

        sendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
                if (userEmail.getText().toString().matches(emailRegex)) {
                    passwordResetRequest(userEmail.getText().toString());
                } else {
                    Toast.makeText(Forgot_Password_Activity.this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
                    userEmail.setText("");
                }
            }
        });
    }

    private void passwordResetRequest(String toString) {
        // TODO: Get Backend to create reset password feature
        String url = ConstUrl.GET_RESET_MAIL_URL + userEmail.getText().toString();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(Forgot_Password_Activity.this, response, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Forgot_Password_Activity.this, Login_Activity.class));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Forgot_Password_Activity.this, "Failed to handle your request", Toast.LENGTH_SHORT).show();
            }
        });

        AppController.getInstance().addToRequestQueue(stringRequest, TAG);
    }
}