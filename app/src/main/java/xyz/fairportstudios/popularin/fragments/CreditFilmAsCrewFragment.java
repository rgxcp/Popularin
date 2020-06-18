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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.List;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.adapters.FilmGridAdapter;
import xyz.fairportstudios.popularin.models.Film;

public class CreditFilmAsCrewFragment extends Fragment {
    // Variable member
    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerFilm;
    private SwipeRefreshLayout mSwipeRefresh;
    private TextView mTextMessage;

    // Variable constructor
    private List<Film> filmAsCrewList;

    public CreditFilmAsCrewFragment(List<Film> filmAsCrewList) {
        this.filmAsCrewList = filmAsCrewList;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reusable_recycler, container, false);

        // Context
        Context context = getActivity();

        // Binding
        mProgressBar = view.findViewById(R.id.pbr_rr_layout);
        mRecyclerFilm = view.findViewById(R.id.recycler_rr_layout);
        mSwipeRefresh = view.findViewById(R.id.swipe_refresh_rr_layout);
        mTextMessage = view.findViewById(R.id.text_rr_message);

        // Mendapatkan data
        getFilmAsCrew(context);

        // Activity
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefresh.setRefreshing(false);
            }
        });

        return view;
    }

    private void getFilmAsCrew(Context context) {
        if (!filmAsCrewList.isEmpty()) {
            FilmGridAdapter filmGridAdapter = new FilmGridAdapter(context, filmAsCrewList);
            mRecyclerFilm.setAdapter(filmGridAdapter);
            mRecyclerFilm.setLayoutManager(new GridLayoutManager(context, 4));
            mRecyclerFilm.setHasFixedSize(true);
            mRecyclerFilm.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
        } else {
            mProgressBar.setVisibility(View.GONE);
            mTextMessage.setVisibility(View.VISIBLE);
            mTextMessage.setText(R.string.empty_credit_film_as_crew);
        }
    }
}
