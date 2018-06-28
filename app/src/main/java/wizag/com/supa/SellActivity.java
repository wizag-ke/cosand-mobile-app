package wizag.com.supa;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.http.RequestQueue;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;
import wizag.com.supa.ModelResults.MaterialResults;
import wizag.com.supa.TopModels.TopMaterials;

public class SellActivity extends AppCompatActivity {
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
    String id_material;
    String id_quality;

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


        proceed_sell.setOnClickListener(new View.OnClickListener() {
            String quantity_txt = quantity.getText().toString();

            @Override
            public void onClick(View view) {
                //post material to db
                // Instantiate the RequestQueue.
                com.android.volley.RequestQueue queue = Volley.newRequestQueue(SellActivity.this);


                // Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.POST, POST_MATERIAL,
                        new com.android.volley.Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    JSONObject data = jsonObject.getJSONObject("data");
                                    String success_message = data.getString("message");
                                    // Snackbar.make(sell_layout, "New Request Created Successfully" , Snackbar.LENGTH_LONG).show();
                                    Toast.makeText(getApplicationContext(), "New Request Created Successfully", Toast.LENGTH_SHORT).show();

                                    startActivity(new Intent(getApplicationContext(), MenuActivity.class));

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                //Toast.makeText(Activity_Buy.this, "", Toast.LENGTH_SHORT).show();
                            }
                        }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                        Snackbar.make(sell_layout, "An error occurred", Snackbar.LENGTH_LONG).show();
                    }
                }) {
                    //adding parameters to the request
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("material_id", id_material);
                        params.put("quality_id", id_quality);
                        params.put("quantity", quantity.getText().toString());
                        params.put("supplier", spinner_sell_supplier.getSelectedItem().toString());
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


        });


    }

    private void loadSpinnerData(String url) {
        final List<String> materialsList = new ArrayList<>();
        final List<String> materials = new ArrayList<>();
        com.android.volley.RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
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


                                    if (supplier != null) {
                                        if (SupplierName.contains(supplier.getJSONObject(j).getString("name"))) {


                                        } else {


                                            SupplierName.add(supplier.getJSONObject(j).getString("name"));
                                            //  Toast.makeText(getApplicationContext(), "data\n" + supplier.getJSONObject(j).getString("name"), Toast.LENGTH_SHORT).show();


                                        }


                                    }
                                    //get
                                    spinner_sell_supplier.setAdapter(new ArrayAdapter<String>(SellActivity.this, android.R.layout.simple_spinner_dropdown_item, SupplierName));


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


                                            if (size != null) {

                                                if (SizeName.contains(size.getJSONObject(m).getString("size"))) {


                                                } else {

                                                    SizeName.add(size.getJSONObject(m).getString("size"));
                                                    //Toast.makeText(getApplicationContext(), "data\n" + size.getJSONObject(m).getString("size"), Toast.LENGTH_SHORT).show();


                                                }

                                            }
                                            spinner_sell_size.setAdapter(new ArrayAdapter<String>(SellActivity.this, android.R.layout.simple_spinner_dropdown_item, SizeName));

                                        }


                                        if (quality != null) {

                                            if (QualityName.contains(quality.getJSONObject(l).getString("value"))) {


                                            } else {

                                                QualityName.add(quality.getJSONObject(l).getString("value"));
                                                // Toast.makeText(getApplicationContext(), "data\n" + quality.getJSONObject(l).getString("value"), Toast.LENGTH_SHORT).show();


                                            }

                                        }
                                        spinner_sell_quality.setAdapter(new ArrayAdapter<String>(SellActivity.this, android.R.layout.simple_spinner_dropdown_item, QualityName));

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
                                        Toast.makeText(SellActivity.this, ""+id, Toast.LENGTH_SHORT).show();
*/

                                        SellName.add(materials.getJSONObject(i).getString("name"));
                                        SellId.add(materials.getJSONObject(i).getString("id"));


                                    }


                                }

                            }
                        }


                    }

                    spinner_sell_material.setAdapter(new ArrayAdapter<String>(SellActivity.this, android.R.layout.simple_spinner_dropdown_item, SellName));


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
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



