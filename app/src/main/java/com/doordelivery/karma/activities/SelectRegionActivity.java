package com.doordelivery.karma.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.doordelivery.karma.R;

public class SelectRegionActivity extends AppCompatActivity {

    String origin;
    ListView rvRegion;
    ArrayAdapter aAdapter;

    TextView tvSelectOrigin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_region);

        tvSelectOrigin = findViewById(R.id.tv_select_origin);

        rvRegion = findViewById(R.id.rv_regions);
        String[] regs = getResources().getStringArray(R.array.regions);

        aAdapter = new ArrayAdapter(this, R.layout.region_selector_layout, R.id.tv_region_name, regs);
        rvRegion.setAdapter(aAdapter);

        rvRegion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = view.findViewById(R.id.tv_region_name);
                String string = textView.getText().toString();
                Toast.makeText(SelectRegionActivity.this, "Selected "+string, Toast.LENGTH_SHORT).show();
                SharedPreferences.Editor editor=getSharedPreferences("REGION_SELECTOR",MODE_PRIVATE).edit();
                editor.putString("REGION",string);
                editor.putBoolean("IS_SELECTED",true);
                editor.apply();
                Intent intent=new Intent(SelectRegionActivity.this,BottomBarActivity.class);
                startActivity(intent);


            }
        });





    }
}
