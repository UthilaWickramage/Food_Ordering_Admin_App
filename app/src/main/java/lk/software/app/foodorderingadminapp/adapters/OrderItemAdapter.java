package lk.software.app.foodorderingadminapp.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

import lk.software.app.foodorderingadminapp.R;
import lk.software.app.foodorderingadminapp.model.Order;
import lk.software.app.foodorderingadminapp.model.OrderItem;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.ViewHolder>{
    private Context context;
    private ArrayList<OrderItem> orderItems;

    private FirebaseStorage firebaseStorage;


    public OrderItemAdapter(Context context,FirebaseStorage firebaseStorage, ArrayList<OrderItem> orderItems) {
this.firebaseStorage = firebaseStorage;
        this.context = context;
        this.orderItems = orderItems;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderItem orderItem = orderItems.get(position);
        holder.titleText.setText(orderItem.getProduct_name());
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        holder.order_item_price.setText(decimalFormat.format(orderItem.getPrice()));
        holder.order_qty.setText("x"+String.valueOf(orderItem.getQuantity()));
        firebaseStorage.getReference("productImages/"+orderItem.getImage()).getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).centerCrop().resize(75,75).into(holder.imageView);
                    }
                });
    }

    @Override
    public int getItemCount() {
        return orderItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        TextView titleText, order_item_price, order_qty;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.titleText = itemView.findViewById(R.id.titleText);
            this.order_item_price = itemView.findViewById(R.id.order_item_price);
            this.order_qty = itemView.findViewById(R.id.order_qty);
            this.imageView = itemView.findViewById(R.id.order_item_image);

        }
    }
}
