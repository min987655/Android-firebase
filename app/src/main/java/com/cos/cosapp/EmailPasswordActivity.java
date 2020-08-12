package com.cos.cosapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EmailPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "EmailPasswordActivity";
    private Context mContext = EmailPasswordActivity.this;

    private FirebaseAuth mAuth;
    public EditText fieldEmail, fieldPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_password);

        Button emailSignInButton = findViewById(R.id.emailSignInButton);
        Button emailCreateAccountButton = findViewById(R.id.emailCreateAccountButton);
        Button signOutButton = findViewById(R.id.signOutButton);
        Button verifyEmailButton = findViewById(R.id.verifyEmailButton);
        Button reloadButton = findViewById(R.id.reloadButton);

        emailSignInButton.setOnClickListener(this);
        emailCreateAccountButton.setOnClickListener(this);
        signOutButton.setOnClickListener(this);
        verifyEmailButton.setOnClickListener(this);
        reloadButton.setOnClickListener(this);
//
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
//
        fieldEmail = findViewById(R.id.fieldEmail);
        fieldPassword = findViewById(R.id.fieldPassword);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
//        updateUI(currentUser);
    }

    private void createAccount(String email, String password) {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(mContext, MainActivity.class);
                            startActivity(intent);
                            Toast.makeText(mContext, "회원가입 성공 : " + user.getEmail(), Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "createUserWithEmail:success:user:" + user);
//                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void signIn(String email, String password) {

        if (!validateForm()) {
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            Intent intent = new Intent(mContext, MainTestActivity.class);
                            startActivity(intent);
                            FirebaseUser user = mAuth.getCurrentUser();
//                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                            // ...
                        }

                        // ...
                    }
                });
    }

    private void signOut() {
        mAuth.signOut();
//        updateUI(null);
    }

    private void reload() {
        mAuth.getCurrentUser().reload().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
//                    updateUI(mAuth.getCurrentUser());
                    Toast.makeText(EmailPasswordActivity.this,
                            "Reload successful!",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Log.e(TAG, "reload", task.getException());
                    Toast.makeText(EmailPasswordActivity.this,
                            "Failed to reload user.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean validateForm() {
        boolean valid = true;

        String email = fieldEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            fieldEmail.setError("Required.");
            valid = false;
        } else {
            fieldEmail.setError(null);
        }

        String password = fieldPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            fieldPassword.setError("Required.");
            valid = false;
        } else {
            fieldPassword.setError(null);
        }

        return valid;
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        Log.d(TAG, "onClick: i: " + i );
        Log.d(TAG, "onClick: i: " + R.id.emailCreateAccountButton);

        if (i == R.id.emailCreateAccountButton) {
            createAccount(fieldEmail.getText().toString(), fieldPassword.getText().toString());
        } else if (i == R.id.emailSignInButton) {
            signIn(fieldEmail.getText().toString(), fieldPassword.getText().toString());
        } else if (i == R.id.signOutButton) {
            signOut();
//        }
//        else if (i == R.id.verifyEmailButton) {
//            sendEmailVerification();
        } else if (i == R.id.reloadButton) {
            reload();
        }
    }


}