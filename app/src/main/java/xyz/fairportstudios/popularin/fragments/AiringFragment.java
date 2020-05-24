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
    private Context context;
    private CoordinatorLayout anchorLayout;
    private ProgressBar progressBar;
    private RecyclerView recyclerFilm;
    private TextView textEmptyResult;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reusable_recycler, container, false);

        // Binding
        context = getActivity();
        anchorLayout = view.findViewById(R.id.anchor_rr_layout);
        progressBar = view.findViewById(R.id.pbr_rr_layout);
        recyclerFilm = view.findViewById(R.id.recycler_rr_layout);
        textEmptyResult = view.findViewById(R.id.text_rr_empty_result);

        // Mendapatkan data
        getAiringFilm();

        return view;
    }

    private void getAiringFilm() {
        List<Film> filmList = new ArrayList<>();
        AiringFilmRequest airingFilmRequest = new AiringFilmRequest(context, filmList, recyclerFilm);
        airingFilmRequest.sendRequest(new AiringFilmRequest.APICallback() {
            @Override
            public void onSuccess() {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onEmpty() {
                progressBar.setVisibility(View.GONE);
                textEmptyResult.setVisibility(View.VISIBLE);
                textEmptyResult.setText(R.string.empty_airing);
            }

            @Override
            public void onError() {
                progressBar.setVisibility(View.GONE);
                textEmptyResult.setVisibility(View.VISIBLE);
                textEmptyResult.setText(R.string.empty_airing);
                Snackbar.make(anchorLayout, R.string.network_error, Snackbar.LENGTH_LONG).show();
            }
        });
    }
}
