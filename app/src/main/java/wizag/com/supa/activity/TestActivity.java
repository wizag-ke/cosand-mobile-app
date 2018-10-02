package wizag.com.supa.activity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
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

public class TestActivity extends AppCompatActivity {
String token;
SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);
        sessionManager= new SessionManager(getApplicationContext());

        HashMap<String, String> user = sessionManager.getUserDetails();
        token = user.get("access_token");
        getDriverProfile();
    }

    private void getDriverProfile() {
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
                    pDialog.dismiss();
                    if (jsonObject != null) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        JSONObject user = data.getJSONObject("user");

                        String fname = user.getString("fname");
                        String lname = user.getString("lname");
                        String email = user.getString("email");
                        String phone = user.getString("phone");
                        String id_no = user.getString("id_no");

                        JSONObject role = user.getJSONObject("role");
                        String code = role.getString("code");

                        JSONObject truck = user.getJSONObject("truck");
                        String plate_no = truck.getString("plate_no");

                        JSONObject tonnage = truck.getJSONObject("tonnage");
                        String description = tonnage.getString("description");

                        String log_book = truck.getString("log_book");
                        String make = truck.getString("make");
                        String model = truck.getString("model");
                        String year = truck.getString("year");

                        SharedPreferences sp = getSharedPreferences("profile", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();


                        editor.putString("driver_fname", fname);
                        editor.putString("driver_lname", lname);
                        editor.putString("driver_email", email);
                        editor.putString("driver_phone", phone);
                        editor.putString("driver_id_no", id_no);
                        editor.putString("driver_code", code);
                        editor.putString("driver_plate_no", plate_no);
                        editor.putString("driver_description", description);
                        editor.putString("logbook", log_book);
                        editor.putString("make", make);
                        editor.putString("model", model);
                        editor.putString("year", year);
                        editor.apply();


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(TestActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
//               pDialog.dismiss();
                Toast.makeText(getApplicationContext(), "An Error Occurred while loading Driver profile" + error.getMessage(), Toast.LENGTH_LONG).show();

            }


        }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                sessionManager = new SessionManager(getApplicationContext());
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
