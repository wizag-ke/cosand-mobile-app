package wizag.com.supa.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Rect;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
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
import wizag.com.supa.adapter.Adapter_Transaction;
import wizag.com.supa.adapter.Adapter_Truck_Owner;
import wizag.com.supa.adapter.Adapter_Trucks;
import wizag.com.supa.models.Model_Transaction;
import wizag.com.supa.models.Model_Truck_Owner;
import wizag.com.supa.models.Model_Trucks;
import wizag.com.supa.models.Trucks;

public class Activity_Truck_Owner_Profile extends AppCompatActivity implements View.OnClickListener {
    ViewFlipper flipper;
    EditText fname, lname, id_no, email, mobile_no;
    Button next;
    EditText sacco_name, sacco_number, kra_pin;
    Button previous, previous_list, next_trucks;
    SessionManager session;
    ListView listView;
    RecyclerView recyclerView;
    List<Trucks> trucksList;
    Adapter_Trucks adapter_trucks;
    JSONArray roles;
    String truck_kra_pin, truck_sacco, truck_sacco_member;
    String plate_no, axle_count, make, model, tonnage;
    Adapter_Trucks adapter;
    List<Model_Trucks> trucks_add_list = new ArrayList<>();
    Button previous_trucks;
    String LoadTrucks = "http://sduka.wizag.biz/api/v1/profiles";
    JSONObject details;
    Activity_Truck_Owner_Profile login;
    SharedPreferences sp;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_truck_owner_user_profile);
        context = this;
        login = new Activity_Truck_Owner_Profile();
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter_Trucks((ArrayList<Model_Trucks>) trucks_add_list, this);
        recyclerView.setAdapter(adapter);

        previous_trucks = findViewById(R.id.previous_trucks);
        previous_trucks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flipper.showPrevious();
            }
        });

        sp = getSharedPreferences("profile", MODE_PRIVATE);
        String truck_owner_fname = sp.getString("truck_owner_fname", null);
        String driver_code = sp.getString("user_type", null);
        String truck_owner_lname = sp.getString("truck_owner_lname", null);
        String truck_owner_email = sp.getString("truck_owner_email", null);
        String truck_owner_phone = sp.getString("truck_owner_phone", null);
        String truck_owner_id_no = sp.getString("truck_owner_id_no", null);


//            loadTrucks();

//        loadTrucks();

        flipper = findViewById(R.id.flipper);
        fname = findViewById(R.id.fname);
        lname = findViewById(R.id.lname);
        id_no = findViewById(R.id.id_no);
        email = findViewById(R.id.email);
        mobile_no = findViewById(R.id.mobile_no);
        sacco_name = findViewById(R.id.sacco_name);
        sacco_number = findViewById(R.id.sacco_number);
        kra_pin = findViewById(R.id.kra_pin);

        next = findViewById(R.id.next);
//        previous_list = findViewById(R.id.previous_list);
        previous = findViewById(R.id.previous);
        next_trucks = findViewById(R.id.next_trucks);
        next.setOnClickListener(this);
        next_trucks.setOnClickListener(this);

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flipper.showPrevious();
            }
        });


        fname.setText(truck_owner_fname);
        lname.setText(truck_owner_lname);
        email.setText(truck_owner_email);
        mobile_no.setText(truck_owner_phone);
        id_no.setText(truck_owner_id_no);
        sacco_number.setText(truck_sacco_member);
        sacco_name.setText(truck_sacco);
        kra_pin.setText(truck_kra_pin);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.next:
                flipper.showNext();
                break;

            case R.id.next_trucks:
                flipper.showNext();
                break;
        }
    }

    private void loadTrucks() {

        try {


            JSONArray trucks = details.getJSONArray("trucks");
            if (trucks != null) {
                for (int k = 0; k < trucks.length(); k++) {
                    JSONObject trucks_object = trucks.getJSONObject(k);


                    model = trucks_object.getString("model");
                    make = trucks_object.getString("make");
                    plate_no = trucks_object.getString("plate_no");
                    axle_count = trucks_object.getString("axle_count");

                    JSONObject tonnage_obj = trucks_object.getJSONObject("tonnage");
                    tonnage = tonnage_obj.getString("description");

                    Model_Trucks trucks_model = new Model_Trucks();
                    trucks_model.setModel(model);
                    trucks_model.setMake(make);
                    trucks_model.setAxle_count(axle_count);
                    trucks_model.setPlate_no(plate_no);
                    trucks_model.setTonnage_id(tonnage);

                    if (trucks_add_list.contains(plate_no)) {

                    } else {
                        trucks_add_list.add(trucks_model);
                    }


                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.roles_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.switch_role) {

            // setup the alert builder
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Choose an Account");

// add a list
            String[] codes = {"Corporate Client", "Driver", "Supplier"};
            builder.setItems(codes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0:

                            try {
                                roles = new JSONArray(sp.getString("user_type", null));
                                for (int i = 0; i <= roles.length(); i++) {
                                    try {
                                        JSONObject role_object = roles.getJSONObject(i);
                                        if (role_object.getString("code").contains("XCOR")) {
                                            getCorporateProfile();
                                            finish();
                                            startActivity(new Intent(getApplicationContext(), Activity_Corporate_Profile.class));

                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                                builder1.setMessage("Create Corporate Client account to continue");
                                builder1.setCancelable(false);

                                builder1.setPositiveButton(
                                        "Proceed",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                startActivity(new Intent(getApplicationContext(), Activity_Register_Dashboard.class));
                                                finish();
                                            }
                                        });

                                builder1.setNegativeButton(
                                        "Not now",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                                startActivity(new Intent(getApplicationContext(), Activity_Home.class));
                                                finish();

                                            }
                                        });

                                AlertDialog alert11 = builder1.create();
                                alert11.show();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            break;

                        case 1:
                            try {
                                roles = new JSONArray(sp.getString("user_type", null));
                                for (int i = 0; i <= roles.length(); i++) {
                                    try {
                                        JSONObject role_object = roles.getJSONObject(i);
                                        if (role_object.getString("code").contains("XDRI")) {
                                            getDriverProfile();
                                            finish();
                                            startActivity(new Intent(getApplicationContext(), Activity_Driver_Profile.class));

                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                                builder1.setMessage("Create Driver account to continue");
                                builder1.setCancelable(false);

                                builder1.setPositiveButton(
                                        "Proceed",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                startActivity(new Intent(getApplicationContext(), Activity_Register_Dashboard.class));
                                                finish();
                                            }
                                        });

                                builder1.setNegativeButton(
                                        "Not now",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                                startActivity(new Intent(getApplicationContext(), Activity_Home.class));
                                                finish();

                                            }
                                        });

                                AlertDialog alert11 = builder1.create();
                                alert11.show();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            break;

                        case 2:
                            try {
                                roles = new JSONArray(sp.getString("user_type", null));
                                for (int i = 0; i <= roles.length(); i++) {
                                    try {
                                        JSONObject role_object = roles.getJSONObject(i);
                                        if (role_object.getString("code").contains("XSUP")) {
                                            getDriverProfile();
                                            finish();
                                            startActivity(new Intent(getApplicationContext(), Activity_Supplier_Profile.class));

                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                                builder1.setMessage("Create Supplier account to continue");
                                builder1.setCancelable(false);

                                builder1.setPositiveButton(
                                        "Proceed",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                startActivity(new Intent(getApplicationContext(), Activity_Register_Dashboard.class));
                                                finish();
                                            }
                                        });

                                builder1.setNegativeButton(
                                        "Not now",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                                startActivity(new Intent(getApplicationContext(), Activity_Home.class));
                                                finish();

                                            }
                                        });

                                AlertDialog alert11 = builder1.create();
                                alert11.show();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            break;

                        default:
                            Toast.makeText(getApplicationContext(), "No roles defined", Toast.LENGTH_SHORT).show();


                    }

                }


            });

            // create and show the alert dialog
            AlertDialog dialog_opt = builder.create();
            dialog_opt.show();
        } else if (id == R.id.add_role) {
            /*open the registration dashboard*/
            startActivity(new Intent(getApplicationContext(), Activity_Register_Dashboard.class));

        }


        return super.onOptionsItemSelected(item);
    }

    void getIndividualProfile() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();


        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://sduka.wizag.biz/api/v1/profiles", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
////                    pDialog.dismiss();
                    if (jsonObject != null) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        JSONObject user = data.getJSONObject("user");

                        String fname = user.getString("fname");
                        String lname = user.getString("lname");
                        String email = user.getString("email");
                        String phone = user.getString("phone");
                        String id_no = user.getString("id_no");

                        JSONArray roles = user.getJSONArray("roles");
                        SharedPreferences sp = getSharedPreferences("profile", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("individual_fname", fname);
                        editor.putString("individual_lname", lname);
                        editor.putString("individual_email", email);
                        editor.putString("individual_phone", phone);
                        editor.putString("individual_id_no", id_no);
                        editor.putString("user_type", roles.toString());
                        editor.apply();


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Activity_Truck_Owner_Profile.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
//                pDialog.dismiss();
                Toast.makeText(getApplicationContext(), "An Error Occurred while loading Individual client profile" + error.getMessage(), Toast.LENGTH_LONG).show();

            }


        }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                session = new SessionManager(getApplicationContext());
                HashMap<String, String> user = session.getUserDetails();
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


    public void getDriverProfile() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();


        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://sduka.wizag.biz/api/v1/profiles", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
////                    pDialog.dismiss();
                    if (jsonObject != null) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        JSONObject user = data.getJSONObject("user");
                        String fname = user.getString("fname");
                        String lname = user.getString("lname");
                        String email = user.getString("email");
                        String phone = user.getString("phone");
                        String id_no = user.getString("id_no");


                        JSONArray roles = user.getJSONArray("roles");
                        SharedPreferences sp = getSharedPreferences("profile", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();

                        editor.putString("driver_fname", fname);
                        editor.putString("driver_lname", lname);
                        editor.putString("driver_email", email);
                        editor.putString("driver_phone", phone);
                        editor.putString("driver_id_no", id_no);
                        editor.putString("user_type", roles.toString());
                        editor.apply();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
//                    Toast.makeText(Activity_Truck_Owner_Profile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
////               pDialog.dismiss();
//                Toast.makeText(getApplicationContext(), "An Error Occurred while loading Driver profile" + error.getMessage(), Toast.LENGTH_LONG).show();

            }


        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                SessionManager session = new SessionManager(getApplicationContext());
                HashMap<String, String> user = session.getUserDetails();
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

    public void getCorporateProfile() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();


        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://sduka.wizag.biz/api/v1/profiles", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
//                    pDialog.dismiss();
                    if (jsonObject != null) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        JSONObject user = data.getJSONObject("user");

                        String fname = user.getString("fname");
                        String lname = user.getString("lname");
                        String email = user.getString("email");
                        String phone = user.getString("phone");
                        String id_no = user.getString("id_no");

                        JSONArray roles = user.getJSONArray("roles");
                        SharedPreferences sp = getSharedPreferences("profile", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("corporate_fname", fname);
                        editor.putString("corporate_lname", lname);
                        editor.putString("corporate_email", email);
                        editor.putString("corporate_phone", phone);
                        editor.putString("corporate_id_no", id_no);
                        editor.putString("user_type", roles.toString());
                        editor.apply();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
//                    Toast.makeText(Activity_Truck_Owner_Profile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
////               pDialog.dismiss();
//                Toast.makeText(getApplicationContext(), "An Error Occurred while loading Driver profile" + error.getMessage(), Toast.LENGTH_LONG).show();

            }


        }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                SessionManager session = new SessionManager(getApplicationContext());
                HashMap<String, String> user = session.getUserDetails();
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


    public void getSupplierProfile() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();


        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://sduka.wizag.biz/api/v1/profiles", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
//                    pDialog.dismiss();
                    if (jsonObject != null) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        JSONObject user = data.getJSONObject("user");

                        String fname = user.getString("fname");
                        String lname = user.getString("lname");
                        String email = user.getString("email");
                        String phone = user.getString("phone");
                        String id_no = user.getString("id_no");

                        JSONArray role = user.getJSONArray("roles");
                        SharedPreferences sp = getSharedPreferences("profile", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();

                        editor.putString("supplier_fname", fname);
                        editor.putString("supplier_lname", lname);
                        editor.putString("supplier_email", email);
                        editor.putString("supplier_phone", phone);
                        editor.putString("supplier_id_no", id_no);
                        editor.putString("user_type", role.toString());
                        editor.apply();


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
//                    Toast.makeText(Activity_Truck_Owner_Profile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
////               pDialog.dismiss();
//                Toast.makeText(getApplicationContext(), "An Error Occurred while loading Driver profile" + error.getMessage(), Toast.LENGTH_LONG).show();

            }


        }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                SessionManager session = new SessionManager(getApplicationContext());
                HashMap<String, String> user = session.getUserDetails();
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
/*

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        builder.setTitle("info");
        builder.setMessage("Do you want to exit from the App?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();

    }
*/

}


