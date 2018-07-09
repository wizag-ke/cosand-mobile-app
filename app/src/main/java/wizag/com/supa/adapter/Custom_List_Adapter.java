package wizag.com.supa.adapter;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import wizag.com.supa.Model_Existing_Places;
import wizag.com.supa.R;


public class Custom_List_Adapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Model_Existing_Places> locationItems;


    public Custom_List_Adapter(Activity activity, List<Model_Existing_Places> locationItems) {
        this.activity = activity;
        this.locationItems = locationItems;
    }

    @Override
    public int getCount() {
        return locationItems.size();
    }

    @Override
    public Object getItem(int location) {
        return locationItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_row, null);

        TextView name_txt = (TextView) convertView.findViewById(R.id.name);
        TextView cordinates_txt = (TextView) convertView.findViewById(R.id.cordinates);


        // getting movie data for the row
        Model_Existing_Places m = locationItems.get(position);


        // name
        name_txt.setText(m.getName());
        cordinates_txt.setText(m.getName());

        return convertView;
    }

}