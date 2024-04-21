package com.example.joboishi.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.joboishi.Fragments.BottomSheetDialog.FilterJobFragment;
import com.example.joboishi.Fragments.MyBottomSheetDialogFragment;
import com.example.joboishi.Fragments.MyJobFragment;
import com.example.joboishi.R;

public class TestActivity extends AppCompatActivity {

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            MyBottomSheetDialogFragment dialogFragment = MyBottomSheetDialogFragment.newInstance();
            if (v.getId() == R.id.btnShow){
                dialogFragment.setFragment(new MyJobFragment());
            }else if (v.getId() == R.id.btn2){
                dialogFragment.setFragment(new FilterJobFragment());
            }
            dialogFragment.show(getSupportFragmentManager(),dialogFragment.getTag());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Button btn = findViewById(R.id.btnShow);
        Button btn2 = findViewById(R.id.btn2);

        //
        btn.setOnClickListener(onClickListener);
        btn2.setOnClickListener(onClickListener);
    }
}