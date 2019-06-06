package com.example.socialnetwork.Adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class MainView extends FragmentPagerAdapter {
  private ArrayList<Fragment> arrayFragment = new ArrayList<>();
  private ArrayList<String> arrayTittle = new ArrayList<>();
  public MainView(FragmentManager fm) {
    super(fm);
  }

  @Override
  public Fragment getItem(int position) {
    return arrayFragment.get(position);
  }

  @Override
  public int getCount() {
    return arrayFragment.size();
  }
  public void addFragment(Fragment fragment, String tittle )
  {
    arrayFragment.add(fragment);
    arrayTittle.add(tittle);
  }

  @Nullable
  @Override
  public CharSequence getPageTitle(int position) {
    return arrayTittle.get(position);
  }
}