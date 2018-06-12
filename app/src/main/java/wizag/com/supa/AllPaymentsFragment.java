package wizag.com.supa;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.concurrent.TimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import wizag.com.supa.ModelResults.PaymentResults;
import wizag.com.supa.ModelResults.TripsResults;
import wizag.com.supa.TopModels.TopPayments;
import wizag.com.supa.TopModels.TopTrips;

/**
 * Created by User on 12/05/2018.
 */

public class AllPaymentsFragment extends Fragment {
    CoordinatorLayout coordinatorLayout;
    PaymentsAdapter adapter;
    PaymentsService tripsService;
    LinearLayoutManager linearLayoutManager;
    RecyclerView rv;

    String name = "Susan";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_all_trips_fragment,container,false);

        coordinatorLayout = (CoordinatorLayout) v.findViewById(R.id.coordinatorLayout);
        rv = (RecyclerView) v.findViewById(R.id.main_recycler);
        adapter = new PaymentsAdapter(getActivity().getApplicationContext());

        linearLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(adapter);

        //init service and load data
        tripsService = PermissionsAPI.getPermissions().create(PaymentsService.class);
        if (isNetworkConnected()) {
            getPermissions();

        }
        return v;

        // return inflater.inflate(R.layout.fragment_all_trips_fragment,container,false);
    }

    private void getPermissions(){
        //progressBar.setVisibility(View.VISIBLE);
        Call<TopPayments> callPermissions = tripsService.name(String.valueOf(name));
        callPermissions.enqueue(new Callback<TopPayments>() {
            @Override
            public void onResponse(Call<TopPayments> call, Response<TopPayments> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {


                        List<PaymentResults> results = fetchResults(response);
                        adapter.addRecycler(results);
                        adapter.notifyDataSetChanged();


                    }
                }

                else if (response.code() >= 400 && response.code() < 599) {
                    getPermissions();
                }
            }

            @Override
            public void onFailure(Call<TopPayments> call, Throwable t) {
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

    private List<PaymentResults> fetchResults(Response<TopPayments> response){
        TopPayments topPermissions = response.body();
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
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    interface PaymentsService{

        @FormUrlEncoded
        @POST("jijinews/supaduka_payments.php")
        Call<TopPayments> name(
                @Field("name") String name);
    }
}