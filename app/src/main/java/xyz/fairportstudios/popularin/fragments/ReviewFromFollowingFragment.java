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
import xyz.fairportstudios.popularin.apis.popularin.get.ReviewFromFollowingRequest;
import xyz.fairportstudios.popularin.models.FilmReview;

public class ReviewFromFollowingFragment extends Fragment {
    private CoordinatorLayout layout;
    private ProgressBar progressBar;
    private String filmID;

    public ReviewFromFollowingFragment(String filmID) {
        this.filmID = filmID;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reusable_recycler, container, false);

        // Binding
        layout = view.findViewById(R.id.layout_rr_anchor);
        progressBar = view.findViewById(R.id.pbr_rr_layout);
        Context context = getActivity();
        RecyclerView recyclerView = view.findViewById(R.id.recycler_rr_layout);

        // List
        List<FilmReview> filmReviewList = new ArrayList<>();

        // GET
        ReviewFromFollowingRequest reviewFromFollowingRequest = new ReviewFromFollowingRequest(context, filmReviewList, recyclerView);
        String requestURL = reviewFromFollowingRequest.getRequestURL(filmID, 1);
        reviewFromFollowingRequest.sendRequest(requestURL, new ReviewFromFollowingRequest.APICallback() {
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
                Snackbar.make(layout, R.string.get_error, Snackbar.LENGTH_LONG).show();
            }
        });

        return view;
    }
}
