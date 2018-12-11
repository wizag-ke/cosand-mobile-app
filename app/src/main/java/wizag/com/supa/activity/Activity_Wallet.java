package wizag.com.supa.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.design.widget.Snackbar;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import wizag.com.supa.CurrencyFormat;
import wizag.com.supa.MySingleton;
import wizag.com.supa.R;
import wizag.com.supa.SessionManager;
import wizag.com.supa.adapter.Adapter_Transaction;
import wizag.com.supa.models.Model_Transaction;

public class Activity_Wallet extends AppCompatActivity {
    TextView balance;
    Button deposit, withdraw, cancel;
    String LoadWalletUrl = "http://sduka.wizag.biz/api/v1/wallet/load";
    String LoadTransactions = "http://sduka.wizag.biz/api/v1/wallet/summary";
    SessionManager sessionManager;
    String token, amount_txt, email_txt, phone_txt, prefs_phone;
    RecyclerView recycler_view;
    private RecyclerView.LayoutManager layoutManager;
    private List<Model_Transaction> listTransactions;
    Adapter_Transaction adapter_transaction;
    String date, balance_txt;
    private static final String SHARED_PREF_NAME = "wallet";
    String from_txt, to_txt;
    EditText from, to;
    AlertDialog alertDialog = null;
    EditText password;
    String flag_type = "";
    String top_up_cost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);


//        AuthWallet();
        SharedPreferences prefs_orders = getSharedPreferences("profile", MODE_PRIVATE);
        String driver_code_orders = prefs_orders.getString("driver_code", null);
        prefs_phone = prefs_orders.getString("phone", null);
//        FilterDialog();
//        Toast.makeText(this, phone_txt, Toast.LENGTH_SHORT).show();
        date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        recycler_view = findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recycler_view.setLayoutManager(layoutManager);
        listTransactions = new ArrayList<>();


        //initializing our adapter
        adapter_transaction = new Adapter_Transaction((ArrayList<Model_Transaction>) listTransactions, this);

        //Adding adapter to recyclerview
        recycler_view.setAdapter(adapter_transaction);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        sessionManager = new SessionManager(getApplicationContext());
        HashMap<String, String> user = sessionManager.getUserDetails();
        token = user.get("access_token");


        balance = findViewById(R.id.balance);
        balance.setFilters(new InputFilter[]{new CurrencyFormat()});

        deposit = (Button) findViewById(R.id.deposit);
        withdraw = (Button) findViewById(R.id.withdraw);

        deposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChangeLangDialog();

            }
        });

        withdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        getBalance();
        loadTransactions();

    }

    public void showChangeLangDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.layout_amount, null);
        dialogBuilder.setView(dialogView);
        final EditText amount = dialogView.findViewById(R.id.load_amount);
        final TextView phone = dialogView.findViewById(R.id.phone);
        final TextView email = dialogView.findViewById(R.id.email);
        amount.setInputType(InputType.TYPE_CLASS_NUMBER);

        SharedPreferences prefs_orders = getSharedPreferences("profile", MODE_PRIVATE);
        String phone_prefs = prefs_orders.getString("reg_phone", null);
        String email_prefs = prefs_orders.getString("reg_email", null);
        phone.setText(phone_prefs);
        email.setText(email_prefs);

        /*if request is initiated from quotation*/
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            flag_type = extras.getString("flag_type");
            if (flag_type.equalsIgnoreCase("top_up")) ;
            top_up_cost = extras.getString("amount");
//            Toast.makeText(this, flag_type, Toast.LENGTH_LONG).show();
            amount.setText("Ksh:\t" + top_up_cost);
            amount.setFilters(new InputFilter[]{new CurrencyFormat()});

        }


//        Toast.makeText(Activity_Wallet.this, prefs_phone, Toast.LENGTH_SHORT).show();

        dialogBuilder.setTitle("Load Wallet");
        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                amount_txt = amount.getText().toString();
                phone_txt = phone.getText().toString();
                email_txt = email.getText().toString();

               /* String phone_254 = String.valueOf(phone_txt.charAt(0));
                if (phone_254.equalsIgnoreCase("0")) {
                    StringBuilder ipay_phone = new StringBuilder(phone_txt);
                    ipay_phone.deleteCharAt(0);
                    ipay_phone.append("254");
                    phone_txt = ipay_phone.toString();

                }
*/

                if (amount_txt.isEmpty()) {
                    Toast.makeText(Activity_Wallet.this, "Enter amount to proceed", Toast.LENGTH_SHORT).show();
                } /*else if (flag_type.equalsIgnoreCase("top_up")) {

//                    topUpWallet();
                    *//*we moved to iPay*//*
                    Intent ipay = new Intent(getApplicationContext(), Wallet_Ipay.class);
                    ipay.putExtra("phone", phone_txt);
                    ipay.putExtra("email", email_txt);
                    ipay.putExtra("amount", amount_txt);
                    startActivity(ipay);
                    finish();

                    *//*delete value from shared prefs*//*


                }*/ else {
//                    loadWallet();
                    Intent ipay = new Intent(getApplicationContext(), Wallet_Ipay.class);
                    ipay.putExtra("phone", phone_txt);
                    ipay.putExtra("email", email_txt);
                    ipay.putExtra("amount", amount_txt);
                    startActivity(ipay);
                    finish();
                }


            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }


    private void loadWallet() {
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(Activity_Wallet.this);
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, LoadWalletUrl,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            pDialog.dismiss();
//                            JSONObject data = jsonObject.getJSONObject("data");
                            String message = jsonObject.getString("message");
                            String status = jsonObject.getString("status");

//                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                            if (status.equalsIgnoreCase("success")) {
                                Toast.makeText(Activity_Wallet.this, message, Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), Activity_Home.class));
                                finish();
                            }

                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            for (int k = 0; k < jsonArray.length(); k++) {
                                String data_message = jsonArray.getString(k);

                                if (status.equalsIgnoreCase("fail")) {
                                    Toast.makeText(Activity_Wallet.this, data_message, Toast.LENGTH_LONG).show();
                                }
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //Toast.makeText(Activity_Wallet.this, "", Toast.LENGTH_SHORT).show();
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
                params.put("amount", amount_txt);
                params.put("phoneNumber", phone_txt);

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


    private void topUpWallet() {
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(Activity_Wallet.this);
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, LoadWalletUrl,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            pDialog.dismiss();
//                            JSONObject data = jsonObject.getJSONObject("data");
                            String message = jsonObject.getString("message");
                            String status = jsonObject.getString("status");

//                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                            if (status.equalsIgnoreCase("success")) {
                                Toast.makeText(Activity_Wallet.this, message, Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), Activity_Buy_Quotation.class));
                                finish();
                            }

                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            for (int k = 0; k < jsonArray.length(); k++) {
                                String data_message = jsonArray.getString(k);

                                if (status.equalsIgnoreCase("fail")) {
                                    Toast.makeText(Activity_Wallet.this, data_message, Toast.LENGTH_LONG).show();
                                }
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //Toast.makeText(Activity_Wallet.this, "", Toast.LENGTH_SHORT).show();
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
                params.put("amount", amount_txt);
                params.put("phoneNumber", phone_txt);

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


    private void loadTransactions() {
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(Activity_Wallet.this);
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, LoadTransactions,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            pDialog.dismiss();
                            String message = jsonObject.getString("message");
                            String status = jsonObject.getString("status");
                            JSONObject data = jsonObject.getJSONObject("data");
                            JSONArray transactions = data.getJSONArray("transactions");
                            for (int p = 0; p < transactions.length(); p++) {
                                Model_Transaction model_transaction = new Model_Transaction();
                                JSONObject transaction_items = transactions.getJSONObject(p);
                                if (transaction_items == null) {
                                    Toast.makeText(Activity_Wallet.this, "No transaction history available", Toast.LENGTH_SHORT).show();
                                } else {
                                    String date = transaction_items.getString("date");
                                    String description = transaction_items.getString("description");
                                    String amount = transaction_items.getString("amount");
                                    String type = transaction_items.getString("type");
                                    String transaction_status = transaction_items.getString("status");

                                    if (transaction_status.equalsIgnoreCase("1")) {
                                        transaction_status = "Pending";
                                    } else if (transaction_status.equalsIgnoreCase("2")) {
                                        transaction_status = "Cancelled";
                                    } else if (transaction_status.equalsIgnoreCase("3")) {
                                        transaction_status = "Completed";
                                    } else if (transaction_status.equalsIgnoreCase("4")) {
                                        transaction_status = "Failed";
                                    }


//                                    Toast.makeText(Activity_Wallet.this, date, Toast.LENGTH_SHORT).show();
                                    if (type.equalsIgnoreCase("1")) {
                                        type = "Deposit";
                                    } else if (type.equalsIgnoreCase("2")) {
                                        type = "Payment";
                                    } else if (type.equalsIgnoreCase("3")) {
                                        type = "Commission";
                                    } else if (type.equalsIgnoreCase("4")) {
                                        type = "Withdrawal";
                                    }

                                    model_transaction.setAmount("Ksh." + amount);
                                    model_transaction.setDescription(description);
                                    model_transaction.setDate(date);
                                    model_transaction.setType(type);
                                    model_transaction.setStatus(transaction_status);


                                }

                                if (listTransactions.contains(date)) {
                                    /*do nothing*/
                                } else {
                                    listTransactions.add(model_transaction);
                                }


                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        adapter_transaction.notifyDataSetChanged();

                        //Toast.makeText(Activity_Wallet.this, "", Toast.LENGTH_SHORT).show();
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


                Toast.makeText(getApplicationContext(), "Data could not be loaded", Snackbar.LENGTH_LONG).show();
                pDialog.dismiss();
            }
        }) {
            //adding parameters to the request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("start_date", "2018-10-01");
                params.put("end_date", date);

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

    private void getBalance() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();


        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://sduka.wizag.biz/api/v1/wallet/balance", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    pDialog.dismiss();
                    if (jsonObject != null) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        balance_txt = data.getString("balance");

                        balance.setText(balance_txt);

                        SharedPreferences sp = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("balance", balance_txt);
                        editor.apply();
//                        balance.addTextChangedListener(new Currency_Format(balance));
//                          Toast.makeText(Activity_Wallet.this, balance_txt, Toast.LENGTH_SHORT).show();

                    }


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


    private void AuthWallet() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(Activity_Wallet.this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.wallet_login, null);
        password = dialogView.findViewById(R.id.password);

        final Button proceed = dialogView.findViewById(R.id.proceed);

        builder.setCancelable(false);
        builder.setView(dialogView);
        builder.setCancelable(false);


        proceed.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //allow access to the wallet after validating password
                SharedPreferences sp = getSharedPreferences("profile", MODE_PRIVATE);
                String password_txt = sp.getString("password", null);
                if (password_txt.equalsIgnoreCase(password.getText().toString())) {
                    startActivity(new Intent(getApplicationContext(), Activity_Wallet.class));
                    finish();

                } else {
                    WrongPassword();
                }


            }

        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                finish();
                alertDialog.dismiss();
            }
        });
        alertDialog = builder.create();

        alertDialog.show();
    }


    public void WrongPassword() {

        AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        // Set Custom Title
        TextView title = new TextView(this);
        // Title Properties
        title.setText("Info");
        title.setPadding(20, 20, 20, 20);   // Set Position
        title.setGravity(Gravity.CENTER);

        title.setTextColor(Color.BLACK);
        title.setTextSize(20);
        alertDialog.setCustomTitle(title);

        // Set Message
        TextView msg = new TextView(this);
        // Message Properties
        msg.setText("You have Entered a Wrong Password");
        msg.setGravity(Gravity.CENTER_HORIZONTAL);
        msg.setTextColor(Color.BLACK);
        msg.setTextSize(18);
        alertDialog.setView(msg);


        // Set Button
        // you can more buttons
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();

            }
        });


        new Dialog(getApplicationContext());
        alertDialog.show();

        // Set Properties for OK Button
        final Button okBT = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        LinearLayout.LayoutParams neutralBtnLP = (LinearLayout.LayoutParams) okBT.getLayoutParams();
        neutralBtnLP.gravity = Gravity.FILL_HORIZONTAL;
        okBT.setPadding(50, 10, 10, 10);   // Set Position
        okBT.setTextColor(Color.BLUE);
        okBT.setLayoutParams(neutralBtnLP);

        final Button cancelBT = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        LinearLayout.LayoutParams negBtnLP = (LinearLayout.LayoutParams) okBT.getLayoutParams();
        negBtnLP.gravity = Gravity.FILL_HORIZONTAL;
        cancelBT.setTextColor(Color.RED);
        cancelBT.setLayoutParams(negBtnLP);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), Activity_Home.class);
        startActivity(intent);

    }
}
