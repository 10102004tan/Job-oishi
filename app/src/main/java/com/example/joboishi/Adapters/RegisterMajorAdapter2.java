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

public class RegisterMajorAdapter2 extends RecyclerView.Adapter<RegisterMajorAdapter2.MyViewHolder> {
    private Activity context;
    private ArrayList<String> majors;

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public RegisterMajorAdapter2(Activity context, ArrayList<String> majors) {
        this.context = context;
        this.majors = majors;
    }

    public void updateData(ArrayList<String> newMajors) {
        majors = newMajors;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Sử dụng MajorsItemBinding để inflate layout và tạo instance cho ViewHolder
        MajorsItemBinding binding = MajorsItemBinding.inflate(LayoutInflater.from(context), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String keyword = majors.get(position);
        holder.majorsItemBinding.job.setText(keyword);
        holder.majorsItemBinding.company.setText("keyword");
//        holder.majorsItemBinding.customCheckbox.setChecked(major.getChecked());
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
            // Gán sự kiện click cho CardView
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    itemClickListener.onItemClick(MyViewHolder.this, position);
                }
            });
        }
    }

    public interface ItemClickListener {
        void onItemClick(MyViewHolder holder, int position);
    }
}
