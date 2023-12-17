package lk.software.app.foodorderingadminapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.UUID;

import lk.software.app.foodorderingadminapp.model.Banner;

public class ViewBannersActivity extends AppCompatActivity {
    private FirebaseStorage firebaseStorage;
    private FirebaseFirestore firebaseFirestore;
    private Uri imagePath;
    ImageButton homeBanner;
    ImageButton subBanner;
    ImageButton subBanner2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_banners);
        firebaseStorage = FirebaseStorage.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        homeBanner = findViewById(R.id.imageButton2);
        subBanner = findViewById(R.id.imageButton3);
        subBanner2 = findViewById(R.id.imageButton4);
        homeBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                activityResultLauncherForBanner.launch(Intent.createChooser(intent, "Select Image"));
            }
        });
        subBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                activityResultLauncherSubBanner.launch(Intent.createChooser(intent, "Select Image"));
            }
        });
        subBanner2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                activityResultLauncherSubBanner2.launch(Intent.createChooser(intent, "Select Image"));
            }
        });
    }

    ActivityResultLauncher<Intent> activityResultLauncherForBanner = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        if (result.getData() != null) {
                            imagePath = result.getData().getData();
                            Log.d(AddProductActivity.TAG, imagePath.getPath());
                            homeBanner.setImageURI(imagePath.normalizeScheme());
                            Snackbar snackbar = Snackbar.make(findViewById(R.id.drawerLayout),"Do you want upload this banner",10000).setAction("Upload",
                                    new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            String imageName = UUID.randomUUID().toString();
                                            Banner banner = new Banner("Home Banner",imageName);

                                            firebaseFirestore.collection("banners").add(banner)
                                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                        @Override
                                                        public void onSuccess(DocumentReference documentReference) {
if(imagePath!=null){
    StorageReference storageReference = firebaseStorage.getReference("bannerImages").child(imageName);
    storageReference.putFile(imagePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
        @Override
        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
            Log.i("ImageUploading","Uploaded");
        }
    });
}
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {

                                                        }
                                                    });

                                        }
                                    });
                            snackbar.show();

                        } else {
                            Toast.makeText(ViewBannersActivity.this, "No image selected", Toast.LENGTH_LONG).show();
                        }
                    } else {

                    }
                }
            }
    );
    ActivityResultLauncher<Intent> activityResultLauncherSubBanner = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        if (result.getData() != null) {
                            imagePath = result.getData().getData();
                            Log.d(AddProductActivity.TAG, imagePath.getPath());
                            subBanner.setImageURI(imagePath.normalizeScheme());
                            Snackbar snackbar = Snackbar.make(findViewById(R.id.drawerLayout),"Do you want upload this banner",10000).setAction("Upload",
                                    new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            String imageName = UUID.randomUUID().toString();
                                            Banner banner = new Banner("Side Banner 1",imageName);

                                            firebaseFirestore.collection("banners").add(banner)
                                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                        @Override
                                                        public void onSuccess(DocumentReference documentReference) {
                                                            if(imagePath!=null){
                                                                StorageReference storageReference = firebaseStorage.getReference("bannerImages").child(imageName);
                                                                storageReference.putFile(imagePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                                    @Override
                                                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                                        Log.i("ImageUploading","Uploaded");
                                                                    }
                                                                });
                                                            }
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {

                                                        }
                                                    });

                                        }
                                    });
                            snackbar.show();

                        } else {
                            Toast.makeText(ViewBannersActivity.this, "No image selected", Toast.LENGTH_LONG).show();
                        }
                    } else {

                    }
                }
            }
    );
    ActivityResultLauncher<Intent> activityResultLauncherSubBanner2 = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        if (result.getData() != null) {
                            imagePath = result.getData().getData();
                            Log.d(AddProductActivity.TAG, imagePath.getPath());
                            subBanner2.setImageURI(imagePath.normalizeScheme());
                            Snackbar snackbar = Snackbar.make(findViewById(R.id.drawerLayout),"Do you want upload this banner",Snackbar.LENGTH_INDEFINITE).setAction("Upload",
                                    new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Log.i("Snack Bar","Clicked");
                                            String imageName = UUID.randomUUID().toString();
                                            Banner banner = new Banner("Side Banner 2",imageName);

                                            firebaseFirestore.collection("banners").add(banner)
                                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                        @Override
                                                        public void onSuccess(DocumentReference documentReference) {
                                                            if(imagePath!=null){
                                                                StorageReference storageReference = firebaseStorage.getReference("bannerImages").child(imageName);
                                                                storageReference.putFile(imagePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                                    @Override
                                                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                                        Log.i("ImageUploading","Uploaded");
                                                                    }
                                                                });
                                                            }else{
                                                                Log.i("ImagePath","Null");
                                                            }
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {

                                                        }
                                                    });

                                        }
                                    });
                            snackbar.show();
                        } else {
                            Toast.makeText(ViewBannersActivity.this, "No image selected", Toast.LENGTH_LONG).show();
                        }
                    } else {

                    }
                }
            }
    );
}