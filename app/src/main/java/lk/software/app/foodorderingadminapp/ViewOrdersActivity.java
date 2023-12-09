package lk.software.app.foodorderingadminapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

import lk.software.app.foodorderingadminapp.adapters.OrderAdapter;
import lk.software.app.foodorderingadminapp.adapters.ProductAdapter;
import lk.software.app.foodorderingadminapp.model.Order;
import lk.software.app.foodorderingadminapp.model.Product;
import lk.software.app.foodorderingadminapp.model.User;

public class ViewOrdersActivity extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;
    private OrderAdapter orderAdapter;
    private ArrayList<Order> orders;
    private String documentId;
    private String customer_name;
private Order order;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_orders);
        documentId = getIntent().getExtras().getString("documentId");
        customer_name = getIntent().getExtras().getString("customer_name");
        orders = new ArrayList<>();
        firebaseFirestore = FirebaseFirestore.getInstance();

        RecyclerView recyclerView = findViewById(R.id.orderRecycler);
        loadOrders();
        orderAdapter = new OrderAdapter(ViewOrdersActivity.this, orders);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ViewOrdersActivity.this);
        recyclerView.setAdapter(orderAdapter);
        recyclerView.setLayoutManager(linearLayoutManager);

        findViewById(R.id.imageView3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void loadOrders() {
        new Thread(()->{
            firebaseFirestore.collection("orders").document(documentId).collection("order_list")
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            orders.clear();
                            for (DocumentSnapshot snapshot : value.getDocuments()) {
                                order = snapshot.toObject(Order.class);
                                order.setDocumentId(snapshot.getId());
                                order.setCustomer_id(documentId);
                                order.setCustomer_name(customer_name);
                                orders.add(order);
                            }
                            orderAdapter.notifyDataSetChanged();
                        }

                    });
        }).start();
    }



}

