package wizag.com.supa.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import wizag.com.supa.adapter.Adapter_Indivindual_Sites;
import wizag.com.supa.adapter.Adapter_View_orders;
import wizag.com.supa.models.Model_Indivindual_Sites;
import wizag.com.supa.models.Model_Orders;

public class Activity_View_Indivindual_Sites extends AppCompatActivity {
    Adapter_Indivindual_Sites adapter_indivindual_sites;
    TextView name, descritpion;
    RecyclerView recycle;
    private RecyclerView.Adapter adapter;
    private List<Model_Indivindual_Sites> indivindual_sites;
    private static final String URL_DATA = "http://sduka.wizag.biz/api/v1/profiles/sites/";
    String id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_client_sites);

        //initializing the recycler view
        recycle = findViewById(R.id.recycler_view);
        recycle.setHasFixedSize(true);
        recycle.setLayoutManager(new LinearLayoutManager(this));
        indivindual_sites = new ArrayList<>();

        //initializing adapter
        adapter_indivindual_sites = new Adapter_Indivindual_Sites(indivindual_sites, this);
       // adapter_indivindual_sites = new Adapter_Indivindual_Sites.MyViewHolder(indivindual_sites, this);
        recycle.setAdapter(adapter_indivindual_sites);

        name = findViewById(R.id.name);
        descritpion = findViewById(R.id.description);
        loadUrlData();
    }

    public void loadUrlData()
    {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://sduka.wizag.biz/api/v1/profiles/sites/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    pDialog.dismiss();
                    if (jsonObject != null) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        JSONArray sites = data.getJSONArray("sites");
                        for (int k = 0; k < sites.length(); k++) {
                            Model_Indivindual_Sites model_indivindual_sites = new Model_Indivindual_Sites();
                            JSONObject ordersObject = sites.getJSONObject(k);


                            id = ordersObject.getString("id");
                            String name = ordersObject.getString("name");
                            String description = ordersObject.getString("description");



//                        JSONObject site = ordersObject.getJSONObject("site");
//                        String name = site.getString("name");
                            model_indivindual_sites.setId(id);
                            model_indivindual_sites.setName(name);
                            model_indivindual_sites.setDescription(description);




                            if (indivindual_sites.contains(id)) {
                                /*do nothing*/
                            } else {
                                indivindual_sites.add(model_indivindual_sites);
                            }
                        }


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapter_indivindual_sites.notifyDataSetChanged();
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
