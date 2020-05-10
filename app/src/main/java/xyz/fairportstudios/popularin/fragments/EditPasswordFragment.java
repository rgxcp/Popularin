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

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.apis.popularin.put.UpdatePasswordRequest;

public class EditPasswordFragment extends Fragment {
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
        layout = view.findViewById(R.id.layout_fepw_anchor);
        inputCurrentPassword = view.findViewById(R.id.text_fepw_current_password);
        inputNewPassword = view.findViewById(R.id.text_fepw_new_password);
        inputConfirmPassword = view.findViewById(R.id.text_fepw_confirm_password);
        Button buttonSave = view.findViewById(R.id.button_fepw_save);

        // Activity
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Menyimpan data
                String currentPassword = Objects.requireNonNull(inputCurrentPassword.getText()).toString();
                String newPassword = Objects.requireNonNull(inputNewPassword.getText()).toString();
                String confirmPassword = Objects.requireNonNull(inputConfirmPassword.getText()).toString();

                // PUT
                UpdatePasswordRequest updatePasswordRequest = new UpdatePasswordRequest(context, currentPassword, newPassword, confirmPassword);
                updatePasswordRequest.sendRequest(new UpdatePasswordRequest.APICallback() {
                    @Override
                    public void onSuccess() {
                        Objects.requireNonNull(getFragmentManager()).popBackStack();
                    }

                    @Override
                    public void onInvalid() {
                        Snackbar.make(layout, R.string.invalid_password, Snackbar.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailed(String message) {
                        Snackbar.make(layout, message, Snackbar.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError() {
                        Snackbar.make(layout, R.string.failed_update_password, Snackbar.LENGTH_LONG).show();
                    }
                });
            }
        });

        return view;
    }
}
