package com.example.rohanspc.healthcare;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> list_fragment = new ArrayList<>();
    private final List<String> list_Titles = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return list_fragment.get(position);
    }

    @Override
    public int getCount() {
        return list_Titles.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return list_Titles.get(position);
    }

    public void AddFragments(Fragment fragment,String title){
        list_fragment.add(fragment);
        list_Titles.add(title);
    }
}
