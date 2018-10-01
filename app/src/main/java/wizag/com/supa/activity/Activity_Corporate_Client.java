package wizag.com.supa.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import wizag.com.supa.R;

public class Activity_Corporate_Client extends AppCompatActivity implements View.OnClickListener {
    EditText fname, lname, id_no, phone, email, password, confirm_password;
    Button next, submit, previous;
    EditText office_location, cert_no, company_pin, company_name;
    ViewFlipper flipper;
    String register_corporate_client_url = "http://sduka.wizag.biz/api/v1/auth/signup";

    String fname_txt, lname_txt, id_no_txt, email_txt, phone_txt,
            password_txt, confirm_password_txt, office_location_txt, cert_no_txt,
            company_pin_txt, company_name_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_corporate_client);


        fname = findViewById(R.id.fname);
        lname = findViewById(R.id.lname);
        id_no = findViewById(R.id.id_no);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        password = findViewById(R.id.password);
        confirm_password = findViewById(R.id.confirm_password);
        office_location = findViewById(R.id.office_location);
        cert_no = findViewById(R.id.cert_no);
        company_pin = findViewById(R.id.company_pin);
        company_name = findViewById(R.id.company_name);
        flipper = findViewById(R.id.flipper);

        next = findViewById(R.id.next);
        next.setOnClickListener(this);
        submit = findViewById(R.id.submit);
        submit.setOnClickListener(this);

        previous = findViewById(R.id.previous);
        next.setOnClickListener(this);
        previous.setOnClickListener(new View.OnClickListener() {
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
                /*validate*/

                if (fname.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Enter first name to continue", Toast.LENGTH_LONG).show();
                } else if (lname.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Enter last name to continue", Toast.LENGTH_LONG).show();
                } else if (id_no.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Enter ID No or Passport to continue", Toast.LENGTH_LONG).show();
                } else if (email.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Enter Email address to continue", Toast.LENGTH_LONG).show();
                } else if (phone.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Enter Phone Number to continue", Toast.LENGTH_LONG).show();
                } else if (password.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Enter Password to continue", Toast.LENGTH_LONG).show();
                } else if (!password.getText().toString().equals(confirm_password.getText().toString())) {
                    Toast.makeText(this, "Both passwords should be matching", Toast.LENGTH_LONG).show();
                } else {
                    flipper.showNext();
                }


                break;

            case R.id.submit:
//                validate
                if (office_location.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Enter Vehicle model to continue", Toast.LENGTH_LONG).show();
                } else if (cert_no.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Enter Vehicle make to continue", Toast.LENGTH_LONG).show();
                } else if (company_pin.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Enter Vehicle plate number to continue", Toast.LENGTH_LONG).show();
                } else if (company_name.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Enter Vehicle logbook number to continue", Toast.LENGTH_LONG).show();
                } else if (!isNetworkConnected()) {

                    Toast.makeText(Activity_Corporate_Client.this, "Ensure you have internet connection", Toast.LENGTH_SHORT).show();

                } else {

                    registerCorporateClient();

//

                }

                break;


        }

    }


    private void registerCorporateClient() {
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(Activity_Corporate_Client.this);
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Signing in...");
        progressDialog.show();
        //getText

//        Toast.makeText(this, String.valueOf(id_tonnage), Toast.LENGTH_SHORT).show();
        fname_txt = fname.getText().toString();
        lname_txt = lname.getText().toString();
        id_no_txt = id_no.getText().toString();
        email_txt = email.getText().toString();
        phone_txt = phone.getText().toString();
        password_txt = password.getText().toString();
        confirm_password_txt = confirm_password.getText().toString();
        office_location_txt = office_location.getText().toString();
        cert_no_txt = cert_no.getText().toString();
        company_pin_txt = company_pin.getText().toString();
        company_name_txt = company_name.getText().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, register_corporate_client_url, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();

                try {
                    //converting response to json object
                    JSONObject obj = new JSONObject(response);

                    String message = obj.getString("message");
                    String status = obj.getString("status");
//                            JSONObject data = new JSONObject("data");
                    if (status.equalsIgnoreCase("success")) {
                        Toast.makeText(Activity_Corporate_Client.this, message, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MenuActivity.class));
                        finish();
                    } else if(status.equalsIgnoreCase("error")){

                        Toast.makeText(Activity_Corporate_Client.this, message, Toast.LENGTH_SHORT).show();


                    }

                    JSONArray jsonArray = obj.getJSONArray("data");
                    for (int k = 0; k < jsonArray.length(); k++) {
                        String data_message = jsonArray.getString(k);

                        if (status.equalsIgnoreCase("fail")) {
                            Toast.makeText(Activity_Corporate_Client.this, data_message, Toast.LENGTH_SHORT).show();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Activity_Corporate_Client.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(Activity_Corporate_Client.this, "An error occurred" + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }


        ) {

            @Override
            protected HashMap<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("email", email_txt);
                params.put("id_no", id_no_txt);
                params.put("password", password_txt);
                params.put("password_confirmation", confirm_password_txt);
                params.put("fname", fname_txt);
                params.put("lname", lname_txt);
                params.put("phone", phone_txt);
                params.put("location", office_location_txt);
                params.put("certificate_number", cert_no_txt);
                params.put("kra_pin", company_pin_txt);
                params.put("company", company_name_txt);
                params.put("role_id", "XCOR");
                return params;
            }

        };


        queue.add(stringRequest);

    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

}
