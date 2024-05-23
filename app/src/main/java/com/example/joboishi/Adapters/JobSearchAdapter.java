package com.example.joboishi.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.joboishi.Models.JobBasic;
import com.example.joboishi.databinding.ItemLoadingBinding;
import com.example.joboishi.databinding.JobItemHolderBinding;

import java.util.ArrayList;

public class JobSearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // View types
    private static final int TYPE_ITEM = 1;
    private static final int TYPE_LOADING = 2;

    private ArrayList<JobBasic> jobs;
    private Context context;
    private boolean isLoadingAdd;

    public void setData(ArrayList<JobBasic> jobs) {
        this.jobs = jobs;
        notifyDataSetChanged();
    }

    private iClickItem iClickViewDetail;

    public void setiClickViewDetail(iClickItem iClickViewDetail) {
        this.iClickViewDetail = iClickViewDetail;
    }

    public JobSearchAdapter(ArrayList<JobBasic> jobs, Context context) {
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
            JobBasic job = jobs.get(position);
            jobViewHolder.bind(job); // Bind the job object
        } else if (holder instanceof LoadingViewHolder) {
            // Handle the loading view holder binding if necessary
        }
    }

    @Override
    public int getItemCount() {
        return jobs.size();
    }

    public class JobViewHolder extends RecyclerView.ViewHolder {
        private JobItemHolderBinding binding;
        private JobBasic job;

        public JobViewHolder(@NonNull JobItemHolderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

//            this.binding.getRoot().setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (iClickViewDetail != null && job != null) {
//                        iClickViewDetail.clickViewDetail(job.getId());
//                    } else {
//                        Log.d("test", "Interface or job is null" + iClickViewDetail.toString() + " " + job.getId());
//                    }
//                }
//            });
        }

        public void bind(JobBasic job) {
            this.job = job;
            binding.companyNameTxt.setText(job.getCompany_name());
            binding.jobTitleTxt.setText(job.getTitle());
            binding.sortAddressesTxt.setText(job.getSort_addresses());
            binding.published.setText(job.getPublished());
            binding.salaryTxt.setVisibility((job.isIs_salary_visible()) ? View.VISIBLE : View.GONE);
            binding.bookmarkImage.setVisibility(View.GONE);
            Glide.with(context).load(job.getCompany_logo()).into(binding.companyLogo);
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
        jobs.add(null);
        notifyItemInserted(jobs.size() - 1);
    }

    public void removeFooterLoading() {
        if (isLoadingAdd && jobs.size() > 0) {
            isLoadingAdd = false;
            int pos = jobs.size() - 1;
            jobs.remove(pos);
            notifyItemRemoved(pos);
        }
    }

    public interface iClickItem {
        void clickViewDetail(int id);
    }
}
