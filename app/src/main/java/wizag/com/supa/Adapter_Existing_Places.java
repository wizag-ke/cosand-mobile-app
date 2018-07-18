package wizag.com.supa;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import wizag.com.supa.models.Model_Existing_Places;


public class Adapter_Existing_Places extends RecyclerView.Adapter<Adapter_Existing_Places.ProductViewHolder> {

    private static RecyclerTouchListener.ClickListener clickListener;
    private Context mCtx;
    private List<Model_Existing_Places> productList;

    public Adapter_Existing_Places(Context mCtx, List<Model_Existing_Places> productList) {
        this.mCtx = mCtx;
        this.productList = productList;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.list_row, null);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        Model_Existing_Places product = productList.get(position);

        holder.name.setText(product.getName());
        holder.cordinates.setText(product.getCordinates());


    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{

        TextView name, cordinates;
        LinearLayout listItemLayout;


        public ProductViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            name = itemView.findViewById(R.id.name);
            cordinates = itemView.findViewById(R.id.cordinates);


        }

        @Override
        public void onClick(View view) {

        }
    }

}