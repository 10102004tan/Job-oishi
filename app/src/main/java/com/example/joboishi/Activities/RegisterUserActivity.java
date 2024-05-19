package com.example.joboishi.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.joboishi.Api.ApiClient;
import com.example.joboishi.Api.UserApi;
import com.example.joboishi.Api.UserApiResponse;
import com.example.joboishi.Api.UserRegisterEmailRequest;
import com.example.joboishi.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterUserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_user);

        EditText firstnameEditText = findViewById(R.id.firstname);
        EditText lastnameEditText = findViewById(R.id.lastname);
        EditText emailEditText = findViewById(R.id.email);
        EditText passwordEditText = findViewById(R.id.password);
        EditText confirmPasswordEditText = findViewById(R.id.confirm_password);
        Button registerButton = findViewById(R.id.register_button);
        TextView loginTextView = findViewById(R.id.login_textview);

        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterUserActivity.this, LoginEmailActivity.class);
                startActivity(intent);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstname = firstnameEditText.getText().toString().trim();
                String lastname = lastnameEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                String confirmPassword = confirmPasswordEditText.getText().toString().trim();

                if (firstname.isEmpty() || lastname.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(RegisterUserActivity.this, "Vui lòng điền vào tất cả các trường", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(confirmPassword)) {
                    Toast.makeText(RegisterUserActivity.this, "Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
                } else {

                    UserRegisterEmailRequest request = new UserRegisterEmailRequest(firstname, lastname, email, password);
                    // Thực hiện đăng ký tài khoản (gửi dữ liệu lên server)
                    UserApi userApi = ApiClient.getUserAPI();
                    Call<UserApiResponse> callUser = userApi.registerUser(request);
                    callUser.enqueue(new Callback<UserApiResponse>() {
                        @Override
                        public void onResponse(@NonNull Call<UserApiResponse> call, @NonNull Response<UserApiResponse> response) {
                            Log.d("respone", response.toString());
                           Intent intent = new Intent(RegisterUserActivity.this, LoginEmailActivity.class);
                           startActivity(intent);
                           finish();
                        }

                        @Override
                        public void onFailure(@NonNull Call<UserApiResponse> call, @NonNull Throwable t) {
                            Log.d("respone", t.toString());
                        }
                    });
                }
            }
        });
    }
}