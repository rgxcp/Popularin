package xyz.fairportstudios.popularin.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.apis.popularin.get.ReviewLikedRequest;
import xyz.fairportstudios.popularin.models.FilmReview;

public class LikedReviewFragment extends Fragment {
    private CoordinatorLayout layout;
    private ProgressBar progressBar;
    private String filmID;

    public LikedReviewFragment(String filmID) {
        this.filmID = filmID;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reusable_recycler, container, false);

        // Binding
        layout = view.findViewById(R.id.anchor_rr_layout);
        progressBar = view.findViewById(R.id.pbr_rr_layout);
        Context context = getActivity();
        RecyclerView recyclerView = view.findViewById(R.id.recycler_rr_layout);

        // List
        List<FilmReview> filmReviewList = new ArrayList<>();

        // GET
        ReviewLikedRequest reviewLikedRequest = new ReviewLikedRequest(context, filmReviewList, recyclerView);
        String requestURL = reviewLikedRequest.getRequestURL(filmID, 1);
        reviewLikedRequest.sendRequest(requestURL, new ReviewLikedRequest.APICallback() {
            @Override
            public void onSuccess() {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onEmpty() {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError() {
                progressBar.setVisibility(View.GONE);
                Snackbar.make(layout, R.string.network_error, Snackbar.LENGTH_LONG).show();
            }
        });

        return view;
    }
}
