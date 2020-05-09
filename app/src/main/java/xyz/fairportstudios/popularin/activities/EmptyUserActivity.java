package xyz.fairportstudios.popularin.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import xyz.fairportstudios.popularin.R;

public class EmptyUserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.global_empty_user);

        // Binding
        Button buttonSignIn = findViewById(R.id.button_geu_signin);
        Button buttonSignUp = findViewById(R.id.button_geu_signup);

        // Activity
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoSignIn = new Intent(EmptyUserActivity.this, SignInActivity.class);
                startActivity(gotoSignIn);
            }
        });

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoSignUp = new Intent(EmptyUserActivity.this, SignUpActivity.class);
                startActivity(gotoSignUp);
            }
        });
    }
}
