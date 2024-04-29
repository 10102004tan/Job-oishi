package com.example.joboishi.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.joboishi.databinding.RelatedInformationHolderBinding;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class RelatedInforJobApdater extends  RecyclerView.Adapter<RelatedInforJobApdater.RelatedInforJobViewHolder>{
    private ArrayList<String> relatedInfor;

    public RelatedInforJobApdater(ArrayList<String> relatedInfor) {
        this.relatedInfor = relatedInfor;
    }
    @NonNull
    @Override
    public RelatedInforJobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        RelatedInformationHolderBinding binding = RelatedInformationHolderBinding.inflate(layoutInflater, parent, false);
        return new RelatedInforJobViewHolder(binding);
    }
    @Override
    public void onBindViewHolder(@NonNull RelatedInforJobViewHolder holder, int position) {
        String content = relatedInfor.get(position);
        holder.binding.relatedInforTxt.setText(content);
    }

    @Override
    public int getItemCount() {
        return relatedInfor.size();
    }

    class RelatedInforJobViewHolder extends RecyclerView.ViewHolder {

        private RelatedInformationHolderBinding binding;
        public RelatedInforJobViewHolder(@NonNull RelatedInformationHolderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
