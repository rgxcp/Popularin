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
    private FilmReviewAdapter mFilmReviewAdapter;
    private FilmReviewAdapter.OnClickListener mOnClickListener;
    private FilmReviewFromAllRequest mFilmReviewFromAllRequest;
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
        int filmID = intent.getIntExtra(Popularin.FILM_ID, 0);

        // Auth
        Auth auth = new Auth(mContext);
        mIsAuth = auth.isAuth();
        mAuthID = auth.getAuthID();

        // Menampilkan layout berdasarkan kondisi
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
    public void onFilmReviewItemClick(int position) {
        FilmReview currentItem = mFilmReviewList.get(position);
        int id = currentItem.getId();
        boolean isSelf = currentItem.getUser_id() == mAuthID;
        gotoReviewDetail(id, isSelf);
    }

    @Override
    public void onFilmReviewUserProfileClick(int position) {
        FilmReview currentItem = mFilmReviewList.get(position);
        int id = currentItem.getUser_id();
        gotoUserDetail(id);
    }

    @Override
    public void onFilmReviewLikeClick(int position) {
        if (mIsAuth) {
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
        } else {
            gotoEmptyAccount();
        }
    }

    @Override
    public void onFilmReviewCommentClick(int position) {
        FilmReview currentItem = mFilmReviewList.get(position);
        int id = currentItem.getId();
        boolean isSelf = currentItem.getUser_id() == mAuthID;
        gotoReviewComment(id, isSelf);
    }

    private void getFilmReviewFromAll(int page, final boolean refreshPage) {
        mFilmReviewFromAllRequest.sendRequest(page, new FilmReviewFromAllRequest.Callback() {
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
                mTextMessage.setText(R.string.empty_film_review);
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

    private void gotoEmptyAccount() {
        Intent intent = new Intent(mContext, EmptyAccountActivity.class);
        startActivity(intent);
    }
}
