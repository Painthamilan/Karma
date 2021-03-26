package com.doordelivery.karma.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.doordelivery.karma.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.doordelivery.karma.activities.Utils.RELEASE_TYPE;

public class UpdateProfileActivity extends AppCompatActivity {
    DatabaseReference cfUserRef;
    EditText etAddress,etNumber;
    String userId;
    TextView tvSave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        etAddress=findViewById(R.id.et_adress);
        etNumber=findViewById(R.id.et_mobile);
        userId=Utils.getUserid();
        cfUserRef=FirebaseDatabase.getInstance().getReference().child(RELEASE_TYPE).child("User").child(userId);
        tvSave=findViewById(R.id.tv_save);

        cfUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
             if (dataSnapshot.hasChild("PhoneNumber")){
                 etNumber.setText(dataSnapshot.child("PhoneNumber").getValue().toString());

                 etNumber.setSelection(etNumber.getText().length());
             }
             if (dataSnapshot.hasChild("Address")){
                 etAddress.setText(dataSnapshot.child("Address").getValue().toString());
                 etAddress.setSelection(etAddress.getText().length());
             }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cfUserRef.child("PhoneNumber").setValue(etNumber.getText().toString());
                cfUserRef.child("Address").setValue(etAddress.getText().toString());
                Intent intent=new Intent(UpdateProfileActivity.this,BottomBarActivity.class);
                startActivity(intent);
            }
        });

    }
}
