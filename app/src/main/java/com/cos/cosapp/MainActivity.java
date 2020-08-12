package com.cos.cosapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.common.SignInButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Main_Activity";
    private Context mContext = MainActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SignInButton btnGoogleLogin = findViewById(R.id.btn_google_login);

        // 버튼 클릭 시 구글 로그인 화면(startActivityForResult) 뜸
        btnGoogleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Choose authentication providers
                List<AuthUI.IdpConfig> providers = Arrays.asList(
                        new AuthUI.IdpConfig.GoogleBuilder().build());

                // Create and launch sign-in intent
                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setAvailableProviders(providers)
                                .build(),
                        3648);
            }
        });

        final Button btnLogout = findViewById(R.id.btn_google_logout);

        // 로그아웃. FirebaseAuth 비우기
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Toast.makeText(MainActivity.this, "구글 로그아웃 성공, user : " + user, Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onClick: " + user);
            }
        });

        Button btnEmailPasswordLogin = findViewById(R.id.btn_email_password_login);

        btnEmailPasswordLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, EmailPasswordActivity.class);
                startActivity(intent);
            }
        });

    }

    // intent
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 파이어베이스로부터 응답받는 정보
        // 응답받는 동시에 사용자 정보 오브젝트를 힙에 띄워줌 : FirebaseAuth == session과 비슷함
        // FirebaseAuth : static, 모든 액티비티에서 접근 가능
        if (requestCode == 3648) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Toast.makeText(this, "구글로그인 성공 : user.getEmail : " + user.getEmail(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext, MainTestActivity.class);
                Log.d(TAG, "onActivityResult: 로그인 완료 : " + user.getEmail());
                Log.d(TAG, "onActivityResult: 로그인 완료 : response : " + response);
                // ...
            } else {
                Log.d(TAG, "onActivityResult: 로그인 실패 : " );
                Log.d(TAG, "onActivityResult: 에러 로그 : " +  response.getError());
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }
    }
}