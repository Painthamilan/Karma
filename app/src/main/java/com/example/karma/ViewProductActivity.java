package com.example.karma;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.karma.admin_fragments.AdminViewProductActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
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
    TextView tvProductName,tvPrice,tvAddCart,tvNewPrice;
    TextView tvOrder,tvSpecs,tvPercentage;
    DatabaseReference productRef,ordersref,cardRef,imageRef;
    FirebaseAuth cfAuth;
    RecyclerView rvImage;
    String curUserId,key,curDate,curTime,randomid;
    String productUrl;
    String productName,productSpecs;
    String price,phoneNum,adress,actualPrice,percentage;
    long countPosts;
    boolean isOffer,isInstant;
    EditText etPhoneNumber,etAdress;
    boolean isCart;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_product);
        Utils.setTopBar(getWindow(),getResources());
        ivProductImage=findViewById(R.id.iv_product_image);
        tvProductName=findViewById(R.id.tv_product_name);
        tvPrice=findViewById(R.id.tv_price);
        tvOrder=findViewById(R.id.tv_order);
        tvNewPrice=findViewById(R.id.tv_new_price);
        tvPercentage=findViewById(R.id.tv_percentage);
        cfAuth=FirebaseAuth.getInstance();
        if (cfAuth.getCurrentUser() != null) {
            curUserId = cfAuth.getCurrentUser().getUid().toString();
            if (curUserId.equals(Constants.ADMIN_ID)){
                tvOrder.setVisibility(View.INVISIBLE);
            }

            cardRef=FirebaseDatabase.getInstance().getReference().child("User").child(curUserId).child("MyCart");
        }
        rvImage=findViewById(R.id.rv_images);
        rvImage.setHasFixedSize(true);
        LinearLayoutManager horizontalYalayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,true);
        horizontalYalayoutManager.setStackFromEnd(true);
        rvImage.setLayoutManager(horizontalYalayoutManager);

       // curUserId=cfAuth.getCurrentUser().getUid();
        etAdress=findViewById(R.id.et_adress);
        tvSpecs=findViewById(R.id.tv_specifications);
        etPhoneNumber=findViewById(R.id.et_phone_number);
        tvAddCart=findViewById(R.id.tv_cart);
        key=getIntent().getStringExtra("REF_KEY");
        isOffer=getIntent().getBooleanExtra("isOffer",false);
        isInstant=getIntent().getBooleanExtra("IsInstant",false);
        showAllImages();






        ordersref= FirebaseDatabase.getInstance().getReference().child("Orders");
        if (isOffer){
            actualPrice=getIntent().getStringExtra("ActualPrice");
            productRef= FirebaseDatabase.getInstance().getReference().child("Offers").child(key);
            productRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        productUrl=dataSnapshot.child("ProductImage").getValue().toString();
                        productName=dataSnapshot.child("ProductName").getValue().toString();
                        price=actualPrice;
                        productSpecs=dataSnapshot.child("Specifications").getValue().toString();
                        if (dataSnapshot.hasChild("Percentage")) {
                            percentage = dataSnapshot.child("Percentage").getValue().toString();
                            tvPercentage.setText(percentage+"%  discount");
                        }
                        Picasso.get().load(productUrl).into(ivProductImage);
                        tvProductName.setText(productName);
                        tvPrice.setText(price+".00");
                        tvSpecs.setText(productSpecs);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }{
            productRef= FirebaseDatabase.getInstance().getReference().child("Products").child(key);
            productRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        productUrl=dataSnapshot.child("ProductImage").getValue().toString();
                        productName=dataSnapshot.child("ProductName").getValue().toString();
                        price=dataSnapshot.child("Price").getValue().toString();
                        productSpecs=dataSnapshot.child("Specifications").getValue().toString();
                        if (dataSnapshot.hasChild("Percentage")) {
                            percentage = dataSnapshot.child("Percentage").getValue().toString();
                            tvPercentage.setText(percentage+"%");
                            tvPrice.setText(price+".00 ");
                            tvPrice.setTextColor(Color.RED);
                            tvPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);

                           // tvPercentage.setText(percentage +" %");


                            price=Utils.getActualPrice(price,percentage);
                            tvNewPrice.setText(price+".00");

                        }else{
                            tvPrice.setText(price+".00");
                            tvPrice.setTextColor(Color.RED);
                            tvPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                        }
                        Picasso.get().load(productUrl).into(ivProductImage);
                        tvProductName.setText(productName);

                        tvSpecs.setText(productSpecs);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }


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
                if (curUserId==null){
                    showDialogForNotLoggedId();
                }else {

                    if (isInstant) {
                        showDialogForConfirmInstant();
                    }
                    else {
                        showDialogForConfirmorder();
                    }
                }

            }
        });
        tvAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCart=true;
                if (curUserId==null){
                    showDialogForNotLoggedId();
                }else {

                    if (isInstant) {
                        showDialogForConfirmInstant();
                    }
                    else {
                        showDialogForConfirmorder();
                    }
                }
            }
        });


    }

    private void showDialogForConfirmInstant() {

        AlertDialog.Builder dialogBuilder=new AlertDialog.Builder(ViewProductActivity.this, R.style.AlertDialogTheme).setCancelable(false);
        View rowView= LayoutInflater.from(ViewProductActivity.this).inflate(R.layout.instant_layout,null);
        dialogBuilder.setView(rowView);
        AlertDialog dialog = dialogBuilder.create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        TextView dialogTitleTextView=rowView.findViewById(R.id.dialogTitle);
        EditText etNic=rowView.findViewById(R.id.et_nic);
        TextView dialogMessageTextView=rowView.findViewById(R.id.dialogText);
        TextView dialogCancelTextView=rowView.findViewById(R.id.dialogCancel);
        TextView dialogConfirmTextView=rowView.findViewById(R.id.dialogConfirm);
        if (isCart){
           dialogMessageTextView.setText("Are you sure you want to add to cart?");
            dialogTitleTextView.setText("Add to cart");
        }
        dialogMessageTextView.setText("Need to give NIC number");
        dialogConfirmTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nic=etNic.getText().toString();
                if (TextUtils.isEmpty(nic)){
                    Toast.makeText(ViewProductActivity.this, "Please enter NIC number", Toast.LENGTH_SHORT).show();
                }else {
                    if (isCart){
                        addtoCart();
                    }else {
                        orderProduct(nic);
                    }
                }
                dialog.dismiss();

                /*Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"painthamilan29@gmail.com"});
                i.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
                i.putExtra(Intent.EXTRA_TEXT   , "body of email");
                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(ViewProductActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }*/
            }
        });
        dialogCancelTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void addtoCart() {
        phoneNum = etPhoneNumber.getText().toString();
        adress = etAdress.getText().toString();
        if (TextUtils.isEmpty(phoneNum) || TextUtils.isEmpty(adress)) {
            Toast.makeText(ViewProductActivity.this, "please give phone number and address!", Toast.LENGTH_SHORT).show();
        } else {
            HashMap postMap = new HashMap();
            postMap.put("CartId", curUserId + randomid);
            postMap.put("ProductId", key);
            postMap.put("ProductName", productName);
            postMap.put("ProductImage", productUrl);
            postMap.put("UserId", curUserId);
            postMap.put("Counter", countPosts);
            postMap.put("PhoneNumber", phoneNum);
            postMap.put("Address", adress);
            postMap.put("Price", price);
            postMap.put("Status", "Not Verified");
            postMap.put("Email", cfAuth.getCurrentUser().getEmail().toString());
            postMap.put("NIC", "NO_NIC");
            if (isInstant) {
                postMap.put("IsInstant", true);
            }
            cardRef.child(curUserId + randomid).updateChildren(postMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(ViewProductActivity.this, "Added", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    private void showDialogForConfirmorder() {
        AlertDialog.Builder dialogBuilder=new AlertDialog.Builder(ViewProductActivity.this, R.style.AlertDialogTheme).setCancelable(false);
        View rowView= LayoutInflater.from(ViewProductActivity.this).inflate(R.layout.general_alert_dialog,null);
        dialogBuilder.setView(rowView);
        AlertDialog dialog = dialogBuilder.create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        TextView dialogTitleTextView=rowView.findViewById(R.id.dialogTitle);
        TextView dialogMessageTextView=rowView.findViewById(R.id.dialogText);
        TextView dialogCancelTextView=rowView.findViewById(R.id.dialogCancel);
        TextView dialogConfirmTextView=rowView.findViewById(R.id.dialogConfirm);
        dialogConfirmTextView.setText("Confirm");

        dialogTitleTextView.setText("Confirm order");
        dialogMessageTextView.setText("Are you sure you want to purchase?");
        if (isCart){
            dialogMessageTextView.setText("Are you sure you want to add to cart?");
            dialogTitleTextView.setText("Add to cart");
        }
        dialogConfirmTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCart){
                    addtoCart();
                }else {
                    orderProduct("NO_NIC");
                }
                dialog.dismiss();

            }
        });
        dialogCancelTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void orderProduct(String nic) {
        phoneNum = etPhoneNumber.getText().toString();
        adress = etAdress.getText().toString();
        if (TextUtils.isEmpty(phoneNum) || TextUtils.isEmpty(adress)) {
            Toast.makeText(ViewProductActivity.this, "please give phone number and address!", Toast.LENGTH_SHORT).show();
        } else {

            HashMap postMap = new HashMap();
            postMap.put("OrderId", curUserId + randomid);
            postMap.put("ProductId", key);
            postMap.put("ProductName", productName);
            postMap.put("ProductImage", productUrl);
            postMap.put("UserId", curUserId);
            postMap.put("Counter", countPosts);
            postMap.put("PhoneNumber", phoneNum);
            postMap.put("Address", adress);
            postMap.put("Price",price);
            postMap.put("Status","Not Verified");
            postMap.put("Email",cfAuth.getCurrentUser().getEmail().toString());
            postMap.put("NIC", nic);
            if (isInstant){
                postMap.put("IsInstant", true);
            }

            ordersref.child(curUserId + randomid).updateChildren(postMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    Toast.makeText(ViewProductActivity.this, "Ordered successfully, We will verify soon..", Toast.LENGTH_SHORT).show();
                    intentToUpdateDetails();
                }
            });
        }
    }

    private void showDialogForNotLoggedId() {
        AlertDialog.Builder dialogBuilder=new AlertDialog.Builder(ViewProductActivity.this, R.style.AlertDialogTheme).setCancelable(false);
        View rowView= LayoutInflater.from(ViewProductActivity.this).inflate(R.layout.general_alert_dialog,null);
        dialogBuilder.setView(rowView);
        AlertDialog dialog = dialogBuilder.create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        TextView dialogTitleTextView=rowView.findViewById(R.id.dialogTitle);
        TextView dialogMessageTextView=rowView.findViewById(R.id.dialogText);
        TextView dialogCancelTextView=rowView.findViewById(R.id.dialogCancel);
        TextView dialogConfirmTextView=rowView.findViewById(R.id.dialogConfirm);
        dialogConfirmTextView.setText("Sign In");

        dialogTitleTextView.setText("Sign In required");
        dialogMessageTextView.setText("You have to login befor order something..");
        dialogConfirmTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent=new Intent(ViewProductActivity.this,LoginActivity.class);
               startActivity(intent);
            }
        });
        dialogCancelTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void intentToUpdateDetails() {
        Intent intent=new Intent(ViewProductActivity.this,BottomBarActivity.class);

        startActivity(intent);

    }
    private void showAllImages() {
        imageRef = FirebaseDatabase.getInstance().getReference().child("Products").child(key).child("DetailImages");

        FirebaseRecyclerAdapter<Img, AdminViewProductActivity.TopViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Img, AdminViewProductActivity.TopViewHolder>(
                        Img.class,
                        R.layout.image_layout,
                        AdminViewProductActivity.TopViewHolder.class,
                        imageRef

                ) {
                    @Override
                    protected void populateViewHolder(AdminViewProductActivity.TopViewHolder postViewHolder, Img model, int position) {
                        String postKey = getRef(position).getKey();
                        postViewHolder.setImage(model.getImageUrl());
                        postViewHolder.cfView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                postViewHolder.showDialog(model.getImageUrl(),ViewProductActivity.this);
                            }
                        });

                    }
                };
        rvImage.setAdapter(firebaseRecyclerAdapter);
    }
}

