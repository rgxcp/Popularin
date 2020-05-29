package xyz.fairportstudios.popularin.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.apis.tmdb.get.SearchFilmRequest;
import xyz.fairportstudios.popularin.models.Film;

public class SearchFilmFragment extends Fragment {
    private List<Film> filmList;
    private ProgressBar progressBar;
    private RelativeLayout anchorLayout;
    private SearchFilmRequest searchFilmRequest;
    private TextView textMessage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_film, container, false);

        // Binding
        progressBar = view.findViewById(R.id.pbr_fsf_layout);
        anchorLayout = view.findViewById(R.id.anchor_fsf_layout);
        textMessage = view.findViewById(R.id.text_fsf_message);
        Context context = getActivity();
        RecyclerView recyclerFilm = view.findViewById(R.id.recycler_fsf_layout);
        SearchView searchView = view.findViewById(R.id.search_fsf_layout);

        // Request
        filmList = new ArrayList<>();
        searchFilmRequest = new SearchFilmRequest(context, filmList, recyclerFilm);

        // Activity
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchFilm(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return view;
    }

    private void searchFilm(String query) {
        progressBar.setVisibility(View.VISIBLE);
        textMessage.setVisibility(View.GONE);
        filmList.clear();

        searchFilmRequest.sendRequest(query, new SearchFilmRequest.APICallback() {
            @Override
            public void onSuccess() {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onEmpty() {
                progressBar.setVisibility(View.GONE);
                textMessage.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError() {
                progressBar.setVisibility(View.GONE);
                textMessage.setVisibility(View.VISIBLE);
                Snackbar.make(anchorLayout, R.string.network_error, Snackbar.LENGTH_LONG).show();
            }
        });
    }
}
