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
import xyz.fairportstudios.popularin.apis.tmdb.get.AiringFilmRequest;
import xyz.fairportstudios.popularin.models.Film;

public class AiringFragment extends Fragment {
    private CoordinatorLayout layout;
    private ProgressBar progressBar;
    private TextView emptyResult;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.global_recycler, container, false);

        // Binding
        layout = view.findViewById(R.id.layout_gr_anchor);
        progressBar = view.findViewById(R.id.pbr_gr_layout);
        emptyResult = view.findViewById(R.id.text_gr_empty);
        Context context = getActivity();
        RecyclerView recyclerView = view.findViewById(R.id.recycler_gr_layout);

        // List
        List<Film> filmList = new ArrayList<>();

        // GET
        AiringFilmRequest airingFilmRequest = new AiringFilmRequest(context, filmList, recyclerView);
        airingFilmRequest.sendRequest(new AiringFilmRequest.APICallback() {
            @Override
            public void onSuccess() {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onEmpty() {
                progressBar.setVisibility(View.GONE);
                emptyResult.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError() {
                progressBar.setVisibility(View.GONE);
                emptyResult.setVisibility(View.VISIBLE);
                Snackbar.make(layout, R.string.get_error, Snackbar.LENGTH_LONG).show();
            }
        });

        return view;
    }
}
