package com.example.karma;

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

public class LoginActivity extends AppCompatActivity {
    EditText etMail,etPassword;
    String mail,password,curUser;
    TextView tvLogin,newAccount;
    private FirebaseAuth cfAuth;
    private DatabaseReference userRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etMail=findViewById(R.id.et_email);
        etPassword=findViewById(R.id.et_password);
        tvLogin=findViewById(R.id.tv_login);
        newAccount=findViewById(R.id.tv_signin);


        cfAuth=FirebaseAuth.getInstance();
        userRef= FirebaseDatabase.getInstance().getReference().child("User");
        newAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,SignupActivity.class);
                startActivity(intent);
            }
        });
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mail=etMail.getText().toString();
                password=etPassword.getText().toString();
                if (mail.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Please enter your mail id", Toast.LENGTH_SHORT).show();
                }
                else if (password.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Please enter a new password", Toast.LENGTH_SHORT).show();
                }else {
                    allowUserToLogin();
                }


            }
        });
    }

    private void allowUserToLogin() {
        cfAuth.signInWithEmailAndPassword(mail,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            validateProfile();
                            // SendUserToMainActivity();
                            Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            String message=task.getException().getMessage();
                            Toast.makeText(LoginActivity.this, "Error.."+message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void validateProfile() {
        if (cfAuth.getCurrentUser().getUid().equals(Constants.ADMIN_ID)){
            Intent intent=new Intent(LoginActivity.this,AdminBottomBarActivity.class);
            startActivity(intent);
        }else {
            Intent intent=new Intent(LoginActivity.this,BottomBarActivity.class);
            startActivity(intent);
        }
    }
}
