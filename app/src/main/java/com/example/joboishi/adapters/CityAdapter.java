package com.example.joboishi.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.joboishi.databinding.CityItemBinding;

import java.util.ArrayList;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.MyViewHolder> {
    private Activity context;
    private ArrayList<String> cities = new ArrayList<>();
    private ArrayList<String> majorsChosen;

    public CityAdapter(Activity context, ArrayList<String> cities) {
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
        String city = cities.get(position);

        holder.cityItemBinding.city.setText(city);
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
                    Toast.makeText(context, "click", Toast.LENGTH_SHORT).show();
                }

            });
        }

    }

}
