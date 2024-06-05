package com.example.joboishi.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.example.joboishi.R;
import com.example.joboishi.ViewModels.HomeViewModel;
import com.example.joboishi.databinding.ListCityRycBinding;

import java.util.ArrayList;


public class SimpleStringRecyclerViewAdapter2 extends RecyclerView.Adapter<SimpleStringRecyclerViewAdapter2.MyHolder> {

    private final Activity context;
    private final LifecycleOwner lifecycleOwner;
    private final ArrayList<String> data;
    private ItemClickListener itemClickListener;
    private HomeViewModel homeViewModel;

    public SimpleStringRecyclerViewAdapter2(Activity context, ArrayList<String> data,HomeViewModel homeViewModel,LifecycleOwner lifecycleOwner) {
        this.context = context;
        this.data = data;
        this.homeViewModel = homeViewModel;
        this.lifecycleOwner = lifecycleOwner;
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
        holder.title.setText(data.get(position));
        homeViewModel.getCurrentTabPosition().observe(lifecycleOwner, integer -> {
            if (integer == 0) {
                homeViewModel.getSelectedValueJoboishi().observe(lifecycleOwner, str -> {
                    if (str.equals(data.get(position))) {
                        holder.image.setImageResource(com.example.joboishi.R.drawable.check_svgrepo_com);
                        holder.title.setTextColor(Color.parseColor("#1D7CA3"));
                    } else {
                        holder.image.setImageResource(R.drawable.border_radius_input);
                        holder.title.setTextColor(Color.parseColor("#000000"));
                    }
                });
            } else {
                homeViewModel.getSelectedValueTopDev().observe(lifecycleOwner, str -> {
                    if (str.equals(data.get(position))) {
                        holder.image.setImageResource(com.example.joboishi.R.drawable.check_svgrepo_com);
                        holder.title.setTextColor(Color.parseColor("#1D7CA3"));
                    } else {
                        holder.image.setImageResource(R.drawable.border_radius_input);
                        holder.title.setTextColor(Color.parseColor("#000000"));
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    // Define Interface for click event
    public interface ItemClickListener {
        void onItemClick(SimpleStringRecyclerViewAdapter2.MyHolder holder);
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private final ViewBinding binding;
        private final TextView title;
        private final ImageButton image;
        private int pos;
        public MyHolder(@NonNull ViewBinding view) {
            super(view.getRoot());
            binding = view;
            title = binding.getRoot().findViewById(R.id.city_name);
            image = binding.getRoot().findViewById(R.id.btn_check);

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


        public TextView getTitle() {
            return title;
        }

        public ImageButton getImage() {
            return image;
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
