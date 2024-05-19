package com.example.joboishi.Fragments;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.joboishi.Activities.DetailJobActivity;
import com.example.joboishi.Adapters.JobAdapter;
import com.example.joboishi.Api.IJobsService;
import com.example.joboishi.BroadcastReceiver.InternetBroadcastReceiver;

import com.example.joboishi.Models.JobBasic;
import com.example.joboishi.R;
import com.example.joboishi.abstracts.BaseFragment;
import com.example.joboishi.databinding.FragmentSavedJobBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thecode.aestheticdialogs.AestheticDialog;
import com.thecode.aestheticdialogs.DialogStyle;
import com.thecode.aestheticdialogs.DialogType;
import com.thecode.aestheticdialogs.OnDialogClickListener;
import com.vdx.designertoast.DesignerToast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class SavedJobFragment extends BaseFragment {
    private FragmentSavedJobBinding binding;
    private ArrayList<JobBasic> jobs;
    private JobAdapter adapter;
    private InternetBroadcastReceiver internetBroadcastReceiver;
    private IntentFilter intentFilter;
    private boolean isFirst = true;
    private final  int STATUS_NO_INTERNET = 0;
    private final  int STATUS_LOW_INTERNET = 1;
    private final  int STATUS_GOOD_INTERNET = 2;
    private int statusInternet = -1;
    private int statusPreInternet = -1;
    private IJobsService iJobsService;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSavedJobBinding.inflate(inflater, container, false);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(iJobsService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        iJobsService = retrofit.create(IJobsService.class);
        jobs = new ArrayList<>();
        adapter = new JobAdapter(jobs,getContext());
        adapter.setBookmark(true);
        binding.listJob.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        binding.listJob.setAdapter(adapter);
        getJobsSaved();
        //Add event for adapter
        adapter.setiClickJob(new JobAdapter.IClickJob() {
            @Override
            public void onClickJob(int id) {
                Intent intent = new Intent(getActivity(), DetailJobActivity.class);
                intent.putExtra("id",id);
                startActivity(intent);
            }

            @Override
            public void onAddJobBookmark(JobBasic job, ImageView bookmarkImage) {
                //Khong lam gi
            }

            @Override
            public void onRemoveBookmark(JobBasic job, ImageView bookmarkImage) {
                DatabaseReference bookmarksRef = FirebaseDatabase.getInstance().getReference("bookmarks");
                bookmarksRef.child("userId3").child("job"+job.getId()).removeValue();
            }
        });

        //Add event for swipe refresh layout
        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (statusPreInternet != statusInternet){
                    registerInternetBroadcastReceiver();
                    isFirst = true;
                }
                getJobsSaved();
                if (statusInternet == STATUS_NO_INTERNET){
                    binding.swipeRefreshLayout.setRefreshing(false);
                    binding.image.setAnimation(R.raw.a404);
                    binding.image.playAnimation();
                }
            }
        });
        return binding.getRoot();
    }

    //get data from server

    private void getJobsSaved(){
        binding.listJob.setVisibility(View.GONE);
        binding.image.setVisibility(View.VISIBLE);
        binding.image.setAnimation(R.raw.fetch_api_loading);
        binding.image.playAnimation();
        DatabaseReference bookmarksRef = FirebaseDatabase.getInstance().getReference("bookmarks").child("userId3");
        bookmarksRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                jobs.clear();
                for (DataSnapshot jobSnapshot : dataSnapshot.getChildren()) {
                    JobBasic job = jobSnapshot.getValue(JobBasic.class);
                    jobs.add(job);
                }
                // Cập nhật Adapter của RecyclerView với danh sách bookmarkedJobs
                binding.swipeRefreshLayout.setRefreshing(false);
                if (jobs.size() == 0){
                    binding.image.setVisibility(View.VISIBLE);
                    binding.image.setAnimation(R.raw.no_data);
                    binding.image.playAnimation();
                    binding.listJob.setVisibility(View.GONE);
                }
                else{
                    binding.listJob.setVisibility(View.VISIBLE);
                    binding.image.setVisibility(View.GONE);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    @Override
    protected void handleNoInternet() {
        //When no internet, disable bookmark
        adapter.setEnableBookmark(false);
        statusPreInternet = STATUS_NO_INTERNET;
        if (isFirst) {
            binding.image.setVisibility(View.VISIBLE);
            binding.image.setAnimation(R.raw.a404);
            binding.image.playAnimation();
            statusInternet = STATUS_NO_INTERNET;
            binding.swipeRefreshLayout.setRefreshing(false);
            isFirst = false;
            ;
        }

        MotionToast.Companion.createToast(getActivity(), "😍",
                getString(R.string.kh_ng_c_k_t_n_i_m_ng),
                MotionToastStyle.ERROR,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                ResourcesCompat.getFont(getContext(), R.font.helvetica_regular));
    }

    @Override
    protected void handleLowInternet() {
        MotionToast.Companion.createToast(getActivity(), "😍",
                getString(R.string.ang_k_t_n_i),
                MotionToastStyle.WARNING,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                ResourcesCompat.getFont(getContext(), R.font.helvetica_regular));
    }

    @Override
    protected void handleGoodInternet() {
        //When internet is good, enable bookmark
        adapter.setEnableBookmark(true);
        statusPreInternet = STATUS_GOOD_INTERNET;
        if (isFirst) {
            statusInternet = STATUS_GOOD_INTERNET;
            isFirst = false;
        }
        else{
            if (statusInternet == STATUS_NO_INTERNET){
                binding.image.setVisibility(View.GONE);
                binding.listJob.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}