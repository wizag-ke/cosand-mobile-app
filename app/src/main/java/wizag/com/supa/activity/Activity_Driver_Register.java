package wizag.com.supa.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ViewFlipper;

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
import java.util.Map;

import wizag.com.supa.MySingleton;
import wizag.com.supa.R;
import wizag.com.supa.SessionManager;

public class Activity_Driver_Register extends AppCompatActivity implements View.OnClickListener {

    EditText fname, lname, id_no, phone, email, password, confirm_password;
    Button next, submit, previous;
    EditText model, make, plate_no, log_book, year;
    ViewFlipper flipper;
    String register_Driver_url = "http://sduka.wizag.biz/api/v1/auth/signup";
    String tonnages_url = "http://sduka.wizag.biz/api/v1/trucks/tonnages";
    Spinner tonnage;
    ArrayList<String> Tonnage;
    int id_tonnage;
    JSONArray tonnage_array;

    String fname_txt, lname_txt, id_no_txt, email_txt, phone_txt,
            password_txt, confirm_password_txt, model_txt, make_txt,
            plate_no_txt, log_book_txt, year_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_register);


        Tonnage = new ArrayList<>();
        fname = findViewById(R.id.fname);
        lname = findViewById(R.id.lname);
        id_no = findViewById(R.id.id_no);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        password = findViewById(R.id.password);
        confirm_password = findViewById(R.id.confirm_password);
        model = findViewById(R.id.model);
        make = findViewById(R.id.make);
        plate_no = findViewById(R.id.plate_no);
        log_book = findViewById(R.id.log_book);
        year = findViewById(R.id.year);
        flipper = findViewById(R.id.flipper);
        tonnage = findViewById(R.id.tonnage);
        year = findViewById(R.id.year);
        next = findViewById(R.id.next);
        next.setOnClickListener(this);
        submit = findViewById(R.id.submit);
        submit.setOnClickListener(this);

        previous = findViewById(R.id.previous);
        next.setOnClickListener(this);

        tonnage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String value = tonnage.getSelectedItem().toString();

                try {
                    JSONObject dataClicked = tonnage_array.getJSONObject(i);
                    id_tonnage = dataClicked.getInt("id");
//                    getMaterialUnits();
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flipper.showPrevious();
            }
        });
        getTonnage();

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
                if (model.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Enter Vehicle model to continue", Toast.LENGTH_LONG).show();
                } else if (make.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Enter Vehicle make to continue", Toast.LENGTH_LONG).show();
                } else if (plate_no.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Enter Vehicle plate number to continue", Toast.LENGTH_LONG).show();
                } /*else if (log_book.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Enter Vehicle logbook number to continue", Toast.LENGTH_LONG).show();
                } else if (year.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Enter Vehicle year of manufacture to continue", Toast.LENGTH_LONG).show();
                }*/

                else if (!isNetworkConnected()) {

                    Toast.makeText(Activity_Driver_Register.this, "Ensure you have internet connection", Toast.LENGTH_SHORT).show();

                } else {

                    registerDriver();

//

                }

                break;


        }

    }

    private void registerDriver() {
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(Activity_Driver_Register.this);
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
        model_txt = model.getText().toString();
        make_txt = make.getText().toString();
        plate_no_txt = plate_no.getText().toString();
        log_book_txt = log_book.getText().toString();
        year_txt = year.getText().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, register_Driver_url, new Response.Listener<String>() {


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
                        Toast.makeText(Activity_Driver_Register.this, message, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MenuActivity.class));
                        finish();
                    } else if(status.equalsIgnoreCase("error")){

                        Toast.makeText(Activity_Driver_Register.this, message, Toast.LENGTH_SHORT).show();


                    }

                    JSONArray jsonArray = obj.getJSONArray("data");
                    for (int k = 0; k < jsonArray.length(); k++) {
                        String data_message = jsonArray.getString(k);

                        if (status.equalsIgnoreCase("fail")) {
                            Toast.makeText(Activity_Driver_Register.this, data_message, Toast.LENGTH_SHORT).show();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Activity_Driver_Register.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(Activity_Driver_Register.this, "An error occurred" + error.getMessage(), Toast.LENGTH_SHORT).show();
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
                params.put("model", model_txt);
                params.put("make", make_txt);
                params.put("plate_no", plate_no_txt);
                params.put("log_book", log_book_txt);
                params.put("year", year_txt);
                params.put("tonnage_id", String.valueOf(id_tonnage));
                params.put("role_id", "XDRI");
                return params;
            }

        };


        queue.add(stringRequest);

    }


    private void getTonnage() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();


        StringRequest stringRequest = new StringRequest(Request.Method.GET, tonnages_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    pDialog.dismiss();
                    if (jsonObject != null) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        tonnage_array = data.getJSONArray("tonnages");

                        for (int z = 0; z < tonnage_array.length(); z++) {
                            JSONObject suppliers_object = tonnage_array.getJSONObject(z);

                            String material_id = suppliers_object.getString("id");
                            String name = suppliers_object.getString("description");


                            if (tonnage_array != null) {

                                if (Tonnage.contains(name)) {


                                } else {

                                    //Toast.makeText(getApplicationContext(), "data\n" + size.getJSONObject(m).getString("size"), Toast.LENGTH_SHORT).show();
                                    Tonnage.add(name);
//                                    Type.add(materialTypes.getJSONObject(p).getString("id"));

                                }

                            }


                        }


                    }
                    tonnage.setAdapter(new ArrayAdapter<String>(Activity_Driver_Register.this, android.R.layout.simple_spinner_dropdown_item, Tonnage));


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


        });

        MySingleton.getInstance(this).addToRequestQueue(stringRequest);


        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);


    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

}
