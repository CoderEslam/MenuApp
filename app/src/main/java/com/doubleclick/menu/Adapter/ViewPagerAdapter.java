package com.doubleclick.menu.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.doubleclick.menu.Fragment.NormalMenuFragment;
import com.doubleclick.menu.Fragment.VIPMenuFragment;

/**
 * Created By Eslam Ghazy on 10/19/2022
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private int nNumOfTabs;

    public ViewPagerAdapter(@NonNull FragmentManager fm, int n) {
        super(fm);
        nNumOfTabs = n;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                NormalMenuFragment normalMenuFragment = new NormalMenuFragment();
                return normalMenuFragment;
            case 1:
                VIPMenuFragment vipMenuFragment = new VIPMenuFragment();
                return vipMenuFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return nNumOfTabs;
    }
}
