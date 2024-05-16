package com.example.joboishi.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
                        public void onResponse(Call<UserApiResponse> call, Response<UserApiResponse> response) {
                            Log.d("respone", response.toString());
                            //Toast.makeText(RegisterUserActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<UserApiResponse> call, Throwable t) {
                            Log.d("respone", t.toString());
                        }
                    });
                }
            }
        });
    }
}