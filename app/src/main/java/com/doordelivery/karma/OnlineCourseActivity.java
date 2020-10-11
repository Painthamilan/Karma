package com.doordelivery.karma;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.doordelivery.karma.adapters.CourseAdapter;
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

public class OnlineCourseActivity extends AppCompatActivity {
    String catName, catImage, mainCatName, userId,defaultDistrict;
    DatabaseReference subCatRef, instantsRef;
    Query mainRef;
    boolean hasSub;
    RecyclerView rvCourses;
    FirebaseAuth cfAuth;
    boolean isInstant, isAdmin;

    final int ITEM_LOAD_COUNT = 5;
    int tota_item = 0, last_visible_item;

    CourseAdapter courseAdapter;
    boolean isLoading = false, isMaxData = false;
    String last_node = "", last_key = "";
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_course);

        Utils.setTopBar(getWindow(),getResources());


        SharedPreferences preferences=getSharedPreferences("REGION_SELECTOR",MODE_PRIVATE);
        defaultDistrict=preferences.getString("REGION","Kandy");



        rvCourses=findViewById(R.id.rv_list_items);
        subCatRef= FirebaseDatabase.getInstance().getReference().child("Test").child("Online Courses");


        getLastItem();

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvCourses.setLayoutManager(mLayoutManager);

        DividerItemDecoration dividerItemDecoration=new DividerItemDecoration(rvCourses.getContext(),mLayoutManager.getOrientation());
        rvCourses.addItemDecoration(dividerItemDecoration);



        courseAdapter = new CourseAdapter(this);
        rvCourses.setAdapter(courseAdapter);

        showAllItems();

        rvCourses.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

        courseAdapter.notifyDataSetChanged();
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
                        List<Courses> newProducts = new ArrayList<>();
                        for (DataSnapshot productSnapShot : snapshot.getChildren()) {
                            newProducts.add(productSnapShot.getValue(Courses.class));
                        }
                        last_node = newProducts.get(newProducts.size() - 1).getProductId();

                        if (!last_node.equals(last_key))
                            newProducts.remove(newProducts.size() - 1);
                        else
                            last_node = "end";

                        courseAdapter.addAll(newProducts);
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

                Toast.makeText(OnlineCourseActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
