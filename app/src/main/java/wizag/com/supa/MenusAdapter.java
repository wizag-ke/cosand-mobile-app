package wizag.com.supa;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import wizag.com.supa.activity.Activity_Buy;
import wizag.com.supa.activity.Activity_Corporate_Client;
import wizag.com.supa.activity.Activity_Corporate_Profile;
import wizag.com.supa.activity.Activity_Driver_Profile;
import wizag.com.supa.activity.Activity_Driver_Register;
import wizag.com.supa.activity.Activity_Individual_Client;
import wizag.com.supa.activity.Activity_Indvidual_Client_Profile;
import wizag.com.supa.activity.Activity_Location;
import wizag.com.supa.activity.Activity_Login;
import wizag.com.supa.activity.Activity_Sell;
import wizag.com.supa.activity.Activity_Truck_Owner_Profile;
import wizag.com.supa.activity.Activity_Wallet;
import wizag.com.supa.activity.MapsActivity;
import wizag.com.supa.activity.MyTripsActivity;
import wizag.com.supa.activity.OrdersActivity;
import wizag.com.supa.activity.ProfileActivity;
import wizag.com.supa.activity.SuppliesActivity;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by User on 07/05/2018.
 */

public class MenusAdapter extends RecyclerView.Adapter<MenusAdapter.ViewHolder> {
    private static final String SHARED_PREF_NAME = "profilePref";
    private Context context;
    SessionManager sessionManager;
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.menus, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        sessionManager = new SessionManager(parent.getContext());
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        //Getting the particular item from the list
        PermissionResults result = movieResults.get(position);
        final String permission_image = result.getPermission();
        holder.permission.setText(result.getPermission());

        switch (permission_image) {
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
                switch (permission_image) {
                    case "Buy":
                        SharedPreferences sharedPreferences = context.getSharedPreferences("profile", MODE_PRIVATE);
                        String driver_code_buy = sharedPreferences.getString("driver_code", null);
                        if (sharedPreferences != null) {
                            if (driver_code_buy.equalsIgnoreCase("XDRI") || driver_code_buy.equalsIgnoreCase("XTON")) {
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                                builder1.setTitle("Access Denied!");
                                builder1.setMessage("Login as a Client to proceed");
                                builder1.setCancelable(true);

                                builder1.setPositiveButton(
                                        "Proceed",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                sessionManager.logoutUser();
                                            }
                                        });

                                builder1.setNegativeButton(
                                        "Not now",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });

                                AlertDialog alert11 = builder1.create();
                                alert11.show();
                            } else {
                                Intent buy = new Intent(context, Activity_Buy.class);
                                context.startActivity(buy);
                            }
                        }

                        break;
                    case "Sell":
                        SharedPreferences sharedPreferences_sell = context.getSharedPreferences("profile", MODE_PRIVATE);
                        String driver_code_sell = sharedPreferences_sell.getString("driver_code", null);
                        if (sharedPreferences_sell != null) {
                            if (driver_code_sell.equalsIgnoreCase("XIND") || driver_code_sell.equalsIgnoreCase("XCOR")) {
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                                builder1.setTitle("Access Denied!");
                                builder1.setMessage("Login as a Driver or Truck Owner to proceed");
                                builder1.setCancelable(true);

                                builder1.setPositiveButton(
                                        "Proceed",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                sessionManager.logoutUser();
                                            }
                                        });

                                builder1.setNegativeButton(
                                        "Not now",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });

                                AlertDialog alert11 = builder1.create();
                                alert11.show();
                            } else {
                                Intent sell = new Intent(context, Activity_Sell.class);
                                context.startActivity(sell);
                            }
                        }


                        break;
                    case "Wallet":
                        Intent wallet = new Intent(context, Activity_Wallet.class);
                        context.startActivity(wallet);
                        break;
                    case "Profile":
                        SharedPreferences sp = context.getSharedPreferences("profile", MODE_PRIVATE);
                        String driver_code = sp.getString("driver_code", null);
                        if (sp != null) {
                            if (driver_code.equalsIgnoreCase("XDRI")) {
                                Intent driver_profile = new Intent(context, Activity_Driver_Profile.class);
                                context.startActivity(driver_profile);
                            } else if (driver_code.equalsIgnoreCase("XIND")) {
                                Intent ind_profile = new Intent(context, Activity_Indvidual_Client_Profile.class);
                                context.startActivity(ind_profile);
                            } else if (driver_code.equalsIgnoreCase("XCOR")) {
                                Intent cor_profile = new Intent(context, Activity_Corporate_Profile.class);
                                context.startActivity(cor_profile);
                            } else if (driver_code.equalsIgnoreCase("XTON")) {
                                Intent truck_profile = new Intent(context, Activity_Truck_Owner_Profile.class);
                                context.startActivity(truck_profile);
                            }
                        }


                        break;
                    case "Locations":
                        /*Intent check_in = new Intent(context, MapsActivity.class);
                        context.startActivity(check_in);*/
                        break;
                    case "Supply":
                        SharedPreferences prefs = context.getSharedPreferences("profile", MODE_PRIVATE);
                        String driver_code_supply = prefs.getString("driver_code", null);
                        if (prefs != null) {
                            if (driver_code_supply.equalsIgnoreCase("XCOR") || driver_code_supply.equalsIgnoreCase("XIND")) {
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                                builder1.setTitle("Access Denied!");
                                builder1.setMessage("Login as a Driver or Truck Owner to proceed");
                                builder1.setCancelable(true);

                                builder1.setPositiveButton(
                                        "Proceed",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                sessionManager.logoutUser();
                                            }
                                        });

                                builder1.setNegativeButton(
                                        "Not now",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });

                                AlertDialog alert11 = builder1.create();
                                alert11.show();
                            } else {
                                Intent supply = new Intent(context, SuppliesActivity.class);
                                context.startActivity(supply);
                            }
                        }


                        break;
                    case "Trips":
                        SharedPreferences prefs_trip = context.getSharedPreferences("profile", MODE_PRIVATE);
                        String driver_code_trip = prefs_trip.getString("driver_code", null);
                        if (prefs_trip != null) {
                            if (driver_code_trip.equalsIgnoreCase("XCOR") || driver_code_trip.equalsIgnoreCase("XIND")) {
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                                builder1.setTitle("Access Denied!");
                                builder1.setMessage("Login as a Driver or Truck Owner to proceed");
                                builder1.setCancelable(true);

                                builder1.setPositiveButton(
                                        "Proceed",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                sessionManager.logoutUser();
                                            }
                                        });

                                builder1.setNegativeButton(
                                        "Not now",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });

                                AlertDialog alert11 = builder1.create();
                                alert11.show();
                            } else {
                                Intent trips = new Intent(context, TripsActivity.class);
                                context.startActivity(trips);
                            }
                        }

                        break;
                    case "Payments":
                        /*Intent payments = new Intent(context, PaymentActivity.class);
                        context.startActivity(payments);*/
                        break;
                    case "Orders":
                        SharedPreferences prefs_orders = context.getSharedPreferences("profile", MODE_PRIVATE);
                        String driver_code_orders = prefs_orders.getString("driver_code", null);
                        if (prefs_orders != null) {
                            if (driver_code_orders.equalsIgnoreCase("XCOR") || driver_code_orders.equalsIgnoreCase("XIND")) {
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                                builder1.setTitle("Access Denied!");
                                builder1.setMessage("Login as a Driver or Truck Owner to proceed");
                                builder1.setCancelable(true);

                                builder1.setPositiveButton(
                                        "Proceed",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                sessionManager.logoutUser();
                                            }
                                        });

                                builder1.setNegativeButton(
                                        "Not now",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });

                                AlertDialog alert11 = builder1.create();
                                alert11.show();
                            } else {
                                Intent orders = new Intent(context, OrdersActivity.class);
                                context.startActivity(orders);
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
        });

    }

    public void addRecycler(List<PermissionResults> moveResults) {
        movieResults.addAll(moveResults);
        notifyDataSetChanged();

    }

    public void clearRecycler() {
        movieResults.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return movieResults.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
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