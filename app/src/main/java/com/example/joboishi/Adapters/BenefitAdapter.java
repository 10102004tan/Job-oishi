package com.example.joboishi.Adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.example.joboishi.databinding.BenefitItemBinding;

import java.util.ArrayList;

public class BenefitAdapter extends RecyclerView.Adapter<BenefitAdapter.MyViewHolder> {
    private Activity context;
    private ArrayList<String> list;

    //Contractor
    public BenefitAdapter(Activity context, ArrayList<String> list) {
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
        String item = list.get(position);
        holder.binding.benefitsTitle.setText(item);
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
