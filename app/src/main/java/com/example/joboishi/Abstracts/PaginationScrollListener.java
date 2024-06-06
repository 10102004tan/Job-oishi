package com.example.joboishi.Abstracts;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract class PaginationScrollListener extends RecyclerView.OnScrollListener {
    private LinearLayoutManager linearLayoutManager;

    public PaginationScrollListener(LinearLayoutManager linearLayoutManager) {
        this.linearLayoutManager = linearLayoutManager;
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        // Số lượng nhìn thấy trên 1 layout
        int visibleItemCount = linearLayoutManager.getChildCount();
        // Số lượng hiển thị trên 1 page
        int totalItemCount = linearLayoutManager.getItemCount();
        int firstVisibleItemPos = linearLayoutManager.findFirstVisibleItemPosition();
//        Log.d("onScrolled", visibleItemCount + " " + totalItemCount + " " + firstVisibleItemPos);

        if (isLoading() || isLastPage()) {
            return;
        }

        if (firstVisibleItemPos >= 0 && (firstVisibleItemPos + visibleItemCount) >= totalItemCount) {
            isLoadItem();
        }
    }

    // Khởi tạo abstract
    public abstract void isLoadItem();
    public abstract boolean isLoading();
    public abstract boolean isLastPage();
}
