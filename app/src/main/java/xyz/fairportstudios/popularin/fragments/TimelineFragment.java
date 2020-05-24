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
import xyz.fairportstudios.popularin.apis.popularin.get.TimelineRequest;
import xyz.fairportstudios.popularin.models.Review;

public class TimelineFragment extends Fragment {
    private Context context;
    private CoordinatorLayout anchorLayout;
    private ProgressBar progressBar;
    private RecyclerView recyclerTimeline;
    private TextView textEmptyTimeline;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reusable_recycler, container, false);

        // Binding
        context = getActivity();
        anchorLayout = view.findViewById(R.id.anchor_rr_layout);
        progressBar = view.findViewById(R.id.pbr_rr_layout);
        recyclerTimeline = view.findViewById(R.id.recycler_rr_layout);
        textEmptyTimeline = view.findViewById(R.id.text_rr_empty_result);

        // Request
        getTimeline();

        return view;
    }

    private void getTimeline() {
        List<Review> reviewList = new ArrayList<>();
        TimelineRequest timelineRequest = new TimelineRequest(context, reviewList, recyclerTimeline);
        timelineRequest.sendRequest(new TimelineRequest.APICallback() {
            @Override
            public void onSuccess() {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onEmpty() {
                progressBar.setVisibility(View.GONE);
                textEmptyTimeline.setVisibility(View.VISIBLE);
                textEmptyTimeline.setText(R.string.empty_timeline);
            }

            @Override
            public void onError() {
                progressBar.setVisibility(View.GONE);
                textEmptyTimeline.setVisibility(View.VISIBLE);
                textEmptyTimeline.setText(R.string.empty_timeline);
                Snackbar.make(anchorLayout, R.string.network_error, Snackbar.LENGTH_LONG).show();
            }
        });
    }
}
