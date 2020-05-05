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
import xyz.fairportstudios.popularin.apis.popularin.put.UpdatePassword;

public class EditPasswordActivity extends AppCompatActivity {
    private Context context;
    private CoordinatorLayout layout;
    private TextInputEditText inputCurrentPassword;
    private TextInputEditText inputNewPassword;
    private TextInputEditText inputConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);

        // Binding
        context = EditPasswordActivity.this;
        layout = findViewById(R.id.coordinator_aepw_layout);
        inputCurrentPassword = findViewById(R.id.text_aepw_current_password);
        inputNewPassword = findViewById(R.id.text_aepw_new_password);
        inputConfirmPassword = findViewById(R.id.text_aepw_confirm_password);
        Button buttonSave = findViewById(R.id.button_aepw_save);

        // Activity
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Menyimpan data
                String currentPassword = Objects.requireNonNull(inputCurrentPassword.getText()).toString();
                String newPassword = Objects.requireNonNull(inputNewPassword.getText()).toString();
                String confirmPassword = Objects.requireNonNull(inputConfirmPassword.getText()).toString();

                // Mengirim data
                UpdatePassword updatePassword = new UpdatePassword(
                        context,
                        currentPassword,
                        newPassword,
                        confirmPassword
                );

                // Mendapatkan hasil
                updatePassword.sendRequest(new UpdatePassword.JSONCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        try {
                            int status = response.getInt("status");

                            if (status == 303) {
                                Intent gotoMain = new Intent(context, MainActivity.class);
                                startActivity(gotoMain);
                                finishAffinity();
                            } else if (status == 616) {
                                Snackbar.make(layout, "Password lama tidak sesuai.", Snackbar.LENGTH_SHORT).show();
                            } else if (status == 626) {
                                JSONArray jsonArrayResult = response.getJSONArray("result");
                                String errorMessage = jsonArrayResult.get(0).toString();
                                Snackbar.make(layout, errorMessage, Snackbar.LENGTH_SHORT).show();
                            } else {
                                Snackbar.make(layout, "Ada kesalahan dalam database.", Snackbar.LENGTH_SHORT).show();
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
