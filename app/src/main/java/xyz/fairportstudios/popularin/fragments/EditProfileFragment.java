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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.apis.popularin.get.UserSelf;
import xyz.fairportstudios.popularin.apis.popularin.put.UpdateProfileRequest;

public class EditProfileFragment extends Fragment {
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
        layout = view.findViewById(R.id.layout_fep_anchor);
        inputFirstName = view.findViewById(R.id.text_fep_first_name);
        inputLastName = view.findViewById(R.id.text_fep_last_name);
        inputUsername = view.findViewById(R.id.text_fep_username);
        inputEmail = view.findViewById(R.id.text_fep_email);
        Button buttonSave = view.findViewById(R.id.button_fep_save);
        Button buttonEditPassword = view.findViewById(R.id.button_fep_edit_password);

        // Mendapatkan data
        UserSelf userSelf = new UserSelf(context);
        userSelf.sendRequest(new UserSelf.JSONCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    JSONObject jsonObjectResult = response.getJSONObject("result");
                    inputFirstName.setText(jsonObjectResult.getString("first_name"));
                    inputLastName.setText(jsonObjectResult.getString("last_name"));
                    inputUsername.setText(jsonObjectResult.getString("username"));
                    inputEmail.setText(jsonObjectResult.getString("email"));
                } catch (JSONException error) {
                    error.printStackTrace();
                }
            }
        });

        // Activity
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Menyimpan data
                String firstName = Objects.requireNonNull(inputFirstName.getText()).toString();
                String lastName = Objects.requireNonNull(inputLastName.getText()).toString();
                String username = Objects.requireNonNull(inputUsername.getText()).toString();
                String email = Objects.requireNonNull(inputEmail.getText()).toString();

                // PUT
                UpdateProfileRequest updateProfileRequest = new UpdateProfileRequest(context, firstName, lastName, username, email);
                updateProfileRequest.sendRequest(new UpdateProfileRequest.APICallback() {
                    @Override
                    public void onSuccess() {
                        Objects.requireNonNull(getFragmentManager()).popBackStack();
                    }

                    @Override
                    public void onFailed(String message) {
                        Snackbar.make(layout, message, Snackbar.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError() {
                        Snackbar.make(layout, R.string.failed_update_profile, Snackbar.LENGTH_LONG).show();
                    }
                });
            }
        });

        buttonEditPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getFragmentManager() != null) {
                    getFragmentManager().beginTransaction()
                            .replace(R.id.fragment_am_container, new EditPasswordFragment())
                            .addToBackStack(null)
                            .commit();
                }
            }
        });

        return view;
    }
}
