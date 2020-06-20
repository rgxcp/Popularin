package xyz.fairportstudios.popularin.fragments;

import android.content.Context;
import android.content.Intent;
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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.activities.FilmListActivity;
import xyz.fairportstudios.popularin.adapters.GenreAdapter;
import xyz.fairportstudios.popularin.models.Genre;
import xyz.fairportstudios.popularin.statics.Popularin;
import xyz.fairportstudios.popularin.statics.TMDbAPI;

public class GenreFragment extends Fragment implements GenreAdapter.OnClickListener {
    private Context mContext;
    private GenreAdapter.OnClickListener mOnClickListener;
    private List<Genre> mGenreList;
    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerGenre;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reusable_recycler, container, false);

        // Context
        mContext = getActivity();

        // Binding
        mProgressBar = view.findViewById(R.id.pbr_rr_layout);
        mRecyclerGenre = view.findViewById(R.id.recycler_rr_layout);
        final SwipeRefreshLayout swipeRefresh = view.findViewById(R.id.swipe_refresh_rr_layout);

        // Menampilkan genre
        mOnClickListener = this;
        mGenreList = new ArrayList<>();
        showGenre();

        // Activity
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefresh.setRefreshing(false);
            }
        });

        return view;
    }

    @Override
    public void onItemClick(int position) {
        Genre currentItem = mGenreList.get(position);
        int id = currentItem.getId();
        String title = currentItem.getTitle();
        gotoFilmList(id, title);
    }

    private void showGenre() {
        mGenreList.add(new Genre(TMDbAPI.ACTION_ID, R.drawable.img_action, getString(R.string.genre_action)));
        mGenreList.add(new Genre(TMDbAPI.ANIMATION_ID, R.drawable.img_animation, getString(R.string.genre_animation)));
        mGenreList.add(new Genre(TMDbAPI.DOCUMENTARY_ID, R.drawable.img_documentary, getString(R.string.genre_documentary)));
        mGenreList.add(new Genre(TMDbAPI.DRAMA_ID, R.drawable.img_drama, getString(R.string.genre_drama)));
        mGenreList.add(new Genre(TMDbAPI.FANTASY_ID, R.drawable.img_fantasy, getString(R.string.genre_fantasy)));
        mGenreList.add(new Genre(TMDbAPI.FICTION_ID, R.drawable.img_fiction, getString(R.string.genre_fiction)));
        mGenreList.add(new Genre(TMDbAPI.HORROR_ID, R.drawable.img_horror, getString(R.string.genre_horror)));
        mGenreList.add(new Genre(TMDbAPI.CRIME_ID, R.drawable.img_crime, getString(R.string.genre_crime)));
        mGenreList.add(new Genre(TMDbAPI.FAMILY_ID, R.drawable.img_family, getString(R.string.genre_family)));
        mGenreList.add(new Genre(TMDbAPI.COMEDY_ID, R.drawable.img_comedy, getString(R.string.genre_comedy)));
        mGenreList.add(new Genre(TMDbAPI.MYSTERY_ID, R.drawable.img_mystery, getString(R.string.genre_mystery)));
        mGenreList.add(new Genre(TMDbAPI.WAR_ID, R.drawable.img_war, getString(R.string.genre_war)));
        mGenreList.add(new Genre(TMDbAPI.ADVENTURE_ID, R.drawable.img_adventure, getString(R.string.genre_adventure)));
        mGenreList.add(new Genre(TMDbAPI.ROMANCE_ID, R.drawable.img_romance, getString(R.string.genre_romance)));
        mGenreList.add(new Genre(TMDbAPI.HISTORY_ID, R.drawable.img_history, getString(R.string.genre_history)));
        mGenreList.add(new Genre(TMDbAPI.THRILLER_ID, R.drawable.img_thriller, getString(R.string.genre_thriller)));

        GenreAdapter genreAdapter = new GenreAdapter(mContext, mGenreList, mOnClickListener);
        mRecyclerGenre.setAdapter(genreAdapter);
        mRecyclerGenre.setLayoutManager(new GridLayoutManager(mContext, 2));
        mRecyclerGenre.setHasFixedSize(true);
        mRecyclerGenre.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
    }

    private void gotoFilmList(int id, String title) {
        Intent intent = new Intent(mContext, FilmListActivity.class);
        intent.putExtra(Popularin.GENRE_ID, id);
        intent.putExtra(Popularin.GENRE_TITLE, title);
        startActivity(intent);
    }
}
