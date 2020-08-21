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
import xyz.fairportstudios.popularin.activities.ReviewActivity;
import xyz.fairportstudios.popularin.activities.UserDetailActivity;
import xyz.fairportstudios.popularin.adapters.FilmReviewAdapter;
import xyz.fairportstudios.popularin.apis.popularin.delete.UnlikeReviewRequest;
import xyz.fairportstudios.popularin.apis.popularin.get.SelfFilmReviewRequest;
import xyz.fairportstudios.popularin.apis.popularin.post.LikeReviewRequest;
import xyz.fairportstudios.popularin.models.FilmReview;
import xyz.fairportstudios.popularin.statics.Popularin;

public class SelfReviewFragment extends Fragment implements FilmReviewAdapter.OnClickListener {
    // Variable untuk fitur onResume
    private boolean mIsResumeFirstTime = true;

    // Variable untuk fitur load more
    private boolean mIsLoading = true;
    private boolean mIsLoadFirstTimeSuccess = false;
    private int mStartPage = 1;
    private int mCurrentPage = 1;
    private int mTotalPage;

    // Variable member
    private int mTotalLike;
    private Context mContext;
    private CoordinatorLayout mAnchorLayout;
    private FilmReviewAdapter mFilmReviewAdapter;
    private FilmReviewAdapter.OnClickListener mOnClickListener;
    private List<FilmReview> mFilmReviewList;
    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerFilmReview;
    private SelfFilmReviewRequest mSelfFilmReviewRequest;
    private SwipeRefreshLayout mSwipeRefresh;
    private TextView mTextMessage;

    // Variable constructor
    private int mFilmID;

    public SelfReviewFragment(int filmID) {
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
        mRecyclerFilmReview = view.findViewById(R.id.recycler_rr_layout);
        mSwipeRefresh = view.findViewById(R.id.swipe_refresh_rr_layout);
        mTextMessage = view.findViewById(R.id.text_rr_message);

        // Activity
        mRecyclerFilmReview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (!mIsLoading && mCurrentPage <= mTotalPage) {
                        mIsLoading = true;
                        mSwipeRefresh.setRefreshing(true);
                        getSelfFilmReview(mCurrentPage, false);
                    }
                }
            }
        });

        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mIsLoading = true;
                mSwipeRefresh.setRefreshing(true);
                getSelfFilmReview(mStartPage, true);
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
            mSelfFilmReviewRequest = new SelfFilmReviewRequest(mContext, mFilmID);
            getSelfFilmReview(mStartPage, false);
        }
    }

    @Override
    public void onFilmReviewItemClick(int position) {
        FilmReview currentItem = mFilmReviewList.get(position);
        int id = currentItem.getId();
        gotoReviewDetail(id);
    }

    @Override
    public void onFilmReviewUserProfileClick(int position) {
        FilmReview currentItem = mFilmReviewList.get(position);
        int id = currentItem.getUser_id();
        gotoUserDetail(id);
    }

    @Override
    public void onFilmReviewLikeClick(int position) {
        FilmReview currentItem = mFilmReviewList.get(position);
        int id = currentItem.getId();
        boolean isLiked = currentItem.getIs_liked();
        mTotalLike = currentItem.getTotal_like();

        if (!mIsLoading) {
            mIsLoading = true;
            if (!isLiked) {
                likeReview(id, position);
            } else {
                unlikeReview(id, position);
            }
        }
    }

    @Override
    public void onFilmReviewCommentClick(int position) {
        FilmReview currentItem = mFilmReviewList.get(position);
        int id = currentItem.getId();
        gotoReviewComment(id);
    }

    private void getSelfFilmReview(int page, final boolean refreshPage) {
        mSelfFilmReviewRequest.sendRequest(page, new SelfFilmReviewRequest.Callback() {
            @Override
            public void onSuccess(int totalPage, List<FilmReview> filmReviewList) {
                if (!mIsLoadFirstTimeSuccess) {
                    mFilmReviewList = new ArrayList<>();
                    int insertIndex = mFilmReviewList.size();
                    mFilmReviewList.addAll(insertIndex, filmReviewList);
                    mFilmReviewAdapter = new FilmReviewAdapter(mContext, mFilmReviewList, mOnClickListener);
                    mRecyclerFilmReview.setAdapter(mFilmReviewAdapter);
                    mRecyclerFilmReview.setLayoutManager(new LinearLayoutManager(mContext));
                    mRecyclerFilmReview.setVisibility(View.VISIBLE);
                    mProgressBar.setVisibility(View.GONE);
                    mTotalPage = totalPage;
                    mIsLoadFirstTimeSuccess = true;
                } else {
                    if (refreshPage) {
                        mCurrentPage = 1;
                        mTotalPage = totalPage;
                        mFilmReviewList.clear();
                        mFilmReviewAdapter.notifyDataSetChanged();
                    }
                    int insertIndex = mFilmReviewList.size();
                    mFilmReviewList.addAll(insertIndex, filmReviewList);
                    mFilmReviewAdapter.notifyItemChanged(insertIndex - 1);
                    mFilmReviewAdapter.notifyItemRangeInserted(insertIndex, filmReviewList.size());
                    mRecyclerFilmReview.scrollToPosition(insertIndex);
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
                    mFilmReviewList.clear();
                    mFilmReviewAdapter.notifyDataSetChanged();
                }
                mTextMessage.setVisibility(View.VISIBLE);
                mTextMessage.setText(R.string.empty_self_film_review);
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

    private void gotoReviewDetail(int id) {
        Intent intent = new Intent(mContext, ReviewActivity.class);
        intent.putExtra(Popularin.REVIEW_ID, id);
        intent.putExtra(Popularin.IS_SELF, true);
        startActivity(intent);
    }

    private void gotoReviewComment(int id) {
        Intent intent = new Intent(mContext, ReviewActivity.class);
        intent.putExtra(Popularin.REVIEW_ID, id);
        intent.putExtra(Popularin.IS_SELF, true);
        intent.putExtra(Popularin.VIEW_PAGER_INDEX, 1);
        startActivity(intent);
    }

    private void gotoUserDetail(int id) {
        Intent intent = new Intent(mContext, UserDetailActivity.class);
        intent.putExtra(Popularin.USER_ID, id);
        startActivity(intent);
    }

    private void likeReview(int id, final int position) {
        LikeReviewRequest likeReviewRequest = new LikeReviewRequest(mContext, id);
        likeReviewRequest.sendRequest(new LikeReviewRequest.Callback() {
            @Override
            public void onSuccess() {
                mTotalLike++;
                FilmReview currentItem = mFilmReviewList.get(position);
                currentItem.setIs_liked(true);
                currentItem.setTotal_like(mTotalLike);
                mFilmReviewAdapter.notifyItemChanged(position);
            }

            @Override
            public void onError(String message) {
                Snackbar.make(mAnchorLayout, message, Snackbar.LENGTH_LONG).show();
            }
        });

        // Memberhentikan loading
        mIsLoading = false;
    }

    private void unlikeReview(int id, final int position) {
        UnlikeReviewRequest unlikeReviewRequest = new UnlikeReviewRequest(mContext, id);
        unlikeReviewRequest.sendRequest(new UnlikeReviewRequest.Callback() {
            @Override
            public void onSuccess() {
                mTotalLike--;
                FilmReview currentItem = mFilmReviewList.get(position);
                currentItem.setIs_liked(false);
                currentItem.setTotal_like(mTotalLike);
                mFilmReviewAdapter.notifyItemChanged(position);
            }

            @Override
            public void onError(String message) {
                Snackbar.make(mAnchorLayout, message, Snackbar.LENGTH_LONG).show();
            }
        });

        // Memberhentikan loading
        mIsLoading = false;
    }
}
