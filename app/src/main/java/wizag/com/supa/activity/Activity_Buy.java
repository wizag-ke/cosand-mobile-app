package wizag.com.supa.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import wizag.com.supa.GPSLocation;
import wizag.com.supa.MySingleton;
import wizag.com.supa.R;
import wizag.com.supa.SessionManager;
import wizag.com.supa.activity.Activity_Search_Places;


public class Activity_Buy extends AppCompatActivity {
    Spinner spinner, buy_spinner_size, buy_spinner_quality, buy_spinner_category;
    String URL = "http://sduka.wizag.biz/api/material";
    String POST_MATERIAL_URL = "http://sduka.wizag.biz/api/order-request";
    String POST_FCM = "http://sduka.wizag.biz/api/fcm-token";
    private static final String SHARED_PREF_NAME = "mysharedpref";
    LinearLayout buy_layout;
    ArrayList<String> CountryName;
    SessionManager sessionManager;
    String token;
    Button proceed;
    GPSLocation gps;

    ArrayList<String> CategoryName;
    ArrayList<String> QualityName;
    ArrayList<String> SizeName;
    ArrayList<String> PriceName;


    HashMap<String, String> map_values;
    HashMap<String, String> quality_values;
    HashMap<String, String> size_values;
    HashMap<String, String> category_values;

    String id_material;
    String id_quality;
    String id_size;
    String id_category;

    EditText quantity;
    String location;

    ProgressDialog progressDialog;
    String quantity_txt;
    private DrawerLayout mDrawerLayout;
    String firebaseToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);
        CountryName = new ArrayList<>();

        firebaseToken = FirebaseInstanceId.getInstance().getToken();
        Toast.makeText(getApplicationContext(), firebaseToken, Toast.LENGTH_SHORT).show();
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        /*post  fcm token*/
        postFCM();

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        progressDialog = new ProgressDialog(getApplicationContext());

        spinner = (Spinner) findViewById(R.id.buy_spinner);
        buy_spinner_size = (Spinner) findViewById(R.id.buy_spinner_size);
        buy_spinner_quality = (Spinner) findViewById(R.id.buy_spinner_quality);
        buy_spinner_category = (Spinner) findViewById(R.id.buy_spinner_category);

        map_values = new HashMap<String, String>();
        quality_values = new HashMap<String, String>();
        size_values = new HashMap<String, String>();
        category_values = new HashMap<String, String>();


        quantity = (EditText) findViewById(R.id.buy_quantity);

        CategoryName = new ArrayList<>();
        QualityName = new ArrayList<>();
        SizeName = new ArrayList<>();
        PriceName = new ArrayList<>();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here

                        return true;
                    }
                });


        buy_layout = (LinearLayout) findViewById(R.id.buy_layout);
        proceed = (Button) findViewById(R.id.proceed_buy);
        sessionManager = new SessionManager(getApplicationContext());
        HashMap<String, String> user = sessionManager.getUserDetails();
        token = user.get("access_token");
        //Toast.makeText(this, "Data" + token, Toast.LENGTH_SHORT).show();
        gps = new GPSLocation(this);
        if (gps.canGetLocation()) {
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            // Toast.makeText(this, "data\n"+longitude, Toast.LENGTH_SHORT).show();
            location = latitude + "," + longitude;
            // editor.putString("key_name", location);
            //String coordinates = latitude+ ","+longitude;
            //  Toast.makeText(gps, "data\n\n"+latitude, Toast.LENGTH_SHORT).show();
        } else {
            gps.showSettingsAlert();
        }

        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);


            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String value = spinner.getSelectedItem().toString();
                id_material = map_values.get(value);

                //Toast.makeText(getApplicationContext(), ""+id_material, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        buy_spinner_size.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String value = buy_spinner_size.getSelectedItem().toString();
                id_size = size_values.get(value);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        buy_spinner_quality.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String value = buy_spinner_quality.getSelectedItem().toString();
                id_quality = quality_values.get(value);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        buy_spinner_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String value = buy_spinner_category.getSelectedItem().toString();
                id_category = category_values.get(value);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sp = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();


                editor.putString("material_id", id_material);
                editor.putString("quality_id", id_quality);
                editor.putString("size_id", id_size);
                editor.apply();


                Intent intent = new Intent(getApplicationContext(), Activity_Search_Places.class);
                intent.putExtra("material_id", id_material);
                intent.putExtra("quality_id", id_quality);
                intent.putExtra("size_id", id_size);
                startActivity(intent);

            }
        });

        loadSpinnerData(URL);

    }

    private void postFCM() {

        com.android.volley.RequestQueue queue = Volley.newRequestQueue(Activity_Buy.this);
        com.android.volley.RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, POST_FCM,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            pDialog.dismiss();
                            JSONObject data = jsonObject.getJSONObject("data");
                            String success_message = data.getString("message");
                            // Snackbar.make(sell_layout, "New Request Created Successfully" , Snackbar.LENGTH_LONG).show();
                            //Snackbar.make(sell_layout, "New request created successfully", Snackbar.LENGTH_LONG).show();

                            Toast.makeText(getApplicationContext(), success_message, Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MenuActivity.class));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //Toast.makeText(Activity_Buy.this, "", Toast.LENGTH_SHORT).show();
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                Snackbar.make(buy_layout, "Request could not be placed", Snackbar.LENGTH_LONG).show();
                pDialog.dismiss();
            }
        }) {
            //adding parameters to the request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("token", firebaseToken);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                String bearer = "Bearer ".concat(token);
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


    private void loadSpinnerData(String url) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    pDialog.hide();
                    if (jsonObject != null) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        JSONArray materials = data.getJSONArray("materials");

                        if (materials != null) {
                            for (int i = 0; i < materials.length(); i++) {

                                JSONObject material_items = materials.getJSONObject(i);
                                String material_name = material_items.getString("name");
                                String material_id = material_items.getString("id");
                                map_values.put(material_name, material_id);

                                // Toast.makeText(getApplicationContext(), ""+map_values, Toast.LENGTH_SHORT).show();

                                if (material_items != null) {
                                    if (CountryName.contains(materials.getJSONObject(i).getString("name"))) {

                                    } else {

                                        CountryName.add(materials.getJSONObject(i).getString("name"));

                                    }


                                }

                                //categories
                                /*loop through categories*/

                                JSONArray category = material_items.getJSONArray("category");
                                for (int k = 0; k < category.length(); k++) {

                                    JSONObject category_items = category.getJSONObject(k);
                                    String category_name = category_items.getString("name");
                                    String category_id = category_items.getString("id");
                                    category_values.put(category_name, category_id);
                                    /*loop thro quality*/


                                    JSONArray quality = category_items.getJSONArray("quality");
                                    for (int l = 0; l < quality.length(); l++) {
                                        JSONObject quality_object = quality.getJSONObject(l);
                                        String quality_name = quality_object.getString("value");
                                        String quality_id = quality_object.getString("id");
                                        quality_values.put(quality_name, quality_id);
                                        /*loop thro size*/
                                        JSONObject size_items = quality.getJSONObject(l);
                                        JSONArray size = size_items.getJSONArray("size");

                                        for (int m = 0; m < size.length(); m++) {
                                            JSONObject size_objects = size.getJSONObject(m);
                                            String size_name = size_objects.getString("size");
                                            String size_id = size_objects.getString("id");
                                            size_values.put(size_name, size_id);

                                            if (size != null) {

                                                if (SizeName.contains(size.getJSONObject(m).getString("size"))) {


                                                } else {

                                                    SizeName.add(size.getJSONObject(m).getString("size"));
                                                    // Toast.makeText(getApplicationContext(), "data\n" + size.getJSONObject(m).getString("size"), Toast.LENGTH_SHORT).show();


                                                }

                                            }
                                            buy_spinner_size.setAdapter(new ArrayAdapter<String>(Activity_Buy.this, android.R.layout.simple_spinner_dropdown_item, SizeName));

                                        }


                                        if (quality != null) {

                                            if (QualityName.contains(quality.getJSONObject(l).getString("value"))) {


                                            } else {

                                                QualityName.add(quality.getJSONObject(l).getString("value"));
                                                // Toast.makeText(getApplicationContext(), "data\n" + quality.getJSONObject(l).getString("value"), Toast.LENGTH_SHORT).show();


                                            }

                                        }
                                        buy_spinner_quality.setAdapter(new ArrayAdapter<String>(Activity_Buy.this, android.R.layout.simple_spinner_dropdown_item, QualityName));

                                    }


                                    if (category != null) {
                                        if (CategoryName.contains(category.getJSONObject(k).getString("name"))) {


                                        } else {

                                            CategoryName.add(category.getJSONObject(k).getString("name"));
                                            //Toast.makeText(getApplicationContext(), "category\n" + category.getJSONObject(k).getString("name"), Toast.LENGTH_SHORT).show();


                                        }


                                    }
                                    buy_spinner_category.setAdapter(new ArrayAdapter<String>(Activity_Buy.this, android.R.layout.simple_spinner_dropdown_item, CategoryName));


                                }


                            }
                        }


                    }


                    spinner.setAdapter(new ArrayAdapter<String>(Activity_Buy.this, android.R.layout.simple_spinner_dropdown_item, CountryName));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(getApplicationContext(), "An Error Occurred", Toast.LENGTH_SHORT).show();
                pDialog.hide();
            }


        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                String bearer = "Bearer ".concat(token);
                Map<String, String> headersSys = super.getHeaders();
                Map<String, String> headers = new HashMap<String, String>();
                headersSys.remove("Authorization");
                headers.put("Authorization", bearer);
                headers.putAll(headersSys);
                return headers;
            }

        };
        MySingleton.getInstance(this).addToRequestQueue(stringRequest);


        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);


    }

    public void loadRequest() {

        com.android.volley.RequestQueue queue = Volley.newRequestQueue(Activity_Buy.this);
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, POST_MATERIAL_URL,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            pDialog.dismiss();
                            JSONObject data = jsonObject.getJSONObject("data");
                            String success_message = data.getString("message");
                            // Snackbar.make(sell_layout, "New Request Created Successfully" , Snackbar.LENGTH_LONG).show();
                            //Snackbar.make(sell_layout, "New request created successfully", Snackbar.LENGTH_LONG).show();

                            Toast.makeText(getApplicationContext(), "New request created successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), Activity_Search_Places.class));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //Toast.makeText(Activity_Buy.this, "", Toast.LENGTH_SHORT).show();
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                Snackbar.make(buy_layout, "Request could not be placed", Snackbar.LENGTH_LONG).show();
                pDialog.dismiss();
            }
        }) {
            //adding parameters to the request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("material_id", "1");
                params.put("quality_id", "1");
                params.put("quantity", "12");
                params.put("size_id", "1");
                params.put("location", "Nairobi");
                //params.put("code", "blst786");
                //  params.put("")
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                String bearer = "Bearer ".concat(token);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;


            // case blocks for other MenuItems (if any)
        }
        return false;
    }

}
