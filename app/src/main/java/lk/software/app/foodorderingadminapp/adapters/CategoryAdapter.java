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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import lk.software.app.foodorderingadminapp.MainActivity;
import lk.software.app.foodorderingadminapp.R;
import lk.software.app.foodorderingadminapp.model.Category;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    public static final String TAG = CategoryAdapter.class.getName();
    private FirebaseStorage firebaseStorage;
    private ArrayList<Category> categories;
    private Context context;

    private FirebaseFirestore firebaseFirestore;

    public CategoryAdapter(ArrayList<Category> categories, FirebaseFirestore firebaseFirestore, FirebaseStorage firebaseStorage, Context context) {
        this.categories = categories;
        this.context = context;
        this.firebaseStorage = firebaseStorage;
        this.firebaseFirestore = firebaseFirestore;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.category_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Category category = categories.get(position);

        holder.textView.setText(category.getName());
        new Thread(new Runnable() {
            @Override
            public void run() {
                firebaseStorage.getReference("categoryImages/" + category.getImage()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        holder.imageView.post(() -> Picasso.get().load(uri).centerCrop().resize(100, 100).into(holder.imageView));
                    }
                });
            }
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
                            StorageReference childReference = storageReference.child("categoryImages/" + categories.get(position).getImage());
                            childReference.delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            firebaseFirestore.collection("categories").document(categories.get(position).getDocumentId())
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
        return categories.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;

Button button;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.category_item_name);
            imageView = itemView.findViewById(R.id.category_item_image);
            button = itemView.findViewById(R.id.button5);
        }
    }


}
