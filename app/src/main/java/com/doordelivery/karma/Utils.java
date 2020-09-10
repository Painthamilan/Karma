package com.doordelivery.karma;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;

import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Utils {


    public static int CURRENT_NAVIGATION_BAR =R.id.navigation_home ;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void setTopBar(Window window, Resources resources) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            Drawable background = resources.getDrawable(R.drawable.main_theme);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(resources.getColor(android.R.color.transparent));
            window.setNavigationBarColor(resources.getColor(android.R.color.transparent));
            window.setBackgroundDrawable(background);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }
    public static boolean isAdmin(String userId){
        return Constants.ADMIN_ID.equals(userId);
    }
    public static String getUserid(){
        FirebaseAuth cfAuth=FirebaseAuth.getInstance();
        return cfAuth.getCurrentUser().getUid();
    }
    public static String createRandomId(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        String curDate = dateFormat.format(new Date());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH-mm-ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        String curTime = timeFormat.format(new Date());
        String randomId = curDate + curTime;
        return randomId;

    }
    public static String getActualPrice(String price,String percentage){
        if (!percentage.isEmpty()) {
            int percent = Integer.parseInt(percentage);
            int pr = Integer.parseInt(price);
            int newPrice = pr - (pr * percent / 100);
            return String.valueOf(newPrice);
        }else {
            return price;
        }

    }
}
