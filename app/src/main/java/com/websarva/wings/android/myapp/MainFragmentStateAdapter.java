package com.websarva.wings.android.myapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class MainFragmentStateAdapter extends FragmentStateAdapter {
    public MainFragmentStateAdapter(Fragment fragment){
        super(fragment);
    }

    @Override
    @NonNull
    public Fragment createFragment(int position){
        if (position == 1)
            return new TimetableFragment();
        else
            return new TasksFragment();
    }

    @Override
    public int getItemCount(){
        return 2;
    }
}
