package com.example.joboishi.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.datto.demo_android.models.InforProfileAdd;
import com.example.joboishi.Adapters.ProfileRecyclerViewAdapter;
import com.example.joboishi.R;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {

    private final ArrayList<InforProfileAdd> profileRecyclerViewData = new ArrayList<>();
    private final ArrayList<ImageView> indicators = new ArrayList<>();
    LinearLayout indicatorLayout;
    private int currentPosition = 0;
    private Intent profileIntent;


    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(com.example.joboishi.R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        profileRecyclerViewData.add(new InforProfileAdd("Công việc gần nhất", "Thêm kinh nghiệm làm việc để nhà tuyển dụng thấy thành tựu và trách nhiệm mà bạn đạt được", "Thêm kinh nghiệm làm việc", ProfileRecyclerViewAdapter.BUILDING_ICON));
        profileRecyclerViewData.add(new InforProfileAdd("Trình độ học vấn của bạn", "Thêm trình độ học vấn để nhà tuyển dụng dễ dàng tìm thấy bạn thấy", "Thêm trình độ học vấn", ProfileRecyclerViewAdapter.EDU_ICON));
        profileRecyclerViewData.add(new InforProfileAdd("Trường học gần đây", "Thêm học vấn để nhà tuyển dụng cân nhắc dễ dàng hơn", "Thêm học vẫn", ProfileRecyclerViewAdapter.EDU_ICON));
        profileRecyclerViewData.add(new InforProfileAdd("Top kỹ năng của bạn", "Cho nhà tuyển dụng thấy khả năng của bạn như thế nào", "Thêm kĩ năng", ProfileRecyclerViewAdapter.SKILL_ICON));


        // Get recycler view
        RecyclerView recyclerView = findViewById(R.id.profile_recycler_view);
        ProfileRecyclerViewAdapter recyclerViewAdapter = new ProfileRecyclerViewAdapter(this, profileRecyclerViewData, recyclerView);
        // Get button navigate to edit profile page
        ImageButton btnNavigateToEditProfilePage = findViewById(R.id.button_navigate_edit_profile_screen);
        // Change title of toolbar
        TextView textTitle = findViewById(R.id.toolbar_text_title);
        textTitle.setText(R.string.profile_toolbar_title);

        // Management layout of recycler view
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();

        // Create indicator slide
        indicatorLayout = findViewById(R.id.indicator);

        for (int i = 0; i < profileRecyclerViewData.size(); i++) {
            ImageView indicator = new ImageView(this);
            indicator.setImageResource(R.drawable.indicator);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(14, 14);
            layoutParams.setMargins(8, 0, 8, 0);
            indicator.setLayoutParams(layoutParams);
            indicatorLayout.addView(indicator);
            indicators.add(indicator);
        }

        // Scroll Smooth
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);

        // Update indicator when scroll
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                assert layoutManager != null;
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                updateIndicators(firstVisibleItemPosition);
            }
        });


        // Event handler for button
        // Button navigate to edit profile screen
        btnNavigateToEditProfilePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileIntent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                startActivity(profileIntent);
            }
        });

        // Button back in toolbar
        ImageButton btnBack = findViewById(R.id.btn_toolbar_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Unprocessed function", Toast.LENGTH_SHORT).show();
                // Handle back here
            }
        });

    }

    // Update indicator
    private void updateIndicators(int position) {
        for (int i = 0; i < indicators.size(); i++) {
            if (i == position) {
                indicators.get(i).setImageResource(R.drawable.indicator_active); // Active
            } else {
                indicators.get(i).setImageResource(R.drawable.indicator); // Not active
            }
        }
    }

    // Auto slide recycler view
    private void autoSlide(RecyclerView recyclerView) {
        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                currentPosition++;
                if (currentPosition == profileRecyclerViewData.size()) {
                    currentPosition = 0;
                }

                recyclerView.smoothScrollToPosition(currentPosition);
                // handle loop
                handler.postDelayed(this, 4000);
            }
        });
    }
}