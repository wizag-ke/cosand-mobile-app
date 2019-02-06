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
import android.util.Log;
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

    String driver_fname,tonnage_txt, driver_lname, driver_email, driver_phone, driver_id_no, driver_plate_no, driver_description, driver_log_book, driver_make, driver_model, driver_year;
    JSONArray roles;
    Activity_Login login;
    SharedPreferences sp;
    Context context;
    String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_profile);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context = this;
        getDriverDetails();
        sp = getSharedPreferences("profile", MODE_PRIVATE);

        driver_fname = sp.getString("reg_fname", null);
        driver_lname = sp.getString("reg_lname", null);
        driver_email = sp.getString("reg_email", null);
        driver_phone = sp.getString("reg_phone", null);
        driver_id_no = sp.getString("reg_id", null);
//
//        driver_plate_no = sp.getString("driver_plate_no", null);
//        driver_description = sp.getString("driver_description", null);
//        driver_log_book = sp.getString("driver_logbook", null);
//        driver_make = sp.getString("driver_make", null);
//        driver_model = sp.getString("driver_model", null);
//        driver_year = sp.getString("driver_year", null);

//        getUserProfileRoles();

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



        /*switch logic*/

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.switch_role) {

            // setup the alert builder
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Choose an Account");

            /*get codes*/
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

// add a list
            String[] codes = {"Corporate Client", "Truck Owner", "Supplier","Individual"};
            builder.setItems(codes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0:

                            StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://sduka.dnsalias.com/api/v1/profiles", new com.android.volley.Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {

                                        JSONObject jsonObject = new JSONObject(response);
                                        JSONObject user_data = jsonObject.getJSONObject("data").getJSONObject("user");
                                        JSONArray roles = user_data.getJSONArray("roles");

                                        for(int i = 0;i<roles.length();i++){
                                            JSONObject roles_object = roles.getJSONObject(i);
                                            code = roles_object.getString("code");
                                            if(code.equals("XCOR")){

                                                startActivity(new Intent(getApplicationContext(), Activity_Corporate_Profile.class));
                                                finish();

                                            }


                                            else {
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
                                            }



                                        }




                                        /*pDialog.dismiss();*/

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new com.android.volley.Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    error.printStackTrace();
                                    /*pDialog.dismiss();*/
                                    Toast.makeText(getApplicationContext(), "An Error Occurred" + error.getMessage(), Toast.LENGTH_LONG).show();

                                }


                            }) {


                                @Override
                                public Map<String, String> getHeaders() throws AuthFailureError {
                                    Map<String, String> params = new HashMap<String, String>();
                                    SessionManager  sessionManager = new SessionManager(getApplicationContext());
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


                            MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);


                            int socketTimeout = 30000;
                            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                            stringRequest.setRetryPolicy(policy);
                            requestQueue.add(stringRequest);


//


                            break;

                        case 1:

                            StringRequest stringRequest1 = new StringRequest(Request.Method.GET, "http://sduka.dnsalias.com/api/v1/profiles", new com.android.volley.Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {

                                        JSONObject jsonObject = new JSONObject(response);
                                        JSONObject user_data = jsonObject.getJSONObject("data").getJSONObject("user");
                                        JSONArray roles = user_data.getJSONArray("roles");

                                        for(int i = 0;i<roles.length();i++){
                                            JSONObject roles_object = roles.getJSONObject(i);
                                            code = roles_object.getString("code");
                                            if(code.equalsIgnoreCase("XTON")){

                                                startActivity(new Intent(getApplicationContext(), Activity_Corporate_Profile.class));
                                                finish();

                                            }else {
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
                                            }



                                        }




                                        /*pDialog.dismiss();*/

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new com.android.volley.Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    error.printStackTrace();
                                    /*pDialog.dismiss();*/
                                    Toast.makeText(getApplicationContext(), "An Error Occurred" + error.getMessage(), Toast.LENGTH_LONG).show();

                                }


                            }) {


                                @Override
                                public Map<String, String> getHeaders() throws AuthFailureError {
                                    Map<String, String> params = new HashMap<String, String>();
                                    SessionManager  sessionManager = new SessionManager(getApplicationContext());
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


                            MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest1);


                            int socketTimeout1 = 30000;
                            RetryPolicy policy1 = new DefaultRetryPolicy(socketTimeout1, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                            stringRequest1.setRetryPolicy(policy1);
                            requestQueue.add(stringRequest1);

                            break;

                        case 2:
                            StringRequest stringRequest2 = new StringRequest(Request.Method.GET, "http://sduka.dnsalias.com/api/v1/profiles", new com.android.volley.Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {

                                        JSONObject jsonObject = new JSONObject(response);
                                        JSONObject user_data = jsonObject.getJSONObject("data").getJSONObject("user");
                                        JSONArray roles = user_data.getJSONArray("roles");

                                        for(int i = 0;i<roles.length();i++){
                                            JSONObject roles_object = roles.getJSONObject(i);
                                            code = roles_object.getString("code");
                                            if(code.equalsIgnoreCase("XSUP")){

                                                startActivity(new Intent(getApplicationContext(), Activity_Supplier_Profile.class));
                                                finish();

                                            }else {
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
                                            }



                                        }




                                        /*pDialog.dismiss();*/

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new com.android.volley.Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    error.printStackTrace();
                                    /*pDialog.dismiss();*/
                                    Toast.makeText(getApplicationContext(), "An Error Occurred" + error.getMessage(), Toast.LENGTH_LONG).show();

                                }


                            }) {


                                @Override
                                public Map<String, String> getHeaders() throws AuthFailureError {
                                    Map<String, String> params = new HashMap<String, String>();
                                    SessionManager  sessionManager = new SessionManager(getApplicationContext());
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


                            MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest2);


                            int socketTimeout2 = 30000;
                            RetryPolicy policy2 = new DefaultRetryPolicy(socketTimeout2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                            stringRequest2.setRetryPolicy(policy2);
                            requestQueue.add(stringRequest2);
                            break;

                        case 3:

                            StringRequest stringRequest3 = new StringRequest(Request.Method.GET, "http://sduka.dnsalias.com/api/v1/profiles", new com.android.volley.Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {

                                        JSONObject jsonObject = new JSONObject(response);
                                        JSONObject user_data = jsonObject.getJSONObject("data").getJSONObject("user");
                                        JSONArray roles = user_data.getJSONArray("roles");

                                        for(int i = 0;i<roles.length();i++){
                                            JSONObject roles_object = roles.getJSONObject(i);
                                            code = roles_object.getString("code");
                                            if(code.equalsIgnoreCase("XIND")){

                                                startActivity(new Intent(getApplicationContext(), Activity_Indvidual_Client_Profile.class));
                                                finish();

                                            }


                                        }




                                        /*pDialog.dismiss();*/

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new com.android.volley.Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    error.printStackTrace();
                                    /*pDialog.dismiss();*/
                                    Toast.makeText(getApplicationContext(), "An Error Occurred" + error.getMessage(), Toast.LENGTH_LONG).show();

                                }


                            }) {


                                @Override
                                public Map<String, String> getHeaders() throws AuthFailureError {
                                    Map<String, String> params = new HashMap<String, String>();
                                    SessionManager  sessionManager = new SessionManager(getApplicationContext());
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


                            MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest3);


                            int socketTimeout3 = 30000;
                            RetryPolicy policy3 = new DefaultRetryPolicy(socketTimeout3, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                            stringRequest3.setRetryPolicy(policy3);
                            requestQueue.add(stringRequest3);
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


    private void getDriverDetails() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://sduka.dnsalias.com/api/v1/profiles", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {


                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject user_data = jsonObject.getJSONObject("data").getJSONObject("user");




                    JSONArray roles = user_data.getJSONArray("roles");
                    for(int i = 0;i<roles.length();i++){
                        JSONObject roles_object = roles.getJSONObject(i);
                        String driver_code = roles_object.getString("code");

                        JSONObject details_driver = roles_object.getJSONObject("details");
                        if(driver_code.equalsIgnoreCase("XDRI")){

                            JSONObject truck = details_driver.getJSONObject("truck");


                            driver_plate_no = truck.getString("plate_no");
                            Toast.makeText(Activity_Driver_Profile.this, "Data"+driver_plate_no, Toast.LENGTH_SHORT).show();
                            Log.d("truck_no",driver_plate_no);
                            tonnage_txt = truck.getString("tonnage");
                            driver_make = truck.getString("make");
                            driver_model = truck.getString("model");
                            driver_year = truck.getString("year");

                            plate_no.setText(driver_plate_no);
                            logbook.setText(driver_log_book);
                            tonnage.setText(driver_description);
                            model.setText(driver_model);
                            make.setText(driver_make);
                            year.setText(driver_year);

                            Toast.makeText(Activity_Driver_Profile.this, driver_plate_no, Toast.LENGTH_SHORT).show();

                        }






//                        String parent_type = roles_object.getString("roles");

                    }


                    /*pDialog.dismiss();*/

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                /*pDialog.dismiss();*/
                Toast.makeText(getApplicationContext(), "An Error Occurred" + error.getMessage(), Toast.LENGTH_LONG).show();

            }


        }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                SessionManager  sessionManager = new SessionManager(getApplicationContext());
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