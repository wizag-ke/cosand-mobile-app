package wizag.com.supa.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ViewFlipper;

import wizag.com.supa.R;

public class Activity_Supplier_Register extends AppCompatActivity {
ViewFlipper flipper;
EditText fname,lname,phone,password,confirm_password,email,id_no;
EditText company_name,kra_pin, company_mobile_no,location;
Button previous,company_next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier_register);
    }
}
