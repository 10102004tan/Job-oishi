package com.example.joboishi.Activities;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.joboishi.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.facebook.FacebookSdk;
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
    private static final int RC_SIGN_IN = 123;
    private GoogleSignInClient mGoogleSignInClient;
    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseDatabase database;
    public String TAG = "uilover";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();


        //Login Facebook
        callbackManager = CallbackManager.Factory.create();
        FacebookSdk.sdkInitialize(getApplicationContext());
        LoginButton facebook = (LoginButton) findViewById(R.id.facebook);
        facebook.setReadPermissions("email", "public_profile");


        facebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });


        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile"));
            }
        });
    }


    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            user = mAuth.getCurrentUser();
                            assert user != null;
                            String currentUserUID = user.getUid();
                            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users")
                                    .child(currentUserUID);
                            String email = user.getEmail();
                            String fullname = user.getDisplayName();
                            String number = user.getPhoneNumber();
                            userRef.child("fullname").setValue(fullname);
                            userRef.child("number").setValue(number);
                            userRef.child("email").setValue(email);
                            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    // Kiểm tra xem nếu người dùng có fullname trong cơ sở dữ liệu
                                    if (dataSnapshot.hasChild("fullname")) {
                                        // Lấy fullname từ cơ sở dữ liệu
                                        String fullname = dataSnapshot.child("fullname").getValue(String.class);

                                        // Truyền fullname sang MainActivity
                                        Intent intent = new Intent(LoginActivity.this, RegisterMajorActivity.class);
//                                        intent.putExtra("userFullname", fullname);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(LoginActivity.this, "Error: ", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    // Xử lý lỗi nếu có
                                }
                            });


//                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    // Gọi khi người dùng nhấn nút Đăng nhập bằng Google
    public void signIn(View view) {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    // Xử lý kết quả trả về từ quy trình đăng nhập
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

//        if (requestCode == RC_SIGN_IN) {
//            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//            handleSignInResult(task);
//        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Đăng nhập thành công, sử dụng account để lấy thông tin của người dùng
            String displayName = account.getDisplayName();
            String email = account.getEmail();
            // Thực hiện các hoạt động cần thiết sau khi đăng nhập thành công
            Toast.makeText(this, "Welcome, " + displayName, Toast.LENGTH_SHORT).show();
        } catch (ApiException e) {
            // Đăng nhập thất bại, xử lý các trường hợp thất bại tại đây
            Log.w("GoogleSignIn", "đăng nhập thất bại");
            Toast.makeText(this, "Sign in failed", Toast.LENGTH_SHORT).show();
        }
    }





    // Đ


}