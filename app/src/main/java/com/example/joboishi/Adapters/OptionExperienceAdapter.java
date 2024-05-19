package com.example.joboishi.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.joboishi.R;
import com.example.joboishi.databinding.OptionTypeJobItemBinding;

import java.util.ArrayList;

public class OptionExperienceAdapter extends RecyclerView.Adapter<OptionExperienceAdapter.MyViewHolder> {

    private Activity context;
    private ArrayList<String> listOption = new ArrayList<>();
    private int selectedPosition = RecyclerView.NO_POSITION;
    private static final String PREF_NAME = "OptionExperiencePrefs";
    private static final String SELECTED_POSITION_KEY = "OptionExperiencePos";

    public ArrayList<String> getListOption() {
        return listOption;
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public OptionExperienceAdapter(Activity context, ArrayList<String> lists) {
        this.context = context;
        this.listOption = lists;
        this.selectedPosition = getSavedSelectedPosition(context);
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

        // Change background and text color based on selection state
        if (selectedPosition == position) {
            holder.optionTypeJobItemBinding.name.setBackgroundResource(R.drawable.background_item_selected);
            holder.optionTypeJobItemBinding.name.setTextColor(ContextCompat.getColor(context, R.color.selected_color));
        }
        else {
            holder.optionTypeJobItemBinding.name.setBackgroundResource(R.drawable.background_item_chosen);
            holder.optionTypeJobItemBinding.name.setTextColor(ContextCompat.getColor(context, R.color.default_color));
        }

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

            // Set click event for the item
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        if (selectedPosition == position) {
                            // Deselect the item if it's already selected
                            notifyItemChanged(selectedPosition);
                            selectedPosition = RecyclerView.NO_POSITION;
                            clearSavedSelectedPosition(context);
                        } else {
                            // Notify item click
                            if (onItemClickListener != null) {
                                onItemClickListener.onItemClick(position);
                            }
                            // Update selected position
                            notifyItemChanged(selectedPosition);
                            selectedPosition = position;
                            notifyItemChanged(selectedPosition);
                            saveSelectedPosition(context, selectedPosition);
                        }
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    private void saveSelectedPosition(Context context, int position) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(SELECTED_POSITION_KEY, position);
        editor.apply();
    }

    private int getSavedSelectedPosition(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(SELECTED_POSITION_KEY, RecyclerView.NO_POSITION);
    }

    public void clearSavedSelectedPosition(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(SELECTED_POSITION_KEY);
        editor.apply();
        selectedPosition = RecyclerView.NO_POSITION;
    }
}
