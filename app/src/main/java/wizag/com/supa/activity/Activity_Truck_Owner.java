package wizag.com.supa.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.internal.http.multipart.MultipartEntity;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;
import com.koushikdutta.async.http.body.StringBody;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wizag.com.supa.MySingleton;
import wizag.com.supa.R;
import wizag.com.supa.SessionManager;
import wizag.com.supa.adapter.Adapter_Truck_Owner;
import wizag.com.supa.adapter.Supplier_Adapter;
import wizag.com.supa.models.Model_Supplier;
import wizag.com.supa.models.Model_Truck_Owner;

import static wizag.com.supa.activity.Activity_Driver_Register.encodeTobase64;

public class Activity_Truck_Owner extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    String register_truck_owner_url = "http://sduka.wizag.biz/api/v1/profiles/roles";
    RecyclerView recyclerView;
    Button submit;
    Adapter_Truck_Owner adapter;
    List<Model_Truck_Owner> trucks_list = new ArrayList<>();
    FloatingActionButton fab;
    JSONArray trucks;
    String plate_no_txt, driver_id_no_txt, axle_count_txt;
    SessionManager sessionManager;

    LinearLayout logbook_layout, link_driver_layout;

    private int SELECT_FILE = 1;
    private int REQUEST_CAMERA = 0;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    ImageView logbook_image;
    String logbook_image_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_truck_owner);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recycler_view);
        submit = findViewById(R.id.submit);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter_Truck_Owner(trucks_list, this);
        recyclerView.setAdapter(adapter);


        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTrucksDialog();

            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                JSONArray jsonArray = new JSONArray();
                for (int i = 0; i < trucks_list.size(); i++) {
                    trucks = jsonArray.put(trucks_list.get(i).getJSONObject());
                }


                registerTruckOwner();
            }
        });

    }

    public void showTrucksDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.layout_truck_owner_dialog, null);
        dialogBuilder.setView(dialogView);


        EditText plate_no;
        EditText driver_id_no;
        CheckBox link_driver;


        logbook_image = dialogView.findViewById(R.id.logbook_image);
        plate_no = dialogView.findViewById(R.id.plate_no);
        driver_id_no = dialogView.findViewById(R.id.driver_id);
        link_driver_layout = dialogView.findViewById(R.id.link_driver_layout);
        logbook_layout = dialogView.findViewById(R.id.logbook_layout);
        link_driver = dialogView.findViewById(R.id.link_driver);

        dialogBuilder.setTitle("Truck Details");
        dialogBuilder.setCancelable(false);
        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {


                plate_no_txt = plate_no.getText().toString();
                driver_id_no_txt = driver_id_no.getText().toString();
                /*get image */


                if (driver_id_no_txt.isEmpty() && link_driver.isChecked()) {
                    Toast.makeText(Activity_Truck_Owner.this, "Enter Attached Driver ID Number", Toast.LENGTH_LONG).show();
                } else if (plate_no_txt.isEmpty()) {
                    Toast.makeText(Activity_Truck_Owner.this, "Enter Truck Plate Number ", Toast.LENGTH_LONG).show();
                } else {

                    trucks_list.add(new Model_Truck_Owner(
                            logbook_image_txt,
                            plate_no_txt,
                            driver_id_no_txt));
                    adapter.notifyDataSetChanged();


                }

            }
        });

        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();


    }

    private void registerTruckOwner() {
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(Activity_Truck_Owner.this);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Signing in...");
        progressDialog.show();
        //getText


        StringRequest stringRequest = new StringRequest(Request.Method.POST, register_truck_owner_url, new Response.Listener<String>() {


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
                        Toast.makeText(Activity_Truck_Owner.this, message, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), Activity_Home.class));
                        finish();
                    } else if (status.equalsIgnoreCase("error")) {

                        Toast.makeText(Activity_Truck_Owner.this, message, Toast.LENGTH_LONG).show();


                    }

                    JSONArray jsonArray = obj.getJSONArray("data");
                    if (jsonArray != null) {
                        for (int k = 0; k < jsonArray.length(); k++) {
                            String data_message = jsonArray.getString(k);

                            if (status.equalsIgnoreCase("fail")) {
                                Toast.makeText(Activity_Truck_Owner.this, data_message, Toast.LENGTH_SHORT).show();
                            }
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Activity_Truck_Owner.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        error.printStackTrace();
                        Toast.makeText(Activity_Truck_Owner.this, "An error occurred" + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }


        ) {

            @Override
            protected HashMap<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();

                params.put("trucks", String.valueOf(trucks));
                params.put("role_id", "XTON");
                params.put("client_type", "mobile");

//                params.put("licence_file", "adwerty");
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


    public void itemClicked(View view) {
        CheckBox checkBox = (CheckBox) view;
        if (checkBox.isChecked()) {
            link_driver_layout.setVisibility(View.VISIBLE);
        } else {
            link_driver_layout.setVisibility(View.GONE);

        }
    }

    public void uploadLogbook(View view) {
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
                } else {
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

        Bitmap photo;
        if (requestCode == SELECT_FILE && resultCode == Activity.RESULT_OK) {
            photo = null;
            if (data != null) {
                try {
                    photo = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            logbook_layout.setVisibility(View.VISIBLE);
            logbook_image.setImageBitmap(photo);
            logbook_image_txt = encodeTobase64(photo);

        } else if (requestCode == REQUEST_CAMERA && resultCode == Activity.RESULT_OK) {
            logbook_layout.setVisibility(View.VISIBLE);
            photo = (Bitmap) data.getExtras().get("data");
            logbook_image.setImageBitmap(photo);
            logbook_image_txt = encodeTobase64(photo);
        }


    }


}
