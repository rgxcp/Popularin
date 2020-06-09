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
import androidx.fragment.app.Fragment;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.activities.SignInActivity;
import xyz.fairportstudios.popularin.activities.SignUpActivity;

public class EmptyAccountFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reusable_empty_account, container, false);

        // Context
        final Context context = getActivity();

        // Activity
        Button buttonSignIn = view.findViewById(R.id.button_rea_sign_in);
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoSignIn(context);
            }
        });

        Button buttonSignUp = view.findViewById(R.id.button_rea_sign_up);
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoSignUp(context);
            }
        });

        return view;
    }

    private void gotoSignIn(Context context) {
        Intent intent = new Intent(context, SignInActivity.class);
        startActivity(intent);
    }

    private void gotoSignUp(Context context) {
        Intent intent = new Intent(context, SignUpActivity.class);
        startActivity(intent);
    }
}
