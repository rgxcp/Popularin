package xyz.fairportstudios.popularin.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.apis.popularin.SignUpRequest;

public class SignUpActivity extends AppCompatActivity {
    private Context context;
    private CoordinatorLayout layout;
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
        layout = findViewById(R.id.coordinator_asu_layout);
        inputFirstName = findViewById(R.id.text_asu_first_name);
        inputLastName = findViewById(R.id.text_asu_last_name);
        inputUsername = findViewById(R.id.text_asu_username);
        inputEmail = findViewById(R.id.text_asu_email);
        inputPassword = findViewById(R.id.text_asu_password);
        Button buttonSignUp = findViewById(R.id.button_asu_signup);

        // Activity
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Menyimpan data
                String firstName = Objects.requireNonNull(inputFirstName.getText()).toString();
                String lastName = Objects.requireNonNull(inputLastName.getText()).toString();
                String username = Objects.requireNonNull(inputUsername.getText()).toString();
                String email = Objects.requireNonNull(inputEmail.getText()).toString();
                String password = Objects.requireNonNull(inputPassword.getText()).toString();

                // Mengirim data
                SignUpRequest signUpRequest = new SignUpRequest(
                        context,
                        firstName,
                        lastName,
                        username,
                        email,
                        password
                );

                // Mendapatkan hasil
                signUpRequest.sendRequest(new SignUpRequest.JSONCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        try {
                            int status = response.getInt("status");

                            if (status == 505) {
                                JSONObject jsonObjectResult = response.getJSONObject("result");
                                String id = String.valueOf(jsonObjectResult.getInt("id"));
                                String token = jsonObjectResult.getString("token");

                                SharedPreferences sharedPreferences = getSharedPreferences("AUTH", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("UID", id);
                                editor.putString("TOKEN", token);
                                editor.apply();

                                Intent gotoMain = new Intent(context, MainActivity.class);
                                startActivity(gotoMain);
                                finishAffinity();
                            } else if (status == 626) {
                                JSONArray jsonArrayResult = response.getJSONArray("result");
                                String errorMessage = jsonArrayResult.get(0).toString();
                                Snackbar.make(layout, errorMessage, Snackbar.LENGTH_SHORT).show();
                            } else {
                                Snackbar.make(layout, "Ada kesalahan dalam database", Snackbar.LENGTH_SHORT).show();
                            }
                        } catch (JSONException error) {
                            error.printStackTrace();
                        }
                    }
                });
            }
        });
    }
}
