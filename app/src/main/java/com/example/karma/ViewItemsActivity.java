package com.example.karma;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ViewItemsActivity extends AppCompatActivity {
    String catName,catImage,mainCatName,userId;
    DatabaseReference subCatRef,instantsRef;
    Query mainRef;
    RecyclerView rvCats;
    FirebaseAuth cfAuth;
    boolean isInstant,isAdmin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_items);
        cfAuth=FirebaseAuth.getInstance();
        userId=cfAuth.getCurrentUser().getUid();
        catName=getIntent().getStringExtra("CAT_NAME");
        mainCatName=getIntent().getStringExtra("MAIN_CAT_NAME");
        isInstant=getIntent().getBooleanExtra("IsInstant",false);
        subCatRef= FirebaseDatabase.getInstance().getReference().child("Catagories").child(mainCatName).child("SubCatagories").child(catName).child("Products");
        instantsRef=FirebaseDatabase.getInstance().getReference().child("Instants");
        rvCats=findViewById(R.id.rv_list_items);
        rvCats.setHasFixedSize(true);
        // rvPosts.setLayoutManager(new LinearLayoutManager(getContext()));
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        mLayoutManager=new GridLayoutManager(ViewItemsActivity.this,3);
        // Set the layout manager to your recyclerview
        rvCats.setLayoutManager(mLayoutManager);

        shoeAllItems();
    }

    private void shoeAllItems() {
        if (Utils.isAdmin(userId)) {
            mainRef=instantsRef;
        }else{
            if (isInstant){
                mainRef=instantsRef.orderByChild("IsAvailable").equalTo("Yes");
            }
            else {
                mainRef=subCatRef;
            }
        }

        FirebaseRecyclerAdapter<Products, CatsViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Products, CatsViewHolder>(
                        Products.class,
                        R.layout.item_grid_layout,
                        CatsViewHolder.class,
                        mainRef

                ) {
                    @Override
                    protected void populateViewHolder(CatsViewHolder postViewHolder, Products model, int position) {
                        String postKey = getRef(position).getKey();
                        postViewHolder.getItemDetails(model.getProductId());



                            postViewHolder.cfView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (Utils.isAdmin(postViewHolder.userId)) {
                                        postViewHolder.manageDropDown(postViewHolder.cfView, ViewItemsActivity.this, postKey);
                                    }
                                    else {
                                        Intent intent = new Intent(ViewItemsActivity.this, ViewProductActivity.class);
                                        intent.putExtra("REF_KEY", postKey);
                                        intent.putExtra("isOffer", false);
                                        intent.putExtra("IsInstant", isInstant);
                                        startActivity(intent);
                                    }
                                }
                            });


                    }
                };

        rvCats.setAdapter(firebaseRecyclerAdapter);
    }
    public static class CatsViewHolder extends RecyclerView.ViewHolder {
        View cfView;

        DatabaseReference itemRef;
        TextView tvName,tvPrice;
        ImageView tvImage,ivDownArrow;
        FirebaseAuth cfAuth;
        String userId;
        public CatsViewHolder(@NonNull View itemView) {
            super(itemView);
            cfView = itemView;
            tvName=cfView.findViewById(R.id.tv_product_name);
            tvImage=cfView.findViewById(R.id.iv_product_image);
            tvPrice=cfView.findViewById(R.id.price);
            ivDownArrow=cfView.findViewById(R.id.iv_down_arrow);
            cfAuth=FirebaseAuth.getInstance();
            userId=cfAuth.getCurrentUser().getUid();

        }

        public void getItemDetails(String productId) {
            itemRef=FirebaseDatabase.getInstance().getReference().child("Products").child(productId);
            itemRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 if (dataSnapshot.exists()){
                     String name=dataSnapshot.child("ProductName").getValue().toString();
                     tvName.setText(name);
                     String image=dataSnapshot.child("ProductImage").getValue().toString();
                     Picasso.get().load(image).into(tvImage);
                     String price=dataSnapshot.child("Price").getValue().toString();
                     tvPrice.setText(price);
                 }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        public void manageDropDown(View cfView, Context context, String postKey) {
            PopupMenu popup = new PopupMenu(context,cfView);
            popup.getMenuInflater().inflate(R.menu.instant_available_menu, popup.getMenu());
            cfView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    popup.setOnMenuItemClickListener(item->{
                        switch (item.getItemId()) {
                            case R.id.availability_yes:
                                // Toast.makeText(context, ""+postId, Toast.LENGTH_SHORT).show();
                                updateAvailability("Yes",postKey);
                                break;
                            case R.id.availability_no:
                                // Toast.makeText(context, "second", Toast.LENGTH_SHORT).show();
                                updateAvailability("No",postKey);
                                break;

                        }
                        return true;
                    });
                    popup.show();
                }
            });
        }

        private void updateAvailability(String available,String key) {
            DatabaseReference instantRef=FirebaseDatabase.getInstance().getReference().child("Instants").child(key);
            instantRef.child("IsAvailable").setValue(available);
        }
    }
}
