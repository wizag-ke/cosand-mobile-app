package wizag.com.supa.activity;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.libs.ipay.ipayLibrary.Channel;

import wizag.com.supa.R;
import wizag.com.supa.Wallet;

public class Wallet_Ipay extends AppCompatActivity {
    private static final String SHARED_PREF_NAME = "ipay";
    String phone, email, amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_test);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            phone = extras.getString("phone");
            email = extras.getString("email");
            amount = extras.getString("amount");
//            Toast.makeText(this, flag_type, Toast.LENGTH_LONG).show();
        }

        SharedPreferences sp = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("phone", phone);
        editor.putString("email", email);
        editor.putString("amount", amount);
        editor.apply();


        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();


        ft.replace(R.id.your_placeholder, new Wallet());
        ft.commit();


    }
}
