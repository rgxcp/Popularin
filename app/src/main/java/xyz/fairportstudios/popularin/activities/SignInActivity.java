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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.apis.popularin.SignInRequest;
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
        layout = findViewById(R.id.coordinator_asi_layout);
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

                // Mengirim data
                SignInRequest signInRequest = new SignInRequest(
                        context,
                        username,
                        password
                );

                // Mendapatkan hasil
                signInRequest.sendRequest(new SignInRequest.JSONCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        try {
                            int status = response.getInt("status");

                            if (status == 515) {
                                JSONObject jsonObjectResult = response.getJSONObject("result");
                                String id = String.valueOf(jsonObjectResult.getInt("id"));
                                String token = jsonObjectResult.getString("token");

                                Auth auth = new Auth(context);
                                auth.setAuth(id, token);

                                Intent gotoMain = new Intent(context, MainActivity.class);
                                startActivity(gotoMain);
                                finishAffinity();
                            } else if (status == 616) {
                                String errorMessage = response.getString("message");
                                Snackbar.make(layout, errorMessage, Snackbar.LENGTH_SHORT).show();
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
