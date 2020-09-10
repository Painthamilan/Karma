package com.doordelivery.karma;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.doordelivery.karma.adapters.CatagoryAdapter;
import com.doordelivery.karma.adapters.ProductAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ViewItemsActivity extends AppCompatActivity {
    String catName, catImage, mainCatName, userId;
    DatabaseReference subCatRef, instantsRef;
    Query mainRef;
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
        setContentView(R.layout.activity_view_items);
        Utils.setTopBar(getWindow(),getResources());
        cfAuth=FirebaseAuth.getInstance();
        if (cfAuth.getCurrentUser() != null) {
            userId = cfAuth.getCurrentUser().getUid().toString();
        }else {
            userId="bkblkhlkhlhjg";
        }

        rvCats=findViewById(R.id.rv_list_items);

        catName=getIntent().getStringExtra("CAT_NAME");
        mainCatName=getIntent().getStringExtra("MAIN_CAT_NAME");
        hasSub=getIntent().getBooleanExtra("hasSub",false);
        isInstant=getIntent().getBooleanExtra("IsInstant",false);
        if(hasSub){
            subCatRef= FirebaseDatabase.getInstance().getReference().child("Catagories").child(mainCatName).child("SubCatagories").child(catName).child("Products");
        }else {
            subCatRef= FirebaseDatabase.getInstance().getReference().child("Catagories").child(mainCatName).child("Products");
        }
      instantsRef=FirebaseDatabase.getInstance().getReference().child("Instants");

        getLastItem();

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        // Set the layout manager to your recyclerview
        mLayoutManager.setStackFromEnd(true);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvCats.setLayoutManager(mLayoutManager);

        DividerItemDecoration dividerItemDecoration=new DividerItemDecoration(rvCats.getContext(),mLayoutManager.getOrientation());
        rvCats.addItemDecoration(dividerItemDecoration);



        productAdapter = new ProductAdapter(this);
        rvCats.setAdapter(productAdapter);

        showAllItems();

        rvCats.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                tota_item = mLayoutManager.getItemCount();
                last_visible_item = mLayoutManager.findLastCompletelyVisibleItemPosition();

                if (!isLoading && tota_item <= ((last_visible_item + ITEM_LOAD_COUNT))) {
                    showAllItems();
                    isLoading = true;
                }

            }
        });

        productAdapter.notifyDataSetChanged();

    }

    private void showAllItems() {

        if (!isMaxData) {
            Query query;
            if (TextUtils.isEmpty(last_node))

                query = subCatRef
                        .orderByKey()
                        .limitToFirst(ITEM_LOAD_COUNT);

            else

                query = subCatRef
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

        Query getLastKey = subCatRef
                .orderByKey()
                .limitToLast(1);
        getLastKey.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot lastKey : snapshot.getChildren())
                    last_key = lastKey.getKey();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(ViewItemsActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
