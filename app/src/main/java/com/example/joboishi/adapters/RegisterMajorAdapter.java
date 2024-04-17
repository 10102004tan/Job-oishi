package com.example.joboishi.adapters;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.joboishi.databinding.MajorsItemBinding;
import com.example.joboishi.models.RegisterMajors;

import java.util.ArrayList;

public class RegisterMajorAdapter extends RecyclerView.Adapter<RegisterMajorAdapter.MyViewHolder> {
    private Activity context;
    private ArrayList<RegisterMajors> majors = new ArrayList<>();

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public RegisterMajorAdapter(Activity context, ArrayList<RegisterMajors> majors) {
        this.context = context;
        this.majors = majors;

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
        RegisterMajors major = majors.get(position);

        holder.majorsItemBinding.job.setText(major.getName_job());
        holder.majorsItemBinding.company.setText(major.getName_cpn());
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
        public void onItemClick(MyViewHolder holder, int position);

    }

}
