package com.doordelivery.karma.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.doordelivery.karma.Catagories;
import com.doordelivery.karma.R;
import com.doordelivery.karma.ViewItemsActivity;
import com.doordelivery.karma.ViewSubCatsActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class ShopFragment extends Fragment {
RecyclerView rvCats;
DatabaseReference listCatsRef;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_shop, container, false);
        final TextView textView = root.findViewById(R.id.text_dashboard);
        rvCats=root.findViewById(R.id.rv_list_cats);
        listCatsRef= FirebaseDatabase.getInstance().getReference().child("Catagories");
        rvCats.setHasFixedSize(true);
        // rvPosts.setLayoutManager(new LinearLayoutManager(getContext()));
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        mLayoutManager=new GridLayoutManager(getContext(),3);
        // Set the layout manager to your recyclerview
        rvCats.setLayoutManager(mLayoutManager);

        shoeAllCats();

        return root;
    }

    private void shoeAllCats() {
        FirebaseRecyclerAdapter<Catagories, CatsViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Catagories, CatsViewHolder>(
                        Catagories.class,
                        R.layout.catagory_list_layout,
                        CatsViewHolder.class,
                        listCatsRef

                ) {
                    @Override
                    protected void populateViewHolder(CatsViewHolder postViewHolder, Catagories model, int position) {
                        String postKey = getRef(position).getKey();
                        postViewHolder.setCatagoryName(model.getCatagoryName());
                        postViewHolder.setCatagoryImage(model.getCatagoryImage());

                        postViewHolder.cfView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(model.isHasSub()) {
                                    Intent intent = new Intent(getActivity(), ViewSubCatsActivity.class);
                                    intent.putExtra("REF_KEY", postKey);
                                    intent.putExtra("CAT_NAME", model.getCatagoryName());
                                    intent.putExtra("hasSub", false);

                                    startActivity(intent);
                                }else {
                                    Intent intent = new Intent(getActivity(), ViewItemsActivity.class);
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