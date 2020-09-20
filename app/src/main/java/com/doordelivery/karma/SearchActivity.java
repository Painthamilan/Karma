package com.doordelivery.karma;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class SearchActivity extends AppCompatActivity {

    String searchKey;
    EditText etSearch;
    RecyclerView rvSearchedItems;
    ImageView ivSearch;
    DatabaseReference searchRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ivSearch = findViewById(R.id.iv_search);
        rvSearchedItems = findViewById(R.id.rv_search);
        etSearch=findViewById(R.id.et_search_bar);
        searchKey=etSearch.getText().toString().trim();

        searchRef = FirebaseDatabase.getInstance().getReference().child("Products");
        rvSearchedItems.setHasFixedSize(true);
        rvSearchedItems.setLayoutManager(new LinearLayoutManager(this));
      //  SearchPeopleAndFriends("");
        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = etSearch.getText().toString();
                if (SearchPeopleAndFriends(text)==0){
                    SearchPeopleAndFriends(text.toUpperCase());
                }
            }
        });

    }

    private int SearchPeopleAndFriends(String searchKey) {
        Query searchPeopleAndFriendsQuery = searchRef.orderByChild("ProductName")
                .startAt(searchKey)
                .endAt(searchKey + "\uf8ff");

        FirebaseRecyclerAdapter<Products, ItemViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Products, ItemViewHolder>(
                        Products.class,
                        R.layout.searched_items_layout,
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
                                Intent intent=new Intent(SearchActivity.this, ViewSingleProductActivity.class);
                                intent.putExtra("REF_KEY",searchedUserId);
                                intent.putExtra("isOffer",false);
                                startActivity(intent);

                            }
                        });


                    }
                };

        rvSearchedItems.setAdapter(firebaseRecyclerAdapter);
        return firebaseRecyclerAdapter.getItemCount();

    }
    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        View cfView;
        TextView itemPrice, itemName;
        //  private String CURRENT_STATE;
        private FirebaseAuth cfAuth;
        private String currentUserId;
        DatabaseReference cfFollowerRef;
        DatabaseReference cfFollowingRef;
        ImageView itemImage;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            cfView = itemView;
            itemPrice = cfView.findViewById(R.id.price);
            itemImage = cfView.findViewById(R.id.iv_product_image);
            itemName = cfView.findViewById(R.id.tv_product_name);
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
