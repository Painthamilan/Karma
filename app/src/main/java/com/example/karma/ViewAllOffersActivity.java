package com.example.karma;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.style.StrikethroughSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.karma.admin_fragments.AdminHomeFragment;
import com.example.karma.fragments.TopFragment;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class ViewAllOffersActivity extends AppCompatActivity {
    private FirebaseAuth cfAuth;
    private String curUserId;
    private RecyclerView rvProducts;
    private DatabaseReference cfPostRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_offers);
        rvProducts=findViewById(R.id.rv_list_offers);
        rvProducts.setHasFixedSize(true);
        // rvPosts.setLayoutManager(new LinearLayoutManager(getContext()));
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(false);
        mLayoutManager.setStackFromEnd(false);
        // Set the layout manager to your recyclerview
        rvProducts.setLayoutManager(mLayoutManager);
        showAllProducts();
    }

    private void showAllProducts() {
        cfPostRef = FirebaseDatabase.getInstance().getReference().child("Offers");
        Query searchPeopleAndFriendsQuery = cfPostRef.orderByChild("Counter");
        FirebaseRecyclerAdapter<Products, PostViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Products, PostViewHolder>(
                        Products.class,
                        R.layout.offers_layout,
                        PostViewHolder.class,
                        searchPeopleAndFriendsQuery

                ) {
                    @Override
                    protected void populateViewHolder(PostViewHolder postViewHolder, Products model, int position) {
                        String postKey = getRef(position).getKey();
                        postViewHolder.setNewPrice(model.getPercentage(),model.getPrice());
                        postViewHolder.setProductImage(model.getProductImage());
                        postViewHolder.setProductName(model.getProductName());
                      postViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent=new Intent(ViewAllOffersActivity.this, ViewProductActivity.class);
                                intent.putExtra("REF_KEY",postKey);
                                intent.putExtra("isOffer",true);
                                intent.putExtra("ActualPrice",postViewHolder.setNewPrice(model.getPercentage(),model.getPrice()));
                                startActivity(intent);
                            }
                        });
                    }
                };

        rvProducts.setAdapter(firebaseRecyclerAdapter);
    }
    public static class PostViewHolder extends RecyclerView.ViewHolder {
        View cfView;
        FirebaseAuth cfAuth=FirebaseAuth.getInstance();
        String userId;
        TextView tvProductName,tvPrice,tvNewPrice,tvPercentage;
        ImageView ivproductImage,ivDropArrow;
        DatabaseReference topRef;
        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            cfView = itemView;
            tvPrice=cfView.findViewById(R.id.price);
            tvProductName=cfView.findViewById(R.id.tv_product_name);
            ivproductImage=cfView.findViewById(R.id.iv_product_image);
            ivDropArrow=cfView.findViewById(R.id.iv_down_arrow);
            tvNewPrice=cfView.findViewById(R.id.tv_new_price);
            tvPercentage=cfView.findViewById(R.id.tv_percentage);
            userId=cfAuth.getCurrentUser().getUid();
            if (!userId.equals(Constants.ADMIN_ID)){
                ivDropArrow.setVisibility(View.INVISIBLE);
            }

        }


        public void setProductName(String productName) {
            tvProductName.setText(productName);
        }

        public void setProductImage(String productImage) {
            Picasso.get().load(productImage).into(ivproductImage);
        }

        public String setNewPrice(String percentage, String price) {
            tvPrice.setText(price+".00 ₹");
            tvPrice.setTextColor(Color.RED);
            tvPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);

            tvPercentage.setText(percentage +" %");

            int percent=Integer.parseInt(percentage);
            int pr=Integer.parseInt(price);
            int newPrice=pr-(pr*percent/100);
            String amt=String.valueOf(newPrice);
            tvNewPrice.setText(amt+".00 ₹");
            return amt;
        }

    }

}
