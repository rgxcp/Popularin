package xyz.fairportstudios.popularin.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.apis.popularin.post.SignInRequest;
import xyz.fairportstudios.popularin.preferences.Auth;

public class SignInActivity extends AppCompatActivity {
    private Button buttonSignIn;
    private Context context;
    private LinearLayout layout;
    private TextInputEditText inputUsername;
    private TextInputEditText inputPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // Binding
        context = SignInActivity.this;
        buttonSignIn = findViewById(R.id.button_asi_signin);
        layout = findViewById(R.id.layout_asi_anchor);
        inputUsername = findViewById(R.id.input_asi_username);
        inputPassword = findViewById(R.id.input_asi_password);

        // Activity
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonSignIn.setEnabled(false);
                buttonSignIn.setText(R.string.loading);
                signIn();
            }
        });
    }

    private void signIn() {
        // Menyimpan input
        String username = Objects.requireNonNull(inputUsername.getText()).toString();
        String password = Objects.requireNonNull(inputPassword.getText()).toString();

        // Membuat objek
        SignInRequest signInRequest = new SignInRequest(
                context,
                username,
                password
        );

        // Mengirim request
        signInRequest.sendRequest(new SignInRequest.APICallback() {
            @Override
            public void onSuccess(String id, String token) {
                // Menyimpan pref dalam storage lokal
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
                Snackbar.make(layout, R.string.invalid_credentials, Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onFailed(String message) {
                buttonSignIn.setEnabled(true);
                buttonSignIn.setText(R.string.sign_in);
                Snackbar.make(layout, message, Snackbar.LENGTH_LONG).show();

            }

            @Override
            public void onError() {
                buttonSignIn.setEnabled(true);
                buttonSignIn.setText(R.string.sign_in);
                Snackbar.make(layout, R.string.sign_in_error, Snackbar.LENGTH_LONG).show();
            }
        });
    }
}
