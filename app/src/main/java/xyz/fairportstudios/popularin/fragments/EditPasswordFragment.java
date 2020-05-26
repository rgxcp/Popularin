package xyz.fairportstudios.popularin.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.activities.MainActivity;
import xyz.fairportstudios.popularin.apis.popularin.put.UpdatePasswordRequest;

public class EditPasswordFragment extends Fragment {
    private Button buttonSavePassword;
    private Context context;
    private LinearLayout anchorLayout;
    private String currentPassword;
    private String newPassword;
    private String confirmPassword;
    private TextInputEditText inputCurrentPassword;
    private TextInputEditText inputNewPassword;
    private TextInputEditText inputConfirmPassword;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_password, container, false);

        // Binding
        context = getActivity();
        buttonSavePassword = view.findViewById(R.id.button_fepw_save_password);
        anchorLayout = view.findViewById(R.id.anchor_fepw_layout);
        inputCurrentPassword = view.findViewById(R.id.input_fepw_current_password);
        inputNewPassword = view.findViewById(R.id.input_fepw_new_password);
        inputConfirmPassword = view.findViewById(R.id.input_fepw_confirm_password);
        TextView textWelcome = view.findViewById(R.id.text_fepw_welcome);

        // Pesan
        String welcome = getString(R.string.edit_password_welcome);
        SpannableString spannableString = new SpannableString(welcome);
        RelativeSizeSpan relativeSizeSpan = new RelativeSizeSpan(2f);
        spannableString.setSpan(relativeSizeSpan, 0, 4, 0);
        textWelcome.setText(spannableString);

        // Text watcher
        inputCurrentPassword.addTextChangedListener(editPasswordWatcher);
        inputNewPassword.addTextChangedListener(editPasswordWatcher);
        inputConfirmPassword.addTextChangedListener(editPasswordWatcher);

        // Activity
        buttonSavePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePassword();
            }
        });

        return view;
    }

    private TextWatcher editPasswordWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            // Tidak digunakan
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            currentPassword = Objects.requireNonNull(inputCurrentPassword.getText()).toString();
            newPassword = Objects.requireNonNull(inputNewPassword.getText()).toString();
            confirmPassword = Objects.requireNonNull(inputConfirmPassword.getText()).toString();
            buttonSavePassword.setEnabled(!currentPassword.isEmpty() && !newPassword.isEmpty() && !confirmPassword.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // Tidak digunakan
        }
    };

    private boolean validatePassword() {
        if (newPassword.length() < 8) {
            Snackbar.make(anchorLayout, R.string.validate_new_password, Snackbar.LENGTH_LONG).show();
            return false;
        } else if (!confirmPassword.equals(newPassword)) {
            Snackbar.make(anchorLayout, R.string.validate_un_match_confirm_password, Snackbar.LENGTH_LONG).show();
            return false;
        } else if (newPassword.equals(currentPassword)) {
            Snackbar.make(anchorLayout, R.string.validate_match_new_password, Snackbar.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    private void savePassword() {
        buttonSavePassword.setEnabled(false);
        buttonSavePassword.setText(R.string.loading);

        if (validatePassword()) {
            UpdatePasswordRequest updatePasswordRequest = new UpdatePasswordRequest(context, currentPassword, newPassword, confirmPassword);
            updatePasswordRequest.sendRequest(new UpdatePasswordRequest.APICallback() {
                @Override
                public void onSuccess() {
                    Intent intent = new Intent(context, MainActivity.class);
                    startActivity(intent);
                    Objects.requireNonNull(getActivity()).finishAffinity();
                }

                @Override
                public void onInvalid() {
                    buttonSavePassword.setEnabled(true);
                    buttonSavePassword.setText(R.string.save_password);
                    Snackbar.make(anchorLayout, R.string.invalid_current_password, Snackbar.LENGTH_LONG).show();
                }

                @Override
                public void onFailed(String message) {
                    buttonSavePassword.setEnabled(true);
                    buttonSavePassword.setText(R.string.save_password);
                    Snackbar.make(anchorLayout, message, Snackbar.LENGTH_LONG).show();
                }

                @Override
                public void onError() {
                    buttonSavePassword.setEnabled(true);
                    buttonSavePassword.setText(R.string.save_password);
                    Snackbar.make(anchorLayout, R.string.failed_update_password, Snackbar.LENGTH_LONG).show();
                }
            });
        } else {
            buttonSavePassword.setEnabled(true);
            buttonSavePassword.setText(R.string.save_password);
        }
    }
}
