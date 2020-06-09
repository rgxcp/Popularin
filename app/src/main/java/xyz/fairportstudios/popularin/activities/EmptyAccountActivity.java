package xyz.fairportstudios.popularin.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
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

        // Context
        final Context context = EmptyAccountActivity.this;

        // Activity
        Button buttonSignIn = findViewById(R.id.button_rea_sign_in);
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoSignIn(context);
            }
        });

        Button buttonSignUp = findViewById(R.id.button_rea_sign_up);
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoSignUp(context);
            }
        });
    }

    private void gotoSignIn(Context context) {
        Intent intent = new Intent(context, SignInActivity.class);
        startActivity(intent);
    }

    private void gotoSignUp(Context context) {
        Intent intent = new Intent(context, SignUpActivity.class);
        startActivity(intent);
    }
}
