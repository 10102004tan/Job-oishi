package com.example.joboishi.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.joboishi.Api.ApiClient;
import com.example.joboishi.Api.UserApi;
import com.example.joboishi.Api.UserApiResponse;
import com.example.joboishi.Api.UserLoginEmailRequest;
import com.example.joboishi.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

        // Hiển thị lỗi nếu các trường nhập liệu trống khi khởi tạo
        if (emailEditText.getText().toString().trim().isEmpty()) {
            emailErrorTextView.setVisibility(View.VISIBLE);
        }
        if (passwordEditText.getText().toString().trim().isEmpty()) {
            passwordErrorTextView.setVisibility(View.VISIBLE);
        }

        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

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
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().isEmpty()) {
                    passwordErrorTextView.setVisibility(View.VISIBLE);
                } else {
                    passwordErrorTextView.setVisibility(View.GONE);
                }
            }
        });

        TextView loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                // Kiểm tra nếu email hoặc mật khẩu trống
                if (email.isEmpty() || password.isEmpty()) {
                    // Hiển thị thông báo lỗi nếu có ô nhập liệu rỗng
                    Toast.makeText(LoginEmailActivity.this, "Vui lòng nhập email và mật khẩu", Toast.LENGTH_SHORT).show();
                } else {
                    // Xử lý logic đăng nhập nếu không có lỗi
                    UserLoginEmailRequest request = new UserLoginEmailRequest(email, password);
                    UserApi userApi = ApiClient.getUserAPI();
                    Call<UserApiResponse> callUser = userApi.loginUser(request);
                    callUser.enqueue(new Callback<UserApiResponse>() {
                        @Override
                        public void onResponse(Call<UserApiResponse> call, Response<UserApiResponse> response) {
                            if (response.isSuccessful()) {
                                UserApiResponse userApiResponse = response.body();
                                String userEmail = userApiResponse.getEmail();
                                int userId = userApiResponse.getId();

                                // Lưu email người dùng vào SharedPreferences
                                SharedPreferences sharedPref = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putString("user_email", userEmail);
                                editor.putString("user_id", userId + "");
                                editor.apply();

                                // Log ra thông tin đã lưu
                                // Log.d("UserInfo", "Email Người Dùng: " + userEmail);
                                // Chuyển sang màn hình RegisterMajorActivity
                                Intent intent = new Intent(LoginEmailActivity.this, ProfileActivity.class);
                                startActivity(intent);
                            } else {
                                // Xử lý lỗi nếu có
                                Log.e("LoginError", "Đăng nhập không thành công");
                                Toast.makeText(LoginEmailActivity.this, "Đăng nhập không thành công. Vui lòng thử lại sau.", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<UserApiResponse> call, Throwable t) {
                            // Ghi nhật ký lỗi
                            Log.e("LoginError", "Đăng nhập không thành công: " + t.getMessage());
                            // Thông báo cho người dùng
                            Toast.makeText(LoginEmailActivity.this, "Đăng nhập không thành công. Vui lòng thử lại sau.", Toast.LENGTH_SHORT).show();
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
