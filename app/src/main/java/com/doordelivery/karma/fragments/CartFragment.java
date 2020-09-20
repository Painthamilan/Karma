package com.doordelivery.karma.fragments;


import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.doordelivery.karma.Products;
import com.doordelivery.karma.R;
import com.doordelivery.karma.SelectPaymentMethodActivity;
import com.doordelivery.karma.Utils;
import com.doordelivery.karma.ViewSingleProductActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * A simple {@link Fragment} subclass.
 */
public class CartFragment extends Fragment {
    RecyclerView rvCart;
    DatabaseReference cartRef,orderRef;
    FirebaseAuth cfAuth;
    String curUserId,allname,allid;
    ArrayList<String> totalName;
    ArrayList<String> totalId;
    TextView tvBuyAll;
EditText etphone,etAddress;
    TextView tvNothing;
    int numOfItems;
long countPosts;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root=inflater.inflate(R.layout.fragment_cart, container, false);
        cfAuth=FirebaseAuth.getInstance();
        rvCart=root.findViewById(R.id.rv_cart);
        tvBuyAll=root.findViewById(R.id.tv_buy_all);
        tvNothing=root.findViewById(R.id.tv_nothing);

        totalName=new ArrayList<String>();
        totalId=new ArrayList<String>();
        if (cfAuth.getCurrentUser() != null) {
            tvBuyAll.setVisibility(View.VISIBLE);
            curUserId = cfAuth.getCurrentUser().getUid().toString();

            rvCart.setHasFixedSize(true);
            rvCart.setLayoutManager(new LinearLayoutManager(getContext()));

            cartRef = FirebaseDatabase.getInstance().getReference().child("User").child(curUserId).child("MyCart");
            orderRef= FirebaseDatabase.getInstance().getReference().child("Orders");
            cartRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        Iterator<DataSnapshot> dataSnapshotsorder =  dataSnapshot.getChildren().iterator();
                        while (dataSnapshotsorder .hasNext()) {
                            DataSnapshot dataSnapshotChild = dataSnapshotsorder.next();
                            numOfItems += Integer.parseInt(String.valueOf(dataSnapshotChild .child("Price").getValue()));
                            totalName.add(dataSnapshotChild .child("ProductName").getValue().toString());
                            totalId.add(dataSnapshotChild .child("CartId").getValue().toString());

                        }
                        tvBuyAll.setText("BUY All "+String.valueOf(numOfItems)+".00 ₹");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            tvBuyAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder dialogBuilder=new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme).setCancelable(false);
                    View rowView= LayoutInflater.from(getContext()).inflate(R.layout.buy_all_dialog,null);
                    dialogBuilder.setView(rowView);
                    AlertDialog dialog = dialogBuilder.create();
                    if (dialog.getWindow() != null) {
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                    }
                    TextView tvCashOnDelivery=rowView.findViewById(R.id.tv_cash_on_delivery);
                    TextView tvCartPay=rowView.findViewById(R.id.tv_cart_payment);
                    etphone=rowView.findViewById(R.id.et_phone_number);
                    etAddress=rowView.findViewById(R.id.et_adress);;
                    tvCashOnDelivery.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (TextUtils.isEmpty(etAddress.getText().toString())&&TextUtils.isEmpty(etphone.getText().toString())){
                                Toast.makeText(getContext(), "enter phone number and address", Toast.LENGTH_SHORT).show();
                            }else {
                                saveOrder(totalId, totalName, numOfItems);
                            }
                            dialog.dismiss();
                        }
                    });

                    tvCartPay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
dialog.dismiss();
                        }
                    });


                    TextView dialogCancelTextView=rowView.findViewById(R.id.dialogCancel);

                    dialogCancelTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });

                    dialog.show();
                }
            });

            showMyCart();
        }else {
            tvNothing.setVisibility(View.VISIBLE);
        }


        return root;
    }
    private void saveOrder(ArrayList<String> totalId, ArrayList<String> totalName, int numOfItems) {
        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    countPosts = dataSnapshot.getChildrenCount();
                } else {
                    countPosts = 0;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        allname="";
        for (int i=0;i<totalName.size();i++){
            allname=allname+totalName.get(i)+",";
        }
        for (int i=0;i<totalId.size();i++){
            allid=totalId.get(i)+",";
        }
        cartRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    HashMap postMap = new HashMap();
                    postMap.put("OrderId", Utils.createRandomId());
                    postMap.put("ProductId", allid);
                    postMap.put("ProductName", allname.substring(0,allname.length()-1));
                    postMap.put("UserId", curUserId);
                    postMap.put("Counter", countPosts);
                    postMap.put("PhoneNumber", etphone.getText().toString());
                    postMap.put("Address", etAddress.getText().toString());
                    postMap.put("Price",numOfItems);
                    postMap.put("Status","NOT VERIFIED");
                    postMap.put("Email",cfAuth.getCurrentUser().getEmail().toString());

                    orderRef.child(curUserId + Utils.createRandomId()).updateChildren(postMap).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            Toast.makeText(getContext(), "Ordered successfully, We will verify soon..", Toast.LENGTH_SHORT).show();
                            //intentToUpdateDetails();
                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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
                                Intent intent=new Intent(getContext(), ViewSingleProductActivity.class);
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
