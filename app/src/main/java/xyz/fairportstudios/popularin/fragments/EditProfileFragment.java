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
    // Variable member
    private Button mButtonSaveProfile;
    private LinearLayout mAnchorLayout;
    private String mFullName;
    private String mUsername;
    private String mEmail;
    private TextInputEditText mInputFullName;
    private TextInputEditText mInputUsername;
    private TextInputEditText mInputEmail;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        // Context
        final Context context = getActivity();

        // Binding
        mButtonSaveProfile = view.findViewById(R.id.button_fep_save_profile);
        mAnchorLayout = view.findViewById(R.id.anchor_fep_layout);
        mInputFullName = view.findViewById(R.id.input_fep_full_name);
        mInputUsername = view.findViewById(R.id.input_fep_username);
        mInputEmail = view.findViewById(R.id.input_fep_email);
        Button buttonEditPassword = view.findViewById(R.id.button_fep_edit_password);
        TextView textWelcome = view.findViewById(R.id.text_fep_welcome);

        // Pesan
        textWelcome.setText(getWelcomeMessage());

        // Menampilkan data diri awal
        getSelfDetail(context);

        // Text watcher
        mInputFullName.addTextChangedListener(editProfileWatcher);
        mInputUsername.addTextChangedListener(editProfileWatcher);
        mInputEmail.addTextChangedListener(editProfileWatcher);

        // Activity
        mButtonSaveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSaveProfileButtonState(false);
                saveProfile(context);
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

    private SpannableString getWelcomeMessage() {
        String welcomeMessage = getString(R.string.edit_profile_welcome_message);
        SpannableString spannableString = new SpannableString(welcomeMessage);
        RelativeSizeSpan relativeSizeSpan = new RelativeSizeSpan(2f);
        spannableString.setSpan(relativeSizeSpan, 0, 4, 0);
        return spannableString;
    }

    private TextWatcher editProfileWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            // Tidak digunakan
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            mFullName = Objects.requireNonNull(mInputFullName.getText()).toString();
            mUsername = Objects.requireNonNull(mInputUsername.getText()).toString();
            mEmail = Objects.requireNonNull(mInputEmail.getText()).toString();
            mButtonSaveProfile.setEnabled(!mFullName.isEmpty() && !mUsername.isEmpty() && !mEmail.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // Tidak digunakan
        }
    };

    private void getSelfDetail(Context context) {
        SelfDetailRequest selfDetailRequest = new SelfDetailRequest(context);
        selfDetailRequest.sendRequest(new SelfDetailRequest.Callback() {
            @Override
            public void onSuccess(SelfDetail selfDetail) {
                mInputFullName.setText(selfDetail.getFull_name());
                mInputUsername.setText(selfDetail.getUsername());
                mInputEmail.setText(selfDetail.getEmail());
            }

            @Override
            public void onError(String message) {
                Snackbar.make(mAnchorLayout, message, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private boolean usernameValidated() {
        if (mUsername.length() < 5) {
            Snackbar.make(mAnchorLayout, R.string.validate_username_length, Snackbar.LENGTH_LONG).show();
            return false;
        } else if (mUsername.contains(" ")) {
            Snackbar.make(mAnchorLayout, R.string.validate_alpha_dash, Snackbar.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    private boolean emailValidated() {
        if (!Patterns.EMAIL_ADDRESS.matcher(mEmail).matches()) {
            Snackbar.make(mAnchorLayout, R.string.validate_email_format, Snackbar.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    private void setSaveProfileButtonState(boolean enable) {
        mButtonSaveProfile.setEnabled(enable);
        if (enable) {
            mButtonSaveProfile.setText(R.string.save_profile);
        } else {
            mButtonSaveProfile.setText(R.string.loading);
        }
    }

    private void saveProfile(final Context context) {
        if (usernameValidated() && emailValidated()) {
            UpdateProfileRequest updateProfileRequest = new UpdateProfileRequest(context, mFullName, mUsername, mEmail);
            updateProfileRequest.sendRequest(new UpdateProfileRequest.Callback() {
                @Override
                public void onSuccess() {
                    Intent intent = new Intent(context, MainActivity.class);
                    startActivity(intent);
                    Objects.requireNonNull(getActivity()).finishAffinity();
                }

                @Override
                public void onFailed(String message) {
                    setSaveProfileButtonState(true);
                    Snackbar.make(mAnchorLayout, message, Snackbar.LENGTH_LONG).show();
                }

                @Override
                public void onError(String message) {
                    setSaveProfileButtonState(true);
                    Snackbar.make(mAnchorLayout, message, Snackbar.LENGTH_LONG).show();
                }
            });
        } else {
            setSaveProfileButtonState(true);
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
