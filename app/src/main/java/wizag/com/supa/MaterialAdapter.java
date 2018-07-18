/*
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

public class MaterialAdapter extends ArrayAdapter<Model_Material> {
    private List<Model_Material> materialList = new ArrayList<>();

    MaterialAdapter(@NonNull Context context, int resource, int spinnerText, @NonNull List<Model_Material> materialList) {
        super(context, resource, spinnerText, materialList);
        this.materialList = materialList;
    }

    @Override
    public Model_Material getItem(int position) {
        return materialList.get(position);
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



    private View initView(int position) {
        Model_Material state = getItem(position);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.material_list, null);
        TextView textView =  v.findViewById(R.id.spinnerTextItem);
        textView.setText(state.getMaterial());
        return v;

    }

}
*/
