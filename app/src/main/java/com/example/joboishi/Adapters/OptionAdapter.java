package com.example.joboishi.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.joboishi.databinding.OptionTypeJobItemBinding;

import java.util.ArrayList;

public class OptionAdapter extends RecyclerView.Adapter<OptionAdapter.MyViewHolder> {

    private Activity context;
    private ArrayList<String> listOption = new ArrayList<>();

    public ArrayList<String> getListOption() {
        return listOption;
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public OptionAdapter(Activity context, ArrayList<String> lists) {
        this.context = context;
        this.listOption = lists;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        OptionTypeJobItemBinding binding = OptionTypeJobItemBinding.inflate(LayoutInflater.from(context), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String optionName = listOption.get(position);
        holder.optionTypeJobItemBinding.name.setText(optionName);
    }

    @Override
    public int getItemCount() {
        return listOption.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private OptionTypeJobItemBinding optionTypeJobItemBinding;

        public MyViewHolder(@NonNull OptionTypeJobItemBinding binding) {
            super(binding.getRoot());
            this.optionTypeJobItemBinding = binding;

            // Gán sự kiện click cho CardView
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        onItemClickListener.onItemClick(position);
                    }
                }

            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

}
