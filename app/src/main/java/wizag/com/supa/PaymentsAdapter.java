package wizag.com.supa;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import wizag.com.supa.ModelResults.PaymentResults;
import wizag.com.supa.ModelResults.TripsResults;

/**
 * Created by User on 15/05/2018.
 */

public class PaymentsAdapter extends RecyclerView.Adapter<PaymentsAdapter.ViewHolder> {

    private Context context;

    //List to store all superheroes
    private List<PaymentResults> movieResults;

    //Constructor of this class
    public PaymentsAdapter(Context context) {
        super();
        //Getting all superheroes
        movieResults = new ArrayList<>();
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_card,parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        //Getting the particular item from the list
        PaymentResults result = movieResults.get(position);
        holder.company.setText(result.getCompany());
        holder.day_date.setText(result.getDayDate());
        holder.amount.setText(result.getAmount());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PaymentResults result = movieResults.get(position);
                final Context context = v.getContext();
            }
        });

    }

    public void addRecycler(List<PaymentResults>moveResults){
        movieResults.addAll(moveResults);
        notifyDataSetChanged();

    }

    public void clearRecycler(){
        movieResults.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return movieResults.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        //Views
        private TextView company;
        private TextView day_date;
        private TextView amount;

        //Initializing Views
        public ViewHolder(View itemView) {
            super(itemView);

            company = (TextView) itemView.findViewById(R.id.name);
            day_date = (TextView) itemView.findViewById(R.id.date);
            amount = (TextView) itemView.findViewById(R.id.amount);

        }
    }

}