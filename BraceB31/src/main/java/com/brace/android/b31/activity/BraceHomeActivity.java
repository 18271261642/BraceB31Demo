package com.brace.android.b31.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.MenuItem;

import com.brace.android.b31.R;
import com.brace.android.b31.activity.adapter.FragmentAdapter;
import com.brace.android.b31.activity.ui.dashboard.DashboardFragment;
import com.brace.android.b31.activity.ui.home.HomeFragment;

import java.util.ArrayList;
import java.util.List;


public class BraceHomeActivity extends BaseActivity implements ViewPager.OnPageChangeListener, BottomNavigationView.OnNavigationItemSelectedListener
{

    BottomNavigationView navView;
    ViewPager homeViewPager;
    private List<Fragment> fragmentList;
    private FragmentAdapter fragmentAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brace_home);

        initViews();

        //BleConnDataOperate.getBleConnDataOperate().getDeviceBasicData();

    }

    private void initViews() {
        navView = findViewById(R.id.nav_view);
        homeViewPager = findViewById(R.id.homeViewPager);
        fragmentList = new ArrayList<>();
        fragmentList.add(new HomeFragment());
        fragmentList.add(new DashboardFragment());
        fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(),fragmentList);
        homeViewPager.setAdapter(fragmentAdapter);
        homeViewPager.setCurrentItem(0);
        homeViewPager.addOnPageChangeListener(this);
        navView.setOnNavigationItemSelectedListener(this);


    }


    /**
     * viewPager的回调
     */
    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        navView.getMenu().getItem(i).setChecked(true);
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == R.id.navigation_home) {
            homeViewPager.setCurrentItem(0);
        } else if (itemId == R.id.navigation_dashboard) {
            homeViewPager.setCurrentItem(1);
        }
        return false;
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 过滤按键动作
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);

        } else if (keyCode == KeyEvent.KEYCODE_MENU) {
            moveTaskToBack(true);
        } else if (keyCode == KeyEvent.KEYCODE_HOME) {
            moveTaskToBack(true);
        }
        return super.onKeyDown(keyCode, event);
    }
}
