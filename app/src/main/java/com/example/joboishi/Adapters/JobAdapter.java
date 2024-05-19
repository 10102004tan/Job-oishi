package com.example.joboishi.Adapters;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.joboishi.Models.JobBasic;
import com.example.joboishi.Models.data.Job;
import com.example.joboishi.databinding.JobItemHolderBinding;
import java.util.ArrayList;
public class JobAdapter extends RecyclerView.Adapter<JobAdapter.JobViewHolder>{
    private ArrayList<JobBasic> jobs;
    private Context context;
    private IClickJob iClickJob;
    private boolean isBookmark = false;
    private OnLoadMoreListener onLoadMoreListener;
    private boolean isLoading = false;
    private ArrayList<Integer> arrId;
    private boolean isVisibleBookmark = true;
    private boolean isEnableBookmark = true;


    public void setBookmark(boolean bookmark) {
        isBookmark = bookmark;
    }

    public void setiClickJob(IClickJob iClickJob) {
        this.iClickJob = iClickJob;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    public void setArrId(ArrayList<Integer> arrId) {
        this.arrId = arrId;
    }

    public void setVisibleBookmark(boolean visibleBookmark) {
        isVisibleBookmark = visibleBookmark;
    }

    public void setEnableBookmark(boolean enableBookmark) {
        isEnableBookmark = enableBookmark;
    }

    public JobAdapter(ArrayList<JobBasic> jobs, Context context) {
        this.jobs = jobs;
        this.context = context;
    }

    @NonNull
    @Override
    public JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new JobViewHolder(JobItemHolderBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull JobViewHolder holder, int position) {
        JobBasic job = jobs.get(position);
        if (position == jobs.size() - 1 && !isLoading){
            isLoading = true;
            if (onLoadMoreListener != null){
                onLoadMoreListener.onLoadMore();
            }
        }
        holder.job = job;
        holder.binding.companyNameTxt.setText(job.getCompany_name());
        holder.binding.jobTitleTxt.setText(job.getTitle());
        holder.binding.sortAddressesTxt.setText(job.getSort_addresses());
        holder.binding.published.setText(job.getPublished());
        holder.binding.salaryTxt.setVisibility((job.isIs_salary_visible()) ? View.VISIBLE : View.GONE);
        holder.binding.bookmarkImage.setVisibility((isVisibleBookmark) ? View.VISIBLE : View.GONE);
        holder.binding.bookmarkImage.setEnabled(isEnableBookmark);
        Glide.with(context).load(job.getCompany_logo()).into(holder.binding.companyLogo);
        holder.bindData(job, position);
        boolean isBookmarked = arrId != null && arrId.contains(job.getId()) || job.isBookmarked();
        holder.binding.bookmarkImage.setSelected(isBookmarked);
    }
    @Override
    public int getItemCount() {
        return jobs.size();
    }
    class JobViewHolder extends RecyclerView.ViewHolder {
        private JobItemHolderBinding binding;
        private JobBasic job;
        public void bindData(JobBasic job, int position) {
            binding.bookmarkImage.setSelected(job.isBookmarked());
            binding.bookmarkImage.setOnClickListener(v -> {
                if (iClickJob != null) {
                    if (binding.bookmarkImage.isSelected()) {
                        iClickJob.onRemoveBookmark(job, binding.bookmarkImage,position);
                    } else {
                        iClickJob.onAddJobBookmark(job, binding.bookmarkImage,position);
                    }
                }
            });
        }
        public JobViewHolder(@NonNull JobItemHolderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (iClickJob != null){
                        iClickJob.onClickJob(job.getId());
                    }
                }
            });
        }
    }

    public void updateData(ArrayList<JobBasic> jobs){
        this.jobs.clear();
        this.jobs.addAll(jobs);
        notifyDataSetChanged();
    }

    public interface IClickJob{
        void onClickJob(int id);
        void onAddJobBookmark(JobBasic job, ImageView bookmarkImage, int position);
        void onRemoveBookmark(JobBasic jobBasic, ImageView bookmarkImage, int position);
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }


    public interface OnLoadMoreListener {
        void onLoadMore();
    }

}