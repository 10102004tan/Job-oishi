package com.example.joboishi.Adapters;

import android.app.Activity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.datto.demo_android.models.InforProfileAdd;
import com.example.joboishi.R;
import com.example.joboishi.databinding.ProfileRecyclerViewItemBinding;

import java.util.ArrayList;


public class ProfileRecyclerViewAdapter extends RecyclerView.Adapter<ProfileRecyclerViewAdapter.MyHolder> {

    public static final String BUILDING_ICON = "building";
    public static final String EDU_ICON = "edu";
    public static final String SKILL_ICON = "skill";
    private final Activity context;
    private final ArrayList<InforProfileAdd> data;
    private final RecyclerView recyclerView;

    public ProfileRecyclerViewAdapter(Activity context, ArrayList<InforProfileAdd> data, RecyclerView recyclerView) {
        this.context = context;
        this.data = data;
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyHolder(ProfileRecyclerViewItemBinding.inflate(context.getLayoutInflater(), parent, false));
    }


    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        ImageView image = holder.getBinding().getRoot().findViewById(com.example.joboishi.R.id.item_image);
        TextView title = holder.getBinding().getRoot().findViewById(R.id.item_title);
        TextView des = holder.getBinding().getRoot().findViewById(R.id.item_des);
        AppCompatButton button = holder.getBinding().getRoot().findViewById(R.id.item_button);


        title.setText(data.get(position).getTitle());
        des.setText(data.get(position).getDescription());
        button.setText(data.get(position).getButtonTitle());

        switch (data.get(position).getIcon()) {
            case BUILDING_ICON:
                image.setImageResource(R.drawable.build_svgrepo_com);
                break;
            case EDU_ICON:
                image.setImageResource(R.drawable.education_svgrepo_com);
                break;
            case SKILL_ICON:
                image.setImageResource(R.drawable.fix_mechanic_repair_svgrepo_com);
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void scrollToPosition(int position) {
        recyclerView.smoothScrollToPosition(position);
    }


    public static class MyHolder extends RecyclerView.ViewHolder {
        private final ViewBinding binding;

        public MyHolder(@NonNull ViewBinding view) {
            super(view.getRoot());
            binding = view;
        }

        public ViewBinding getBinding() {
            return binding;
        }
    }
}
