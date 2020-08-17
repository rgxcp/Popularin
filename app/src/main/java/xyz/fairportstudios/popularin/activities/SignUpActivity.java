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
    // Variable untuk fitur load
    private boolean mIsLoading = false;

    // Variable member
    private Button mButtonSignUp;
    private LinearLayout mAnchorLayout;
    private String mFullName;
    private String mUsername;
    private String mEmail;
    private String mPassword;
    private TextInputEditText mInputFullName;
    private TextInputEditText mInputUsername;
    private TextInputEditText mInputEmail;
    private TextInputEditText mInputPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Context
        final Context context = SignUpActivity.this;

        // Binding
        mButtonSignUp = findViewById(R.id.button_asu_sign_up);
        mAnchorLayout = findViewById(R.id.anchor_asu_layout);
        mInputFullName = findViewById(R.id.input_asu_full_name);
        mInputUsername = findViewById(R.id.input_asu_username);
        mInputEmail = findViewById(R.id.input_asu_email);
        mInputPassword = findViewById(R.id.input_asu_password);
        TextView textWelcome = findViewById(R.id.text_asu_welcome);

        // Pesan
        textWelcome.setText(getWelcomeMessage());

        // Text watcher
        mInputFullName.addTextChangedListener(mSignUpWatcher);
        mInputUsername.addTextChangedListener(mSignUpWatcher);
        mInputEmail.addTextChangedListener(mSignUpWatcher);
        mInputPassword.addTextChangedListener(mSignUpWatcher);

        // Activity
        mButtonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIsLoading = true;
                setSignUpButtonState(false);
                signUp(context);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (!mIsLoading) {
            super.onBackPressed();
        }
    }

    private SpannableString getWelcomeMessage() {
        String welcomeMessage = getString(R.string.sign_up_welcome_message);
        SpannableString spannableString = new SpannableString(welcomeMessage);
        RelativeSizeSpan relativeSizeSpan = new RelativeSizeSpan(2f);
        spannableString.setSpan(relativeSizeSpan, 0, 5, 0);
        return spannableString;
    }

    private TextWatcher mSignUpWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            // Tidak digunakan
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            mFullName = Objects.requireNonNull(mInputFullName.getText()).toString();
            mUsername = Objects.requireNonNull(mInputUsername.getText()).toString();
            mEmail = Objects.requireNonNull(mInputEmail.getText()).toString();
            mPassword = Objects.requireNonNull(mInputPassword.getText()).toString();
            mButtonSignUp.setEnabled(!mFullName.isEmpty() && !mUsername.isEmpty() && !mEmail.isEmpty() && !mPassword.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // Tidak digunakan
        }
    };

    private boolean usernameValidated() {
        if (mUsername.length() < 5) {
            Snackbar.make(mAnchorLayout, R.string.validate_username_length, Snackbar.LENGTH_LONG).show();
            return false;
        } else if (mUsername.contains(" ")) {
            Snackbar.make(mAnchorLayout, R.string.validate_alpha_dash, Snackbar.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    private boolean emailValidated() {
        if (!Patterns.EMAIL_ADDRESS.matcher(mEmail).matches()) {
            Snackbar.make(mAnchorLayout, R.string.validate_email_format, Snackbar.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    private boolean passwordValidated() {
        if (mPassword.length() < 8) {
            Snackbar.make(mAnchorLayout, R.string.validate_password_length, Snackbar.LENGTH_LONG).show();
            return false;
        } else if (mPassword.equals(mUsername)) {
            Snackbar.make(mAnchorLayout, R.string.validate_username_match_password, Snackbar.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    private void setSignUpButtonState(boolean state) {
        mButtonSignUp.setEnabled(state);
        if (state) {
            mButtonSignUp.setText(R.string.sign_up);
        } else {
            mButtonSignUp.setText(R.string.loading);
        }
    }

    private void signUp(final Context context) {
        if (usernameValidated() && emailValidated() && passwordValidated()) {
            SignUpRequest signUpRequest = new SignUpRequest(context, mFullName, mUsername, mEmail, mPassword);
            signUpRequest.sendRequest(new SignUpRequest.Callback() {
                @Override
                public void onSuccess(int id, String token) {
                    Auth auth = new Auth(context);
                    auth.setAuth(id, token);

                    Intent intent = new Intent(context, MainActivity.class);
                    startActivity(intent);
                    finishAffinity();
                }

                @Override
                public void onFailed(String message) {
                    setSignUpButtonState(true);
                    Snackbar.make(mAnchorLayout, message, Snackbar.LENGTH_LONG).show();
                }

                @Override
                public void onError(String message) {
                    setSignUpButtonState(true);
                    Snackbar.make(mAnchorLayout, message, Snackbar.LENGTH_LONG).show();
                }
            });
        } else {
            setSignUpButtonState(true);
        }

        // Memberhentikan loading
        mIsLoading = false;
    }
}
