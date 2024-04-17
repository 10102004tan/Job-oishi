package com.example.joboishi.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.joboishi.adapters.RegisterMajorAdapter;
import com.example.joboishi.adapters.MajorChosenAdapter;
import com.example.joboishi.databinding.RegisterMajorLayoutBinding;
import com.example.joboishi.models.RegisterMajors;

import java.util.ArrayList;

public class RegisterMajorActivity extends AppCompatActivity{

    private RegisterMajorLayoutBinding registerMajorLayoutBinding;
    private ArrayList<RegisterMajors> majors = new ArrayList<>();
    private ArrayList<RegisterMajors> majorsChosen = new ArrayList<>();

    RegisterMajorAdapter registerMajorAdapter;
    MajorChosenAdapter majorChosenAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerMajorLayoutBinding = RegisterMajorLayoutBinding.inflate(getLayoutInflater());
        setContentView(registerMajorLayoutBinding.getRoot());


        RegisterMajors majors1 = new RegisterMajors("Kỹ sư trí tuệ nhân tạo", "AI Engineer", true);
        RegisterMajors majors2 = new RegisterMajors("Frontend", "AI Engineer", false);
        RegisterMajors majors3 = new RegisterMajors("Backend", "AI Engineer", false);
        RegisterMajors majors4 = new RegisterMajors("DevOps", "AI Engineer", false);
        RegisterMajors majors5 = new RegisterMajors("Design App", "AI Engineer", false);
        RegisterMajors majors6 = new RegisterMajors("Design Web", "AI Engineer", false);
        RegisterMajors majors7 = new RegisterMajors("Full Stack", "AI Engineer", false);

        majors.add(majors1);
        majors.add(majors2);
        majors.add(majors3);
        majors.add(majors4);
        majors.add(majors5);
        majors.add(majors6);
        majors.add(majors7);

        for (RegisterMajors major : majors) {
            if (major.getChecked()) {
                majorsChosen.add(major);
            }
        }

        updateChosenCountTextView();
        // Set adapter cho RecyclerView hiển thị danh sách majors
        registerMajorAdapter = new RegisterMajorAdapter(this, majors);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        registerMajorLayoutBinding.showListMajor.setLayoutManager(layoutManager);
        registerMajorLayoutBinding.showListMajor.setAdapter(registerMajorAdapter);

        registerMajorAdapter.setItemClickListener(new RegisterMajorAdapter.ItemClickListener() {
            @Override
            public void onItemClick(RegisterMajorAdapter.MyViewHolder holder, int position) {
                    if (position != RecyclerView.NO_POSITION) {
                        updateChosenCountTextView();
                        RegisterMajors clickedMajor = majors.get(position);
                        // Kiểm tra nếu major chưa được chọn
                        if (!clickedMajor.getChecked()) {
                            // Thêm major vào danh sách majorsChosen
                            majorsChosen.add(clickedMajor);
                        } else {
                            majorsChosen.remove(clickedMajor);
                        }
                        // Thay đổi trạng thái của CheckBox khi click vào CardView
                        clickedMajor.setChecked(!clickedMajor.getChecked());

                        registerMajorAdapter.notifyItemChanged(position);
                        majorChosenAdapter.notifyDataSetChanged();

                        // Hiển thị Toast để kiểm tra
                        Log.d("TAG", majorsChosen.size() + "");
                    }

            }

        });

        // Set adapter cho RecyclerView hiển thị danh sách majors đã chọn
        majorChosenAdapter = new MajorChosenAdapter(this, majorsChosen);

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this);
        layoutManager2.setOrientation(RecyclerView.HORIZONTAL);
        registerMajorLayoutBinding.showListMajorsChosen.setLayoutManager(layoutManager2);
        registerMajorLayoutBinding.showListMajorsChosen.setAdapter(majorChosenAdapter);

        majorChosenAdapter.setItemClickListener(new MajorChosenAdapter.ItemClickListener() {
            @Override
            public void onItemClick(MajorChosenAdapter.MyViewHolder holder, int position) {
                RegisterMajors major = majorsChosen.get(position);
                majorsChosen.remove(major);

                // Xóa major khỏi danh sách majorsChosen
                majorChosenAdapter.notifyDataSetChanged();
                updateChosenCountTextView();
                // Cập nhật trạng thái của major trong danh sách majors ban đầu
                for (int i = 0; i < majors.size(); i++) {
                    RegisterMajors originalMajor = majors.get(i);
                    if (originalMajor.equals(major)) {
                        originalMajor.setChecked(false);
                        registerMajorAdapter.notifyItemChanged(i);
                        break;
                    }
                }

            }
        });


        AppCompatButton btnNext = registerMajorLayoutBinding.btnNextAdress;
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Kiểm tra xem có danh sách majors nào được chọn hay không
                if (hasMajorsChosen()) {
                    // Tạo Intent để chuyển đến NextActivity và đính kèm danh sách majors đã chọn
                    Intent intent = new Intent(RegisterMajorActivity.this, AddressActivity.class);
                    intent.putExtra("majorsChosen", majorsChosen);
                    // Chuyển đến NextActivity
                    startActivity(intent);

                } else {
                    // Hiển thị thông báo cho người dùng rằng họ cần phải chọn ít nhất một vị trí trước khi tiếp tục
                    Toast.makeText(RegisterMajorActivity.this, "Bạn cần phải chọn ít nhất một vị trí trước khi tiếp tục", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean hasMajorsChosen() {
        return majorsChosen != null && !majorsChosen.isEmpty();
    }

    private void updateChosenCountTextView() {
        TextView countChosenAllow = registerMajorLayoutBinding.countChosenAllow;
        if (majorsChosen != null) {
            int count = majorsChosen.size();
            int remainingCount = 5 - count;
            if (remainingCount > 0) {
                countChosenAllow.setText("Bạn còn có thể chọn " + remainingCount + " vị trí.");
            } else {
                countChosenAllow.setText("Bạn đã chọn tối đa.");
            }
        } else {
            countChosenAllow.setText("Bạn có thể lựa chọn tối đa 5 vị trí.");
        }
    }


}
