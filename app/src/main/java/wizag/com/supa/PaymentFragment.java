package wizag.com.supa;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.rahatarmanahmed.cpv.CircularProgressView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by User on 11/05/2018.
 */

public class PaymentFragment extends DialogFragment {
    ImageView payment_image, progress_complete;
    TextView your_order, payment_method_label, payment_amount, your_payment;
    Button proceed_buy, edit, ok;
    Spinner pay_via;
    String[] material = new String[]{"Select Payment Method",
            "Pay via MPESA", "Pay via Airtel Money", "Pay via Equitel"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.payment_fragment, container, false);

        final CircularProgressView progressView = (CircularProgressView) rootView.findViewById(R.id.progress_view);
        progress_complete = rootView.findViewById(R.id.progress_complete);

        your_order = rootView.findViewById(R.id.your_order);
        payment_method_label = rootView.findViewById(R.id.payment_method_label);
        payment_amount = rootView.findViewById(R.id.payment_amount);

        your_payment = rootView.findViewById(R.id.your_payment);
        edit = rootView.findViewById(R.id.edit);
        ok = rootView.findViewById(R.id.ok);


        proceed_buy = rootView.findViewById(R.id.proceed_buy);
        proceed_buy.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                progressView.setVisibility(View.VISIBLE);
                progressView.startAnimation();

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        progress_complete.setVisibility(View.VISIBLE);
                        progressView.stopAnimation();
                        progressView.setVisibility(View.GONE);

                        payment_image.setVisibility(View.GONE);
                        pay_via.setVisibility(View.GONE);

                        payment_amount.setVisibility(View.GONE);
                        your_payment.setVisibility(View.GONE);
                        proceed_buy.setVisibility(View.GONE);
                        edit.setVisibility(View.GONE);

                        getDialog().setTitle("Order received!");
                        payment_method_label.setText("You will receive a message once your order is confirmed!");
                        payment_method_label.setTextColor(Color.BLACK);



                        your_order.setVisibility(View.GONE);
                        ok.setVisibility(View.VISIBLE);
                        ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getDialog().dismiss();
                            }
                        });

                    }
                }, 5000);

            }
        });

        payment_image = rootView.findViewById(R.id.payment_image);
        pay_via = rootView.findViewById(R.id.pay_via);

        final List<String> materialList = new ArrayList<>(Arrays.asList(material));
        // Initializing Material ArrayAdapter
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                getActivity().getApplicationContext(), R.layout.spinner_item, materialList) {

             @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                return view;
            }
        };

        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        pay_via.setAdapter(spinnerArrayAdapter);

        pay_via.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                // If user change the default selection
                // First item is disable and it is used for hint
                if (position == 0){

                }

                else if (position == 1) {
                    payment_image.setImageResource(R.drawable.mpesa);
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(200, 100);
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    payment_image.setLayoutParams(layoutParams);
                }

                else if (position == 2) {
                    payment_image.setImageResource(R.drawable.airtel_money);
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(200, 100);
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    payment_image.setLayoutParams(layoutParams);
                }

                else {
                    payment_image.setImageResource(R.drawable.equitel);
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(200, 100);
                    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    payment_image.setLayoutParams(layoutParams);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        getDialog().setTitle("Complete Payment");
        return rootView;
     }
}
