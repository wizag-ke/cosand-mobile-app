package wizag.com.supa.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import wizag.com.supa.R;
import wizag.com.supa.SessionManager;

public class Activity_Quotation extends AppCompatActivity {
    Button submit;
    TextView unit_measure, distance, quantity_text, number_text, delivery_cost;
    EditText quantity_buy;
    String unit_measure_txt, cost_of_delivery_per_unit;
    Button calc;
    TextView total_cost_text;
    int distance_txt;
    String location, material_id, quality_id, size_id, location_address;
    private static final String SHARED_PREF_NAME = "mysharedpref";
    private static final String PREF_NAME = "location";

    SessionManager sessionManager;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotation);

        Intent mIntent = getIntent();
        if (mIntent != null) {
            distance_txt = mIntent.getIntExtra("distance", 0);
            unit_measure_txt = mIntent.getStringExtra("unit_measure");
            location = mIntent.getStringExtra("location");
            cost_of_delivery_per_unit = mIntent.getStringExtra("cost_of_delivery_per_unit");
            // Toast.makeText(this, "" + location, Toast.LENGTH_SHORT).show();

        }
        sessionManager = new SessionManager(getApplicationContext());
        HashMap<String, String> user = sessionManager.getUserDetails();
        token = user.get("access_token");


       /* material_id = preferences.getString("material_id", "");
        quality_id = preferences.getString("quality_id", "");
        size_id = preferences.getString("size_id", "");
*/

        total_cost_text = (TextView) findViewById(R.id.total_cost_text);
        unit_measure = (TextView) findViewById(R.id.unit_measure);
        delivery_cost = (TextView) findViewById(R.id.delivery_cost);
        distance = (TextView) findViewById(R.id.distance);
        quantity_text = (TextView) findViewById(R.id.quantity_text);
        number_text = (TextView) findViewById(R.id.number_text);
        // quantity_text.setVisibility(View.GONE);
        number_text.setVisibility(View.GONE);

        quantity_buy = (EditText) findViewById(R.id.quantity_buy);
        // final int quantity_txt = Integer.parseInt(quantity.getText().toString());


        //set text
       /* distance.setText(String.valueOf(distance_txt));
        // distance.setText(distance_txt);
        unit_measure.setText(unit_measure_txt);
        delivery_cost.setText(cost_of_delivery_per_unit);
*/
        calc = (Button) findViewById(R.id.calc);
        calc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //calculate cost
                distance.setText("10");
                unit_measure.setText("50");
                delivery_cost.setText("75");

                if (quantity_text.getText().toString().isEmpty()) {
                    quantity_text.setText("12");
                } else {
                    int quantity = Integer.parseInt(quantity_buy.getText().toString());
                    int cost = Integer.parseInt(delivery_cost.getText().toString());
                    int result = quantity * cost;
                    total_cost_text.setText(String.valueOf(result));


                }

                //Toast.makeText(Activity_Quotation.this, "" +result , Toast.LENGTH_SHORT).show();


            }
        });
        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //order request
//                loadRequest();
                if (quantity_text.getText().toString().isEmpty()) {
                    quantity_text.setError("Please Enter Quantity");
                } else {
                    Toast.makeText(Activity_Quotation.this, "Data has been submited successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), Activity_Wallet.class));
                    finish();
                }

            }
        });


    }

    public void loadRequest() {
        String POST_MATERIAL_URL = "http://sduka.dnsalias.com/api/order-request";
        SharedPreferences sp = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        material_id = sp.getString("material_id", null);
        quality_id = sp.getString("quality_id", null);
        size_id = sp.getString("size_id", null);
//get location
       /* final SharedPreferences location = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        location_address = location.getString("address", null);
*/


        String quantity_text_value = quantity_text.getText().toString();


        com.android.volley.RequestQueue queue = Volley.newRequestQueue(Activity_Quotation.this);
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, POST_MATERIAL_URL,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            pDialog.dismiss();
                            JSONObject data = jsonObject.getJSONObject("data");
                            String success_message = data.getString("message");
                            // Snackbar.make(sell_layout, "New Request Created Successfully" , Snackbar.LENGTH_LONG).show();
                            //Snackbar.make(sell_layout, "New request created successfully", Snackbar.LENGTH_LONG).show();

                            Toast.makeText(getApplicationContext(), "New request created successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), Activity_Wallet.class));
                            finish();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //Toast.makeText(Activity_Buy.this, "", Toast.LENGTH_SHORT).show();
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                Toast.makeText(getApplicationContext(), "Request could not be placed", Snackbar.LENGTH_LONG).show();
                pDialog.dismiss();
            }
        }) {
            //adding parameters to the request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("material_id", material_id);
                params.put("quality_id", quality_id);
                params.put("quantity", quantity_buy.getText().toString());
                params.put("size_id", size_id);
                params.put("location", location);
                //params.put("code", "blst786");
                //  params.put("")
                return params;
            }

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
// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }


}
