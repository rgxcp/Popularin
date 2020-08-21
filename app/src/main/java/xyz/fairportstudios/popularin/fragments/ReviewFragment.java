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
import xyz.fairportstudios.popularin.activities.EmptyAccountActivity;
import xyz.fairportstudios.popularin.activities.FilmDetailActivity;
import xyz.fairportstudios.popularin.activities.ReviewActivity;
import xyz.fairportstudios.popularin.activities.UserDetailActivity;
import xyz.fairportstudios.popularin.adapters.ReviewAdapter;
import xyz.fairportstudios.popularin.apis.popularin.delete.UnlikeReviewRequest;
import xyz.fairportstudios.popularin.apis.popularin.get.ReviewRequest;
import xyz.fairportstudios.popularin.apis.popularin.post.LikeReviewRequest;
import xyz.fairportstudios.popularin.modals.FilmModal;
import xyz.fairportstudios.popularin.models.Review;
import xyz.fairportstudios.popularin.preferences.Auth;
import xyz.fairportstudios.popularin.services.ParseDate;
import xyz.fairportstudios.popularin.statics.Popularin;

public class ReviewFragment extends Fragment implements ReviewAdapter.OnClickListener {
    // Variable untuk fitur load more
    private boolean mIsLoading = true;
    private boolean mIsLoadFirstTimeSuccess = false;
    private int mStartPage = 1;
    private int mCurrentPage = 1;
    private int mTotalPage;

    // Variable member
    private boolean mIsAuth;
    private int mAuthID;
    private int mTotalLike;
    private Context mContext;
    private CoordinatorLayout mAnchorLayout;
    private List<Review> mReviewList;
    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerReview;
    private ReviewAdapter mReviewAdapter;
    private ReviewAdapter.OnClickListener mOnClickListener;
    private ReviewRequest mReviewRequest;
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
        mRecyclerReview = view.findViewById(R.id.recycler_rr_layout);
        mSwipeRefresh = view.findViewById(R.id.swipe_refresh_rr_layout);
        mTextMessage = view.findViewById(R.id.text_rr_message);

        // Auth
        Auth auth = new Auth(mContext);
        mIsAuth = auth.isAuth();
        mAuthID = auth.getAuthID();

        // Mendapatkan data awal
        mOnClickListener = this;
        mReviewRequest = new ReviewRequest(mContext);
        getReview(mStartPage, false);

        // Activity
        mRecyclerReview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (!mIsLoading && mCurrentPage <= mTotalPage) {
                        mIsLoading = true;
                        mSwipeRefresh.setRefreshing(true);
                        getReview(mCurrentPage, false);
                    }
                }
            }
        });

        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mIsLoading = true;
                mSwipeRefresh.setRefreshing(true);
                getReview(mStartPage, true);
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
    public void onReviewItemClick(int position) {
        Review currentItem = mReviewList.get(position);
        int id = currentItem.getId();
        boolean isSelf = currentItem.getUser_id() == mAuthID;
        gotoReviewDetail(id, isSelf);
    }

    @Override
    public void onReviewUserProfileClick(int position) {
        Review currentItem = mReviewList.get(position);
        int id = currentItem.getUser_id();
        gotoUserDetail(id);
    }

    @Override
    public void onReviewFilmPosterClick(int position) {
        Review currentItem = mReviewList.get(position);
        int id = currentItem.getTmdb_id();
        gotoFilmDetail(id);
    }

    @Override
    public void onReviewFilmPosterLongClick(int position) {
        Review currentItem = mReviewList.get(position);
        int id = currentItem.getTmdb_id();
        String title = currentItem.getTitle();
        String year = new ParseDate().getYear(currentItem.getRelease_date());
        String poster = currentItem.getPoster();
        showFilmModal(id, title, year, poster);
    }

    @Override
    public void onReviewLikeClick(int position) {
        if (mIsAuth) {
            Review currentItem = mReviewList.get(position);
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
        } else {
            gotoEmptyAccount();
        }
    }

    @Override
    public void onReviewCommentClick(int position) {
        Review currentItem = mReviewList.get(position);
        int id = currentItem.getId();
        boolean isSelf = currentItem.getUser_id() == mAuthID;
        gotoReviewComment(id, isSelf);
    }

    private void getReview(int page, final boolean refreshPage) {
        mReviewRequest.sendRequest(page, new ReviewRequest.Callback() {
            @Override
            public void onSuccess(int totalPage, List<Review> reviewList) {
                if (!mIsLoadFirstTimeSuccess) {
                    mReviewList = new ArrayList<>();
                    int insertIndex = mReviewList.size();
                    mReviewList.addAll(insertIndex, reviewList);
                    mReviewAdapter = new ReviewAdapter(mContext, mReviewList, mOnClickListener);
                    mRecyclerReview.setAdapter(mReviewAdapter);
                    mRecyclerReview.setLayoutManager(new LinearLayoutManager(mContext));
                    mRecyclerReview.setVisibility(View.VISIBLE);
                    mProgressBar.setVisibility(View.GONE);
                    mTotalPage = totalPage;
                    mIsLoadFirstTimeSuccess = true;
                } else {
                    if (refreshPage) {
                        mCurrentPage = 1;
                        mTotalPage = totalPage;
                        mReviewList.clear();
                        mReviewAdapter.notifyDataSetChanged();
                    }
                    int insertIndex = mReviewList.size();
                    mReviewList.addAll(insertIndex, reviewList);
                    mReviewAdapter.notifyItemChanged(insertIndex - 1);
                    mReviewAdapter.notifyItemRangeInserted(insertIndex, reviewList.size());
                    mRecyclerReview.scrollToPosition(insertIndex);
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
                    mReviewList.clear();
                    mReviewAdapter.notifyDataSetChanged();
                }
                mTextMessage.setVisibility(View.VISIBLE);
                mTextMessage.setText(R.string.empty_review);
            }

            @Override
            public void onError(String message) {
                if (!mIsLoadFirstTimeSuccess) {
                    mProgressBar.setVisibility(View.GONE);
                    mTextMessage.setVisibility(View.VISIBLE);
                    mTextMessage.setText(R.string.empty_review);
                }
                Snackbar.make(mAnchorLayout, message, Snackbar.LENGTH_LONG).show();
            }
        });

        // Memberhentikan loading
        mIsLoading = false;
        mSwipeRefresh.setRefreshing(false);
    }

    private void gotoReviewDetail(int id, boolean isSelf) {
        Intent intent = new Intent(mContext, ReviewActivity.class);
        intent.putExtra(Popularin.REVIEW_ID, id);
        intent.putExtra(Popularin.IS_SELF, isSelf);
        startActivity(intent);
    }

    private void gotoReviewComment(int id, boolean isSelf) {
        Intent intent = new Intent(mContext, ReviewActivity.class);
        intent.putExtra(Popularin.REVIEW_ID, id);
        intent.putExtra(Popularin.IS_SELF, isSelf);
        intent.putExtra(Popularin.VIEW_PAGER_INDEX, 1);
        startActivity(intent);
    }

    private void gotoUserDetail(int id) {
        Intent intent = new Intent(mContext, UserDetailActivity.class);
        intent.putExtra(Popularin.USER_ID, id);
        startActivity(intent);
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

    private void likeReview(int id, final int position) {
        LikeReviewRequest likeReviewRequest = new LikeReviewRequest(mContext, id);
        likeReviewRequest.sendRequest(new LikeReviewRequest.Callback() {
            @Override
            public void onSuccess() {
                mTotalLike++;
                Review currentItem = mReviewList.get(position);
                currentItem.setIs_liked(true);
                currentItem.setTotal_like(mTotalLike);
                mReviewAdapter.notifyItemChanged(position);
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
                Review currentItem = mReviewList.get(position);
                currentItem.setIs_liked(false);
                currentItem.setTotal_like(mTotalLike);
                mReviewAdapter.notifyItemChanged(position);
            }

            @Override
            public void onError(String message) {
                Snackbar.make(mAnchorLayout, message, Snackbar.LENGTH_LONG).show();
            }
        });

        // Memberhentikan loading
        mIsLoading = false;
    }

    private void gotoEmptyAccount() {
        Intent intent = new Intent(mContext, EmptyAccountActivity.class);
        startActivity(intent);
    }

    private void resetState() {
        mIsLoading = true;
        mIsLoadFirstTimeSuccess = false;
        mCurrentPage = 1;
    }
}
