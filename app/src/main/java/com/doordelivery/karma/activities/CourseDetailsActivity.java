package com.doordelivery.karma.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.doordelivery.karma.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class CourseDetailsActivity extends AppCompatActivity {

    TextView tvCourseName, tvRequirements, tvDuration, tvFee, tvTeacher, tvDetails, tvApply;
    ImageView ivCourseImage;
    DatabaseReference courseRef;
    String courseId,courseName,requirements,duration,fee,teacher,details,courseImage;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);

        Utils.setTopBar(getWindow(),getResources());
        tvCourseName = findViewById(R.id.tv_course_name);
        tvRequirements = findViewById(R.id.tv_requirements);
        tvDuration = findViewById(R.id.tv_duration);
        tvFee = findViewById(R.id.tv_fee);
        tvTeacher = findViewById(R.id.tv_teacher);
        tvDetails = findViewById(R.id.tv_details);
        tvApply = findViewById(R.id.tv_apply);
        ivCourseImage = findViewById(R.id.iv_course_image);

        courseId=getIntent().getStringExtra("REF_KEY");
        courseRef= FirebaseDatabase.getInstance().getReference().child("Online Courses").child(courseId);




        courseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    courseName=snapshot.child("CourseName").getValue().toString();
                    requirements=snapshot.child("Requirements").getValue().toString();
                    duration=snapshot.child("Duration").getValue().toString();
                    fee=snapshot.child("CourseFee").getValue().toString();
                    teacher=snapshot.child("Teacher").getValue().toString();
                    details=snapshot.child("Details").getValue().toString();
                    courseImage=snapshot.child("CourseImage").getValue().toString();

                    Picasso.get().load(courseImage).into(ivCourseImage);
                    tvCourseName.setText(courseName);
                    tvRequirements.setText(": "+requirements);
                    tvDuration.setText(": "+duration);
                    tvFee.setText(": "+fee);
                    tvTeacher.setText(": "+teacher);
                    tvDetails.setText(": "+details);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        tvApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CourseDetailsActivity.this,ApplyActivity.class);
                intent.putExtra("CourseName",courseName);
                startActivity(intent);
            }
        });



    }
}
