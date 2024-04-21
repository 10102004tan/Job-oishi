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


import com.example.joboishi.Api.CountryApiResponse;
import com.example.joboishi.R;
import com.example.joboishi.databinding.ListCountryRycBinding;

import java.util.ArrayList;


public class CountryRecyclerViewAdapter extends RecyclerView.Adapter<CountryRecyclerViewAdapter.MyHolder> {

    private final Activity context;
    private final ArrayList<CountryApiResponse> data;
    private final RecyclerView recyclerView;
    private final String selectedCountry;
    private ItemClickListener itemClickListener;

    public CountryRecyclerViewAdapter(Activity context, ArrayList<CountryApiResponse> data, RecyclerView recyclerView, String selectedCountry) {
        this.context = context;
        this.data = data;
        this.recyclerView = recyclerView;
        this.selectedCountry = selectedCountry;
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
        return new MyHolder(ListCountryRycBinding.inflate(context.getLayoutInflater(), parent, false));
    }

    @SuppressLint("ResourceAsColor")
    public void onBindViewHolder(@NonNull MyHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.setPos(position);

        TextView title = holder.getBinding().getRoot().findViewById(com.example.joboishi.R.id.country_name);
        ImageView image = holder.getBinding().getRoot().findViewById(R.id.btn_check);

        title.setText(data.get(position).getName().getCommon());

        // Display check icon when user choose a country
        if (selectedCountry.equals(data.get(position).getName().getCommon())) {
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
        void onItemClick(CountryRecyclerViewAdapter.MyHolder holder);
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
