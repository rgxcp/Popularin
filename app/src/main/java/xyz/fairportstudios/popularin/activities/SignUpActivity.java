package xyz.fairportstudios.popularin.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.apis.popularin.post.SignUpRequest;
import xyz.fairportstudios.popularin.preferences.Auth;

public class SignUpActivity extends AppCompatActivity {
    private Context context;
    private LinearLayout layout;
    private TextInputEditText inputFirstName;
    private TextInputEditText inputLastName;
    private TextInputEditText inputUsername;
    private TextInputEditText inputEmail;
    private TextInputEditText inputPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Binding
        context = SignUpActivity.this;
        layout = findViewById(R.id.layout_asu_anchor);
        inputFirstName = findViewById(R.id.input_asu_first_name);
        inputLastName = findViewById(R.id.input_asu_last_name);
        inputUsername = findViewById(R.id.input_asu_username);
        inputEmail = findViewById(R.id.input_asu_email);
        inputPassword = findViewById(R.id.input_asu_password);
        Button buttonSignUp = findViewById(R.id.button_asu_sign_up);

        // Activity
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp();
            }
        });
    }

    private void signUp() {
        // Menyimpan input
        String firstName = Objects.requireNonNull(inputFirstName.getText()).toString();
        String lastName = Objects.requireNonNull(inputLastName.getText()).toString();
        String username = Objects.requireNonNull(inputUsername.getText()).toString();
        String email = Objects.requireNonNull(inputEmail.getText()).toString();
        String password = Objects.requireNonNull(inputPassword.getText()).toString();

        // Membuat objek
        SignUpRequest signUpRequest = new SignUpRequest(
                context,
                firstName,
                lastName,
                username,
                email,
                password
        );

        // Mengirim request
        signUpRequest.sendRequest(new SignUpRequest.APICallback() {
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
            public void onFailed(String message) {
                Snackbar.make(layout, message, Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onError() {
                Snackbar.make(layout, R.string.sign_up_error, Snackbar.LENGTH_LONG).show();
            }
        });
    }
}
