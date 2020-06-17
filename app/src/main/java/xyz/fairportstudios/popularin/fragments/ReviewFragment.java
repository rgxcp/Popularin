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
    private Boolean mIsLoadFirstTimeSuccess = false;
    private Boolean mIsLoading = true;
    private Integer mStartPage = 1;
    private Integer mCurrentPage = 1;
    private Integer mTotalPage;

    // Variable member
    private Context mContext;
    private Boolean mIsAuth;
    private CoordinatorLayout mAnchorLayout;
    private Integer mAuthID;
    private Integer mTotalLike;
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
        mReviewList = new ArrayList<>();
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
    public void onItemClick(int position) {
        Review currentItem = mReviewList.get(position);
        Integer reviewID = currentItem.getId();
        Boolean isSelf = currentItem.getUser_id().equals(mAuthID);
        gotoReviewDetail(reviewID, isSelf);
    }

    @Override
    public void onUserProfileClicked(int position) {
        Review currentItem = mReviewList.get(position);
        Integer userID = currentItem.getUser_id();
        gotoUserDetail(userID);
    }

    @Override
    public void onFilmPosterClick(int position) {
        Review currentItem = mReviewList.get(position);
        Integer filmID = currentItem.getTmdb_id();
        gotoFilmDetail(filmID);
    }

    @Override
    public void onFilmPosterLongClick(int position) {
        Review currentItem = mReviewList.get(position);
        Integer filmID = currentItem.getTmdb_id();
        String filmTitle = currentItem.getTitle();
        String filmYear = new ParseDate().getYear(currentItem.getRelease_date());
        String filmPoster = currentItem.getPoster();
        showFilmModal(filmID, filmTitle, filmYear, filmPoster);
    }

    @Override
    public void onLikeClick(int position) {
        if (mIsAuth) {
            Review currentItem = mReviewList.get(position);
            Integer reviewID = currentItem.getId();
            Boolean isLiked = currentItem.getIs_liked();
            mTotalLike = currentItem.getTotal_like();

            if (!mIsLoading) {
                mIsLoading = true;
                if (isLiked) {
                    unlikeReview(reviewID, position);
                } else {
                    likeReview(reviewID, position);
                }
            }
        } else {
            gotoEmptyAccount();
        }
    }

    @Override
    public void onCommentClick(int position) {
        Review currentItem = mReviewList.get(position);
        Integer reviewID = currentItem.getId();
        Boolean isSelf = currentItem.getUser_id().equals(mAuthID);
        gotoReviewComment(reviewID, isSelf);
    }

    private void getReview(Integer page, final Boolean refreshPage) {
        // Menghilangkan pesan setiap kali method dijalankan
        mTextMessage.setVisibility(View.GONE);

        // Mengirim request
        mReviewRequest.sendRequest(page, new ReviewRequest.Callback() {
            @Override
            public void onSuccess(Integer totalPage, List<Review> reviewList) {
                if (!mIsLoadFirstTimeSuccess) {
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
                        mReviewList.clear();
                        mReviewAdapter.notifyDataSetChanged();
                    }
                    int insertIndex = mReviewList.size();
                    mReviewList.addAll(insertIndex, reviewList);
                    mReviewAdapter.notifyItemChanged(insertIndex - 1);
                    mReviewAdapter.notifyItemRangeInserted(insertIndex, reviewList.size());
                    mRecyclerReview.scrollToPosition(insertIndex);
                }
                mCurrentPage++;
            }

            @Override
            public void onNotFound() {
                mProgressBar.setVisibility(View.GONE);
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

    private void gotoReviewDetail(Integer reviewID, Boolean isSelf) {
        Intent intent = new Intent(mContext, ReviewActivity.class);
        intent.putExtra(Popularin.REVIEW_ID, reviewID);
        intent.putExtra(Popularin.IS_SELF, isSelf);
        intent.putExtra(Popularin.VIEW_PAGER_INDEX, 0);
        startActivity(intent);
    }

    private void gotoReviewComment(Integer reviewID, Boolean isSelf) {
        Intent intent = new Intent(mContext, ReviewActivity.class);
        intent.putExtra(Popularin.REVIEW_ID, reviewID);
        intent.putExtra(Popularin.IS_SELF, isSelf);
        intent.putExtra(Popularin.VIEW_PAGER_INDEX, 1);
        startActivity(intent);
    }

    private void gotoUserDetail(Integer userID) {
        Intent intent = new Intent(mContext, UserDetailActivity.class);
        intent.putExtra(Popularin.USER_ID, userID);
        startActivity(intent);
    }

    private void gotoFilmDetail(Integer filmID) {
        Intent intent = new Intent(mContext, FilmDetailActivity.class);
        intent.putExtra(Popularin.FILM_ID, filmID);
        startActivity(intent);
    }

    private void showFilmModal(Integer filmID, String filmTitle, String filmYear, String filmPoster) {
        FragmentManager fragmentManager = ((FragmentActivity) mContext).getSupportFragmentManager();
        FilmModal filmModal = new FilmModal(filmID, filmTitle, filmYear, filmPoster);
        filmModal.show(fragmentManager, Popularin.FILM_STATUS_MODAL);
    }

    private void likeReview(Integer reviewID, final Integer position) {
        // Mengirim request
        LikeReviewRequest likeReviewRequest = new LikeReviewRequest(mContext, reviewID);
        likeReviewRequest.sendRequest(new LikeReviewRequest.Callback() {
            @Override
            public void onSuccess() {
                mTotalLike++;
                mReviewList.get(position).setIs_liked(true);
                mReviewList.get(position).setTotal_like(mTotalLike);
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

    private void unlikeReview(Integer reviewID, final Integer position) {
        // Mengirim request
        UnlikeReviewRequest unlikeReviewRequest = new UnlikeReviewRequest(mContext, reviewID);
        unlikeReviewRequest.sendRequest(new UnlikeReviewRequest.Callback() {
            @Override
            public void onSuccess() {
                mTotalLike--;
                mReviewList.get(position).setIs_liked(false);
                mReviewList.get(position).setTotal_like(mTotalLike);
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
        mIsLoadFirstTimeSuccess = false;
        mIsLoading = true;
        mCurrentPage = 1;
    }
}
