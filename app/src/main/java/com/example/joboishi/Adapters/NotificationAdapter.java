package com.example.joboishi.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.joboishi.Models.Notification;
import com.example.joboishi.databinding.NotificationItemViewholderBinding;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

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
        if (notification.getIsRead() == 0) {
            holder.itemView.noRead.setVisibility(View.VISIBLE);
        } else {
            holder.itemView.noRead.setVisibility(View.GONE);
        }
        holder.itemView.txtTitle.setText(notification.getTitle());
        if (notification.getMessage().length() > 30){
            holder.itemView.txtContent.setText(notification.getMessage().substring(0,30)+" ... Xem thêm");
        }

        holder.itemView.txtTime.setText(notification.getCreatedAt());
    }

    //format time

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
