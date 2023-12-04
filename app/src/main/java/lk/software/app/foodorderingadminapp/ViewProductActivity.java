package lk.software.app.foodorderingadminapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

import lk.software.app.foodorderingadminapp.adapters.ProductAdapter;
import lk.software.app.foodorderingadminapp.model.Product;

public class ViewProductActivity extends AppCompatActivity  {

    private FirebaseStorage firebaseStorage;
    private FirebaseFirestore firebaseFirestore;
    private ProductAdapter productAdapter;
    private ArrayList<Product> products;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product);


        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        products = new ArrayList<>();





        RecyclerView recyclerView = findViewById(R.id.productRecyclerView);
        loadProducts();
        productAdapter = new ProductAdapter(firebaseStorage,ViewProductActivity.this,products);
        LinearLayoutManager layoutManager = new LinearLayoutManager(ViewProductActivity.this);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(productAdapter);

        findViewById(R.id.imageView3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ViewProductActivity.this,AddProductActivity.class));
            }
        });
    }




    private void loadProducts(){
        firebaseFirestore.collection("products")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        products.clear();
                        for(DocumentSnapshot snapshot:value.getDocuments()){
                            Product product = snapshot.toObject(Product.class);
                            product.setDocumentId(snapshot.getId());
                            products.add(product);
                        }
                        productAdapter.notifyDataSetChanged();
                    }
                });
    }
}



