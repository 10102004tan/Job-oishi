package com.example.joboishi.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.example.joboishi.Api.ApiClient;
import com.example.joboishi.Api.UserApi;
import com.example.joboishi.Api.UserApiResponse;
import com.example.joboishi.Api.UserLoginEmailRequest;
import com.example.joboishi.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class LoginEmailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_email);

        EditText emailEditText = findViewById(R.id.email);
        EditText passwordEditText = findViewById(R.id.password);
        TextView emailErrorTextView = findViewById(R.id.email_error);
        TextView passwordErrorTextView = findViewById(R.id.password_error);
        TextView signupTextView = findViewById(R.id.sign_up);
        CheckBox showPasswordCheckBox = findViewById(R.id.show_password);
        TextView forgotPassword = findViewById(R.id.forgot_password);

        // Hiển thị lỗi nếu các trường nhập liệu trống khi khởi tạo
        if (emailEditText.getText().toString().trim().isEmpty()) {
            emailErrorTextView.setVisibility(View.VISIBLE);
        }
        if (passwordEditText.getText().toString().trim().isEmpty()) {
            passwordErrorTextView.setVisibility(View.VISIBLE);
        }

        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().isEmpty()) {
                    emailErrorTextView.setVisibility(View.VISIBLE);
                } else {
                    emailErrorTextView.setVisibility(View.GONE);
                }
            }
        });

        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().isEmpty()) {
                    passwordErrorTextView.setVisibility(View.VISIBLE);
                } else {
                    passwordErrorTextView.setVisibility(View.GONE);
                }
            }
        });

        //Hiển thị mật khẩu
        showPasswordCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Hiển thị mật khẩu, là kiểu dữ liệu cho phép hiển thị mật khẩu
                passwordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            } else {
                // Ẩn mật khẩu
                passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
            // Di chuyển con trỏ đến cuối văn bản
            passwordEditText.setSelection(passwordEditText.length());
        });


        //Quên mật khẩu
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginEmailActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });



        //ktra các trường khi nhập thiếu thông tin
        TextView loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                // Kiểm tra nếu email hoặc mật khẩu trống
                if (email.isEmpty() || password.isEmpty()) {
                    // Hiển thị thông báo lỗi nếu có ô nhập liệu rỗng
                    MotionToast.Companion.createToast(LoginEmailActivity.this, "Lỗi",
                            "Vui lòng nhập email và mật khẩu",
                            MotionToastStyle.ERROR,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(LoginEmailActivity.this, R.font.helvetica_regular));

                } else {
                    // Xử lý logic đăng nhập nếu không có lỗi
                    UserLoginEmailRequest request = new UserLoginEmailRequest(email, password);
                    UserApi userApi = ApiClient.getUserAPI();
                    Call<UserApiResponse> callUser = userApi.loginUser(request);
                    callUser.enqueue(new Callback<UserApiResponse>() {
                        @Override
                        public void onResponse(@NonNull Call<UserApiResponse> call, @NonNull Response<UserApiResponse> response) {
                            if (response.isSuccessful()) {
                                UserApiResponse userApiResponse = response.body();
                                assert userApiResponse != null;
                                String userEmail = userApiResponse.getEmail();
                                int userId = userApiResponse.getId();

                                // Lưu email người dùng vào SharedPreferences
                                SharedPreferences sharedPref = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                //lưu trữ dữ liệu trong SharedPreferences
                                editor.putString("user_email", userEmail);
                                editor.putInt("user_id", userId);
                                editor.apply();

                                // Log ra thông tin đã lưu
                                //Log.d("UserInfo", "Email Người Dùng: " + userEmail);

                                MotionToast.Companion.createToast(LoginEmailActivity.this, "Thành công",
                                        "Đã đăng nhập thành công.",
                                        MotionToastStyle.SUCCESS,
                                        MotionToast.GRAVITY_BOTTOM,
                                        MotionToast.LONG_DURATION,
                                        ResourcesCompat.getFont(LoginEmailActivity.this, R.font.helvetica_regular));

                                //chuyển sang màn hình nhập mã
                                Intent intent = new Intent(LoginEmailActivity.this, EmailVerificationActivity.class);
                                intent.putExtra("email", email);
                                startActivity(intent);
                            } else {
                                // Xử lý lỗi nếu có
                                Log.e("LoginError", "Đăng nhập không thành công");
                                MotionToast.Companion.createToast(LoginEmailActivity.this, "Lỗi",
                                        "Thông tin tài khoản không chính xác. Vui lòng thử lại sau",
                                        MotionToastStyle.ERROR,
                                        MotionToast.GRAVITY_BOTTOM,
                                        MotionToast.LONG_DURATION,
                                        ResourcesCompat.getFont(LoginEmailActivity.this, R.font.helvetica_regular));
                            }
                        }
                        @Override
                        public void onFailure(@NonNull Call<UserApiResponse> call, @NonNull Throwable t) {
                            // Ghi nhật ký lỗi
                            Log.e("LoginError", "Đăng nhập không thành công: " + t.getMessage());
                            // Thông báo cho người dùng
                            // Toast.makeText(LoginEmailActivity.this, "Đăng nhập không thành công. Vui lòng thử lại sau.", Toast.LENGTH_SHORT).show();
                            MotionToast.Companion.createToast(LoginEmailActivity.this, "Lỗi",
                                    "Đăng nhập không thành công. Vui lòng thử lại sau",
                                    MotionToastStyle.ERROR,
                                    MotionToast.GRAVITY_BOTTOM,
                                    MotionToast.LONG_DURATION,
                                    ResourcesCompat.getFont(LoginEmailActivity.this, R.font.helvetica_regular));
                        }
                    });
                }
            }
        });

        signupTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginEmailActivity.this, RegisterUserActivity.class);
                startActivity(intent);
            }
        });
    }
}
