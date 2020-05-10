package xyz.fairportstudios.popularin.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.apis.popularin.post.SignInRequest;
import xyz.fairportstudios.popularin.preferences.Auth;

public class SignInActivity extends AppCompatActivity {
    private Context context;
    private CoordinatorLayout layout;
    private TextInputEditText inputUsername;
    private TextInputEditText inputPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // Binding
        context = SignInActivity.this;
        layout = findViewById(R.id.layout_asi_anchor);
        inputUsername = findViewById(R.id.text_asi_username);
        inputPassword = findViewById(R.id.text_asi_password);
        Button buttonSignIn = findViewById(R.id.button_asi_signin);

        // Activity
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Menyimpan data
                String username = Objects.requireNonNull(inputUsername.getText()).toString();
                String password = Objects.requireNonNull(inputPassword.getText()).toString();

                // POST
                SignInRequest signInRequest = new SignInRequest(context, username, password);
                signInRequest.sendRequest(new SignInRequest.APICallback() {
                    @Override
                    public void onSuccess(String id, String token) {
                        Auth auth = new Auth(context);
                        auth.setAuth(id, token);

                        Intent gotoMain = new Intent(context, MainActivity.class);
                        startActivity(gotoMain);
                        finishAffinity();
                    }

                    @Override
                    public void onInvalid() {
                        Snackbar.make(layout, R.string.invalid_credentials, Snackbar.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailed(String message) {
                        Snackbar.make(layout, message, Snackbar.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError() {
                        Snackbar.make(layout, R.string.failed_sign_in, Snackbar.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}
