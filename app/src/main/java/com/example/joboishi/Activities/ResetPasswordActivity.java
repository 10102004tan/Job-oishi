package com.example.joboishi.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.example.joboishi.Api.ApiClient;
import com.example.joboishi.Api.UserApi;
import com.example.joboishi.Api.UserApiResponse;
import com.example.joboishi.Api.UserResetPasswordRequest;
import com.example.joboishi.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class ResetPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password);

        EditText newPasswordEditText = findViewById(R.id.new_password);
        EditText confirmPasswordEditText = findViewById(R.id.confirm_password);
        Button resetPasswordButton = findViewById(R.id.reset_password_button);

        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPassword = newPasswordEditText.getText().toString().trim();
                String confirmPassword = confirmPasswordEditText.getText().toString().trim();

                // Kiểm tra nếu mật khẩu trống hoặc không khớp
                if (newPassword.isEmpty() || !newPassword.equals(confirmPassword)) {
                    MotionToast.Companion.createToast(ResetPasswordActivity.this, "Lỗi",
                            "Mật khẩu không khớp hoặc trống",
                            MotionToastStyle.ERROR,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(ResetPasswordActivity.this, R.font.helvetica_regular));
                } else {
                    // Gửi yêu cầu đặt lại mật khẩu đến máy chủ
                    UserResetPasswordRequest request = new UserResetPasswordRequest(newPassword);
                    UserApi userApi = ApiClient.getUserAPI();
                    Call<UserApiResponse> callUser = userApi.resetPassword(request);
                    callUser.enqueue(new Callback<UserApiResponse>() {
                        @Override
                        public void onResponse(@NonNull Call<UserApiResponse> call, @NonNull Response<UserApiResponse> response) {
                            if (response.isSuccessful()) {
                                MotionToast.Companion.createToast(ResetPasswordActivity.this, "Thành công",
                                        "Mật khẩu của bạn đã được đặt lại thành công.",
                                        MotionToastStyle.SUCCESS,
                                        MotionToast.GRAVITY_BOTTOM,
                                        MotionToast.LONG_DURATION,
                                        ResourcesCompat.getFont(ResetPasswordActivity.this, R.font.helvetica_regular));
                                // Chuyển về màn hình đăng nhập hoặc hoạt động khác
                                Intent intent = new Intent(ResetPasswordActivity.this, LoginEmailActivity.class);
                                startActivity(intent);
                            } else {
                                MotionToast.Companion.createToast(ResetPasswordActivity.this, "Lỗi",
                                        "Có lỗi xảy ra. Vui lòng thử lại sau.",
                                        MotionToastStyle.ERROR,
                                        MotionToast.GRAVITY_BOTTOM,
                                        MotionToast.LONG_DURATION,
                                        ResourcesCompat.getFont(ResetPasswordActivity.this, R.font.helvetica_regular));
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<UserApiResponse> call, @NonNull Throwable t) {
                            MotionToast.Companion.createToast(ResetPasswordActivity.this, "Lỗi",
                                    "Đã xảy ra lỗi. Vui lòng thử lại sau.",
                                    MotionToastStyle.ERROR,
                                    MotionToast.GRAVITY_BOTTOM,
                                    MotionToast.LONG_DURATION,
                                    ResourcesCompat.getFont(ResetPasswordActivity.this, R.font.helvetica_regular));
                        }
                    });
                }
            }
        });
    }
}
