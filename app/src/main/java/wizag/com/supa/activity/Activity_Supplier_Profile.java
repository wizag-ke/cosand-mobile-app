package wizag.com.supa.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import wizag.com.supa.adapter.Adapter_Supplier_Items;
import wizag.com.supa.adapter.Adapter_Supplier_Items;
import wizag.com.supa.models.Model_Supplier_Profile;
import wizag.com.supa.models.Model_Trucks;

public class Activity_Supplier_Profile extends AppCompatActivity implements View.OnClickListener {
    JSONArray roles;
    ViewFlipper flipper;
    EditText fname, lname, id_no, email, mobile_no;
    Button next, next_supplier_company, previous, previous_materials;
    JSONObject details;
    EditText location, kra_pin, name;
    RecyclerView recyclerView;
    String name_txt, kra_pin_txt, location_txt;
    Adapter_Supplier_Items adapter;
    List<Model_Supplier_Profile> materials_list = new ArrayList<>();
    JSONObject role;
    String item, detail, class_txt, unit_txt, unit_price;
    Activity_Login login;
    SharedPreferences sp;
    Context context;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier_profile);
        context = this;
        sp = getSharedPreferences("profile", MODE_PRIVATE);
        String supplier_fname = sp.getString("supplier_fname", null);
        String driver_code = sp.getString("user_type", null);
        String supplier_lname = sp.getString("supplier_lname", null);
        String supplier_email = sp.getString("supplier_email", null);
        String supplier_phone = sp.getString("supplier_phone", null);
        String supplier_id_no = sp.getString("supplier_id_no", null);


        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter_Supplier_Items((ArrayList<Model_Supplier_Profile>) materials_list, this);
        recyclerView.setAdapter(adapter);


//        loadSupplierMaterials();

        flipper = findViewById(R.id.flipper);
        fname = findViewById(R.id.fname);
        lname = findViewById(R.id.lname);
        id_no = findViewById(R.id.id_no);
        email = findViewById(R.id.email);
        mobile_no = findViewById(R.id.mobile_no);
        location = findViewById(R.id.location);
        name = findViewById(R.id.name);
        kra_pin = findViewById(R.id.kra_pin);

        fname.setText(supplier_fname);
        lname.setText(supplier_lname);
        id_no.setText(supplier_id_no);
        email.setText(supplier_email);
        mobile_no.setText(supplier_phone);

        name.setText(name_txt);
        location.setText(location_txt);
        kra_pin.setText(kra_pin_txt);


        next = findViewById(R.id.next);
        next.setOnClickListener(this);
        next_supplier_company = findViewById(R.id.next_supplier_company);
        next_supplier_company.setOnClickListener(this);
        previous = findViewById(R.id.previous);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flipper.showPrevious();
            }
        });
        previous_materials = findViewById(R.id.previous_materials);
        previous_materials.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flipper.showPrevious();
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.next:
                flipper.showNext();
                break;

            case R.id.next_supplier_company:
                flipper.showNext();
                break;
        }

    }


    private void loadSupplierMaterials() {

        try {


            JSONArray trucks = details.getJSONArray("materials");
            if (trucks != null) {
                for (int k = 0; k < trucks.length(); k++) {
                    JSONObject trucks_object = trucks.getJSONObject(k);


                    item = trucks_object.getString("item");
                    detail = trucks_object.getString("detail");
                    class_txt = trucks_object.getString("class");
                    unit_txt = trucks_object.getString("unit");
                    unit_price = trucks_object.getString("unit_price");


                    Model_Supplier_Profile supplier_model = new Model_Supplier_Profile();
                    supplier_model.setMaterial_name(item);
                    supplier_model.setDetails_name(detail);
                    supplier_model.setClass_name(class_txt);
                    supplier_model.setUnits_name(unit_txt);
                    supplier_model.setCost(unit_price);

                    if (materials_list.contains(detail)) {

                    } else {
                        materials_list.add(supplier_model);
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
            String[] codes = {"Corporate Client", "Truck Owner", "Driver"};
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
                            }         break;

                        case 1:
                            try {
                                roles = new JSONArray(sp.getString("user_type", null));
                                for (int i = 0; i <= roles.length(); i++) {
                                    try {
                                        JSONObject role_object = roles.getJSONObject(i);
                                        if (role_object.getString("code").contains("XTON")) {
                                            getTruckOwner();
                                            finish();
                                            startActivity(new Intent(getApplicationContext(), Activity_Truck_Owner_Profile.class));

                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                                builder1.setMessage("Create Truck Owner account to continue");
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

                        case 3:
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
                            }  break;


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

    void getTruckOwner() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
//        pDialog.show();


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

                        editor.putString("truck_owner_fname", fname);
                        editor.putString("truck_owner_lname", lname);
                        editor.putString("truck_owner_email", email);
                        editor.putString("truck_owner_phone", phone);
                        editor.putString("user_type", role.toString());
                        editor.putString("truck_owner_id_no", id_no);
                        editor.apply();


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
//                    Toast.makeText(Activity_Login.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(Activity_Supplier_Profile.this, e.getMessage(), Toast.LENGTH_LONG).show();
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


}
