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
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.adapters.PagerAdapter;
import xyz.fairportstudios.popularin.adapters.UserAdapter;
import xyz.fairportstudios.popularin.apis.popularin.get.WatchlistFromAllRequest;
import xyz.fairportstudios.popularin.fragments.WatchlistFromAllFragment;
import xyz.fairportstudios.popularin.fragments.WatchlistFromFollowingFragment;
import xyz.fairportstudios.popularin.models.User;
import xyz.fairportstudios.popularin.preferences.Auth;
import xyz.fairportstudios.popularin.statics.Popularin;

public class WatchlistedByActivity extends AppCompatActivity implements UserAdapter.OnClickListener {
    // Variable untuk fitur load more
    private boolean mIsLoading = true;
    private boolean mIsLoadFirstTimeSuccess = false;
    private int mStartPage = 1;
    private int mCurrentPage = 1;
    private int mTotalPage;

    // Variable member
    private Context mContext;
    private List<User> mUserList;
    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerUser;
    private RelativeLayout mAnchorLayout;
    private SwipeRefreshLayout mSwipeRefresh;
    private TextView mTextMessage;
    private UserAdapter mUserAdapter;
    private UserAdapter.OnClickListener mOnClickListener;
    private WatchlistFromAllRequest mWatchlistFromAllRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Context
        mContext = WatchlistedByActivity.this;

        // Extra
        Intent intent = getIntent();
        int filmID = intent.getIntExtra(Popularin.FILM_ID, 0);

        // Auth
        boolean isAuth = new Auth(mContext).isAuth();

        // Menampilkan layout berdasarkan kondisi
        if (isAuth) {
            setContentView(R.layout.reusable_toolbar_pager);

            // Binding
            TabLayout tabLayout = findViewById(R.id.tab_rtp_layout);
            Toolbar toolbar = findViewById(R.id.toolbar_rtp_layout);
            ViewPager viewPager = findViewById(R.id.pager_rtp_layout);

            // Toolbar
            toolbar.setTitle(R.string.watchlisted_by);

            // Tab pager
            PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
            pagerAdapter.addFragment(new WatchlistFromAllFragment(filmID), getString(R.string.all));
            pagerAdapter.addFragment(new WatchlistFromFollowingFragment(filmID), getString(R.string.following));
            viewPager.setAdapter(pagerAdapter);
            tabLayout.setupWithViewPager(viewPager);

            // Activity
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
        } else {
            setContentView(R.layout.reusable_toolbar_recycler);

            // Binding
            mProgressBar = findViewById(R.id.pbr_rtr_layout);
            mRecyclerUser = findViewById(R.id.recycler_rtr_layout);
            mAnchorLayout = findViewById(R.id.anchor_rtr_layout);
            mSwipeRefresh = findViewById(R.id.swipe_refresh_rtr_layout);
            mTextMessage = findViewById(R.id.text_aud_message);
            Toolbar toolbar = findViewById(R.id.toolbar_rtr_layout);

            // Toolbar
            toolbar.setTitle(R.string.watchlisted_by);

            // Mendapatkan data awal
            mOnClickListener = this;
            mWatchlistFromAllRequest = new WatchlistFromAllRequest(mContext, filmID);
            getWatchlistFromAll(mStartPage, false);

            // Activity
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });

            mRecyclerUser.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                        if (!mIsLoading && mCurrentPage <= mTotalPage) {
                            mIsLoading = true;
                            mSwipeRefresh.setRefreshing(true);
                            getWatchlistFromAll(mCurrentPage, false);
                        }
                    }
                }
            });

            mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    mIsLoading = true;
                    mSwipeRefresh.setRefreshing(true);
                    getWatchlistFromAll(mStartPage, true);
                }
            });
        }
    }

    @Override
    public void onUserItemClick(int position) {
        User currentItem = mUserList.get(position);
        int id = currentItem.getId();
        gotoUserDetail(id);
    }

    private void getWatchlistFromAll(int page, final boolean refreshPage) {
        mWatchlistFromAllRequest.sendRequest(page, new WatchlistFromAllRequest.Callback() {
            @Override
            public void onSuccess(int totalPage, List<User> userList) {
                if (!mIsLoadFirstTimeSuccess) {
                    mUserList = new ArrayList<>();
                    int insertIndex = mUserList.size();
                    mUserList.addAll(insertIndex, userList);
                    mUserAdapter = new UserAdapter(mContext, mUserList, mOnClickListener);
                    mRecyclerUser.setAdapter(mUserAdapter);
                    mRecyclerUser.setLayoutManager(new LinearLayoutManager(mContext));
                    mRecyclerUser.setVisibility(View.VISIBLE);
                    mProgressBar.setVisibility(View.GONE);
                    mTotalPage = totalPage;
                    mIsLoadFirstTimeSuccess = true;
                } else {
                    if (refreshPage) {
                        mCurrentPage = 1;
                        mTotalPage = totalPage;
                        mUserList.clear();
                        mUserAdapter.notifyDataSetChanged();
                    }
                    int insertIndex = mUserList.size();
                    mUserList.addAll(insertIndex, userList);
                    mUserAdapter.notifyItemRangeInserted(insertIndex, userList.size());
                    mRecyclerUser.scrollToPosition(insertIndex);
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
                    mUserList.clear();
                    mUserAdapter.notifyDataSetChanged();
                }
                mTextMessage.setVisibility(View.VISIBLE);
                mTextMessage.setText(R.string.empty_film_watchlist);
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

    private void gotoUserDetail(int id) {
        Intent intent = new Intent(mContext, UserDetailActivity.class);
        intent.putExtra(Popularin.USER_ID, id);
        startActivity(intent);
    }
}
