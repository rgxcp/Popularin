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
import xyz.fairportstudios.popularin.adapters.FilmReviewAdapter;
import xyz.fairportstudios.popularin.adapters.PagerAdapter;
import xyz.fairportstudios.popularin.apis.popularin.delete.UnlikeReviewRequest;
import xyz.fairportstudios.popularin.apis.popularin.get.FilmReviewFromAllRequest;
import xyz.fairportstudios.popularin.apis.popularin.post.LikeReviewRequest;
import xyz.fairportstudios.popularin.fragments.LikedReviewFragment;
import xyz.fairportstudios.popularin.fragments.ReviewFromAllFragment;
import xyz.fairportstudios.popularin.fragments.ReviewFromFollowingFragment;
import xyz.fairportstudios.popularin.fragments.SelfReviewFragment;
import xyz.fairportstudios.popularin.models.FilmReview;
import xyz.fairportstudios.popularin.preferences.Auth;
import xyz.fairportstudios.popularin.statics.Popularin;

public class FilmReviewActivity extends AppCompatActivity implements FilmReviewAdapter.OnClickListener {
    // Variable untuk fitur load more
    private Boolean mIsLoadFirstTimeSuccess = false;
    private Boolean mIsLoading = true;
    private Integer mStartPage = 1;
    private Integer mCurrentPage = 1;
    private Integer mTotalPage;

    // Variable member
    private Context mContext;
    private Boolean mIsAuth;
    private FilmReviewAdapter mFilmReviewAdapter;
    private FilmReviewAdapter.OnClickListener mOnClickListener;
    private FilmReviewFromAllRequest mFilmReviewFromAllRequest;
    private Integer mAuthID;
    private Integer mTotalLike;
    private List<FilmReview> mFilmReviewList;
    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerFilmReview;
    private RelativeLayout mAnchorLayout;
    private SwipeRefreshLayout mSwipeRefresh;
    private TextView mTextMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Context
        mContext = FilmReviewActivity.this;

        // Extra
        Intent intent = getIntent();
        Integer filmID = intent.getIntExtra(Popularin.FILM_ID, 0);

        // Auth
        Auth auth = new Auth(mContext);
        mIsAuth = auth.isAuth();
        mAuthID = auth.getAuthID();

        // Tampilan
        if (mIsAuth) {
            setContentView(R.layout.reusable_toolbar_pager);

            // Binding
            TabLayout tabLayout = findViewById(R.id.tab_rtp_layout);
            Toolbar toolbar = findViewById(R.id.toolbar_rtp_layout);
            ViewPager viewPager = findViewById(R.id.pager_rtp_layout);

            // Toolbar
            toolbar.setTitle(R.string.review);

            // Tab pager
            PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
            pagerAdapter.addFragment(new ReviewFromAllFragment(filmID), getString(R.string.all));
            pagerAdapter.addFragment(new ReviewFromFollowingFragment(filmID), getString(R.string.following));
            pagerAdapter.addFragment(new LikedReviewFragment(filmID), getString(R.string.liked));
            pagerAdapter.addFragment(new SelfReviewFragment(filmID), getString(R.string.self));
            viewPager.setAdapter(pagerAdapter);
            viewPager.setOffscreenPageLimit(4);
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
            mRecyclerFilmReview = findViewById(R.id.recycler_rtr_layout);
            mAnchorLayout = findViewById(R.id.anchor_rtr_layout);
            mSwipeRefresh = findViewById(R.id.swipe_refresh_rtr_layout);
            mTextMessage = findViewById(R.id.text_aud_message);
            Toolbar toolbar = findViewById(R.id.toolbar_rtr_layout);

            // Toolbar
            toolbar.setTitle(R.string.review);

            // Mendapatkan data awal
            mOnClickListener = this;
            mFilmReviewList = new ArrayList<>();
            mFilmReviewFromAllRequest = new FilmReviewFromAllRequest(mContext, filmID);
            getFilmReviewFromAll(mStartPage, false);

            // Activity
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });

            mRecyclerFilmReview.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                        if (!mIsLoading && mCurrentPage <= mTotalPage) {
                            mIsLoading = true;
                            mSwipeRefresh.setRefreshing(true);
                            getFilmReviewFromAll(mCurrentPage, false);
                        }
                    }
                }
            });

            mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    mIsLoading = true;
                    mSwipeRefresh.setRefreshing(true);
                    getFilmReviewFromAll(mStartPage, true);
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        resetState();
    }

    @Override
    public void onItemClick(int position) {
        FilmReview currentItem = mFilmReviewList.get(position);
        Integer reviewID = currentItem.getId();
        Boolean isSelf = currentItem.getUser_id().equals(mAuthID);
        gotoReviewDetail(reviewID, isSelf);
    }

    @Override
    public void onUserProfileClick(int position) {
        FilmReview currentItem = mFilmReviewList.get(position);
        Integer userID = currentItem.getUser_id();
        gotoUserDetail(userID);
    }

    @Override
    public void onLikeClick(int position) {
        if (mIsAuth) {
            FilmReview currentItem = mFilmReviewList.get(position);
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
        FilmReview currentItem = mFilmReviewList.get(position);
        Integer reviewID = currentItem.getId();
        Boolean isSelf = currentItem.getUser_id().equals(mAuthID);
        gotoReviewComment(reviewID, isSelf);
    }

    private void getFilmReviewFromAll(Integer page, final Boolean refreshPage) {
        // Menghilangkan pesan setiap kali method dijalankan
        mTextMessage.setVisibility(View.GONE);

        // Mengirim request
        mFilmReviewFromAllRequest.sendRequest(page, new FilmReviewFromAllRequest.Callback() {
            @Override
            public void onSuccess(Integer totalPage, List<FilmReview> filmReviewList) {
                if (!mIsLoadFirstTimeSuccess) {
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
                        mFilmReviewList.clear();
                        mFilmReviewAdapter.notifyDataSetChanged();
                    }
                    int insertIndex = mFilmReviewList.size();
                    mFilmReviewList.addAll(insertIndex, filmReviewList);
                    mFilmReviewAdapter.notifyItemChanged(insertIndex - 1);
                    mFilmReviewAdapter.notifyItemRangeInserted(insertIndex, filmReviewList.size());
                    mRecyclerFilmReview.scrollToPosition(insertIndex);
                }
                mCurrentPage++;
            }

            @Override
            public void onNotFound() {
                mProgressBar.setVisibility(View.GONE);
                mTextMessage.setVisibility(View.VISIBLE);
                mTextMessage.setText(R.string.empty_film_review);
            }

            @Override
            public void onError(String message) {
                if (!mIsLoadFirstTimeSuccess) {
                    mProgressBar.setVisibility(View.GONE);
                    mTextMessage.setVisibility(View.VISIBLE);
                    mTextMessage.setText(R.string.empty_film_review);
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

    private void likeReview(Integer reviewID, final Integer position) {
        // Mengirim request
        LikeReviewRequest likeReviewRequest = new LikeReviewRequest(mContext, reviewID);
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

    private void unlikeReview(Integer reviewID, final Integer position) {
        // Mengirim request
        UnlikeReviewRequest unlikeReviewRequest = new UnlikeReviewRequest(mContext, reviewID);
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
