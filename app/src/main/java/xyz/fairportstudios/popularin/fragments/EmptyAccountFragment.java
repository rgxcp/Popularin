package xyz.fairportstudios.popularin.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.activities.SignInActivity;
import xyz.fairportstudios.popularin.activities.SignUpActivity;

public class EmptyAccountFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reusable_empty_account, container, false);

        // Binding
        Button buttonSignIn = view.findViewById(R.id.button_rea_sign_in);
        Button buttonSignUp = view.findViewById(R.id.button_rea_sign_up);

        // Activity
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoSignIn();
            }
        });

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoSignUp();
            }
        });

        return view;
    }

    private void gotoSignIn() {
        Intent intent = new Intent(getActivity(), SignInActivity.class);
        startActivity(intent);
    }

    private void gotoSignUp() {
        Intent intent = new Intent(getActivity(), SignUpActivity.class);
        startActivity(intent);
    }
}
