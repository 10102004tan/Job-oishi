package com.example.joboishi.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.joboishi.databinding.ChosenItemBinding;
import com.example.joboishi.models.RegisterMajors;

import java.util.ArrayList;

public class MajorChosenAdapter extends RecyclerView.Adapter<MajorChosenAdapter.MyViewHolder> {

    Activity context;
    private ArrayList<RegisterMajors> registerMajorsChosen;
    private ArrayList<RegisterMajors> majors;

    private ItemClickListener itemClickListener;

    public MajorChosenAdapter(Activity context, ArrayList<RegisterMajors> registerMajorsChosen) {
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

        ChosenItemBinding binding = ChosenItemBinding.inflate(LayoutInflater.from(context), parent, false);
        return new MyViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(@NonNull MajorChosenAdapter.MyViewHolder holder, int position) {
        RegisterMajors major = registerMajorsChosen.get(position);

        holder.majorsChosenItemBinding.jobChosen.setText(major.getName_job());
    }

    @Override
    public int getItemCount() {
        return registerMajorsChosen.size();
    }

    // Thêm một phương thức để xóa một major khỏi danh sách registerMajorsChosen
    public void removeMajor(RegisterMajors major) {

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ChosenItemBinding majorsChosenItemBinding;
        public MyViewHolder(@NonNull ChosenItemBinding binding) {
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
