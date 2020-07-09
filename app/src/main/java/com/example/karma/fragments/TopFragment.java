package com.example.karma.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.karma.Products;
import com.example.karma.R;
import com.example.karma.RoundedCorners;
import com.example.karma.SearchActivity;
import com.example.karma.Top;
import com.example.karma.Utils;
import com.example.karma.ViewAllOffersActivity;
import com.example.karma.ViewProductActivity;
import com.example.karma.ViewSubCatsActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class TopFragment extends Fragment {
    private FirebaseAuth cfAuth;
    private String curUserId;
    TextView etSearch;
    private RecyclerView rvProducts,rvTopFragments,rvInstants;
    private DatabaseReference cfPostRef,topRef,instantsRef;
    private TextView ivInstant,tvMobile;
    ImageView ivOffers;

    AlertDialog.Builder dialogBuilder;
    AlertDialog dialog;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_top_items, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        rvProducts=root.findViewById(R.id.rv_top);
        rvTopFragments=root.findViewById(R.id.rv_topItems);
        ivOffers=root.findViewById(R.id.iv_offer_of_day);
        rvInstants=root.findViewById(R.id.rv_instants);
        tvMobile=root.findViewById(R.id.tv_mobile);
        tvMobile.setSelected(true);
        etSearch=root.findViewById(R.id.et_search_bar);
        etSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
            }
        });

        ivOffers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), ViewAllOffersActivity.class);
                startActivity(intent);
            }
        });

        rvProducts.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        mLayoutManager.setStackFromEnd(true);
        rvProducts.setLayoutManager(mLayoutManager);


        rvInstants.setHasFixedSize(true);
        LinearLayoutManager instantsLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        instantsLayoutManager.setStackFromEnd(true);
        rvInstants.setLayoutManager(instantsLayoutManager);



        rvTopFragments.setHasFixedSize(true);
        LinearLayoutManager horizontalYalayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        horizontalYalayoutManager.setStackFromEnd(true);
        rvTopFragments.setLayoutManager(horizontalYalayoutManager);
        ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("loading");
        pd.show();
// Hide after some seconds
        final Handler handler  = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (pd.isShowing()) {
                    pd.dismiss();
                }
            }
        };

        pd.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                handler.removeCallbacks(runnable);
            }
        });

        handler.postDelayed(runnable, 5000);
        showAllProducts();
        showHomeInstants();
        showTop();
        return root;
    }




    private void showInstants() {
        Intent intent=new Intent(getActivity(), ViewSubCatsActivity.class);
        intent.putExtra("CAT_NAME","Instant");
        intent.putExtra("IsInstant",true);

        startActivity(intent);
    }

    private void showTop() {
        topRef = FirebaseDatabase.getInstance().getReference().child("TopItems");
        Query searchPeopleAndFriendsQuery = topRef.orderByChild("Rank");
        FirebaseRecyclerAdapter<Top, TopViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Top, TopViewHolder>(
                        Top.class,
                        R.layout.item_layout,
                        TopViewHolder.class,
                        searchPeopleAndFriendsQuery

                ) {
                    @Override
                    protected void populateViewHolder(TopViewHolder postViewHolder, Top model, int position) {
                        String postKey = getRef(position).getKey();
                        postViewHolder.setPrice(model.getItemPrice(),model.getPercentage());
                        postViewHolder.setProductImage(model.getItemImage());
                        postViewHolder.setProductName(model.getItemName());
                        postViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent=new Intent(getActivity(), ViewProductActivity.class);
                                intent.putExtra("REF_KEY",model.getItemId());
                                intent.putExtra("isOffer",false);
                                startActivity(intent);
                            }
                        });
                    }
                };
        rvTopFragments.setAdapter(firebaseRecyclerAdapter);


    }

    private void showHomeInstants() {
        instantsRef = FirebaseDatabase.getInstance().getReference().child("Instants");
        Query searchPeopleAndFriendsQuery = instantsRef.orderByChild("Counter");
        FirebaseRecyclerAdapter<Products, TopViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Products, TopViewHolder>(
                        Products.class,
                        R.layout.item_layout,
                        TopViewHolder.class,
                        searchPeopleAndFriendsQuery

                ) {
                    @Override
                    protected void populateViewHolder(TopViewHolder postViewHolder, Products model, int position) {
                        String postKey = getRef(position).getKey();
                        postViewHolder.setPrice(model.getPrice(), model.getPercentage());
                        postViewHolder.setProductImage(model.getProductImage());
                        postViewHolder.setProductName(model.getProductName());
                        postViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent=new Intent(getActivity(), ViewProductActivity.class);
                                intent.putExtra("REF_KEY",model.getProductId());
                                intent.putExtra("isOffer",false);
                                intent.putExtra("isInstant",true);
                                startActivity(intent);
                            }
                        });
                    }
                };
        rvInstants.setAdapter(firebaseRecyclerAdapter);

    }

    private void showAllProducts() {
        cfPostRef = FirebaseDatabase.getInstance().getReference().child("Products");
        Query searchPeopleAndFriendsQuery = cfPostRef.orderByChild("Counter").limitToLast(6);
        FirebaseRecyclerAdapter<Products, PostViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Products, PostViewHolder>(
                        Products.class,
                        R.layout.item_layout,
                        PostViewHolder.class,
                        searchPeopleAndFriendsQuery

                ) {
                    @Override
                    protected void populateViewHolder(PostViewHolder postViewHolder, Products model, int position) {
                        String postKey = getRef(position).getKey();
                        postViewHolder.setPrice(model.getPrice(),model.getPercentage());
                        postViewHolder.setProductImage(model.getProductImage());
                        postViewHolder.setProductName(model.getProductName());
                        postViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent=new Intent(getActivity(), ViewProductActivity.class);
                                intent.putExtra("REF_KEY",postKey);
                                intent.putExtra("isOffer",false);
                                startActivity(intent);
                            }
                        });








                    }
                };

        rvProducts.setAdapter(firebaseRecyclerAdapter);
    }
    public static class TopViewHolder extends RecyclerView.ViewHolder {
        View cfView;
        FirebaseAuth cfAuth=FirebaseAuth.getInstance();
        String userId;
        TextView tvProductName,tvPrice;
        ImageView ivproductImage,ivDropArrow;
        DatabaseReference topRef;
        public TopViewHolder(@NonNull View itemView) {
            super(itemView);
            cfView = itemView;
            tvPrice=cfView.findViewById(R.id.price);
            tvProductName=cfView.findViewById(R.id.tv_product_name);
            tvProductName.setSelected(true);
            ivproductImage=cfView.findViewById(R.id.iv_product_image);
            if (cfAuth.getCurrentUser() != null) {
                userId=cfAuth.getCurrentUser().getUid();
            }


            topRef=FirebaseDatabase.getInstance().getReference().child("TopItems");



        }

        public void setPrice(String price, String percentage) {
            tvPrice.setText(Utils.getActualPrice(price,percentage) +".00");

        }

        public void setProductName(String productName) {
            tvProductName.setText(productName);
        }

        public void setProductImage(String productImage) {
            Picasso.get().load(productImage).transform(new RoundedCorners(80, 0)).into(ivproductImage);
        }

    }
    public static class PostViewHolder extends RecyclerView.ViewHolder {
        View cfView;
        FirebaseAuth cfAuth=FirebaseAuth.getInstance();
        String userId;
        TextView tvProductName,tvPrice;
        ImageView ivproductImage,ivDropArrow;
        DatabaseReference topRef;
        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            cfView = itemView;
            tvPrice=cfView.findViewById(R.id.price);
            tvProductName=cfView.findViewById(R.id.tv_product_name);
            tvProductName.setSelected(true);
            ivproductImage=cfView.findViewById(R.id.iv_product_image);
                    ivDropArrow=cfView.findViewById(R.id.iv_down_arrow);

            topRef=FirebaseDatabase.getInstance().getReference().child("TopItems");

        }

        public void setPrice(String price, String percentage) {
            tvPrice.setText(Utils.getActualPrice(price,percentage)+".00");

        }

        public void setProductName(String productName) {
         tvProductName.setText(productName);
        }

        public void setProductImage(String productImage) {
            Picasso.get().load(productImage).transform(new RoundedCorners(80, 0)).into(ivproductImage);
        }
        public void dropDownClicker(String name,String image,String price,String postId, Context context) {
            PopupMenu popup = new PopupMenu(context,ivDropArrow);
            popup.getMenuInflater().inflate(R.menu.top_selector, popup.getMenu());
            ivDropArrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    popup.setOnMenuItemClickListener(item->{
                        switch (item.getItemId()) {
                            case R.id.rank_first:
                                // Toast.makeText(context, ""+postId, Toast.LENGTH_SHORT).show();
                                updateRank("First",postId, name, image, price,"a");
                                break;
                            case R.id.rank_second:
                                // Toast.makeText(context, "second", Toast.LENGTH_SHORT).show();
                                updateRank("Second",postId, name, image, price, "b");
                                break;
                            case R.id.rank_thirt:
                                // Toast.makeText(context, "thirt", Toast.LENGTH_SHORT).show();
                                updateRank("Thirt",postId, name, image, price, "c");
                                break;
                            case R.id.rank_forth:
                                // Toast.makeText(context, "thirt", Toast.LENGTH_SHORT).show();
                                updateRank("Fourth",postId, name, image, price, "d");
                                break;
                        }
                        return true;
                    });
                    popup.show();
                }
            });
        }
        private void updateRank(String rank, String postId, String name, String image, String price, String i) {
            topRef.child(rank).child("ItemId").setValue(postId);
            topRef.child(rank).child("ItemName").setValue(name);
            topRef.child(rank).child("ItemImage").setValue(image);
            topRef.child(rank).child("ItemPrice").setValue(price);
            topRef.child(rank).child("Rank").setValue(i);
        }


    }
}