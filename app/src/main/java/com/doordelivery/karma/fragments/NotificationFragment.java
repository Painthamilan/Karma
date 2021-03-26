package com.doordelivery.karma.fragments;


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

import com.doordelivery.karma.domains.Notifications;
import com.doordelivery.karma.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends Fragment {
    RecyclerView rvNotification;
    DatabaseReference notificationRef;
    FirebaseAuth cfAuth;
    String curUserId;
    TextView tvNothing;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root= inflater.inflate(R.layout.fragment_notification, container, false);
        cfAuth=FirebaseAuth.getInstance();
        rvNotification=root.findViewById(R.id.rv_notification);
        tvNothing=root.findViewById(R.id.tv_nothing);
        if (cfAuth.getCurrentUser() != null) {
            curUserId = cfAuth.getCurrentUser().getUid().toString();
            notificationRef = FirebaseDatabase.getInstance().getReference().child("User").child(curUserId).child("Notifications");

            rvNotification.setHasFixedSize(true);
            rvNotification.setLayoutManager(new LinearLayoutManager(getContext()));

            showMyNotification();
        }else {
            tvNothing.setVisibility(View.VISIBLE);
        }


        return root;

    }

    private void showMyNotification() {
        Query searchPeopleAndFriendsQuery = notificationRef.orderByChild("Counter");

        FirebaseRecyclerAdapter<Notifications, ItemViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Notifications, ItemViewHolder>(
                        Notifications.class,
                        R.layout.cart_layout,
                        ItemViewHolder.class,
                        searchPeopleAndFriendsQuery

                ) {
                    @Override
                    protected void populateViewHolder(ItemViewHolder findFriendsViewHolder, Notifications model, int position) {

                        findFriendsViewHolder.setDisplayName(model.getTitle());
                        findFriendsViewHolder.setCharacterName(model.getMessage());
                        findFriendsViewHolder.setProfileImage(model.getNotificationImage());
                        String searchedUserId = getRef(position).getKey();
                        //  findFriendsViewHolder.manageRequestButton(searchedUserId, findFriendsViewHolder.tvRequest);



                    }
                };
      
        rvNotification.setAdapter(firebaseRecyclerAdapter);

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
            tvBuy.setVisibility(View.INVISIBLE);
            ivRemove.setVisibility(View.INVISIBLE);


        }

        public void setProfileImage(String ProfileImage) {
            Picasso.get().load(ProfileImage).into(itemImage);
        }

        public void setDisplayName(String DisplayName) {
            itemName.setText(DisplayName);
        }

        public void setCharacterName(String CharacterName) {
            itemPrice.setText(CharacterName);
        }


    }

}
