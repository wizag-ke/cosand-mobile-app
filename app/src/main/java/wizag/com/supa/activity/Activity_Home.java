package wizag.com.supa.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import wizag.com.supa.MySingleton;
import wizag.com.supa.R;
import wizag.com.supa.SessionManager;

public class Activity_Home extends AppCompatActivity {
    CardView buy, sell, wallet, profile, supply, owner;
    JSONArray role_array;
    SessionManager session;
    String token, user_role;
    Context context;
    JSONArray roles;
    String order_id_txt, order_id_confirm;
    String firebase_token;
    String PostToken = "http://sduka.wizag.biz/api/v1/profiles/token";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        firebase_token = FirebaseInstanceId.getInstance().getToken();
        postFirebaseToken();


        context = this;
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

      /*  SharedPreferences sp = getSharedPreferences("notification", MODE_PRIVATE);
        if (sp != null) {
            order_id_txt = sp.getString("35", null);
        }

        SharedPreferences sp_confirm = getSharedPreferences("confirm_notification", MODE_PRIVATE);
        if (sp_confirm != null) {
            order_id_confirm = sp_confirm.getString("35", null);
        }

        if (order_id_confirm != null && order_id_txt != null) {
            if (order_id_txt.equalsIgnoreCase(order_id_confirm)) {
                sp_confirm.edit().remove("order_id").commit();
                startActivity(new Intent(getApplicationContext(), Activity_Confirm_Notification_Order.class));
                finish();

            }
        }*/



        /*kill shot!*/
        checkUserRole();


        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        token = user.get("access_token");

        buy = findViewById(R.id.buy);
        sell = findViewById(R.id.sell);
        wallet = findViewById(R.id.wallet);
        profile = findViewById(R.id.profile);
        supply = findViewById(R.id.supply);
        owner = findViewById(R.id.trip);

        /*get user code*/
        SharedPreferences sharedPreferences_sell = getSharedPreferences("profile", MODE_PRIVATE);
        String driver_code_sell = sharedPreferences_sell.getString("user_type", null);

//        Toast.makeText(context, driver_code_sell, Toast.LENGTH_LONG).show();


        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Activity_Home.class));

            }
        });


        sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                startActivity(new Intent(getApplicationContext(), Activity_Sell.class));

            }
        });

        supply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Activity_List_Orders.class));
            }
        });

        wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(), ActivityLoginWallet.class));
//                if (driver_code_sell.isEmpty()) {
//                    AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
////   dont uncomment here                             builder1.setTitle("Access Denied!");
//                    builder1.setMessage("Create a User Account to continue");
//                    builder1.setCancelable(true);
//
//                    builder1.setPositiveButton(
//                            "Proceed",
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    startActivity(new Intent(getApplicationContext(), Activity_Register_Dashboard.class));
//                                    finish();
//                                }
//                            });
//
//                    builder1.setNegativeButton(
//                            "Not now",
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    dialog.cancel();
//                                }
//                            });
//
//                    AlertDialog alert11 = builder1.create();
//                    alert11.show();
//                } else {
//
//                    startActivity(new Intent(getApplicationContext(), Activity_Wallet.class));
//                }
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sp = getSharedPreferences("profile", MODE_PRIVATE);
                String driver_code = sp.getString("user_type", null);

                try {
                    roles = new JSONArray(sp.getString("user_type", null));

                } catch (JSONException e) {
                    e.printStackTrace();
                    roles = new JSONArray();
                }

                if (user_role.equalsIgnoreCase("XDRI")) {
                    Intent driver_profile = new Intent(getApplicationContext(), Activity_Driver_Profile.class);
                    startActivity(driver_profile);
                } else if (user_role.equalsIgnoreCase("XIND")) {
                    Intent ind_profile = new Intent(getApplicationContext(), Activity_Indvidual_Client_Profile.class);
                    startActivity(ind_profile);
                } else if (user_role.equalsIgnoreCase("XCOR")) {
                    Intent cor_profile = new Intent(getApplicationContext(), Activity_Corporate_Profile.class);
                    startActivity(cor_profile);
                } else if (user_role.equalsIgnoreCase("XTON")) {
//                    Intent truck_profile = new Intent(getApplicationContext(), Activity_Truck_Owner_Profile.class);
//                    startActivity(truck_profile);
                } else if (user_role.equalsIgnoreCase("XSUP")) {
                    Intent truck_profile = new Intent(getApplicationContext(), Activity_Supplier_Profile.class);
                    startActivity(truck_profile);
                }


            }
        });


    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        killActivity();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void killActivity() {
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sign_out) {

            session.logoutUser();
            finish();

//            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void checkUserRole() {
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
                    pDialog.dismiss();
                    if (jsonObject != null) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        JSONObject user = data.getJSONObject("user");

                        role_array = user.getJSONArray("roles");
                        for (int i = 0; i < role_array.length(); i++) {
                            JSONObject object = role_array.getJSONObject(i);
                            user_role = object.getString("code");

                            Toast.makeText(context, user_role, Toast.LENGTH_SHORT).show();


                            if (user_role.isEmpty()) {
                                startActivity(new Intent(getApplicationContext(), Activity_Register_Dashboard.class));
                                finish();

                            } else {
                                pDialog.dismiss();
                            }

                        }

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
//               pDialog.dismiss();
//                Toast.makeText(getApplicationContext(), "An Error Occurred while loading Driver profile" + error.getMessage(), Toast.LENGTH_LONG).show();

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


    private void postFirebaseToken() {
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(Activity_Home.this);
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, PostToken,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            pDialog.dismiss();
                            String message = jsonObject.getString("message");
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //Toast.makeText(Activity_Home.this, "", Toast.LENGTH_SHORT).show();
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                error.getMessage();
                Toast.makeText(Activity_Home.this, "Error sending instance token", Toast.LENGTH_LONG).show();
                pDialog.dismiss();
            }
        }) {
            //adding parameters to the request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("token", firebase_token);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                SessionManager sessionManager = new SessionManager(getApplicationContext());
                HashMap<String, String> user = sessionManager.getUserDetails();
                String token = user.get("access_token");
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


}
