package com.doordelivery.karma.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.doordelivery.karma.activities.AllCatActivity;
import com.doordelivery.karma.activities.ContactUs;
import com.doordelivery.karma.activities.OnlineCourseActivity;
import com.doordelivery.karma.domains.Products;
import com.doordelivery.karma.R;
import com.doordelivery.karma.activities.RecentItemsActivity;
import com.doordelivery.karma.activities.RoundedCorners;
import com.doordelivery.karma.activities.SearchActivity;
import com.doordelivery.karma.activities.Utils;
import com.doordelivery.karma.activities.ViewAllItemsActivity;
import com.doordelivery.karma.activities.ViewAllOffersActivity;
import com.doordelivery.karma.activities.ViewItemsActivity;
import com.doordelivery.karma.activities.ViewSingleProductActivity;
import com.doordelivery.karma.activities.ViewSliderProductActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.doordelivery.karma.activities.Utils.RELEASE_TYPE;

public class TopFragment extends Fragment {
    private FirebaseAuth cfAuth;
    private String curUserId, sliderType, message, name, key, catagory;
    TextView etSearch, tvContact, tvD2d,tvViewAll;
    private RecyclerView rvProducts, rvTopFragments, rvInstants;
    private DatabaseReference cfPostRef, topRef, instantsRef;
    private TextView ivInstant, tvMobile;
    TextView ivOffers, tvCats, tvCourses,ivOnlineCourses;
    boolean hasSub;
    ImageView ivSamsung,ivXiaomi,ivOppo,ivVivo;

    ImageSlider imageSlider;

    AlertDialog.Builder dialogBuilder;
    AlertDialog dialog;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
            View root = inflater.inflate(R.layout.fragment_top_items, container, false);
        final TextView textView = root.findViewById(R.id.text_home);

        tvD2d = root.findViewById(R.id.tv_d2d2);
        tvD2d.setText(Html.fromHtml("D" + "<font color=\"#E51616\">" + 2 + "</font>" + "D"));


        ivSamsung=root.findViewById(R.id.iv_samsung);
        ivXiaomi=root.findViewById(R.id.iv_xiaomi);
        ivOppo=root.findViewById(R.id.iv_oppo);
        ivVivo=root.findViewById(R.id.iv_vivo);

        ivSamsung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ViewItemsActivity.class);
                intent.putExtra("REF_KEY", "Samsung");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });


        ivXiaomi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ViewItemsActivity.class);
                intent.putExtra("REF_KEY", "Xiaomi");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        ivOppo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ViewItemsActivity.class);
                intent.putExtra("REF_KEY", "Oppo");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        ivVivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ViewItemsActivity.class);
                intent.putExtra("REF_KEY", "Vivo");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });



        tvViewAll=root.findViewById(R.id.tv_view_more_top);
        tvViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), ViewAllItemsActivity.class);
                startActivity(intent);
            }
        });

        tvCourses=root.findViewById(R.id.iv_courses);
        tvCourses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), OnlineCourseActivity.class);
               // intent.putExtra("REF_KEY", "Online Courses");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        imageSlider = root.findViewById(R.id.is_slider);

        showImageSlider(imageSlider);

        tvCats = root.findViewById(R.id.tv_more_brands);
        rvTopFragments = root.findViewById(R.id.rv_topItems);
       // tvRecent = root.findViewById(R.id.recent);
        tvContact = root.findViewById(R.id.tv_mobile);
        tvContact.setSelected(true);
        ivOnlineCourses=root.findViewById(R.id.iv_courses);

        ivOnlineCourses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), OnlineCourseActivity.class);
                startActivity(intent);
            }
        });
        tvContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ContactUs.class);
                startActivity(intent);
            }
        });



        etSearch = root.findViewById(R.id.et_search_bar);
        etSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
            }
        });

        tvCats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AllCatActivity.class);
                startActivity(intent);
            }
        });


        rvTopFragments.setHasFixedSize(true);
        LinearLayoutManager horizontalYalayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        horizontalYalayoutManager.setStackFromEnd(true);
        rvTopFragments.setLayoutManager(horizontalYalayoutManager);
        showTop();

        ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("loading");
        pd.show();
// Hide after some seconds
        final Handler handler = new Handler();
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

        handler.postDelayed(runnable, 2000);

        return root;
    }

    @Override
    public void onStop() {
        imageSlider.stopSliding();
        super.onStop();
    }

    private void showImageSlider(ImageSlider imageSlider) {
        List<SlideModel> remoteImages = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference().child(RELEASE_TYPE).child("Slider")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot data : snapshot.getChildren()) {
                            sliderType = data.child("SliderType").getValue().toString();
                            name = data.child("ProductName").getValue().toString();
                            remoteImages.add(new SlideModel(data.child("ProductImage").getValue().toString(),
                                    name, ScaleTypes.FIT));


                            imageSlider.setImageList(remoteImages, ScaleTypes.FIT);

                            imageSlider.setItemClickListener(new ItemClickListener() {
                                @Override
                                public void onItemSelected(int i) {

                                    switch (i) {
                                        case 0:

                                            FirebaseDatabase.getInstance().getReference().child(RELEASE_TYPE).child("Slider").child("Catagory")
                                                    .addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            if (snapshot.exists()) {
                                                                catagory = snapshot.child("ProductCatagory").getValue().toString();
                                                                try {
                                                                    hasSub = Boolean.parseBoolean(snapshot.child("HasSub").getValue().toString());
                                                                } catch (Exception e) {
                                                                    e.printStackTrace();
                                                                }
                                                                Intent intent1 = new Intent(getContext(), ViewItemsActivity.class);
                                                                intent1.putExtra("MAIN_CAT_NAME", catagory);
                                                                startActivity(intent1);
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });

                                            break;

                                        case 1:

                                            FirebaseDatabase.getInstance().getReference().child(RELEASE_TYPE).child("Slider").child("Message")
                                                    .addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            if (snapshot.exists()) {
                                                                name = snapshot.child("ProductName").getValue().toString();
                                                                message = snapshot.child("Specifications").getValue().toString();
                                                                openPopupMessage(message, name);
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });
                                            break;
                                        case 2:

                                            FirebaseDatabase.getInstance().getReference().child(RELEASE_TYPE).child("Slider").child("Offer")
                                                    .addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            if (snapshot.exists()) {
                                                                sliderType = snapshot.child("SliderType").getValue().toString();
                                                                key = snapshot.child("ProductId").getValue().toString();
                                                                Intent intent = new Intent(getContext(), ViewSliderProductActivity.class);
                                                                intent.putExtra("TYPE", sliderType);
                                                                intent.putExtra("REF_KEY", key);
                                                                startActivity(intent);
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });

                                            break;
                                        case 3:
                                            FirebaseDatabase.getInstance().getReference().child(RELEASE_TYPE).child("Slider").child("Product")
                                                    .addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            if (snapshot.exists()) {

                                                                sliderType = snapshot.child("SliderType").getValue().toString();
                                                                key = snapshot.child("ProductId").getValue().toString();
                                                                Intent intent2 = new Intent(getContext(), ViewSliderProductActivity.class);
                                                                intent2.putExtra("TYPE", sliderType);
                                                                intent2.putExtra("REF_KEY", key);
                                                                startActivity(intent2);
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });
                                            break;


                                    }
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    private void openPopupMessage(String message, String title) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(TopFragment.this.getContext(), R.style.AlertDialogTheme).setCancelable(false);
        View rowView = LayoutInflater.from(TopFragment.this.getContext()).inflate(R.layout.message_display_layout, null);
        dialogBuilder.setView(rowView);
        AlertDialog dialog = dialogBuilder.create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        TextView dialogTitleTextView = rowView.findViewById(R.id.dialogTitle);
        TextView dialogMessageTextView = rowView.findViewById(R.id.dialogText);
        TextView dialogCancelTextView = rowView.findViewById(R.id.dialogCancel);

        dialogTitleTextView.setText(title);
        dialogMessageTextView.setText(message);


        dialogCancelTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void showrecentItems(Context context) {
        Intent intent = new Intent(context, RecentItemsActivity.class);
        startActivity(intent);
    }


    private void showTop() {
        topRef = FirebaseDatabase.getInstance().getReference().child(RELEASE_TYPE).child("Products");
        Query searchPeopleAndFriendsQuery = topRef.orderByChild("Counter").limitToLast(4);
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
                                Intent intent = new Intent(getActivity(), ViewSingleProductActivity.class);
                                intent.putExtra("REF_KEY", model.getProductId());
                                intent.putExtra("isOffer", false);
                                startActivity(intent);
                            }
                        });
                    }
                };
        rvTopFragments.setAdapter(firebaseRecyclerAdapter);


    }


    public static class TopViewHolder extends RecyclerView.ViewHolder {
        View cfView;
        FirebaseAuth cfAuth = FirebaseAuth.getInstance();
        String userId;
        TextView tvProductName, tvPrice;
        ImageView ivproductImage, ivDropArrow;
        DatabaseReference topRef;

        public TopViewHolder(@NonNull View itemView) {
            super(itemView);
            cfView = itemView;
            tvPrice = cfView.findViewById(R.id.price);
            tvProductName = cfView.findViewById(R.id.tv_product_name);
            tvProductName.setSelected(true);
            ivproductImage = cfView.findViewById(R.id.iv_product_image);
            if (cfAuth.getCurrentUser() != null) {
                userId = cfAuth.getCurrentUser().getUid();
            }


            topRef = FirebaseDatabase.getInstance().getReference().child(RELEASE_TYPE).child("TopItems");


        }

        public void setPrice(String price, String percentage) {
            tvPrice.setText(Utils.getActualPrice(price, percentage) + ".00");

        }

        public void setProductName(String productName) {
            tvProductName.setText(productName);
        }

        public void setProductImage(String productImage) {
            Picasso.get().load(productImage).transform(new RoundedCorners(10,1)).into(ivproductImage);
        }


    }
}