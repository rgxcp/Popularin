package xyz.fairportstudios.popularin.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.adapters.FilmAdapter;
import xyz.fairportstudios.popularin.apis.popularin.get.UserWatchlistRequest;
import xyz.fairportstudios.popularin.modals.FilmModal;
import xyz.fairportstudios.popularin.models.Film;
import xyz.fairportstudios.popularin.preferences.Auth;
import xyz.fairportstudios.popularin.services.ParseDate;
import xyz.fairportstudios.popularin.statics.Popularin;

public class UserWatchlistActivity extends AppCompatActivity implements FilmAdapter.OnClickListener {
    // Variable untuk fitur load more
    private boolean mIsLoading = true;
    private boolean mIsLoadFirstTimeSuccess = false;
    private int mStartPage = 1;
    private int mCurrentPage = 1;
    private int mTotalPage;

    // Variable member
    private boolean mIsSelf;
    private Context mContext;
    private FilmAdapter mFilmAdapter;
    private FilmAdapter.OnClickListener mOnClickListener;
    private List<Film> mFilmList;
    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerFilm;
    private RelativeLayout mAnchorLayout;
    private SwipeRefreshLayout mSwipeRefresh;
    private TextView mTextMessage;
    private UserWatchlistRequest mUserWatchlistRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reusable_toolbar_recycler);

        // Context
        mContext = UserWatchlistActivity.this;

        // Extra
        Intent intent = getIntent();
        int userID = intent.getIntExtra(Popularin.USER_ID, 0);

        // Auth
        mIsSelf = userID == new Auth(mContext).getAuthID();

        // Binding
        mProgressBar = findViewById(R.id.pbr_rtr_layout);
        mRecyclerFilm = findViewById(R.id.recycler_rtr_layout);
        mAnchorLayout = findViewById(R.id.anchor_rtr_layout);
        mSwipeRefresh = findViewById(R.id.swipe_refresh_rtr_layout);
        mTextMessage = findViewById(R.id.text_aud_message);
        Toolbar toolbar = findViewById(R.id.toolbar_rtr_layout);

        // Toolbar
        toolbar.setTitle(R.string.watchlist);

        // Mendapatkan data awal
        mOnClickListener = this;
        mUserWatchlistRequest = new UserWatchlistRequest(mContext, userID);
        getUserWatchlist(mStartPage, false);

        // Activity
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        mRecyclerFilm.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (!mIsLoading && mCurrentPage <= mTotalPage) {
                        mIsLoading = true;
                        mSwipeRefresh.setRefreshing(true);
                        getUserWatchlist(mCurrentPage, false);
                    }
                }
            }
        });

        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mIsLoading = true;
                mSwipeRefresh.setRefreshing(true);
                getUserWatchlist(mStartPage, true);
            }
        });
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

    private void getUserWatchlist(int page, final boolean refreshPage) {
        mUserWatchlistRequest.sendRequest(page, new UserWatchlistRequest.Callback() {
            @Override
            public void onSuccess(int totalPage, List<Film> filmList) {
                if (!mIsLoadFirstTimeSuccess) {
                    mFilmList = new ArrayList<>();
                    int insertIndex = mFilmList.size();
                    mFilmList.addAll(insertIndex, filmList);
                    mFilmAdapter = new FilmAdapter(mContext, mFilmList, mOnClickListener);
                    mRecyclerFilm.setAdapter(mFilmAdapter);
                    mRecyclerFilm.setLayoutManager(new LinearLayoutManager(mContext));
                    mRecyclerFilm.setVisibility(View.VISIBLE);
                    mProgressBar.setVisibility(View.GONE);
                    mTotalPage = totalPage;
                    mIsLoadFirstTimeSuccess = true;
                } else {
                    if (refreshPage) {
                        mCurrentPage = 1;
                        mFilmList.clear();
                        mFilmAdapter.notifyDataSetChanged();
                    }
                    int insertIndex = mFilmList.size();
                    mFilmList.addAll(insertIndex, filmList);
                    mFilmAdapter.notifyItemRangeInserted(insertIndex, filmList.size());
                    mRecyclerFilm.scrollToPosition(insertIndex);
                }
                mTextMessage.setVisibility(View.GONE);
                mCurrentPage++;
            }

            @Override
            public void onNotFound() {
                if (!mIsLoadFirstTimeSuccess) {
                    mProgressBar.setVisibility(View.GONE);
                } else {
                    mCurrentPage = 1;
                    mFilmList.clear();
                    mFilmAdapter.notifyDataSetChanged();
                }
                mTextMessage.setVisibility(View.VISIBLE);
                if (mIsSelf) {
                    mTextMessage.setText(R.string.empty_self_watchlist_film);
                } else {
                    mTextMessage.setText(R.string.empty_user_watchlist);
                }
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
        mIsLoading = false;
        mSwipeRefresh.setRefreshing(false);
    }

    private void gotoFilmDetail(int id) {
        Intent intent = new Intent(mContext, FilmDetailActivity.class);
        intent.putExtra(Popularin.FILM_ID, id);
        startActivity(intent);
    }

    private void showFilmModal(int id, String title, String year, String poster) {
        FilmModal filmModal = new FilmModal(id, title, year, poster);
        filmModal.show(getSupportFragmentManager(), Popularin.FILM_MODAL);
    }
}
