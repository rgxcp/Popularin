package xyz.fairportstudios.popularin.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.apis.popularin.get.UserSelf;
import xyz.fairportstudios.popularin.apis.popularin.put.UpdateProfile;

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
        View view = inflater.inflate(R.layout.activity_edit_profile, container, false);

        // Binding
        context = getActivity();
        layout = view.findViewById(R.id.coordinator_aep_layout);
        inputFirstName = view.findViewById(R.id.text_aep_first_name);
        inputLastName = view.findViewById(R.id.text_aep_last_name);
        inputUsername = view.findViewById(R.id.text_aep_username);
        inputEmail = view.findViewById(R.id.text_aep_email);
        Button buttonSave = view.findViewById(R.id.button_aep_save);
        TextView textEditPassword = view.findViewById(R.id.text_aep_edit_password);

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

                // Mengirim data
                UpdateProfile updateProfile = new UpdateProfile(
                        context,
                        firstName,
                        lastName,
                        username,
                        email
                );

                // Mendapatkan hasil
                updateProfile.sendRequest(new UpdateProfile.JSONCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        try {
                            int status = response.getInt("status");

                            if (status == 303) {
                                Objects.requireNonNull(getFragmentManager()).popBackStack();
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

        textEditPassword.setOnClickListener(new View.OnClickListener() {
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
