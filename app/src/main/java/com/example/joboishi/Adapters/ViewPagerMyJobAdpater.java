package com.example.joboishi.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.joboishi.Fragments.AppliedJobFragment;
import com.example.joboishi.Fragments.SavedJobFragment;

public class ViewPagerMyJobAdpater extends FragmentStateAdapter {
    public ViewPagerMyJobAdpater(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public ViewPagerMyJobAdpater(@NonNull Fragment fragment) {
        super(fragment);
    }

    public ViewPagerMyJobAdpater(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0 : return new AppliedJobFragment();

            case 1 : return new SavedJobFragment();
        }
        return null;
    }
    @Override
    public int getItemCount() {
        return 2;
    }


}
