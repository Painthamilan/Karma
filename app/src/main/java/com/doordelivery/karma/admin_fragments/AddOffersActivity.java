package com.doordelivery.karma.admin_fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.doordelivery.karma.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

public class AddOffersActivity extends AppCompatActivity {
    private EditText etProductName,etPrice,etPercentage,etSpecifications;
    private ImageView ivProductImage;
    private TextView tvSelect,tvUpload;

    private String seletedCatagory="",curDate,curTime,randomid,downloadUrl,selectedSubCatagory,specifications;
    private StorageReference itemStorageRef;
    private DatabaseReference itemsRef;
    private long countPosts;
    private static final int GalleryPick = 1;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_offers);
        etPrice=findViewById(R.id.et_price);
        etProductName=findViewById(R.id.et_product_name);
        etPercentage=findViewById(R.id.et_percentage);
        etSpecifications=findViewById(R.id.et_specifications);
        ivProductImage=findViewById(R.id.iv_product_image);
        tvUpload=findViewById(R.id.tv_upload);
        tvSelect=findViewById(R.id.tv_select_image);

        itemStorageRef= FirebaseStorage.getInstance().getReference().child("OfferImages");
        itemsRef= FirebaseDatabase.getInstance().getReference().child("Offers");
        tvSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              openGallery();
            }
        });
        tvUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeImage();
            }
        });

    }
    private void storeImage() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        curDate = dateFormat.format(new Date());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH-mm-ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        curTime = timeFormat.format(new Date());
        randomid=curDate.replace("-", "")+curTime.replace("-", "");

        if (imageUri != null&&!etPrice.getText().toString().isEmpty()&&!etProductName.getText().toString().isEmpty()) {
            final StorageReference filepath = itemStorageRef.child(imageUri.getLastPathSegment() + randomid + "jpg");
            filepath.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(AddOffersActivity.this, "File Uploaded..", Toast.LENGTH_SHORT).show();

                        filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                downloadUrl = uri.toString();
                                savePostInformation();

                            }
                        });
                    } else {
                        String message = task.getException().getMessage();
                        Toast.makeText(AddOffersActivity.this, "UPLOAD ERROR" + message, Toast.LENGTH_SHORT).show();

                    }
                }
            });


        }
        else {
            Toast.makeText(AddOffersActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
        }


    }

    private void savePostInformation() {
        itemsRef= FirebaseDatabase.getInstance().getReference().child("Offers");
        itemsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    countPosts = dataSnapshot.getChildrenCount();
                } else {
                    countPosts = 0;
                }

                HashMap postMap = new HashMap();
                postMap.put("ProductId",randomid);
                postMap.put("ProductName", etProductName.getText().toString());
                postMap.put("Percentage", etPercentage.getText().toString());
                postMap.put("Price",etPrice.getText().toString());
                postMap.put("ProductImage",downloadUrl);
                postMap.put("Specifications",etSpecifications.getText().toString());
                postMap.put("Counter", countPosts);
                itemsRef.child(randomid).updateChildren(postMap).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()){
                            Toast.makeText(AddOffersActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void openGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GalleryPick);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GalleryPick && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            ivProductImage.setImageURI(imageUri);
        }

    }
}
