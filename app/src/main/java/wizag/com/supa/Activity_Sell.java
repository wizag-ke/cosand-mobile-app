package wizag.com.supa;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Activity_Sell extends AppCompatActivity {
    TextView time, date;
    Spinner spinner_sell_material, spinner_sell_quantity, spinner_sell_quality, spinner_sell_supplier, spinner_sell_size;
    EditText linear_supplier;
    LinearLayout sell_layout;
    //MaterialService materialService;
    Button proceed_sell;


    String GET_MATERIALS = "http://sduka.wizag.biz/api/material";
    String POST_MATERIAL = "http://sduka.wizag.biz/api/load-request";

    ArrayList<String> SellName;
    ArrayList<String> SupplierName;
    ArrayList<String> CategoryName;
    ArrayList<String> QualityName;
    ArrayList<String> SizeName;
    ArrayList<String> PriceName;
    ArrayList<String> SellId;
    SessionManager sessionManager;
    String token;
    Button proceed;
    GPSLocation gps;
    EditText quantity;
    String location;

    HashMap<String, String> map_values;
    HashMap<String, String> quality_values;
    HashMap<String, String> supplier_values;
    HashMap<String, String> size_values;


    String id_material;
    String id_quality;
    String id_size;
    String id_supplier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sell);


        spinner_sell_material = findViewById(R.id.spinner_sell_material);
        spinner_sell_supplier = findViewById(R.id.spinner_sell_supplier);
        spinner_sell_quality = findViewById(R.id.spinner_sell_quality);
        spinner_sell_size = findViewById(R.id.spinner_sell_size);
        sell_layout = (LinearLayout) findViewById(R.id.sell_layout);
        proceed_sell = (Button) findViewById(R.id.proceed_sell);
        quantity = (EditText) findViewById(R.id.quantity_text);

        map_values = new HashMap<String, String>();
        quality_values = new HashMap<String, String>();
        size_values = new HashMap<String, String>();
        supplier_values = new HashMap<String, String>();


        SellName = new ArrayList<>();
        SupplierName = new ArrayList<>();
        CategoryName = new ArrayList<>();
        QualityName = new ArrayList<>();
        SizeName = new ArrayList<>();
        PriceName = new ArrayList<>();
        SellId = new ArrayList<>();


        sessionManager = new SessionManager(getApplicationContext());
        HashMap<String, String> user = sessionManager.getUserDetails();
        token = user.get("access_token");
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

        loadSpinnerData(GET_MATERIALS);


        spinner_sell_material.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String value = spinner_sell_material.getSelectedItem().toString();
                id_material = map_values.get(value);

                // Toast.makeText(getApplicationContext(), "id"+id, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
            }
        });

        spinner_sell_quality.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String value = spinner_sell_quality.getSelectedItem().toString();
                id_quality = quality_values.get(value);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner_sell_size.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String value = spinner_sell_size.getSelectedItem().toString();
                id_size = size_values.get(value);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner_sell_supplier.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String value = spinner_sell_supplier.getSelectedItem().toString();
                id_supplier = supplier_values.get(value);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        proceed_sell.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String quantity_txt = quantity.getText().toString();
                if (quantity_txt.isEmpty()) {
                    Snackbar snackbar = Snackbar.make(sell_layout, "Enter Quantity to continue", Snackbar.LENGTH_LONG);
                    View snackbar_view = snackbar.getView();
                    snackbar_view.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
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
                } else {
                    loadRequest();
                }


            }


        });
    }


    private void loadRequest() {

        com.android.volley.RequestQueue queue = Volley.newRequestQueue(Activity_Sell.this);
        com.android.volley.RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
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
                            JSONObject data = jsonObject.getJSONObject("data");
                            String success_message = data.getString("message");
                            // Snackbar.make(sell_layout, "New Request Created Successfully" , Snackbar.LENGTH_LONG).show();
                            //Snackbar.make(sell_layout, "New request created successfully", Snackbar.LENGTH_LONG).show();

                            Toast.makeText(getApplicationContext(), "New request created successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MenuActivity.class));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //Toast.makeText(Activity_Buy.this, "", Toast.LENGTH_SHORT).show();
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                Snackbar.make(sell_layout, "Request could not be placed", Snackbar.LENGTH_LONG).show();
                pDialog.dismiss();
            }
        }) {
            //adding parameters to the request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("material_id", id_material);
                params.put("quality_id", id_quality);
                params.put("quantity", quantity.getText().toString());
                params.put("supplier_id", id_supplier);
                params.put("material_size_id", id_size);
                params.put("location", location);
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

    private void loadSpinnerData(String url) {
        final List<String> materialsList = new ArrayList<>();
        final List<String> materials = new ArrayList<>();
        com.android.volley.RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
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

                                JSONArray supplier = material_items.getJSONArray("supplier");


                                /*loop thro categories*/

                                for (int j = 0; j < supplier.length(); j++) {
                                    JSONObject supplier_items = supplier.getJSONObject(j);
                                    String supplier_name = supplier_items.getString("name");
                                    String supplier_id = supplier_items.getString("id");
                                    supplier_values.put(supplier_name, supplier_id);


                                    if (supplier != null) {
                                        if (SupplierName.contains(supplier.getJSONObject(j).getString("name"))) {


                                        } else {


                                            SupplierName.add(supplier.getJSONObject(j).getString("name"));
                                            //  Toast.makeText(getApplicationContext(), "data\n" + supplier.getJSONObject(j).getString("name"), Toast.LENGTH_SHORT).show();


                                        }


                                    }
                                    //get
                                    spinner_sell_supplier.setAdapter(new ArrayAdapter<String>(Activity_Sell.this, android.R.layout.simple_spinner_dropdown_item, SupplierName));


                                }

                                /*loop through categories*/

                                JSONArray category = material_items.getJSONArray("category");
                                for (int k = 0; k < category.length(); k++) {

                                    JSONObject category_items = category.getJSONObject(k);

                                    /*loop thro quality*/


                                    JSONArray quality = category_items.getJSONArray("quality");
                                    for (int l = 0; l < quality.length(); l++) {
                                        JSONObject quality_object = quality.getJSONObject(l);
                                        String quality_name = quality_object.getString("value");
                                        String quality_id = quality_object.getString("id");
                                        quality_values.put(quality_name, quality_id);
                                        /*loop thro size*/
                                        JSONObject size_items = quality.getJSONObject(k);
                                        JSONArray size = size_items.getJSONArray("size");

                                        for (int m = 0; m < size.length(); m++) {
                                            JSONObject size_objects = size.getJSONObject(m);
                                            String size_name = size_objects.getString("size");
                                            String size_id = size_objects.getString("id");
                                            size_values.put(size_name, size_id);
                                            // Toast.makeText(Activity_Sell.this, "" + size_name, Toast.LENGTH_SHORT).show();

                                            if (size != null) {

                                                if (SizeName.contains(size.getJSONObject(m).getString("size"))) {


                                                } else {

                                                    SizeName.add(size.getJSONObject(m).getString("size"));
                                                    //Toast.makeText(getApplicationContext(), "data\n" + size.getJSONObject(m).getString("size"), Toast.LENGTH_SHORT).show();


                                                }

                                            }
                                            spinner_sell_size.setAdapter(new ArrayAdapter<String>(Activity_Sell.this, android.R.layout.simple_spinner_dropdown_item, SizeName));

                                        }


                                        if (quality != null) {

                                            if (QualityName.contains(quality.getJSONObject(l).getString("value"))) {


                                            } else {

                                                QualityName.add(quality.getJSONObject(l).getString("value"));
                                                // Toast.makeText(getApplicationContext(), "data\n" + quality.getJSONObject(l).getString("value"), Toast.LENGTH_SHORT).show();


                                            }

                                        }
                                        spinner_sell_quality.setAdapter(new ArrayAdapter<String>(Activity_Sell.this, android.R.layout.simple_spinner_dropdown_item, QualityName));

                                    }


                                    if (category != null) {
                                        if (CategoryName.contains(category.getJSONObject(k).getString("name"))) {


                                        } else {

                                            CategoryName.add(category.getJSONObject(k).getString("name"));
                                            //Toast.makeText(getApplicationContext(), "category\n" + category.getJSONObject(k).getString("name"), Toast.LENGTH_SHORT).show();


                                        }


                                    }


                                }


                                //  Toast.makeText(getApplicationContext(), "data\n"+ category, Toast.LENGTH_SHORT).show();

                                //List<String> materialsList = new ArrayList<>();
                                if (material_items != null) {
                                    if (SellName.contains(materials.getJSONObject(i).getString("name"))) {

                                    } else {

                                       /* int id = materials.getJSONObject(i).getInt("id");
                                        Toast.makeText(Activity_Sell.this, ""+id, Toast.LENGTH_SHORT).show();
*/

                                        SellName.add(materials.getJSONObject(i).getString("name"));
                                        SellId.add(materials.getJSONObject(i).getString("id"));


                                    }


                                }

                            }
                        }


                    }

                    spinner_sell_material.setAdapter(new ArrayAdapter<String>(Activity_Sell.this, android.R.layout.simple_spinner_dropdown_item, SellName));


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(getApplicationContext(), "An error occured" + error, Toast.LENGTH_SHORT).show();
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
}



