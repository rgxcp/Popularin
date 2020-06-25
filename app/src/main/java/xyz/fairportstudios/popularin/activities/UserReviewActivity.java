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
import xyz.fairportstudios.popularin.adapters.UserReviewAdapter;
import xyz.fairportstudios.popularin.apis.popularin.delete.UnlikeReviewRequest;
import xyz.fairportstudios.popularin.apis.popularin.get.UserReviewRequest;
import xyz.fairportstudios.popularin.apis.popularin.post.LikeReviewRequest;
import xyz.fairportstudios.popularin.modals.FilmModal;
import xyz.fairportstudios.popularin.models.UserReview;
import xyz.fairportstudios.popularin.preferences.Auth;
import xyz.fairportstudios.popularin.services.ParseDate;
import xyz.fairportstudios.popularin.statics.Popularin;

public class UserReviewActivity extends AppCompatActivity implements UserReviewAdapter.OnClickListener {
    // Variable untuk fitur load more
    private boolean mIsLoading = true;
    private boolean mIsLoadFirstTimeSuccess = false;
    private int mStartPage = 1;
    private int mCurrentPage = 1;
    private int mTotalPage;

    // Variable member
    private boolean mIsAuth;
    private boolean mIsSelf;
    private int mTotalLike;
    private Context mContext;
    private List<UserReview> mUserReviewList;
    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerUserReview;
    private RelativeLayout mAnchorLayout;
    private SwipeRefreshLayout mSwipeRefresh;
    private TextView mTextMessage;
    private UserReviewAdapter mUserReviewAdapter;
    private UserReviewAdapter.OnClickListener mOnClickListener;
    private UserReviewRequest mUserReviewRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reusable_toolbar_recycler);

        // Context
        mContext = UserReviewActivity.this;

        // Binding
        mProgressBar = findViewById(R.id.pbr_rtr_layout);
        mRecyclerUserReview = findViewById(R.id.recycler_rtr_layout);
        mAnchorLayout = findViewById(R.id.anchor_rtr_layout);
        mSwipeRefresh = findViewById(R.id.swipe_refresh_rtr_layout);
        mTextMessage = findViewById(R.id.text_aud_message);
        Toolbar toolbar = findViewById(R.id.toolbar_rtr_layout);

        // Extra
        Intent intent = getIntent();
        int userID = intent.getIntExtra(Popularin.USER_ID, 0);

        // Auth
        Auth auth = new Auth(mContext);
        mIsAuth = auth.isAuth();
        mIsSelf = userID == auth.getAuthID();

        // Toolbar
        toolbar.setTitle(R.string.review);

        // Mendapatkan data awal
        mOnClickListener = this;
        mUserReviewRequest = new UserReviewRequest(mContext, userID);
        getUserReview(mStartPage, false);

        // Activity
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        mRecyclerUserReview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (!mIsLoading && mCurrentPage <= mTotalPage) {
                        mIsLoading = true;
                        mSwipeRefresh.setRefreshing(true);
                        getUserReview(mCurrentPage, false);
                    }
                }
            }
        });

        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mIsLoading = true;
                mSwipeRefresh.setRefreshing(true);
                getUserReview(mStartPage, true);
            }
        });
    }

    @Override
    public void onUserReviewItemClick(int position) {
        UserReview currentItem = mUserReviewList.get(position);
        int id = currentItem.getId();
        gotoReviewDetail(id, mIsSelf);
    }

    @Override
    public void onUserReviewFilmPosterClick(int position) {
        UserReview currentItem = mUserReviewList.get(position);
        int id = currentItem.getTmdb_id();
        gotoFilmDetail(id);
    }

    @Override
    public void onUserReviewFilmPosterLongClick(int position) {
        UserReview currentItem = mUserReviewList.get(position);
        int id = currentItem.getTmdb_id();
        String title = currentItem.getTitle();
        String year = new ParseDate().getYear(currentItem.getRelease_date());
        String poster = currentItem.getPoster();
        showFilmModal(id, title, year, poster);
    }

    @Override
    public void onUserReviewLikeClick(int position) {
        if (mIsAuth) {
            UserReview currentItem = mUserReviewList.get(position);
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
    public void onUserReviewCommentClick(int position) {
        UserReview currentItem = mUserReviewList.get(position);
        int id = currentItem.getId();
        gotoReviewComment(id, mIsSelf);
    }

    private void getUserReview(int page, final boolean refreshPage) {
        mUserReviewRequest.sendRequest(page, new UserReviewRequest.Callback() {
            @Override
            public void onSuccess(int totalPage, List<UserReview> userReviewList) {
                if (!mIsLoadFirstTimeSuccess) {
                    mUserReviewList = new ArrayList<>();
                    int insertIndex = mUserReviewList.size();
                    mUserReviewList.addAll(insertIndex, userReviewList);
                    mUserReviewAdapter = new UserReviewAdapter(mContext, mUserReviewList, mOnClickListener);
                    mRecyclerUserReview.setAdapter(mUserReviewAdapter);
                    mRecyclerUserReview.setLayoutManager(new LinearLayoutManager(mContext));
                    mRecyclerUserReview.setVisibility(View.VISIBLE);
                    mProgressBar.setVisibility(View.GONE);
                    mTotalPage = totalPage;
                    mIsLoadFirstTimeSuccess = true;
                } else {
                    if (refreshPage) {
                        mCurrentPage = 1;
                        mUserReviewList.clear();
                        mUserReviewAdapter.notifyDataSetChanged();
                    }
                    int insertIndex = mUserReviewList.size();
                    mUserReviewList.addAll(insertIndex, userReviewList);
                    mUserReviewAdapter.notifyItemChanged(insertIndex - 1);
                    mUserReviewAdapter.notifyItemRangeInserted(insertIndex, userReviewList.size());
                    mRecyclerUserReview.scrollToPosition(insertIndex);
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
                    mUserReviewList.clear();
                    mUserReviewAdapter.notifyDataSetChanged();
                }
                mTextMessage.setVisibility(View.VISIBLE);
                if (mIsSelf) {
                    mTextMessage.setText(R.string.empty_self_review);
                } else {
                    mTextMessage.setText(R.string.empty_user_review);
                }
            }

            @Override
            public void onError(String message) {
                if (!mIsLoadFirstTimeSuccess) {
                    mProgressBar.setVisibility(View.GONE);
                    mTextMessage.setVisibility(View.VISIBLE);
                    if (mIsSelf) {
                        mTextMessage.setText(R.string.empty_self_review);
                    } else {
                        mTextMessage.setText(R.string.empty_user_review);
                    }
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

    private void gotoFilmDetail(int id) {
        Intent intent = new Intent(mContext, FilmDetailActivity.class);
        intent.putExtra(Popularin.FILM_ID, id);
        startActivity(intent);
    }

    private void showFilmModal(int id, String title, String year, String poster) {
        FilmModal filmModal = new FilmModal(id, title, year, poster);
        filmModal.show(getSupportFragmentManager(), Popularin.FILM_MODAL);
    }

    private void likeReview(int id, final int position) {
        LikeReviewRequest likeReviewRequest = new LikeReviewRequest(mContext, id);
        likeReviewRequest.sendRequest(new LikeReviewRequest.Callback() {
            @Override
            public void onSuccess() {
                mTotalLike++;
                UserReview currentItem = mUserReviewList.get(position);
                currentItem.setIs_liked(true);
                currentItem.setTotal_like(mTotalLike);
                mUserReviewAdapter.notifyItemChanged(position);
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
                UserReview currentItem = mUserReviewList.get(position);
                currentItem.setIs_liked(false);
                currentItem.setTotal_like(mTotalLike);
                mUserReviewAdapter.notifyItemChanged(position);
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
}
