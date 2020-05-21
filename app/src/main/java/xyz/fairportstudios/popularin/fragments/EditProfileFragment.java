package xyz.fairportstudios.popularin.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.activities.MainActivity;
import xyz.fairportstudios.popularin.apis.popularin.get.SelfDetailRequest;
import xyz.fairportstudios.popularin.apis.popularin.put.UpdateProfileRequest;
import xyz.fairportstudios.popularin.models.SelfDetail;

public class EditProfileFragment extends Fragment {
    private Button buttonSave;
    private Context context;
    private CoordinatorLayout layout;
    private TextInputEditText inputFirstName;
    private TextInputEditText inputLastName;
    private TextInputEditText inputUsername;
    private TextInputEditText inputEmail;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        // Binding
        context = getActivity();
        buttonSave = view.findViewById(R.id.button_fep_save);
        layout = view.findViewById(R.id.layout_fep_anchor);
        inputFirstName = view.findViewById(R.id.input_fep_first_name);
        inputLastName = view.findViewById(R.id.input_fep_last_name);
        inputUsername = view.findViewById(R.id.text_fep_username);
        inputEmail = view.findViewById(R.id.input_fep_email);
        Button buttonEditPassword = view.findViewById(R.id.button_fep_edit_password);

        // Mendapatkan data awal
        getSelf();

        // Activity
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveProfile();
                buttonSave.setEnabled(false);
                buttonSave.setText(R.string.loading);
            }
        });

        buttonEditPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getFragmentManager() != null) {
                    getFragmentManager().beginTransaction()
                            .replace(R.id.fragment_aep_container, new EditPasswordFragment())
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .addToBackStack(null)
                            .commit();
                }
            }
        });

        return view;
    }

    private void getSelf() {
        // Membuat objek
        SelfDetailRequest selfDetailRequest = new SelfDetailRequest(context);

        // Mengirim request
        selfDetailRequest.sendRequest(new SelfDetailRequest.APICallback() {
            @Override
            public void onSuccess(SelfDetail selfDetail) {
                inputFirstName.setText(selfDetail.getFirst_name());
                inputLastName.setText(selfDetail.getLast_name());
                inputUsername.setText(selfDetail.getUsername());
                inputEmail.setText(selfDetail.getEmail());
                buttonSave.setEnabled(true);
            }

            @Override
            public void onError() {
                Snackbar.make(layout, R.string.network_error, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void saveProfile() {
        // Menyimpan data
        String firstName = Objects.requireNonNull(inputFirstName.getText()).toString();
        String lastName = Objects.requireNonNull(inputLastName.getText()).toString();
        String username = Objects.requireNonNull(inputUsername.getText()).toString();
        String email = Objects.requireNonNull(inputEmail.getText()).toString();

        // Membuat objek
        UpdateProfileRequest updateProfileRequest = new UpdateProfileRequest(
                context,
                firstName,
                lastName,
                username,
                email
        );

        // Mengirim request
        updateProfileRequest.sendRequest(new UpdateProfileRequest.APICallback() {
            @Override
            public void onSuccess() {
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
                Objects.requireNonNull(getActivity()).finish();
            }

            @Override
            public void onFailed(String message) {
                buttonSave.setText(R.string.save_profile);
                Snackbar.make(layout, message, Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onError() {
                buttonSave.setText(R.string.save_profile);
                Snackbar.make(layout, R.string.failed_update_profile, Snackbar.LENGTH_LONG).show();
            }
        });
    }
}
