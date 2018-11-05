package wizag.com.supa.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import wizag.com.supa.GPSLocation;
import wizag.com.supa.MySingleton;
import wizag.com.supa.R;
import wizag.com.supa.SessionManager;

public class Activity_Sell extends AppCompatActivity {
    TextView time, date;
    Spinner spinner_sell_material, spinner_sell_units, spinner_sell_details, spinner_sell_supplier, spinner_sell_class;
    EditText linear_supplier;
    LinearLayout sell_layout;
    //MaterialService materialService;
    Button proceed_sell;
    private static final String SHARED_PREF_NAME = "mysharedpref";
    Spinner material_type;

    String POST_MATERIAL = "http://sduka.wizag.biz/api/v1/orders/load-request";

    ArrayList<String> SellName;
    ArrayList<String> Supplier;
    ArrayList<String> CategoryName;
    ArrayList<String> DetailsName;
    ArrayList<String> UnitsName;
    ArrayList<String> ClassName;
    ArrayList<String> SizeName;
    ArrayList<String> Type;
    ArrayList<String> Material;
    ArrayList<String> PriceName;
    ArrayList<String> SellId;
    JSONArray materialTypes, materials, details_array, class_array, units_array, suppliers_array;
    SessionManager sessionManager;
    String token;
    Button proceed;
    GPSLocation gps;
    EditText quantity;
    String location;
    String quantity_txt;

    HashMap<String, String> map_values;
    HashMap<String, String> details_values;
    HashMap<String, String> supplier_values;
    HashMap<String, String> size_values;
    Map<String, String> Service_values;
    Map<String, String> Material_values;


    int id_material;
    int id_class;
    int id_unit;
    int id_detail;
    int id_supplier;
    int id_service;
    AlertDialog alertDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sell);

//        LoadMaterialTypeSpinner();
        getServiceType();


        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);


        spinner_sell_material = findViewById(R.id.spinner_sell_material);
        spinner_sell_supplier = findViewById(R.id.spinner_sell_supplier);
        spinner_sell_details = findViewById(R.id.spinner_sell_details);
        spinner_sell_class = findViewById(R.id.spinner_sell_class);
        spinner_sell_units = findViewById(R.id.spinner_sell_units);
        sell_layout = (LinearLayout) findViewById(R.id.sell_layout);
        proceed_sell = (Button) findViewById(R.id.proceed_sell);
        quantity = (EditText) findViewById(R.id.quantity_text);

        map_values = new HashMap<String, String>();
        details_values = new HashMap<String, String>();
        size_values = new HashMap<String, String>();
        supplier_values = new HashMap<String, String>();
        Service_values = new HashMap<String, String>();
        Material_values = new HashMap<String, String>();


        SellName = new ArrayList<>();
        Supplier = new ArrayList<>();
        CategoryName = new ArrayList<>();
        DetailsName = new ArrayList<>();
        UnitsName = new ArrayList<>();
        ClassName = new ArrayList<>();
        SizeName = new ArrayList<>();
        Type = new ArrayList<>();
        Material = new ArrayList<>();
        PriceName = new ArrayList<>();
        SellId = new ArrayList<>();


        sessionManager = new SessionManager(getApplicationContext());
        HashMap<String, String> user = sessionManager.getUserDetails();
        token = user.get("access_token");
//        Toast.makeText(getApplicationContext(), ""+token, Toast.LENGTH_SHORT).show();

        gps = new GPSLocation(this);
        if (gps.canGetLocation()) {
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            location = latitude + "," + longitude;
            // Toast.makeText(getApplicationContext(), "" + location, Toast.LENGTH_SHORT).show();

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


        spinner_sell_material.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String value = spinner_sell_material.getSelectedItem().toString();
//                Toast.makeText(getApplicationContext(), ""+value, Toast.LENGTH_SHORT).show();
                try {
                    JSONObject dataClicked = materials.getJSONObject(i);
                    id_material = dataClicked.getInt("id");
//                    getMaterialDetails();
                    Toast.makeText(getApplicationContext(), "" + id_material, Toast.LENGTH_SHORT).show();

//                    Material.clear();

                    getMaterialDetails();
                    DetailsName.clear();
                    getMaterialClasses();
                    ClassName.clear();
                    getMaterialUnits();
                    UnitsName.clear();
                    getSupplier();
                    Supplier.clear();

                } catch (Exception e) {
                    e.printStackTrace();
//                    Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner_sell_details.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String value = spinner_sell_details.getSelectedItem().toString();

                try {
                    JSONObject dataClicked = details_array.getJSONObject(i);
                    id_detail = dataClicked.getInt("id");

//                    getMaterialClasses();

                } catch (Exception e) {
                    e.printStackTrace();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        spinner_sell_class.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String value = spinner_sell_class.getSelectedItem().toString();

                try {
                    JSONObject dataClicked = class_array.getJSONObject(i);
                    id_class = dataClicked.getInt("id");
//                    getMaterialUnits();
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner_sell_units.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String value = spinner_sell_units.getSelectedItem().toString();

                try {
                    JSONObject dataClicked = units_array.getJSONObject(i);
                    id_unit = dataClicked.getInt("id");


                } catch (Exception e) {
                    e.printStackTrace();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        spinner_sell_supplier.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String value = spinner_sell_supplier.getSelectedItem().toString();

                try {
                    JSONObject dataClicked = suppliers_array.getJSONObject(i);
                    id_supplier = dataClicked.getInt("id");

//                    getMaterialClasses();

                } catch (Exception e) {
                    e.printStackTrace();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        proceed_sell.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                quantity_txt = quantity.getText().toString();
                if (quantity_txt.isEmpty()) {
                    Snackbar snackbar = Snackbar.make(sell_layout, "Enter Quantity to continue", Snackbar.LENGTH_LONG);
                    View snackbar_view = snackbar.getView();
                    snackbar_view.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
                    snackbar.show();

                } else if (Integer.parseInt(quantity_txt) < 12) {
                    Snackbar snackbar = Snackbar.make(sell_layout, "Quantity value should be more than 12", Snackbar.LENGTH_LONG);
                    View snackbar_view = snackbar.getView();
                    snackbar_view.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
                    snackbar.show();
                } else if ((Integer.parseInt(quantity_txt)) % 2 != 0) {
                    Snackbar snackbar = Snackbar.make(sell_layout, "Quantity value should be an even number", Snackbar.LENGTH_LONG);
                    View snackbar_view = snackbar.getView();
                    snackbar_view.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
                    snackbar.show();
                }
                
                else {
                    loadRequest();
                }


            }


        });


    }

    /*get supplier*/
    private void getSupplier() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();


        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://sduka.wizag.biz/api/v1/materials/" + id_material + "/suppliers", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    pDialog.dismiss();
                    if (jsonObject != null) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        suppliers_array = data.getJSONArray("suppliers");

                        for (int z = 0; z < suppliers_array.length(); z++) {
                            JSONObject suppliers_object = suppliers_array.getJSONObject(z);

                            String material_id = suppliers_object.getString("id");
                            String name = suppliers_object.getString("name");


                            if (suppliers_array != null) {

                                if (Supplier.contains(name)) {


                                } else {

                                    //Toast.makeText(getApplicationContext(), "data\n" + size.getJSONObject(m).getString("size"), Toast.LENGTH_SHORT).show();
                                    Supplier.add(name);
//                                    Type.add(materialTypes.getJSONObject(p).getString("id"));

                                }

                            }


                        }


                    }
                    spinner_sell_supplier.setAdapter(new ArrayAdapter<String>(Activity_Sell.this, android.R.layout.simple_spinner_dropdown_item, Supplier));


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


        }) {


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


        MySingleton.getInstance(this).addToRequestQueue(stringRequest);


        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);


    }


    /*get material units*/
    private void getMaterialUnits() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();


        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://sduka.wizag.biz/api/v1/materials/" + id_material, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    pDialog.dismiss();
                    if (jsonObject != null) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        JSONObject units_obj = data.getJSONObject("materialItem");
                        units_array = units_obj.getJSONArray("materialUnits");


                        for (int q = 0; q < units_array.length(); q++) {
                            JSONObject materials_object = units_array.getJSONObject(q);
                            String detail_id = materials_object.getString("id");
                            String name = materials_object.getString("name");

                            if (units_array != null) {

                                if (UnitsName.contains(name)) {


                                } else {

                                    //Toast.makeText(getApplicationContext(), "data\n" + size.getJSONObject(m).getString("size"), Toast.LENGTH_SHORT).show();
                                    UnitsName.add(name);
//                                    Type.add(materialTypes.getJSONObject(p).getString("id"));

                                }

                            }


                        }


                    }
                    spinner_sell_units.setAdapter(new ArrayAdapter<String>(Activity_Sell.this, android.R.layout.simple_spinner_dropdown_item, UnitsName));


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


        }) {


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


        MySingleton.getInstance(this).addToRequestQueue(stringRequest);


        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);


    }


    /*get material classes*/
    private void getMaterialClasses() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();


        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://sduka.wizag.biz/api/v1/materials/" + id_material, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    pDialog.dismiss();
                    if (jsonObject != null) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        JSONObject classes_obj = data.getJSONObject("materialItem");
                        class_array = classes_obj.getJSONArray("materialClasses");


                        for (int m = 0; m < class_array.length(); m++) {
                            JSONObject materials_object = class_array.getJSONObject(m);
                            String classes_id = materials_object.getString("id");
                            String name = materials_object.getString("name");


                            if (class_array != null) {

                                if (ClassName.contains(name)) {


                                } else {

                                    //Toast.makeText(getApplicationContext(), "data\n" + size.getJSONObject(m).getString("size"), Toast.LENGTH_SHORT).show();
                                    ClassName.add(name);
//                                    Type.add(materialTypes.getJSONObject(p).getString("id"));

                                }

                            }


                        }


                    }
                    spinner_sell_class.setAdapter(new ArrayAdapter<String>(Activity_Sell.this, android.R.layout.simple_spinner_dropdown_item, ClassName));


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


        }) {


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


        MySingleton.getInstance(this).addToRequestQueue(stringRequest);


        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);


    }


    /*get material details*/
    private void getMaterialDetails() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();


        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://sduka.wizag.biz/api/v1/materials/" + id_material, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    pDialog.dismiss();
                    if (jsonObject != null) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        JSONObject details_obj = data.getJSONObject("materialItem");
                        details_array = details_obj.getJSONArray("materialDetails");


                        for (int p = 0; p < details_array.length(); p++) {
                            JSONObject materials_object = details_array.getJSONObject(p);
                            String detail_id = materials_object.getString("id");
                            String name = materials_object.getString("name");
                            details_values.put(detail_id, name);

                            if (details_array != null) {

                                if (DetailsName.contains(name)) {


                                } else {

                                    //Toast.makeText(getApplicationContext(), "data\n" + size.getJSONObject(m).getString("size"), Toast.LENGTH_SHORT).show();
                                    DetailsName.add(name);
//                                    Type.add(materialTypes.getJSONObject(p).getString("id"));

                                }

                            }


                        }


                    }
                    spinner_sell_details.setAdapter(new ArrayAdapter<String>(Activity_Sell.this, android.R.layout.simple_spinner_dropdown_item, DetailsName));


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


        }) {


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


        MySingleton.getInstance(this).addToRequestQueue(stringRequest);


        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);


    }


    private void loadRequest() {
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(Activity_Sell.this);
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, POST_MATERIAL,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            pDialog.dismiss();
                            String message = jsonObject.getString("message");
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), Activity_Home.class));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //Toast.makeText(Activity_Buy.this, "", Toast.LENGTH_SHORT).show();
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                error.getMessage();
                Snackbar.make(sell_layout, "Request could not be placed" + error.getMessage(), Snackbar.LENGTH_LONG).show();
                pDialog.dismiss();
            }
        }) {
            //adding parameters to the request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("material_item", String.valueOf(id_material));
                params.put("material_detail", String.valueOf(id_detail));
                params.put("material_quantity", quantity_txt);
                params.put("material_class", String.valueOf(id_class));
                params.put("material_unit", String.valueOf(id_unit));
                params.put("supplier_id", String.valueOf(id_supplier));
                params.put("driver_location", location);
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


    /*Selecting Material type*/
    private void getMaterial() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();


        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://sduka.wizag.biz/api/v1/materials/types/" + id_service + "/items", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    pDialog.dismiss();
                    if (jsonObject != null) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        materials = data.getJSONArray("materials");

                        for (int n = 0; n < materials.length(); n++) {
                            JSONObject materials_object = materials.getJSONObject(n);

                            String material_id = materials_object.getString("id");
                            String name = materials_object.getString("name");


                            if (materials != null) {

                                if (Material.contains(name)) {


                                } else {

                                    //Toast.makeText(getApplicationContext(), "data\n" + size.getJSONObject(m).getString("size"), Toast.LENGTH_SHORT).show();
                                    Material.add(name);
//                                    Type.add(materialTypes.getJSONObject(p).getString("id"));

                                }

                            }

                        }


                    }
                    spinner_sell_material.setAdapter(new ArrayAdapter<String>(Activity_Sell.this, android.R.layout.simple_spinner_dropdown_item, Material));


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


        }) {


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


        MySingleton.getInstance(this).addToRequestQueue(stringRequest);


        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);


    }


    /*Service selection*/
    private void getServiceType() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(Activity_Sell.this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.activity_material_types, null);
        material_type = dialogView.findViewById(R.id.spinner_service_type);

        material_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String value = material_type.getSelectedItem().toString();
//                Toast.makeText(getApplicationContext(), ""+value, Toast.LENGTH_SHORT).show();
                try {
                    JSONObject dataClicked = materialTypes.getJSONObject(i);
                    id_service = dataClicked.getInt("id");

                } catch (Exception e) {
                    e.printStackTrace();
//                    Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        final Button cancel = dialogView.findViewById(R.id.cancel);
        final Button proceed = dialogView.findViewById(R.id.proceed);


        builder.setView(dialogView);
        builder.setCancelable(false);
        LoadMaterialTypeSpinner();

        proceed.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
//               get id and proceed to sell activity

                if (material_type.getSelectedItem().equals("--Select Material Type--")) {
                    Toast.makeText(Activity_Sell.this, "Select Material Type to continue", Toast.LENGTH_SHORT).show();
                } else {

//                   LoadMaterialTypeSpinner();
                    alertDialog.dismiss();
                    getMaterial();
                   /* getMaterialClasses();
                    getMaterialDetails();
                    getMaterialUnits();*/

                }
            }

        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                finish();
                alertDialog.dismiss();
            }
        });
        alertDialog = builder.create();

        alertDialog.show();
    }

    private void LoadMaterialTypeSpinner() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://sduka.wizag.biz/api/v1/materials/types", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    pDialog.dismiss();
                    if (jsonObject != null) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        materialTypes = data.getJSONArray("materialTypes");

                        for (int p = 0; p < materialTypes.length(); p++) {
                            JSONObject materials_object = materialTypes.getJSONObject(p);

                            String type_id = materials_object.getString("id");
                            String name = materials_object.getString("name");
                            Service_values.put(type_id, name);

                            if (materialTypes != null) {

                                if (Type.contains(name)) {


                                } else {

                                    //Toast.makeText(getApplicationContext(), "data\n" + size.getJSONObject(m).getString("size"), Toast.LENGTH_SHORT).show();
                                    Type.add(name);
//                                    Type.add(materialTypes.getJSONObject(p).getString("id"));

                                }

                            }

                        }


                    }
                    material_type.setAdapter(new ArrayAdapter<String>(Activity_Sell.this, android.R.layout.simple_spinner_dropdown_item, Type));


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


        }) {


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


        MySingleton.getInstance(this).addToRequestQueue(stringRequest);


        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);


    }


}



