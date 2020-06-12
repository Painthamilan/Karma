package com.example.karma;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

public class ViewProductActivity extends AppCompatActivity {
    ImageView ivProductImage;
    TextView tvProductName,tvPrice;
    TextView tvOrder;
    DatabaseReference productRef,ordersref;
    FirebaseAuth cfAuth;
    String curUserId,key,curDate,curTime,randomid;
    String productUrl;
    String productName;
    String price,phoneNum,adress;
    long countPosts;
    EditText etPhoneNumber,etAdress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product);
        ivProductImage=findViewById(R.id.iv_product_image);
        tvProductName=findViewById(R.id.tv_product_name);
        tvPrice=findViewById(R.id.tv_price);
        tvOrder=findViewById(R.id.tv_order);
        cfAuth=FirebaseAuth.getInstance();
        curUserId=cfAuth.getCurrentUser().getUid();
        etAdress=findViewById(R.id.et_adress);
        etPhoneNumber=findViewById(R.id.et_phone_number);
        key=getIntent().getStringExtra("REF_KEY");




        if (curUserId.equals(Constants.ADMIN_ID)){
            tvOrder.setVisibility(View.INVISIBLE);
        }
        ordersref= FirebaseDatabase.getInstance().getReference().child("Orders");
        productRef= FirebaseDatabase.getInstance().getReference().child("Products").child(key);
        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              if (dataSnapshot.exists()){
                   productUrl=dataSnapshot.child("ProductImage").getValue().toString();
                   productName=dataSnapshot.child("ProductName").getValue().toString();
                   price=dataSnapshot.child("Price").getValue().toString();

                  Picasso.get().load(productUrl).into(ivProductImage);
                  tvProductName.setText(productName);
                  tvPrice.setText(price);
              }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        curDate = dateFormat.format(new Date());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH-mm-ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        curTime = timeFormat.format(new Date());
        randomid=curDate.replace("-", "")+curTime.replace("-", "");
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
        tvOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                phoneNum=etPhoneNumber.getText().toString();
                adress=etAdress.getText().toString();
                if (TextUtils.isEmpty(phoneNum)||TextUtils.isEmpty(adress)){
                    Toast.makeText(ViewProductActivity.this, "please give phone mu,ber and address!", Toast.LENGTH_SHORT).show();
                }else {

                    HashMap postMap = new HashMap();
                    postMap.put("OrderId", curUserId + randomid);
                    postMap.put("ProductId", key);
                    postMap.put("ProductName", productName);
                    postMap.put("ProductImage", productUrl);
                    postMap.put("UserId", curUserId);
                    postMap.put("Counter", countPosts);
                    postMap.put("PhoneNumber", phoneNum);
                    postMap.put("Address", adress);
                    postMap.put("Status", "Not verified");
                    ordersref.child(curUserId + randomid).updateChildren(postMap).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                             Toast.makeText(ViewProductActivity.this, "Ordered successfully, We will verify soon..", Toast.LENGTH_SHORT).show();
                            intentToUpdateDetails();
                        }
                    });
                }

            }
        });

    }

    private void intentToUpdateDetails() {
        Intent intent=new Intent(ViewProductActivity.this,BottomBarActivity.class);

        startActivity(intent);

    }
}

