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
import wizag.com.supa.models.Model_Truck_Owner;
import wizag.com.supa.utils.ItemClickListener;

public class Adapter_Truck_Owner extends RecyclerView.Adapter<Adapter_Truck_Owner.MyViewHolder> {

    private List<Model_Truck_Owner> dataSet;
    ItemClickListener itemClickListener;

    Context context;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView plate_no;
        TextView driver_id_no;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.plate_no =  itemView.findViewById(R.id.plate_no);
            this.driver_id_no =  itemView.findViewById(R.id.driver_id_no);
        }
    }

    public Adapter_Truck_Owner(List<Model_Truck_Owner> data, Context context) {
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
        final Model_Truck_Owner supplier = dataSet.get(listPosition);


        TextView plate_no = holder.plate_no;
        TextView driver_id_no = holder.driver_id_no;


        plate_no.setText(supplier.getPlate_no());
        driver_id_no.setText(supplier.getDriver_id_no());

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }


}
