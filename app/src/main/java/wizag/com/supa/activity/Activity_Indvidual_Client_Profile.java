package wizag.com.supa.activity;

import android.app.ProgressDialog;
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

public class Activity_Indvidual_Client_Profile extends AppCompatActivity implements View.OnClickListener {
    ViewFlipper flipper;
    EditText fname, lname, id_no, email, phone;
    Button next;
    String individual_fname, individual_lname, individual_email, individual_phone, individual_id_no, user_code;
    JSONArray roles;
    Activity_Login login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_client_profile);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences sp = getSharedPreferences("profile", MODE_PRIVATE);
        individual_fname = sp.getString("individual_fname", null);
        individual_lname = sp.getString("individual_lname", null);
        individual_email = sp.getString("individual_email", null);
        individual_phone = sp.getString("individual_phone", null);
        individual_id_no = sp.getString("individual_id_no", null);
        user_code = sp.getString("user_type", null);

        try {
            roles = new JSONArray(sp.getString("user_type", null));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (roles.length() < 1) {
            Toast.makeText(this, "You got no roles", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean available = false;
        for (int i = 0; i < roles.length(); i++) {
            try {
                JSONObject role = roles.getJSONObject(i);
                if (role.getString("code").contains("XIND")) {
                    available = true;
                    break;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        if (!available) {

            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage("Create Individual Client account to continue");
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
                            finish();
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
        }


        flipper = findViewById(R.id.flipper);
        fname = findViewById(R.id.fname);
        lname = findViewById(R.id.lname);
        id_no = findViewById(R.id.id_no);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);

        fname.setText(individual_fname);
        lname.setText(individual_lname);
        email.setText(individual_email);
        phone.setText(individual_phone);
        id_no.setText(individual_id_no);

        next = findViewById(R.id.next);
        next.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.next:
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

// add a list
            String[] codes = {"Corporate Client", "Truck Owner", "Driver", "Supplier"};
            builder.setItems(codes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0:

                            login = new Activity_Login();
                            getCorporateProfile();

                            startActivity(new Intent(getApplicationContext(), Activity_Corporate_Profile.class));
                            break;

                        case 1:
                            getTruckOwner();
                            startActivity(new Intent(getApplicationContext(), Activity_Truck_Owner_Profile.class));
                            break;

                        case 2:
                            getDriverProfile();
                            startActivity(new Intent(getApplicationContext(), Activity_Driver_Profile.class));
                            break;

                        case 3:
                            getSupplierProfile();
                            startActivity(new Intent(getApplicationContext(), Activity_Supplier_Profile.class));
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
                SessionManager    session = new SessionManager(getApplicationContext());
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

    public void getTruckOwner() {
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
                SessionManager  session = new SessionManager(getApplicationContext());
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
                SessionManager  session = new SessionManager(getApplicationContext());
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
