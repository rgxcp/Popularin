package xyz.fairportstudios.popularin.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.apis.popularin.Popularin;
import xyz.fairportstudios.popularin.apis.popularin.SignIn;

public class SignInActivity extends AppCompatActivity {
    private Button buttonSignIn;
    private Context context;
    private CoordinatorLayout layout;
    private TextInputEditText inputUsername, inputPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        buttonSignIn = findViewById(R.id.btn_asi);
        context = SignInActivity.this;
        layout = findViewById(R.id.cl_asi);
        inputUsername = findViewById(R.id.txt_asi_username);
        inputPassword = findViewById(R.id.txt_asi_password);

        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonSignIn.setEnabled(false);

                String username = Objects.requireNonNull(inputUsername.getText()).toString();
                String password = Objects.requireNonNull(inputPassword.getText()).toString();

                SignIn signIn = new SignIn(context, layout, username, password);
                signIn.sendRequest(Popularin.SIGN_IN_REQUEST);

                buttonSignIn.setEnabled(true);
            }
        });
    }
}
