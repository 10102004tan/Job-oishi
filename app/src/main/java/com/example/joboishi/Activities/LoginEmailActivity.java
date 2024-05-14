package com.example.joboishi.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.joboishi.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class LoginEmailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_email);

        TextView signUpTextView = findViewById(R.id.sign_up);
        signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginEmailActivity.this, RegisterUserActivity.class);
                startActivity(intent);
            }
        });
    }
}