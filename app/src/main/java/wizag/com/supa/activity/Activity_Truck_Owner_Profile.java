package wizag.com.supa.activity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;

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
import wizag.com.supa.adapter.Adapter_Trucks;
import wizag.com.supa.models.Trucks;

public class Activity_Truck_Owner_Profile extends AppCompatActivity implements View.OnClickListener {
    ViewFlipper flipper;
    EditText fname, lname, id_no, email, mobile_no;
    Button next;
    EditText company_name, location, kra_pin, company_mobile_no;
    Button previous, previous_list, next_trucks;
    SessionManager session;
    ListView listView;
    List<Trucks> trucksList;
    Adapter_Trucks adapter_trucks;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_truck_owner_user_profile);

        trucksList = new ArrayList<>();
        adapter_trucks = new Adapter_Trucks(this, trucksList);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);


        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter_trucks);


        SharedPreferences sp = getSharedPreferences("profile", MODE_PRIVATE);
        String truck_owner_fname = sp.getString("truck_owner_fname", null);
        String truck_owner_lname = sp.getString("truck_owner_lname", null);
        String truck_owner_email = sp.getString("truck_owner_email", null);
        String truck_owner_phone = sp.getString("truck_owner_phone", null);
        String truck_owner_id_no = sp.getString("truck_owner_id_no", null);
        String truck_owner_company_name = sp.getString("truck_owner_company_name", null);
        String truck_owner_company_location = sp.getString("truck_owner_company_location", null);
        String truck_owner_company_kra_pin = sp.getString("truck_owner_company_kra_pin", null);
        String truck_owner_company_phone = sp.getString("truck_owner_company_phone", null);

        flipper = findViewById(R.id.flipper);
        fname = findViewById(R.id.fname);
        lname = findViewById(R.id.lname);
        id_no = findViewById(R.id.id_no);
        email = findViewById(R.id.email);
        mobile_no = findViewById(R.id.mobile_no);
        company_name = findViewById(R.id.company_name);
        location = findViewById(R.id.location);
        kra_pin = findViewById(R.id.kra_pin);
        company_mobile_no = findViewById(R.id.truck_owner_co_mobile_no);

        next = findViewById(R.id.next);
//        previous_list = findViewById(R.id.previous_list);
        previous = findViewById(R.id.previous);
        next_trucks = findViewById(R.id.next_trucks);
        next.setOnClickListener(this);
        next_trucks.setOnClickListener(this);

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flipper.showPrevious();
            }
        });

       /* previous_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flipper.showPrevious();
            }
        });*/

        fname.setText(truck_owner_fname);
        lname.setText(truck_owner_lname);
        email.setText(truck_owner_email);
        mobile_no.setText(truck_owner_phone);
        id_no.setText(truck_owner_id_no);
        location.setText(truck_owner_company_location);
        company_name.setText(truck_owner_company_name);
        kra_pin.setText(truck_owner_company_kra_pin);
        company_mobile_no.setText(truck_owner_company_phone);

        getTruckOwner();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.next:
                flipper.showNext();
                break;

            case R.id.next_trucks:
                flipper.showNext();
                break;
        }
    }


    private void getTruckOwner() {
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
                        JSONArray trucks = user.getJSONArray("trucks");
                        for (int i = 0; i < trucks.length(); i++) {
                            JSONObject heroObject = trucks.getJSONObject(i);
                            JSONObject tonnage = heroObject.getJSONObject("tonnage");


                           /* Trucks hero = new Trucks(heroObject.getString("plate_no"),
                                    heroObject.getString("year"),
                                    heroObject.getString("make"),
                                    heroObject.getString("model"),
                                    tonnage.getString("tonnage"));
                            */

                            Trucks hero = new Trucks("KCN 112P",
                                   "2012",
                                    "Isuzu",
                                    "FH",
                                    "20 Tonnes");


                            Trucks hero1 = new Trucks("KBN 132P",
                                    "2007",
                                   "Isuzu",
                                    "NPR",
                                    "12 Tonnes");






                            //adding the hero to herolist
                            trucksList.add(hero);
                            trucksList.add(hero1);
                            adapter_trucks.notifyDataSetChanged();
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
//                    Toast.makeText(Activity_Truck_Owner_Profile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
//               pDialog.dismiss();
//                Toast.makeText(getApplicationContext(), "An Error Occurred while loading Driver profile" + error.getMessage(), Toast.LENGTH_LONG).show();

            }


        }) {


            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                session = new SessionManager(getApplicationContext());
                HashMap<String, String> user = session.getUserDetails();
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


        //adding the string request to request queue
        requestQueue.add(stringRequest);
    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}


