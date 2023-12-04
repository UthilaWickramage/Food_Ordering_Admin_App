package lk.software.app.foodorderingadminapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import jp.wasabeef.picasso.transformations.MaskTransformation;
import lk.software.app.foodorderingadminapp.MainActivity;
import lk.software.app.foodorderingadminapp.R;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;


import lk.software.app.foodorderingadminapp.ViewOrderItemsActivity;
import lk.software.app.foodorderingadminapp.ViewOrdersActivity;
import lk.software.app.foodorderingadminapp.model.Order;
import lk.software.app.foodorderingadminapp.model.OrderStatusEnum;
import lk.software.app.foodorderingadminapp.model.User;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {


    private Context context;
    private ArrayList<Order> orders;


    public OrderAdapter(Context context, ArrayList<Order> orders) {

        this.context = context;
        this.orders = orders;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_order, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order order = orders.get(position);

        holder.id.setText("#"+order.getDocumentId());


        holder.date.setText(order.getCurrentSaveDate());
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        holder.price.setText(decimalFormat.format(order.getTotalOrderPrice()));

        holder.status.setText(order.getOrderStatus());
        String orderStatus = order.getOrderStatus();
        if (orderStatus.equals(OrderStatusEnum.PENDING.toString())) {
            holder.frameLayout.setBackgroundColor(context.getColor(R.color.light_blue));
            holder.status.setTextColor(context.getColor(R.color.blue));
        } else if (orderStatus.equals(OrderStatusEnum.ACCEPTED.toString())) {
            holder.frameLayout.setBackgroundColor(context.getColor(R.color.light_yellow));
            holder.status.setTextColor(context.getColor(R.color.dark_yellow));

        } else if (orderStatus.equals(OrderStatusEnum.DELIVERED.toString())) {
            holder.frameLayout.setBackgroundColor(context.getColor(R.color.light_green));
            holder.status.setTextColor(context.getColor(R.color.green));

        } else if (orderStatus.equals(OrderStatusEnum.REJECTED.toString())) {
            holder.frameLayout.setBackgroundColor(context.getColor(R.color.light_red));
            holder.frameLayout.setBackgroundColor(context.getColor(R.color.red));

        }

        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewOrderItemsActivity.class);
                intent.putExtra("documentId",order.getDocumentId());
                intent.putExtra("price",String.valueOf(order.getTotalOrderPrice()));
                intent.putExtra("date",order.getCurrentSaveDate());
                intent.putExtra("customer",order.getCustomer_name());
                Log.d(MainActivity.TAG,order.getCustomer_name());
                intent.putExtra("customer_id",order.getCustomer_id());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout constraintLayout;
        FrameLayout frameLayout;
        TextView id, date, price, status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.id = itemView.findViewById(R.id.textView13);
            this.date = itemView.findViewById(R.id.textView11);
            this.price = itemView.findViewById(R.id.textView9);
            this.status = itemView.findViewById(R.id.textView12);
            this.frameLayout = itemView.findViewById(R.id.frameLayout);
            this.constraintLayout = itemView.findViewById(R.id.constraintLayout);
        }
    }
}
