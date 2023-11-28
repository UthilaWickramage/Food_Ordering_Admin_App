package lk.software.app.foodorderingadminapp.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import lk.software.app.foodorderingadminapp.R;
import lk.software.app.foodorderingadminapp.model.Product;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    public static final String TAG = ProductAdapter.class.getName();
    private FirebaseStorage firebaseStorage;
    private Context context;
    private ArrayList<Product> products;

    public ProductAdapter() {
    }

    public ProductAdapter(FirebaseStorage firebaseStorage, Context context, ArrayList<Product> products) {
        this.firebaseStorage = firebaseStorage;
        this.context = context;
        this.products = products;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        return new ViewHolder(layoutInflater.inflate(R.layout.product_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.product_name.setText(products.get(position).getName());
        holder.category_name.setText(products.get(position).getCategory_name());
        String string_price = new DecimalFormat("0.00").format(products.get(position).getPrice());
        holder.price.setText(string_price);

        firebaseStorage.getReference("productImages/"+products.get(position).getImage()).getDownloadUrl()
                        .addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Picasso.get().load(uri).centerCrop().resize(100, 100).into(holder.product_image);

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });

    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView product_image;
        TextView product_name, category_name, price;
        Button button;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            product_image = itemView.findViewById(R.id.category_item_image);
            product_name = itemView.findViewById(R.id.category_item_name);
            category_name = itemView.findViewById(R.id.product_category_name);
            price = itemView.findViewById(R.id.product_price);
            button = itemView.findViewById(R.id.button5);
        }
    }
}
