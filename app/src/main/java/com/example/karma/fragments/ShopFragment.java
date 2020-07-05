package com.example.karma.fragments;

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

import com.example.karma.Cats;
import com.example.karma.CatsDetailActivity;
import com.example.karma.R;
import com.example.karma.ViewItemsActivity;
import com.example.karma.ViewSubCatsActivity;
import com.example.karma.admin_fragments.AddCatagoriesDetails;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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