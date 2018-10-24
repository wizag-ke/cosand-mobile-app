package wizag.com.supa.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.view.GravityCompat;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.google.firebase.iid.FirebaseInstanceId;

import java.util.List;
import java.util.concurrent.TimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import wizag.com.supa.MenusAdapter;
import wizag.com.supa.PermissionResults;
import wizag.com.supa.PermissionsAPI;
import wizag.com.supa.R;
import wizag.com.supa.SessionManager;
import wizag.com.supa.TopPermissions;

public class MenuActivity extends AppCompatActivity {
    CoordinatorLayout coordinatorLayout;
    MenusAdapter adapter;
    PermissionsService permissionsService;
    GridLayoutManager gridLayoutManager;
    LinearLayoutManager linearLayoutManager;
    RecyclerView rv;
    SessionManager session;
    String name = "Susan";

    AlertDialog.Builder dialogBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new SessionManager(getApplicationContext());
        setContentView(R.layout.menu);

        isNetworkConnectionAvailable();


        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);




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

    public void checkNetworkConnection(){
        AlertDialog.Builder builder =new AlertDialog.Builder(this);
        builder.setTitle("No internet Connection");
        builder.setMessage("Please turn on internet connection to continue");
        builder.setNegativeButton("close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    public boolean isNetworkConnectionAvailable(){
        ConnectivityManager cm =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnected();
        if(isConnected) {
            Log.d("Network", "Connected");
            return true;
        }
        else{
            checkNetworkConnection();
            Log.d("Network","Not Connected");
            return false;
        }
    }

    private void getPermissions() {
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
                } else if (response.code() >= 400 && response.code() < 599) {
                    getPermissions();
                }
            }

            @Override
            public void onFailure(Call<TopPermissions> call, Throwable t) {
                //progressBar.setVisibility(View.GONE);
                t.printStackTrace();
                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, " " + fetchErrorMessage(t), Snackbar.LENGTH_INDEFINITE)
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

    private List<PermissionResults> fetchResults(Response<TopPermissions> response) {
        TopPermissions topPermissions = response.body();
        return topPermissions.getListPermissionsM();
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

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    interface PermissionsService {

        @FormUrlEncoded
        @POST("jijinews/supaduka_permissions.php")
        Call<TopPermissions> name(
                @Field("name") String name);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        killActivity();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void killActivity() {
        finish();
    }





    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sign_out) {

            session.logoutUser();
//            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
