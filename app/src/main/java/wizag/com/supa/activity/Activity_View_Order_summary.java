package wizag.com.supa.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

public class Activity_View_Order_summary extends AppCompatActivity {
    TextView service, material, detail, material_class, unit, quantity_confirm, location_confirm;
    Button view_orders;
    String order_id;
    String site_id;
    private static final String SHARED_PREF_NAME = "order";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_orders);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            order_id = extras.getString("order_id");
//            Toast.makeText(this, order_id, Toast.LENGTH_SHORT).show();

        }


        service = findViewById(R.id.service);
        material = findViewById(R.id.material);
        detail = findViewById(R.id.detail);
        material_class = findViewById(R.id.material_class);
        unit = findViewById(R.id.unit);
        quantity_confirm = findViewById(R.id.quantity);
        location_confirm = findViewById(R.id.location);
        view_orders = findViewById(R.id.view_orders);
        getOrderSummary();


        view_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Activity_View_Order_summary.this, Activity_List_Orders.class);
                intent.putExtra("order_id", order_id);
                intent.putExtra("site_id", site_id);
                startActivity(intent);
                finish();
            }
        });


    }

    private void getOrderSummary() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://sduka.dnsalias.com/api/v1/orders/" + order_id, new Response.Listener<String>() {
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
                        site_id = String.valueOf(site.getInt("id"));

                        SharedPreferences sp = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("site_id", site_id);
                        editor.putString("order_id", order_id);
                        editor.apply();
                        Toast.makeText(Activity_View_Order_summary.this, "site id"+site_id, Toast.LENGTH_SHORT).show();


                        service.setText(material_type);
                        material.setText(material_item);
                        detail.setText(material_detail);
                        material_class.setText(material_class_txt);
                        quantity_confirm.setText(material_quantity);
                        unit.setText(material_cost);
                        location_confirm.setText(name);


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
}
