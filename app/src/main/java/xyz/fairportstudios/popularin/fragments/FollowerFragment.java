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
import xyz.fairportstudios.popularin.apis.popularin.get.UserFollowerRequest;
import xyz.fairportstudios.popularin.models.User;

public class FollowerFragment extends Fragment {
    // Untuk fitur onResume
    private Boolean firstTime = true;

    // Mmeber variable
    private Context context;
    private CoordinatorLayout anchorLayout;
    private List<User> userList;
    private ProgressBar progressBar;
    private RecyclerView recyclerFollower;
    private TextView textEmptyFollower;

    // Constructor variable
    private String userID;

    public FollowerFragment(String userID) {
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
        recyclerFollower = view.findViewById(R.id.recycler_rr_layout);
        textEmptyFollower = view.findViewById(R.id.text_rr_empty_result);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (firstTime) {
            userList = new ArrayList<>();
            getUserFollower();
            firstTime = false;
        }
    }

    private void getUserFollower() {
        UserFollowerRequest userFollowerRequest = new UserFollowerRequest(context, userID, userList, recyclerFollower);
        String requestURL = userFollowerRequest.getRequestURL(1);
        userFollowerRequest.sendRequest(requestURL, new UserFollowerRequest.APICallback() {
            @Override
            public void onSuccess() {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onEmpty() {
                progressBar.setVisibility(View.GONE);
                textEmptyFollower.setVisibility(View.VISIBLE);
                textEmptyFollower.setText(R.string.empty_follower);
            }

            @Override
            public void onError() {
                progressBar.setVisibility(View.GONE);
                textEmptyFollower.setVisibility(View.VISIBLE);
                textEmptyFollower.setText(R.string.not_found);
                Snackbar.make(anchorLayout, R.string.network_error, Snackbar.LENGTH_LONG).show();
            }
        });
    }
}
