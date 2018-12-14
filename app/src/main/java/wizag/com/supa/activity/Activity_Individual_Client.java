package wizag.com.supa.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import wizag.com.supa.R;
import wizag.com.supa.SessionManager;

public class Activity_Individual_Client extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    EditText fname, lname, id_no, phone, kra_pin;
    Button register;
    String register_ind_client_url = "http://sduka.wizag.biz/api/v1/profiles/roles";
    String fname_txt, lname_txt, id_no_txt, phone_txt,
            kra_pin_txt;
    Bitmap photo;
    LinearLayout image_layout;
    ImageView id_image;
    private int SELECT_FILE = 1;
    private int REQUEST_CAMERA = 0;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_client);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        fname = findViewById(R.id.fname);
        lname = findViewById(R.id.lname);
        id_no = findViewById(R.id.id_no);
        phone = findViewById(R.id.phone);
        kra_pin = findViewById(R.id.kra_pin);

        id_image = findViewById(R.id.id_image);
        image_layout = findViewById(R.id.image_layout);
        image_layout.setVisibility(View.GONE);

        register = findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isNetworkConnected()) {
                    Toast.makeText(Activity_Individual_Client.this, "Ensure that you are connected to the internet", Toast.LENGTH_SHORT).show();
                } else {
                    registerIndividualClient();
                }

            }
        });
    }

    private void registerIndividualClient() {
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(Activity_Individual_Client.this);
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        fname_txt = fname.getText().toString();
        lname_txt = lname.getText().toString();
        id_no_txt = id_no.getText().toString();
        phone_txt = phone.getText().toString();
        kra_pin_txt = kra_pin.getText().toString();





        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, register_ind_client_url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject obj = new JSONObject(response);
                            pDialog.dismiss();
                            String message = obj.getString("message");
                            String status = obj.getString("status");
//                            JSONObject data = new JSONObject("data");
                            if (status.equalsIgnoreCase("success")) {
                                Toast.makeText(Activity_Individual_Client.this, message, Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), Activity_Home.class));
                                finish();
                            } else if (status.equalsIgnoreCase("error")) {

                                Toast.makeText(Activity_Individual_Client.this, message, Toast.LENGTH_SHORT).show();


                            }

                            JSONArray jsonArray = obj.getJSONArray("data");
                            for (int k = 0; k < jsonArray.length(); k++) {
                                String data_message = jsonArray.getString(k);

                                if (status.equalsIgnoreCase("fail")) {
                                    Toast.makeText(Activity_Individual_Client.this, data_message, Toast.LENGTH_SHORT).show();
                                }
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //Toast.makeText(Activity_Buy.this, "", Toast.LENGTH_SHORT).show();
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(Activity_Individual_Client.this, "Registration could not be completed" + error.getMessage(), Toast.LENGTH_SHORT).show();
                pDialog.dismiss();
            }
        }) {
            //adding parameters to the request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("fname", fname_txt);
                params.put("lname", lname_txt);
                params.put("phone", phone_txt);
                params.put("id_no", id_no_txt);
                params.put("kra_pin", kra_pin_txt);
                params.put("role_id", "XIND");
//                params.put("id_file", "blst786");
                //  params.put("")
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                sessionManager = new SessionManager(getApplicationContext());
                HashMap<String, String> user = sessionManager.getUserDetails();
                String accessToken = user.get("access_token");

                String bearer = "Bearer " + accessToken;
                Map<String, String> headersSys = super.getHeaders();
                Map<String, String> headers = new HashMap<String, String>();
                headersSys.remove("Authorization");
                headers.put("Authorization", bearer);
                headers.putAll(headersSys);
                return headers;
            }


        };
// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }


    public void showOptions(View view) {


        PopupMenu popup = new PopupMenu(this, view);

        // This activity implements OnMenuItemClickListener
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.id_menu);
        popup.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.existing:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);//
                startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);

                return true;
            case R.id.camera_photo:

                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_CAMERA_PERMISSION_CODE);
                }


               /* if (ContextCompat.checkSelfPermission(Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA},
                            MY_CAMERA_PERMISSION_CODE);
                }*/
                else {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, REQUEST_CAMERA);
                }

                return true;
            default:
                return false;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == SELECT_FILE) {
                photo = null;
                if (data != null) {
                    image_layout.setVisibility(View.VISIBLE);
                    try {
                        photo = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                id_image.setImageBitmap(photo);
            } else if (requestCode == REQUEST_CAMERA && resultCode == Activity.RESULT_OK) {
                image_layout.setVisibility(View.VISIBLE);
                photo = (Bitmap) data.getExtras().get("data");
                id_image.setImageBitmap(photo);

//                encodedCameraImage = encodeImage(photo);


            }


        }
    }
}
