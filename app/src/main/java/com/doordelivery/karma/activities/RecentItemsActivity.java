package com.doordelivery.karma.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.doordelivery.karma.R;
import com.doordelivery.karma.adapters.RecentAdapters;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.app.PendingIntent.getActivity;

public class RecentItemsActivity extends AppCompatActivity {

        DatabaseReference cfPostRef;
        RecyclerView rvProducts;

        final int ITEM_LOAD_COUNT = 5;
        int tota_item = 0, last_visible_item;

        RecentAdapters productAdapter;
        boolean isLoading = false, isMaxData = false;
        String last_node = "", last_key = "";


        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_recent_items);

            Utils.setTopBar(getWindow(),getResources());
            rvProducts=findViewById(R.id.rv_list_recent);

            getLastItem();


            LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
            // Set the layout manager to your recyclerview
            mLayoutManager.setStackFromEnd(true);
            mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            rvProducts.setLayoutManager(mLayoutManager);

            DividerItemDecoration dividerItemDecoration=new DividerItemDecoration(rvProducts.getContext(),mLayoutManager.getOrientation());
            rvProducts.addItemDecoration(dividerItemDecoration);



            productAdapter = new RecentAdapters(this);
            rvProducts.setAdapter(productAdapter);

            showAllProducts();

            rvProducts.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    tota_item = mLayoutManager.getItemCount();
                    last_visible_item = mLayoutManager.findLastCompletelyVisibleItemPosition();

                    if (!isLoading && tota_item <= ((last_visible_item + ITEM_LOAD_COUNT))) {
                        showAllProducts();
                        isLoading = true;
                    }

                }
            });

            productAdapter.notifyDataSetChanged();

        }
        private void showAllProducts() {
            cfPostRef=FirebaseDatabase.getInstance().getReference().child("Products");
            if (!isMaxData) {
                Query query;
                if (TextUtils.isEmpty(last_node))

                    query = cfPostRef
                            .orderByKey()
                            .limitToFirst(ITEM_LOAD_COUNT);

                else

                    query = cfPostRef
                            .orderByKey()
                            .startAt(last_node)
                            .limitToFirst(ITEM_LOAD_COUNT);

                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChildren()) {
                            List<Products> newProducts = new ArrayList<>();
                            for (DataSnapshot productSnapShot : snapshot.getChildren()) {
                                newProducts.add(productSnapShot.getValue(Products.class));
                            }
                            last_node = newProducts.get(newProducts.size() - 1).getProductId();

                            if (!last_node.equals(last_key))
                                newProducts.remove(newProducts.size() - 1);
                            else
                                last_node = "end";

                            productAdapter.addAll(newProducts);
                            isLoading = false;

                        } else {
                            isLoading = false;
                            isMaxData = true;
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        isLoading = false;

                    }
                });
            }
        }

        private void getLastItem() {
            cfPostRef=FirebaseDatabase.getInstance().getReference().child("Products");
            Query getLastKey = cfPostRef
                    .orderByChild("Counter")
                    .limitToLast(1);
            getLastKey.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot lastKey : snapshot.getChildren())
                        last_key = lastKey.getKey();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                    Toast.makeText(RecentItemsActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();
                }
            });
        }

}
