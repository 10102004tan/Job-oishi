package com.example.joboishi.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.joboishi.Adapters.SimpleStringRecyclerViewAdapter;
import com.example.joboishi.R;
import com.example.joboishi.Views.TimePicker;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class SelectExperienceFragment extends BottomSheetDialogFragment {
    private Fragment fragment;
    private final Date currentDate = new Date();
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private final String formattedDate = sdf.format(currentDate);
    private ArrayList<String> experienceData = new ArrayList<>();
    private String experienceSelectedValue = "";
    private TextView experienceTextView;
    private String experienceStartedDateValue = ""; // Begin employment time
    private boolean isHaveExperience = false; // experience of user
    private String tempExperience = "";

    public String getTempExperience() {
        return tempExperience;
    }



    public void setTempExperience(String tempExperience) {
        this.tempExperience = tempExperience;
    }

    public ArrayList<String> getExperienceData() {
        return experienceData;
    }

    public void setExperienceData(ArrayList<String> experienceData) {
        this.experienceData = experienceData;
    }

    public String getExperienceSelectedValue() {
        return experienceSelectedValue;
    }

    public void setExperienceSelectedValue(String experienceSelectedValue) {
        this.experienceSelectedValue = experienceSelectedValue;
    }

    public TextView getExperienceTextView() {
        return experienceTextView;
    }

    public void setExperienceTextView(TextView experienceTextView) {
        this.experienceTextView = experienceTextView;
    }

    public String getExperienceStartedDateValue() {
        return experienceStartedDateValue;
    }

    public void setExperienceStartedDateValue(String experienceStartedDateValue) {
        this.experienceStartedDateValue = experienceStartedDateValue;
    }

    public boolean isHaveExperience() {
        return isHaveExperience;
    }

    public void setHaveExperience(boolean haveExperience) {
        isHaveExperience = haveExperience;
    }

    public Fragment getFragment() {
        return fragment;
    }
    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }
    public static MyBottomSheetDialogFragment newInstance() {
        return new MyBottomSheetDialogFragment();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_sheet_experience_layout, container, false);
    }
    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if (fragment != null){
            fragment = getFragment();
            FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment,fragment).commit();
        }

        RecyclerView ListExperienceRyc = view.findViewById(R.id.list_experience_ryc);
        SimpleStringRecyclerViewAdapter recyclerViewAdapter = new SimpleStringRecyclerViewAdapter((Activity) getContext(), experienceData, ListExperienceRyc, experienceSelectedValue);
        // Management layout of recycler view
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        assert ListExperienceRyc != null;
        ListExperienceRyc.setLayoutManager(layoutManager);
        ListExperienceRyc.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();

        TimePicker monthPicker = view.findViewById(R.id.month_picker_start);
        TimePicker yearPicker = view.findViewById(R.id.year_picker_end);

        int currentMonth = Integer.parseInt(formattedDate.split("/")[1]);
        int currentYear = Integer.parseInt(formattedDate.split("/")[2]);

        String messageExprience = "";
        TextView experienceMessageTextView = view.findViewById(R.id.experience_year_message);

        // Choose time experience
        assert monthPicker != null;
        monthPicker.setOnScrollListener(new NumberPicker.OnScrollListener() {
            @Override
            public void onScrollStateChange(NumberPicker view, int scrollState) {
                int month = Integer.parseInt(String.valueOf(view.getValue()));
            }
        });


        assert yearPicker != null;
        yearPicker.setOnScrollListener(new NumberPicker.OnScrollListener() {
            @Override
            public void onScrollStateChange(NumberPicker view, int scrollState) {

            }
        });


        // Show section time experience if user have experience
        LinearLayout haveExperience = view.findViewById(R.id.have_experience);
        tempExperience = experienceData.get(1); // No experience
        if (isHaveExperience) {
            tempExperience = experienceData.get(0); // Have experience
            assert haveExperience != null;
            haveExperience.setVisibility(View.VISIBLE);
            int month = Integer.parseInt(experienceStartedDateValue.split("/")[0]);
            int year = Integer.parseInt(experienceStartedDateValue.split("/")[1]);

            int monthsBetween = (currentYear - year) * 12 + (currentMonth - month);
            int yearsBetween = monthsBetween / 12;
            monthsBetween %= 12;

            messageExprience = monthsBetween > 0 ? monthsBetween + " tháng, " + yearsBetween + " năm kinh nghiệm" : yearsBetween + " năm kinh nghiệm";

            assert experienceMessageTextView != null;
            experienceMessageTextView.setText(messageExprience);

            monthPicker.setValue(month);
            yearPicker.setValue(year);
        }

        // Click event for item of country recycler view
        recyclerViewAdapter.setItemClickListener(new SimpleStringRecyclerViewAdapter.ItemClickListener() {
            @Override
            public void onItemClick(SimpleStringRecyclerViewAdapter.MyHolder holder) {
                tempExperience = experienceData.get(holder.getPos());

                assert haveExperience != null;
                if (tempExperience.equals(experienceData.get(0))) {
                    // Have experience
                    haveExperience.setVisibility(View.VISIBLE);
                } else {
                    // Not have experience
                    haveExperience.setVisibility(View.GONE);
                }
            }
        });

        // Save experience
        Button btnSaveExperience = view.findViewById(R.id.btn_save_experience);
        assert btnSaveExperience != null;
        btnSaveExperience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                experienceSelectedValue = tempExperience;
                Log.d("EXEX", experienceSelectedValue);

                isHaveExperience = experienceSelectedValue.equals(experienceData.get(0)); // Update is have experience

                if (isHaveExperience) {
                    experienceStartedDateValue = monthPicker.getValue() < 10 ? "0" + monthPicker.getValue() + "/" + yearPicker.getValue() : monthPicker.getValue() + "/" + yearPicker.getValue();
                    experienceTextView.setText(experienceStartedDateValue);
                }else {
                    experienceTextView.setText(experienceSelectedValue);
                }

                tempExperience = "";
                onCloseDialog();
            }
        });

        // Close dialog
        ImageButton btnClose = view.findViewById(R.id.btn_close_experience_dialog);
        assert btnClose != null;
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCloseDialog();
            }
        });
    }
    public void  onCloseDialog() {
        MyBottomSheetDialogFragment bottomSheetFragment = (MyBottomSheetDialogFragment) getParentFragment();
        if (bottomSheetFragment != null) {
            bottomSheetFragment.dismiss();
        }
    }
}
