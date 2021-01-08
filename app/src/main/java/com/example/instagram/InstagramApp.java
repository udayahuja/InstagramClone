package com.example.instagram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

public class InstagramApp extends AppCompatActivity {

    static boolean goToHomePage = true;

    private static final int NUM_PAGES = 4;
    private ViewPager2 viewPager;

    private static String TAG = "error";

    private FragmentStateAdapter pagerAdapter;


    private FrameLayout mFrameLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instagram_app);

        UserData user = SignInFragment.user;
        Log.d(TAG, "onCreate Of InstagramApp: printing userdetails");
        if(user!=null)
        Log.d(TAG, user.getId()+" "+user.getName()+" "+user.getEmail()+" "+user.getPhoneNo()+" "+user.getUsername()+" "+user.getPassword());

        mFrameLayout = findViewById(R.id.instagram_app_layout);
        /*if(goToHomePage){
            setFragment(new HomePageFragment());
        }else{

        }*/
        viewPager = findViewById(R.id.home_view_pager);
        pagerAdapter = new ScreenSliderPagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);
        if(goToHomePage){
            viewPager.setCurrentItem(1);
        }else{
            viewPager.setCurrentItem(2);
        }

    }


    private class ScreenSliderPagerAdapter extends FragmentStateAdapter{

        public ScreenSliderPagerAdapter(FragmentActivity fa) {
            super(fa);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            Fragment fragment = null;
            if(position == 0){
                fragment = new StoryCameraFragment();
            }else if(position == 1){
                fragment = new HomePageFragment();
            }else if(position == 2){
                fragment = new ProfileFragment();
            }else{
                fragment = new PostImageFragment();
            }
            return fragment;
        }

        @Override
        public int getItemCount() {
            return NUM_PAGES;
        }
    }
    private void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(mFrameLayout.getId(), fragment).commit();
    }
}