package com.example.joboishi.Adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.example.joboishi.Models.data.Benefit;
import com.example.joboishi.databinding.BenefitItemBinding;

import java.util.ArrayList;

public class BenefitAdapter extends RecyclerView.Adapter<BenefitAdapter.MyViewHolder> {
    private Activity context;
    private ArrayList<Benefit> list;

    //Contractor
    public BenefitAdapter(Activity context, ArrayList<Benefit> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(BenefitItemBinding.inflate(context.getLayoutInflater(), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Benefit item = list.get(position);
        String title = item.getValue();
        if (title.length() > 20) {
            int endIndex = title.lastIndexOf(" ", 20);
            if(endIndex != -1) {
                endIndex = 20;
            }
            title = title.substring(0, endIndex) + "...";
        }
        holder.binding.benefitsTitle.setText(title);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends  RecyclerView.ViewHolder {
        private BenefitItemBinding binding;

        public MyViewHolder(@NonNull BenefitItemBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
