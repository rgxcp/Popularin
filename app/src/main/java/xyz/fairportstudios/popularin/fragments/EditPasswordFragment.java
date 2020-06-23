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
import xyz.fairportstudios.popularin.preferences.Auth;

public class EditPasswordFragment extends Fragment {
    // Variable member
    private Button mButtonSavePassword;
    private LinearLayout mAnchorLayout;
    private String mCurrentPassword;
    private String mNewPassword;
    private String mConfirmPassword;
    private TextInputEditText mInputCurrentPassword;
    private TextInputEditText mInputNewPassword;
    private TextInputEditText mInputConfirmPassword;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_password, container, false);

        // Context
        final Context context = getActivity();

        // Binding
        mButtonSavePassword = view.findViewById(R.id.button_fepw_save_password);
        mAnchorLayout = view.findViewById(R.id.anchor_fepw_layout);
        mInputCurrentPassword = view.findViewById(R.id.input_fepw_current_password);
        mInputNewPassword = view.findViewById(R.id.input_fepw_new_password);
        mInputConfirmPassword = view.findViewById(R.id.input_fepw_confirm_password);
        TextView textWelcome = view.findViewById(R.id.text_fepw_welcome);

        // Pesan
        textWelcome.setText(getWelcomeMessage());

        // Text watcher
        mInputCurrentPassword.addTextChangedListener(editPasswordWatcher);
        mInputNewPassword.addTextChangedListener(editPasswordWatcher);
        mInputConfirmPassword.addTextChangedListener(editPasswordWatcher);

        // Activity
        mButtonSavePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSavePasswordButtonState(false);
                savePassword(context);
            }
        });

        return view;
    }

    private SpannableString getWelcomeMessage() {
        String welcome = getString(R.string.edit_password_welcome);
        SpannableString spannableString = new SpannableString(welcome);
        RelativeSizeSpan relativeSizeSpan = new RelativeSizeSpan(2f);
        spannableString.setSpan(relativeSizeSpan, 0, 4, 0);
        return spannableString;
    }

    private TextWatcher editPasswordWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            // Tidak digunakan
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            mCurrentPassword = Objects.requireNonNull(mInputCurrentPassword.getText()).toString();
            mNewPassword = Objects.requireNonNull(mInputNewPassword.getText()).toString();
            mConfirmPassword = Objects.requireNonNull(mInputConfirmPassword.getText()).toString();
            mButtonSavePassword.setEnabled(!mCurrentPassword.isEmpty() && !mNewPassword.isEmpty() && !mConfirmPassword.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // Tidak digunakan
        }
    };

    private boolean passwordValidated() {
        if (mNewPassword.length() < 8) {
            Snackbar.make(mAnchorLayout, R.string.validate_new_password_length, Snackbar.LENGTH_LONG).show();
            return false;
        } else if (!mConfirmPassword.equals(mNewPassword)) {
            Snackbar.make(mAnchorLayout, R.string.validate_confirm_password_un_match_new_password, Snackbar.LENGTH_LONG).show();
            return false;
        } else if (mNewPassword.equals(mCurrentPassword)) {
            Snackbar.make(mAnchorLayout, R.string.validate_new_password_match_current_password, Snackbar.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    private void setSavePasswordButtonState(boolean enable) {
        mButtonSavePassword.setEnabled(enable);
        if (enable) {
            mButtonSavePassword.setText(R.string.save_password);
        } else {
            mButtonSavePassword.setText(R.string.loading);
        }
    }

    private void savePassword(final Context context) {
        if (passwordValidated()) {
            UpdatePasswordRequest updatePasswordRequest = new UpdatePasswordRequest(context, mCurrentPassword, mNewPassword, mConfirmPassword);
            updatePasswordRequest.sendRequest(new UpdatePasswordRequest.Callback() {
                @Override
                public void onSuccess(int id, String token) {
                    Auth auth = new Auth(context);
                    auth.setAuth(id, token);

                    Intent intent = new Intent(context, MainActivity.class);
                    startActivity(intent);
                    Objects.requireNonNull(getActivity()).finishAffinity();
                }

                @Override
                public void onInvalidCurrentPassword() {
                    setSavePasswordButtonState(true);
                    Snackbar.make(mAnchorLayout, R.string.invalid_current_password, Snackbar.LENGTH_LONG).show();
                }

                @Override
                public void onFailed(String message) {
                    setSavePasswordButtonState(true);
                    Snackbar.make(mAnchorLayout, message, Snackbar.LENGTH_LONG).show();
                }

                @Override
                public void onError(String message) {
                    setSavePasswordButtonState(true);
                    Snackbar.make(mAnchorLayout, message, Snackbar.LENGTH_LONG).show();
                }
            });
        } else {
            setSavePasswordButtonState(true);
        }
    }
}
