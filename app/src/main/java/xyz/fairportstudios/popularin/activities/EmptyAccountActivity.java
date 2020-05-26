package xyz.fairportstudios.popularin.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import xyz.fairportstudios.popularin.R;

public class EmptyAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reusable_empty_account);

        // Binding
        Button buttonSignIn = findViewById(R.id.button_rea_sign_in);
        Button buttonSignUp = findViewById(R.id.button_rea_sign_up);

        // Activity
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoSignIn();
            }
        });

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoSignUp();
            }
        });
    }

    private void gotoSignIn() {
        Intent intent = new Intent(EmptyAccountActivity.this, SignInActivity.class);
        startActivity(intent);
    }

    private void gotoSignUp() {
        Intent intent = new Intent(EmptyAccountActivity.this, SignUpActivity.class);
        startActivity(intent);
    }
}
