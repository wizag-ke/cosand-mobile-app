package wizag.com.supa.adapter;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.List;

import wizag.com.supa.R;
import wizag.com.supa.models.Trucks;

/**
 * Created by Ravi Tamada on 18/05/16.
 */
public class Adapter_Trucks extends RecyclerView.Adapter<Adapter_Trucks.MyViewHolder> {

    private Context mContext;
    private List<Trucks> truckList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView plate_no, year, model, make, tonnage;

        public MyViewHolder(View view) {
            super(view);
            plate_no = (TextView) view.findViewById(R.id.plate_no);
            year = (TextView) view.findViewById(R.id.year);
            model = (TextView) view.findViewById(R.id.model);
            make = (TextView) view.findViewById(R.id.make);
            tonnage = (TextView) view.findViewById(R.id.tonnage);
        }
    }


    public Adapter_Trucks(Context mContext, List<Trucks> truckList) {
        this.mContext = mContext;
        this.truckList = truckList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.owner_trucks_model, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Trucks truck = truckList.get(position);
        holder.plate_no.setText(truck.getPlate_no());
        holder.make.setText(truck.getMake());
        holder.model.setText(truck.getModel());
        holder.tonnage.setText(truck.getTonnage());
        holder.year.setText(truck.getYear());
    }


    @Override
    public int getItemCount() {
        return truckList.size();
    }
}