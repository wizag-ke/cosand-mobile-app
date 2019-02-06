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
import android.widget.LinearLayout;
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

public class Activity_Corporate_Profile extends AppCompatActivity implements View.OnClickListener {
    ViewFlipper flipper;
    EditText fname, lname, id_no, email, mobile_no;
    Button next;
    EditText company_name, kra_pin, certificate_no, company_mobile_no, location, co_email;
    Button previous, company_next;
    JSONArray roles;
    String corporate_fname;
    String corporate_lname;
    String corporate_email;
    String corporate_phone;
    String corporate_id_no;

    String company_name_txt;
    String kra_pin_txt;
    String cert_no_txt;
    String location_txt;
    String email_txt;
    LinearLayout xind_layout, xton_layout, xsup_layout, xdri_layout;
    Button xind, xton, xsup, xdri;
    String code;
    JSONObject role;

    SharedPreferences sp;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_corporate_client_profile);

        context = this;
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        sp = getSharedPreferences("profile", MODE_PRIVATE);
        corporate_fname = sp.getString("reg_fname", null);
        corporate_lname = sp.getString("reg_lname", null);
        corporate_email = sp.getString("reg_email", null);
        corporate_phone = sp.getString("reg_phone", null);
        corporate_id_no = sp.getString("reg_id", null);


        /*on changing role, eg to driver, open driver profile activity then the results will be populated */

        flipper = findViewById(R.id.flipper);
        lname = findViewById(R.id.lname);
        id_no = findViewById(R.id.id_no);
        email = findViewById(R.id.email);
        fname = findViewById(R.id.fname);
        mobile_no = findViewById(R.id.mobile_no);
        company_name = findViewById(R.id.company_name);
        kra_pin = findViewById(R.id.kra_pin);
        certificate_no = findViewById(R.id.certificate_no);
        company_mobile_no = findViewById(R.id.company_mobile_no);
        location = findViewById(R.id.location);
        co_email = findViewById(R.id.co_email);

        xind_layout = findViewById(R.id.xind_layout);
        xind_layout.setVisibility(View.GONE);
        xton_layout = findViewById(R.id.xton_layout);
        xton_layout.setVisibility(View.GONE);
        xsup_layout = findViewById(R.id.xsup_layout);
        xsup_layout.setVisibility(View.GONE);
        xdri_layout = findViewById(R.id.xdri_layout);
        xdri_layout.setVisibility(View.GONE);

        xind = findViewById(R.id.xind);
        xton = findViewById(R.id.xton);
        xsup = findViewById(R.id.xsup);
        xdri = findViewById(R.id.xdri);


        next = findViewById(R.id.next);
        next.setOnClickListener(this);
        previous = findViewById(R.id.previous);
        company_next = findViewById(R.id.company_next);
        company_next.setOnClickListener(this);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flipper.showPrevious();
            }
        });

        getCompanyDetails();


        fname.setText(corporate_fname);
        lname.setText(corporate_lname);
        email.setText(corporate_email);
        mobile_no.setText(corporate_phone);
        id_no.setText(corporate_id_no);






    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.next:
                flipper.showNext();
                break;

            case R.id.company_next:
                flipper.showNext();
                break;
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

        if (id == R.id.switch_role) {

            // setup the alert builder
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Choose an Account");

            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());


// add a list
            String[] codes = {"Driver", "Truck Owner", "Supplier","Individual"};
            builder.setItems(codes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0:
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
                                            if(code.equalsIgnoreCase("XDRI")){

                                                startActivity(new Intent(getApplicationContext(), Activity_Driver_Profile.class));
                                                finish();

                                            }


                                            else {
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

                                                startActivity(new Intent(getApplicationContext(), Activity_Truck_Owner_Profile.class));
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


                            MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest3);


                            int socketTimeout3 = 30000;
                            RetryPolicy policy3 = new DefaultRetryPolicy(socketTimeout3, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                            stringRequest3.setRetryPolicy(policy3);
                            requestQueue.add(stringRequest3);
                            break;

                        case 3:
                            StringRequest stringRequest4 = new StringRequest(Request.Method.GET, "http://sduka.dnsalias.com/api/v1/profiles", new com.android.volley.Response.Listener<String>() {
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


                            MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest4);


                            int socketTimeout4 = 30000;
                            RetryPolicy policy4 = new DefaultRetryPolicy(socketTimeout4, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                            stringRequest4.setRetryPolicy(policy4);
                            requestQueue.add(stringRequest4);
                            break;



                        default:
                            Toast.makeText(getApplicationContext(), "No roles defined", Toast.LENGTH_SHORT).show();


                    }

                }


            });

            // create and show the alert dialog
            AlertDialog dialog_opt = builder.create();
            dialog_opt.show();
        }
        if (id == R.id.add_role) {
            /*open the registration dashboard*/
            startActivity(new Intent(getApplicationContext(), Activity_Register_Dashboard.class));

        }


        return super.onOptionsItemSelected(item);
    }




    private void getCompanyDetails() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
      /*  final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();*/

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://sduka.dnsalias.com/api/v1/profiles", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {


                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject user_data = jsonObject.getJSONObject("data").getJSONObject("user");




                    JSONArray roles = user_data.getJSONArray("roles");
                    for(int i = 0;i<roles.length();i++){
                        JSONObject roles_object = roles.getJSONObject(i);
                        String coop_code = roles_object.getString("code");

                        JSONObject details_driver = roles_object.getJSONObject("details");
                        if(coop_code.equalsIgnoreCase("XCOR")) {

                            JSONObject company = details_driver.getJSONObject("company");
                            company_name_txt = company.getString("company");
                            kra_pin_txt = company.getString("kra_pin");
                            cert_no_txt = company.getString("certificate_number");
                            location_txt = company.getString("location");
                            email_txt = company.getString("email");
                            Toast.makeText(Activity_Corporate_Profile.this, company_name_txt, Toast.LENGTH_SHORT).show();
                            Log.d("company_name_txt",company_name_txt);

                            company_name.setText(company_name_txt);
                            kra_pin.setText(kra_pin_txt);
                            certificate_no.setText(cert_no_txt);
                            location.setText(location_txt);
                            co_email.setText(email_txt);


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
