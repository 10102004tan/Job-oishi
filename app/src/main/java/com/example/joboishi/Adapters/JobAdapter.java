package com.example.joboishi.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.joboishi.Models.Job;
import com.example.joboishi.databinding.JobItemHolderBinding;
import java.util.ArrayList;
public class JobAdapter extends RecyclerView.Adapter<JobAdapter.JobViewHolder>{
    private ArrayList<Job> jobs;
    private Context context;

    public JobAdapter(ArrayList<Job> jobs,Context context) {
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
        Job job = jobs.get(position);
        holder.binding.companyNameTxt.setText(job.getCompany_name());
        holder.binding.jobTitleTxt.setText(job.getTitle());
        holder.binding.sortAddressesTxt.setText(job.getSort_addresses());
        Glide.with(context).load(job.getCompany_logo()).into(holder.binding.companyLogo);
    }

    @Override
    public int getItemCount() {
        return jobs.size();
    }
    class JobViewHolder extends RecyclerView.ViewHolder {
        private JobItemHolderBinding binding;
        public JobViewHolder(@NonNull JobItemHolderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}