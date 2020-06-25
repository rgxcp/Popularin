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
import xyz.fairportstudios.popularin.activities.DiscoverFilmActivity;
import xyz.fairportstudios.popularin.adapters.GenreAdapter;
import xyz.fairportstudios.popularin.models.Genre;
import xyz.fairportstudios.popularin.statics.Popularin;

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
    public void onGenreItemClick(int position) {
        Genre currentItem = mGenreList.get(position);
        int id = currentItem.getId();
        String title = currentItem.getTitle();
        gotoDiscoverFilm(id, title);
    }

    private void showGenre() {
        mGenreList = new ArrayList<>();
        mGenreList.add(new Genre(28, R.drawable.img_action, getString(R.string.genre_action)));
        mGenreList.add(new Genre(16, R.drawable.img_animation, getString(R.string.genre_animation)));
        mGenreList.add(new Genre(99, R.drawable.img_documentary, getString(R.string.genre_documentary)));
        mGenreList.add(new Genre(18, R.drawable.img_drama, getString(R.string.genre_drama)));
        mGenreList.add(new Genre(14, R.drawable.img_fantasy, getString(R.string.genre_fantasy)));
        mGenreList.add(new Genre(878, R.drawable.img_fiction, getString(R.string.genre_fiction)));
        mGenreList.add(new Genre(27, R.drawable.img_horror, getString(R.string.genre_horror)));
        mGenreList.add(new Genre(80, R.drawable.img_crime, getString(R.string.genre_crime)));
        mGenreList.add(new Genre(10751, R.drawable.img_family, getString(R.string.genre_family)));
        mGenreList.add(new Genre(35, R.drawable.img_comedy, getString(R.string.genre_comedy)));
        mGenreList.add(new Genre(9648, R.drawable.img_mystery, getString(R.string.genre_mystery)));
        mGenreList.add(new Genre(10752, R.drawable.img_war, getString(R.string.genre_war)));
        mGenreList.add(new Genre(12, R.drawable.img_adventure, getString(R.string.genre_adventure)));
        mGenreList.add(new Genre(10749, R.drawable.img_romance, getString(R.string.genre_romance)));
        mGenreList.add(new Genre(36, R.drawable.img_history, getString(R.string.genre_history)));
        mGenreList.add(new Genre(53, R.drawable.img_thriller, getString(R.string.genre_thriller)));

        GenreAdapter genreAdapter = new GenreAdapter(mContext, mGenreList, mOnClickListener);
        mRecyclerGenre.setAdapter(genreAdapter);
        mRecyclerGenre.setLayoutManager(new GridLayoutManager(mContext, 2));
        mRecyclerGenre.setHasFixedSize(true);
        mRecyclerGenre.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
    }

    private void gotoDiscoverFilm(int id, String title) {
        Intent intent = new Intent(mContext, DiscoverFilmActivity.class);
        intent.putExtra(Popularin.GENRE_ID, id);
        intent.putExtra(Popularin.GENRE_TITLE, title);
        startActivity(intent);
    }
}
