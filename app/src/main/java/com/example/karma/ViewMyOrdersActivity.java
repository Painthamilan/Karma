package com.example.karma;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.karma.admin_fragments.OrdersFragment;
import com.example.karma.fragments.ShopFragment;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class ViewMyOrdersActivity extends AppCompatActivity {

    RecyclerView rvOrders;
    DatabaseReference orderRef;
    FirebaseAuth cfAuth;
    String curUserId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_orders);

        cfAuth=FirebaseAuth.getInstance();
        curUserId=cfAuth.getCurrentUser().getUid();
        rvOrders=findViewById(R.id.rv_list_orders);
        orderRef= FirebaseDatabase.getInstance().getReference().child("Orders");
        rvOrders.setHasFixedSize(true);
        // rvPosts.setLayoutManager(new LinearLayoutManager(getContext()));
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        // Set the layout manager to your recyclerview
        rvOrders.setLayoutManager(mLayoutManager);

        shoeAllOrders();

    }

    private void shoeAllOrders() {

        Query ordersQuery = orderRef
                .orderByChild("UserId")
                .equalTo(curUserId);
        FirebaseRecyclerAdapter<Orders, OrderViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Orders, OrderViewHolder>(
                        Orders.class,
                        R.layout.order_layout,
                        OrderViewHolder.class,
                        ordersQuery

                ) {
                    @Override
                    protected void populateViewHolder(OrderViewHolder postViewHolder, Orders model, int position) {
                        String postKey = getRef(position).getKey();
                        postViewHolder.setProductName(model.getProductName());
                        postViewHolder.setProductImage(model.getProductImage());
                        postViewHolder.setOrderStatus(model.getStatus());

                    }
                };

        rvOrders.setAdapter(firebaseRecyclerAdapter);
    }
    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        View cfView;

        TextView tvCatName,tvUpdateImage,tvStatus;
        ImageView ivCatImage;
        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            cfView = itemView;
            tvCatName=cfView.findViewById(R.id.tv_product_name);
            ivCatImage=cfView.findViewById(R.id.iv_product_image);
            tvStatus=cfView.findViewById(R.id.tv_status);

        }


        public void setProductName(String catagoryName) {
            tvCatName.setText(catagoryName);

        }

        public void setProductImage(String catagoryImage) {
            if (!TextUtils.isEmpty(catagoryImage)){
                Picasso.get().load(catagoryImage).into(ivCatImage);
            }
        }

        public void setOrderStatus(String status) {
            tvStatus.setText(status);
        }
    }
}
