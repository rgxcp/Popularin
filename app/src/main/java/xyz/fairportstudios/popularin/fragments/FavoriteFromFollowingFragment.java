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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.activities.UserDetailActivity;
import xyz.fairportstudios.popularin.adapters.UserAdapter;
import xyz.fairportstudios.popularin.apis.popularin.get.FavoriteFromFollowingRequest;
import xyz.fairportstudios.popularin.models.User;
import xyz.fairportstudios.popularin.statics.Popularin;

public class FavoriteFromFollowingFragment extends Fragment implements UserAdapter.OnClickListener {
    // Variable untuk fitur onResume
    private boolean mIsResumeFirstTime = true;

    // Variable untuk fitur load more
    private boolean mIsLoading = true;
    private boolean mIsLoadFirstTimeSuccess = false;
    private int mStartPage = 1;
    private int mCurrentPage = 1;
    private int mTotalPage;

    // Variable member
    private Context mContext;
    private CoordinatorLayout mAnchorLayout;
    private FavoriteFromFollowingRequest mFavoriteFromFollowingRequest;
    private List<User> mUserList;
    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerUser;
    private SwipeRefreshLayout mSwipeRefresh;
    private TextView mTextMessage;
    private UserAdapter mUserAdapter;
    private UserAdapter.OnClickListener mOnClickListener;

    // Variable constructor
    private int mFilmID;

    public FavoriteFromFollowingFragment(int filmID) {
        mFilmID = filmID;
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
        mRecyclerUser = view.findViewById(R.id.recycler_rr_layout);
        mSwipeRefresh = view.findViewById(R.id.swipe_refresh_rr_layout);
        mTextMessage = view.findViewById(R.id.text_rr_message);

        // Activity
        mRecyclerUser.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (!mIsLoading && mCurrentPage <= mTotalPage) {
                        mIsLoading = true;
                        mSwipeRefresh.setRefreshing(true);
                        getFavoriteFromFollowing(mCurrentPage, false);
                    }
                }
            }
        });

        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mIsLoading = true;
                mSwipeRefresh.setRefreshing(true);
                getFavoriteFromFollowing(mStartPage, true);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mIsResumeFirstTime) {
            // Mendapatkan data awal
            mIsResumeFirstTime = false;
            mOnClickListener = this;
            mFavoriteFromFollowingRequest = new FavoriteFromFollowingRequest(mContext, mFilmID);
            getFavoriteFromFollowing(mStartPage, false);
        }
    }

    @Override
    public void onUserItemClick(int position) {
        User currentItem = mUserList.get(position);
        int id = currentItem.getId();
        gotoUserDetail(id);
    }

    private void getFavoriteFromFollowing(int page, final boolean refreshPage) {
        mFavoriteFromFollowingRequest.sendRequest(page, new FavoriteFromFollowingRequest.Callback() {
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
                mTextMessage.setText(R.string.empty_film_favorite_from_following);
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
