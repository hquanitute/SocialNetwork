package com.example.socialnetwork;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.socialnetwork.Adapter.MainView;
import com.example.socialnetwork.Fragment.Fragment_Chating;
import com.example.socialnetwork.Fragment.Fragment_Friends;
import com.example.socialnetwork.Fragment.Fragment_NewFeed;

public class Index extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        Bundle extras = this.getIntent().getExtras();

        String displayname = extras.getString("displayname");
        String email  = extras.getString("email");
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
