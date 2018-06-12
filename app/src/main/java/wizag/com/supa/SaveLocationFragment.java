package wizag.com.supa;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by User on 17/05/2018.
 */

public class SaveLocationFragment extends DialogFragment {
    Button cancel, save;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.save_locations_fragment, container, false);

        save = rootView.findViewById(R.id.save);
        cancel = rootView.findViewById(R.id.cancel);

        getDialog().setTitle("Save As:");
        return rootView;
    }
}
