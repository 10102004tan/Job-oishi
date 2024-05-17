package com.example.joboishi.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.joboishi.Models.JobSearch;
import com.example.joboishi.databinding.ChosenItemBinding;
import com.example.joboishi.Models.RegisterMajors;
import com.example.joboishi.databinding.JobSelectItemLayoutBinding;

import java.util.ArrayList;

public class MajorChosenAdapter extends RecyclerView.Adapter<MajorChosenAdapter.MyViewHolder> {

    Activity context;
    private final ArrayList<JobSearch> registerMajorsChosen;
    private ArrayList<JobSearch> majors;

    private ItemClickListener itemClickListener;

    public MajorChosenAdapter(Activity context, ArrayList<JobSearch> registerMajorsChosen) {
        this.context = context;
        if (registerMajorsChosen == null) {
            this.registerMajorsChosen = new ArrayList<>();
        } else {
            this.registerMajorsChosen = registerMajorsChosen;
        }
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public MajorChosenAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        JobSelectItemLayoutBinding binding = JobSelectItemLayoutBinding.inflate(LayoutInflater.from(context), parent, false);
        return new MyViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(@NonNull MajorChosenAdapter.MyViewHolder holder, int position) {
        JobSearch major = registerMajorsChosen.get(position);

        holder.majorsChosenItemBinding.jobTitle.setText(major.getTitle());
    }

    @Override
    public int getItemCount() {
        return registerMajorsChosen.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final JobSelectItemLayoutBinding majorsChosenItemBinding;
        public MyViewHolder(@NonNull JobSelectItemLayoutBinding binding) {
            super(binding.getRoot());
            majorsChosenItemBinding = binding;
            // Xử lý sự kiện click
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        itemClickListener.onItemClick(MyViewHolder.this, position);
                    }
                }
            });
        }
    }

    public interface ItemClickListener {
        public void onItemClick(MyViewHolder holder, int position);

    }

}
