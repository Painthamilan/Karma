package com.doordelivery.karma;

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

import com.doordelivery.karma.fragments.ShopFragment;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class AllCatActivity extends AppCompatActivity {
    RecyclerView rvCats;
    DatabaseReference listCatsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_cat);

        final TextView textView = findViewById(R.id.text_dashboard);
        rvCats=findViewById(R.id.rv_list_cats);
        listCatsRef= FirebaseDatabase.getInstance().getReference().child("Catagories");
        rvCats.setHasFixedSize(true);
        // rvPosts.setLayoutManager(new LinearLayoutManager(getContext()));
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        mLayoutManager=new GridLayoutManager(this,3);
        // Set the layout manager to your recyclerview
        rvCats.setLayoutManager(mLayoutManager);

        shoeAllCats();
    }

    private void shoeAllCats() {
        FirebaseRecyclerAdapter<Cats, CatsViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Cats, CatsViewHolder>(
                        Cats.class,
                        R.layout.catagory_list_layout,
                        CatsViewHolder.class,
                        listCatsRef

                ) {
                    @Override
                    protected void populateViewHolder(CatsViewHolder postViewHolder, Cats model, int position) {
                        String postKey = getRef(position).getKey();
                        postViewHolder.setCatagoryName(model.getCatagoryName());
                        postViewHolder.setCatagoryImage(model.getCatagoryImage());

                        postViewHolder.cfView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(model.isHasSub()) {
                                    Intent intent = new Intent(AllCatActivity.this, ViewSubCatsActivity.class);
                                    intent.putExtra("REF_KEY", postKey);
                                    intent.putExtra("CAT_NAME", model.getCatagoryName());
                                    intent.putExtra("hasSub", false);

                                    startActivity(intent);
                                }else {
                                    Intent intent = new Intent(AllCatActivity.this, ViewItemsActivity.class);
                                    intent.putExtra("REF_KEY", postKey);
                                    intent.putExtra("CAT_NAME", model.getCatagoryName());
                                    intent.putExtra("MAIN_CAT_NAME", model.getCatagoryName());
                                    intent.putExtra("hasSub", false);

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
