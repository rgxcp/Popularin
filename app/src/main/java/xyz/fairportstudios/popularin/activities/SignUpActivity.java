package xyz.fairportstudios.popularin.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.apis.popularin.Popularin;
import xyz.fairportstudios.popularin.apis.popularin.SignUp;

public class SignUpActivity extends AppCompatActivity {
    private Button buttonSignUp;
    private Context context;
    private CoordinatorLayout layout;
    private TextInputEditText inputFirstName, inputLastName, inputUsername, inputEmail, inputPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        buttonSignUp = findViewById(R.id.btn_asu);
        context = SignUpActivity.this;
        layout = findViewById(R.id.cl_asu);
        inputFirstName = findViewById(R.id.txt_asu_first_name);
        inputLastName = findViewById(R.id.txt_asu_last_name);
        inputUsername = findViewById(R.id.txt_asu_username);
        inputEmail = findViewById(R.id.txt_asu_email);
        inputPassword = findViewById(R.id.txt_asu_password);

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonSignUp.setEnabled(false);

                String firstName = Objects.requireNonNull(inputFirstName.getText()).toString();
                String lastName = Objects.requireNonNull(inputLastName.getText()).toString();
                String username = Objects.requireNonNull(inputUsername.getText()).toString();
                String email = Objects.requireNonNull(inputEmail.getText()).toString();
                String password = Objects.requireNonNull(inputPassword.getText()).toString();

                SignUp signUp = new SignUp(context, layout, firstName, lastName, username, email, password);
                signUp.sendRequest(Popularin.SIGN_UP_REQUEST);

                buttonSignUp.setEnabled(true);
            }
        });
    }
}
