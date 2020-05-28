package com.example.karma.fragments;

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
import com.google.firebase.auth.FirebaseAuth;

public class UserProfileFragment extends Fragment {
    FirebaseAuth cfAuth;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_user_notifications, container, false);
        final TextView textView = root.findViewById(R.id.text_notifications);
        cfAuth=FirebaseAuth.getInstance();
        textView.setText("Logout");
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cfAuth.signOut();
                Intent intent=new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);

            }
        });

        return root;
    }
}