package wizag.com.supa.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import wizag.com.supa.MySingleton;
import wizag.com.supa.R;

public class Activity_Register extends AppCompatActivity {
    ProgressDialog progressDialog;
    EditText edit_firstname, edit_lastname, edit_id, edit_phone, email_address, create_password, confirm_password;

    Button button_register;
    String RegisterUrl = "http://sduka.wizag.biz/api/register";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        progressDialog = new ProgressDialog(this);


        edit_firstname = (EditText) findViewById(R.id.edit_firstname);
        edit_lastname = (EditText) findViewById(R.id.edit_lastname);
        edit_id = (EditText) findViewById(R.id.edit_id);
        edit_phone = (EditText) findViewById(R.id.edit_phone);
        email_address = (EditText) findViewById(R.id.email_address);
        create_password = (EditText) findViewById(R.id.create_password);
        confirm_password = (EditText) findViewById(R.id.confirm_password);
        button_register = (Button) findViewById(R.id.button_register);
        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String f_name = edit_firstname.getText().toString();
                final String l_name = edit_lastname.getText().toString();
                final String edit_id_txt = edit_id.getText().toString();
                final String edit_phone_txt = edit_phone.getText().toString();
                final String email_address_txt = email_address.getText().toString();
                final String create_password_txt = create_password.getText().toString();
                final String confirm_password_txt = confirm_password.getText().toString();


                if (TextUtils.isEmpty(f_name)) {
                    edit_firstname.setError("Please enter first name");
                    edit_firstname.requestFocus();
                    return;
                } else if (TextUtils.isEmpty(l_name)) {
                    edit_lastname.setError("Please enter last name");
                    edit_lastname.requestFocus();
                    return;
                } else if (TextUtils.isEmpty(edit_id_txt)) {
                    edit_id.setError("Please enter ID or Passport number");
                    edit_id.requestFocus();
                    return;
                } else if (TextUtils.isEmpty(edit_phone_txt) || !edit_phone_txt.startsWith("07") || edit_phone_txt.length() < 10) {
                    edit_phone.setError("Please enter a valid Phone number");
                    edit_phone.requestFocus();
                    return;
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email_address_txt).matches()) {
                    email_address.setError("Enter a valid email");
                    email_address.requestFocus();
                    return;
                } else if (TextUtils.isEmpty(create_password_txt)) {
                    create_password.setError("Enter a password");
                    create_password.requestFocus();
                    return;
                } else if (TextUtils.isEmpty(confirm_password_txt)) {
                    confirm_password.setError("Confirm password");
                    confirm_password.requestFocus();
                    return;
                } else if (!create_password_txt.equals(confirm_password_txt)) {
                    confirm_password.setError("Both passwords should match");
                    confirm_password.requestFocus();
                    return;
                } else if (!isNetworkConnected()) {

                    Toast.makeText(Activity_Register.this, "Ensure you have internet connection", Toast.LENGTH_SHORT).show();

                } else {
                    registerUser();
                }


            }
        });


    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }


    private void registerUser() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Signing in...");
        progressDialog.show();
        //getText

        StringRequest stringRequest = new StringRequest(Request.Method.POST, RegisterUrl, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();

                try {
                    //converting response to json object
                    JSONObject obj = new JSONObject(response);


                    Toast.makeText(Activity_Register.this, "User added", Toast.LENGTH_SHORT).show();


                    startActivity(new Intent(getApplicationContext(), Activity_Login.class));
                    finish();


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(Activity_Register.this, "An error occurred" + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }


        ) {

            @Override
            protected HashMap<String, String> getParams() {
                HashMap<String, String> params = new HashMap<>();
                params.put("email", email_address.getText().toString());
                params.put("id_no", edit_id.getText().toString());
                params.put("password", create_password.getText().toString());
                params.put("password_confirmation", confirm_password.getText().toString());
                params.put("fname", edit_firstname.getText().toString());
                params.put("lname", edit_lastname.getText().toString());
                params.put("phone", edit_phone.getText().toString());
                params.put("role_id", "2");
                return params;
            }

        };

        MySingleton.getInstance(this).addToRequestQueue(stringRequest);


    }
}
