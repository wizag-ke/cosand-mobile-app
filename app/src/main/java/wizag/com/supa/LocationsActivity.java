package wizag.com.supa;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LocationsActivity extends AppCompatActivity {
    Button add_locations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locations);

        add_locations = findViewById(R.id.add_locations);
        add_locations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fm = getSupportFragmentManager();
                AddLocationFragment addLocationFragment = new AddLocationFragment();
                addLocationFragment.show(fm, "");

            }
        });


    }
}
