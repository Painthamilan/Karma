package com.example.karma.admin_fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.karma.Img;
import com.example.karma.R;
import com.example.karma.Top;
import com.example.karma.Utils;
import com.example.karma.ViewProductActivity;
import com.example.karma.fragments.TopFragment;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AdminViewProductActivity extends AppCompatActivity {

    String key,downloadUrl;
    EditText etPrice,etSpecification,etName;
    TextView tvSelect,tvUpload,tvSave;
    ImageView ivImage;
    DatabaseReference productRef,imageRef;
    Uri imageUri;
    int GalleryPick=1;
    private StorageReference itemStorageRef;
    RecyclerView rvImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_product);
        key=getIntent().getStringExtra("REF_KEY");
        productRef= FirebaseDatabase.getInstance().getReference().child("Products").child(key);
        itemStorageRef= FirebaseStorage.getInstance().getReference().child("ProductImages").child(key);
        etPrice=findViewById(R.id.et_price);
        etSpecification=findViewById(R.id.et_specifications);
        etName=findViewById(R.id.et_product_name);
        tvSelect=findViewById(R.id.tv_select_image);
        tvUpload=findViewById(R.id.tv_upload_image);
        tvSave=findViewById(R.id.tv_save);
        ivImage=findViewById(R.id.iv_product_image);
        etName.setSelection(etName.length());
        etPrice.setSelection(etPrice.length());
        etSpecification.setSelection(etSpecification.length());
        rvImage=findViewById(R.id.rv_images);
        rvImage.setHasFixedSize(true);
        LinearLayoutManager horizontalYalayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,true);
        horizontalYalayoutManager.setStackFromEnd(true);
        rvImage.setLayoutManager(horizontalYalayoutManager);
        showAllImages();
        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               if (dataSnapshot.exists()){
                   Picasso.get().load(dataSnapshot.child("ProductImage").getValue().toString()).into(ivImage);
                   etName.setText(dataSnapshot.child("ProductName").getValue().toString());
                   etPrice.setText(dataSnapshot.child("Price").getValue().toString());
                   etSpecification.setText(dataSnapshot.child("Specifications").getValue().toString());

               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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
        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        HashMap postMap = new HashMap();
                        postMap.put("ProductName", etName.getText().toString());
                        postMap.put("Price", etPrice.getText().toString());
                        postMap.put("Specifications", etSpecification.getText().toString());
                        productRef.updateChildren(postMap).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(AdminViewProductActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

    }

    private void showAllImages() {
        imageRef = FirebaseDatabase.getInstance().getReference().child("Products").child(key).child("DetailImages");

        FirebaseRecyclerAdapter<Img,TopViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Img, TopViewHolder>(
                        Img.class,
                        R.layout.image_layout,
                        TopViewHolder.class,
                        imageRef

                ) {
                    @Override
                    protected void populateViewHolder(TopViewHolder postViewHolder, Img model, int position) {
                        String postKey = getRef(position).getKey();
                        postViewHolder.setImage(model.getImageUrl());

                    }
                };
        rvImage.setAdapter(firebaseRecyclerAdapter);
    }
    public static class TopViewHolder extends RecyclerView.ViewHolder {
        View cfView;
        FirebaseAuth cfAuth = FirebaseAuth.getInstance();
        String userId;
        TextView tvProductName, tvPrice;
        ImageView ivproductImage;
        DatabaseReference topRef;

        public TopViewHolder(@NonNull View itemView) {
            super(itemView);
            cfView = itemView;
            ivproductImage = cfView.findViewById(R.id.iv_image);
        }

        public void setImage(String price) {
            Picasso.get().load(price).into(ivproductImage);
        }
    }
    private void storeImage() {
        if (imageUri != null) {
            final StorageReference filepath = itemStorageRef.child(imageUri.getLastPathSegment() + Utils.createRandomId() + "jpg");
            filepath.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(AdminViewProductActivity.this, "File Uploaded..", Toast.LENGTH_SHORT).show();

                        filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                downloadUrl = uri.toString();
                                savePostInformation(downloadUrl);

                            }
                        });
                    } else {
                        String message = task.getException().getMessage();
                        Toast.makeText(AdminViewProductActivity.this, "UPLOAD ERROR" + message, Toast.LENGTH_SHORT).show();

                    }
                }
            });


        }
        else {
            Toast.makeText(AdminViewProductActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
        }
    }

    private void savePostInformation(String downloadUrl) {
        productRef.child("DetailImages").child(Utils.createRandomId()).child("ImageUrl").setValue(downloadUrl);
        Toast.makeText(this, "done", Toast.LENGTH_SHORT).show();
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
            ivImage.setImageURI(imageUri);
        }

    }
}
