package lk.software.app.foodorderingadminapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

import java.util.UUID;

import lk.software.app.foodorderingadminapp.model.Product;

public class AddProductActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, NavigationBarView.OnItemSelectedListener {

    public static final String TAG = AddProductActivity.class.getName();
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private MaterialToolbar materialToolbar;
    private FirebaseFirestore firebaseFirestore;

    private FirebaseStorage firebaseStorage;
    private ImageButton imageButton;
    private Uri imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        materialToolbar = findViewById(R.id.toolbar);
        firebaseStorage = FirebaseStorage.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

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
        imageButton = findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                activityResultLauncher.launch(Intent.createChooser(intent, "Select Image"));
                Log.i(TAG, "Working");
            }
        });

        //adding product to firebase

        findViewById(R.id.addCategoryBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText product_name = findViewById(R.id.categoryName);
                EditText product_price = findViewById(R.id.product_price_input);
                EditText prepare_time = findViewById(R.id.prepare_time_input);
                EditText person_per_serve = findViewById(R.id.person_per_serve_input);
                Spinner categorySpinner = findViewById(R.id.categorySpinner);
                EditText product_desc = findViewById(R.id.product_desc_input);
                String imageId = UUID.randomUUID().toString();
                Product product = new Product(
                        product_name.getText().toString(),
                        Double.valueOf(product_price.getText().toString()),
                        Integer.valueOf(prepare_time.getText().toString()),
                        0,
                        product_desc.getText().toString(),
                        "Pasta",
                        Integer.parseInt(person_per_serve.getText().toString()),
                        imageId
                );

                ProgressDialog progressDialog = new ProgressDialog(AddProductActivity.this);
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Saving the new product");
                progressDialog.show();

                firebaseFirestore.collection("products").add(product)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                if(imagePath!=null){
                                    progressDialog.setMessage("Uploading the image");
                                    StorageReference storageReference = firebaseStorage.getReference("productImages")
                                            .child(imageId);
                                    storageReference.putFile(imagePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            progressDialog.dismiss();
                                            product_name.clearComposingText();
                                            Toast.makeText(AddProductActivity.this,"Product saved successfully",Toast.LENGTH_LONG).show();

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            progressDialog.dismiss();
                                            Toast.makeText(AddProductActivity.this,"Oops! Something went wrong",Toast.LENGTH_LONG).show();

                                        }
                                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                                            double progress = (100.0*snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                                        progressDialog.setMessage("Uploading image... "+(int) progress+"% done");
                                        }
                                    });
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(AddProductActivity.this,"Couldn't save the product",Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });

    }

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                       if(result.getData()!=null){
                           imagePath = result.getData().getData();
                           Log.d(TAG, imagePath.getPath());
                           Picasso.get().load(imagePath).centerCrop().resize(200, 200).into(imageButton);

                       }else{
                           Toast.makeText(AddProductActivity.this,"No image selected",Toast.LENGTH_LONG).show();
                       }
                    }else{

                    }
                }
            }
    );

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}