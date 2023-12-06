package lk.software.app.foodorderingadminapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.UUID;

import lk.software.app.foodorderingadminapp.model.Category;

public class AddCategoryActivity extends AppCompatActivity {


    private FirebaseStorage firebaseStorage;
    private FirebaseFirestore firebaseFirestore;

    private ImageButton imageButton;
    private Uri imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        imageButton = findViewById(R.id.imageButton);

        findViewById(R.id.imageView3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //loading image to the image button
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                activityResultLauncher.launch(Intent.createChooser(intent, "Select Image"));
            }
        });

        //adding category to firebase

        findViewById(R.id.addCategoryBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText editText = findViewById(R.id.categoryName);


                String title = editText.getText().toString();
                if(title.isEmpty()){
                    editText.setError("Category name can't be empty");
                    return;
                }

                if(imagePath==null){
                    Toast.makeText(AddCategoryActivity.this,"Please select an image",Toast.LENGTH_SHORT).show();
                    return;
                }

                String imageId = UUID.randomUUID().toString();

                Category category = new Category(title, imageId);

                ProgressDialog progressDialog = new ProgressDialog(AddCategoryActivity.this);
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Saving the new category");
                progressDialog.show();

                new Thread(()->{
                    firebaseFirestore.collection("categories").add(category)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    if (imagePath != null) {
                                        AddCategoryActivity.this.runOnUiThread(()->{
                                            progressDialog.setMessage("Uploading the category image");
                                        });
                                        StorageReference reference = firebaseStorage.getReference("categoryImages")
                                                .child(imageId);
                                        reference.putFile(imagePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                AddCategoryActivity.this.runOnUiThread(progressDialog::dismiss);
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                AddCategoryActivity.this.runOnUiThread(progressDialog::dismiss);
                                                Toast.makeText(AddCategoryActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                                                double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                                                AddCategoryActivity.this.runOnUiThread(()->{
                                                    progressDialog.setMessage("Uploading image... " + (int) progress + "% done");

                                                });
                                            }
                                        });
                                    }

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    AddCategoryActivity.this.runOnUiThread(progressDialog::dismiss);
                                    Toast.makeText(AddCategoryActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

                                }
                            });
                }).start();

            }
        });
    }



    ActivityResultLauncher activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getData()!=null){

                        imagePath = result.getData().getData();
                        Picasso.get().load(imagePath).centerCrop().resize(200, 200).into(imageButton);
                    }else{
                        Toast.makeText(AddCategoryActivity.this,"No Image Selected",Toast.LENGTH_LONG).show();
                    }

                }
            }
    );
}