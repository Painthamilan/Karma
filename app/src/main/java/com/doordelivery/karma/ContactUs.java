package com.doordelivery.karma;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ContactUs extends AppCompatActivity {

    TextView tvColombo,tvKandy,tvJaffna;
    String numColombo="0760454446",numKandy="0760454446",numJaffna="0762447455";
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setTopBar(getWindow(),getResources());
        setContentView(R.layout.activity_contact_us);
        tvColombo=findViewById(R.id.tv_colombo);
        tvKandy=findViewById(R.id.tv_contact_kandy);
        tvJaffna=findViewById(R.id.tv_contact_jaffna);

        tvColombo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intentCities(numColombo);
            }
        });

        tvKandy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intentCities(numKandy);
            }
        });
        tvJaffna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intentCities(numJaffna);
            }
        });

    }

    private void intentCities(String number) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", number, null));
        startActivity(intent);
    }
}
