package com.example.joboishi.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.example.joboishi.Api.ApiClient;
import com.example.joboishi.Api.UserApi;
import com.example.joboishi.Api.UserApiResponse;
import com.example.joboishi.Api.UserRegisterEmailRequest;
import com.example.joboishi.R;

import java.util.regex.Pattern;

import io.github.cutelibs.cutedialog.CuteDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

public class RegisterUserActivity extends AppCompatActivity {

    private static final Pattern PASSWORD_UPPERCASE = Pattern.compile(".*[A-Z].*");
    private static final Pattern PASSWORD_LOWERCASE = Pattern.compile(".*[a-z].*");
    private static final Pattern PASSWORD_DIGIT = Pattern.compile(".*[0-9].*");
    private static final Pattern PASSWORD_SPECIAL = Pattern.compile(".*[@#$%^&+=].*");
    private static final Pattern PASSWORD_LENGTH = Pattern.compile(".{8,}");

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
                    MotionToast.Companion.createToast(RegisterUserActivity.this,
                            "Lỗi",
                            "Vui lòng điền vào tất cả các trường",
                            MotionToastStyle.ERROR,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(RegisterUserActivity.this, R.font.helvetica_regular));
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches() || !email.endsWith("@gmail.com")) {
                    MotionToast.Companion.createToast(RegisterUserActivity.this,
                            "Lỗi",
                            "Vui lòng nhập @gmail.com",
                            MotionToastStyle.ERROR,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(RegisterUserActivity.this, R.font.helvetica_regular));
                } else if (!PASSWORD_LENGTH.matcher(password).matches()) {
                    MotionToast.Companion.createToast(RegisterUserActivity.this,
                            "Lỗi",
                            "Mật khẩu phải có ít nhất 8 ký tự",
                            MotionToastStyle.ERROR,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(RegisterUserActivity.this, R.font.helvetica_regular));
                } else if (!PASSWORD_UPPERCASE.matcher(password).matches()) {
                    MotionToast.Companion.createToast(RegisterUserActivity.this,
                            "Lỗi",
                            "Mật khẩu phải có ít nhất 1 chữ cái viết hoa",
                            MotionToastStyle.ERROR,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(RegisterUserActivity.this, R.font.helvetica_regular));
                } else if (!PASSWORD_LOWERCASE.matcher(password).matches()) {
                    MotionToast.Companion.createToast(RegisterUserActivity.this,
                            "Lỗi",
                            "Mật khẩu phải có ít nhất 1 chữ cái thường",
                            MotionToastStyle.ERROR,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(RegisterUserActivity.this, R.font.helvetica_regular));
                } else if (!PASSWORD_DIGIT.matcher(password).matches()) {
                    MotionToast.Companion.createToast(RegisterUserActivity.this,
                            "Lỗi",
                            "Mật khẩu phải có ít nhất 1 chữ số",
                            MotionToastStyle.ERROR,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(RegisterUserActivity.this, R.font.helvetica_regular));
                } else if (!PASSWORD_SPECIAL.matcher(password).matches()) {
                    MotionToast.Companion.createToast(RegisterUserActivity.this,
                            "Lỗi",
                            "Mật khẩu phải có ít nhất 1 ký tự đặc biệt (@#$%^&+=)",
                            MotionToastStyle.ERROR,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(RegisterUserActivity.this, R.font.helvetica_regular));
                } else if (!password.equals(confirmPassword)) {
                    MotionToast.Companion.createToast(RegisterUserActivity.this,
                            "Lỗi",
                            "Mật khẩu không khớp",
                            MotionToastStyle.ERROR,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(RegisterUserActivity.this, R.font.helvetica_regular));
                } else {
                    Log.d("register", "register");
                    UserRegisterEmailRequest request = new UserRegisterEmailRequest(firstname, lastname, email, password);
                    UserApi userApi = ApiClient.getUserAPI();
                    Call<UserApiResponse> callUser = userApi.registerUser(request);
                    callUser.enqueue(new Callback<UserApiResponse>() {
                        @Override
                        public void onResponse(Call<UserApiResponse> call, Response<UserApiResponse> response) {
                            Log.d("response", response.toString());
                            if (response.isSuccessful()) {
                                CuteDialog.withIcon
                                        dialog =
                                        new CuteDialog.withIcon(RegisterUserActivity.this)
                                                .setIcon(R.drawable.logo)
                                                .setTitle("Thông báo")
                                                .hideCloseIcon(true)
                                                .setDescription("Đăng kí tài khoản thành công")
                                                .setNegativeButtonText("Đăng nhập", v1 -> {
                                                    Intent intent = new Intent(RegisterUserActivity.this, LoginEmailActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                });
                                dialog.setCanceledOnTouchOutside(false);
                                dialog.show();

                            } else {
                                MotionToast.Companion.createToast(RegisterUserActivity.this,
                                        "Thất bại",
                                        "Emai đã tồn tại !!!",
                                        MotionToastStyle.ERROR,
                                        MotionToast.GRAVITY_BOTTOM,
                                        MotionToast.LONG_DURATION,
                                        ResourcesCompat.getFont(RegisterUserActivity.this, R.font.helvetica_regular));
                            }
                        }

                        @Override
                        public void onFailure(Call<UserApiResponse> call, Throwable t) {
                            Log.d("Register Error", t.toString());
                            MotionToast.Companion.createToast(RegisterUserActivity.this,
                                    "Lỗi",
                                    "Đăng ký tài khoản thất bại",
                                    MotionToastStyle.ERROR,
                                    MotionToast.GRAVITY_BOTTOM,
                                    MotionToast.LONG_DURATION,
                                    ResourcesCompat.getFont(RegisterUserActivity.this, R.font.helvetica_regular));
                        }
                    });
                }
            }
        });
    }
}
