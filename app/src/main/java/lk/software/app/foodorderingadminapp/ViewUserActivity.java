package lk.software.app.foodorderingadminapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

import lk.software.app.foodorderingadminapp.adapters.ProductAdapter;
import lk.software.app.foodorderingadminapp.adapters.UserAdapter;
import lk.software.app.foodorderingadminapp.model.Product;
import lk.software.app.foodorderingadminapp.model.User;

public class ViewUserActivity extends AppCompatActivity {
    private FirebaseStorage firebaseStorage;
    private FirebaseFirestore firebaseFirestore;
    private UserAdapter userAdapter;
    private ArrayList<User> users;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        users = new ArrayList<>();

        findViewById(R.id.imageView3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        RecyclerView recyclerView = findViewById(R.id.userRecycler);
        loadUsers();
        userAdapter = new UserAdapter(firebaseStorage,ViewUserActivity.this,users);
        LinearLayoutManager layoutManager = new LinearLayoutManager(ViewUserActivity.this);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(userAdapter);
    }

    private void loadUsers() {
        firebaseFirestore.collection("customers")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        users.clear();
                        for(DocumentSnapshot snapshot:value.getDocuments()){
                            User user = snapshot.toObject(User.class);
                            user.setDocumentId(snapshot.getId());
                            users.add(user);

                        }
                        userAdapter.notifyDataSetChanged();
                    }
                });
    }
}