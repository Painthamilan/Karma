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

import com.doordelivery.karma.CourseDetailsActivity;
import com.doordelivery.karma.Courses;
import com.doordelivery.karma.Products;
import com.doordelivery.karma.R;
import com.doordelivery.karma.Utils;
import com.doordelivery.karma.ViewSingleProductActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {
    List<Courses> mCourseList;
    Context context;

    public CourseAdapter(Context context) {
        this.mCourseList = new ArrayList<>();
        this.context = context;
    }

    public void addAll(List<Courses> newProducts) {

        int initSize = newProducts.size();
        mCourseList.addAll(newProducts);
        notifyItemRangeChanged(initSize, newProducts.size());

    }

    public String getLastItemId() {
        return mCourseList.get(mCourseList.size() - 1).getProductId();
    }


    @NonNull
    @Override
    public CourseAdapter.CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(context).inflate(R.layout.layout_courses, parent, false);

        return new CourseAdapter.CourseViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull CourseAdapter.CourseViewHolder holder, int position) {

       // holder.getItemDetails(mCourseList.get(position).getProductId());
        holder.tvName.setText(mCourseList.get(position).getProductName());
        Picasso.get().load(mCourseList.get(position).getProductImage()).into(holder.ivImage);
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CourseDetailsActivity.class);
                intent.putExtra("REF_KEY", mCourseList.get(position).getCourseId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCourseList.size();
    }

    public class CourseViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;
        ImageView ivImage;
        ConstraintLayout constraintLayout;
        DatabaseReference itemRef;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName=itemView.findViewById(R.id.tv_course_name);
            ivImage=itemView.findViewById(R.id.iv_course_image);

            constraintLayout=itemView.findViewById(R.id.con_layout);

        }
        public void getItemDetails(String productId) {


           /* itemRef= FirebaseDatabase.getInstance().getReference().child("Products").child(productId);
            itemRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        String name=dataSnapshot.child("ProductName").getValue().toString();
                        tvName.setText(name);
                        String image=dataSnapshot.child("ProductImage").getValue().toString();
                        Picasso.get().load(image).into(tvImage);

                        String price=dataSnapshot.child("Price").getValue().toString();
                        String percentage=dataSnapshot.child("Percentage").getValue().toString();
                        tvPrice.setText(Utils.getActualPrice(price,percentage) +".00");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            */
        }

    }
}
