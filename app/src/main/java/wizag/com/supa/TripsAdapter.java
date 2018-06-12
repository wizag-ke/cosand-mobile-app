package wizag.com.supa;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import wizag.com.supa.ModelResults.TripsResults;

/**
 * Created by User on 08/05/2018.
 */

public class TripsAdapter extends RecyclerView.Adapter<TripsAdapter.ViewHolder> {

    private Context context;

    //List to store all superheroes
    private List<TripsResults> movieResults;

    //Constructor of this class
    public TripsAdapter(Context context) {
        super();
        //Getting all superheroes
        movieResults = new ArrayList<>();
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.trip,parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        //Getting the particular item from the list
        TripsResults result = movieResults.get(position);
        String my_status = result.getStatus();

        holder.order_no.setText("Order No: "+result.getOrderNo());
        holder.from_loc.setText("From: "+result.getFromLoc());
        holder.to_loc.setText("To: "+result.getToLoc());
        holder.start_time.setText(result.getStartTime());
        holder.end_time.setText(result.getEndTime());
        holder.duration.setText(result.getDuration());
        holder.material.setText("#"+result.getMaterial());
        holder.status.setText(result.getStatus());
        holder.payment.setText("$"+result.getPayment());

        switch (my_status){
            case "INCOMPLETE":
                holder.status.setBackgroundColor(Color.RED);
                holder.status_icon.setBackgroundColor(Color.RED);
                holder.status_icon.setImageResource(R.drawable.incomplete);
                break;
            case "COMPLETE":
                break;
                default:
                    break;
        }
        holder.status.setText(result.getStatus());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final TripsResults result = movieResults.get(position);
                final Context context = v.getContext();
             }
        });

    }

    public void addRecycler(List<TripsResults>moveResults){
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
        private TextView order_no;
        private TextView from_loc;
        private TextView to_loc;
        private TextView start_time;
        private TextView end_time;
        private TextView duration;
        private TextView status;
        private TextView material;
        private TextView payment;
        private ImageView status_icon;

        //Initializing Views
        public ViewHolder(View itemView) {
            super(itemView);

            order_no = (TextView) itemView.findViewById(R.id.order_title);
            from_loc = (TextView) itemView.findViewById(R.id.starting_point);
            to_loc = (TextView) itemView.findViewById(R.id.destination);
            start_time = (TextView) itemView.findViewById(R.id.starting_time);
            end_time = (TextView) itemView.findViewById(R.id.destination_time);
            duration = (TextView) itemView.findViewById(R.id.duration_time);
            status = (TextView) itemView.findViewById(R.id.order_status);
            material = (TextView) itemView.findViewById(R.id.material);
            payment = (TextView) itemView.findViewById(R.id.trip_payment);
            status_icon = itemView.findViewById(R.id.status_icon);

        }
    }

}