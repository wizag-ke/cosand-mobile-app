package wizag.com.supa;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.List;
import java.util.concurrent.TimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class MenuActivity extends AppCompatActivity {
    CoordinatorLayout coordinatorLayout;
    MenusAdapter adapter;
    PermissionsService permissionsService;
    GridLayoutManager gridLayoutManager;
    LinearLayoutManager linearLayoutManager;
    RecyclerView rv;
    SessionManager session;
    String name = "Susan";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // name = getIntent().getStringExtra("Name");
        setContentView(R.layout.menu);

        // Session Manager
       // session = new SessionManager(getApplicationContext());

//session.checkLogin();
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        rv = (RecyclerView) findViewById(R.id.main_recycler);
        //progressBar = (ProgressBar) findViewById(R.id.main_progress);
        adapter = new MenusAdapter(this);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        rv.setLayoutManager(mLayoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(adapter);


        //linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        //rv.setLayoutManager(linearLayoutManager);
        //rv.setItemAnimator(new DefaultItemAnimator());
       // rv.setAdapter(adapter);

        //init service and load data
        permissionsService = PermissionsAPI.getPermissions().create(PermissionsService.class);
        if (isNetworkConnected()) {
            getPermissions();

        }

    }

    private void getPermissions(){
        //progressBar.setVisibility(View.VISIBLE);
        Call<TopPermissions> callPermissions = permissionsService.name(String.valueOf(name));
        callPermissions.enqueue(new Callback<TopPermissions>() {
            @Override
            public void onResponse(Call<TopPermissions> call, Response<TopPermissions> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {


                        List<PermissionResults> results = fetchResults(response);
                        adapter.addRecycler(results);
                        adapter.notifyDataSetChanged();


                    }
                }

                else if (response.code() >= 400 && response.code() < 599) {
                    getPermissions();
                }
            }

            @Override
            public void onFailure(Call<TopPermissions> call, Throwable t) {
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

    private List<PermissionResults> fetchResults(Response<TopPermissions> response){
        TopPermissions topPermissions = response.body();
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

    interface PermissionsService{

        @FormUrlEncoded
        @POST("jijinews/supaduka_permissions.php")
        Call<TopPermissions> name(
                @Field("name") String name);
    }
}
