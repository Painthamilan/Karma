package com.example.karma;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
FirebaseAuth cfAuth;
DatabaseReference cfUserRef;
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
       /* if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {

            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED) {

                // Log.d("permission", "permission denied to SEND_SMS - requesting it");
                String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

                requestPermissions(permissions, 1);

            }
        }

        */
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(curUserId==null){
         Intent intent=new Intent(MainActivity.this,LoginActivity.class);
         startActivity(intent);

        }
        else {
            if (curUserId.equals(Constants.ADMIN_ID)){
                sendUserToAdminBottomBarActivity();
            }else {
                sendUserToBottomBarActivity();
            }
            // CheckUserExistence();

        }

            // CheckUserExistence();

    }

    private void SendUserToLoginActivity() {
        Intent intent=new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private void sendUserToBottomBarActivity() {
        Intent intent=new Intent(MainActivity.this,BottomBarActivity.class);
        startActivity(intent);
    }
    private void sendUserToAdminBottomBarActivity() {
        Intent intent=new Intent(MainActivity.this,AdminBottomBarActivity.class);
        startActivity(intent);
    }

}
