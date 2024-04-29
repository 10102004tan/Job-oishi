package com.example.joboishi.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;


import com.example.joboishi.Api.ProvinceApiResponse;
import com.example.joboishi.R;
import com.example.joboishi.databinding.ListCityRycBinding;

import java.util.ArrayList;


public class CityRecyclerViewAdapter extends RecyclerView.Adapter<CityRecyclerViewAdapter.MyHolder> {

    private final Activity context;
    private final ArrayList<ProvinceApiResponse> data;
    private final RecyclerView recyclerView;
    private final String selectedCity;
    private ItemClickListener itemClickListener;

    public CityRecyclerViewAdapter(Activity context, ArrayList<ProvinceApiResponse> data, RecyclerView recyclerView, String selectedCity) {
        this.context = context;
        this.data = data;
        this.recyclerView = recyclerView;
        this.selectedCity = selectedCity;
    }

    public ItemClickListener getItemClickListener() {
        return itemClickListener;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyHolder(ListCityRycBinding.inflate(context.getLayoutInflater(), parent, false));
    }

    @SuppressLint("ResourceAsColor")
    public void onBindViewHolder(@NonNull MyHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.setPos(position);

        TextView title = holder.getBinding().getRoot().findViewById(com.example.joboishi.R.id.city_name);
        ImageView image = holder.getBinding().getRoot().findViewById(R.id.btn_check);

        title.setText(data.get(position).getProvinceName());

        // Display check icon when user choose a country
        if (selectedCity.equals(data.get(position).getProvinceName())) {
            image.setImageResource(R.drawable.check_svgrepo_com);
            title.setTextColor(Color.parseColor("#1D7CA3"));
        } else {
            image.setImageResource(R.drawable.border_radius_input);
            title.setTextColor(Color.parseColor("#000000"));
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void scrollToPosition(int position) {
        recyclerView.smoothScrollToPosition(position);
    }

    // Define Interface for click event
    public interface ItemClickListener {
        void onItemClick(CityRecyclerViewAdapter.MyHolder holder);
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private final ViewBinding binding;
        private int pos;

        public MyHolder(@NonNull ViewBinding view) {
            super(view.getRoot());
            binding = view;

            view.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null) {
                        itemClickListener.onItemClick(MyHolder.this);
                    } else {
                        Log.d("Adapter: ", "You must create an ItemClickListener before !!!");
                    }
                }
            });
        }

        public int getPos() {
            return pos;
        }

        public void setPos(int pos) {
            this.pos = pos;
        }

        public ViewBinding getBinding() {
            return binding;
        }
    }
}
