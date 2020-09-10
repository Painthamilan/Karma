package com.doordelivery.karma.admin_fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.doordelivery.karma.Img;
import com.doordelivery.karma.R;
import com.doordelivery.karma.Utils;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AdminViewProductActivity extends AppCompatActivity {

    String key,downloadUrl;
    EditText etPrice,etSpecification,etName,etPercentage;
    TextView tvSelect,tvUpload,tvSave,tvAvailability;
    ImageView ivImage,ivDelete;
    DatabaseReference productRef,imageRef,catRef;
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
        tvAvailability=findViewById(R.id.tv_select_availability);
        etName.setSelection(etName.length());
        etPrice.setSelection(etPrice.length());
        etSpecification.setSelection(etSpecification.length());
        rvImage=findViewById(R.id.rv_images);
        etPercentage=findViewById(R.id.et_percentage);
        ivDelete=findViewById(R.id.iv_delete);

        PopupMenu popup = new PopupMenu(this,tvAvailability);
        popup.getMenuInflater().inflate(R.menu.instant_available_menu, popup.getMenu());
        tvAvailability.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                popup.setOnMenuItemClickListener(item->{
                    switch (item.getItemId()) {
                        case R.id.availability_yes:
                            // Toast.makeText(context, ""+postId, Toast.LENGTH_SHORT).show();
                            productRef.child("Availability").setValue("*Available");
                            break;
                        case R.id.availability_no:
                            productRef.child("Availability").setValue("*Out of stock");
                            break;

                    }
                    return true;
                });
                popup.show();
            }
        });

        catRef=FirebaseDatabase.getInstance().getReference().child("Catagories");
        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                           String catName=dataSnapshot.child("ProductCatagory").getValue().toString();
                            String subCatName=dataSnapshot.child("ProductCatagory").getValue().toString();
                            catRef.child(catName).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                  if (dataSnapshot.hasChild("ProductSubCatagory")) {
                                      catRef.child(catName).child(subCatName).child("Products").child(key).removeValue();
                                  }else {
                                      catRef.child(catName).child("Products").child(key).removeValue();
                                  }
                                    Toast.makeText(AdminViewProductActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                productRef.removeValue();


            }
        });
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
                   if (dataSnapshot.hasChild("Percentage")){
                       etPercentage.setText(dataSnapshot.child("Percentage").getValue().toString());
                   }

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
                        postMap.put("Percentage", etPercentage.getText().toString());
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
                        postViewHolder.cfView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                postViewHolder.showDialog(model.getImageUrl(),AdminViewProductActivity.this);
                            }
                        });

                    }
                };
        rvImage.setAdapter(firebaseRecyclerAdapter);
    }
    public static class TopViewHolder extends RecyclerView.ViewHolder {
        public  View cfView;
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

        public void showDialog(String imageUrl, Context context) {

            AlertDialog.Builder dialogBuilder=new AlertDialog.Builder(context, R.style.AlertDialogTheme).setCancelable(false);
            View rowView= LayoutInflater.from(context).inflate(R.layout.image_view_layout,null);
            dialogBuilder.setView(rowView);
            AlertDialog dialog = dialogBuilder.create();
            if (dialog.getWindow() != null) {
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }
            ImageView dialogCancel=rowView.findViewById(R.id.image_close);
            ImageView contentImage=rowView.findViewById(R.id.dialogText);

            Picasso.get().load(imageUrl).into(contentImage);
            dialogCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog.dismiss();

                }
            });

            dialog.show();
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
