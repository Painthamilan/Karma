package com.doordelivery.karma;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import static android.app.PendingIntent.getActivity;

public class RecentItemsActivity extends AppCompatActivity {

    DatabaseReference cfPostRef;
    RecyclerView rvProducts;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_items);

        Utils.setTopBar(getWindow(),getResources());
        rvProducts=findViewById(R.id.rv_list_recent);

        rvProducts.setHasFixedSize(true);
        // rvPosts.setLayoutManager(new LinearLayoutManager(getContext()));
        // LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        LinearLayoutManager mLayoutManager =new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        mLayoutManager=new GridLayoutManager(this,4);;
        // Set the layout manager to your recyclerview
        rvProducts.setLayoutManager(mLayoutManager);
        showAllProducts();
    }
    private void showAllProducts() {
        cfPostRef = FirebaseDatabase.getInstance().getReference().child("Products");
        Query searchPeopleAndFriendsQuery = cfPostRef.orderByChild("Counter");
        FirebaseRecyclerAdapter<Products, PostViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Products, PostViewHolder>(
                        Products.class,
                        R.layout.recent_layout,
                        PostViewHolder.class,
                        searchPeopleAndFriendsQuery

                ) {
                    @Override
                    protected void populateViewHolder(PostViewHolder postViewHolder, Products model, int position) {
                        String postKey = getRef(position).getKey();
                        postViewHolder.setPrice(model.getPrice());
                        postViewHolder.setProductImage(model.getProductImage());
                        postViewHolder.setProductName(model.getProductName());
                        postViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent=new Intent(RecentItemsActivity.this, ViewProductActivity.class);
                                intent.putExtra("REF_KEY",postKey);
                                intent.putExtra("isOffer",false);
                                startActivity(intent);
                            }
                        });
                    }
                };

        rvProducts.setAdapter(firebaseRecyclerAdapter);
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        View cfView;
        FirebaseAuth cfAuth= FirebaseAuth.getInstance();
        String userId;
        TextView tvProductName,tvPrice;
        ImageView ivproductImage,ivDropArrow;
        DatabaseReference topRef;
        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            cfView = itemView;
            tvPrice=cfView.findViewById(R.id.price);
            tvProductName=cfView.findViewById(R.id.tv_product_name);
            ivproductImage=cfView.findViewById(R.id.iv_product_image);
            ivDropArrow=cfView.findViewById(R.id.iv_down_arrow);
            topRef= FirebaseDatabase.getInstance().getReference().child("TopItems");

        }

        public void setPrice(String price) {
            tvPrice.setText(price+".00 ₹");

        }

        public void setProductName(String productName) {
            tvProductName.setText(productName);
        }

        public void setProductImage(String productImage) {
            Picasso.get().load(productImage).into(ivproductImage);
        }


    }
}
