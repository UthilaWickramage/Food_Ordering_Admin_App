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

import lk.software.app.foodorderingadminapp.adapters.CategoryAdapter;
import lk.software.app.foodorderingadminapp.model.Category;
import lk.software.app.foodorderingadminapp.model.Product;

public class ViewCategoryActivity extends AppCompatActivity{

    private FirebaseStorage firebaseStorage;
    private FirebaseFirestore firebaseFirestore;

    private CategoryAdapter categoryAdapter;
    ArrayList<Category> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_category);

        //for database reading
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        categories = new ArrayList<>();



        RecyclerView recyclerView = findViewById(R.id.categoryRecyclerView);
        loadCategories();
        categoryAdapter = new CategoryAdapter(categories,firebaseStorage,ViewCategoryActivity.this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(ViewCategoryActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(categoryAdapter);

findViewById(R.id.imageView3).setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        finish();
    }
});
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ViewCategoryActivity.this, AddCategoryActivity.class));
            }
        });
    }



    private void loadCategories(){
        new Thread(()->{
            firebaseFirestore.collection("categories")
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {

                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            categories.clear();
                            for(DocumentSnapshot snapshot:value.getDocuments()) {
                                Category category = snapshot.toObject(Category.class);
                                categories.add(category);
                            }
                            categoryAdapter.notifyDataSetChanged();
                        }
                    });
        }).start();


    }

}


