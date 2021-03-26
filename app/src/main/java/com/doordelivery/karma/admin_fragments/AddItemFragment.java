package com.doordelivery.karma.admin_fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
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

import static android.app.Activity.RESULT_OK;
import static com.doordelivery.karma.activities.Utils.RELEASE_TYPE;

public class AddItemFragment extends Fragment {
    private EditText etProductName,etPrice,etSpecifications,etPercentage;
    private ImageView ivProductImage;
    private TextView tvSelectcatagory,tvUpload,tvSelectImage,tvSelectSubCatagory;
    private String seletedCatagory="",curDate,curTime,randomid,downloadUrl,selectedSubCatagory,specifications;
    private StorageReference itemStorageRef;
    private DatabaseReference itemsRef,catRef,instantRef;
    private long countPosts;
    private boolean isinstant,hasSubCat;
    private static final int GalleryPick = 1;
    Uri imageUri;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
         View root = inflater.inflate(R.layout.fragment_add_items, container, false);
         TextView textView = root.findViewById(R.id.text_dashboard);
         etProductName=root.findViewById(R.id.et_product_name);
         etPrice=root.findViewById(R.id.et_price);
         etSpecifications=root.findViewById(R.id.et_specifications);
         etPercentage=root.findViewById(R.id.et_percentage);
         ivProductImage=root.findViewById(R.id.iv_product_image);
         tvSelectcatagory=root.findViewById(R.id.tv_select_catogary);
         tvSelectImage=root.findViewById(R.id.tv_select_image);
         tvUpload=root.findViewById(R.id.tv_upload);
         tvSelectSubCatagory=root.findViewById(R.id.tv_select_sub_catogary);

         itemStorageRef= FirebaseStorage.getInstance().getReference().child(RELEASE_TYPE).child("ProductImages");
         itemsRef= FirebaseDatabase.getInstance().getReference().child(RELEASE_TYPE).child("Products");
         catRef=FirebaseDatabase.getInstance().getReference().child(RELEASE_TYPE).child("Catagories");
         hasSubCat=false;
        PopupMenu popup = new PopupMenu(AddItemFragment.this.getContext(),tvSelectcatagory);

        popup.getMenuInflater().inflate(R.menu.main_catagory, popup.getMenu());
        isinstant=false;
        tvSelectcatagory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tvSelectSubCatagory.setVisibility(View.INVISIBLE);
                popup.setOnMenuItemClickListener(item->{
                    switch (item.getItemId()) {
                        case R.id.main_cat_mobile:
                            seletedCatagory="Mobile";
                            tvSelectSubCatagory.setVisibility(View.INVISIBLE);
                            break;

                        case R.id.main_cat_mobile_accessories:
                            seletedCatagory="Mobile Accessories";
                            tvSelectSubCatagory.setVisibility(View.INVISIBLE);
                            break;

                        case R.id.main_cat_sports:
                          seletedCatagory="Sports";
                          tvSelectSubCatagory.setVisibility(View.VISIBLE);
                          hasSubCat=true;
                            break;

                        case R.id.main_cat_education:
                            seletedCatagory="Education";
                            tvSelectSubCatagory.setVisibility(View.VISIBLE);
                            hasSubCat=true;
                            break;

                        case R.id.main_cat_instant:
                            seletedCatagory="Instant";
                            isinstant=true;
                            break;

                        case R.id.main_cat_cab:
                            seletedCatagory="Cab";

                            break;

                        case R.id.main_cat_gifts:
                            seletedCatagory="Gifts";

                            break;
                        case R.id.main_cat_dth:
                            seletedCatagory="DTH";
                            break;
                        case R.id.main_cat_cctv:
                            seletedCatagory="CCTV";
                            break;
                        case R.id.main_cat_event_management:
                            seletedCatagory="Event Management";
                            break;
                        case R.id.main_cat_accessories:
                            seletedCatagory="Accessories";
                            break;
                        case R.id.main_cosmetics:
                            seletedCatagory="Cosmetics";
                            break;
                        case R.id.main_cat_birthday_party:
                            seletedCatagory="Birthday Party";
                            break;
                        case R.id.main_cat_second_hand:
                            seletedCatagory="Second Hand";
                            break;
                        case R.id.main_cat_essential_goods:
                            seletedCatagory="Essential Goods";
                            break;
                        case R.id.main_cat_clouth:
                            seletedCatagory="Clothings";
                            break;
                        case R.id.main_cake:
                            seletedCatagory="Cake";
                            break;
                        case R.id.main_cat_library:
                            seletedCatagory="Library";
                            break;
                        case R.id.main_cat_flowers:
                            seletedCatagory="Flowers & Bouquests";
                            break;
                    }
                    tvSelectcatagory.setText(seletedCatagory);
                    return true;
                });
                popup.show();

            }
        });

        tvSelectSubCatagory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateSubmenus();
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

    private void validateSubmenus() {
      switch (seletedCatagory){
          case "Sports":
              PopupMenu subMenuSports=new PopupMenu(AddItemFragment.this.getContext(),tvSelectcatagory);
              subMenuSports.getMenuInflater().inflate(R.menu.sub_catagory_sports, subMenuSports.getMenu());
              subMenuSports.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                  @Override
                  public boolean onMenuItemClick(MenuItem item) {
                      switch (item.getItemId()) {
                          case R.id.sub_cat_sports_cards:
                              selectedSubCatagory="Cards";
                              break;
                          case R.id.sub_cat_sports_carrom:
                              selectedSubCatagory="Carrom";
                              break;
                          case R.id.sub_cat_sports_tennis_ball:
                              selectedSubCatagory="Tennis Ball";
                              break;
                          case R.id.sub_cat_sports_volly_ball:
                              selectedSubCatagory="Volly Ball";
                              break;
                      }
                      tvSelectSubCatagory.setText(selectedSubCatagory);
                      return true;
                  }
              });
              subMenuSports.show();
              break;

          case "Education":
              PopupMenu subMenuEducation=new PopupMenu(AddItemFragment.this.getContext(),tvSelectcatagory);
              subMenuEducation.getMenuInflater().inflate(R.menu.sub_catagory_education, subMenuEducation.getMenu());


              subMenuEducation.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
              @Override
              public boolean onMenuItemClick(MenuItem item) {
                  switch (item.getItemId()) {
                      case R.id.sub_cat_education_al:
                          selectedSubCatagory="Advance Level";
                          break;
                      case R.id.sub_cat_education_ol:
                          selectedSubCatagory="Ordianary Level";
                          break;
                      case R.id.sub_cat_education_other_stationary:
                          selectedSubCatagory="Other Stationary";
                          break;
                      case R.id.sub_cat_education_syllabus:
                          selectedSubCatagory="Scholardhip";
                          break;
                  }
                  tvSelectSubCatagory.setText(selectedSubCatagory);
                  return true;
              }
          });
              subMenuEducation.show();
              break;
              default:
                  Toast.makeText(AddItemFragment.this.getContext(), "Please select Main catagory..", Toast.LENGTH_SHORT).show();
      }

    }
   private void saveCats(boolean hasCats, String seletedCatagory){
       catRef.child(seletedCatagory).child("CatagoryName").setValue(this.seletedCatagory);
        if (hasCats){
            catRef.child(seletedCatagory).child("SubCatagories").child(selectedSubCatagory).child("SubCatagoryName").setValue(selectedSubCatagory);
            catRef.child(seletedCatagory).child("SubCatagories").child(selectedSubCatagory).child("Products").child(randomid).child("ProductId").setValue(randomid);
        }else{
            catRef.child(seletedCatagory).child("Products").child(randomid).child("ProductId").setValue(randomid);
        }

       catRef.child(this.seletedCatagory).child("HasSub").setValue(hasCats);
       // catRef.child(seletedCatagory).child(selectedSubCatagory).child("ProductId").setValue(randomid);

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
        if (isinstant){
            instantRef= FirebaseDatabase.getInstance().getReference().child(RELEASE_TYPE).child("Instants");
            instantRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    HashMap postMap = new HashMap();
                    postMap.put("ProductId",randomid);
                    postMap.put("ProductName", etProductName.getText().toString());
                    postMap.put("ProductCatagory", seletedCatagory);
                    postMap.put("ProductSubCatagory", selectedSubCatagory);
                    postMap.put("Price",etPrice.getText().toString());
                    postMap.put("Percentage",etPercentage.getText().toString());
                    postMap.put("ProductImage",downloadUrl);
                    postMap.put("Specifications",etSpecifications.getText().toString());
                    postMap.put("Counter", countPosts);
                    postMap.put("IsAvailable","Yes");

                    instantRef.child(randomid).updateChildren(postMap).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (task.isSuccessful()){
                                Toast.makeText(AddItemFragment.this.getContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        saveCats(hasSubCat,seletedCatagory);
        itemsRef= FirebaseDatabase.getInstance().getReference().child(RELEASE_TYPE).child("Products");
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
        postMap.put("ProductCatagory", seletedCatagory);
        postMap.put("ProductSubCatagory", selectedSubCatagory);
        postMap.put("Price",etPrice.getText().toString());
        postMap.put("Percentage",etPercentage.getText().toString());
        postMap.put("ProductImage",downloadUrl);
        postMap.put("Specifications",etSpecifications.getText().toString());
        postMap.put("Counter", countPosts);
        postMap.put("IsInstant", isinstant);

        itemsRef.child(randomid).updateChildren(postMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()){
                    Toast.makeText(getContext(), "Uploaded", Toast.LENGTH_SHORT).show();
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