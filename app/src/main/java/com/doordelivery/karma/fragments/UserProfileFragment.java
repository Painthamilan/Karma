package com.doordelivery.karma.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.doordelivery.karma.LoginActivity;
import com.doordelivery.karma.R;
import com.doordelivery.karma.SignupActivity;
import com.doordelivery.karma.UpdateProfileActivity;
import com.doordelivery.karma.ViewMyOrdersActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class UserProfileFragment extends Fragment {
    FirebaseAuth cfAuth;
    TextView tvMyOrders,tvName,tvUpdateProfile,tvLogin,tvSignup;
    ImageView ivImage;
    ConstraintLayout layoutProfile,layoutLogin;
    String userId;
    DatabaseReference cfUserRef;
    private FirebaseUser mCurrentUser;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_user_profile, container, false);
        cfAuth=FirebaseAuth.getInstance();
        if (cfAuth.getCurrentUser() != null) {
            userId=cfAuth.getCurrentUser().getUid();

        }
        ivImage=root.findViewById(R.id.iv_profile);
        final TextView textView = root.findViewById(R.id.text_notifications);
        tvMyOrders=root.findViewById(R.id.tv_my_orders);
        layoutProfile=root.findViewById(R.id.constraint_layout_ptofile);
        layoutLogin=root.findViewById(R.id.constraint_layout_not_logged_in);
        tvName=root.findViewById(R.id.tv_name);

        tvUpdateProfile=root.findViewById(R.id.tv_update_profile);
        tvLogin=root.findViewById(R.id.tv_login);
        tvSignup=root.findViewById(R.id.tv_signin);
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });
        tvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), SignupActivity.class);
                startActivity(intent);
            }
        });


        if (TextUtils.isEmpty(userId)){
            layoutLogin.setVisibility(View.VISIBLE);
            layoutProfile.setVisibility(View.INVISIBLE);
        }else {
            
            layoutProfile.setVisibility(View.VISIBLE);
            layoutLogin.setVisibility(View.INVISIBLE);
            showProfile(userId);
        }



        tvMyOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), ViewMyOrdersActivity.class);
                startActivity(intent);
            }
        });

        tvUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), UpdateProfileActivity.class);
                startActivity(intent);
            }
        });

        textView.setText("Logout");
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cfAuth.signOut();
                Intent intent=new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);

            }
        });

        return root;
    }

    private void showProfile(String userId) {
        cfUserRef= FirebaseDatabase.getInstance().getReference().child("User").child(userId);
        cfUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               if (dataSnapshot.exists()){
                   try {
                       String profileImage=cfAuth.getCurrentUser().getPhotoUrl().toString();
                       Picasso.get().load(profileImage).into(ivImage);
                   }catch (Exception e){
                       e.printStackTrace();
                   }

                   String name=dataSnapshot.child("DisplayName").getValue().toString();
                   tvName.setText(name);
               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}