package com.example.socialnetwork;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import com.example.socialnetwork.Adapter.MainView;
import com.example.socialnetwork.Fragment.Fragment_Chating;
import com.example.socialnetwork.Fragment.Fragment_Friends;
import com.example.socialnetwork.Fragment.Fragment_NewFeed;
import com.example.socialnetwork.Objects.Post;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        showFragment();
        init();
    }

    private void init() {
        MainView mainViewPagerApdater = new MainView(getSupportFragmentManager());
        mainViewPagerApdater.addFragment(new Fragment_NewFeed(), "");
        mainViewPagerApdater.addFragment(new Fragment_Friends(), "");
        mainViewPagerApdater.addFragment(new Fragment_Chating(), "");
        viewPager.setAdapter(mainViewPagerApdater);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.newsfeed);
        tabLayout.getTabAt(1).setIcon(R.drawable.friends);
        tabLayout.getTabAt(2).setIcon(R.drawable.messenger);
    }

    private void showFragment() {
        tabLayout = findViewById(R.id.TabLayout);
        viewPager = findViewById(R.id.ViewPaper);
    }


}
