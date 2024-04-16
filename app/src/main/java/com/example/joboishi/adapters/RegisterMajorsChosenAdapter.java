package com.example.joboishi.adapters;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.joboishi.databinding.MajorsChosenItemBinding;
import com.example.joboishi.databinding.MajorsItemBinding;
import com.example.joboishi.models.RegisterMajors;

import java.util.ArrayList;

public class RegisterMajorsChosenAdapter extends RecyclerView.Adapter<RegisterMajorsChosenAdapter.MyViewHolder> {

    Activity context;
    private ArrayList<RegisterMajors> registerMajorsChosen;
    private ArrayList<RegisterMajors> majors;

    public RegisterMajorsChosenAdapter(Activity context, ArrayList<RegisterMajors> registerMajorsChosen, ArrayList<RegisterMajors> majors) {
        this.context = context;
        if (registerMajorsChosen == null) {
            this.registerMajorsChosen = new ArrayList<>();
        } else {
            this.registerMajorsChosen = registerMajorsChosen;
        }
        if (majors == null) {
            this.majors = new ArrayList<>();
        } else {
            this.majors = majors;
        }

    }

    @NonNull
    @Override
    public RegisterMajorsChosenAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        MajorsChosenItemBinding binding = MajorsChosenItemBinding.inflate(LayoutInflater.from(context), parent, false);
        return new MyViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(@NonNull RegisterMajorsChosenAdapter.MyViewHolder holder, int position) {
        RegisterMajors major = registerMajorsChosen.get(position);

        holder.majorsChosenItemBinding.jobChosen.setText(major.getName_job());
    }

    @Override
    public int getItemCount() {
        return registerMajorsChosen.size();
    }

    // Thêm một phương thức để xóa một major khỏi danh sách registerMajorsChosen
    public void removeMajor(RegisterMajors major) {
        registerMajorsChosen.remove(major);
        // Cập nhật trạng thái của major trong danh sách majors ban đầu
        for (RegisterMajors originalMajor : majors) {
            if (originalMajor.equals(major)) {
                originalMajor.setChecked(false);
                break;
            }
        }
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private MajorsChosenItemBinding majorsChosenItemBinding;
        public MyViewHolder(@NonNull MajorsChosenItemBinding binding) {
            super(binding.getRoot());
            majorsChosenItemBinding = binding;
            // Xử lý sự kiện click
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Toast.makeText(context, position + "", Toast.LENGTH_SHORT).show();
                    if (position != RecyclerView.NO_POSITION) {
                        RegisterMajors clickedMajor = registerMajorsChosen.get(position);
                        // Xóa major khỏi danh sách registerMajorsChosen
                        removeMajor(clickedMajor);
                    }
                }
            });
        }
    }

}
