package wizag.com.supa.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import wizag.com.supa.R;
import wizag.com.supa.models.Model_Trucks;
import wizag.com.supa.utils.ItemClickListener;

public class Adapter_Trucks extends RecyclerView.Adapter<Adapter_Trucks.MyViewHolder> {

    private List<Model_Trucks> dataSet;
      Context context;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView make;
        TextView model;
        TextView axle_count;
        TextView plate_no;
        TextView tonnage;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.make =  itemView.findViewById(R.id.make);
            this.model =  itemView.findViewById(R.id.model);
            this.axle_count = itemView.findViewById(R.id.axle_count);
            this.plate_no = itemView.findViewById(R.id.owner_plate_no);
            this.tonnage = itemView.findViewById(R.id.tonnage);
        }
    }

    public Adapter_Trucks(List<Model_Trucks> data, Context context) {
        this.dataSet = data;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_truck_owner, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {
        final Model_Trucks trucks = dataSet.get(listPosition);


        TextView make = holder.make;
        TextView model = holder.model;
        TextView axle_count = holder.axle_count;
        TextView plate_no = holder.plate_no;
        TextView tonnage = holder.tonnage;


        make.setText(trucks.getMake());
        model.setText(trucks.getModel());
        axle_count.setText(trucks.getAxle_count());
        plate_no.setText(trucks.getPlate_no());
        tonnage.setText(trucks.getTonnage_id());

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }


}
