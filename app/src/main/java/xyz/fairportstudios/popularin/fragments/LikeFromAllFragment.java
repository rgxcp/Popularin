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
import xyz.fairportstudios.popularin.apis.popularin.get.LikeFromAllRequest;
import xyz.fairportstudios.popularin.models.User;

public class LikeFromAllFragment extends Fragment {
    // Member variable
    private Context context;
    private CoordinatorLayout anchorLayout;
    private ProgressBar progressBar;
    private RecyclerView recyclerUser;
    private TextView textEmptyResult;

    // Constructor variable
    private String reviewID;

    public LikeFromAllFragment(String reviewID) {
        this.reviewID = reviewID;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reusable_recycler, container, false);

        // Binding
        context = getActivity();
        anchorLayout = view.findViewById(R.id.anchor_rr_layout);
        progressBar = view.findViewById(R.id.pbr_rr_layout);
        recyclerUser = view.findViewById(R.id.recycler_rr_layout);
        textEmptyResult = view.findViewById(R.id.text_rr_message);

        // Mendapatkan data
        getAllLike();

        return view;
    }

    private void getAllLike() {
        List<User> userList = new ArrayList<>();
        LikeFromAllRequest likeFromAllRequest = new LikeFromAllRequest(context, userList, recyclerUser);
        String requestURL = likeFromAllRequest.getRequestURL(reviewID, 1);
        likeFromAllRequest.sendRequest(requestURL, new LikeFromAllRequest.APICallback() {
            @Override
            public void onSuccess() {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onEmpty() {
                progressBar.setVisibility(View.GONE);
                textEmptyResult.setVisibility(View.VISIBLE);
                textEmptyResult.setText(R.string.empty_like);
            }

            @Override
            public void onError() {
                progressBar.setVisibility(View.GONE);
                textEmptyResult.setVisibility(View.VISIBLE);
                textEmptyResult.setText(R.string.not_found);
                Snackbar.make(anchorLayout, R.string.network_error, Snackbar.LENGTH_LONG).show();
            }
        });
    }
}
