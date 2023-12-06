package lk.software.app.foodorderingadminapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.text.DecimalFormat;
import java.util.ArrayList;

import lk.software.app.foodorderingadminapp.adapters.OrderAdapter;
import lk.software.app.foodorderingadminapp.adapters.OrderItemAdapter;
import lk.software.app.foodorderingadminapp.model.Order;
import lk.software.app.foodorderingadminapp.model.OrderItem;

public class ViewOrderItemsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private FirebaseFirestore firebaseFirestore;
    FirebaseStorage firebaseStorage;
    private OrderItemAdapter orderItemAdapter;
    private ArrayList<OrderItem> orderItems;
    private String documentId;
    private String customerId;
    private OrderItem orderItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_order_items_actvity);
        Spinner spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.order_status,
                android.R.layout.simple_spinner_item
        );
// Specify the layout to use when the list of choices appears.
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner.
        spinner.setAdapter(adapter);
        orderItems = new ArrayList<>();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        customerId = getIntent().getExtras().getString("customer_id");
        documentId = getIntent().getExtras().getString("documentId");

        loadOrderItems();
        orderItemAdapter = new OrderItemAdapter(getApplicationContext(), firebaseStorage, orderItems);
        String totalPrice = getIntent().getExtras().getString("price");
        String date = getIntent().getExtras().getString("date");

        String customer = getIntent().getExtras().getString("customer");

        TextView order_made = findViewById(R.id.textView16);
        order_made.setText(date);
        TextView orderPrice = findViewById(R.id.textView19);

        orderPrice.setText(totalPrice);
        TextView customer_name = findViewById(R.id.textView14);
        customer_name.setText(customer);
        RecyclerView recyclerView = findViewById(R.id.order_item_recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ViewOrderItemsActivity.this);
        recyclerView.setAdapter(orderItemAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        findViewById(R.id.imageView3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.button7).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewOrderItemsActivity.this, ViewLocationActivity.class);
                intent.putExtra("customerId", customerId);
                startActivity(intent);
            }
        });
    }

    private void loadOrderItems() {
        new Thread(()->{
            firebaseFirestore.collection("orders").document(customerId).collection("order_list").document(documentId).collection("order_items")
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            orderItems.clear();
                            for (DocumentSnapshot snapshot : value.getDocuments()) {
                                orderItem = snapshot.toObject(OrderItem.class);

                                orderItems.add(orderItem);
                            }
                            orderItemAdapter.notifyDataSetChanged();
                        }

                    });
        }).start();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        Log.d(MainActivity.TAG, parent.getItemAtPosition(position).toString());
        firebaseFirestore.collection("orders").document(customerId).collection("order_list").document(documentId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            Order order = task.getResult().toObject(Order.class);
                            if (order != null) {
                                order.setOrderStatus(parent.getItemAtPosition(position).toString());

                                updateOrderStatus(order);
                                Toast.makeText(ViewOrderItemsActivity.this, "You change the status to " + parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();

                            }
                        }

                    }
                });
    }

    private void updateOrderStatus(Order order) {
        firebaseFirestore.collection("orders").document(customerId).collection("order_list").document(documentId).set(order);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(ViewOrderItemsActivity.this, "You haven't change the status", Toast.LENGTH_SHORT).show();

    }
}