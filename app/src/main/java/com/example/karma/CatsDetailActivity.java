package com.example.karma;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.karma.admin_fragments.AddItemFragment;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
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
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class CatsDetailActivity extends AppCompatActivity {

    String catName,mainCatName,catImage,curDate,curTime,randomid,downloadUrl,imageLabel;

    TextView tvSelectImage,tvUploadImage,tvCatName;
    ImageView ivCatImage;
    RecyclerView rvSubCats;
    DatabaseReference cf_displaySubCatsRef,updateDetailsRef;
    StorageReference storeCatImageRef;
    Uri imageUri;

    boolean isSub;
    public static int GallleryPik=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cats_detail);
        catName=getIntent().getStringExtra("CAT_NAME");
        catImage=getIntent().getStringExtra("CAT_IMAGE");
        mainCatName=getIntent().getStringExtra("MAIN_CAT_NAME");
        isSub=getIntent().getBooleanExtra("isSub",false);
        tvCatName=findViewById(R.id.tv_cat_name);
        tvSelectImage=findViewById(R.id.tv_select_image);
        tvUploadImage=findViewById(R.id.tv_update_image);

        ivCatImage=findViewById(R.id.iv_cats_image);

        tvCatName.setText(catName);
        if(!TextUtils.isEmpty(catImage)){
            Picasso.get().load(catImage).into(ivCatImage);
        }

        cf_displaySubCatsRef =FirebaseDatabase.getInstance().getReference().child("Catagories").child(catName).child("SubCatagories");
        rvSubCats=findViewById(R.id.rv_list_sub_cats);
        if (!isSub) {
            rvSubCats.setHasFixedSize(true);
            // rvPosts.setLayoutManager(new LinearLayoutManager(getContext()));
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
            mLayoutManager.setReverseLayout(true);
            mLayoutManager.setStackFromEnd(true);
            // Set the layout manager to your recyclerview
            rvSubCats.setLayoutManager(mLayoutManager);

            storeCatImageRef= FirebaseStorage.getInstance().getReference().child("Catagories").child(catName);
            updateDetailsRef=FirebaseDatabase.getInstance().getReference().child("Catagories").child(catName);
            imageLabel="CatagoryImage";
            showAllSubCats();
        }else {
            rvSubCats.setVisibility(View.INVISIBLE);
            imageLabel="SubCatagoryImage";
            storeCatImageRef= FirebaseStorage.getInstance().getReference().child("Catagories").child(catName).child("SubCatagories");
            updateDetailsRef=FirebaseDatabase.getInstance().getReference().child("Catagories").child(mainCatName).child("SubCatagories").child(catName);
        }

        tvSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        tvUploadImage.setOnClickListener(new View.OnClickListener() {
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

        if (imageUri != null) {
            final StorageReference filepath = storeCatImageRef.child(imageUri.getLastPathSegment() + randomid + "jpg");
            filepath.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(CatsDetailActivity.this, "File Uploaded..", Toast.LENGTH_SHORT).show();

                        filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                downloadUrl = uri.toString();
                                updateDetais();

                            }
                        });
                    } else {
                        String message = task.getException().getMessage();
                        Toast.makeText(CatsDetailActivity.this, "UPLOAD ERROR" + message, Toast.LENGTH_SHORT).show();

                    }
                }
            });


        }
        else {
            Toast.makeText(CatsDetailActivity.this, "Please select image", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateDetais() {
        updateDetailsRef.child(imageLabel).setValue(downloadUrl);
        Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show();
    }


    private void showAllSubCats() {
        FirebaseRecyclerAdapter<SubCats, CatsViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<SubCats, CatsViewHolder>(
                        SubCats.class,
                        R.layout.list_cats_layout,
                        CatsViewHolder.class,
                        cf_displaySubCatsRef

                ) {
                    @Override
                    protected void populateViewHolder(CatsViewHolder postViewHolder, SubCats model, int position) {
                        String postKey = getRef(position).getKey();
                        postViewHolder.setCatagoryName(model.getSubCatagoryName());
                        postViewHolder.setCatagoryImage(model.getSubCatagoryImage());

                        postViewHolder.tvUpdateImage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent=new Intent(CatsDetailActivity.this, CatsDetailActivity.class);
                                intent.putExtra("REF_KEY",postKey);
                                intent.putExtra("CAT_NAME",model.getSubCatagoryName());
                                intent.putExtra("MAIN_CAT_NAME",catName);
                                intent.putExtra("CAT_IMAGE",model.getSubCatagoryImage());
                                intent.putExtra("isSub",true);
                                startActivity(intent);
                            }
                        });
                    }
                };

        rvSubCats.setAdapter(firebaseRecyclerAdapter);
    }
    public static class CatsViewHolder extends RecyclerView.ViewHolder {
        View cfView;

        TextView tvCatName,tvUpdateImage;
        ImageView ivCatImage;
        public CatsViewHolder(@NonNull View itemView) {
            super(itemView);
            cfView = itemView;
            tvCatName=cfView.findViewById(R.id.tv_cat_name);
            ivCatImage=cfView.findViewById(R.id.iv_cats_image);
            tvUpdateImage=cfView.findViewById(R.id.tv_update_image);
        }


        public void setCatagoryName(String catagoryName) {
            tvCatName.setText(catagoryName);

        }

        public void setCatagoryImage(String catagoryImage) {
            if (catagoryImage!=""){
                Picasso.get().load(catagoryImage).into(ivCatImage);
            }
        }
    }

    private void openGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GallleryPik);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GallleryPik && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            ivCatImage.setImageURI(imageUri);
        }

    }

}
