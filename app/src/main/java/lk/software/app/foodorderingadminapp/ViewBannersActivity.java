package lk.software.app.foodorderingadminapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.UUID;

import lk.software.app.foodorderingadminapp.model.Banner;
import lk.software.app.foodorderingadminapp.model.BannerNameEnum;

public class ViewBannersActivity extends AppCompatActivity {
    private FirebaseStorage firebaseStorage;
    private FirebaseFirestore firebaseFirestore;
    private Uri imagePath;
    ImageButton homeBanner;
    ImageButton subBanner;
    ImageButton subBanner2;

    String bannerDocumentId;
    String subBannerDocumentId;
    String subBanner2DocumentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_banners);
        firebaseStorage = FirebaseStorage.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        homeBanner = findViewById(R.id.imageButton2);
        subBanner = findViewById(R.id.imageButton3);
        subBanner2 = findViewById(R.id.imageButton4);

        firebaseFirestore.collection("banners")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        assert value != null;
                        for (DocumentSnapshot documentSnapshot : value.getDocuments()) {
                            Banner banner = documentSnapshot.toObject(Banner.class);
                            if (banner != null) {
                                if(banner.getDocumentId()!=null){
                                    if (banner.getDocumentId().equals(BannerNameEnum.HOME_BANNER.toString())) {
                                        bannerDocumentId = banner.getDocumentId();
                                        if (banner.getBannerImage() != null) {
                                            firebaseStorage.getReference("bannerImages/" + banner.getBannerImage()).getDownloadUrl()
                                                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                        @Override
                                                        public void onSuccess(Uri uri) {
                                                            homeBanner.setImageURI(uri);
                                                        }
                                                    });
                                        }
                                    }else if(banner.getDocumentId().equals(BannerNameEnum.SUB_BANNER_1.toString())){
                                        subBannerDocumentId = banner.getDocumentId();
                                        if (banner.getBannerImage() != null) {
                                            firebaseStorage.getReference("bannerImages/" + banner.getBannerImage()).getDownloadUrl()
                                                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                        @Override
                                                        public void onSuccess(Uri uri) {
                                                            subBanner.setImageURI(uri);
                                                        }
                                                    });
                                        }
                                    }else if(banner.getDocumentId().equals(BannerNameEnum.SUB_BANNER_2.toString())){
                                        subBanner2DocumentId = banner.getDocumentId();
                                        if (banner.getBannerImage() != null) {
                                            firebaseStorage.getReference("bannerImages/" + banner.getBannerImage()).getDownloadUrl()
                                                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                        @Override
                                                        public void onSuccess(Uri uri) {
                                                            subBanner.setImageURI(uri);
                                                        }
                                                    });
                                        }
                                    }
                                }
                            }

                        }
                    }
                });
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
                            Snackbar snackbar = Snackbar.make(findViewById(R.id.drawerLayout), "Do you want upload this banner", 10000).setAction("Upload",
                                    new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if(bannerDocumentId==null){
                                                String imageName = UUID.randomUUID().toString();
                                                Banner banner = new Banner(BannerNameEnum.HOME_BANNER.toString(), imageName);

                                                Task<DocumentReference> documentReferenceTask = firebaseFirestore.collection("banners").add(banner);
                                                        documentReferenceTask.addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                            @Override
                                                            public void onSuccess(DocumentReference documentReference) {
                                                                if (imagePath != null) {
                                                                    StorageReference storageReference = firebaseStorage.getReference("bannerImages").child(imageName);
                                                                    storageReference.putFile(imagePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                                        @Override
                                                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                                            if (documentReferenceTask.isComplete()){
                                                                                String id = documentReferenceTask.getResult().get().getResult().getId();
                                                                                banner.setDocumentId(id);
                                                                            }
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
                                            }else{

                                            }


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
                            Snackbar snackbar = Snackbar.make(findViewById(R.id.drawerLayout), "Do you want upload this banner", 10000).setAction("Upload",
                                    new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            String imageName = UUID.randomUUID().toString();
                                            Banner banner = new Banner(BannerNameEnum.SUB_BANNER_1.toString(), imageName);

                                            Task<DocumentReference> documentReferenceTask = firebaseFirestore.collection("banners").add(banner);
                                            documentReferenceTask.addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {

                                                    if (imagePath != null) {
                                                        StorageReference storageReference = firebaseStorage.getReference("bannerImages").child(imageName);
                                                        storageReference.putFile(imagePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                            @Override
                                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                                String id = documentReferenceTask.getResult().get().getResult().getId();
                                                                banner.setDocumentId(id);
                                                                Log.i("ImageUploading", "Uploaded");
                                                            }
                                                        });
                                                    }
                                                }
                                            });
                                            documentReferenceTask.addOnFailureListener(new OnFailureListener() {
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
                            Snackbar snackbar = Snackbar.make(findViewById(R.id.drawerLayout), "Do you want upload this banner", Snackbar.LENGTH_INDEFINITE).setAction("Upload",
                                    new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Log.i("Snack Bar", "Clicked");
                                            String imageName = UUID.randomUUID().toString();
                                            Banner banner = new Banner(BannerNameEnum.SUB_BANNER_2.toString(), imageName);

                                            Task<DocumentReference> documentReferenceTask = firebaseFirestore.collection("banners").add(banner);
                                                    documentReferenceTask.addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                        @Override
                                                        public void onSuccess(DocumentReference documentReference) {
                                                            if (imagePath != null) {
                                                                StorageReference storageReference = firebaseStorage.getReference("bannerImages").child(imageName);
                                                                storageReference.putFile(imagePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                                    @Override
                                                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                                        String id = documentReferenceTask.getResult().get().getResult().getId();
                                                                        banner.setDocumentId(id);
                                                                    }
                                                                });
                                                            } else {
                                                                Log.i("ImagePath", "Null");
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