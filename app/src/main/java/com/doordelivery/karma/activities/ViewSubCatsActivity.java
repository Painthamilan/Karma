package com.doordelivery.karma.activities;

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

import com.doordelivery.karma.R;
import com.doordelivery.karma.domains.SubCats;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import static com.doordelivery.karma.activities.Utils.RELEASE_TYPE;

public class ViewSubCatsActivity extends AppCompatActivity {
String catName,catImage,mainCatName;
DatabaseReference subCatRef;
RecyclerView rvCats;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_sub_cats);
        catName=getIntent().getStringExtra("CAT_NAME");
        subCatRef= FirebaseDatabase.getInstance().getReference().child(RELEASE_TYPE).child("Catagories").child(catName).child("SubCatagories");

        rvCats=findViewById(R.id.rv_list_sub_cats);
        rvCats.setHasFixedSize(true);
        // rvPosts.setLayoutManager(new LinearLayoutManager(getContext()));
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        // Set the layout manager to your recyclerview
        rvCats.setLayoutManager(mLayoutManager);

        shoeAllSubCats();
    }

    private void shoeAllSubCats() {
        FirebaseRecyclerAdapter<SubCats,CatsViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<SubCats, CatsViewHolder>(
                        SubCats.class,
                        R.layout.catagory_list_layout,
                        CatsViewHolder.class,
                        subCatRef

                ) {
                    @Override
                    protected void populateViewHolder(CatsViewHolder postViewHolder, SubCats model, int position) {
                        String postKey = getRef(position).getKey();
                        postViewHolder.setCatagoryName(model.getSubCatagoryName());
                        postViewHolder.setCatagoryImage(model.getSubCatagoryImage());

                        postViewHolder.cfView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent=new Intent(ViewSubCatsActivity.this, ViewItemsActivity.class);
                                intent.putExtra("REF_KEY",postKey);
                                intent.putExtra("CAT_NAME",model.getSubCatagoryName());
                                intent.putExtra("MAIN_CAT_NAME",catName);
                                intent.putExtra("hasSub",true);
                                startActivity(intent);
                            }
                        });
                    }
                };

        rvCats.setAdapter(firebaseRecyclerAdapter);
    }
    public static class CatsViewHolder extends RecyclerView.ViewHolder {
        View cfView;

        TextView tvCatName,tvUpdateImage;
        ImageView ivCatImage;
        public CatsViewHolder(@NonNull View itemView) {
            super(itemView);
            cfView = itemView;
            tvCatName=cfView.findViewById(R.id.tv_cat_name);
            ivCatImage=cfView.findViewById(R.id.iv_cat_label);

        }


        public void setCatagoryName(String catagoryName) {
            tvCatName.setText(catagoryName);

        }

        public void setCatagoryImage(String catagoryImage) {
            if (!TextUtils.isEmpty(catagoryImage)){
                Picasso.get().load(catagoryImage).into(ivCatImage);
            }
        }
    }

}
