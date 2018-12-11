package wizag.com.supa;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
import com.libs.ipay.ipayLibrary.Channel;

import static android.content.Context.MODE_PRIVATE;

public class Wallet extends Fragment {
    String live = "1";
    String vid = "annisa";
    String cbk = "http://sduka.wizag.biz/api/v1/cb/ipayaadb";
    String security_key = "65ksc8dsckd5djud";
    String amount = "10";
    String p1 = "value1";
    String p2 = "value2";
    String p3 = "value3";
    String p4 = "value4";
    String curr = "KES"; //or USD
    String phone_number = "0714980450";
    String email = "simonwambua433@gmail.com";

    public Wallet() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wallet, container, false);
        android.support.v4.app.FragmentManager fragmentManager = getFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Channel fragment1 = new Channel();
        Bundle data = new Bundle();

        /*get prefs data*/
       /* SharedPreferences preferences = this.getActivity().getSharedPreferences("ipay", Context.MODE_PRIVATE);
        String amount_txt = preferences.getString("amount",null);

        SharedPreferences profile_preferences = this.getActivity().getSharedPreferences("ipay_profile", Context.MODE_PRIVATE);
        String email_txt = profile_preferences.getString("email", null);
        String phone_txt = profile_preferences.getString("phone", null);

*/
        data.putString("live", live);
        data.putString("vid", vid);
        data.putString("cbk", cbk);
        data.putString("key", security_key);
        data.putString("amount", amount);
        data.putString("p1", p1);
        data.putString("p2", p2);
        data.putString("p3", p3);
        data.putString("p4", p4);
        data.putString("currency", curr);
        data.putString("phone", phone_number);
        data.putString("email", email);
        fragment1.setArguments(data);
        fragmentTransaction.add(R.id.your_placeholder, fragment1, "fragment");
        fragmentTransaction.commit();

        return view;


    }

}
