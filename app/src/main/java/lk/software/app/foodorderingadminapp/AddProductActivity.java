package lk.software.app.foodorderingadminapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;

import lk.software.app.foodorderingadminapp.model.Category;
import lk.software.app.foodorderingadminapp.model.Product;

public class AddProductActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static final String TAG = AddProductActivity.class.getName();
    private FirebaseFirestore firebaseFirestore;

    private FirebaseStorage firebaseStorage;
    private ImageButton imageButton;
    private Uri imagePath;

    ArrayList<String> categories;

    String category_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        firebaseStorage = FirebaseStorage.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        Spinner spinner = findViewById(R.id.categorySpinner);
        spinner.setOnItemSelectedListener(this);
        categories = new ArrayList<>();
        categories.add("Select Category");
        loadCategoryNames();
        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
// Specify the layout to use when the list of choices appears.
        stringArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner.
        spinner.setAdapter(stringArrayAdapter);

        findViewById(R.id.imageView3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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
                EditText product_desc = findViewById(R.id.product_desc_input);
                String name = product_name.getText().toString();
                String price = product_price.getText().toString();
                String prep_time = prepare_time.getText().toString();
                String desc = product_desc.getText().toString();
                String persons = person_per_serve.getText().toString();
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat currentDate = new SimpleDateFormat("MM.dd.yyyy");
                String saveCurrentDate = currentDate.format(calendar.getTime());
                if (name.isEmpty()) {
                    product_name.setError("name can't be empty");
                    return;
                }

                if (price.isEmpty()) {
                    product_price.setError("price can't be empty");
                    return;
                }

                if (desc.isEmpty()) {
                    product_desc.setError("please add a description");
                    return;
                }
                if (persons.isEmpty()) {
                    product_desc.setError("persons per serve can't be empty");
                    return;
                }

                if (imagePath == null) {
                    Toast.makeText(AddProductActivity.this, "Please select an image", Toast.LENGTH_SHORT).show();
                    return;
                }

                String imageId = UUID.randomUUID().toString();
                Product product = new Product(
                        name,
                        Double.parseDouble(price),
                        Integer.parseInt(prep_time),
                        0,
                        desc,
                        category_name,
                        Integer.parseInt(persons),
                        imageId,
                        saveCurrentDate);

                ProgressDialog progressDialog = new ProgressDialog(AddProductActivity.this);
                progressDialog.setCancelable(false);
                progressDialog.setMessage("Saving the new product");
                progressDialog.show();

                new Thread(() -> {
                    firebaseFirestore.collection("products").add(product)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    if (imagePath != null) {
                                        AddProductActivity.this.runOnUiThread(() -> {
                                            progressDialog.setMessage("Uploading the image");
                                        });
                                        StorageReference storageReference = firebaseStorage.getReference("productImages")
                                                .child(imageId);
                                        storageReference.putFile(imagePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                AddProductActivity.this.runOnUiThread(progressDialog::dismiss);
                                                product_name.clearComposingText();
                                                Toast.makeText(AddProductActivity.this, "Product saved successfully", Toast.LENGTH_LONG).show();

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                AddProductActivity.this.runOnUiThread(progressDialog::dismiss);
                                                Toast.makeText(AddProductActivity.this, "Oops! Something went wrong", Toast.LENGTH_LONG).show();

                                            }
                                        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                                                double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                                                AddProductActivity.this.runOnUiThread(() -> {
                                                    progressDialog.setMessage("Uploading image... " + (int) progress + "% done");

                                                });
                                            }
                                        });
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    AddProductActivity.this.runOnUiThread(progressDialog::dismiss);
                                    Toast.makeText(AddProductActivity.this, "Couldn't save the product", Toast.LENGTH_LONG).show();
                                }
                            });
                }).start();
            }
        });

    }

    private void loadCategoryNames() {
        new Thread(()->{
            firebaseFirestore.collection("categories")
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            for (DocumentSnapshot snapshot : value.getDocuments()) {
                                Category category = snapshot.toObject(Category.class);
                                categories.add(category.getName());
                            }

                        }
                    });
        }).start();
    }

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        if (result.getData() != null) {
                            imagePath = result.getData().getData();
                            Log.d(TAG, imagePath.getPath());
                            Picasso.get().load(imagePath).centerCrop().resize(200, 200).into(imageButton);

                        } else {
                            Toast.makeText(AddProductActivity.this, "No image selected", Toast.LENGTH_LONG).show();
                        }
                    } else {

                    }
                }
            }
    );

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        category_name = parent.getItemAtPosition(position).toString();

        Log.d("onItemSelected", category_name);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Toast.makeText(AddProductActivity.this, "nothing selected", Toast.LENGTH_LONG).show();

    }
}