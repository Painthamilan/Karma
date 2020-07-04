package com.example.karma.admin_fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.karma.AdminBottomBarActivity;
import com.example.karma.Constants;
import com.example.karma.Products;
import com.example.karma.R;
import com.example.karma.RoundedCorners;
import com.example.karma.ViewProductActivity;
import com.example.karma.fragments.TopFragment;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class AdminHomeFragment extends Fragment {
    private FirebaseAuth cfAuth;
    private String curUserId;
    private RecyclerView rvProducts;
    private DatabaseReference cfPostRef;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_admin_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);

        rvProducts=root.findViewById(R.id.rv_admin_home);
        rvProducts.setHasFixedSize(true);
        // rvPosts.setLayoutManager(new LinearLayoutManager(getContext()));
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        // Set the layout manager to your recyclerview
        rvProducts.setLayoutManager(mLayoutManager);
        showAllProducts();
        return root;
    }
    private void showAllProducts() {
        cfPostRef = FirebaseDatabase.getInstance().getReference().child("Products");
        Query searchPeopleAndFriendsQuery = cfPostRef.orderByChild("Counter");
        FirebaseRecyclerAdapter<Products, PostViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Products, PostViewHolder>(
                        Products.class,
                        R.layout.item_layout,
                        PostViewHolder.class,
                        searchPeopleAndFriendsQuery

                ) {
                    @Override
                    protected void populateViewHolder(PostViewHolder postViewHolder, Products model, int position) {
                        String postKey = getRef(position).getKey();
                        postViewHolder.setPrice(model.getPrice());
                        postViewHolder.setProductImage(model.getProductImage());
                        postViewHolder.setProductName(model.getProductName());
                        postViewHolder.dropDownClicker(model.getProductName(),model.getProductImage(),model.getPrice(),postKey,AdminHomeFragment.this.getContext());
                        postViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent=new Intent(getActivity(), AdminViewProductActivity.class);
                                intent.putExtra("REF_KEY",postKey);
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
        TextView tvProductName,tvPrice;
        ImageView ivproductImage,ivDropArrow;
        DatabaseReference topRef;
        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            cfView = itemView;
            tvPrice=cfView.findViewById(R.id.price);
            tvProductName=cfView.findViewById(R.id.tv_product_name);
            tvProductName.setSelected(true);
            ivproductImage=cfView.findViewById(R.id.iv_product_image);
            ivDropArrow=cfView.findViewById(R.id.iv_down_arrow);
            ivDropArrow.setVisibility(View.VISIBLE);

            topRef=FirebaseDatabase.getInstance().getReference().child("TopItems");

        }

        public void setPrice(String price) {
            tvPrice.setText(price+".00");

        }

        public void setProductName(String productName) {
            tvProductName.setText(productName);
        }

        public void setProductImage(String productImage) {
            Picasso.get().load(productImage).transform(new RoundedCorners(80, 0)).into(ivproductImage);
        }
        public void dropDownClicker(String name,String image,String price,String postId, Context context) {
            PopupMenu popup = new PopupMenu(context,ivDropArrow);
            popup.getMenuInflater().inflate(R.menu.top_selector, popup.getMenu());
            ivDropArrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    popup.setOnMenuItemClickListener(item->{
                        switch (item.getItemId()) {
                            case R.id.rank_first:
                                // Toast.makeText(context, ""+postId, Toast.LENGTH_SHORT).show();
                                updateRank("First",postId, name, image, price,"a");
                                break;
                            case R.id.rank_second:
                                // Toast.makeText(context, "second", Toast.LENGTH_SHORT).show();
                                updateRank("Second",postId, name, image, price, "b");
                                break;
                            case R.id.rank_thirt:
                                // Toast.makeText(context, "thirt", Toast.LENGTH_SHORT).show();
                                updateRank("Thirt",postId, name, image, price, "c");
                                break;
                            case R.id.rank_forth:
                                // Toast.makeText(context, "thirt", Toast.LENGTH_SHORT).show();
                                updateRank("Fourth",postId, name, image, price, "d");
                                break;
                        }
                        return true;
                    });
                    popup.show();
                }
            });
        }
        private void updateRank(String rank, String postId, String name, String image, String price, String i) {
            topRef.child(rank).child("ItemId").setValue(postId);
            topRef.child(rank).child("ItemName").setValue(name);
            topRef.child(rank).child("ItemImage").setValue(image);
            topRef.child(rank).child("ItemPrice").setValue(price);
            topRef.child(rank).child("Rank").setValue(i);
        }


    }
}