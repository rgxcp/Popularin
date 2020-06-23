package xyz.fairportstudios.popularin.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.List;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.activities.FilmDetailActivity;
import xyz.fairportstudios.popularin.adapters.FilmGridAdapter;
import xyz.fairportstudios.popularin.modals.FilmModal;
import xyz.fairportstudios.popularin.models.Film;
import xyz.fairportstudios.popularin.services.ParseDate;
import xyz.fairportstudios.popularin.statics.Popularin;

public class CreditFilmAsCrewFragment extends Fragment implements FilmGridAdapter.OnClickListener {
    // Variable member
    private Context mContext;
    private FilmGridAdapter.OnClickListener mOnClickListener;
    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerFilm;
    private TextView mTextMessage;

    // Variable constructor
    private List<Film> mFilmAsCrewList;

    public CreditFilmAsCrewFragment(List<Film> filmAsCrewList) {
        mFilmAsCrewList = filmAsCrewList;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reusable_recycler, container, false);

        // Context
        mContext = getActivity();

        // Binding
        mProgressBar = view.findViewById(R.id.pbr_rr_layout);
        mRecyclerFilm = view.findViewById(R.id.recycler_rr_layout);
        mTextMessage = view.findViewById(R.id.text_rr_message);
        final SwipeRefreshLayout swipeRefresh = view.findViewById(R.id.swipe_refresh_rr_layout);

        // Mendapatkan data
        mOnClickListener = this;
        getFilmAsCrew();

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
        Film currentItem = mFilmAsCrewList.get(position);
        int id = currentItem.getId();
        gotoFilmDetail(id);
    }

    @Override
    public void onItemLongClick(int position) {
        Film currentItem = mFilmAsCrewList.get(position);
        int id = currentItem.getId();
        String title = currentItem.getOriginal_title();
        String year = new ParseDate().getYear(currentItem.getRelease_date());
        String poster = currentItem.getPoster_path();
        showFilmModal(id, title, year, poster);
    }

    private void getFilmAsCrew() {
        if (!mFilmAsCrewList.isEmpty()) {
            FilmGridAdapter filmGridAdapter = new FilmGridAdapter(mContext, mFilmAsCrewList, mOnClickListener);
            mRecyclerFilm.setAdapter(filmGridAdapter);
            mRecyclerFilm.setLayoutManager(new GridLayoutManager(mContext, 4));
            mRecyclerFilm.setHasFixedSize(true);
            mRecyclerFilm.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
        } else {
            mProgressBar.setVisibility(View.GONE);
            mTextMessage.setVisibility(View.VISIBLE);
            mTextMessage.setText(R.string.empty_credit_film_as_crew);
        }
    }

    private void gotoFilmDetail(int id) {
        Intent intent = new Intent(mContext, FilmDetailActivity.class);
        intent.putExtra(Popularin.FILM_ID, id);
        startActivity(intent);
    }

    private void showFilmModal(int id, String title, String year, String poster) {
        FragmentManager fragmentManager = ((FragmentActivity) mContext).getSupportFragmentManager();
        FilmModal filmModal = new FilmModal(id, title, year, poster);
        filmModal.show(fragmentManager, Popularin.FILM_MODAL);
    }
}
