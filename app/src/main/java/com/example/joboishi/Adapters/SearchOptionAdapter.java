package com.example.joboishi.Adapters;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.joboishi.R;
import com.example.joboishi.databinding.OptionSearchItemBinding;

import java.util.ArrayList;

public class SearchOptionAdapter extends RecyclerView.Adapter<SearchOptionAdapter.MyViewHolder> {

    private Activity context;
    private ArrayList<String> listOption = new ArrayList<>();
    private boolean isRemoteSelected = false; // Biến trạng thái để theo dõi mục "remote"

    public ArrayList<String> getListOption() {
        return listOption;
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public SearchOptionAdapter(Activity context, ArrayList<String> lists) {
        this.context = context;
        this.listOption = lists;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        OptionSearchItemBinding binding = OptionSearchItemBinding.inflate(LayoutInflater.from(context), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String optionName = listOption.get(position);
        holder.optionSearchItemBinding.optionName.setText(optionName);

        // Cập nhật màu nền dựa trên trạng thái lựa chọn
        if (position == 1 && isRemoteSelected) {
            holder.optionSearchItemBinding.optionName.setTextColor(ContextCompat.getColor(context, R.color.selected_color));
            holder.optionSearchItemBinding.optionName.setBackgroundResource(R.drawable.background_item_selected);
        } else {
            holder.optionSearchItemBinding.optionName.setTextColor(ContextCompat.getColor(context, R.color.default_color));
            holder.optionSearchItemBinding.optionName.setBackgroundResource(R.drawable.background_item_chosen);

        }
    }

    @Override
    public int getItemCount() {
        return listOption.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private OptionSearchItemBinding optionSearchItemBinding;

        public MyViewHolder(@NonNull OptionSearchItemBinding binding) {
            super(binding.getRoot());
            this.optionSearchItemBinding = binding;

            // Gán sự kiện click cho CardView
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        // Đổi trạng thái của mục "remote" khi được click
                        if (position == 1) {
                            isRemoteSelected = !isRemoteSelected;
                            notifyDataSetChanged(); // Cập nhật lại giao diện
                        }
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
