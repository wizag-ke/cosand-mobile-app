package wizag.com.supa;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 07/05/2018.
 */

public class MenusAdapter extends RecyclerView.Adapter<MenusAdapter.ViewHolder> {

    private Context context;

    //List to store all superheroes
    private List<PermissionResults> movieResults;

    //Constructor of this class
    public MenusAdapter(Context context) {
        super();
        //Getting all superheroes
        movieResults = new ArrayList<>();
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.menus,parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        //Getting the particular item from the list
        PermissionResults result = movieResults.get(position);
        final String permission_image = result.getPermission();
        holder.permission.setText(result.getPermission());

        switch (permission_image){
            case "Buy":
                holder.menu_image.setBackgroundResource(R.drawable.menu_buy);
                holder.menu_image.setImageResource(R.drawable.new_order);
                break;
            case "Sell":
                holder.menu_image.setBackgroundResource(R.drawable.menu_sell);
                holder.menu_image.setImageResource(R.drawable.sell_icon);
                break;
            case "Wallet":
                holder.menu_image.setBackgroundResource(R.drawable.menu_wallet);
                holder.menu_image.setImageResource(R.drawable.wallet_icon);
                break;
            case "Profile":
                holder.menu_image.setBackgroundResource(R.drawable.menu_profile);
                holder.menu_image.setImageResource(R.drawable.profile);
                break;
            case "Locations":
                holder.menu_image.setBackgroundResource(R.drawable.menu_check_in);
                holder.menu_image.setImageResource(R.drawable.locations_image);
                break;
            case "Supply":
                holder.menu_image.setBackgroundResource(R.drawable.menu_supply);
                holder.menu_image.setImageResource(R.drawable.supply_icon);
                break;
            case "Trips":
                holder.menu_image.setBackgroundResource(R.drawable.menu_trips);
                holder.menu_image.setImageResource(R.drawable.truck_icon);
                break;
            case "Payments":
                holder.menu_image.setBackgroundResource(R.drawable.menu_payments);
                holder.menu_image.setImageResource(R.drawable.payments_image);
                break;
            case "Orders":
                holder.menu_image.setBackgroundResource(R.drawable.menu_orders);
                holder.menu_image.setImageResource(R.drawable.orders_image);
                break;
            default:
                break;
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Context context = v.getContext();
                switch (permission_image){
                    case "Buy":
                       Intent buy = new Intent(context, Activity_Buy.class);
                       context.startActivity(buy);

                        break;
                    case "Sell":
                        Intent sell = new Intent(context,Activity_Sell.class);
                        context.startActivity(sell);
                        break;
                    case "Wallet":
                        Intent wallet = new Intent(context,WalletActivity.class);
                        context.startActivity(wallet);
                        break;
                    case "Profile":
                        Intent profile = new Intent(context,ProfileActivity.class);
                        context.startActivity(profile);
                        break;
                    case "Locations":
                        Intent check_in = new Intent(context,Activity_Location.class);
                        context.startActivity(check_in);
                        break;
                    case "Supply":
                        Intent supply = new Intent(context,SuppliesActivity.class);
                        context.startActivity(supply);
                        break;
                    case "Trips":
                        Intent trips = new Intent(context,MyTripsActivity.class);
                        context.startActivity(trips);
                        break;
                    case "Payments":
                        Intent payments = new Intent(context,PaymentActivity.class);
                        context.startActivity(payments);
                        break;
                    case "Orders":
                        Intent orders = new Intent(context,OrdersActivity.class);
                        context.startActivity(orders);
                        break;
                    default:
                        break;
                }
                 }
        });

    }

    public void addRecycler(List<PermissionResults>moveResults){
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
        private ImageView menu_image;
        private TextView permission;


        //Initializing Views
        public ViewHolder(View itemView) {
            super(itemView);
            menu_image = (ImageView) itemView.findViewById(R.id.menu_image);
            permission = (TextView) itemView.findViewById(R.id.permission);

        }
    }

}