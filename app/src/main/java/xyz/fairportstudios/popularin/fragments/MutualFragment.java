package xyz.fairportstudios.popularin.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.apis.popularin.get.UserMutualRequest;
import xyz.fairportstudios.popularin.models.User;

public class MutualFragment extends Fragment {
    // Untuk fitur onResume
    // private Integer onResumeCount = 0;

    // Mmeber variable
    private Context context;
    private CoordinatorLayout anchorLayout;
    private List<User> userList;
    private ProgressBar progressBar;
    private RecyclerView recyclerMutual;
    private TextView textEmptyMutual;

    // Constructor variable
    private String userID;

    public MutualFragment(String userID) {
        this.userID = userID;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reusable_recycler, container, false);

        // Binding
        context = getActivity();
        anchorLayout = view.findViewById(R.id.anchor_rr_layout);
        progressBar = view.findViewById(R.id.pbr_rr_layout);
        recyclerMutual = view.findViewById(R.id.recycler_rr_layout);
        textEmptyMutual = view.findViewById(R.id.text_rr_empty_result);

        // Mendapatkan data
        userList = new ArrayList<>();
        getUserMutual();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        /*
        onResumeCount++;
        if (onResumeCount >= 1) {
            getUserMutual();
        }
         */
    }

    private void getUserMutual() {
        UserMutualRequest userMutualRequest = new UserMutualRequest(context, userID, userList, recyclerMutual);
        String requestURL = userMutualRequest.getRequestURL(1);
        userMutualRequest.sendRequest(requestURL, new UserMutualRequest.APICallback() {
            @Override
            public void onSuccess() {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onEmpty() {
                progressBar.setVisibility(View.GONE);
                textEmptyMutual.setVisibility(View.VISIBLE);
                textEmptyMutual.setText(R.string.empty_mutual);
            }

            @Override
            public void onError() {
                progressBar.setVisibility(View.GONE);
                textEmptyMutual.setVisibility(View.VISIBLE);
                textEmptyMutual.setText(R.string.not_found);
                Snackbar.make(anchorLayout, R.string.network_error, Snackbar.LENGTH_LONG).show();
            }
        });
    }
}
