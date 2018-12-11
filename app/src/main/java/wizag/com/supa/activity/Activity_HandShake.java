package wizag.com.supa.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import wizag.com.supa.BuildConfig;
import wizag.com.supa.R;
import wizag.com.supa.SessionManager;

public class Activity_HandShake extends AppCompatActivity {
    SharedPreferences prefs  ;
    EditText otp;
    String message;
    String order_id, site_id;
    private static final String SHARED_PREF_NAME2 = "handshake";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hand_shake);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        prefs=this.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);


        otp = findViewById(R.id.otp);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            order_id = extras.getString("order_id");
            site_id = extras.getString("site_id");

            //Toast.makeText(this, order_id+"\n"+site_id, Toast.LENGTH_SHORT).show();
            //save sHared preferences here
            // MY_PREFS_NAME - a static String variable like:
            //public static final String MY_PREFS_NAME = "MyPrefsFile";
            SharedPreferences prefs = getSharedPreferences(SHARED_PREF_NAME2, MODE_PRIVATE);
            prefs.edit().putString(order_id, order_id).apply();
            prefs.edit().putString(site_id, site_id).apply();


        }


        Button proceed = findViewById(R.id.confirm);

        proceed.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                /*validate otp then start questionaire activity*/
                if (!otp.getText().toString().isEmpty()) {
                    validateOTP();

                } else {
                    Toast.makeText(Activity_HandShake.this, "Enter OTP to continue", Toast.LENGTH_SHORT).show();
                }
//
            }

        });


    }

    private void validateOTP() {
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(Activity_HandShake.this);
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://sduka.wizag.biz/api/v1/orders/" + order_id + "/complete",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            pDialog.dismiss();
                            message = jsonObject.getString("message");
                            String status = jsonObject.getString("status");
                            if (status.equalsIgnoreCase("success")) {

                                /*delete shared prefs value for OTP on the buyer side*/
                                SharedPreferences sp = getSharedPreferences("notification", MODE_PRIVATE);
                                sp.edit().remove("driver_name").commit();
                                sp.edit().remove("driver_phone").commit();
                                sp.edit().remove("order_otp").commit();

                                Toast.makeText(Activity_HandShake.this, message, Toast.LENGTH_LONG).show();

                                Intent intent = new Intent(getApplicationContext(), Activity_Questionaire.class);
                                intent.putExtra("order_id", order_id);
                                intent.putExtra("site_id", site_id);
                                startActivity(intent);


                            } else if (status.equalsIgnoreCase("fail")) {

                                Toast.makeText(Activity_HandShake.this, message, Toast.LENGTH_LONG).show();


                            }


                            if (status.equalsIgnoreCase("error")) {
                                Toast.makeText(Activity_HandShake.this, message, Toast.LENGTH_LONG).show();

                                /*close ur eyes n ride along*/
                                Intent intent = new Intent(getApplicationContext(), Activity_Questionaire.class);
                                startActivity(intent);

                            }
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }



                    }
                }, error -> {
                    error.printStackTrace();
                    Toast.makeText(Activity_HandShake.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                })

        {
            //adding parameters to the request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("otp", otp.getText().toString());

                return params;
            }

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
// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

}
