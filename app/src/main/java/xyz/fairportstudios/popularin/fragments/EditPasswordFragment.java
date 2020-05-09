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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.apis.popularin.put.UpdatePassword;

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
                                Objects.requireNonNull(getFragmentManager()).popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
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

        return view;
    }
}
