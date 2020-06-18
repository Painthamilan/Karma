package com.example.karma;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;

public class Utils {
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
        return userId.equals(Constants.ADMIN_ID);
    }
}
