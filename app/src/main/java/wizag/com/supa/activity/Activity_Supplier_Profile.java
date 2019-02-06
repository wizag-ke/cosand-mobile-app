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
    String code;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier_profile);
        context = this;
        sp = getSharedPreferences("profile", MODE_PRIVATE);


        String supplier_fname = sp.getString("reg_fname", null);
        String supplier_lname = sp.getString("reg_lname", null);
        String supplier_email = sp.getString("reg_email", null);
        String supplier_phone = sp.getString("reg_phone", null);
        String supplier_id_no = sp.getString("reg_id", null);


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
            String[] codes = {"Corporate Client", "Truck Owner", "Driver","Individual"};
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
                                            if(code.equalsIgnoreCase("XCOR")){

                                                startActivity(new Intent(getApplicationContext(), Activity_Corporate_Profile.class));
                                                finish();

                                            }else {
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

                                            }else {
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
        } else if (id == R.id.add_role) {
            /*open the registration dashboard*/
            startActivity(new Intent(getApplicationContext(), Activity_Register_Dashboard.class));

        }


        return super.onOptionsItemSelected(item);
    }











}
