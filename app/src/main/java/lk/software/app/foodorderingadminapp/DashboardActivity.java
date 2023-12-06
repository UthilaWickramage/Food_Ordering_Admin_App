package lk.software.app.foodorderingadminapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainer;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class DashboardActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        CardView addProductCard = findViewById(R.id.addProductCard);
        CardView viewUsersCard = findViewById(R.id.viewUsersCard);
        CardView addCategoryCard = findViewById(R.id.addCategoryCard);
        CardView viewCategories = findViewById(R.id.viewCategoryCard);
        CardView viewProducts = findViewById(R.id.viewProductsCard);
        CardView viewOrders = findViewById(R.id.viewOrdersCard);
        addProductCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(DashboardActivity.this, AddProductActivity.class);
                startActivity(intent1);

            }
        });
        viewUsersCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(DashboardActivity.this, ViewUserActivity.class);
                startActivity(intent1);
            }
        });
        viewCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent1 = new Intent(DashboardActivity.this, ViewCategoryActivity.class);
                startActivity(intent1);

            }
        });
        addCategoryCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent1 = new Intent(DashboardActivity.this, AddCategoryActivity.class);
                startActivity(intent1);

            }
        });
        viewProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent1 = new Intent(DashboardActivity.this, ViewProductActivity.class);
                startActivity(intent1);

            }
        });

        viewOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent1 = new Intent(DashboardActivity.this, AddProductActivity.class);
                startActivity(intent1);

            }
        });

        findViewById(R.id.imageView5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(DashboardActivity.this);
                alertDialog.setTitle("Do you want to Logout?");
                alertDialog.setMessage("This will exit the app");
                alertDialog.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        firebaseAuth.signOut();
                        finish();
                    }
                });
                alertDialog.show();
            }
        });


    }





}