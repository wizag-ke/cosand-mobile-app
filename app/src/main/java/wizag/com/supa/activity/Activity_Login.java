package wizag.com.supa.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.ArraySet;
import android.view.View;
import android.widget.ArrayAdapter;
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

import junit.runner.Version;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import wizag.com.supa.APIClient;
import wizag.com.supa.ApiInterface;
import wizag.com.supa.AuthUser;
import wizag.com.supa.BuildConfig;
import wizag.com.supa.MySingleton;
import wizag.com.supa.R;
import wizag.com.supa.SessionManager;
import wizag.com.supa.Validation;

public class Activity_Login extends AppCompatActivity {
    public static final String ACCESS_TOKEN = "accessToken";
    public static final String REFRESH_TOKEN = "refreshToken";
    public static final String TOKEN_TYPE = "tokenType";
    String access_token, refresh_token, token_type;
    //String username = "admin@cosand.com";
    String username, password;
    //String password = "Qwerty123!";
    private static final String SHARED_PREF_NAME2 = "handshake";

    Button supaduka_login, supaduka_signup;
    CoordinatorLayout coordinatorLayout;
    EditText enter_username;
    TextInputEditText enter_password;
    SharedPreferences prefs;
    SessionManager session;
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
        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        token = user.get("access_token");


        if (session.isLoggedIn()) {
            startActivity(new Intent(getApplicationContext(), Activity_Home.class));
            finish();
            SharedPreferences prefs = getSharedPreferences(SHARED_PREF_NAME2, MODE_PRIVATE);
            String restoredText = prefs.getString("site_id", null);
            if (restoredText != null) {
                Intent intent=new Intent(getApplicationContext(), Activity_HandShake.class);
//                String name = prefs.getString("name", "No name defined");//"No name defined" is the default value.
//                int idName = prefs.getInt("idName", 0); //0 is the default value.
            }
        }
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


        supaduka_signup = findViewById(R.id.supaduka_signup);
        supaduka_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_Login.this, Activity_Register.class);
                startActivity(intent);
//                finish();
            }
        });

        prefs = this.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);
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


                        prefs.edit().putBoolean("oauth.loggedin", true).apply();
                        prefs.edit().putString(ACCESS_TOKEN, access_token).apply();
                        prefs.edit().putString(REFRESH_TOKEN, refresh_token).apply();
                        prefs.edit().putString(TOKEN_TYPE, token_type).apply();

                        //  String token = prefs.getString("access_token", ACCESS_TOKEN);

                        session.createLoginSession(username, password, access_token);

                        SharedPreferences sp = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("password", password);
                        editor.apply();


//                        getIndividualProfile();
                       /* getDriverProfile();
                        getCorporateProfile();
                        getTruckOwner();
                        getSupplierProfile();*/
                        Intent intent=new Intent(getApplicationContext(), Activity_Home.class);
                        intent.putExtra("USERNAME", username);
                        startActivity(intent);
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
//                session.logoutUser();
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
