package wizag.com.supa.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import wizag.com.supa.MySingleton;
import wizag.com.supa.R;
import wizag.com.supa.SessionManager;

public class Activity_Questionaire extends AppCompatActivity {
    ArrayList<String> Type;
    LinearLayout layout;
    String qn_response = "";
    String order_id, site_id;
    String qn_id;
    Button submit;
    String resp, text_response;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionaire);

       /* Bundle extras = getIntent().getExtras();
        if (extras != null) {
            site_id = extras.getString("site_id");

            Toast.makeText(this, site_id, Toast.LENGTH_SHORT).show();


        }*/

        submit = findViewById(R.id.submit);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        layout = findViewById(R.id.qns);


        Type = new ArrayList<>();

        loadQuestions();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QnResponse();
            }
        });


    }


    private void loadQuestions() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        pDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://sduka.wizag.biz/api/v1/sites/questionnaire", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    pDialog.dismiss();
                    if (jsonObject != null) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        JSONArray materialTypes = data.getJSONArray("questions");

                        for (int p = 0; p < materialTypes.length(); p++) {
                            JSONObject materials_object = materialTypes.getJSONObject(p);

                            qn_id = materials_object.getString("id");
                            String answer = materials_object.getString("question");
                            String type = materials_object.getString("answer_type");


                            if (materialTypes != null) {

                                if (Type.contains(answer)) {


                                } else {
                                    if (type.equalsIgnoreCase("MULTIPLE_CHOICE")) {
                                        TextView dynamicTextView = new TextView(getApplicationContext());
                                        dynamicTextView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                                LinearLayout.LayoutParams.WRAP_CONTENT));
                                        dynamicTextView.setTextSize(18);
                                        dynamicTextView.setPadding(16, 16, 16, 16);
                                        dynamicTextView.setTypeface(Typeface.DEFAULT_BOLD);
                                        dynamicTextView.setTextColor(Color.parseColor("#CD570D"));
                                        dynamicTextView.setText(answer);

                                        /*dynamic radio buttons*/
                                        RadioGroup group = new RadioGroup(getApplicationContext());
                                        group.setOrientation(RadioGroup.HORIZONTAL);
                                        RadioButton btn1 = new RadioButton(getApplicationContext());
                                        btn1.setText("Yes");
                                        group.addView(btn1);
                                        RadioButton btn2 = new RadioButton(getApplicationContext());
                                        group.addView(btn2);
                                        btn2.setText("No");

                                        /*get checked radio buttons*/
                                        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                            public void onCheckedChanged(RadioGroup rg, int checkedId) {
                                                for (int i = 0; i < rg.getChildCount(); i++) {
                                                    RadioButton btn = (RadioButton) rg.getChildAt(i);
                                                    if (btn.getId() == checkedId) {
                                                        text_response = String.valueOf(btn.getText());
                                                        qn_response = text_response;

                                                        // do something with text
                                                        return;
                                                    }
                                                }
                                            }
                                        });

                                        /*dynamic edittext*/
                                        EditText dynamicEdittext = new EditText(getApplicationContext());
                                        dynamicTextView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                                LinearLayout.LayoutParams.WRAP_CONTENT));
                                        dynamicEdittext.setTextSize(18);
                                        dynamicEdittext.setPadding(16, 16, 16, 16);
                                        dynamicEdittext.setTypeface(Typeface.DEFAULT_BOLD);
                                        dynamicEdittext.setTextColor(Color.parseColor("#000000"));
                                        dynamicEdittext.setText(answer);


                                        layout.addView(dynamicTextView);
                                        layout.addView(group);

                                        //Toast.makeText(getApplicationContext(), "data\n" + size.getJSONObject(m).getString("size"), Toast.LENGTH_SHORT).show();
                                        Type.add(answer);
//                                    Type.add(materialTypes.getJSONObject(p).getString("id"));
                                    } else if (type.equalsIgnoreCase("OPEN")) {

                                        TextView dynamicTextView = new TextView(getApplicationContext());
                                        dynamicTextView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                                LinearLayout.LayoutParams.WRAP_CONTENT));
                                        dynamicTextView.setTextSize(18);
                                        dynamicTextView.setPadding(16, 16, 16, 16);
                                        dynamicTextView.setTypeface(Typeface.DEFAULT_BOLD);
                                        dynamicTextView.setTextColor(Color.parseColor("#CD570D"));
                                        dynamicTextView.setText(answer);
                                        layout.addView(dynamicTextView);


                                        /*dynamic edittext*/
                                        EditText dynamicEdittext = new EditText(getApplicationContext());
                                        dynamicTextView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                                LinearLayout.LayoutParams.WRAP_CONTENT));
                                        dynamicEdittext.setTextSize(18);
                                        dynamicEdittext.setPadding(16, 16, 16, 16);
                                        dynamicEdittext.setTypeface(Typeface.DEFAULT_BOLD);
                                        dynamicEdittext.setTextColor(Color.parseColor("#000000"));

                                        resp = dynamicEdittext.getText().toString();
                                        Toast.makeText(Activity_Questionaire.this, resp, Toast.LENGTH_SHORT).show();
                                        layout.addView(dynamicEdittext);
                                        /*get text from dynamic edittext*/
                                        qn_response = resp;

                                        Type.add(answer);
                                    }


                                }

                            }


                        }


                    }
//

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(getApplicationContext(), "An Error Occurred" + error.getMessage(), Toast.LENGTH_LONG).show();
                pDialog.hide();
            }


        }) {


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


        MySingleton.getInstance(this).addToRequestQueue(stringRequest);


        int socketTimeout = 30000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        requestQueue.add(stringRequest);


    }


    public void QnResponse() {
        SharedPreferences sp = getSharedPreferences("site_id", MODE_PRIVATE);
         site_id = sp.getString("site_id", null);


        SharedPreferences sp_order = getSharedPreferences("confirm_notification", MODE_PRIVATE);
        order_id = sp_order.getString("order_id", null);

        Toast.makeText(this, site_id+"\n"+order_id, Toast.LENGTH_SHORT).show();

//        Toast.makeText(this, site_id, Toast.LENGTH_SHORT).show();
        com.android.volley.RequestQueue queue = Volley.newRequestQueue(Activity_Questionaire.this);
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://sduka.wizag.biz/api/v1/sites/" + 101 + "/feedback",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            pDialog.dismiss();
                            String message = jsonObject.getString("message");
                            String status = jsonObject.getString("status");
                            if (status.equalsIgnoreCase("success")) {
                                Toast.makeText(Activity_Questionaire.this, message, Toast.LENGTH_LONG).show();

                                Intent intent = new Intent(getApplicationContext(), Activity_Home.class);
                                startActivity(intent);
                                finish();


                            }


                            if (status.equalsIgnoreCase("fail")) {

                                Toast.makeText(Activity_Questionaire.this, message, Toast.LENGTH_LONG).show();


                            } else if (status.equalsIgnoreCase("error")) {
                                Toast.makeText(Activity_Questionaire.this, message, Toast.LENGTH_LONG).show();

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //Toast.makeText(Activity_Questionaire.this, "", Toast.LENGTH_SHORT).show();
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(Activity_Questionaire.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                pDialog.dismiss();
            }
        }) {
            //adding parameters to the request
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("question_id", qn_id);
                params.put("answer", qn_response);
                params.put("order_id", order_id);
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
