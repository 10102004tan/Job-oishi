package com.example.joboishi.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.joboishi.Models.JobSearch;
import com.example.joboishi.databinding.ItemLoadingBinding;
import com.example.joboishi.databinding.JobItemHolderBinding;

import java.util.ArrayList;

public class JobSearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // View types
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_LOADING = 2;

    private ArrayList<JobSearch> jobs;
    private Context context;
    private boolean isLoadingAdd;

    public void setData(ArrayList<JobSearch> jobs) {
        this.jobs = jobs;
        notifyDataSetChanged();
    }

    public JobSearchAdapter(ArrayList<JobSearch> jobs, Context context) {
        this.jobs = jobs;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        if (jobs != null && position == jobs.size() - 1 && isLoadingAdd) {
            return TYPE_LOADING;
        }
        return TYPE_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            return new JobViewHolder(JobItemHolderBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        } else {
            return new LoadingViewHolder(ItemLoadingBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof JobViewHolder) {
            JobViewHolder jobViewHolder = (JobViewHolder) holder;
            JobSearch job = jobs.get(position);
            jobViewHolder.binding.companyNameTxt.setText(job.getCompany_name());
            jobViewHolder.binding.jobTitleTxt.setText(job.getTitle());
            jobViewHolder.binding.sortAddressesTxt.setText(job.getSort_addresses());
            jobViewHolder.binding.published.setText(job.getPublished());
            jobViewHolder.binding.bookmarkImage.setVisibility(View.GONE);
            Glide.with(context).load(job.getCompany_logo()).into(jobViewHolder.binding.companyLogo);
        } else if (holder instanceof LoadingViewHolder) {

        }
    }

    @Override
    public int getItemCount() {
        if (jobs != null) {
            return jobs.size();
        }
        return 0;
    }

    public class JobViewHolder extends RecyclerView.ViewHolder {
        private JobItemHolderBinding binding;

        public JobViewHolder(@NonNull JobItemHolderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder {

        private ItemLoadingBinding binding;

        public LoadingViewHolder(@NonNull ItemLoadingBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public void addFooterLoading() {
        isLoadingAdd = true;
        jobs.add(new JobSearch());
    }

    public void removeFooterLoading() {
        if (isLoadingAdd && jobs.size() > 0) {
            isLoadingAdd = false;
            int pos = jobs.size() - 1;
            jobs.remove(pos);
            notifyItemRemoved(pos);
        }
    }

}
