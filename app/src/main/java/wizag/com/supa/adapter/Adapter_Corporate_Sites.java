package wizag.com.supa.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import wizag.com.supa.R;
import wizag.com.supa.models.Model_Indivindual_Sites;

public class Adapter_Corporate_Sites extends RecyclerView.Adapter<Adapter_Indivindual_Sites.MyViewHolder>  {

    private List<Model_Indivindual_Sites> dataSet;
    Context context;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView description;



        public MyViewHolder(View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.name);
            this.description = itemView.findViewById(R.id.description);
        }


    }

    public Adapter_Corporate_Sites(List<Model_Indivindual_Sites> data, Context context) {
        this.dataSet = data;
        this.context = context;
    }

    @Override
    public Adapter_Indivindual_Sites.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_indivindual_client_sites_card, parent, false);
        Adapter_Indivindual_Sites.MyViewHolder myViewHolder = new Adapter_Indivindual_Sites.MyViewHolder(view);
        return myViewHolder;
    }
    @Override
    public void onBindViewHolder(final Adapter_Indivindual_Sites.MyViewHolder holder, final int listPosition) {
        final Model_Indivindual_Sites indivindual_sites= dataSet.get(listPosition);
        TextView name = holder.name;
        TextView description = holder.description;


        name.setText(indivindual_sites.getName());
        description.setText(indivindual_sites.getDescription());

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

}
