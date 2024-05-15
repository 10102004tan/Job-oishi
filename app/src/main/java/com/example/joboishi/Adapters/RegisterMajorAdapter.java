package com.example.joboishi.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.joboishi.Models.JobSearch;
import com.example.joboishi.databinding.MajorsItemBinding;

import java.util.ArrayList;

public class RegisterMajorAdapter extends RecyclerView.Adapter<RegisterMajorAdapter.MyViewHolder> {
    private Activity context;
    private ArrayList<JobSearch> majors = new ArrayList<>();

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public RegisterMajorAdapter(Activity context, ArrayList<JobSearch> majors) {
        this.context = context;
        this.majors = majors;
    }

    public void updateData(ArrayList<JobSearch> newMajors) {
        this.majors = newMajors;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MajorsItemBinding binding = MajorsItemBinding.inflate(LayoutInflater.from(context), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        JobSearch major = majors.get(position);

        holder.majorsItemBinding.job.setText(major.getTitle());
        holder.majorsItemBinding.company.setText(major.getCompany_name());
        holder.majorsItemBinding.customCheckbox.setChecked(major.getChecked());
    }

    @Override
    public int getItemCount() {
        return majors.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private MajorsItemBinding majorsItemBinding;

        public MyViewHolder(@NonNull MajorsItemBinding binding) {
            super(binding.getRoot());
            majorsItemBinding = binding;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            itemClickListener.onItemClick(MyViewHolder.this, position);
                        }
                    }
                }
            });
        }
    }

    public interface ItemClickListener {
        void onItemClick(MyViewHolder holder, int position);
    }
}
