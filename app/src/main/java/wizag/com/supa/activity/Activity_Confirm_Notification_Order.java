package wizag.com.supa.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import wizag.com.supa.MySingleton;
import wizag.com.supa.R;
import wizag.com.supa.SessionManager;
import wizag.com.supa.helper.MyApplication;

public class Activity_Confirm_Notification_Order extends AppCompatActivity {
    TextView service, material, detail, material_class, unit, quantity_confirm, location_confirm, description;
    Button accept_order;
    String order_id, client_name_txt, client_phone_txt;
    String site_id;
    TextView client_name, client_phone;

    private static final String SHARED_PREF_NAME = "site_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);
        SharedPreferences sp = getSharedPreferences("notification", MODE_PRIVATE);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            order_id = extras.getString("order_id");

        }


        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        service = findViewById(R.id.service);
        material = findViewById(R.id.material);
        detail = findViewById(R.id.detail);
        material_class = findViewById(R.id.material_class);
        unit = findViewById(R.id.unit);
        quantity_confirm = findViewById(R.id.quantity);
        location_confirm = findViewById(R.id.location);
        description = findViewById(R.id.description);
        accept_order = findViewById(R.id.accept_order);
        client_phone = findViewById(R.id.client_phone);
        client_name = findViewById(R.id.client_name);

        client_name_txt = sp.getString("client_name", null);
        client_phone_txt = sp.getString("client_phone", null);
        client_phone.setText(client_phone_txt);
        client_name.setText(client_name_txt);


        accept_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*call dialog to confirm order*/

                confirmTakingOrder();
            }
        });
        getOrderSummary();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    private void getOrderSummary() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://sduka.wizag.biz/api/v1/orders/" + order_id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    pDialog.dismiss();
                    if (jsonObject != null) {
                        JSONObject data = jsonObject.getJSONObject("data").getJSONObject("order");
                        String material_type = data.getString("material_type");
                        String material_item = data.getString("material_item");
                        String material_detail = data.getString("material_detail");
                        String material_class_txt = data.getString("material_class");
                        String material_quantity = data.getString("material_quantity");
                        String material_cost = data.getString("quote");

                        JSONObject site = data.getJSONObject("site");
                        String name = site.getString("name");

                        site_id = site.getString("id");
                        String description_txt = site.getString("description");

                        String lat_lng = site.getString("lat_lng");

                        SharedPreferences sp = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("site_id", site_id);
                        editor.putString("lat_lng", lat_lng);
                        editor.apply();


                        service.setText(material_type);
                        material.setText(material_item);
                        detail.setText(material_detail);
                        material_class.setText(material_class_txt);
                        quantity_confirm.setText(material_quantity);
                        unit.setText(material_cost);
                        location_confirm.setText(name);
                        description.setText(description_txt);

                    }


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


        }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                SessionManager sessionManager = new SessionManager(getApplicationContext());
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

    public void confirmTakingOrder() {

        AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        // Set Custom Title
        TextView title = new TextView(this);
        // Title Properties
        title.setText("Info");
        title.setPadding(20, 20, 20, 20);   // Set Position
        title.setGravity(Gravity.CENTER);

        title.setTextColor(Color.BLACK);
        title.setTextSize(20);
        alertDialog.setCustomTitle(title);

        // Set Message
        TextView msg = new TextView(this);
        // Message Properties
        msg.setText("By clicking on the 'Agree' button, you accept to deliver the order as outlined in the Order Summary");
        msg.setGravity(Gravity.CENTER_HORIZONTAL);
        msg.setTextColor(Color.BLACK);
        msg.setTextSize(18);
        alertDialog.setView(msg);


        // Set Button
        // you can more buttons
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Agree", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
//                /*send tracking link to buyer and accept order*/
                confirmOrderReceipt();

            }
        });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Perform Action on Button
            }
        });

        new Dialog(getApplicationContext());
        alertDialog.show();

        // Set Properties for OK Button
        final Button okBT = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        LinearLayout.LayoutParams neutralBtnLP = (LinearLayout.LayoutParams) okBT.getLayoutParams();
        neutralBtnLP.gravity = Gravity.FILL_HORIZONTAL;
        okBT.setPadding(50, 10, 10, 10);   // Set Position
        okBT.setTextColor(Color.BLUE);
        okBT.setLayoutParams(neutralBtnLP);

        final Button cancelBT = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        LinearLayout.LayoutParams negBtnLP = (LinearLayout.LayoutParams) okBT.getLayoutParams();
        negBtnLP.gravity = Gravity.FILL_HORIZONTAL;
        cancelBT.setTextColor(Color.RED);
        cancelBT.setLayoutParams(negBtnLP);
    }

    private void confirmOrderReceipt() {
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(Activity_Confirm_Notification_Order.this);
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://sduka.wizag.biz/api/v1/orders/" + order_id + "/deliver",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            pDialog.dismiss();
                            String message = jsonObject.getString("message");
                            Intent intent = new Intent(getApplicationContext(), Activity_HandShake.class);
                            intent.putExtra("site_id", site_id);
                            intent.putExtra("order_id", order_id);
                            startActivity(intent);
                            finish();
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //Toast.makeText(Activity_Buy.this, "", Toast.LENGTH_SHORT).show();
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                error.printStackTrace();
                Toast.makeText(Activity_Confirm_Notification_Order.this, "An Error Occured", Toast.LENGTH_SHORT).show();
                pDialog.dismiss();
            }
        }) {
            //adding parameters to the request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();


                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                SessionManager sessionManager = new SessionManager(getApplicationContext());
                HashMap<String, String> user = sessionManager.getUserDetails();
                String accessToken = user.get("access_token");
                String bearer = "Bearer ".concat(accessToken);
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
