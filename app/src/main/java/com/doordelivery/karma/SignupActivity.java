package com.doordelivery.karma;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    EditText etName,etMail,etPassword,etReTypePassword;
    String name,mail,password,reTypePassword,curUser;
    TextView tvCreate;
    private FirebaseAuth cfAuth;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        etName=findViewById(R.id.et_name);
        etMail=findViewById(R.id.et_email);
        etPassword=findViewById(R.id.et_password);
        etReTypePassword=findViewById(R.id.et_re_type_password);
        tvCreate=findViewById(R.id.tv_Create);


        cfAuth=FirebaseAuth.getInstance();
        userRef= FirebaseDatabase.getInstance().getReference().child("User");

        tvCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name=etName.getText().toString();
                mail=etMail.getText().toString();
                password=etPassword.getText().toString();
                reTypePassword=etReTypePassword.getText().toString();
                if(TextUtils.isEmpty(name)){
                    Toast.makeText(SignupActivity.this, "Please enter your name", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(mail)){
                    Toast.makeText(SignupActivity.this, "Please enter your mail id", Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(password)){
                    Toast.makeText(SignupActivity.this, "Please enter a new password", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(reTypePassword)){
                    Toast.makeText(SignupActivity.this, "Please retype your password", Toast.LENGTH_SHORT).show();
                }else {
                    allowUserToSignIn();
                }
            }
        });

    }

    private void allowUserToSignIn() {
        cfAuth.createUserWithEmailAndPassword(mail, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            cfAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    saveUserInfo(cfAuth,name);
                                }
                            });
                        } else {
                            String message = task.getException().getMessage();
                            Toast.makeText(SignupActivity.this, "Error Ocured" + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void saveUserInfo(FirebaseAuth cfAuth, String name) {
        String currentUser=cfAuth.getCurrentUser().getUid();
        userRef.child(currentUser).child("DisplayName").setValue(name);
        String img=cfAuth.getCurrentUser().getPhotoUrl().toString();
        if (cfAuth.getCurrentUser().getUid().equals(Constants.ADMIN_ID)){
            Intent intent=new Intent(SignupActivity.this,AdminBottomBarActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }else {
            Intent intent=new Intent(SignupActivity.this,BottomBarActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
}
