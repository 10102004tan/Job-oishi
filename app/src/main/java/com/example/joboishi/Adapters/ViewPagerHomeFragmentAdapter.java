package com.example.joboishi.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.joboishi.Fragments.HomeFragment;
import com.example.joboishi.Fragments.HomeMainFragment;
import com.example.joboishi.Fragments.HomeTopDevFragment;
import com.example.joboishi.Fragments.MyJobFragment;
import com.example.joboishi.Fragments.ProfileFragment;

public class ViewPagerHomeFragmentAdapter extends FragmentStateAdapter {
    public ViewPagerHomeFragmentAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public ViewPagerHomeFragmentAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    public ViewPagerHomeFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0 : return new HomeMainFragment();
            case 1 : return new HomeTopDevFragment();
            default: return null;
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
