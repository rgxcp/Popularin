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
import xyz.fairportstudios.popularin.apis.popularin.get.UserFollowingRequest;
import xyz.fairportstudios.popularin.models.User;

public class FollowingFragment extends Fragment {
    // Untuk fitur onCreate & onResume
    // private Integer onCreateCount = 0;
    // private Integer onResumeCount = 0;

    // Member variable
    private Context context;
    private CoordinatorLayout anchorLayout;
    private List<User> userList;
    private ProgressBar progressBar;
    private RecyclerView recyclerFollowing;
    private TextView textEmptyFollowing;

    // Constructor variable
    private String userID;

    public FollowingFragment(String userID) {
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
        recyclerFollowing = view.findViewById(R.id.recycler_rr_layout);
        textEmptyFollowing = view.findViewById(R.id.text_rr_empty_result);

        // Mendapatkan data
        userList = new ArrayList<>();
        getUserFollowing();
        /*
        if (onCreateCount == 0) {
            getUserFollowing();
            onCreateCount++;
        }
         */

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        /*
        onResumeCount++;
        if (onResumeCount >= 2) {
            getUserFollowing();
        }
         */
    }

    private void getUserFollowing() {
        UserFollowingRequest userFollowingRequest = new UserFollowingRequest(context, userID, userList, recyclerFollowing);
        String requestURL = userFollowingRequest.getRequestURL(1);
        userFollowingRequest.sendRequest(requestURL, new UserFollowingRequest.APICallback() {
            @Override
            public void onSuccess() {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onEmpty() {
                progressBar.setVisibility(View.GONE);
                textEmptyFollowing.setVisibility(View.VISIBLE);
                textEmptyFollowing.setText(R.string.empty_following);
            }

            @Override
            public void onError() {
                progressBar.setVisibility(View.GONE);
                textEmptyFollowing.setVisibility(View.VISIBLE);
                textEmptyFollowing.setText(R.string.not_found);
                Snackbar.make(anchorLayout, R.string.network_error, Snackbar.LENGTH_LONG).show();
            }
        });
    }
}
