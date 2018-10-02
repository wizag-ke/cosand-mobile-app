package wizag.com.supa.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import wizag.com.supa.R;
import wizag.com.supa.models.Trucks;

public class Adapter_Trucks extends ArrayAdapter<Trucks> {

    //the truck list that will be displayed
    private List<Trucks> truckList;

    //the context object
    private Context mCtx;

    //here we are getting the herolist and context
    //so while creating the object of this adapter class we need to give herolist and context
    public Adapter_Trucks(List<Trucks> truckList, Context mCtx) {
        super(mCtx, R.layout.owner_trucks_model, truckList);
        this.truckList = truckList;
        this.mCtx = mCtx;
    }

    //this method will return the list item
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //getting the layoutinflater
        LayoutInflater inflater = LayoutInflater.from(mCtx);

        //creating a view with our xml layout
        View listViewItem = inflater.inflate(R.layout.owner_trucks_model, null, true);

        //getting text views
        TextView plate_no= listViewItem.findViewById(R.id.plate_no);
        TextView make= listViewItem.findViewById(R.id.make);
        TextView model= listViewItem.findViewById(R.id.model);
        TextView year= listViewItem.findViewById(R.id.year);
        TextView tonnage= listViewItem.findViewById(R.id.tonnage);

        //Getting the truck for the specified position
        Trucks truck = truckList.get(position);

        //setting truck values to textviews
        plate_no.setText(truck.getPlate_no());
        make.setText(truck.getMake());
        model.setText(truck.getModel());
        year.setText(truck.getYear());
        tonnage.setText(truck.getTonnage());

        //returning the listitem
        return listViewItem;
    }
}