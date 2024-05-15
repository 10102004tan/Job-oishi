package com.example.joboishi.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.joboishi.Fragments.MyBottomSheetDialogFragment;
import com.example.joboishi.Fragments.SelectSalaryFragment;
import com.example.joboishi.R;

import org.w3c.dom.Text;

public class JobCriteriaActivity extends AppCompatActivity {

    private MyBottomSheetDialogFragment myBottomSheetDialogFragment;
    private final SelectSalaryFragment salaryFragment = new SelectSalaryFragment();

    private final int REQUEST_TO_REGISTER_MAJOR_ACTIVITY_CODE = 8080;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_job_criteria);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Change toolbar title
        TextView textTitle = findViewById(R.id.toolbar_text_title);
        textTitle.setText(R.string.job_criteria_toolbar_title);

        // Button back in toolbar
        ImageButton btnBack = findViewById(R.id.btn_toolbar_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Note: Handle later
            }
        });

        // Salary Textview
        TextView salaryTextView = findViewById(R.id.salary_text_view);
        salaryTextView.setText("1 - 9 Tr");
        salaryFragment.setSalaryTextView(salaryTextView);
        salaryFragment.setMinSalarySelected(1);
        salaryFragment.setMaxSalarySelected(9);


        // Button choose salary
        LinearLayout buttonChooseSalary = findViewById(R.id.btn_choose_salary);
        buttonChooseSalary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myBottomSheetDialogFragment = MyBottomSheetDialogFragment.newInstance();
                myBottomSheetDialogFragment.setFragment(salaryFragment);
                myBottomSheetDialogFragment.show(getSupportFragmentManager(),"MyBottomSheetDialogFragmentTag");
            }
        });

        // Go to add job position screen
        LinearLayout buttonGotoAddJobPosition = findViewById(R.id.btn_choose_job_pos);
        buttonGotoAddJobPosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JobCriteriaActivity.this, RegisterMajorActivity.class);
                startActivityForResult(intent, REQUEST_TO_REGISTER_MAJOR_ACTIVITY_CODE);
            }
        });

        // Go to add city screen
        LinearLayout buttonGotoAddCity = findViewById(R.id.btn_choose_city_name);
        buttonGotoAddCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JobCriteriaActivity.this, AddressActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_TO_REGISTER_MAJOR_ACTIVITY_CODE && resultCode == RESULT_OK) {
            if (data != null) {
//                String value = data.get("majorsChosen");
//                assert value != null;
//                Log.d("majorsChosen", value);
            }
        }
    }
}