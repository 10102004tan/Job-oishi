package com.example.joboishi.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.joboishi.databinding.ChosenItemBinding;
import com.example.joboishi.models.CityMajors;
import com.example.joboishi.models.RegisterMajors;

import java.util.ArrayList;

public class CityChosenAdpater extends RecyclerView.Adapter<CityChosenAdpater.MyViewHolder> {

    Activity context;
    private ArrayList<CityMajors> citiesChosenList = new ArrayList<>();

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public CityChosenAdpater(Activity context, ArrayList<CityMajors> citiesChosenList) {
        this.context = context;
        this.citiesChosenList = citiesChosenList;
    }

    @NonNull
    @Override
    public CityChosenAdpater.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ChosenItemBinding binding = ChosenItemBinding.inflate(LayoutInflater.from(context), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CityChosenAdpater.MyViewHolder holder, int position) {
        CityMajors city = citiesChosenList.get(position);

        holder.cityChosenItemBinding.jobChosen.setText(city.getName_city());
    }

    @Override
    public int getItemCount() {
        return citiesChosenList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ChosenItemBinding cityChosenItemBinding;
        public MyViewHolder(@NonNull ChosenItemBinding binding) {
            super(binding.getRoot());
            cityChosenItemBinding = binding;

            // Xử lý sự kiện click
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Toast.makeText(context, position + "", Toast.LENGTH_SHORT).show();
                    if (position != RecyclerView.NO_POSITION) {

                    }
                }
            });
        }
    }

    // Dinh nghia Interface
    public interface ItemClickListener {
        public void onItemClick(RegisterMajorAdapter.MyViewHolder holder, int position);

    }
}
