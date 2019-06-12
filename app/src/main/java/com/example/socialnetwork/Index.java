package com.example.socialnetwork;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import com.example.socialnetwork.Adapter.MainView;
import com.example.socialnetwork.Fragment.Fragment_Chating;
import com.example.socialnetwork.Fragment.Fragment_Friends;
import com.example.socialnetwork.Fragment.Fragment_NewFeed;
import com.google.firebase.auth.FirebaseAuth;

public class Index extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;
    String email;
    String displayName;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        showFragment();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        email = bundle.getString("email");
        displayName= bundle.getString("displayname");

        Bundle bundleChinh= new Bundle();
        bundleChinh.putString("displayname", displayName);
        bundleChinh.putString("email", email);
        //Khoi tao 3 fragment
        MainView mainViewPagerApdater = new MainView(getSupportFragmentManager());
        Fragment_NewFeed fragment_newFeed= new Fragment_NewFeed();
        Fragment_Friends fragment_friends = new Fragment_Friends();
        Fragment_Chating fragment_chating = new Fragment_Chating();
        //set bundle
        fragment_newFeed.setArguments(bundleChinh);
        fragment_friends.setArguments(bundleChinh);
        fragment_chating.setArguments(bundleChinh);
        mainViewPagerApdater.addFragment(fragment_newFeed, "");
        mainViewPagerApdater.addFragment(fragment_friends, "");
        mainViewPagerApdater.addFragment(fragment_chating, "");
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mAuth.getInstance().signOut();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
        return true;
    }
}
