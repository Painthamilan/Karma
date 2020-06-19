package com.example.karma.admin_fragments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.karma.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ManageOrdersActivity extends AppCompatActivity {

    ImageView ivProductImage;
    TextView tvProductName,tvPhoneNumber,tvAddress,tvPrice,tvSelect;
    DatabaseReference orderRef;
    String orderId,state;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_orders);
        ivProductImage=findViewById(R.id.iv_product_image);
         tvProductName=findViewById(R.id.tv_product_name);
         tvPhoneNumber=findViewById(R.id.tv_phone_number);
         tvAddress=findViewById(R.id.tv_address);
         tvPrice=findViewById(R.id.tv_price);
         tvSelect=findViewById(R.id.tv_select);
         orderId=getIntent().getStringExtra("OrderId");

         orderRef= FirebaseDatabase.getInstance().getReference().child("Orders").child(orderId);

         orderRef.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 if (dataSnapshot.exists()){
                     String productimage=dataSnapshot.child("ProductImage").getValue().toString();
                     Picasso.get().load(productimage).into(ivProductImage);
                     String productName=dataSnapshot.child("ProductName").getValue().toString();
                     tvProductName.setText(productName);
                     String price=dataSnapshot.child("Price").getValue().toString();
                     tvPrice.setText(price);
                     String phone=dataSnapshot.child("PhoneNumber").getValue().toString();
                     tvPhoneNumber.setText(phone);
                     String address=dataSnapshot.child("Address").getValue().toString();
                     tvAddress.setText(address);

                 }
             }

             @Override
             public void onCancelled(@NonNull DatabaseError databaseError) {

             }
         });

        PopupMenu popup = new PopupMenu(this,tvSelect);
        popup.getMenuInflater().inflate(R.menu.menu_order_states, popup.getMenu());
        tvSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                popup.setOnMenuItemClickListener(item->{
                    switch (item.getItemId()) {
                        case R.id.state_confirm_order:
                            state="Confirmed";
                            updateState("Confirmed",orderId);
                            break;
                        case R.id.state_pack_order:
                            state="Packed";
                            updateState("Packed", orderId);
                            break;
                        case R.id.state_carry_order:
                            state="On the way";
                            updateState("On the way", orderId);
                            break;
                        case R.id.state_deliver_order:
                            state="Delivered";
                            updateState("Delivered", orderId);
                            break;
                        case R.id.state_return_order:
                            state="Returned";
                            updateState("Returned", orderId);
                            break;
                    }
                    return true;
                });
                popup.show();
            }
        });

    }

    private void updateState(String state, String orderId) {
        orderRef=FirebaseDatabase.getInstance().getReference().child("Orders").child(orderId);
        orderRef.child("Status").setValue(state);
    }
}
