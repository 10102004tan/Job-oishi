package com.example.joboishi.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.joboishi.databinding.JobSelectItemLayoutBinding;

import java.util.ArrayList;

public class CityChosenAdpater extends RecyclerView.Adapter<CityChosenAdpater.MyViewHolder> {

    Activity context;
    private ArrayList<String> citiesChosenList = new ArrayList<>();

    private ItemClickListener itemClickListener;

    public CityChosenAdpater(Activity context, ArrayList<String> citiesChosenList) {
        this.context = context;
        this.citiesChosenList = citiesChosenList;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public CityChosenAdpater.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        JobSelectItemLayoutBinding binding = JobSelectItemLayoutBinding.inflate(LayoutInflater.from(context), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CityChosenAdpater.MyViewHolder holder, int position) {
        String city = citiesChosenList.get(position);

        holder.cityChosenItemBinding.jobTitle.setText(city);
    }

    @Override
    public int getItemCount() {
        return citiesChosenList.size();
    }

    // Dinh nghia Interface
    public interface ItemClickListener {
        void onItemClick(MyViewHolder holder, int position);

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final JobSelectItemLayoutBinding cityChosenItemBinding;

        public MyViewHolder(@NonNull JobSelectItemLayoutBinding binding) {
            super(binding.getRoot());
            cityChosenItemBinding = binding;

            // Xử lý sự kiện click
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    itemClickListener.onItemClick(MyViewHolder.this, position);
                }
            });
        }
    }
}
