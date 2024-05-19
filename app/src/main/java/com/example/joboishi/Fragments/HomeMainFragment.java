package com.example.joboishi.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import com.example.joboishi.Models.JobBasic;
import com.example.joboishi.R;
import com.example.joboishi.databinding.FragmentHomeMainBinding;
import com.facebook.bolts.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.thecode.aestheticdialogs.AestheticDialog;
import com.thecode.aestheticdialogs.DialogStyle;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeMainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeMainFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private IJobsService iJobsService;
    private JobAdapter adapter;
    private ArrayList<JobBasic> jobList;
    private FragmentHomeMainBinding binding;
    private SelectFilterJob selectFilterJob =  new SelectFilterJob();
    private MyBottomSheetDialogFragment myBottomSheetDialogFragment;
    private ArrayList<String> filterJob;
    private boolean isNotification = false;
    private boolean isFirst = true;
    private final  int STATUS_NO_INTERNET = 0;
    private final  int STATUS_LOW_INTERNET = 1;
    private final  int STATUS_GOOD_INTERNET = 2;
    private int statusInternet = -1;
    private int statusPreInternet = -1;
    private AestheticDialog.Builder builder;
    private static int page = 1;
    private ArrayList<Integer> arrId;
    public boolean isNotification() {
        return isNotification;
    }

    public void setNotification(boolean notification) {
        isNotification = notification;
    }

    public HomeMainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeMainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeMainFragment newInstance(String param1, String param2) {
        HomeMainFragment fragment = new HomeMainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        binding = FragmentHomeMainBinding.inflate(inflater, container, false);

        binding.imageNotInternet.setAnimation(R.raw.fetch_api_loading);
        //get All array id bookmark
        arrId = new ArrayList<>();

        //Start init
        Retrofit retrofit = new Retrofit.Builder().baseUrl(iJobsService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        iJobsService = retrofit.create(IJobsService.class);
        jobList = new ArrayList<>();
        //End init

        getJobs();
        adapter = new JobAdapter(jobList, getContext());
        adapter.setArrId(arrId);
        binding.listJob.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        binding.listJob.setAdapter(adapter);

        //refresh
        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getJobs();
            }
        });

        //processing add bookmark
        adapter.setiClickJob(new JobAdapter.IClickJob() {
            @Override
            public void onClickJob(int id) {
                Intent intent = new Intent(getActivity(), DetailJobActivity.class);
                intent.putExtra("JOB_ID", id);
                startActivity(intent);
            }

            @Override
            public void onAddJobBookmark(JobBasic job, ImageView bookmarkImage) {
                saveJobToBookmarks(job);
                bookmarkImage.setSelected(true);
            }

            @Override
            public void onRemoveBookmark(JobBasic job, ImageView bookmarkImage) {
                removeJobBookmark(3, job.getId());
                bookmarkImage.setSelected(false);
            }
        });

        //Get event for button sheet



        //processing load more
        adapter.setOnLoadMoreListener(() -> {
            page+=1;
            Toast.makeText(getContext(), "Dang tai ...", Toast.LENGTH_SHORT).show();
            Call<ArrayList<JobBasic>> call = iJobsService.getListJobsDB(page);
            call.enqueue(new Callback<ArrayList<JobBasic>>() {
                @Override
                public void onResponse(Call<ArrayList<JobBasic>> call, Response<ArrayList<JobBasic>> response) {
                    if (response.isSuccessful()) {
                        jobList.addAll(response.body());
                        binding.imageNotInternet.setVisibility(View.GONE);
                        adapter.updateData(jobList);
                    }
                }
                @Override
                public void onFailure(Call<ArrayList<JobBasic>> call, Throwable t) {
                    binding.swipeRefreshLayout.setRefreshing(false);
                }
            });
            adapter.setLoading(false);
        });


        //Add event for swipe refresh layout
        binding.swipeRefreshLayout.setOnRefreshListener(() -> {
            if (statusPreInternet != statusInternet){
               isFirst = true;
            }
            if (statusPreInternet == STATUS_NO_INTERNET){
                binding.swipeRefreshLayout.setRefreshing(false);
                binding.listJob.setVisibility(View.GONE);
                binding.imageNotInternet.setVisibility(View.VISIBLE);
            }
            else{
                getJobs();
                binding.listJob.setVisibility(View.VISIBLE);
            }
        });

        return binding.getRoot();


    }


    private void getJobs() {
        binding.imageNotInternet.setVisibility(View.VISIBLE);
        Call<ArrayList<JobBasic>> call = iJobsService.getListJobsDB(page);
        call.enqueue(new Callback<ArrayList<JobBasic>>() {
            @Override
            public void onResponse(Call<ArrayList<JobBasic>> call, Response<ArrayList<JobBasic>> response) {
                if (response.isSuccessful()) {
                    jobList = response.body();
                    binding.swipeRefreshLayout.setRefreshing(false);
                    binding.imageNotInternet.setVisibility(View.GONE);
                    adapter.updateData(jobList);
                }
            }
            @Override
            public void onFailure(Call<ArrayList<JobBasic>> call, Throwable t) {
                binding.swipeRefreshLayout.setRefreshing(false);
                Log.d("test11",t.getMessage() + " Loi doc api");
            }
        });
    }



    private void saveJobToBookmarks(JobBasic job) {
        DatabaseReference bookmarksRef = FirebaseDatabase.getInstance().getReference("bookmarks");
        String userId = "3"; // Lấy user ID từ SharedPreferences hoặc nơi lưu trữ khác
        bookmarksRef.child("userId"+userId).child("job"+job.getId()).setValue(job)
                .addOnSuccessListener(aVoid -> {
                    MotionToast.Companion.createToast(getActivity(), "😍",
                            "Đã thêm công việc thành công",
                            MotionToastStyle.SUCCESS,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(getContext(), R.font.helvetica_regular));
                })
                .addOnFailureListener(e -> {
                    // Xử lý lỗi
                    Log.d("test11",e.getMessage());
                    Toast.makeText(getContext(), "Lỗi khi thêm vào bookmark "+ e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void removeJobBookmark(int userId, int jobId) {
        DatabaseReference bookmarksRef = FirebaseDatabase.getInstance().getReference("bookmarks");
        bookmarksRef.child("userId"+userId).child("job"+jobId).removeValue()
                .addOnSuccessListener(aVoid -> {
                    MotionToast.Companion.createToast(getActivity(), "😍",
                            "Đã xoa công việc thành công",
                            MotionToastStyle.SUCCESS,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(getContext(), R.font.helvetica_regular));
                })
                .addOnFailureListener(e -> {
                    // Xử lý lỗi
                    Log.d("test11",e.getMessage());
                    Toast.makeText(getContext(), "Lỗi khi xóa khỏi bookmark "+ e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }



}