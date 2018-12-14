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
import android.widget.ViewFlipper;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
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

public class Activity_Corporate_Client extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
    Button submit;
    EditText office_location, cert_no, company_pin, company_name;
    ViewFlipper flipper;
    String register_corporate_client_url = "http://sduka.wizag.biz/api/v1/profiles/roles";

    String cert_no_txt,
            company_pin_txt, company_name_txt;
    EditText email;
    LinearLayout image_layout;
    ImageView id_image;
    private int SELECT_FILE = 1;
    private int REQUEST_CAMERA = 0;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    Bitmap photo;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.company_details);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        office_location = findViewById(R.id.office_location);
        cert_no = findViewById(R.id.cert_no);
        company_pin = findViewById(R.id.company_pin);
        company_name = findViewById(R.id.company_name);
        email = findViewById(R.id.email);
        id_image = findViewById(R.id.id_image);
        image_layout = findViewById(R.id.image_layout);
        image_layout.setVisibility(View.GONE);

        flipper = findViewById(R.id.flipper);


        submit = findViewById(R.id.submit);
        submit.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.submit:
//                validate
                if (office_location.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Enter Vehicle model to continue", Toast.LENGTH_LONG).show();
                } else if (cert_no.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Enter Vehicle make to continue", Toast.LENGTH_LONG).show();
                } else if (company_pin.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Enter Vehicle plate number to continue", Toast.LENGTH_LONG).show();
                } else if (company_name.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Enter Vehicle logbook number to continue", Toast.LENGTH_LONG).show();
                } else if (email.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Enter email to proceed", Toast.LENGTH_LONG).show();
                } else if (!isNetworkConnected()) {

                    Toast.makeText(Activity_Corporate_Client.this, "Ensure you have internet connection", Toast.LENGTH_SHORT).show();

                } else {

                    registerCorporateClient();

//

                }

                break;


        }

    }


    private void registerCorporateClient() {
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(Activity_Corporate_Client.this);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Signing in...");
        progressDialog.show();
        //getText

        cert_no_txt = cert_no.getText().toString();
        company_pin_txt = company_pin.getText().toString();
        company_name_txt = company_name.getText().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, register_corporate_client_url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();

                try {
                    //converting response to json object
                    JSONObject obj = new JSONObject(response);

                    String message = obj.getString("message");
                    String status = obj.getString("status");
//                            JSONObject data = new JSONObject("data");
                    if (status.equalsIgnoreCase("success")) {
                        Toast.makeText(Activity_Corporate_Client.this, message, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), Activity_Home.class));
                        finish();
                    } else if (status.equalsIgnoreCase("error")) {

                        Toast.makeText(Activity_Corporate_Client.this, message, Toast.LENGTH_SHORT).show();


                    }

                    JSONArray jsonArray = obj.getJSONArray("data");
                    for (int k = 0; k < jsonArray.length(); k++) {
                        String data_message = jsonArray.getString(k);

                        if (status.equalsIgnoreCase("fail")) {
                            Toast.makeText(Activity_Corporate_Client.this, data_message, Toast.LENGTH_SHORT).show();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Activity_Corporate_Client.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(Activity_Corporate_Client.this, "An error occurred" + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }


        ) {

            @Override
            protected HashMap<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("email", email.getText().toString());
                params.put("location", office_location.getText().toString());
                params.put("certificate_number", cert_no_txt);
                params.put("kra_pin", company_pin_txt);
                params.put("company", company_name_txt);
                params.put("role_id", "XCOR");
//                params.put("id_file", "");
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
