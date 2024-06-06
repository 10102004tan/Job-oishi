package com.example.joboishi.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.example.joboishi.Api.ApiClient;
import com.example.joboishi.Api.ForgotPasswordApiResponse;
import com.example.joboishi.Api.UserApi;
import com.example.joboishi.Api.UserApiResponse;
import com.example.joboishi.Api.UserRequest;
import com.example.joboishi.Api.UserVerifyTokenRequest;
import com.example.joboishi.R;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class EmailVerificationActivity extends AppCompatActivity {

    private EditText emailVerifyEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.email_verification);

        emailVerifyEditText = findViewById(R.id.email_verify);
        Button requestEmailVerifyButton = findViewById(R.id.request_email_verify);

        requestEmailVerifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                String email = intent.getStringExtra("email");
                int isFirstLogin = intent.getIntExtra("is_first_login", 0);
                int userId = intent.getIntExtra("user_id", 0);





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
                                assert response.body() != null;
                                if (response.body().getStatus() == 200) {
                                    MotionToast.Companion.createToast(EmailVerificationActivity.this, "Thành công",
                                            "Nhập mã xác thực thành công.",
                                            MotionToastStyle.SUCCESS,
                                            MotionToast.GRAVITY_BOTTOM,
                                            MotionToast.LONG_DURATION,
                                            ResourcesCompat.getFont(EmailVerificationActivity.this, R.font.helvetica_regular));

                                    if (isFirstLogin == 1) {
                                        updateFirstLogin(userId);

                                        Intent intent = new Intent(EmailVerificationActivity.this, RegisterMajorActivity.class);
                                        intent.putExtra("caller", "LoginEmailActivity");
                                        startActivity(intent);
                                    }else {
                                        Intent intent = new Intent(EmailVerificationActivity.this, HomeActivity.class);
                                        startActivity(intent);
                                    }
                                }else {
                                    MotionToast.Companion.createToast(EmailVerificationActivity.this, "Lỗi",
                                            "Mã xác thực không chính xác. Vui lòng thử lại.",
                                            MotionToastStyle.ERROR,
                                            MotionToast.GRAVITY_BOTTOM,
                                            MotionToast.LONG_DURATION,
                                            ResourcesCompat.getFont(EmailVerificationActivity.this, R.font.helvetica_regular));
                                }


                            } else {
                                MotionToast.Companion.createToast(EmailVerificationActivity.this, "Lỗi",
                                        "Đã xảy ra lỗi. Vui lòng thử lại.",
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

    public void updateFirstLogin(int userId) {
        UserRequest userUpdateRequest = new UserRequest();
        userUpdateRequest.setIs_first_login(0);

        UserApi userApi = ApiClient.getUserAPI();
        Call<UserApiResponse> call = userApi.updateUserInfo(userId, userUpdateRequest);
        call.enqueue(new Callback<UserApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<UserApiResponse> call, @NonNull Response<UserApiResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    updateVerifyCode(userId);
                } else {
                    Log.d("UPDATE_USER_ERROR", "ERROR");
                    // Toast.makeText(EditProfileActivity.this, "Đã xảy ra lỗi trong quá trình cập nhật thông tin", Toast.LENGTH_SHORT).show();
                    MotionToast.Companion.createToast(EmailVerificationActivity.this, "Thất bại",
                            "Đã xảy ra lỗi trong quá trình cập nhật thông tin",
                            MotionToastStyle.ERROR,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(EmailVerificationActivity.this, R.font.helvetica_regular));
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserApiResponse> call, @NonNull Throwable t) {
                Log.d("UPDATE_USER_ERROR", t.toString());
                // Toast.makeText(EditProfileActivity.this, "Đã xảy ra lỗi trong quá trình cập nhật thông tin", Toast.LENGTH_SHORT).show();
                MotionToast.Companion.createToast(EmailVerificationActivity.this, "Thất bại",
                        "Đã xảy ra lỗi trong quá trình cập nhật thông tin",
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(EmailVerificationActivity.this, R.font.helvetica_regular));
            }
        });
    }


    public void updateVerifyCode(int userId) {
        UserRequest userUpdateRequest = new UserRequest();
        userUpdateRequest.setVerify_token_code("0");

        UserApi userApi = ApiClient.getUserAPI();
        Call<UserApiResponse> call = userApi.updateUserInfo(userId, userUpdateRequest);
        call.enqueue(new Callback<UserApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<UserApiResponse> call, @NonNull Response<UserApiResponse> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    // Toast.makeText(EmailVerificationActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                    MotionToast.Companion.createToast(EmailVerificationActivity.this, "Thành công",
                            "Đăng nhập thành công",
                            MotionToastStyle.SUCCESS,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(EmailVerificationActivity.this, R.font.helvetica_regular));
                } else {
                    Log.d("UPDATE_USER_ERROR", "ERROR");
                    // Toast.makeText(EmailVerificationActivity.this, "Đã xảy ra lỗi trong quá trình cập nhật thông tin", Toast.LENGTH_SHORT).show();
                    MotionToast.Companion.createToast(EmailVerificationActivity.this, "Thất bại",
                            "Đăng nhập thất bại",
                            MotionToastStyle.ERROR,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(EmailVerificationActivity.this, R.font.helvetica_regular));
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserApiResponse> call, @NonNull Throwable t) {
                Log.d("UPDATE_USER_ERROR", t.toString());
                // Toast.makeText(EmailVerificationActivity.this, "Đã xảy ra lỗi trong quá trình cập nhật thông tin", Toast.LENGTH_SHORT).show();
                MotionToast.Companion.createToast(EmailVerificationActivity.this, "Thất bại",
                        "Đã xảy ra lỗi trong quá trình cập nhật thông tin",
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(EmailVerificationActivity.this, R.font.helvetica_regular));
            }
        });
    }
}
