package com.demo1.smsapp.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import com.demo1.smsapp.fragment.HomeFragment;
import com.demo1.smsapp.fragment.ProfileFragment;
import org.jetbrains.annotations.NotNull;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {


    public ViewPagerAdapter(@NonNull @NotNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @NotNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new HomeFragment();
            case 1:
                return new ProfileFragment();
            default:
                return new HomeFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
