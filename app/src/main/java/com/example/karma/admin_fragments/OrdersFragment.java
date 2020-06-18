package com.example.karma.admin_fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.karma.LoginActivity;
import com.example.karma.R;
import com.example.karma.ViewMyOrdersActivity;
import com.example.karma.ViewSubCatsActivity;
import com.google.firebase.auth.FirebaseAuth;

public class OrdersFragment extends Fragment {
private FirebaseAuth cfAuth;
    TextView tvMyOrders,tvAddOffers,tvManageInstants;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_orders, container, false);
        final TextView textView = root.findViewById(R.id.text_notifications);

        cfAuth=FirebaseAuth.getInstance();
        tvMyOrders=root.findViewById(R.id.tv_my_orders);
        tvMyOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), ViewMyOrdersActivity.class);
                startActivity(intent);
            }
        });
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              cfAuth.signOut();
                Intent intent=new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        tvAddOffers=root.findViewById(R.id.tv_add_special);
        tvAddOffers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), AddOffersActivity.class);
                startActivity(intent);
            }
        });

        tvManageInstants=root.findViewById(R.id.tv_manage_instants);
        tvManageInstants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), ViewSubCatsActivity.class);
                intent.putExtra("CAT_NAME","Instant");
                intent.putExtra("IsInstant",true);

                startActivity(intent);
            }
        });
        return root;
    }
}