package com.example.joboishi.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.joboishi.Models.Keyword;
import com.example.joboishi.databinding.MajorsItemBinding;

import java.util.ArrayList;

public class RegisterMajorAdapter extends RecyclerView.Adapter<RegisterMajorAdapter.MyViewHolder> {
    private final Activity context;
    private ArrayList<Keyword> majors;

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public RegisterMajorAdapter(Activity context, ArrayList<Keyword> majors) {
        this.context = context;
        this.majors = majors != null ? majors : new ArrayList<Keyword>();
    }

    public void updateData(ArrayList<Keyword> newMajors) {
        if (newMajors != null) {
            majors = newMajors;
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MajorsItemBinding binding = MajorsItemBinding.inflate(LayoutInflater.from(context), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Keyword major = majors.get(position);

        holder.majorsItemBinding.job.setText(major.getName());
        holder.majorsItemBinding.company.setText(major.getKeyword());
        holder.majorsItemBinding.customCheckbox.setChecked(major.getChecked());
    }

    @Override
    public int getItemCount() {
        return majors.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final MajorsItemBinding majorsItemBinding;

        public MyViewHolder(@NonNull MajorsItemBinding binding) {
            super(binding.getRoot());
            this.majorsItemBinding = binding;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && itemClickListener != null) {
                        itemClickListener.onItemClick(MyViewHolder.this, position);
                    }
                }
            });
        }
    }

    public interface ItemClickListener {
        void onItemClick(MyViewHolder holder, int position);
    }
}
