package wizag.com.supa.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

public class Activity_Truck_Owner extends AppCompatActivity {
    EditText kra_pin, sacco_name, sacco_number;
    String register_truck_owner_url = "http://sduka.wizag.biz/api/v1/profiles/roles";
    RecyclerView recyclerView;
    Button submit;
    Adapter_Truck_Owner adapter;
    List<Model_Truck_Owner> trucks_list = new ArrayList<>();
    FloatingActionButton fab;
    JSONArray trucks, tonnage_array;
    String model_txt, make_txt, axle_count_txt, plate_no_txt, tonnage_id_txt;
    SessionManager sessionManager;
    int id_tonnage;
    ArrayList<String> Tonnage;
    String tonnages_url = "http://sduka.wizag.biz/api/v1/trucks/tonnages";
    String tonnage_name;
    Spinner tonnage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_truck_owner);

        Tonnage = new ArrayList<>();
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        kra_pin = findViewById(R.id.kra_pin);
        sacco_name = findViewById(R.id.sacco_name);
        sacco_number = findViewById(R.id.sacco_number);
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
                getTonnage();

            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                kra_pin = findViewById(R.id.kra_pin);
                sacco_name = findViewById(R.id.sacco_name);
                sacco_number = findViewById(R.id.sacco_number);

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

        TextView make;
        TextView model;
        TextView axle_count;
        TextView plate_no;


        make = dialogView.findViewById(R.id.dialog_make);
        model = dialogView.findViewById(R.id.dialog_model);
        axle_count = dialogView.findViewById(R.id.dialog_axle_count);
        plate_no = dialogView.findViewById(R.id.dialog_plate_no);
        tonnage = dialogView.findViewById(R.id.dialog_tonnage);


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


        dialogBuilder.setTitle("Truck Details");
        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                make_txt = make.getText().toString();
                model_txt = model.getText().toString();
                axle_count_txt = axle_count.getText().toString();
                plate_no_txt = plate_no.getText().toString();

//                Toast.makeText(Activity_Truck_Owner.this, plate_no_txt, Toast.LENGTH_SHORT).show();

                if (make_txt.isEmpty()) {
                    Toast.makeText(Activity_Truck_Owner.this, "Enter Truck make to continue", Toast.LENGTH_SHORT).show();
                } else if (model_txt.isEmpty()) {
                    Toast.makeText(Activity_Truck_Owner.this, "Enter Truck model to continue", Toast.LENGTH_SHORT).show();
                } else if (axle_count_txt.isEmpty()) {
                    Toast.makeText(Activity_Truck_Owner.this, "Enter Truck axle count to continue", Toast.LENGTH_SHORT).show();
                } else if (plate_no_txt.isEmpty()) {
                    Toast.makeText(Activity_Truck_Owner.this, "Enter Truck Plate Number to continue", Toast.LENGTH_SHORT).show();
                } else {

                    trucks_list.add(new Model_Truck_Owner(
                            model_txt,
                            make_txt,
                            axle_count_txt,
                            plate_no_txt,
                            tonnage_name,
                            id_tonnage));
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
                params.put("kra_pin", kra_pin.getText().toString());
                params.put("sacco", sacco_name.getText().toString());
                params.put("trucks", String.valueOf(trucks));
                params.put("sacco_member", sacco_number.getText().toString());
                params.put("role_id", "XTON");
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
                            tonnage_name = suppliers_object.getString("description");


                            if (tonnage_array != null) {

                                if (Tonnage.contains(tonnage_name)) {


                                } else {

                                    //Toast.makeText(getApplicationContext(), "data\n" + size.getJSONObject(m).getString("size"), Toast.LENGTH_SHORT).show();
                                    Tonnage.add(tonnage_name);
//                                    Type.add(materialTypes.getJSONObject(p).getString("id"));

                                }

                            }


                        }


                    }
                    tonnage.setAdapter(new ArrayAdapter<String>(Activity_Truck_Owner.this, android.R.layout.simple_spinner_dropdown_item, Tonnage));


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

}
