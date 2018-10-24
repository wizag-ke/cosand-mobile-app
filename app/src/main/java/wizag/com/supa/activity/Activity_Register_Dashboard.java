package wizag.com.supa.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import wizag.com.supa.R;
import wizag.com.supa.SessionManager;

public class Activity_Register_Dashboard extends AppCompatActivity {
    CardView driver, truck_owner, individual_client, corporate_client, supplier;
    SessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_dashboard);
        Typeface fontAwesomeFont = Typeface.createFromAsset(getAssets(), "fontawesome_webfont.ttf");

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        session = new SessionManager(getApplicationContext());
        supplier = findViewById(R.id.supplier);
        driver = findViewById(R.id.driver);
        corporate_client = findViewById(R.id.corporate_client);
        individual_client = findViewById(R.id.individual_client);
        truck_owner = findViewById(R.id.truck_owner);
        driver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Activity_Driver_Register.class));
            }
        });

        supplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Activity_Supplier_Register.class));
            }
        });

        truck_owner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Activity_Truck_Owner.class));
            }
        });

        individual_client.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Activity_Individual_Client.class));
            }
        });


        corporate_client.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Activity_Corporate_Client.class));
            }
        });
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
            finish();

//            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
