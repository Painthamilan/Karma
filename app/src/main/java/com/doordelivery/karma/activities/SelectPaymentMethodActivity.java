package com.doordelivery.karma.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.doordelivery.karma.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import static com.doordelivery.karma.activities.Utils.RELEASE_TYPE;

public class SelectPaymentMethodActivity extends AppCompatActivity {
    TextView tvCashOnDelivery,tvCartPayment;
    DatabaseReference ordersref,cartRef;
    FirebaseAuth cfAuth;
    String curUserId,key,curDate,curTime,randomid;
    String productUrl;
    String productName,productSpecs;
    String phoneNum,adress,actualPrice;
    int price;
    long countPosts;
    boolean isOffer,isInstant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_payment_method);
        cfAuth=FirebaseAuth.getInstance();
        key=getIntent().getStringExtra("REF_KEY");

        price=getIntent().getIntExtra("Price",0);
        if (cfAuth.getCurrentUser() != null) {
            curUserId = cfAuth.getCurrentUser().getUid().toString();

            cartRef= FirebaseDatabase.getInstance().getReference().child(RELEASE_TYPE).child("User").child(curUserId).child("MyCart").child(key);
        }
        ordersref=FirebaseDatabase.getInstance().getReference().child(RELEASE_TYPE).child("Orders");

        tvCartPayment=findViewById(R.id.tv_cart_payment);
        tvCashOnDelivery=findViewById(R.id.tv_cash_on_delivery);

        ordersref.addValueEventListener(new ValueEventListener() {
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



        if (price>=20000){
            tvCartPayment.setEnabled(true);
        }
        tvCartPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        tvCashOnDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveOrder();
            }
        });


    }

    private void saveOrder() {
        cartRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    HashMap postMap = new HashMap();
                    postMap.put("OrderId", key);
                    postMap.put("ProductId", dataSnapshot.child("ProductId").getValue().toString());
                    postMap.put("ProductName", dataSnapshot.child("ProductName").getValue().toString());
                    postMap.put("ProductImage", dataSnapshot.child("ProductImage").getValue().toString());
                    postMap.put("UserId", curUserId);
                    postMap.put("Counter", countPosts);
                    postMap.put("PhoneNumber", dataSnapshot.child("PhoneNumber").getValue().toString());
                    postMap.put("Address", dataSnapshot.child("Address").getValue().toString());
                    postMap.put("Price",dataSnapshot.child("Price").getValue().toString());
                    postMap.put("Status",dataSnapshot.child("Status").getValue().toString());
                    postMap.put("Email",cfAuth.getCurrentUser().getEmail().toString());
                    postMap.put("NIC", dataSnapshot.child("NIC").getValue().toString());

                    ordersref.child(curUserId + randomid).updateChildren(postMap).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            Toast.makeText(SelectPaymentMethodActivity.this, "Ordered successfully, We will verify soon..", Toast.LENGTH_SHORT).show();
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
}
