package com.doordelivery.karma.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.doordelivery.karma.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import static com.doordelivery.karma.activities.Utils.RELEASE_TYPE;

public class ApplyActivity extends AppCompatActivity {

    EditText etName, etAddress, etEmail, etPhone, etQualification, etLanguage, etComments;
    TextView tvRegister;
    String name, address, email, phone, qualification, language, comments,
            courseName, randomId;
    ProgressDialog progressdialog;

    DatabaseReference applicationsRef;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply);

        Utils.setTopBar(getWindow(),getResources());
        progressdialog = new ProgressDialog(this);
        progressdialog.setMessage("Please Wait....");
        progressdialog.setCancelable(false);

        applicationsRef = FirebaseDatabase.getInstance().getReference().child(RELEASE_TYPE).child("Applications");
        etName = findViewById(R.id.et_full_name);
        etAddress = findViewById(R.id.et_address);
        etEmail = findViewById(R.id.et_email);
        etPhone = findViewById(R.id.et_mobile);
        etQualification = findViewById(R.id.et_qualification);
        etLanguage = findViewById(R.id.et_language);
        etComments = findViewById(R.id.et_comments);
        tvRegister = findViewById(R.id.tv_register);


        courseName = getIntent().getStringExtra("CourseName");

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etName.getText().toString())) {
                    Toast.makeText(ApplyActivity.this, "Please enter your name..!", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(etAddress.getText().toString())) {
                    Toast.makeText(ApplyActivity.this, "Please enter your address..!", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(etEmail.getText().toString())) {
                    Toast.makeText(ApplyActivity.this, "Please enter your email..!", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(etPhone.getText().toString())) {
                    Toast.makeText(ApplyActivity.this, "Please enter your mobile number..!", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(etQualification.getText().toString())) {
                    Toast.makeText(ApplyActivity.this, "Please enter your educational qualification..!", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(etLanguage.getText().toString())) {
                    Toast.makeText(ApplyActivity.this, "Please enter you prefered language..!", Toast.LENGTH_SHORT).show();
                } else {
                    openPopup();
                }
            }
        });
    }

    private void openPopup() {
        name = etName.getText().toString();
        address = etAddress.getText().toString();
        email = etEmail.getText().toString();
        phone = etPhone.getText().toString();
        qualification = etQualification.getText().toString();
        language = etLanguage.getText().toString();
        comments = etComments.getText().toString();

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ApplyActivity.this, R.style.AlertDialogTheme).setCancelable(false);
        View rowView = LayoutInflater.from(ApplyActivity.this).inflate(R.layout.apply_popup_layout, null);
        dialogBuilder.setView(rowView);
        AlertDialog dialog = dialogBuilder.create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        TextView tvName, tvCourse, tvMobile, tvConfirm, tvCancel;
        tvName = rowView.findViewById(R.id.tv_full_name);
        tvCourse = rowView.findViewById(R.id.tv_course_name);
        tvMobile = rowView.findViewById(R.id.tv_mobile);
        tvConfirm = rowView.findViewById(R.id.tv_confirm);
        tvCancel = rowView.findViewById(R.id.tv_cancel);

        tvName.setText(": " + name);
        tvCourse.setText(": " + courseName);
        tvMobile.setText(": " + phone);
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressdialog.show();
                randomId = Utils.getRandomId();
                if (TextUtils.isEmpty(etComments.getText().toString())) {
                    comments = "None";
                } else {
                    comments = etComments.getText().toString();
                }
                HashMap hashMap = new HashMap();
                hashMap.put("FullName", name);
                hashMap.put("Address", address);
                hashMap.put("Email", email);
                hashMap.put("Mobile", phone);
                hashMap.put("Qualification", qualification);
                hashMap.put("CourseName", courseName);
                hashMap.put("ApplicationId", randomId);
                hashMap.put("Language", language);
                hashMap.put("Comments", comments);

                applicationsRef.child(randomId).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ApplyActivity.this, "Registered Successfully", Toast.LENGTH_LONG).show();
                            progressdialog.dismiss();

                            Intent intent = new Intent(ApplyActivity.this, BottomBarActivity.class);
                            startActivity(intent);

                        } else {
                            dialog.dismiss();
                        }
                    }
                });

            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();


    }
}
