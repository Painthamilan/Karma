package com.doordelivery.karma.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.doordelivery.karma.activities.Catagories;
import com.doordelivery.karma.R;
import com.doordelivery.karma.adapters.CatagoryAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ShopFragment extends Fragment {
RecyclerView rvCats;
DatabaseReference listCatsRef;

    final int ITEM_LOAD_COUNT = 5;
    int tota_item = 0, last_visible_item;

    CatagoryAdapter catagoryAdapter;
    boolean isLoading = false, isMaxData = false;
    String last_node = "", last_key = "";


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_shop, container, false);
        final TextView textView = root.findViewById(R.id.text_dashboard);
        rvCats=root.findViewById(R.id.rv_list_cats);
        getLastItem();

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        // Set the layout manager to your recyclerview
        mLayoutManager.setStackFromEnd(true);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvCats.setLayoutManager(mLayoutManager);

        DividerItemDecoration dividerItemDecoration=new DividerItemDecoration(rvCats.getContext(),mLayoutManager.getOrientation());
        rvCats.addItemDecoration(dividerItemDecoration);



        catagoryAdapter = new CatagoryAdapter(getContext());
        rvCats.setAdapter(catagoryAdapter);
        getCats();

        rvCats.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                tota_item = mLayoutManager.getItemCount();
                last_visible_item = mLayoutManager.findLastCompletelyVisibleItemPosition();

                if (!isLoading && tota_item <= ((last_visible_item + ITEM_LOAD_COUNT))) {
                    getCats();
                    isLoading = true;
                }

            }
        });

        catagoryAdapter.notifyDataSetChanged();

        return root;
    }

    private void getCats() {

        if (!isMaxData) {
            Query query;
            if (TextUtils.isEmpty(last_node))

                query = FirebaseDatabase.getInstance().getReference().child("Catagories")
                        .orderByKey()
                        .limitToFirst(ITEM_LOAD_COUNT);

            else

                query = FirebaseDatabase.getInstance().getReference().child("Catagories")
                        .orderByKey()
                        .startAt(last_node)
                        .limitToFirst(ITEM_LOAD_COUNT);

            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.hasChildren()) {
                        List<Catagories> newCats = new ArrayList<>();
                        for (DataSnapshot catSnapShot : snapshot.getChildren()) {
                            newCats.add(catSnapShot.getValue(Catagories.class));
                        }
                        last_node = newCats.get(newCats.size() - 1).getCatagoryName();

                        if (!last_node.equals(last_key))
                            newCats.remove(newCats.size() - 1);
                        else
                            last_node = "end";

                        catagoryAdapter.addAll(newCats);
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

        Query getLastKey = FirebaseDatabase.getInstance().getReference()
                .child("Catagories")
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

                Toast.makeText(ShopFragment.this.getContext(), "something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

}