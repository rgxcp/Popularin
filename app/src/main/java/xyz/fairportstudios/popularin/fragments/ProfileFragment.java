package xyz.fairportstudios.popularin.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Objects;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.activities.SignInActivity;
import xyz.fairportstudios.popularin.activities.SignUpActivity;
import xyz.fairportstudios.popularin.apis.popularin.Popularin;
import xyz.fairportstudios.popularin.apis.popularin.SignOut;

public class ProfileFragment extends Fragment {
    private Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        context = getActivity();
        Button buttonSignUp = view.findViewById(R.id.btn_fp_sign_up);
        Button buttonSignIn = view.findViewById(R.id.btn_fp_sign_in);
        Button buttonSignOut = view.findViewById(R.id.btn_fp_sign_out);

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoSignUp = new Intent(getActivity(), SignUpActivity.class);
                startActivity(gotoSignUp);
            }
        });

        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoSignIn = new Intent(getActivity(), SignInActivity.class);
                startActivity(gotoSignIn);
            }
        });

        buttonSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences("AUTH", Context.MODE_PRIVATE);
                int id = sharedPreferences.getInt("UID", 0);
                String token = sharedPreferences.getString("TOKEN", "");

                SignOut signOut = new SignOut(context, id, token);
                signOut.sendRequest(Popularin.SIGN_OUT_REQUEST);
            }
        });

        return view;
    }
}
