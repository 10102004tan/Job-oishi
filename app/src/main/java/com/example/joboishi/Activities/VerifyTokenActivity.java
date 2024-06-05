package com.example.joboishi.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.joboishi.R;


public class VerifyTokenActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verify_token);

        EditText verifyToken = findViewById(R.id.verify_token);
        Button requestVerificationCode = findViewById(R.id.request_verification_code);

        requestVerificationCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}
