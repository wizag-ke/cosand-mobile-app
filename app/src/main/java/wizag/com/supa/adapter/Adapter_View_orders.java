package wizag.com.supa.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import wizag.com.supa.R;
import wizag.com.supa.models.Model_Orders;
import wizag.com.supa.utils.ItemClickListener;

public class Adapter_View_orders extends RecyclerView.Adapter<Adapter_View_orders.MyViewHolder> {

    private List<Model_Orders> dataSet;
    ItemClickListener itemClickListener;

    Context context;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView material_type;
        TextView material_item;
        TextView material_detail;
        TextView material_class;
        TextView material_quantity;
        TextView quote;
        TextView order_status;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.material_type = itemView.findViewById(R.id.material_id);
            this.material_item = itemView.findViewById(R.id.material_detail_id);
            this.material_detail = itemView.findViewById(R.id.material_class_id);
            this.material_class = itemView.findViewById(R.id.material_unit_id);
            this.material_quantity = itemView.findViewById(R.id.material_quantity);
            this.quote = itemView.findViewById(R.id.quote);
            this.order_status = itemView.findViewById(R.id.order_status);
        }
    }

    public Adapter_View_orders(List<Model_Orders> data, Context context) {
        this.dataSet = data;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_orderlist, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {
        final Model_Orders orders = dataSet.get(listPosition);


        TextView material_type = holder.material_type;
        TextView material_item = holder.material_item;
        TextView material_detail = holder.material_detail;
        TextView material_class = holder.material_class;
        TextView material_quantity = holder.material_quantity;
        TextView quote = holder.quote;
        TextView order_status = holder.order_status;


        material_type.setText(orders.getMaterial_type());
        material_item.setText(orders.getMaterial_item());
        material_detail.setText(orders.getMaterial_item());
        material_class.setText(orders.getMaterial_class());
        material_quantity.setText(orders.getMaterial_quantity());
        quote.setText(orders.getQuote());
        order_status.setText(orders.getOrder_status());

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }


}
