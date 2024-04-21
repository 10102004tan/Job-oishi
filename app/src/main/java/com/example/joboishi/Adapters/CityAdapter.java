package com.example.joboishi.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.joboishi.databinding.CityItemBinding;
import com.example.joboishi.Models.CityMajors;

import java.util.ArrayList;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.MyViewHolder> {
    private Activity context;
    private ArrayList<CityMajors> cities = new ArrayList<>();

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public CityAdapter(Activity context, ArrayList<CityMajors> cities) {
        this.context = context;
        this.cities = cities;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Sử dụng MajorsItemBinding để inflate layout và tạo instance cho ViewHolder
        CityItemBinding binding = CityItemBinding.inflate(LayoutInflater.from(context), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        CityMajors city = cities.get(position);

        holder.cityItemBinding.city.setText(city.getName_city());
        holder.cityItemBinding.customCheckbox.setChecked(city.getChecked_city());

    }

    @Override
    public int getItemCount() {
        return cities.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private CityItemBinding cityItemBinding;

        public MyViewHolder(@NonNull CityItemBinding binding) {
            super(binding.getRoot());
            cityItemBinding = binding;

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
        public void onItemClick(CityAdapter.MyViewHolder holder, int position);

    }

}
