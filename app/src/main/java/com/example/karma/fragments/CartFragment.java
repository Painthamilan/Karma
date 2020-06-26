package com.example.karma.fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.karma.Constants;
import com.example.karma.Products;
import com.example.karma.R;
import com.example.karma.SearchActivity;
import com.example.karma.SelectPaymentMethodActivity;
import com.example.karma.ViewProductActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.Iterator;

/**
 * A simple {@link Fragment} subclass.
 */
public class CartFragment extends Fragment {
    RecyclerView rvCart;
    DatabaseReference cartRef;
    FirebaseAuth cfAuth;
    String curUserId;
    TextView tvBuyAll;
    int numOfItems;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root=inflater.inflate(R.layout.fragment_cart, container, false);
        cfAuth=FirebaseAuth.getInstance();
        if (cfAuth.getCurrentUser() != null) {
            curUserId = cfAuth.getCurrentUser().getUid().toString();


        }
        rvCart=root.findViewById(R.id.rv_cart);
        tvBuyAll=root.findViewById(R.id.tv_buy_all);
        rvCart.setHasFixedSize(true);
        rvCart.setLayoutManager(new LinearLayoutManager(getContext()));

        cartRef = FirebaseDatabase.getInstance().getReference().child("User").child(curUserId).child("MyCart");
        cartRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Iterator<DataSnapshot> dataSnapshotsorder =  dataSnapshot.getChildren().iterator();
                    while (dataSnapshotsorder .hasNext()) {
                        DataSnapshot dataSnapshotChild = dataSnapshotsorder.next();
                        numOfItems += Integer.parseInt(String.valueOf(dataSnapshotChild .child("Price").getValue()));
                    }
                    tvBuyAll.setText("BUY All "+String.valueOf(numOfItems)+".00 ₹");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        showMyCart();
        return root;
    }

    private void showMyCart() {
        Query searchPeopleAndFriendsQuery = cartRef.orderByChild("ProductName");

        FirebaseRecyclerAdapter<Products, ItemViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Products, ItemViewHolder>(
                        Products.class,
                        R.layout.cart_layout,
                        ItemViewHolder.class,
                        searchPeopleAndFriendsQuery

                ) {
                    @Override
                    protected void populateViewHolder(ItemViewHolder findFriendsViewHolder, Products model, int position) {

                        findFriendsViewHolder.setDisplayName(model.getProductName());
                        findFriendsViewHolder.setCharacterName(model.getPrice());
                        findFriendsViewHolder.setProfileImage(model.getProductImage());
                        String searchedUserId = getRef(position).getKey();
                        //  findFriendsViewHolder.manageRequestButton(searchedUserId, findFriendsViewHolder.tvRequest);
                        findFriendsViewHolder.cfView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent=new Intent(getContext(), ViewProductActivity.class);
                                intent.putExtra("REF_KEY",searchedUserId);
                                intent.putExtra("isOffer",false);
                                startActivity(intent);

                            }
                        });
                        findFriendsViewHolder.tvBuy.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent=new Intent(getContext(), SelectPaymentMethodActivity.class);
                                intent.putExtra("REF_KEY",searchedUserId);
                                intent.putExtra("Price",Integer.parseInt(model.getPrice()));
                                startActivity(intent);
                            }
                        });
                        findFriendsViewHolder.ivRemove.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getRef(position).removeValue();
                            }
                        });



                    }
                };
        rvCart.setAdapter(firebaseRecyclerAdapter);
    }
    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        View cfView;
        TextView itemPrice, itemName;
        //  private String CURRENT_STATE;
        private FirebaseAuth cfAuth;
        private String currentUserId;
        DatabaseReference cfFollowerRef;
        DatabaseReference cfFollowingRef;
        ImageView itemImage,ivRemove;
        TextView tvBuy;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            cfView = itemView;
            itemPrice = cfView.findViewById(R.id.price);
            itemImage = cfView.findViewById(R.id.iv_product_image);
            itemName = cfView.findViewById(R.id.tv_product_name);
            tvBuy=cfView.findViewById(R.id.tv_buy);
            ivRemove=cfView.findViewById(R.id.iv_down_arrow);

        }

        public void setProfileImage(String ProfileImage) {
            Picasso.get().load(ProfileImage).into(itemImage);
        }

        public void setDisplayName(String DisplayName) {
            itemName.setText(DisplayName);
        }

        public void setCharacterName(String CharacterName) {
            itemPrice.setText(CharacterName+".00 ₹");
        }


    }
}
