package com.example.karma;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;

import com.example.karma.fragments.RefreshableFragment;
import com.example.karma.fragments.ShopFragment;
import com.example.karma.fragments.TopFragment;
import com.example.karma.fragments.UserProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.List;

public class BottomBarActivity extends AppCompatActivity {

    private Fragment top;
    private Fragment shop;
    private Fragment profile;
  //  private Fragment myinfo;
    //private Fragment notification;

    private BottomNavigationView navigation;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_bar);
        //BottomNavigationView navView = findViewById(R.id.nav_view);
        Utils.setTopBar(getWindow(),getResources());
        // Pass


        profile = new UserProfileFragment();
        top = new TopFragment();
        shop = new ShopFragment();
      //  myinfo = new MyInfoFragment();
       // notification = new NotificationsFragment();
        navigation = findViewById(R.id.nav_view);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }
    @Override
    protected void onResume() {
        super.onResume();
        navigation.getMenu().findItem(Utils.CURRENT_NAVIGATION_BAR).setChecked(true);
        selectFragment();

    }
    private void selectFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (Fragment fragment : fragments) {
            transaction.hide(fragment);
        }
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(Utils.CURRENT_NAVIGATION_BAR + "");
        if (fragment != null) {
            transaction.show(fragment);
        }

        if (R.id.navigation_home == Utils.CURRENT_NAVIGATION_BAR) {
            if (fragment == null) {
                transaction.add(R.id.nav_host_fragment, top, Utils.CURRENT_NAVIGATION_BAR + "").show(top);
            }
        } else if (R.id.navigation_dashboard == Utils.CURRENT_NAVIGATION_BAR) {
            if (fragment == null) {
                transaction.add(R.id.nav_host_fragment, shop, Utils.CURRENT_NAVIGATION_BAR + "").show(shop);
            }
        } else if (R.id.navigation_notifications == Utils.CURRENT_NAVIGATION_BAR) {
            if (fragment == null) {
                transaction.add(R.id.nav_host_fragment, profile, Utils.CURRENT_NAVIGATION_BAR + "").show(profile);
            }
        }/* else if (R.id.navigation_notification == CrasherAppUtil.CURRENT_NAVIGATION_BAR) {
            if (fragment == null) {
                transaction.add(R.id.content, notification, CrasherAppUtil.CURRENT_NAVIGATION_BAR + "").show(notification);
            }
        }
         */
        else {
            if (fragment == null) {
                transaction.add(R.id.nav_host_fragment, top, Utils.CURRENT_NAVIGATION_BAR + "").show(top);
            }
        }

        transaction.setCustomAnimations(android.R.animator.fade_in,
                android.R.animator.fade_out);
        transaction.commitAllowingStateLoss();

    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        if (item.isChecked()) {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(Utils.CURRENT_NAVIGATION_BAR + "");
            if (fragment instanceof RefreshableFragment) {
                ((RefreshableFragment) fragment).refresh();
                return true;
            }
        }
        Utils.CURRENT_NAVIGATION_BAR = item.getItemId();
        item.setChecked(true);
        selectFragment();
        return true;
    };

}
