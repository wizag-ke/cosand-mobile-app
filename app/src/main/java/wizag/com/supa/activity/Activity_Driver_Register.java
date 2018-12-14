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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import wizag.com.supa.MySingleton;
import wizag.com.supa.R;
import wizag.com.supa.SessionManager;

public class Activity_Driver_Register extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
    SessionManager sessionManager;
    Button next_truck_details, submit, previous, upload_image;
    EditText model, make, plate_no, log_book, year, sacco_name;
    EditText licence_no, issue_date, validity, truck_owner_name,
            truck_owner_phone, axles;
    EditText endorsements, sacco_no;
    ImageView dl_image;

    LinearLayout image_layout;
    Button next_licence_info, previous_images;
    ViewFlipper flipper;
    String register_Driver_url = "http://sduka.wizag.biz/api/v1/profiles/roles";
    String tonnages_url = "http://sduka.wizag.biz/api/v1/trucks/tonnages";
    Spinner tonnage;
    ArrayList<String> Tonnage;
    int id_tonnage;
    JSONArray tonnage_array;
    private int SELECT_FILE = 1;
    private int REQUEST_CAMERA = 0;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    String model_txt, make_txt,
            plate_no_txt, log_book_txt, year_txt;

    String sacco_name_txt, sacco_no_txt, endorsements_txt;
    String licence_no_txt, issue_date_txt, validity_txt, truck_owner_name_txt,
            truck_owner_phone_txt, axles_txt;
    Bitmap photo;

    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_register);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        sessionManager = new SessionManager(getApplicationContext());
        HashMap<String, String> user = sessionManager.getUserDetails();
        token = user.get("access_token");

        Tonnage = new ArrayList<>();
        licence_no = findViewById(R.id.licence_no);
        issue_date = findViewById(R.id.issue_date);
        issue_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                issue();
            }
        });
        validity = findViewById(R.id.validity);
        validity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validity();
            }
        });
        truck_owner_name = findViewById(R.id.truck_owner_name);
        truck_owner_phone = findViewById(R.id.truck_owner_phone);
        axles = findViewById(R.id.axles);
        endorsements = findViewById(R.id.endorsements);
        sacco_no = findViewById(R.id.sacco_no);
        next_truck_details = findViewById(R.id.next_truck_details);
        next_truck_details.setOnClickListener(this);


        model = findViewById(R.id.model);
        make = findViewById(R.id.make);
        plate_no = findViewById(R.id.plate_no);
        log_book = findViewById(R.id.log_book);
        year = findViewById(R.id.year);
        year.setVisibility(View.GONE);
        sacco_name = findViewById(R.id.sacco_name);
        flipper = findViewById(R.id.flipper);
        tonnage = findViewById(R.id.tonnage);

        next_truck_details = findViewById(R.id.next_truck_details);
        upload_image = findViewById(R.id.upload_image);
        next_licence_info = findViewById(R.id.next_licence_info);
        previous_images = findViewById(R.id.previous_images);
        previous_images.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flipper.showPrevious();
            }
        });
        next_licence_info.setOnClickListener(this);
        next_truck_details.setOnClickListener(this);
        submit = findViewById(R.id.submit);
        submit.setOnClickListener(this);

        previous = findViewById(R.id.previous);


        image_layout = findViewById(R.id.image_layout);
        dl_image = findViewById(R.id.dl_image);

        tonnage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String value = tonnage.getSelectedItem().toString();

                try {
                    JSONObject dataClicked = tonnage_array.getJSONObject(i);
                    id_tonnage = dataClicked.getInt("id");
//                    getMaterialUnits();
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flipper.showPrevious();
            }
        });
        getTonnage();

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.next_truck_details:
//                validate
                if (model.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Enter Vehicle model to continue", Toast.LENGTH_LONG).show();
                } else if (make.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Enter Vehicle make to continue", Toast.LENGTH_LONG).show();
                } else if (plate_no.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Enter Vehicle plate number to continue", Toast.LENGTH_LONG).show();
                } /*else if (log_book.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Enter Vehicle logbook number to continue", Toast.LENGTH_LONG).show();
                } else if (year.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Enter Vehicle year of manufacture to continue", Toast.LENGTH_LONG).show();
                }*/ else {

                    flipper.showNext();
                }

                break;

            case R.id.next_licence_info:
                if (licence_no.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Enter DL Number to continue", Toast.LENGTH_SHORT).show();
                } else if (issue_date.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Enter DL issue date", Toast.LENGTH_SHORT).show();
                } else if (validity.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Enter date for DL validity", Toast.LENGTH_SHORT).show();
                } else if (truck_owner_name.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Enter truck owner name to continue", Toast.LENGTH_SHORT).show();
                } else if (truck_owner_phone.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Enter Truck owner phone Number to continue", Toast.LENGTH_SHORT).show();
                } else if (axles.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Enter Number of Axles to continue", Toast.LENGTH_SHORT).show();
                } else {
                    flipper.showNext();
                }

            case R.id.submit:
                if (endorsements.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Enter your DL endorsements to continue", Toast.LENGTH_SHORT).show();
                } else if (sacco_name.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Enter your sacco name to continue", Toast.LENGTH_SHORT).show();
                } else if (sacco_no.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Enter your sacco number to continue", Toast.LENGTH_SHORT).show();
                } else if (dl_image.getDrawable() == null) {
                    Toast.makeText(this, "Ensure Dl Image is taken to continue", Toast.LENGTH_SHORT).show();
                } else if (!isNetworkConnected()) {

                    Toast.makeText(Activity_Driver_Register.this, "Ensure you have internet connection", Toast.LENGTH_SHORT).show();

                } else {
                    registerDriver();
                }


        }

    }

    private void registerDriver() {
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(Activity_Driver_Register.this);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Signing in...");
        progressDialog.show();
        //getText
        model_txt = model.getText().toString();
        make_txt = make.getText().toString();
        plate_no_txt = plate_no.getText().toString();
        log_book_txt = log_book.getText().toString();
        year_txt = year.getText().toString();

        sacco_name_txt = sacco_name.getText().toString();
        sacco_no_txt = sacco_no.getText().toString();
        endorsements_txt = endorsements.getText().toString();
        licence_no_txt = licence_no.getText().toString();
        issue_date_txt = issue_date.getText().toString();
        validity_txt = validity.getText().toString();
        truck_owner_name_txt = truck_owner_name.getText().toString();

        truck_owner_phone_txt = truck_owner_phone.getText().toString();
        axles_txt = axles.getText().toString();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, register_Driver_url, new Response.Listener<String>() {


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
                        Toast.makeText(Activity_Driver_Register.this, message, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), Activity_Home.class));
                        finish();
                    } else if (status.equalsIgnoreCase("error")) {

                        Toast.makeText(Activity_Driver_Register.this, message, Toast.LENGTH_SHORT).show();


                    }

                    JSONArray jsonArray = obj.getJSONArray("data");
                    if (jsonArray != null) {
                        for (int k = 0; k < jsonArray.length(); k++) {
                            String data_message = jsonArray.getString(k);

                            if (status.equalsIgnoreCase("fail")) {
                                Toast.makeText(Activity_Driver_Register.this, data_message, Toast.LENGTH_SHORT).show();
                            }
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Activity_Driver_Register.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        error.printStackTrace();
                        Toast.makeText(Activity_Driver_Register.this, "An error occurred" + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }


        ) {

            @Override
            protected HashMap<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("model", model_txt);
                params.put("make", make_txt);
                params.put("plate_no", plate_no_txt);
                params.put("sacco", sacco_name_txt);
                params.put("sacco_member", sacco_no_txt);
                params.put("licence_endorsements", endorsements_txt);
                params.put("licence_number", licence_no_txt);
                params.put("issue_date", issue_date_txt);
                params.put("valid_upto", validity_txt);
                params.put("owner_name", truck_owner_name_txt);
                params.put("owner_phone", truck_owner_phone_txt);
                params.put("axle_count", axles_txt);
                params.put("tonnage_id", String.valueOf(id_tonnage));
                params.put("role_id", "XDRI");
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


    private void getTonnage() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();


        StringRequest stringRequest = new StringRequest(Request.Method.GET, tonnages_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    pDialog.dismiss();
                    if (jsonObject != null) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        tonnage_array = data.getJSONArray("tonnages");

                        for (int z = 0; z < tonnage_array.length(); z++) {
                            JSONObject suppliers_object = tonnage_array.getJSONObject(z);

                            String material_id = suppliers_object.getString("id");
                            String name = suppliers_object.getString("description");


                            if (tonnage_array != null) {

                                if (Tonnage.contains(name)) {


                                } else {

                                    //Toast.makeText(getApplicationContext(), "data\n" + size.getJSONObject(m).getString("size"), Toast.LENGTH_SHORT).show();
                                    Tonnage.add(name);
//                                    Type.add(materialTypes.getJSONObject(p).getString("id"));

                                }

                            }


                        }


                    }
                    tonnage.setAdapter(new ArrayAdapter<String>(Activity_Driver_Register.this, android.R.layout.simple_spinner_dropdown_item, Tonnage));


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                pDialog.dismiss();
                Toast.makeText(getApplicationContext(), "An Error Occurred" + error.getMessage(), Toast.LENGTH_LONG).show();

            }


        });

        MySingleton.getInstance(this).addToRequestQueue(stringRequest);


        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);


    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }


    public void uploadDL(View view) {


        PopupMenu popup = new PopupMenu(this, view);

        // This activity implements OnMenuItemClickListener
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.truck_menu);
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
                    try {
                        photo = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                image_layout.setVisibility(View.VISIBLE);
                dl_image.setImageBitmap(photo);
            } else if (requestCode == REQUEST_CAMERA && resultCode == Activity.RESULT_OK) {
                image_layout.setVisibility(View.VISIBLE);
                photo = (Bitmap) data.getExtras().get("data");
                dl_image.setImageBitmap(photo);

//                encodedCameraImage = encodeImage(photo);


            }


        }
    }


    public void validity() {
        final Calendar mcurrentDate = Calendar.getInstance();

        mcurrentDate.set(mcurrentDate.get(Calendar.YEAR), mcurrentDate.get(Calendar.MONTH), mcurrentDate.get(Calendar.DAY_OF_MONTH),
                mcurrentDate.get(Calendar.HOUR_OF_DAY), mcurrentDate.get(Calendar.MINUTE), 0);


        int mYear = mcurrentDate.get(Calendar.YEAR);
        int mMonth = mcurrentDate.get(Calendar.MONTH);
        int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

        android.app.DatePickerDialog mDatePicker = new android.app.DatePickerDialog(this, new android.app.DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                // TODO Auto-generated method stub
                /*      Your code   to get date and time    */
                mcurrentDate.set(Calendar.YEAR, selectedyear);
                mcurrentDate.set(Calendar.MONTH, selectedmonth);
                mcurrentDate.set(Calendar.DAY_OF_MONTH, selectedday);
                String myFormat = "yyyy-MM-dd"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
                validity.setText(sdf.format(mcurrentDate.getTime()));
            }
        }, mYear, mMonth, mDay);
        mDatePicker.setTitle("Select date");
        mDatePicker.show();

    }

    public void issue() {
        final Calendar mcurrentDate = Calendar.getInstance();

        mcurrentDate.set(mcurrentDate.get(Calendar.YEAR), mcurrentDate.get(Calendar.MONTH), mcurrentDate.get(Calendar.DAY_OF_MONTH),
                mcurrentDate.get(Calendar.HOUR_OF_DAY), mcurrentDate.get(Calendar.MINUTE), 0);


        int mYear = mcurrentDate.get(Calendar.YEAR);
        int mMonth = mcurrentDate.get(Calendar.MONTH);
        int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

        android.app.DatePickerDialog mDatePicker = new android.app.DatePickerDialog(this, new android.app.DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                // TODO Auto-generated method stub
                /*      Your code   to get date and time    */
                mcurrentDate.set(Calendar.YEAR, selectedyear);
                mcurrentDate.set(Calendar.MONTH, selectedmonth);
                mcurrentDate.set(Calendar.DAY_OF_MONTH, selectedday);
                String myFormat = "yyyy-MM-dd"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
                issue_date.setText(sdf.format(mcurrentDate.getTime()));
            }
        }, mYear, mMonth, mDay);
        mDatePicker.setTitle("Select date");
        mDatePicker.show();

    }

}
