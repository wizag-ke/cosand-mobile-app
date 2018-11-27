package wizag.com.supa.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wizag.com.supa.MySingleton;
import wizag.com.supa.R;
import wizag.com.supa.SessionManager;
import wizag.com.supa.adapter.Adapter_View_orders;
import wizag.com.supa.models.Model_Orders;

public class Activity_List_Orders extends AppCompatActivity {
    AlertDialog alertDialog = null;
    EditText otp;
    String message;
    String order_id,site_id;
    TextView service, material, detail, material_class, unit, quantity_confirm, location_confirm, TxtOrderStatus;
    RecyclerView recycle;
    private RecyclerView.Adapter adapter;
    private List<Adapter_View_orders> orderList;
    private static final String URL_DATA = "http://sduka.wizag.biz/api/v1/orders/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_orders);
        //initializing the recycler view
        recycle=findViewById(R.id.recycler_view);
        recycle.setHasFixedSize(true);
        recycle.setLayoutManager(new LinearLayoutManager(this));


           service = findViewById(R.id.service);
            material = findViewById(R.id.material);
            detail = findViewById(R.id.detail);
            material_class = findViewById(R.id.material_class);
            unit = findViewById(R.id.unit);
            quantity_confirm = findViewById(R.id.quantity);
            location_confirm = findViewById(R.id.location);
            TxtOrderStatus=findViewById(R.id.order_Status);
            loadUrlData();

    }



    private void loadUrlData() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://sduka.wizag.biz/api/v1/orders/" , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    pDialog.dismiss();
                    if (jsonObject != null) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        JSONArray orders = data.getJSONArray("orders");
                        for(int k=0; k<orders.length();k++)
                        {
                            JSONObject ordersObject=orders.getJSONObject(k);


                        String material_type = ordersObject.getString("material_type");
                        String material_item = ordersObject.getString("material_item");
                        String material_detail = ordersObject.getString("material_detail");
                        String material_class_txt = ordersObject.getString("material_class");
                        String material_quantity = ordersObject.getString("material_quantity");
                        String material_cost = ordersObject.getString("quote");
                        String order_status = ordersObject.getString("order_status");

//                        JSONObject site = ordersObject.getJSONObject("site");
//                        String name = site.getString("name");


                        service.setText(material_type);
                        material.setText(material_item);
                        detail.setText(material_detail);
                        material_class.setText(material_class_txt);
                        quantity_confirm.setText(material_quantity);
                        unit.setText(material_cost);
                        TxtOrderStatus.setText(order_status);
                       // location_confirm.setText(name);
                        }

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