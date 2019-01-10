package wizag.com.supa.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.util.HashMap;
import java.util.Map;

import wizag.com.supa.MySingleton;
import wizag.com.supa.R;
import wizag.com.supa.SessionManager;

public class Activity_Driver_Profile extends AppCompatActivity implements View.OnClickListener {
    EditText phone, fname, lname, id_no, email;
    ViewFlipper flipper;
    EditText plate_no, logbook, tonnage, make, model, year;
    Button next, previous, update;

    String driver_fname, driver_lname, driver_email, driver_phone, driver_id_no, driver_plate_no, driver_description, driver_log_book, driver_make, driver_model, driver_year;
    JSONArray roles;
    Activity_Login login;
    SharedPreferences sp;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_profile);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context = this;
        sp = getSharedPreferences("profile", MODE_PRIVATE);

        driver_fname = sp.getString("driver_fname", null);
        driver_lname = sp.getString("driver_lname", null);
        driver_email = sp.getString("driver_email", null);
        driver_phone = sp.getString("driver_phone", null);
        driver_id_no = sp.getString("driver_id_no", null);
        driver_plate_no = sp.getString("driver_plate_no", null);
        driver_description = sp.getString("driver_description", null);
        driver_log_book = sp.getString("driver_logbook", null);
        driver_make = sp.getString("driver_make", null);
        driver_model = sp.getString("driver_model", null);
        driver_year = sp.getString("driver_year", null);


        /*initialize views*/
        phone = findViewById(R.id.phone);
        fname = findViewById(R.id.fname);
        lname = findViewById(R.id.lname);
        id_no = findViewById(R.id.id_no);
        email = findViewById(R.id.email);
        plate_no = findViewById(R.id.plate_no);
        logbook = findViewById(R.id.logbook);
        tonnage = findViewById(R.id.tonnage);
        model = findViewById(R.id.model);
        make = findViewById(R.id.make);
        year = findViewById(R.id.year);
        next = findViewById(R.id.next);
        previous = findViewById(R.id.previous);
        update = findViewById(R.id.update);
        flipper = findViewById(R.id.flipper);

        next.setOnClickListener(this);
        update.setOnClickListener(this);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flipper.showPrevious();
            }
        });

        phone.setText(driver_phone);
        fname.setText(driver_fname);
        lname.setText(driver_lname);
        id_no.setText(driver_id_no);
        email.setText(driver_email);

        plate_no.setText(driver_plate_no);
        logbook.setText(driver_log_book);
        tonnage.setText(driver_description);
        model.setText(driver_model);
        make.setText(driver_make);
        year.setText(driver_year);

        flipper = findViewById(R.id.flipper);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.next:
                flipper.showNext();
                break;


            case R.id.update:
//                edit profile

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.roles_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.switch_role) {

            // setup the alert builder
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Choose an Account");

// add a list
            String[] codes = {"Corporate Client", "Truck Owner", "Supplier"};
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

                        case 2:
                            try {
                                roles = new JSONArray(sp.getString("user_type", null));
                                for (int i = 0; i <= roles.length(); i++) {
                                    try {
                                        JSONObject role_object = roles.getJSONObject(i);
                                        if (role_object.getString("code").contains("XSUP")) {
                                            getSupplierProfile();
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
                            Toast.makeText(Activity_Driver_Profile.this, "No roles defined", Toast.LENGTH_SHORT).show();


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
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
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


    void getCorporateProfile() {
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


    void getSupplierProfile() {
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

}