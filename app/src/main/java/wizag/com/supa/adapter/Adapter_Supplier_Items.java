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
import wizag.com.supa.models.Model_Supplier_Profile;
import wizag.com.supa.utils.ItemClickListener;

public class Adapter_Supplier_Items extends RecyclerView.Adapter<Adapter_Supplier_Items.MyViewHolder> {

    private List<Model_Supplier_Profile> dataSet;
    ItemClickListener itemClickListener;

    Context context;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView material_id;
        TextView material_detail_id;
        TextView material_class_id;
        TextView material_unit_id;
        TextView unit_price;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.material_id = (TextView) itemView.findViewById(R.id.material_id);
            this.material_detail_id = (TextView) itemView.findViewById(R.id.material_detail_id);
            this.material_class_id = itemView.findViewById(R.id.material_class_id);
            this.material_unit_id = itemView.findViewById(R.id.material_unit_id);
            this.unit_price = itemView.findViewById(R.id.unit_price);
        }
    }

    public Adapter_Supplier_Items(List<Model_Supplier_Profile> data, Context context) {
        this.dataSet = data;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_supplier_materials, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {
        final Model_Supplier_Profile supplier = dataSet.get(listPosition);


        TextView material_id = holder.material_id;
        TextView material_detail_id = holder.material_detail_id;
        TextView material_class_id = holder.material_class_id;
        TextView material_unit_id = holder.material_unit_id;
        TextView unit_price = holder.unit_price;


        material_id.setText(supplier.getMaterial_name());
        material_detail_id.setText(supplier.getDetails_name());
        material_class_id.setText(supplier.getClass_name());
        material_unit_id.setText(supplier.getUnits_name());
        unit_price.setText(supplier.getCost());

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }


}