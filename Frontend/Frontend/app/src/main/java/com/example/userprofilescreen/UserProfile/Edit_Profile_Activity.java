package com.example.userprofilescreen.UserProfile;

import static com.example.userprofilescreen.util.ConstUrl.GET_IMAGE_URL;
import static com.example.userprofilescreen.util.ConstUrl.GET_USERPROFILE_DETAILS;
import static com.example.userprofilescreen.util.ConstUrl.PUT_IMAGE_URL;
import static com.example.userprofilescreen.util.ConstUrl.PUT_USERABOUTME_URL;
import static com.example.userprofilescreen.util.ConstUrl.PUT_USERNAME_URL;
import static com.example.userprofilescreen.util.ConstUrl.PUT_USERPROFILE_URL;
import static com.example.userprofilescreen.util.ConstUrl.USERNAME_ID;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.userprofilescreen.*;
import com.example.userprofilescreen.app.AppController;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class Edit_Profile_Activity extends AppCompatActivity {

    private String TAG = Edit_Profile_Activity.class.getSimpleName();
    private String TAG2 = Edit_Profile_Activity.class.getSimpleName() + "aboutMe";
    private String TAG3 = Edit_Profile_Activity.class.getSimpleName() + "profileImage";
    private String imageEncoded;
    private Button editApplyChangesButton, editBackButton;
    private EditText userNameInput, userAboutMeInput;
    private ImageView userImage;
    private ProgressDialog progressDialog;
    private static final int REQUEST_PERMISSIONS = 100;
    private static final int PICK_IMAGE_REQUEST = 1 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);


        editApplyChangesButton = findViewById(R.id.editProfile_ApplyChangesButton);
        editBackButton = findViewById(R.id.editProfile_BackButton);

        userNameInput = findViewById(R.id.editProfile_NameEditText);
        userAboutMeInput = findViewById(R.id.editProfile_AboutMeEditText);
        userImage = findViewById(R.id.editProfile_ImageView);

        GenerateUserData();




        /**
         * Button listener for Applying Changes to User Profile
         */
        editApplyChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendProfileReq();
            }
        });


        /**
         * Button listener for Back Button of the Edit Profile Screen
         */
        editBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Edit_Profile_Activity.this, User_Profile_Activity.class));
            }
        });

        /**
         * Button listener for the image to prompt users to pick an image
         */
        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                /**
//                 * If permission for gallery is not given, ask for permission in settings, else doesn't work
//                 */
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(Edit_Profile_Activity.this,
//                        Manifest.permission.READ_EXTERNAL_STORAGE)
//                        != PackageManager.PERMISSION_GRANTED) {
//                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
//                            Uri.parse("package:" + getPackageName()));
//                    finish();
//                    startActivity(intent);
//                    return;
//                }
//                // Show gallery
//                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(i, 100);


                showFileChooser();
            }
        });
    }

    private void showFileChooser() {
        Intent pickImageIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickImageIntent.setType("image/*");
        pickImageIntent.putExtra("aspectX", 1);
        pickImageIntent.putExtra("aspectY", 1);
        pickImageIntent.putExtra("scale", true);
        pickImageIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        startActivityForResult(pickImageIntent, PICK_IMAGE_REQUEST);
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
//
//            //getting the image Uri
//            Uri imageUri = data.getData();
//            try {
//                //getting bitmap object from uri
//                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
//
//                //displaying selected image to imageview
//                userImage.setImageBitmap(bitmap);
//                uploadBitmap(bitmap);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                Bitmap lastBitmap = null;
                lastBitmap = bitmap;
                userImage.setImageBitmap(lastBitmap);
                //encoding image to string
                imageEncoded = getStringImage(lastBitmap);
                Log.d("image",imageEncoded);
                //passing the image to volley

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 0, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void SendImage( final String image) {
        final StringRequest stringRequest = new StringRequest(Request.Method.PUT, PUT_IMAGE_URL + "test/" + USERNAME_ID,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("upload",response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
    },
            new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(Edit_Profile_Activity.this, "No internet connection", Toast.LENGTH_LONG).show();

        }
    }) {
        @Override
        protected Map<String, String> getParams() throws AuthFailureError {

            Map<String, String> params = new Hashtable<String, String>();

            params.put("pic", image);
            return params;
        }
    };
    {
        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(stringRequest, TAG);
    }
}



    /**
     * A method whoch takes in a Bitmap and give its corresponding byte array to be stored on the server. It compresses the image with 100% quality
     * @param
     * @return Byte array of image in png form with 100% resolution
     */
//    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
//        return byteArrayOutputStream.toByteArray();
//    }

//    private void uploadBitmap(final Bitmap bitmap) {
//
//        String url = PUT_IMAGE_URL + "/" + USERNAME_ID;
//        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.PUT, url, new Response.Listener<NetworkResponse>()
//        {
//            @Override
//            public void onResponse(NetworkResponse response) {
//                try {
//                    JSONObject obj = new JSONObject(new String(response.data));
//                    Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
//                Log.e("GotError",""+error.getMessage());
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.put("tags", TAG3);
//                return params;
//            }
//
//            @Override
//            protected Map<String, DataPart> getByteData() {
//                Map<String, DataPart> params = new HashMap<>();
//                long uniqueImageName = System.currentTimeMillis();
//                params.put("file", new DataPart(uniqueImageName + ".png", getFileDataFromDrawable(bitmap)));
//                return params;
//            }
//        };
//
//        //adding the request to volley
//        AppController.getInstance().addToRequestQueue(volleyMultipartRequest, TAG);
//    }


    private void showProgressDialog() {
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    private void hideProgressDialog() {
        if (progressDialog.isShowing()) {
            progressDialog.hide();
        }
    }

    private void sendProfileReq() {
//        uploadBitmap(bitmap);
        SendImage(imageEncoded);
        putUserReq();
    }

    private void putUserReq() {
        showProgressDialog();
        String url = PUT_USERPROFILE_URL + USERNAME_ID + PUT_USERNAME_URL + userNameInput.getText().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                Toast.makeText(Edit_Profile_Activity.this, "Successfully Upload Username Request", Toast.LENGTH_SHORT).show();
                hideProgressDialog();
                showProgressDialog();

                String url2 = PUT_USERPROFILE_URL + USERNAME_ID + PUT_USERABOUTME_URL + userAboutMeInput.getText().toString();
                StringRequest string2Request = new StringRequest(Request.Method.PUT, url2, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG2, response.toString());
                        Toast.makeText(Edit_Profile_Activity.this, "Successfully Upload User About Me Request", Toast.LENGTH_SHORT).show();
                        hideProgressDialog();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG2, "ERROR: " + error.getMessage());
                        Toast.makeText(Edit_Profile_Activity.this, "Failed to Upload User About Me Request", Toast.LENGTH_LONG).show();
                        hideProgressDialog();
                    }
                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> header = new HashMap<String, String>();
                        header.put("Content-Type", "application/json");
                        return header;
                    }
                };

                AppController.getInstance().addToRequestQueue(string2Request, TAG2);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "ERROR: " + error.getMessage());
                Toast.makeText(Edit_Profile_Activity.this, "Failed to Upload Username Request", Toast.LENGTH_LONG).show();
                hideProgressDialog();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<String, String>();
                header.put("Content-Type", "application/json");
                return header;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest, TAG);
    }


    public void GenerateUserData() {
        showProgressDialog();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, GET_USERPROFILE_DETAILS, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    String aboutMe = response.getString("aboutMe");
                    String userName = response.getString("userName");
                    String imageByte = response.getString("profilePictureBytes");
//                    String base64Image = imageByte.split(",")[1];
                    byte[] encodeByte = Base64.decode(imageByte, Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                    userImage.setImageBitmap(bitmap);
                    userAboutMeInput.setText(aboutMe);
                    userNameInput.setText(userName);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                hideProgressDialog();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(Edit_Profile_Activity.this, "Failed to Retrieve User Data", Toast.LENGTH_LONG).show();
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

    private void GenerateImage() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, GET_IMAGE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String url = response.toString();
                Picasso.get().load(url).into(userImage);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                userImage.setImageResource(R.drawable.noimage);
            }
        });

        AppController.getInstance().addToRequestQueue(stringRequest, TAG);
    }
}
