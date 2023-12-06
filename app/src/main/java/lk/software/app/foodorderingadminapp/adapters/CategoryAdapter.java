package lk.software.app.foodorderingadminapp.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import lk.software.app.foodorderingadminapp.R;
import lk.software.app.foodorderingadminapp.model.Category;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    public static final String TAG = CategoryAdapter.class.getName();
    private FirebaseStorage firebaseStorage;
    private ArrayList<Category> categories;
    private Context context;

    public CategoryAdapter(ArrayList<Category> categories, FirebaseStorage firebaseStorage, Context context) {
        this.categories = categories;
        this.context = context;
        this.firebaseStorage = firebaseStorage;
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
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.category_item_name);
            imageView = itemView.findViewById(R.id.category_item_image);
        }
    }


}
