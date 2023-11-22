package lk.software.app.foodorderingadminapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.util.List;

import lk.software.app.foodorderingadminapp.model.Product;

public class ViewProductActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, NavigationBarView.OnItemSelectedListener {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private MaterialToolbar materialToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product);

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        materialToolbar = findViewById(R.id.toolbar);

        setSupportActionBar(materialToolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open_naigation_bar, R.string.close_navigation_bar);
        drawerLayout.addDrawerListener(toggle);

        toggle.syncState();

        materialToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.open();
            }
        });

        navigationView.setNavigationItemSelectedListener(this);

        LayoutInflater layoutInflater = getLayoutInflater();
        List<Product> newProducts = Product.getNewProducts();

        RecyclerView.Adapter productAdapter = new RecyclerView.Adapter<ProductViewHolder>() {
            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = layoutInflater.inflate(R.layout.product_item, parent, false);
                return new ProductViewHolder(view);
            }

            @Override
            public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
                holder.textView.setText(newProducts.get(position).getName());
                holder.textView2.setText(String.valueOf(newProducts.get(position).getPrice()));
                holder.imageView.setImageResource(newProducts.get(position).getImage());
//                holder.imageView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(HomeActivity.this,ProductActivity.class);
//                        startActivity(intent);
//                    }
//                });

            }

            @Override
            public int getItemCount() {
                return newProducts.size();
            }
        };

        RecyclerView recyclerView = findViewById(R.id.productRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(ViewProductActivity.this);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(productAdapter);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}

class ProductViewHolder extends RecyclerView.ViewHolder {

    ImageView imageView;
    TextView textView;
    TextView textView2;


    public ProductViewHolder(@NonNull View v) {
        super(v);
        imageView = v.findViewById(R.id.category_item_image);
        textView = v.findViewById(R.id.category_item_name);
        textView = v.findViewById(R.id.textView);
        textView2 = v.findViewById(R.id.textView19);

    }
}

