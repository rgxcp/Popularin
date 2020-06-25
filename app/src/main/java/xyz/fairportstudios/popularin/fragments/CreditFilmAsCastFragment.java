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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.activities.FilmDetailActivity;
import xyz.fairportstudios.popularin.adapters.FilmGridAdapter;
import xyz.fairportstudios.popularin.apis.tmdb.get.CreditDetailRequest;
import xyz.fairportstudios.popularin.modals.FilmModal;
import xyz.fairportstudios.popularin.models.CreditDetail;
import xyz.fairportstudios.popularin.models.Film;
import xyz.fairportstudios.popularin.services.ParseDate;
import xyz.fairportstudios.popularin.statics.Popularin;

public class CreditFilmAsCastFragment extends Fragment implements FilmGridAdapter.OnClickListener {
    // Variable untuk fitur onResume
    private boolean mIsResumeFirstTime = true;

    // Variable untuk fitur load
    private boolean mIsLoadFirstTimeSuccess = false;

    // Variable member
    private Context mContext;
    private CoordinatorLayout mAnchorLayout;
    private FilmGridAdapter.OnClickListener mOnClickListener;
    private List<Film> mFilmAsCastList;
    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerFilm;
    private SwipeRefreshLayout mSwipeRefresh;
    private TextView mTextMessage;

    // Variable constructor
    private int mCreditID;

    public CreditFilmAsCastFragment(int creditID) {
        mCreditID = creditID;
    }

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

        // Activity
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!mIsLoadFirstTimeSuccess) {
                    mSwipeRefresh.setRefreshing(true);
                    getFilmAsCast();
                } else {
                    mSwipeRefresh.setRefreshing(false);
                }
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mIsResumeFirstTime) {
            // Mendapatkan data
            mIsResumeFirstTime = false;
            mOnClickListener = this;
            getFilmAsCast();
        }
    }

    @Override
    public void onFilmGridItemClick(int position) {
        Film currentItem = mFilmAsCastList.get(position);
        int id = currentItem.getId();
        gotoFilmDetail(id);
    }

    @Override
    public void onFilmGridItemLongClick(int position) {
        Film currentItem = mFilmAsCastList.get(position);
        int id = currentItem.getId();
        String title = currentItem.getOriginal_title();
        String year = new ParseDate().getYear(currentItem.getRelease_date());
        String poster = currentItem.getPoster_path();
        showFilmModal(id, title, year, poster);
    }

    private void getFilmAsCast() {
        CreditDetailRequest creditDetailRequest = new CreditDetailRequest(mContext, mCreditID);
        creditDetailRequest.sendRequest(new CreditDetailRequest.Callback() {
            @Override
            public void onSuccess(CreditDetail creditDetail, List<Film> filmAsCastList, List<Film> filmAsCrewList) {
                if (!filmAsCastList.isEmpty()) {
                    mFilmAsCastList = new ArrayList<>();
                    mFilmAsCastList.addAll(filmAsCastList);
                    FilmGridAdapter filmGridAdapter = new FilmGridAdapter(mContext, mFilmAsCastList, mOnClickListener);
                    mRecyclerFilm.setAdapter(filmGridAdapter);
                    mRecyclerFilm.setLayoutManager(new GridLayoutManager(mContext, 4));
                    mRecyclerFilm.setHasFixedSize(true);
                    mRecyclerFilm.setVisibility(View.VISIBLE);
                    mTextMessage.setVisibility(View.GONE);
                } else {
                    mTextMessage.setVisibility(View.VISIBLE);
                    mTextMessage.setText(R.string.empty_credit_film_as_cast);
                }
                mProgressBar.setVisibility(View.GONE);
                mIsLoadFirstTimeSuccess = true;
            }

            @Override
            public void onError(String message) {
                if (!mIsLoadFirstTimeSuccess) {
                    mProgressBar.setVisibility(View.GONE);
                    mTextMessage.setVisibility(View.VISIBLE);
                    mTextMessage.setText(message);
                } else {
                    Snackbar.make(mAnchorLayout, message, Snackbar.LENGTH_LONG).show();
                }
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
}
