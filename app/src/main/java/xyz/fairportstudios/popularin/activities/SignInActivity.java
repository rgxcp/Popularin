package xyz.fairportstudios.popularin.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.apis.popularin.post.SignInRequest;
import xyz.fairportstudios.popularin.preferences.Auth;

public class SignInActivity extends AppCompatActivity {
    // Variable untuk fitur load
    private boolean mIsLoading = false;

    // Variable member
    private Button mButtonSignIn;
    private LinearLayout mAnchorLayout;
    private String mUsername;
    private String mPassword;
    private TextInputEditText mInputUsername;
    private TextInputEditText mInputPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // Context
        final Context context = SignInActivity.this;

        // Binding
        mButtonSignIn = findViewById(R.id.button_asi_sign_in);
        mAnchorLayout = findViewById(R.id.anchor_asi_layout);
        mInputUsername = findViewById(R.id.input_asi_username);
        mInputPassword = findViewById(R.id.input_asi_password);
        TextView textWelcome = findViewById(R.id.text_asi_welcome);

        // Pesan
        textWelcome.setText(getWelcomeMessage());

        // Text watcher
        mInputUsername.addTextChangedListener(signInWatcher);
        mInputPassword.addTextChangedListener(signInWatcher);

        // Activity
        mButtonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIsLoading = true;
                setSignInButtonState(false);
                signIn(context);
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
        String welcome = getString(R.string.sign_in_welcome);
        SpannableString spannableString = new SpannableString(welcome);
        RelativeSizeSpan relativeSizeSpan = new RelativeSizeSpan(2f);
        spannableString.setSpan(relativeSizeSpan, 0, 5, 0);
        return spannableString;
    }

    private TextWatcher signInWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            // Tidak digunakan
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            mUsername = Objects.requireNonNull(mInputUsername.getText()).toString();
            mPassword = Objects.requireNonNull(mInputPassword.getText()).toString();
            mButtonSignIn.setEnabled(!mUsername.isEmpty() && !mPassword.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // Tidak digunakan
        }
    };

    private void setSignInButtonState(boolean enable) {
        mButtonSignIn.setEnabled(enable);
        if (enable) {
            mButtonSignIn.setText(R.string.sign_in);
        } else {
            mButtonSignIn.setText(R.string.loading);
        }
    }

    private void signIn(final Context context) {
        SignInRequest signInRequest = new SignInRequest(context, mUsername, mPassword);
        signInRequest.sendRequest(new SignInRequest.Callback() {
            @Override
            public void onSuccess(int id, String token) {
                Auth auth = new Auth(context);
                auth.setAuth(id, token);

                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
                finishAffinity();
            }

            @Override
            public void onInvalidUsername() {
                setSignInButtonState(true);
                Snackbar.make(mAnchorLayout, R.string.invalid_username, Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onInvalidPassword() {
                setSignInButtonState(true);
                Snackbar.make(mAnchorLayout, R.string.invalid_password, Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onFailed(String message) {
                setSignInButtonState(true);
                Snackbar.make(mAnchorLayout, message, Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onError(String message) {
                setSignInButtonState(true);
                Snackbar.make(mAnchorLayout, message, Snackbar.LENGTH_LONG).show();
            }
        });

        // Memberhentikan loading
        mIsLoading = false;
    }
}
