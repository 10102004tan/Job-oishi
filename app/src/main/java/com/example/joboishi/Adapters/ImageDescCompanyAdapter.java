package com.example.joboishi.Adapters;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.joboishi.Activities.DetailCompanyActivity;
import com.example.joboishi.databinding.ImageItemLayoutBinding;

import java.util.ArrayList;

public class ImageDescCompanyAdapter extends RecyclerView.Adapter<ImageDescCompanyAdapter.MyViewHolder> {

    private ArrayList<String> listImages;
    private Activity context;

    public ArrayList<String> getListImages() {
        return listImages;
    }

    public void setListImages(ArrayList<String> listImages) {
        this.listImages = listImages;
    }

    public Activity getContext() {
        return context;
    }

    public void setContext(Activity context) {
        this.context = context;
    }

    public ImageDescCompanyAdapter(ArrayList<String> listImages, Activity context) {
        this.listImages = listImages;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(ImageItemLayoutBinding.inflate(context.getLayoutInflater(), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String image = listImages.get(position);
//        Log.d("test", "Image: " + image);
        Glide.with(holder.itemView.getContext())
                .load(image)
                .into(holder.binding.imageDescCompany);
    }

    @Override
    public int getItemCount() {
        return listImages.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageItemLayoutBinding binding;
        public MyViewHolder(@NonNull ImageItemLayoutBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }

    public interface IClickImage {
        void onClickImage ();
    }
}
