package wizag.com.supa.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import wizag.com.supa.R;

public class Activity_Truck_Owner extends AppCompatActivity {
    EditText fname, lname, id_no, phone, email, password, confirm_password;
    Button register;
    String register_truck_owner_url = "http://sduka.wizag.biz/api/v1/auth/signup";
    String fname_txt, lname_txt, id_no_txt, email_txt, phone_txt,
            password_txt, confirm_password_txt, kra_pin_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_truck_owner);

        fname = findViewById(R.id.fname);
        lname = findViewById(R.id.lname);
        id_no = findViewById(R.id.id_no);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        password = findViewById(R.id.password);
        confirm_password = findViewById(R.id.confirm_password);


        register = findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!isNetworkConnected()) {
                    Toast.makeText(Activity_Truck_Owner.this, "Ensure that you are connected to the internet", Toast.LENGTH_SHORT).show();
                } else {
                    registerTruckOwner();
                }

            }
        });
    }

    private void registerTruckOwner() {
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(Activity_Truck_Owner.this);
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        fname_txt = fname.getText().toString();
        lname_txt = lname.getText().toString();
        id_no_txt = id_no.getText().toString();
        email_txt = email.getText().toString();
        phone_txt = phone.getText().toString();
        password_txt = password.getText().toString();
        confirm_password_txt = confirm_password.getText().toString();

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, register_truck_owner_url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject obj = new JSONObject(response);
                            pDialog.dismiss();
                            String message = obj.getString("message");
                            String status = obj.getString("status");
//                            JSONObject data = new JSONObject("data");
                            if (status.equalsIgnoreCase("success")) {
                                Toast.makeText(Activity_Truck_Owner.this, message, Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), MenuActivity.class));
                                finish();
                            } else if (status.equalsIgnoreCase("error")) {

                                Toast.makeText(Activity_Truck_Owner.this, message, Toast.LENGTH_SHORT).show();


                            }

                            JSONArray jsonArray = obj.getJSONArray("data");
                            for (int k = 0; k < jsonArray.length(); k++) {
                                String data_message = jsonArray.getString(k);

                                if (status.equalsIgnoreCase("fail")) {
                                    Toast.makeText(Activity_Truck_Owner.this, data_message, Toast.LENGTH_SHORT).show();
                                }
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //Toast.makeText(Activity_Buy.this, "", Toast.LENGTH_SHORT).show();
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Activity_Truck_Owner.this, "Registration could not be completed" + error.getMessage(), Toast.LENGTH_SHORT).show();
                pDialog.dismiss();
            }
        }) {
            //adding parameters to the request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("fname", fname_txt);
                params.put("lname", lname_txt);
                params.put("phone", phone_txt);
                params.put("email", email_txt);
                params.put("id_no", id_no_txt);
                params.put("password", password_txt);
                params.put("password_confirmation", confirm_password_txt);
                params.put("role_id", "XTON");

                //params.put("code", "blst786");
                //  params.put("")
                return params;
            }


        };
// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

}
