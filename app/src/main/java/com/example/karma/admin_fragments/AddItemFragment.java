package com.example.karma.admin_fragments;

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
    private TextView tvSelectcatagory,tvUpload,tvSelectImage,tvSelectSubCatagory;
    private String seletedCatagory="",curDate,curTime,randomid,downloadUrl,selectedSubCatagory;
    private StorageReference itemStorageRef;
    private DatabaseReference itemsRef,catRef;
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
         tvSelectSubCatagory=root.findViewById(R.id.tv_select_sub_catogary);

         itemStorageRef= FirebaseStorage.getInstance().getReference().child("ProductImages");
         itemsRef= FirebaseDatabase.getInstance().getReference().child("Products");
         catRef=FirebaseDatabase.getInstance().getReference().child("Catagories");
        PopupMenu popup = new PopupMenu(AddItemFragment.this.getContext(),tvSelectcatagory);

        popup.getMenuInflater().inflate(R.menu.main_catagory, popup.getMenu());
        tvSelectcatagory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                popup.setOnMenuItemClickListener(item->{
                    switch (item.getItemId()) {
                        case R.id.main_cat_clothing:
                            seletedCatagory="Clothing";
                            break;

                        case R.id.main_cat_electronics:
                            seletedCatagory="Electronics";

                            break;

                        case R.id.main_cat_sports:
                          seletedCatagory="Sports";

                            break;

                        case R.id.main_cat_education:
                            seletedCatagory="Education";
                            break;

                        case R.id.main_cat_instant:
                            seletedCatagory="Instant";
                            break;

                        case R.id.main_cat_cab:
                            seletedCatagory="Cab";
                            break;

                        case R.id.main_cat_food_delivery:
                            seletedCatagory="Food";
                            tvSelectSubCatagory.setVisibility(View.INVISIBLE);
                            selectedSubCatagory="Food";
                            break;
                        case R.id.main_cat_essential_goods:
                            seletedCatagory="Essential Goods";
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
          case "Clothing":
              PopupMenu subMenuClothing=new PopupMenu(AddItemFragment.this.getContext(),tvSelectcatagory);
              subMenuClothing.getMenuInflater().inflate(R.menu.sub_catagory_clothing, subMenuClothing.getMenu());
              subMenuClothing.getMenu().findItem(R.id.sub_cat_clothin_woman).setVisible(true);
              subMenuClothing.getMenu().findItem(R.id.sub_cat_clothing_men).setVisible(true);

              subMenuClothing.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                  @Override
                  public boolean onMenuItemClick(MenuItem item) {
                      switch (item.getItemId()) {
                          case R.id.sub_cat_clothing_men:
                              selectedSubCatagory="Men";
                              break;
                          case R.id.sub_cat_clothin_woman:
                              selectedSubCatagory="Female";
                              break;
                      }
                      tvSelectSubCatagory.setText(selectedSubCatagory);
                      return true;
                  }
              });
              subMenuClothing.show();
              break;

          case "Electronics":
              PopupMenu subMenuElectronics=new PopupMenu(AddItemFragment.this.getContext(),tvSelectcatagory);
              subMenuElectronics.getMenuInflater().inflate(R.menu.sub_catagory_electronics, subMenuElectronics.getMenu());

              subMenuElectronics.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                  @Override
                  public boolean onMenuItemClick(MenuItem item) {
                      switch (item.getItemId()) {
                          case R.id.sub_cat_elect_mobile_phones:
                              selectedSubCatagory="Mobile Phones";
                              break;
                          case R.id.sub_cat_elect_tv:
                              selectedSubCatagory="TV";
                              break;
                          case R.id.sub_cat_elect_mobile_accs:
                              selectedSubCatagory="Mobile Accessories";
                              break;
                          case R.id.sub_cat_elect_laptop:
                              selectedSubCatagory="Laptop";
                              break;
                      }
                      tvSelectSubCatagory.setText(selectedSubCatagory);
                      return true;
                  }
              });
              subMenuElectronics.show();
              break;
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
                          case R.id.sub_cat_sports_stumps:
                              selectedSubCatagory="Stumps";
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
                          selectedSubCatagory="O/L";
                          break;
                      case R.id.sub_cat_education_ol:
                          selectedSubCatagory="A/L";
                          break;
                      case R.id.sub_cat_education_other_stationary:
                          selectedSubCatagory="Other Stationary";
                          break;
                      case R.id.sub_cat_education_syllabus:
                          selectedSubCatagory="Syllabus";
                          break;
                  }
                  tvSelectSubCatagory.setText(selectedSubCatagory);
                  return true;
              }
          });
              subMenuEducation.show();
              break;

          case "Instant":
              PopupMenu subMenuInstant=new PopupMenu(AddItemFragment.this.getContext(),tvSelectcatagory);
              subMenuInstant.getMenuInflater().inflate(R.menu.sub_catagory_instants, subMenuInstant.getMenu());

              subMenuInstant.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                  @Override
                  public boolean onMenuItemClick(MenuItem item) {
                      switch (item.getItemId()) {
                          case R.id.sub_cat_instant_book:
                              selectedSubCatagory="Books";
                              break;
                          case R.id.sub_cat_instant_footwear:
                              selectedSubCatagory="Foodwear";
                              break;
                          case R.id.sub_cat_instant_stationaries:
                              selectedSubCatagory="Stationaries";
                              break;
                          case R.id.sub_cat_instant_tennis_ball:
                              selectedSubCatagory="Tennis Ball";
                              break;
                      }
                      tvSelectSubCatagory.setText(selectedSubCatagory);
                      return true;
                  }
              });
              subMenuInstant.show();
              break;

          case"Cab":
              PopupMenu subMenuCab=new PopupMenu(AddItemFragment.this.getContext(),tvSelectcatagory);
              subMenuCab.getMenuInflater().inflate(R.menu.sub_catagory_cab, subMenuCab.getMenu());


              subMenuCab.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                  @Override
                  public boolean onMenuItemClick(MenuItem item) {
                      switch (item.getItemId()) {
                          case R.id.sub_cat_cap_bike:
                              selectedSubCatagory="Bike";
                              break;
                          case R.id.sub_cat_cap_three_wheeler:
                              selectedSubCatagory="Three Wheeler";
                              break;
                          case R.id.sub_cat_cap_van:
                              selectedSubCatagory="Van";
                              break;
                      }
                      tvSelectSubCatagory.setText(selectedSubCatagory);
                      return true;
                  }
              });
              subMenuCab.show();
              break;

          case  "Essential Goods":
              PopupMenu subMenuEssential=new PopupMenu(AddItemFragment.this.getContext(),tvSelectcatagory);
              subMenuEssential.getMenuInflater().inflate(R.menu.sub_catagory_essential_goods, subMenuEssential.getMenu());

              subMenuEssential.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                  @Override
                  public boolean onMenuItemClick(MenuItem item) {
                      switch (item.getItemId()) {
                          case R.id.sub_cat_cap_bike:
                              selectedSubCatagory="Bike";
                              break;
                          case R.id.sub_cat_cap_three_wheeler:
                              selectedSubCatagory="Three Wheeler";
                              break;

                      }
                      tvSelectSubCatagory.setText(selectedSubCatagory);
                      return true;
                  }
              });
              subMenuEssential.show();
              break;
              default:
                  Toast.makeText(AddItemFragment.this.getContext(), "Please select Main catagory..", Toast.LENGTH_SHORT).show();
      }

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
        postMap.put("ProductSubCatagory", selectedSubCatagory);
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
        catRef.child(seletedCatagory).child(selectedSubCatagory).child("ProductId").setValue(randomid);

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