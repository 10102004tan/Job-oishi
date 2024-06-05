package com.example.joboishi.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.example.joboishi.Api.ApiClient;
import com.example.joboishi.Api.ForgotPasswordApiResponse;
import com.example.joboishi.Api.UserApi;
import com.example.joboishi.Api.UserApiResponse;
import com.example.joboishi.Api.UserForgotPasswordRequest;
import com.example.joboishi.R;

import java.io.IOException;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class ForgotPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);

        EditText emailEditText = findViewById(R.id.email);
        TextView emailErrorTextView = findViewById(R.id.email_error);
        Button resetPasswordButton = findViewById(R.id.reset_password_button);

        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();

                // Kiểm tra nếu email trống
                if (email.isEmpty()) {
                    emailErrorTextView.setVisibility(View.VISIBLE);
                } else {
                    emailErrorTextView.setVisibility(View.GONE);

                    // Gửi yêu cầu đặt lại mật khẩu đến máy chủ
                    //UserForgotPasswordRequest request = new UserForgotPasswordRequest(email);


                    UserApi userApi = ApiClient.getUserAPI();
                    Call<ForgotPasswordApiResponse> callUser = userApi.forgotPassword(email);
                    callUser.enqueue(new Callback<ForgotPasswordApiResponse>() {
                        @Override
                        public void onResponse(@NonNull Call<ForgotPasswordApiResponse> call, @NonNull Response<ForgotPasswordApiResponse> response) {
                            if (response.isSuccessful()) {
                                Log.d("TESST", response.body().getMessage() + "");
                                MotionToast.Companion.createToast(ForgotPasswordActivity.this, "Thành công",
                                        "Liên kết đặt lại mật khẩu đã được gửi đến email của bạn.",
                                        MotionToastStyle.SUCCESS,
                                        MotionToast.GRAVITY_BOTTOM,
                                        MotionToast.LONG_DURATION,
                                        ResourcesCompat.getFont(ForgotPasswordActivity.this, R.font.helvetica_regular));
                                //chuyển sang màn hình xác nhận mã
                                Intent intent = new Intent(ForgotPasswordActivity.this, VerifyTokenActivity.class);
                                intent.putExtra("email",email);
                                startActivity(intent);

                            } else {
                                try {
                                    String errorBody = response.errorBody().string();
                                    Log.e("ForgotPassword", "Server error: " + errorBody);
                                    MotionToast.Companion.createToast(ForgotPasswordActivity.this, "Lỗi",
                                            "Có lỗi xảy ra. Vui lòng thử lại sau.",
                                            MotionToastStyle.ERROR,
                                            MotionToast.GRAVITY_BOTTOM,
                                            MotionToast.LONG_DURATION,
                                            ResourcesCompat.getFont(ForgotPasswordActivity.this, R.font.helvetica_regular));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<ForgotPasswordApiResponse> call, @NonNull Throwable t) {
                            Log.e("ForgotPassword", "Network error: " + t.getMessage());
                            MotionToast.Companion.createToast(ForgotPasswordActivity.this, "Lỗi",
                                    "Đã xảy ra lỗi. Vui lòng thử lại sau.",
                                    MotionToastStyle.ERROR,
                                    MotionToast.GRAVITY_BOTTOM,
                                    MotionToast.LONG_DURATION,
                                    ResourcesCompat.getFont(ForgotPasswordActivity.this, R.font.helvetica_regular));
                        }
                    });
                }
            }
        });
    }
}
