package xyz.fairportstudios.popularin.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.apis.popularin.put.UpdatePasswordRequest;

public class EditPasswordFragment extends Fragment {
    private Button buttonSave;
    private Context context;
    private CoordinatorLayout layout;
    private TextInputEditText inputCurrentPassword;
    private TextInputEditText inputNewPassword;
    private TextInputEditText inputConfirmPassword;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_password, container, false);

        // Binding
        context = getActivity();
        buttonSave = view.findViewById(R.id.button_fepw_save);
        layout = view.findViewById(R.id.layout_fepw_anchor);
        inputCurrentPassword = view.findViewById(R.id.input_fepw_current_password);
        inputNewPassword = view.findViewById(R.id.input_fepw_new_password);
        inputConfirmPassword = view.findViewById(R.id.input_fepw_confirm_password);

        // Activity
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonSave.setEnabled(false);
                buttonSave.setText(R.string.loading);
                updatePassword();
            }
        });

        return view;
    }

    private void updatePassword() {
        // Menyimpan data
        String currentPassword = Objects.requireNonNull(inputCurrentPassword.getText()).toString();
        String newPassword = Objects.requireNonNull(inputNewPassword.getText()).toString();
        String confirmPassword = Objects.requireNonNull(inputConfirmPassword.getText()).toString();

        // Membuat objek
        UpdatePasswordRequest updatePasswordRequest = new UpdatePasswordRequest(
                context,
                currentPassword,
                newPassword,
                confirmPassword
        );

        // Mengirim request
        updatePasswordRequest.sendRequest(new UpdatePasswordRequest.APICallback() {
            @Override
            public void onSuccess() {
                if (getFragmentManager() != null) {
                    getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
            }

            @Override
            public void onInvalid() {
                buttonSave.setEnabled(true);
                buttonSave.setText(R.string.save_password);
                Snackbar.make(layout, R.string.invalid_current_password, Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onFailed(String message) {
                buttonSave.setEnabled(true);
                buttonSave.setText(R.string.save_password);
                Snackbar.make(layout, message, Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onError() {
                buttonSave.setEnabled(true);
                buttonSave.setText(R.string.save_password);
                Snackbar.make(layout, R.string.failed_update_password, Snackbar.LENGTH_LONG).show();
            }
        });
    }
}
