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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.adapters.FilmAdapter;
import xyz.fairportstudios.popularin.apis.tmdb.get.AiringFilmRequest;
import xyz.fairportstudios.popularin.models.Film;

public class AiringFragment extends Fragment {
    // Variable untuk fitur swipe refresh
    private Boolean isLoadFirstTime = true;

    // Variable member
    private AiringFilmRequest airingFilmRequest;
    private CoordinatorLayout anchorLayout;
    private FilmAdapter filmAdapter;
    private List<Film> filmList;
    private ProgressBar progressBar;
    private RecyclerView recyclerFilm;
    private SwipeRefreshLayout swipeRefresh;
    private TextView textMessage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reusable_recycler, container, false);

        // Context
        final Context context = getActivity();

        // Binding
        anchorLayout = view.findViewById(R.id.anchor_rr_layout);
        progressBar = view.findViewById(R.id.pbr_rr_layout);
        recyclerFilm = view.findViewById(R.id.recycler_rr_layout);
        swipeRefresh = view.findViewById(R.id.swipe_refresh_rr_layout);
        textMessage = view.findViewById(R.id.text_rr_message);

        // Mendapatkan data
        filmList = new ArrayList<>();
        airingFilmRequest = new AiringFilmRequest(context);
        getAiringFilm(context);

        // Activity
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!isLoadFirstTime) {
                    filmList.clear();
                    filmAdapter.notifyDataSetChanged();
                }
                getAiringFilm(context);
                swipeRefresh.setRefreshing(false);
            }
        });

        return view;
    }

    private void getAiringFilm(final Context context) {
        // Menghilangkan pesan setiap kali method dijalankan
        textMessage.setVisibility(View.GONE);

        airingFilmRequest.sendRequest(new AiringFilmRequest.Callback() {
            @Override
            public void onSuccess(List<Film> films) {
                if (isLoadFirstTime) {
                    int insertIndex = filmList.size();
                    filmList.addAll(insertIndex, films);
                    filmAdapter = new FilmAdapter(context, filmList);
                    recyclerFilm.setAdapter(filmAdapter);
                    recyclerFilm.setLayoutManager(new LinearLayoutManager(context));
                    recyclerFilm.setHasFixedSize(true);
                    recyclerFilm.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    isLoadFirstTime = false;
                } else {
                    int insertIndex = filmList.size();
                    filmList.addAll(insertIndex, films);
                    filmAdapter.notifyItemRangeInserted(insertIndex, films.size());
                }
            }

            @Override
            public void onNotFound() {
                progressBar.setVisibility(View.GONE);
                textMessage.setVisibility(View.VISIBLE);
                textMessage.setText(R.string.empty_film_airing);
            }

            @Override
            public void onError(String message) {
                progressBar.setVisibility(View.GONE);
                textMessage.setVisibility(View.VISIBLE);
                textMessage.setText(R.string.empty_film_airing);
                Snackbar.make(anchorLayout, message, Snackbar.LENGTH_LONG).show();
            }
        });
    }
}
