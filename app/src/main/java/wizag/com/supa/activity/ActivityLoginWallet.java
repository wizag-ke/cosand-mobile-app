package wizag.com.supa.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import wizag.com.supa.APIClient;
import wizag.com.supa.ApiInterface;
import wizag.com.supa.AuthUser;
import wizag.com.supa.MySingleton;
import wizag.com.supa.R;
import wizag.com.supa.Validation;

public class ActivityLoginWallet extends AppCompatActivity {
    public static final String ACCESS_TOKEN = "accessToken";
    public static final String REFRESH_TOKEN = "refreshToken";
    public static final String TOKEN_TYPE = "tokenType";
    String access_token, refresh_token, token_type;
    //String username = "admin@cosand.com";
    String username, password;
    //String password = "Qwerty123!";
    Button supaduka_login;
            //supaduka_signup

    CoordinatorLayout coordinatorLayout;
    EditText enter_username;
    TextInputEditText enter_password;
   // SharedPreferences prefs;
   // SessionManager session;
    private static final String SHARED_PREF_NAME = "profile";
    String get_profile_url = "http://sduka.wizag.biz/api/v1/profiles";
    String token;
    JSONArray role_array;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);


        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

        enter_username = findViewById(R.id.enter_username);
        enter_password = findViewById(R.id.enter_password);

        // Session Manager
//        session = new SessionManager(getApplicationContext());
//        HashMap<String, String> user = session.getUserDetails();
//        token = user.get("access_token");
//
//
//        if (session.isLoggedIn()) {
//            startActivity(new Intent(getApplicationContext(), Activity_Home.class));
//            finish();
//        }
//        checkUserRole();

//        getDriverProfile();
        //session.checkLogin();
        supaduka_login = findViewById(R.id.button_login);
        supaduka_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                /** Validation class will check the error and display the error on respective fields
                 but it won't resist the form submission, so we need to check again before submit
                 */

                if (loginValidation()) {
                    username = enter_username.getText().toString();
                    password = enter_password.getText().toString();
                    loginUser();
//                    getDriverProfile();
                } else {
                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, "Form contains errors", Snackbar.LENGTH_LONG)
                            .setAction("OK", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            });

                    snackbar.show();

                }
            }
        });


//        supaduka_signup = findViewById(R.id.supaduka_signup);
//        supaduka_signup.setEnabled(false);
//        supaduka_signup.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(ActivityLoginWallet.this, Activity_Register.class);
//                startActivity(intent);
////                finish();
//            }
//        });

       // prefs = this.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
        checkConnection();
    }

    private void checkConnection() {
        if (isNetworkConnected()) {
            confirmViews();
        } else {

            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "Internet not connected", Snackbar.LENGTH_LONG)
                    .setAction("RETRY", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            checkConnection();
                        }
                    });

            snackbar.show();
        }
    }

    private void confirmViews() {

        coordinatorLayout = findViewById(R.id.coordinatorLayout);

        enter_username.addTextChangedListener(new TextWatcher() {
            // after every change has been made to this editText, we would like to check validity
            public void afterTextChanged(Editable s) {
                Validation.isEmailAddress(enter_username, true);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });


        enter_password.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                // Validation.hasText(enter_password);
                Validation.isPassword(enter_password, true);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });


    }


    private boolean loginValidation() {
        boolean ret = true;

        if (!Validation.hasText(enter_password)) ret = false;
        if (!Validation.hasText(enter_username)) ret = false;
        return ret;
    }

    private void loginUser() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Signing in...");
        progressDialog.show();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIClient.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface service = retrofit.create(ApiInterface.class);
        // Call<AuthUser> call = service.loginUser("admin@cosand.com", "Qwerty123!","password", "2", "GEf81B8TnpPDibW4NKygaatvBG3RmbYSaJf8SZTA");
        Call<AuthUser> call = service.loginUser(username, password, "password", "2", "GEf81B8TnpPDibW4NKygaatvBG3RmbYSaJf8SZTA");
        call.enqueue(new Callback<AuthUser>() {
            @Override
            public void onResponse(Call<AuthUser> call, Response<AuthUser> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        AuthUser authUser = response.body();
                        access_token = authUser.getAccessToken();
                        refresh_token = authUser.getRefreshToken();
                        token_type = authUser.getTokenType();


//                        prefs.edit().putBoolean("oauth.loggedin", true).apply();
//                        prefs.edit().putString(ACCESS_TOKEN, access_token).apply();
//                        prefs.edit().putString(REFRESH_TOKEN, refresh_token).apply();
//                        prefs.edit().putString(TOKEN_TYPE, token_type).apply();
//
//                        //  String token = prefs.getString("access_token", ACCESS_TOKEN);
//
//                        session.createLoginSession(username, password, access_token);

                        SharedPreferences sp = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("password", password);
                        editor.apply();


                        getIndividualProfile();
                        getDriverProfile();
                        getCorporateProfile();
                        getTruckOwner();
                        getSupplierProfile();
                        startActivity(new Intent(getApplicationContext(), Activity_Wallet.class));
                        finish();

                    }

                } else if (response.code() >= 400 && response.code() < 599) {
                    Snackbar snackbar = Snackbar
                            // .make(coordinatorLayout, ""+response.code(), Snackbar.LENGTH_INDEFINITE)
                            .make(coordinatorLayout, "Wrong username or password", Snackbar.LENGTH_INDEFINITE)
                            .setAction("RETRY", new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    loginUser();
//                                    getDriverProfile();
                                }
                            });

                    snackbar.show();
                } else {
                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, "Something went wrong", Snackbar.LENGTH_INDEFINITE)
                            .setAction("RETRY", new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    loginUser();
//                                    getDriverProfile();
                                }
                            });

                    snackbar.show();
                }

            }

            @Override
            public void onFailure(Call<AuthUser> call, Throwable t) {
                progressDialog.dismiss();
                t.printStackTrace();
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, " " + fetchErrorMessage(t), Snackbar.LENGTH_INDEFINITE)
                        .setAction("RETRY", new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                loginUser();
                            }
                        });

                snackbar.show();

            }
        });
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    private String fetchErrorMessage(Throwable throwable) {
        String errorMsg = getResources().getString(R.string.error_msg_unknown);

        if (!isNetworkConnected()) {
            errorMsg = getResources().getString(R.string.error_msg_no_internet);
        } else if (throwable instanceof TimeoutException) {
            errorMsg = getResources().getString(R.string.error_msg_timeout);
        }

        return errorMsg;
    }

    void getDriverProfile() {
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
////                    pDialog.dismiss();
                    if (jsonObject != null) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        JSONObject user = data.getJSONObject("user");
                        String fname = user.getString("fname");
                        String lname = user.getString("lname");
                        String email = user.getString("email");
                        String phone = user.getString("phone");
                        String id_no = user.getString("id_no");


                        JSONArray roles = user.getJSONArray("roles");
                        SharedPreferences sp = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();

                        editor.putString("driver_fname", fname);
                        editor.putString("driver_lname", lname);
                        editor.putString("driver_email", email);
                        editor.putString("driver_phone", phone);
                        editor.putString("driver_id_no", id_no);
                        editor.putString("user_type", roles.toString());
                        editor.apply();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
//                    Toast.makeText(Activity_Login.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
////               pDialog.dismiss();
//                Toast.makeText(getApplicationContext(), "An Error Occurred while loading Driver profile" + error.getMessage(), Toast.LENGTH_LONG).show();

            }


        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
             //   session = new SessionManager(getApplicationContext());
             //   HashMap<String, String> user = session.getUserDetails();
              //  String accessToken = user.get("access_token");

              //  String bearer = "Bearer " + accessToken;
                Map<String, String> headersSys = super.getHeaders();
                Map<String, String> headers = new HashMap<String, String>();
                headersSys.remove("Authorization");
               // headers.put("Authorization", bearer);
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

    void getIndividualProfile() {
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
////                    pDialog.dismiss();
                    if (jsonObject != null) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        JSONObject user = data.getJSONObject("user");

                        String fname = user.getString("fname");
                        String lname = user.getString("lname");
                        String email = user.getString("email");
                        String phone = user.getString("phone");
                        String id_no = user.getString("id_no");

                        JSONArray roles = user.getJSONArray("roles");
                        SharedPreferences sp = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("individual_fname", fname);
                        editor.putString("individual_lname", lname);
                        editor.putString("individual_email", email);
                        editor.putString("individual_phone", phone);
                        editor.putString("individual_id_no", id_no);
                        editor.putString("user_type", roles.toString());
                        editor.apply();


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ActivityLoginWallet.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
//                pDialog.dismiss();
                Toast.makeText(getApplicationContext(), "An Error Occurred while loading Individual client profile" + error.getMessage(), Toast.LENGTH_LONG).show();

            }


        }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                //session = new SessionManager(getApplicationContext());
              //  HashMap<String, String> user = session.getUserDetails();
               // String accessToken = user.get("access_token");

               // String bearer = "Bearer " + accessToken;
                Map<String, String> headersSys = super.getHeaders();
                Map<String, String> headers = new HashMap<String, String>();
                headersSys.remove("Authorization");
               // headers.put("Authorization", bearer);
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


    void getCorporateProfile() {
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
//                    pDialog.dismiss();
                    if (jsonObject != null) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        JSONObject user = data.getJSONObject("user");

                        String fname = user.getString("fname");
                        String lname = user.getString("lname");
                        String email = user.getString("email");
                        String phone = user.getString("phone");
                        String id_no = user.getString("id_no");

                        JSONArray roles = user.getJSONArray("roles");
                        SharedPreferences sp = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("corporate_fname", fname);
                        editor.putString("corporate_lname", lname);
                        editor.putString("corporate_email", email);
                        editor.putString("corporate_phone", phone);
                        editor.putString("corporate_id_no", id_no);
                        editor.putString("user_type", roles.toString());
                        editor.apply();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
//                    Toast.makeText(Activity_Login.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
////               pDialog.dismiss();
//                Toast.makeText(getApplicationContext(), "An Error Occurred while loading Driver profile" + error.getMessage(), Toast.LENGTH_LONG).show();

            }


        }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
              //  session = new SessionManager(getApplicationContext());
               // HashMap<String, String> user = session.getUserDetails();
              //  String accessToken = user.get("access_token");

               // String bearer = "Bearer " + accessToken;
                Map<String, String> headersSys = super.getHeaders();
                Map<String, String> headers = new HashMap<String, String>();
                headersSys.remove("Authorization");
               // headers.put("Authorization", bearer);
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

    void getTruckOwner() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
//        pDialog.show();


        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://sduka.wizag.biz/api/v1/profiles", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
//                    pDialog.dismiss();
                    if (jsonObject != null) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        JSONObject user = data.getJSONObject("user");

                        String fname = user.getString("fname");
                        String lname = user.getString("lname");
                        String email = user.getString("email");
                        String phone = user.getString("phone");
                        String id_no = user.getString("id_no");

                        JSONArray role = user.getJSONArray("roles");
                        SharedPreferences sp = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();

                        editor.putString("truck_owner_fname", fname);
                        editor.putString("truck_owner_lname", lname);
                        editor.putString("truck_owner_email", email);
                        editor.putString("truck_owner_phone", phone);
                        editor.putString("user_type", role.toString());
                        editor.putString("truck_owner_id_no", id_no);
                        editor.apply();


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
//                    Toast.makeText(Activity_Login.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
////               pDialog.dismiss();
//                Toast.makeText(getApplicationContext(), "An Error Occurred while loading Driver profile" + error.getMessage(), Toast.LENGTH_LONG).show();

            }


        }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
              //  session = new SessionManager(getApplicationContext());
              //  HashMap<String, String> user = session.getUserDetails();
              //  String accessToken = user.get("access_token");

              //  String bearer = "Bearer " + accessToken;
                Map<String, String> headersSys = super.getHeaders();
                Map<String, String> headers = new HashMap<String, String>();
                headersSys.remove("Authorization");
              //  headers.put("Authorization", bearer);
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

    void getSupplierProfile() {
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
//                    pDialog.dismiss();
                    if (jsonObject != null) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        JSONObject user = data.getJSONObject("user");

                        String fname = user.getString("fname");
                        String lname = user.getString("lname");
                        String email = user.getString("email");
                        String phone = user.getString("phone");
                        String id_no = user.getString("id_no");

                        JSONArray role = user.getJSONArray("roles");
                        SharedPreferences sp = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();

                        editor.putString("supplier_fname", fname);
                        editor.putString("supplier_lname", lname);
                        editor.putString("supplier_email", email);
                        editor.putString("supplier_phone", phone);
                        editor.putString("supplier_id_no", id_no);
                        editor.putString("user_type", role.toString());
                        editor.apply();


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
//                    Toast.makeText(Activity_Login.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
////               pDialog.dismiss();
//                Toast.makeText(getApplicationContext(), "An Error Occurred while loading Driver profile" + error.getMessage(), Toast.LENGTH_LONG).show();

            }


        }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
              //  session = new SessionManager(getApplicationContext());
              //  HashMap<String, String> user = session.getUserDetails();
              //  String accessToken = user.get("access_token");

               // String bearer = "Bearer " + accessToken;
                Map<String, String> headersSys = super.getHeaders();
                Map<String, String> headers = new HashMap<String, String>();
                headersSys.remove("Authorization");
               // headers.put("Authorization", bearer);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        builder.setTitle("info");
        builder.setMessage("Do you want to exit from the App?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();

    }


}
