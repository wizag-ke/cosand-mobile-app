package wizag.com.supa;

import android.content.Context;
import android.net.ConnectivityManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;
import java.util.concurrent.TimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import wizag.com.supa.ModelResults.TripsResults;
import wizag.com.supa.TopModels.TopTrips;

public class TripsActivity extends AppCompatActivity {

    CoordinatorLayout coordinatorLayout;
    TripsAdapter adapter;
    TripsService tripsService;
    LinearLayoutManager linearLayoutManager;
    RecyclerView rv;

    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // name = getIntent().getStringExtra("Name");
        name = "Susan";
        setContentView(R.layout.activity_trips);


        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        rv = (RecyclerView) findViewById(R.id.main_recycler);
        adapter = new TripsAdapter(this);

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(adapter);

        //init service and load data
        tripsService = PermissionsAPI.getPermissions().create(TripsService.class);
        if (isNetworkConnected()) {
            getPermissions();

        }

    }

    private void getPermissions(){
        //progressBar.setVisibility(View.VISIBLE);
        Call<TopTrips> callPermissions = tripsService.name(String.valueOf(name));
        callPermissions.enqueue(new Callback<TopTrips>() {
            @Override
            public void onResponse(Call<TopTrips> call, Response<TopTrips> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {


                        List<TripsResults> results = fetchResults(response);
                        adapter.addRecycler(results);
                        adapter.notifyDataSetChanged();


                    }
                }

                else if (response.code() >= 400 && response.code() < 599) {
                    getPermissions();
                }
            }

            @Override
            public void onFailure(Call<TopTrips> call, Throwable t) {
                //progressBar.setVisibility(View.GONE);
                t.printStackTrace();
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, " "+fetchErrorMessage(t), Snackbar.LENGTH_INDEFINITE)
                        .setAction("RETRY", new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                getPermissions();
                            }
                        });

                snackbar.show();
            }
        });
    }

    private List<TripsResults> fetchResults(Response<TopTrips> response){
        TopTrips topPermissions = response.body();
        return topPermissions.getListPermissionsM();
    }



    private String fetchErrorMessage(Throwable throwable){
        String errorMsg = getResources().getString(R.string.error_msg_unknown);

        if (!isNetworkConnected()){
            errorMsg = getResources().getString(R.string.error_msg_no_internet);
        }
        else if (throwable instanceof TimeoutException) {
            errorMsg = getResources().getString(R.string.error_msg_timeout);
        }

        return errorMsg;
    }

    private boolean isNetworkConnected(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    interface TripsService{

        @FormUrlEncoded
        @POST("jijinews/supaduka_trips.php")
        Call<TopTrips> name(
                @Field("name") String name);
    }
}
