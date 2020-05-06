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
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.apis.tmdb.get.SearchFilm;
import xyz.fairportstudios.popularin.models.Film;

public class SearchFilmFragment extends Fragment {
    private Context context;
    private List<Film> filmList;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private TextView emptyResult;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_film, container, false);

        // Binding
        context = getActivity();
        progressBar = view.findViewById(R.id.progress_bar_fsf_layout);
        recyclerView = view.findViewById(R.id.recycler_view_fsf_layout);
        emptyResult = view.findViewById(R.id.text_fsf_empty);
        SearchView searchView = view.findViewById(R.id.search_fsf_layout);

        // Set-up list
        filmList = new ArrayList<>();

        // Activity
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                progressBar.setVisibility(View.VISIBLE);
                emptyResult.setVisibility(View.GONE);

                SearchFilm searchFilm = new SearchFilm(context, filmList, recyclerView);
                String requestURL = searchFilm.getRequestURL(query, "1");
                searchFilm.sendRequest(requestURL, new SearchFilm.JSONCallback() {
                    @Override
                    public void onSuccess() {
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onEmptyResult() {
                        progressBar.setVisibility(View.GONE);
                        emptyResult.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onEmptyIndonesian() {
                        progressBar.setVisibility(View.GONE);
                        emptyResult.setVisibility(View.VISIBLE);
                    }
                });

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });

        return view;
    }
}
