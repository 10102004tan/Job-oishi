package com.example.joboishi.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.core.content.res.ResourcesCompat;

import com.example.joboishi.Api.ApiClient;
import com.example.joboishi.Api.ForgotPasswordApiResponse;
import com.example.joboishi.Api.UserApi;
import com.example.joboishi.Api.UserForgotPasswordRequest;
import com.example.joboishi.Api.UserVerifyTokenRequest;
import com.example.joboishi.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;


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
                Intent intent = getIntent();
                String email = intent.getStringExtra("email");

                String token = verifyToken.getText().toString();

                UserVerifyTokenRequest request = new UserVerifyTokenRequest(token,email);
                UserApi userApi = ApiClient.getUserAPI();
                Call<ForgotPasswordApiResponse> callUser = userApi.verifyPassword(request);
                callUser.enqueue(new Callback<ForgotPasswordApiResponse>() {
                    @Override
                    public void onResponse(Call<ForgotPasswordApiResponse> call, Response<ForgotPasswordApiResponse> response) {
                        if (response.isSuccessful()){
                            assert response.body() != null;
                            if (response.body().getStatus() == 200) {
                                MotionToast.Companion.createToast(VerifyTokenActivity.this, "Thành công",
                                        "Nhập mã xác thực thành công.",
                                        MotionToastStyle.SUCCESS,
                                        MotionToast.GRAVITY_BOTTOM,
                                        MotionToast.LONG_DURATION,
                                        ResourcesCompat.getFont(VerifyTokenActivity.this, R.font.helvetica_regular));

                                Intent intent = new Intent(VerifyTokenActivity.this, ResetPasswordActivity.class);
                                intent.putExtra("email",email);
                                startActivity(intent);
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<ForgotPasswordApiResponse> call, Throwable t) {

                    }
                });

            }
        });

    }
}
