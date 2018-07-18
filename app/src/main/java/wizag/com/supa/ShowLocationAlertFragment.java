package wizag.com.supa;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import wizag.com.supa.activity.MenuActivity;

/**
 * Created by User on 16/05/2018.
 */

public class ShowLocationAlertFragment extends DialogFragment {
    Button yes, no;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.show_location_fragment, container, false);

        yes = rootView.findViewById(R.id.yes);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settings = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(settings);
                dismiss();
            }
        });
        no = rootView.findViewById(R.id.no);
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back = new Intent(getActivity(),MenuActivity.class);
                startActivity(back);
                dismiss();
            }
        });


        getDialog().setTitle("Enable Location?");
        return rootView;
    }
}