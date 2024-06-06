package com.example.joboishi.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.joboishi.Models.Notification;
import com.example.joboishi.databinding.NotificationItemViewholderBinding;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>{


    private ArrayList<Notification> notifications;

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public NotificationAdapter(ArrayList<Notification> notifications) {
        this.notifications = notifications;
    }
    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // return use binding
        return new NotificationViewHolder(NotificationItemViewholderBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        Notification notification = notifications.get(position);
        holder.notification = notification;
        // set data to view
        holder.itemView.txtTitle.setText(notification.getTitle());
        holder.itemView.txtContent.setText(notification.getMessage());
        holder.itemView.txtTime.setText(notification.getCreatedAt());
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public class NotificationViewHolder extends RecyclerView.ViewHolder {
        private NotificationItemViewholderBinding itemView;
        private Notification notification;
        public NotificationViewHolder(@NonNull NotificationItemViewholderBinding itemView) {
            super(itemView.getRoot());
            this.itemView = itemView;

            itemView.getRoot().setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(notification);
                }
                else{
                    Log.d("NotificationAdapter", "onItemClick: onItemClickListener is null");
                }
            });
        }
    }

  public interface OnItemClickListener {
        void onItemClick(Notification notification);
    }
}
