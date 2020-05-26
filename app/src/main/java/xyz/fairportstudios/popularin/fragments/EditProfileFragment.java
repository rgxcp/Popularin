package xyz.fairportstudios.popularin.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.RelativeSizeSpan;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
    private Button buttonSaveProfile;
    private Context context;
    private LinearLayout anchorLayout;
    private String fullName;
    private String username;
    private String email;
    private TextInputEditText inputFullName;
    private TextInputEditText inputUsername;
    private TextInputEditText inputEmail;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        // Binding
        context = getActivity();
        buttonSaveProfile = view.findViewById(R.id.button_fep_save_profile);
        anchorLayout = view.findViewById(R.id.anchor_fep_layout);
        inputFullName = view.findViewById(R.id.input_fep_full_name);
        inputUsername = view.findViewById(R.id.input_fep_username);
        inputEmail = view.findViewById(R.id.input_fep_email);
        Button buttonEditPassword = view.findViewById(R.id.button_fep_edit_password);
        TextView textWelcome = view.findViewById(R.id.text_fep_welcome);

        // Pesan
        String welcome = getString(R.string.edit_profile_welcome);
        SpannableString spannableString = new SpannableString(welcome);
        RelativeSizeSpan relativeSizeSpan = new RelativeSizeSpan(2f);
        spannableString.setSpan(relativeSizeSpan, 0, 4, 0);
        textWelcome.setText(spannableString);

        // Request
        getSelfDetail();

        // Text watcher
        inputFullName.addTextChangedListener(editProfileWatcher);
        inputUsername.addTextChangedListener(editProfileWatcher);
        inputEmail.addTextChangedListener(editProfileWatcher);

        // Activity
        buttonSaveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveProfile();
            }
        });

        buttonEditPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoEditPassword();
            }
        });

        return view;
    }

    private TextWatcher editProfileWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            // Tidak digunakan
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            fullName = Objects.requireNonNull(inputFullName.getText()).toString();
            username = Objects.requireNonNull(inputUsername.getText()).toString();
            email = Objects.requireNonNull(inputEmail.getText()).toString();
            buttonSaveProfile.setEnabled(!fullName.isEmpty() && !username.isEmpty() && !email.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // Tidak digunakan
        }
    };

    private void getSelfDetail() {
        SelfDetailRequest selfDetailRequest = new SelfDetailRequest(context);
        selfDetailRequest.sendRequest(new SelfDetailRequest.APICallback() {
            @Override
            public void onSuccess(SelfDetail selfDetail) {
                inputFullName.setText(selfDetail.getFull_name());
                inputUsername.setText(selfDetail.getUsername());
                inputEmail.setText(selfDetail.getEmail());
            }

            @Override
            public void onError() {
                Snackbar.make(anchorLayout, R.string.network_error, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private boolean validateFullName() {
        if (!fullName.contains(" ")) {
            Snackbar.make(anchorLayout, R.string.validate_full_name, Snackbar.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    private boolean validateUsername() {
        if (username.length() < 5) {
            Snackbar.make(anchorLayout, R.string.validate_username, Snackbar.LENGTH_LONG).show();
            return false;
        } else if (username.contains(" ")) {
            Snackbar.make(anchorLayout, R.string.validate_alpha_dash, Snackbar.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    private boolean validateEmail() {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Snackbar.make(anchorLayout, R.string.validate_email, Snackbar.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    private void saveProfile() {
        buttonSaveProfile.setEnabled(false);
        buttonSaveProfile.setText(R.string.loading);

        if (validateFullName() && validateUsername() && validateEmail()) {
            UpdateProfileRequest updateProfileRequest = new UpdateProfileRequest(context, fullName, username, email);
            updateProfileRequest.sendRequest(new UpdateProfileRequest.APICallback() {
                @Override
                public void onSuccess() {
                    Intent intent = new Intent(context, MainActivity.class);
                    startActivity(intent);
                    Objects.requireNonNull(getActivity()).finishAffinity();
                }

                @Override
                public void onFailed(String message) {
                    buttonSaveProfile.setEnabled(true);
                    buttonSaveProfile.setText(R.string.save_profile);
                    Snackbar.make(anchorLayout, message, Snackbar.LENGTH_LONG).show();
                }

                @Override
                public void onError() {
                    buttonSaveProfile.setEnabled(true);
                    buttonSaveProfile.setText(R.string.save_profile);
                    Snackbar.make(anchorLayout, R.string.failed_update_profile, Snackbar.LENGTH_LONG).show();
                }
            });
        } else {
            buttonSaveProfile.setEnabled(true);
            buttonSaveProfile.setText(R.string.save_profile);
        }
    }

    private void gotoEditPassword() {
        if (getFragmentManager() != null) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.fragment_aep_container, new EditPasswordFragment())
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .addToBackStack(null)
                    .commit();
        }
    }
}
