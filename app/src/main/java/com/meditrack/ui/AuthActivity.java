package com.meditrack.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.meditrack.R;
import com.meditrack.auth.LocalAuthManager;

public class AuthActivity extends AppCompatActivity {
    private TextInputEditText emailInput;
    private TextInputEditText passwordInput;
    private Button loginButton;
    private Button signupButton;
    private LocalAuthManager authManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        authManager = LocalAuthManager.getInstance(this);
        initializeViews();
        setupListeners();

        // Check if user is already logged in
        if (authManager.getCurrentUserId() != null) {
            navigateToMain();
        }
    }

    private void initializeViews() {
        emailInput = findViewById(R.id.email_input);
        passwordInput = findViewById(R.id.password_input);
        loginButton = findViewById(R.id.login_button);
        signupButton = findViewById(R.id.signup_button);
    }

    private void setupListeners() {
        loginButton.setOnClickListener(v -> handleLogin());
        signupButton.setOnClickListener(v -> handleSignup());
    }

    private void handleLogin() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (validateInput(email, password)) {
            authManager.signIn(email, password, new LocalAuthManager.AuthCallback() {
                @Override
                public void onSuccess() {
                    runOnUiThread(() -> navigateToMain());
                }

                @Override
                public void onError(Exception e) {
                    runOnUiThread(() -> showError(getString(R.string.error_login_failed)));
                }
            });
        }
    }

    private void handleSignup() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (validateInput(email, password)) {
            authManager.signUp(email, password, new LocalAuthManager.AuthCallback() {
                @Override
                public void onSuccess() {
                    runOnUiThread(() -> navigateToMain());
                }

                @Override
                public void onError(Exception e) {
                    runOnUiThread(() -> showError(e.getMessage()));
                }
            });
        }
    }

    private boolean validateInput(String email, String password) {
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showError(getString(R.string.error_invalid_email));
            return false;
        }

        if (password.isEmpty() || password.length() < 6) {
            showError(getString(R.string.error_invalid_password));
            return false;
        }

        return true;
    }

    private void navigateToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
