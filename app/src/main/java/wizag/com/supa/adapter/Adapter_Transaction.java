package wizag.com.supa.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import wizag.com.supa.R;
import wizag.com.supa.models.Model_Transaction;

public class Adapter_Transaction extends RecyclerView.Adapter<Adapter_Transaction.MyViewHolder> {

    private ArrayList<Model_Transaction> dataSet;
    Context context;
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView description;
        TextView amount;
        TextView type;
        TextView date;
        TextView status;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.description = itemView.findViewById(R.id.description);
            this.amount = itemView.findViewById(R.id.amount);
            this.type =  itemView.findViewById(R.id.type);
            this.status =  itemView.findViewById(R.id.status);
            this.date =  itemView.findViewById(R.id.date);
        }
    }

    public Adapter_Transaction(ArrayList<Model_Transaction> data, Context context) {
        this.dataSet = data;
        this.context=context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.transactions_list, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView description = holder.description;
        TextView amount = holder.amount;
        TextView status = holder.status;
        TextView type = holder.type;
        TextView date = holder.date;


        description.setText(dataSet.get(listPosition).getDescription());
        amount.setText(dataSet.get(listPosition).getAmount());
        status.setText(dataSet.get(listPosition).getStatus());
        type.setText(dataSet.get(listPosition).getType());
        date.setText(dataSet.get(listPosition).getDate());

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
