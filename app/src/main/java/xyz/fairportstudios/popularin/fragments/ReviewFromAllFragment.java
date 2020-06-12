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
import xyz.fairportstudios.popularin.apis.popularin.get.ReviewFromAllRequest;
import xyz.fairportstudios.popularin.models.FilmReview;

public class ReviewFromAllFragment extends Fragment {
    // Variable untuk fitur load more
    private Integer currentPage = 1;
    private Integer totalPage = 1;

    // Variable member
    private CoordinatorLayout anchorLayout;
    private ProgressBar progressBar;
    private ReviewFromAllRequest reviewFromAllRequest;
    private TextView textMessage;

    // Variable untuk constructor
    private String filmID;

    public ReviewFromAllFragment(String filmID) {
        this.filmID = filmID;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reusable_recycler, container, false);

        // Binding
        anchorLayout = view.findViewById(R.id.anchor_rr_layout);
        progressBar = view.findViewById(R.id.pbr_rr_layout);
        textMessage = view.findViewById(R.id.text_rr_message);
        Context context = getActivity();
        RecyclerView recyclerFilmReview = view.findViewById(R.id.recycler_rr_layout);

        // Request
        List<FilmReview> filmReviewList = new ArrayList<>();
        reviewFromAllRequest = new ReviewFromAllRequest(context, filmID, filmReviewList, recyclerFilmReview);
        getAllFilmReview(1);

        // Activity
        recyclerFilmReview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (currentPage < totalPage) {
                        getAllFilmReview(currentPage);
                    }
                }
            }
        });

        return view;
    }

    private void getAllFilmReview(Integer page) {
        reviewFromAllRequest.sendRequest(page, new ReviewFromAllRequest.APICallback() {
            @Override
            public void onSuccess(Integer lastPage) {
                totalPage = lastPage;
                currentPage++;
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onEmpty() {
                progressBar.setVisibility(View.GONE);
                textMessage.setVisibility(View.VISIBLE);
                textMessage.setText(R.string.empty_film_review);
            }

            @Override
            public void onError() {
                progressBar.setVisibility(View.GONE);
                textMessage.setVisibility(View.VISIBLE);
                textMessage.setText(R.string.empty_film_review);
                Snackbar.make(anchorLayout, R.string.network_error, Snackbar.LENGTH_LONG).show();
            }
        });
    }
}
