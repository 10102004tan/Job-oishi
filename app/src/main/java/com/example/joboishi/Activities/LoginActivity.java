package com.example.joboishi.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {
    CallbackManager callbackManager;
    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseDatabase database;
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
        database = FirebaseDatabase.getInstance();

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
                            updateUserInformation(user);
                        }
                    } else {
                        // Nếu đăng nhập thất bại, hiển thị thông báo tới người dùng.
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        Toast.makeText(LoginActivity.this, "Xác thực thất bại.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateUserInformation(FirebaseUser user) {
        String currentUserUID = user.getUid();
        DatabaseReference userRef = database.getReference("users").child(currentUserUID);
        String email = user.getEmail();
        String fullname = user.getDisplayName();
        String number = user.getPhoneNumber();

        userRef.child("fullname").setValue(fullname);
        userRef.child("number").setValue(number);
        userRef.child("email").setValue(email);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("fullname")) {
                    Intent intent = new Intent(LoginActivity.this, RegisterMajorActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, "Lỗi: Thông tin người dùng không đầy đủ.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Database error: " + databaseError.getMessage());
            }
        });
    }
}
