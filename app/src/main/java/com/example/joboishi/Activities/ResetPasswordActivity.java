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
import com.example.joboishi.Api.UserResetPasswordRequest;
import com.example.joboishi.R;

import java.util.Objects;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class ResetPasswordActivity extends AppCompatActivity {

    private static final Pattern PASSWORD_UPPERCASE = Pattern.compile(".*[A-Z].*");
    private static final Pattern PASSWORD_LOWERCASE = Pattern.compile(".*[a-z].*");
    private static final Pattern PASSWORD_DIGIT = Pattern.compile(".*[0-9].*");
    private static final Pattern PASSWORD_SPECIAL = Pattern.compile(".*[@#$%^&+=].*");
    private static final Pattern PASSWORD_LENGTH = Pattern.compile(".{8,}");

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

                Intent intent = getIntent();
                String email = intent.getStringExtra("email");

                if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                    MotionToast.Companion.createToast(ResetPasswordActivity.this,
                            "Lỗi",
                            "Vui lòng điền vào tất cả các trường",
                            MotionToastStyle.ERROR,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(ResetPasswordActivity.this, R.font.helvetica_regular));
                } else if (!PASSWORD_LENGTH.matcher(newPassword).matches()) {
                    MotionToast.Companion.createToast(ResetPasswordActivity.this,
                            "Lỗi",
                            "Mật khẩu phải có ít nhất 8 ký tự",
                            MotionToastStyle.ERROR,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(ResetPasswordActivity.this, R.font.helvetica_regular));
                } else if (!PASSWORD_UPPERCASE.matcher(newPassword).matches()) {
                    MotionToast.Companion.createToast(ResetPasswordActivity.this,
                            "Lỗi",
                            "Mật khẩu phải có ít nhất 1 chữ cái viết hoa",
                            MotionToastStyle.ERROR,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(ResetPasswordActivity.this, R.font.helvetica_regular));
                } else if (!PASSWORD_LOWERCASE.matcher(newPassword).matches()) {
                    MotionToast.Companion.createToast(ResetPasswordActivity.this,
                            "Lỗi",
                            "Mật khẩu phải có ít nhất 1 chữ cái thường",
                            MotionToastStyle.ERROR,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(ResetPasswordActivity.this, R.font.helvetica_regular));
                } else if (!PASSWORD_DIGIT.matcher(newPassword).matches()) {
                    MotionToast.Companion.createToast(ResetPasswordActivity.this,
                            "Lỗi",
                            "Mật khẩu phải có ít nhất 1 chữ số",
                            MotionToastStyle.ERROR,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(ResetPasswordActivity.this, R.font.helvetica_regular));
                } else if (!PASSWORD_SPECIAL.matcher(newPassword).matches()) {
                    MotionToast.Companion.createToast(ResetPasswordActivity.this,
                            "Lỗi",
                            "Mật khẩu phải có ít nhất 1 ký tự đặc biệt (@#$%^&+=)",
                            MotionToastStyle.ERROR,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(ResetPasswordActivity.this, R.font.helvetica_regular));
                } else if (!newPassword.equals(confirmPassword)) {
                    MotionToast.Companion.createToast(ResetPasswordActivity.this,
                            "Lỗi",
                            "Mật khẩu không khớp",
                            MotionToastStyle.ERROR,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(ResetPasswordActivity.this, R.font.helvetica_regular));
                } else {
                    UserResetPasswordRequest request = new UserResetPasswordRequest(email, newPassword);
                    UserApi userApi = ApiClient.getUserAPI();
                    Call<ForgotPasswordApiResponse> callUser = userApi.resetPassword(request);
                    callUser.enqueue(new Callback<ForgotPasswordApiResponse>() {
                        @Override
                        public void onResponse(@NonNull Call<ForgotPasswordApiResponse> call, @NonNull Response<ForgotPasswordApiResponse> response) {
                            if (response.isSuccessful() && Objects.requireNonNull(response.body()).getStatus() == 200) {
                                Log.d("TESST",response.body().getStatus() + "");
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
                                Log.d("TESST",response.body().getStatus() + "");
                                MotionToast.Companion.createToast(ResetPasswordActivity.this, "Lỗi",
                                        "Có lỗi xảy ra. Vui lòng thử lại sau.",
                                        MotionToastStyle.ERROR,
                                        MotionToast.GRAVITY_BOTTOM,
                                        MotionToast.LONG_DURATION,
                                        ResourcesCompat.getFont(ResetPasswordActivity.this, R.font.helvetica_regular));
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<ForgotPasswordApiResponse> call, @NonNull Throwable t) {
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
