package com.example.joboishi.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.joboishi.Models.JobBasic;
import com.example.joboishi.databinding.JobItemHolderBinding;
import java.util.ArrayList;
public class JobAdapter extends RecyclerView.Adapter<JobAdapter.JobViewHolder>{
    private ArrayList<JobBasic> jobs;
    private Context context;

    private IClickJob iClickJob;
    private boolean isBookmark = false;

    public void setBookmark(boolean bookmark) {
        isBookmark = bookmark;
    }

    public void setiClickJob(IClickJob iClickJob) {
        this.iClickJob = iClickJob;
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
        holder.job = job;
        holder.binding.companyNameTxt.setText(job.getCompany_name());
        holder.binding.jobTitleTxt.setText(job.getTitle());
        holder.binding.sortAddressesTxt.setText(job.getSort_addresses());
        holder.binding.published.setText(job.getPublished());
        holder.binding.salaryTxt.setVisibility((job.isIs_salary_visible()) ? View.VISIBLE : View.GONE);
        Glide.with(context).load(job.getCompany_logo()).into(holder.binding.companyLogo);
    }

    @Override
    public int getItemCount() {
        return jobs.size();
    }
    class JobViewHolder extends RecyclerView.ViewHolder {
        private JobItemHolderBinding binding;
        private JobBasic job;
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
            this.binding.bookmarkImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (iClickJob != null){
                        iClickJob.onClickBookmark(job);
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
        void onClickBookmark(JobBasic job);
    }
}