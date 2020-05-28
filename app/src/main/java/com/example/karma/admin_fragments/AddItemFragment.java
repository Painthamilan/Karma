package com.example.karma.admin_fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;

import com.example.karma.R;
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

import static android.app.Activity.RESULT_OK;

public class AddItemFragment extends Fragment {
    private EditText etProductName,etPrice;
    private ImageView ivProductImage;
    private TextView tvSelectcatagory,tvUpload,tvSelectImage;
    private String seletedCatagory,curDate,curTime,randomid,downloadUrl;
    private StorageReference itemStorageRef;
    private DatabaseReference itemsRef;
    private long countPosts;

    private static final int GalleryPick = 1;
    Uri imageUri;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
         View root = inflater.inflate(R.layout.fragment_add_items, container, false);
         TextView textView = root.findViewById(R.id.text_dashboard);
         etProductName=root.findViewById(R.id.et_product_name);
         etPrice=root.findViewById(R.id.et_price);
         ivProductImage=root.findViewById(R.id.iv_product_image);
         tvSelectcatagory=root.findViewById(R.id.tv_select_catogary);
         tvSelectImage=root.findViewById(R.id.tv_select_image);
         tvUpload=root.findViewById(R.id.tv_upload);

         itemStorageRef= FirebaseStorage.getInstance().getReference().child("ProductImages");
         itemsRef= FirebaseDatabase.getInstance().getReference().child("Products");
        PopupMenu popup = new PopupMenu(AddItemFragment.this.getContext(),tvSelectcatagory);
        popup.getMenuInflater().inflate(R.menu.main_catagory, popup.getMenu());
        tvSelectcatagory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                popup.setOnMenuItemClickListener(item->{
                    switch (item.getItemId()) {
                        case R.id.cat_electronics:
                            seletedCatagory="Electronics";
                            tvSelectcatagory.setText(seletedCatagory);
                            break;
                        case R.id.cat_books:
                            seletedCatagory="Books";
                            tvSelectcatagory.setText(seletedCatagory);
                            break;
                        case R.id.cat_foods:
                          seletedCatagory="Foods";
                            tvSelectcatagory.setText(seletedCatagory);
                            break;
                    }
                    return true;
                });
                popup.show();
            }
        });

        tvSelectImage.setOnClickListener(new View.OnClickListener() {
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

        return root;
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
                        Toast.makeText(AddItemFragment.this.getContext(), "File Uploaded..", Toast.LENGTH_SHORT).show();

                        filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                    downloadUrl = uri.toString();
                                    savePostInformation();
                                    
                            }
                        });
                    } else {
                        String message = task.getException().getMessage();
                        Toast.makeText(AddItemFragment.this.getContext(), "UPLOAD ERROR" + message, Toast.LENGTH_SHORT).show();

                    }
                }
            });


        }
        else {
            Toast.makeText(AddItemFragment.this.getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
        }


    }

    private void savePostInformation() {
        itemsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    countPosts = dataSnapshot.getChildrenCount();
                } else {
                    countPosts = 0;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        HashMap postMap = new HashMap();
        postMap.put("ProductId",randomid);
        postMap.put("ProductName", etProductName.getText().toString());
        postMap.put("ProductCatagory", seletedCatagory);
        postMap.put("Price",etPrice.getText().toString());
        postMap.put("ProductImage",downloadUrl);

        postMap.put("Counter", countPosts);
        itemsRef.child(randomid).updateChildren(postMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()){
                    Toast.makeText(AddItemFragment.this.getContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                }
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