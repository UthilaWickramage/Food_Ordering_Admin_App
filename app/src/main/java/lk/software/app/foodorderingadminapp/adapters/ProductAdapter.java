package lk.software.app.foodorderingadminapp.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import lk.software.app.foodorderingadminapp.MainActivity;
import lk.software.app.foodorderingadminapp.R;
import lk.software.app.foodorderingadminapp.model.Product;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    public static final String TAG = ProductAdapter.class.getName();

    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;
    private Context context;
    private ArrayList<Product> products;

    public ProductAdapter() {
    }

    public ProductAdapter(FirebaseStorage firebaseStorage, Context context, ArrayList<Product> products) {
        this.firebaseStorage = firebaseStorage;
        this.context = context;
        this.products = products;
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
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
        new Thread(() -> {
            firebaseStorage.getReference("productImages/" + products.get(position).getImage()).getDownloadUrl()
                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            holder.product_image.post(() -> {
                                Picasso.get().load(uri).centerCrop().resize(100, 100).into(holder.product_image);

                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        }).start();


        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setTitle("Delete this product?");
                alertDialog.setMessage("this process cannot be undone");
                alertDialog.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        new Thread(() -> {
                            StorageReference storageReference = firebaseStorage.getReference();
                            StorageReference childReference = storageReference.child("productImages/" + products.get(position).getImage());
                            childReference.delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            firebaseFirestore.collection("products").document(products.get(position).getDocumentId())
                                                    .delete()
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {

                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {

                                                        }
                                                    });
                                            Log.d(MainActivity.TAG, "image deleted!");
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e(MainActivity.TAG, "image deletion failed!");

                                        }
                                    });
                            Log.d(MainActivity.TAG, "DocumentSnapshot successfully deleted!");
                        }).start();
                    }

                });

                alertDialog.show();

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
