package com.example.joboishi.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ScrollRecyclerviewListener extends ViewModel {
    private MutableLiveData<Integer> currentTabPosition = new MutableLiveData<>(0);

    public LiveData<Integer> getCurrentTabPosition() {
        return currentTabPosition;
    }

    public void setCurrentTabPosition(int position) {
        currentTabPosition.setValue(position);
    }
}
