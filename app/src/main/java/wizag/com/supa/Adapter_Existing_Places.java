package wizag.com.supa;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


public class Adapter_Existing_Places extends RecyclerView.Adapter<Adapter_Existing_Places.ProductViewHolder> {


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

    class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView name, cordinates;


        public ProductViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            cordinates = itemView.findViewById(R.id.cordinates);

        }
    }
}