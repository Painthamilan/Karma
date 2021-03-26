package com.doordelivery.karma.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.doordelivery.karma.R;
import com.doordelivery.karma.adapters.ProductAdapter;
import com.doordelivery.karma.domains.Products;
import com.doordelivery.karma.fragments.TopFragment;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import static com.doordelivery.karma.activities.Utils.RELEASE_TYPE;

public class ViewAllItemsActivity extends AppCompatActivity {
    String catName, catImage, mainCatName, userId, defaultDistrict;
    DatabaseReference subCatRef, instantsRef;
    Query mainRef;
    TextView tvDefaultRegion, tvChangeRegion;
    boolean hasSub;
    RecyclerView rvCats;
    FirebaseAuth cfAuth;
    boolean isInstant, isAdmin;

    final int ITEM_LOAD_COUNT = 5;
    int tota_item = 0, last_visible_item;

    ProductAdapter productAdapter;
    boolean isLoading = false, isMaxData = false;
    String last_node = "", last_key = "";

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_items);
        Utils.setTopBar(getWindow(), getResources());
        cfAuth = FirebaseAuth.getInstance();
        if (cfAuth.getCurrentUser() != null) {
            userId = cfAuth.getCurrentUser().getUid().toString();
        } else {
            userId = "bkblkhlkhlhjg";
        }



        rvCats = findViewById(R.id.rv_list_items);





        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mLayoutManager=new GridLayoutManager(this,3);
        rvCats.setLayoutManager(mLayoutManager);



        productAdapter = new ProductAdapter(this);
        rvCats.setAdapter(productAdapter);

        showAllItems();


    }



    private void showAllItems() {
        subCatRef = FirebaseDatabase.getInstance().getReference().child(RELEASE_TYPE).child("Products");
        Query searchPeopleAndFriendsQuery = subCatRef.orderByKey().limitToLast(50);
        FirebaseRecyclerAdapter<Products, TopFragment.TopViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Products, TopFragment.TopViewHolder>(
                        Products.class,
                        R.layout.item_layout,
                        TopFragment.TopViewHolder.class,
                        searchPeopleAndFriendsQuery

                ) {
                    @Override
                    protected void populateViewHolder(TopFragment.TopViewHolder postViewHolder, Products model, int position) {
                        String postKey = getRef(position).getKey();
                        postViewHolder.setPrice(model.getPrice(), model.getPercentage());
                        postViewHolder.setProductImage(model.getProductImage());
                        postViewHolder.setProductName(model.getProductName());
                        postViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(ViewAllItemsActivity.this, ViewSingleProductActivity.class);
                                intent.putExtra("REF_KEY", model.getProductId());
                                intent.putExtra("isOffer", false);
                                startActivity(intent);
                            }
                        });
                    }
                };
        rvCats.setAdapter(firebaseRecyclerAdapter);


    }


    public static class TopViewHolder extends RecyclerView.ViewHolder {
        View cfView;
        FirebaseAuth cfAuth = FirebaseAuth.getInstance();
        String userId;
        TextView tvProductName, tvPrice;
        ImageView ivproductImage, ivDropArrow;
        DatabaseReference topRef;

        public TopViewHolder(@NonNull View itemView) {
            super(itemView);
            cfView = itemView;
            tvPrice = cfView.findViewById(R.id.price);
            tvProductName = cfView.findViewById(R.id.tv_product_name);
            tvProductName.setSelected(true);
            ivproductImage = cfView.findViewById(R.id.iv_product_image);
            if (cfAuth.getCurrentUser() != null) {
                userId = cfAuth.getCurrentUser().getUid();
            }


            topRef = FirebaseDatabase.getInstance().getReference().child(RELEASE_TYPE).child("TopItems");


        }

        public void setPrice(String price, String percentage) {
            tvPrice.setText(Utils.getActualPrice(price, percentage) + ".00");

        }

        public void setProductName(String productName) {
            tvProductName.setText(productName);
        }

        public void setProductImage(String productImage) {
            Picasso.get().load(productImage).transform(new RoundedCorners(10,1)).resize(100,100).into(ivproductImage);
        }


    }
}