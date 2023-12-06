package lk.software.app.foodorderingadminapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import jp.wasabeef.picasso.transformations.MaskTransformation;
import lk.software.app.foodorderingadminapp.R;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import lk.software.app.foodorderingadminapp.ViewOrdersActivity;
import lk.software.app.foodorderingadminapp.model.OrderStatusEnum;
import lk.software.app.foodorderingadminapp.model.User;
import lk.software.app.foodorderingadminapp.model.UserStatusEnum;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;


public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private FirebaseStorage firebaseStorage;
    private Context context;
    private ArrayList<User> users;
    private FirebaseFirestore firebaseFirestore;


    public UserAdapter(FirebaseStorage firebaseStorage, FirebaseFirestore firebaseFirestore, Context context, ArrayList<User> users) {
        this.firebaseStorage = firebaseStorage;
        this.context = context;
        this.users = users;
        this.firebaseFirestore = firebaseFirestore;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_user_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = users.get(position);
        if (user.getStatus().equals(UserStatusEnum.BLOCKED.toString())) {
            holder.materialSwitch.setChecked(true);
        }
        if (user.getFull_name() != null) {
            holder.name.setText(users.get(position).getFull_name());
        }
        if (user.getEmail() != null) {
            holder.email.setText(user.getEmail());
        }
        if (user.getPhone() != null) {
            holder.phone.setText(user.getPhone());
        }
        holder.register_date.setText(user.getRegister_date());
        if (user.getProfile_img() != null) {
            new Thread(() -> {
                firebaseStorage.getReference("profileImages/" + user.getProfile_img()).getDownloadUrl()
                        .addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                holder.profileImage.post(() -> {
                                    Transformation transformation = new MaskTransformation(context, R.drawable.profile_image_background);
                                    Picasso.get().load(uri).transform(transformation).centerCrop().resize(100, 100).into(holder.profileImage);

                                });


                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
            }).start();
        }

        holder.order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewOrdersActivity.class);
                intent.putExtra("documentId", user.getDocumentId());
                intent.putExtra("customer_name", user.getFull_name());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });

        holder.materialSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    user.setStatus(UserStatusEnum.BLOCKED.toString());
                    updateUserStatus(user);
                    Toast.makeText(context, "user is blocked", Toast.LENGTH_SHORT).show();
                } else {
                    user.setStatus(UserStatusEnum.UNBLOCKED.toString());
                    updateUserStatus(user);
                    Toast.makeText(context, "user is unblocked", Toast.LENGTH_SHORT).show();

                }
            }
        });


    }

    private void updateUserStatus(User user) {
        new Thread(()->{
            firebaseFirestore.collection("customers").document(user.getDocumentId()).set(user);

        }).start();
    }


    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView profileImage;
        Button order_btn;
        TextView name, email, phone, register_date;
        SwitchMaterial materialSwitch;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.profileImage = itemView.findViewById(R.id.customer_img);
            this.materialSwitch = itemView.findViewById(R.id.material_switch);
            this.name = itemView.findViewById(R.id.name);
            this.email = itemView.findViewById(R.id.email);
            this.phone = itemView.findViewById(R.id.phone);
            this.register_date = itemView.findViewById(R.id.date);
            this.order_btn = itemView.findViewById(R.id.button6);

        }
    }
}
