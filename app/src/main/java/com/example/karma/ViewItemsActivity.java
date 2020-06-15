package com.example.karma;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ViewItemsActivity extends AppCompatActivity {
    String catName,catImage,mainCatName;
    DatabaseReference subCatRef;
    RecyclerView rvCats;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_items);
        catName=getIntent().getStringExtra("CAT_NAME");
        mainCatName=getIntent().getStringExtra("MAIN_CAT_NAME");
        subCatRef= FirebaseDatabase.getInstance().getReference().child("Catagories").child(mainCatName).child("SubCatagories").child(catName).child("Products");

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
        FirebaseRecyclerAdapter<Products, CatsViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Products, CatsViewHolder>(
                        Products.class,
                        R.layout.item_grid_layout,
                        CatsViewHolder.class,
                        subCatRef

                ) {
                    @Override
                    protected void populateViewHolder(CatsViewHolder postViewHolder, Products model, int position) {
                        String postKey = getRef(position).getKey();
                        postViewHolder.getItemDetails(model.getProductId());

                        postViewHolder.cfView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent=new Intent(ViewItemsActivity.this, ViewProductActivity.class);
                                intent.putExtra("REF_KEY",postKey);
                                intent.putExtra("isOffer",false);
                                startActivity(intent);
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
        ImageView tvImage;
        public CatsViewHolder(@NonNull View itemView) {
            super(itemView);
            cfView = itemView;
            tvName=cfView.findViewById(R.id.tv_product_name);
            tvImage=cfView.findViewById(R.id.iv_product_image);
            tvPrice=cfView.findViewById(R.id.price);

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
    }
}
