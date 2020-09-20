package com.doordelivery.karma.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.doordelivery.karma.Products;
import com.doordelivery.karma.R;
import com.doordelivery.karma.ViewSingleProductActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    List<Products> mProductsList;
    Context context;

    public ProductAdapter(Context context) {
        this.mProductsList = new ArrayList<>();
        this.context = context;
    }

    public void addAll(List<Products> newProducts){

        int initSize=newProducts.size();
        mProductsList.addAll(newProducts);
        notifyItemRangeChanged(initSize,newProducts.size());

    }

    public String getLastItemId(){
        return mProductsList.get(mProductsList.size()-1).getProductId();
    }


    @NonNull
    @Override
    public ProductAdapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView= LayoutInflater.from(context).inflate(R.layout.item_grid_layout,parent,false);

        return new ProductAdapter.ProductViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ProductViewHolder holder, int position) {

        holder.getItemDetails(mProductsList.get(position).getProductId());
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewSingleProductActivity.class);
                intent.putExtra("REF_KEY", mProductsList.get(position).getProductId());
                intent.putExtra("isOffer", false);
                intent.putExtra("IsInstant", false);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mProductsList.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView tvName,tvPrice;
        ImageView tvImage,ivDownArrow;
        ConstraintLayout constraintLayout;
        DatabaseReference itemRef;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName=itemView.findViewById(R.id.tv_product_name);
            tvImage=itemView.findViewById(R.id.iv_product_image);
            tvPrice=itemView.findViewById(R.id.price);
            ivDownArrow=itemView.findViewById(R.id.iv_down_arrow);

            constraintLayout=itemView.findViewById(R.id.con_layout);

        }
        public void getItemDetails(String productId) {
            itemRef= FirebaseDatabase.getInstance().getReference().child("Products").child(productId);
            itemRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        String name=dataSnapshot.child("ProductName").getValue().toString();
                        tvName.setText(name);
                        String image=dataSnapshot.child("ProductImage").getValue().toString();
                        Picasso.get().load(image).into(tvImage);
                        String price=dataSnapshot.child("Price").getValue().toString();
                        tvPrice.setText(price+".00");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }
}
