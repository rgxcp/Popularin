package xyz.fairportstudios.popularin.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.adapters.GenreAdapter;
import xyz.fairportstudios.popularin.models.Genre;
import xyz.fairportstudios.popularin.statics.TMDbAPI;

public class GenreFragment extends Fragment {
    private ProgressBar progressBar;
    private RecyclerView recyclerGenre;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reusable_recycler, container, false);

        // Binding
        Context context = getActivity();
        progressBar = view.findViewById(R.id.pbr_rr_layout);
        recyclerGenre = view.findViewById(R.id.recycler_rr_layout);

        // Menampilkan semua genre
        showAllGenre(context);

        return view;
    }

    private void showAllGenre(Context context) {
        List<Genre> genreList = new ArrayList<>();
        genreList.add(new Genre(TMDbAPI.ACTION_ID, R.drawable.img_action, getString(R.string.genre_action)));
        genreList.add(new Genre(TMDbAPI.ANIMATION_ID, R.drawable.img_action, getString(R.string.genre_animation)));
        genreList.add(new Genre(TMDbAPI.DOCUMENTARY_ID, R.drawable.img_action, getString(R.string.genre_documentary)));
        genreList.add(new Genre(TMDbAPI.DRAMA_ID, R.drawable.img_action, getString(R.string.genre_drama)));
        genreList.add(new Genre(TMDbAPI.FANTASY_ID, R.drawable.img_action, getString(R.string.genre_fantasy)));
        genreList.add(new Genre(TMDbAPI.FICTION_ID, R.drawable.img_action, getString(R.string.genre_fiction)));
        genreList.add(new Genre(TMDbAPI.HORROR_ID, R.drawable.img_action, getString(R.string.genre_horror)));
        genreList.add(new Genre(TMDbAPI.CRIME_ID, R.drawable.img_action, getString(R.string.genre_crime)));
        genreList.add(new Genre(TMDbAPI.FAMILY_ID, R.drawable.img_action, getString(R.string.genre_family)));
        genreList.add(new Genre(TMDbAPI.COMEDY_ID, R.drawable.img_action, getString(R.string.genre_comedy)));
        genreList.add(new Genre(TMDbAPI.MYSTERY_ID, R.drawable.img_action, getString(R.string.genre_mystery)));
        genreList.add(new Genre(TMDbAPI.WAR_ID, R.drawable.img_action, getString(R.string.genre_war)));
        genreList.add(new Genre(TMDbAPI.ADVENTURE_ID, R.drawable.img_action, getString(R.string.genre_adventure)));
        genreList.add(new Genre(TMDbAPI.ROMANCE_ID, R.drawable.img_action, getString(R.string.genre_romance)));
        genreList.add(new Genre(TMDbAPI.HISTORY_ID, R.drawable.img_action, getString(R.string.genre_history)));
        genreList.add(new Genre(TMDbAPI.THRILLER_ID, R.drawable.img_action, getString(R.string.genre_thriller)));

        GenreAdapter genreAdapter = new GenreAdapter(context, genreList);
        recyclerGenre.setAdapter(genreAdapter);
        recyclerGenre.setLayoutManager(new GridLayoutManager(context, 2));
        recyclerGenre.setHasFixedSize(true);
        recyclerGenre.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }
}
