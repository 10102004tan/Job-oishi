package com.example.joboishi.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.joboishi.Api.ApiClient;
import com.example.joboishi.Api.UserApi;
import com.example.joboishi.Api.UserFacebookLoginRequest;
import com.example.joboishi.Api.UserLoginEmailRequest;
import com.example.joboishi.Api.UserApiResponse;
import com.example.joboishi.Api.UserRegisterEmailRequest;
import com.example.joboishi.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import www.sanju.motiontoast.MotionToast;
import www.sanju.motiontoast.MotionToastStyle;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {
    CallbackManager callbackManager;
    FirebaseAuth mAuth;
    FirebaseUser user;
    public String TAG = "uilover";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        // Chuyển qua mh đăng nhập bằng email
        TextView loginEmail = findViewById(R.id.login_email);
        loginEmail.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, LoginEmailActivity.class);
            startActivity(intent);
        });

        mAuth = FirebaseAuth.getInstance();

        // Khởi tạo nút đăng nhập Facebook
        callbackManager = CallbackManager.Factory.create();
        LoginButton facebookButton = findViewById(R.id.facebook);
        facebookButton.setReadPermissions(Arrays.asList("email", "public_profile"));

        facebookButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                // Xử lý sự kiện huỷ bỏ
                Log.d(TAG, "Facebook login canceled.");
            }

            @Override
            public void onError(FacebookException exception) {
                // Xử lý sự kiện lỗi
                Log.e(TAG, "Facebook login error: " + exception.getMessage());
            }
        });
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Đăng nhập thành công, cập nhật UI với thông tin người dùng đã đăng nhập
                        user = mAuth.getCurrentUser();
                        if (user != null) {
                            // Gửi dữ liệu đăng nhập lên API
                            sendUserInfoToApi(user.getDisplayName(), user.getEmail(), ""); // Không cần mật khẩu cho đăng nhập bằng Facebook
                            MotionToast.Companion.createToast(LoginActivity.this, "Thành công",
                                    "Đã đăng nhập thành công.",
                                    MotionToastStyle.SUCCESS,
                                    MotionToast.GRAVITY_BOTTOM,
                                    MotionToast.LONG_DURATION,
                                    ResourcesCompat.getFont(LoginActivity.this, R.font.helvetica_regular));
                        }
                    } else {
                        // Nếu đăng nhập thất bại, hiển thị thông báo tới người dùng.
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        MotionToast.Companion.createToast(LoginActivity.this, "Lỗi",
                                "Đã đăng nhập thất bại. Vui lòng thử lại sau.",
                                MotionToastStyle.ERROR,
                                MotionToast.GRAVITY_BOTTOM,
                                MotionToast.LONG_DURATION,
                                ResourcesCompat.getFont(LoginActivity.this, R.font.helvetica_regular));
                    }
                });
    }

    //gửi dữ liệu lên api
    private void sendUserInfoToApi(String fullname ,String email, String password) {
        // Lấy instance của interface UserApi từ ApiClient
        UserApi userApi = ApiClient.getUserAPI();

        // Tạo đối tượng UserLoginEmailRequest với email và mật khẩu
        UserFacebookLoginRequest user = new UserFacebookLoginRequest(fullname,email, password);

        // Gọi phương thức loginUser và truyền đối tượng user vào
        Call<UserApiResponse> call = userApi.registerFacebookUser(user);
        call.enqueue(new Callback<UserApiResponse>() {
            @Override
            public void onResponse(Call<UserApiResponse> call, Response<UserApiResponse> response) {
                if (response.isSuccessful()) {
                    // Xử lý thành công, ví dụ: hiển thị thông báo cho người dùng
                    MotionToast.Companion.createToast(LoginActivity.this, "Thành công",
                            "Đã đăng nhập thành công.",
                            MotionToastStyle.SUCCESS,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(LoginActivity.this, R.font.helvetica_regular));

                    Intent intent = new Intent(LoginActivity.this, RegisterMajorActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    // Xử lý lỗi, ví dụ: hiển thị thông báo lỗi
                    MotionToast.Companion.createToast(LoginActivity.this, "Lỗi",
                            "Đã đăng nhập thất bại. Vui lòng thử lại sau.",
                            MotionToastStyle.ERROR,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(LoginActivity.this, R.font.helvetica_regular));
                }
            }
            @Override
            public void onFailure(Call<UserApiResponse> call, Throwable t) {
                // Xử lý lỗi khi gọi API, ví dụ: hiển thị thông báo lỗi
                Toast.makeText(LoginActivity.this, "Đã xảy ra lỗi khi gửi yêu cầu đăng nhập!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
