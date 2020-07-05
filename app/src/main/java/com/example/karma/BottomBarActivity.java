package com.example.karma;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.example.karma.fragments.CartFragment;
import com.example.karma.fragments.NotificationFragment;
import com.example.karma.fragments.RefreshableFragment;
import com.example.karma.fragments.ShopFragment;
import com.example.karma.fragments.TopFragment;
import com.example.karma.fragments.UserProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.List;

public class BottomBarActivity extends AppCompatActivity {

    private Fragment top;
    private Fragment shop;
    private Fragment profile;
    private Fragment cart;
    private Fragment notifications;
  //  private Fragment myinfo;
    //private Fragment notification;

    private BottomNavigationView navigation;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_bar);
        Utils.setTopBar(getWindow(),getResources());
        //BottomNavigationView navView = findViewById(R.id.nav_view);

        // Pass


        profile = new UserProfileFragment();
        top = new TopFragment();
        shop = new ShopFragment();
        cart=new CartFragment();
        notifications=new NotificationFragment();
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
        } else if (R.id.navigation_profile == Utils.CURRENT_NAVIGATION_BAR) {
            if (fragment == null) {
                transaction.add(R.id.nav_host_fragment, profile, Utils.CURRENT_NAVIGATION_BAR + "").show(profile);
            }
        }
     else if (R.id.navigation_cart == Utils.CURRENT_NAVIGATION_BAR) {
        if (fragment == null) {
            transaction.add(R.id.nav_host_fragment, cart, Utils.CURRENT_NAVIGATION_BAR + "").show(cart);
        }
    } else if (R.id.navigation_notification == Utils.CURRENT_NAVIGATION_BAR) {
        if (fragment == null) {
            transaction.add(R.id.nav_host_fragment, notifications, Utils.CURRENT_NAVIGATION_BAR + "").show(notifications);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory( Intent.CATEGORY_HOME );
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
        System.exit(1);
    }
}
