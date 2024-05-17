package com.example.joboishi.Fragments.BottomSheetDialog;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.joboishi.Adapters.OptionAdapter;
import com.example.joboishi.Fragments.MyBottomSheetDialogFragment;
import com.example.joboishi.R;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.vdx.designertoast.DesignerToast;

import java.util.ArrayList;
import java.util.Arrays;

public class OptionExperienceFragment extends Fragment {
    private OptionAdapter adapter;
    private RecyclerView recyclerView;
    private TextView title;
    private ImageButton btnClose;
    private AppCompatButton btnDone;
    private OptionTypeJobFragment.OnOptionSelectedListener listener;
    private String selectedOption;
    private final int POS = 1;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OptionTypeJobFragment.OnOptionSelectedListener) {
            listener = (OptionTypeJobFragment.OnOptionSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnOptionSelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_option_search_layout, container, false);

        title = view.findViewById(R.id.title);
        recyclerView = view.findViewById(R.id.list_option_type_job);
        btnClose = view.findViewById(R.id.btn_close_dialog);
        btnDone = view.findViewById(R.id.btn_done);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCloseDialog();
            }
        });

        title.setText("Năm kinh nghiệm");

        ArrayList<String> listOption = new ArrayList<>(Arrays.asList("1 năm", "2 năm", "3 năm", "5 năm", "10 năm"));
        adapter = new OptionAdapter(getActivity(), listOption);

        FlexboxLayoutManager flexboxLayoutManager = new FlexboxLayoutManager(getContext());
        flexboxLayoutManager.setFlexDirection(FlexDirection.ROW);
        flexboxLayoutManager.setFlexWrap(FlexWrap.WRAP);
        flexboxLayoutManager.setJustifyContent(JustifyContent.FLEX_START);

        recyclerView.setLayoutManager(flexboxLayoutManager);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new OptionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                selectedOption = listOption.get(position);
                DesignerToast.Success(getActivity(), selectedOption, Gravity.CENTER, Toast.LENGTH_SHORT);
            }
        });

        btnDone.setOnClickListener(v -> {
            if (listener != null) {
                listener.onOptionSelected(selectedOption, POS);
            }
            MyBottomSheetDialogFragment bottomSheetFragment = (MyBottomSheetDialogFragment) getParentFragment();
            if (bottomSheetFragment != null) {
                bottomSheetFragment.dismiss();
            }
        });

        return view;
    }

    public void  onCloseDialog() {
        MyBottomSheetDialogFragment bottomSheetFragment = (MyBottomSheetDialogFragment) getParentFragment();
        if (bottomSheetFragment != null) {
            bottomSheetFragment.dismiss();
        }
    }


}
