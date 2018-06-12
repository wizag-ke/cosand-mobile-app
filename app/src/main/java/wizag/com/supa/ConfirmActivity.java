package wizag.com.supa;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ConfirmActivity extends AppCompatActivity {
  Button proceed_buy, edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirmation_trial);

        edit = findViewById(R.id.edit);
        proceed_buy = findViewById(R.id.proceed_buy);

        proceed_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fm = getSupportFragmentManager();
                PaymentFragment paymentFragment = new PaymentFragment();
                paymentFragment.show(fm, "");
            }
        });
    }
}

