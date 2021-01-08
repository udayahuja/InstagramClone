package com.example.instagram;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity {

    private FrameLayout mFrameLayout;
    static  final String BASE_URL= "http://192.168.1.6:8080/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFrameLayout = findViewById(R.id.RegesterFrameLayout);
        setFragment(new SignInFragment());
    }
    private void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(mFrameLayout.getId(), fragment).commit();
    }
}

/*
*   Todo: Make sure to write a constraint to make username only in small case
*
*   Todo: after you are done instead of storing data in a static variable store it in shared prefrence or it will be a chaios for you
*
*
* */