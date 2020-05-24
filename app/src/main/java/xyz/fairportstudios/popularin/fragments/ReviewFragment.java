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
import xyz.fairportstudios.popularin.apis.popularin.get.ReviewRequest;
import xyz.fairportstudios.popularin.models.Review;

public class ReviewFragment extends Fragment {
    // Untuk fitur load more
    private Integer currentPage = 1;

    // Member variable
    private Context context;
    private CoordinatorLayout anchorLayout;
    private List<Review> reviewList;
    private ProgressBar progressBar;
    private RecyclerView recyclerReview;
    private TextView textEmptyReview;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reusable_recycler, container, false);

        // Binding
        context = getActivity();
        anchorLayout = view.findViewById(R.id.anchor_rr_layout);
        progressBar = view.findViewById(R.id.pbr_rr_layout);
        recyclerReview = view.findViewById(R.id.recycler_rr_layout);
        textEmptyReview = view.findViewById(R.id.text_rr_empty_result);

        // Request
        reviewList = new ArrayList<>();
        getAllReview(currentPage);

        // Activity
        /*
        recyclerReview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    getAllReview(currentPage);
                }
            }
        });
         */

        return view;
    }

    private void getAllReview(Integer page) {
        ReviewRequest reviewRequest = new ReviewRequest(context, reviewList, recyclerReview);
        String requestURL = reviewRequest.getRequestURL(page);
        reviewRequest.sendRequest(requestURL, new ReviewRequest.APICallback() {
            @Override
            public void onSuccess() {
                progressBar.setVisibility(View.GONE);
                currentPage++;
            }

            @Override
            public void onEmpty() {
                progressBar.setVisibility(View.GONE);
                textEmptyReview.setVisibility(View.VISIBLE);
                textEmptyReview.setText(R.string.empty_review);
            }

            @Override
            public void onError() {
                progressBar.setVisibility(View.GONE);
                textEmptyReview.setVisibility(View.VISIBLE);
                textEmptyReview.setText(R.string.empty_review);
                Snackbar.make(anchorLayout, R.string.network_error, Snackbar.LENGTH_LONG).show();
            }
        });
    }
}
