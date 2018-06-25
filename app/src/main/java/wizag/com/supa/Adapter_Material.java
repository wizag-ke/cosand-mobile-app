package wizag.com.supa;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class Adapter_Material extends ArrayAdapter<Model_Material> {
    private List<Model_Material> stateList = new ArrayList<>();

    Adapter_Material(@NonNull Context context, int resource, int spinnerText, @NonNull List<Model_Material> stateList) {
        super(context, resource, spinnerText, stateList);
        this.stateList = stateList;
    }

    @Override
    public Model_Material getItem(int position) {
        return stateList.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position);

    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position);
    }

    /**
     * Gets the state object by calling getItem and
     * Sets the state name to the drop-down TextView.
     *
     * @param position the position of the item selected
     * @return returns the updated View
     */
    private View initView(int position) {
        Model_Material material = getItem(position);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.material_list, null);
        TextView textView =  v.findViewById(R.id.spinnerText);
        textView.setText((CharSequence) material.getName());
        return v;

    }
}