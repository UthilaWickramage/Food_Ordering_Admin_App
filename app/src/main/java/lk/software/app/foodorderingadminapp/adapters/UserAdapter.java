package lk.software.app.foodorderingadminapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import jp.wasabeef.picasso.transformations.MaskTransformation;
import lk.software.app.foodorderingadminapp.R;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import lk.software.app.foodorderingadminapp.ViewOrdersActivity;
import lk.software.app.foodorderingadminapp.model.User;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;


public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private FirebaseStorage firebaseStorage;
    private Context context;
    private ArrayList<User> users;


    public UserAdapter(FirebaseStorage firebaseStorage, Context context, ArrayList<User> users) {
        this.firebaseStorage = firebaseStorage;
        this.context = context;
        this.users = users;
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
        if(user.getProfile_img()!=null){
        firebaseStorage.getReference("profileImages/"+user.getProfile_img()).getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                            Transformation transformation = new MaskTransformation(context, R.drawable.profile_image_background);
                            Picasso.get().load(uri).transform(transformation).centerCrop().resize(100, 100).into(holder.profileImage);


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
        }

        holder.order_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewOrdersActivity.class);
                intent.putExtra("documentId",user.getDocumentId());
                intent.putExtra("customer_name",user.getFull_name());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });





    }
    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView profileImage;
        Button button,order_btn;
        TextView name,email,phone,register_date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.profileImage = itemView.findViewById(R.id.customer_img);
            this.button = itemView.findViewById(R.id.button4);
            this.name = itemView.findViewById(R.id.name);
            this.email = itemView.findViewById(R.id.email);
            this.phone = itemView.findViewById(R.id.phone);
            this.register_date = itemView.findViewById(R.id.date);
            this.order_btn = itemView.findViewById(R.id.button6);

        }
    }
}
