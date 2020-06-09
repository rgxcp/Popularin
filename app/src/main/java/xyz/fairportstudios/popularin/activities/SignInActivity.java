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
    private Context context = SignInActivity.this;
    private Button buttonSignIn;
    private LinearLayout anchorLayout;
    private String username;
    private String password;
    private TextInputEditText inputUsername;
    private TextInputEditText inputPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // Binding
        buttonSignIn = findViewById(R.id.button_asi_sign_in);
        anchorLayout = findViewById(R.id.anchor_asi_layout);
        inputUsername = findViewById(R.id.input_asi_username);
        inputPassword = findViewById(R.id.input_asi_password);
        TextView textWelcome = findViewById(R.id.text_asi_welcome);

        // Pesan
        String welcome = getString(R.string.sign_in_welcome);
        SpannableString spannableString = new SpannableString(welcome);
        RelativeSizeSpan relativeSizeSpan = new RelativeSizeSpan(2f);
        spannableString.setSpan(relativeSizeSpan, 0, 5, 0);
        textWelcome.setText(spannableString);

        // Text watcher
        inputUsername.addTextChangedListener(signInWatcher);
        inputPassword.addTextChangedListener(signInWatcher);

        // Activity
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSignInButtonState(false);
                signIn();
            }
        });
    }

    private TextWatcher signInWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            // Tidak digunakan
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            username = Objects.requireNonNull(inputUsername.getText()).toString();
            password = Objects.requireNonNull(inputPassword.getText()).toString();
            buttonSignIn.setEnabled(!username.isEmpty() && !password.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // Tidak digunakan
        }
    };

    private void setSignInButtonState(Boolean state) {
        buttonSignIn.setEnabled(state);
        if (state) {
            buttonSignIn.setText(R.string.sign_in);
        } else {
            buttonSignIn.setText(R.string.loading);
        }
    }

    private void signIn() {
        SignInRequest signInRequest = new SignInRequest(context, username, password);
        signInRequest.sendRequest(new SignInRequest.APICallback() {
            @Override
            public void onSuccess(String id, String token) {
                Auth auth = new Auth(context);
                auth.setAuth(id, token);

                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
                finishAffinity();
            }

            @Override
            public void onInvalid() {
                buttonSignIn.setEnabled(true);
                buttonSignIn.setText(R.string.sign_in);
                Snackbar.make(anchorLayout, R.string.invalid_credentials, Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onFailed(String message) {
                buttonSignIn.setEnabled(true);
                buttonSignIn.setText(R.string.sign_in);
                Snackbar.make(anchorLayout, message, Snackbar.LENGTH_LONG).show();

            }

            @Override
            public void onError() {
                buttonSignIn.setEnabled(true);
                buttonSignIn.setText(R.string.sign_in);
                Snackbar.make(anchorLayout, R.string.failed_sign_in, Snackbar.LENGTH_LONG).show();
            }
        });
    }
}
