package com.example.joboishi.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.joboishi.Models.WorkForm;
import com.example.joboishi.R;
import com.example.joboishi.databinding.ChooseWorkFormLayoutBinding;

import java.util.ArrayList;

public class WorkFormAdapter extends RecyclerView.Adapter<WorkFormAdapter.MyViewHolder> {
    private final Activity context;
    private ArrayList<WorkForm> forms = new ArrayList<>();

    private ItemClickListener itemClickListener;

    public WorkFormAdapter(Activity context, ArrayList<WorkForm> forms) {
        this.context = context;
        this.forms = forms;

    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ChooseWorkFormLayoutBinding binding = ChooseWorkFormLayoutBinding.inflate(LayoutInflater.from(context), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        WorkForm form = forms.get(position);
        holder.workFormLayoutBinding.formWorkTitle.setText(form.getWorkFormName());
        if (form.isChosen()) {
            holder.workFormLayoutBinding.formWorkItem.setBackgroundResource(R.drawable.button_outline);
            holder.workFormLayoutBinding.formWorkCheckbox.setImageResource(R.drawable.check_svgrepo_com);
        } else {
            holder.workFormLayoutBinding.formWorkCheckbox.setImageResource(R.color.white);
            holder.workFormLayoutBinding.formWorkItem.setBackgroundResource(R.drawable.input_outline);
        }
    }

    @Override
    public int getItemCount() {
        return forms.size();
    }

    public interface ItemClickListener {
        void onItemClick(WorkFormAdapter.MyViewHolder holder, int position);

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final ChooseWorkFormLayoutBinding workFormLayoutBinding;

        public MyViewHolder(@NonNull ChooseWorkFormLayoutBinding binding) {
            super(binding.getRoot());
            workFormLayoutBinding = binding;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    itemClickListener.onItemClick(MyViewHolder.this, position);
                }

            });
        }

        public ChooseWorkFormLayoutBinding getWorkFormLayoutBinding() {
            return workFormLayoutBinding;
        }
    }
}
