package com.doordelivery.karma.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.doordelivery.karma.R;
import com.doordelivery.karma.adapters.ProductAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewItemsActivity extends AppCompatActivity {
    String catName, catImage, mainCatName, userId,defaultDistrict;
    DatabaseReference subCatRef, instantsRef;
    Query mainRef;
    TextView tvDefaultRegion,tvChangeRegion;
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

        tvChangeRegion=findViewById(R.id.tv_change_region);
        tvDefaultRegion=findViewById(R.id.tv_current_region);

        SharedPreferences preferences=getSharedPreferences("REGION_SELECTOR",MODE_PRIVATE);
        defaultDistrict=preferences.getString("REGION","Kandy");

        tvDefaultRegion.setText("Currently displaying items from "+defaultDistrict.toUpperCase());

        tvChangeRegion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPopupForChangeRegion();
            }
        });


        rvCats=findViewById(R.id.rv_list_items);

        catName=getIntent().getStringExtra("CAT_NAME");
        mainCatName=getIntent().getStringExtra("MAIN_CAT_NAME");
        hasSub=getIntent().getBooleanExtra("hasSub",false);
        isInstant=getIntent().getBooleanExtra("IsInstant",false);
        if(hasSub){
            subCatRef= FirebaseDatabase.getInstance().getReference().child("Regions").child(defaultDistrict).child("Catagories").child(mainCatName).child("SubCatagories").child(catName).child("Products");
        }else {
            subCatRef= FirebaseDatabase.getInstance().getReference().child("Regions").child(defaultDistrict).child("Catagories").child(mainCatName).child("Products");
        }
      instantsRef=FirebaseDatabase.getInstance().getReference().child("Instants");

        getLastItem();

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
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

    private void openPopupForChangeRegion() {
        AlertDialog.Builder dialogBuilder=new AlertDialog.Builder(ViewItemsActivity.this, R.style.AlertDialogTheme).setCancelable(false);
        View rowView= LayoutInflater.from(ViewItemsActivity.this).inflate(R.layout.region_popup,null);
        dialogBuilder.setView(rowView);
        AlertDialog dialog = dialogBuilder.create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }

        ListView rvRegion;
        ArrayAdapter aAdapter;

        rvRegion = rowView.findViewById(R.id.rv_regions);
        String[] regs = getResources().getStringArray(R.array.regions);

        aAdapter = new ArrayAdapter(ViewItemsActivity.this, R.layout.region_selector_layout, R.id.tv_region_name, regs);
        rvRegion.setAdapter(aAdapter);

        rvRegion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = view.findViewById(R.id.tv_region_name);
                String string = textView.getText().toString();
                Toast.makeText(ViewItemsActivity.this, "Selected "+string, Toast.LENGTH_SHORT).show();
                SharedPreferences.Editor editor=getSharedPreferences("REGION_SELECTOR",MODE_PRIVATE).edit();
                editor.putString("REGION",string);
                editor.apply();
                Intent intent=new Intent(ViewItemsActivity.this,AllCatActivity.class);
                intent.putExtra("CAT_NAME",catName);
                intent.putExtra("MAIN_CAT_NAME",mainCatName);
                intent.putExtra("hasSub",hasSub);
                intent.putExtra("IsInstant",isInstant);
                startActivity(intent);


                dialog.dismiss();

            }
        });

        dialog.show();
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
