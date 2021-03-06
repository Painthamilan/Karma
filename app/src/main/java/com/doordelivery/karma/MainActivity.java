package com.doordelivery.karma;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
FirebaseAuth cfAuth;
DatabaseReference cfUserRef;
boolean isSelected;
String curUserId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cfAuth=FirebaseAuth.getInstance();
        cfUserRef= FirebaseDatabase.getInstance().getReference();

        if (cfAuth.getCurrentUser() != null) {
            curUserId = cfAuth.getCurrentUser().getUid().toString();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */

                SharedPreferences preferences=getSharedPreferences("REGION_SELECTOR",MODE_PRIVATE);
                isSelected=preferences.getBoolean("IS_SELECTED",false);
                if (!isSelected){
                    sendUserToSelectRegionActivity();
                }else {
                    sendUserToBottomBarActivity();
                }

            }
        }, 2000);

            // CheckUserExistence();

    }


    private void sendUserToBottomBarActivity() {
        Intent intent=new Intent(MainActivity.this,BottomBarActivity.class);
        startActivity(intent);
    }
    private void sendUserToSelectRegionActivity() {
        Intent intent=new Intent(MainActivity.this,SelectRegionActivity.class);
        startActivity(intent);
    }

}
