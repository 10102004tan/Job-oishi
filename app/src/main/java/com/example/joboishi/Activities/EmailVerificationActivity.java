package com.example.joboishi.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.example.joboishi.Api.ApiClient;
import com.example.joboishi.Api.ForgotPasswordApiResponse;
import com.example.joboishi.Api.UserApi;
import com.example.joboishi.Api.UserApiResponse;
import com.example.joboishi.Api.UserVerifyTokenRequest;
import com.example.joboishi.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class EmailVerificationActivity extends AppCompatActivity {

    private EditText emailVerifyEditText;
    private Button requestEmailVerifyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.email_verification);

        emailVerifyEditText = findViewById(R.id.email_verify);
        requestEmailVerifyButton = findViewById(R.id.request_email_verify);

        requestEmailVerifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                String email = intent.getStringExtra("email");

                String token = emailVerifyEditText.getText().toString().trim();

                if (token.isEmpty()) {
                    MotionToast.Companion.createToast(EmailVerificationActivity.this, "Lỗi",
                            "Vui lòng nhập mã xác thực",
                            MotionToastStyle.ERROR,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(EmailVerificationActivity.this, R.font.helvetica_regular));
                } else {
                    // Gọi API để xác nhận mã xác thực
                    UserVerifyTokenRequest request = new UserVerifyTokenRequest(token, email);
                    UserApi userApi = ApiClient.getUserAPI();
                    Call<ForgotPasswordApiResponse> callUser = userApi.verifyPassword(request);
                    callUser.enqueue(new Callback<ForgotPasswordApiResponse>() {
                        @Override
                        public void onResponse(@NonNull Call<ForgotPasswordApiResponse> call, @NonNull Response<ForgotPasswordApiResponse> response) {
                            if (response.isSuccessful()) {
                                if (response.body().getStatus() == 200) {
                                    MotionToast.Companion.createToast(EmailVerificationActivity.this, "Thành công",
                                            "Mã xác thực thành công.",
                                            MotionToastStyle.SUCCESS,
                                            MotionToast.GRAVITY_BOTTOM,
                                            MotionToast.LONG_DURATION,
                                            ResourcesCompat.getFont(EmailVerificationActivity.this, R.font.helvetica_regular));

                                    // Chuyển sang màn hình tiếp theo
                                    Intent intent = new Intent(EmailVerificationActivity.this, RegisterMajorActivity.class);
                                    intent.putExtra("email",email);
                                    startActivity(intent);
                                }
                            } else {
                                MotionToast.Companion.createToast(EmailVerificationActivity.this, "Lỗi",
                                        "Mã xác thực không chính xác. Vui lòng thử lại.",
                                        MotionToastStyle.ERROR,
                                        MotionToast.GRAVITY_BOTTOM,
                                        MotionToast.LONG_DURATION,
                                        ResourcesCompat.getFont(EmailVerificationActivity.this, R.font.helvetica_regular));
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<ForgotPasswordApiResponse> call, @NonNull Throwable t) {
                            Log.e("VerifyTokenError", "Xác nhận mã không thành công: " + t.getMessage());
                            MotionToast.Companion.createToast(EmailVerificationActivity.this, "Lỗi",
                                    "Xác nhận mã không thành công. Vui lòng thử lại sau.",
                                    MotionToastStyle.ERROR,
                                    MotionToast.GRAVITY_BOTTOM,
                                    MotionToast.LONG_DURATION,
                                    ResourcesCompat.getFont(EmailVerificationActivity.this, R.font.helvetica_regular));
                        }
                    });
                }
            }
        });
    }
}
