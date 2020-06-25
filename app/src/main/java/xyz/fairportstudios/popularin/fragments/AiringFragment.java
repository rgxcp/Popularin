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
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.activities.FilmDetailActivity;
import xyz.fairportstudios.popularin.adapters.FilmAdapter;
import xyz.fairportstudios.popularin.apis.tmdb.get.AiringFilmRequest;
import xyz.fairportstudios.popularin.modals.FilmModal;
import xyz.fairportstudios.popularin.models.Film;
import xyz.fairportstudios.popularin.services.ParseDate;
import xyz.fairportstudios.popularin.statics.Popularin;

public class AiringFragment extends Fragment implements FilmAdapter.OnClickListener {
    // Variable untuk fitur load
    private boolean mIsLoadFirstTimeSuccess = false;

    // Variable member
    private Context mContext;
    private AiringFilmRequest mAiringFilmRequest;
    private CoordinatorLayout mAnchorLayout;
    private FilmAdapter mFilmAdapter;
    private FilmAdapter.OnClickListener mOnClickListener;
    private List<Film> mFilmList;
    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerFilm;
    private SwipeRefreshLayout mSwipeRefresh;
    private TextView mTextMessage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reusable_recycler, container, false);

        // Context
        mContext = getActivity();

        // Binding
        mAnchorLayout = view.findViewById(R.id.anchor_rr_layout);
        mProgressBar = view.findViewById(R.id.pbr_rr_layout);
        mRecyclerFilm = view.findViewById(R.id.recycler_rr_layout);
        mSwipeRefresh = view.findViewById(R.id.swipe_refresh_rr_layout);
        mTextMessage = view.findViewById(R.id.text_rr_message);

        // Mendapatkan data
        mOnClickListener = this;
        mAiringFilmRequest = new AiringFilmRequest(mContext);
        getAiringFilm(false);

        // Activity
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefresh.setRefreshing(true);
                getAiringFilm(true);
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        resetState();
    }

    @Override
    public void onFilmItemClick(int position) {
        Film currentItem = mFilmList.get(position);
        int id = currentItem.getId();
        gotoFilmDetail(id);
    }

    @Override
    public void onFilmPosterClick(int position) {
        Film currentItem = mFilmList.get(position);
        int id = currentItem.getId();
        gotoFilmDetail(id);
    }

    @Override
    public void onFilmPosterLongClick(int position) {
        Film currentItem = mFilmList.get(position);
        int id = currentItem.getId();
        String title = currentItem.getOriginal_title();
        String year = new ParseDate().getYear(currentItem.getRelease_date());
        String poster = currentItem.getPoster_path();
        showFilmModal(id, title, year, poster);
    }

    private void getAiringFilm(final boolean refreshPage) {
        mAiringFilmRequest.sendRequest(new AiringFilmRequest.Callback() {
            @Override
            public void onSuccess(List<Film> filmList) {
                if (!mIsLoadFirstTimeSuccess) {
                    mFilmList = new ArrayList<>();
                    int insertIndex = mFilmList.size();
                    mFilmList.addAll(insertIndex, filmList);
                    mFilmAdapter = new FilmAdapter(mContext, mFilmList, mOnClickListener);
                    mRecyclerFilm.setAdapter(mFilmAdapter);
                    mRecyclerFilm.setLayoutManager(new LinearLayoutManager(mContext));
                    mRecyclerFilm.setHasFixedSize(true);
                    mRecyclerFilm.setVisibility(View.VISIBLE);
                    mProgressBar.setVisibility(View.GONE);
                    mIsLoadFirstTimeSuccess = true;
                } else {
                    if (refreshPage) {
                        mFilmList.clear();
                        mFilmAdapter.notifyDataSetChanged();
                    }
                    int insertIndex = mFilmList.size();
                    mFilmList.addAll(insertIndex, filmList);
                    mFilmAdapter.notifyItemRangeInserted(insertIndex, filmList.size());
                }
                mTextMessage.setVisibility(View.GONE);
            }

            @Override
            public void onNotFound() {
                mProgressBar.setVisibility(View.GONE);
                mTextMessage.setVisibility(View.VISIBLE);
                mTextMessage.setText(R.string.empty_airing_film);
            }

            @Override
            public void onError(String message) {
                if (!mIsLoadFirstTimeSuccess) {
                    mProgressBar.setVisibility(View.GONE);
                    mTextMessage.setVisibility(View.VISIBLE);
                    mTextMessage.setText(R.string.empty_airing_film);
                }
                Snackbar.make(mAnchorLayout, message, Snackbar.LENGTH_LONG).show();
            }
        });

        // Memberhentikan loading
        mSwipeRefresh.setRefreshing(false);
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

    private void resetState() {
        mIsLoadFirstTimeSuccess = false;
    }
}
