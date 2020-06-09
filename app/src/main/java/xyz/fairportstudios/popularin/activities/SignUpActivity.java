package xyz.fairportstudios.popularin.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.RelativeSizeSpan;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.apis.popularin.post.SignUpRequest;
import xyz.fairportstudios.popularin.preferences.Auth;

public class SignUpActivity extends AppCompatActivity {
    private Context context = SignUpActivity.this;
    private Button buttonSignUp;
    private LinearLayout anchorLayout;
    private String fullName;
    private String username;
    private String email;
    private String password;
    private TextInputEditText inputFullName;
    private TextInputEditText inputUsername;
    private TextInputEditText inputEmail;
    private TextInputEditText inputPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Binding
        buttonSignUp = findViewById(R.id.button_asu_sign_up);
        anchorLayout = findViewById(R.id.anchor_asu_layout);
        inputFullName = findViewById(R.id.input_asu_full_name);
        inputUsername = findViewById(R.id.input_asu_username);
        inputEmail = findViewById(R.id.input_asu_email);
        inputPassword = findViewById(R.id.input_asu_password);
        TextView textWelcome = findViewById(R.id.text_asu_welcome);

        // Pesan
        String welcome = getString(R.string.sign_up_welcome);
        SpannableString spannableString = new SpannableString(welcome);
        RelativeSizeSpan relativeSizeSpan = new RelativeSizeSpan(2f);
        spannableString.setSpan(relativeSizeSpan, 0, 5, 0);
        textWelcome.setText(spannableString);

        // Text watcher
        inputFullName.addTextChangedListener(signUpWatcher);
        inputUsername.addTextChangedListener(signUpWatcher);
        inputEmail.addTextChangedListener(signUpWatcher);
        inputPassword.addTextChangedListener(signUpWatcher);

        // Activity
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSignUpButtonState(false);
                signUp();
            }
        });
    }

    private TextWatcher signUpWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            // Tidak digunakan
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            fullName = Objects.requireNonNull(inputFullName.getText()).toString();
            username = Objects.requireNonNull(inputUsername.getText()).toString();
            email = Objects.requireNonNull(inputEmail.getText()).toString();
            password = Objects.requireNonNull(inputPassword.getText()).toString();
            buttonSignUp.setEnabled(!fullName.isEmpty() && !username.isEmpty() && !email.isEmpty() && !password.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // Tidak digunakan
        }
    };

    private boolean usernameValidated() {
        if (username.length() < 5) {
            Snackbar.make(anchorLayout, R.string.validate_username_length, Snackbar.LENGTH_LONG).show();
            return false;
        } else if (username.contains(" ")) {
            Snackbar.make(anchorLayout, R.string.validate_alpha_dash, Snackbar.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    private boolean emailValidated() {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Snackbar.make(anchorLayout, R.string.validate_email_format, Snackbar.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    private boolean passwordValidated() {
        if (password.length() < 8) {
            Snackbar.make(anchorLayout, R.string.validate_password_length, Snackbar.LENGTH_LONG).show();
            return false;
        } else if (password.equals(username)) {
            Snackbar.make(anchorLayout, R.string.validate_username_match_password, Snackbar.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    private void setSignUpButtonState(Boolean state) {
        buttonSignUp.setEnabled(state);
        if (state) {
            buttonSignUp.setText(R.string.sign_up);
        } else {
            buttonSignUp.setText(R.string.loading);
        }
    }

    private void signUp() {
        if (usernameValidated() && emailValidated() && passwordValidated()) {
            SignUpRequest signUpRequest = new SignUpRequest(context, fullName, username, email, password);
            signUpRequest.sendRequest(new SignUpRequest.Callback() {
                @Override
                public void onSuccess(Integer id, String token) {
                    Auth auth = new Auth(context);
                    auth.setAuth(id, token);

                    Intent intent = new Intent(context, MainActivity.class);
                    startActivity(intent);
                    finishAffinity();
                }

                @Override
                public void onFailed(String message) {
                    setSignUpButtonState(true);
                    Snackbar.make(anchorLayout, message, Snackbar.LENGTH_LONG).show();
                }

                @Override
                public void onError(String message) {
                    setSignUpButtonState(true);
                    Snackbar.make(anchorLayout, message, Snackbar.LENGTH_LONG).show();
                }
            });
        } else {
            setSignUpButtonState(true);
        }
    }
}
