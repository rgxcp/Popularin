package xyz.fairportstudios.popularin.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.adapters.RecentFavoriteAdapter;
import xyz.fairportstudios.popularin.adapters.RecentReviewAdapter;
import xyz.fairportstudios.popularin.apis.popularin.delete.UnfollowUserRequest;
import xyz.fairportstudios.popularin.apis.popularin.get.UserDetailRequest;
import xyz.fairportstudios.popularin.apis.popularin.post.FollowUserRequest;
import xyz.fairportstudios.popularin.modals.FilmModal;
import xyz.fairportstudios.popularin.models.RecentFavorite;
import xyz.fairportstudios.popularin.models.RecentReview;
import xyz.fairportstudios.popularin.models.UserDetail;
import xyz.fairportstudios.popularin.preferences.Auth;
import xyz.fairportstudios.popularin.services.ParseDate;
import xyz.fairportstudios.popularin.statics.Popularin;

public class UserDetailActivity extends AppCompatActivity implements RecentFavoriteAdapter.OnClickListener, RecentReviewAdapter.OnClickListener {
    // Variable untuk fitur load
    private boolean mIsLoadFirstTimeSuccess = false;

    // Variable member
    private boolean mIsSelf;
    private boolean mIsFollower;
    private boolean mIsFollowing;
    private int mTotalFollower;
    private Context mContext;
    private Button mButtonFollow;
    private ImageView mImageProfile;
    private ImageView mImageEmptyRecentFavorite;
    private ImageView mImageEmptyRecentReview;
    private List<RecentFavorite> mRecentFavoriteList;
    private List<RecentReview> mRecentReviewList;
    private ProgressBar mProgressBar;
    private RecentFavoriteAdapter.OnClickListener mOnRecentFavoriteClickListener;
    private RecentReviewAdapter.OnClickListener mOnRecentReviewClickListener;
    private RecyclerView mRecyclerRecentFavorite;
    private RecyclerView mRecyclerRecentReview;
    private RelativeLayout mAnchorLayout;
    private ScrollView mScrollView;
    private SwipeRefreshLayout mSwipeRefresh;
    private TextView mTextFullName;
    private TextView mTextUsername;
    private TextView mTextFollowMe;
    private TextView mTextTotalReview;
    private TextView mTextTotalFavorite;
    private TextView mTextTotalWatchlist;
    private TextView mTextTotalFollower;
    private TextView mTextTotalFollowing;
    private TextView mTextMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        // Context
        mContext = UserDetailActivity.this;

        // Binding
        mButtonFollow = findViewById(R.id.button_aud_follow);
        mImageProfile = findViewById(R.id.image_aud_profile);
        mImageEmptyRecentFavorite = findViewById(R.id.image_aud_empty_recent_favorite);
        mImageEmptyRecentReview = findViewById(R.id.image_aud_empty_recent_review);
        mProgressBar = findViewById(R.id.pbr_aud_layout);
        mRecyclerRecentFavorite = findViewById(R.id.recycler_aud_recent_favorite);
        mRecyclerRecentReview = findViewById(R.id.recycler_aud_recent_review);
        mAnchorLayout = findViewById(R.id.anchor_aud_layout);
        mScrollView = findViewById(R.id.scroll_aud_layout);
        mSwipeRefresh = findViewById(R.id.swipe_refresh_aud_layout);
        mTextFullName = findViewById(R.id.text_aud_full_name);
        mTextUsername = findViewById(R.id.text_aud_username);
        mTextFollowMe = findViewById(R.id.text_aud_follow_me);
        mTextTotalReview = findViewById(R.id.text_aud_total_review);
        mTextTotalFavorite = findViewById(R.id.text_aud_total_favorite);
        mTextTotalWatchlist = findViewById(R.id.text_aud_total_watchlist);
        mTextTotalFollower = findViewById(R.id.text_aud_total_follower);
        mTextTotalFollowing = findViewById(R.id.text_aud_total_following);
        mTextMessage = findViewById(R.id.text_aud_message);
        LinearLayout totalReviewLayout = findViewById(R.id.layout_aud_total_review);
        LinearLayout totalFavoriteLayout = findViewById(R.id.layout_aud_total_favorite);
        LinearLayout totalWatchlistLayout = findViewById(R.id.layout_aud_total_watchlist);
        LinearLayout totalFollowerLayout = findViewById(R.id.layout_aud_total_follower);
        LinearLayout totalFollowingLayout = findViewById(R.id.layout_aud_total_following);
        Toolbar toolbar = findViewById(R.id.toolbar_aud_layout);

        // Extra
        Intent intent = getIntent();
        final int userID = intent.getIntExtra(Popularin.USER_ID, 0);

        // Auth
        Auth auth = new Auth(mContext);
        final boolean isAuth = auth.isAuth();
        mIsSelf = userID == auth.getAuthID();
        if (mIsSelf) {
            mButtonFollow.setText(R.string.edit_profile);
        }

        // Mendapatkan data
        mOnRecentFavoriteClickListener = this;
        mOnRecentReviewClickListener = this;
        getUserDetail(userID);

        // Activity
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        totalReviewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoUserReview(userID);
            }
        });

        totalFavoriteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoUserFavorite(userID);
            }
        });

        totalWatchlistLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoUserWatchlist(userID);
            }
        });

        totalFollowerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoUserSocial(userID, 0);
            }
        });

        totalFollowingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoUserSocial(userID, 1);
            }
        });

        mButtonFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAuth && !mIsSelf) {
                    if (!mIsFollowing) {
                        setFollowButtonState(false, FollowingState.LOADING);
                        followUser(userID);
                    } else {
                        setFollowButtonState(false, FollowingState.LOADING);
                        unfollowUser(userID);
                    }
                } else if (isAuth) {
                    gotoEditProfile();
                } else {
                    gotoEmptyAccount();
                }
            }
        });

        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefresh.setRefreshing(true);
                getUserDetail(userID);
            }
        });
    }

    @Override
    public void onRecentFavoriteItemClick(int position) {
        RecentFavorite currentItem = mRecentFavoriteList.get(position);
        int id = currentItem.getTmdb_id();
        gotoFilmDetail(id);
    }

    @Override
    public void onRecentFavoriteItemLongClick(int position) {
        RecentFavorite currentItem = mRecentFavoriteList.get(position);
        int id = currentItem.getTmdb_id();
        String title = currentItem.getTitle();
        String year = new ParseDate().getYear(currentItem.getRelease_date());
        String poster = currentItem.getPoster();
        showFilmModal(id, title, year, poster);
    }

    @Override
    public void onRecentReviewItemClick(int position) {
        RecentReview currentItem = mRecentReviewList.get(position);
        int id = currentItem.getId();
        gotoReviewDetail(id);
    }

    @Override
    public void onRecentReviewItemLongClick(int position) {
        RecentReview currentItem = mRecentReviewList.get(position);
        int id = currentItem.getTmdb_id();
        String title = currentItem.getTitle();
        String year = new ParseDate().getYear(currentItem.getRelease_date());
        String poster = currentItem.getPoster();
        showFilmModal(id, title, year, poster);
    }

    private void getUserDetail(int id) {
        final UserDetailRequest userDetailRequest = new UserDetailRequest(mContext, id);
        userDetailRequest.sendRequest(new UserDetailRequest.Callback() {
            @Override
            public void onSuccess(UserDetail userDetail) {
                // Following status
                mIsFollower = userDetail.getIs_follower();
                mIsFollowing = userDetail.getIs_following();
                if (mIsFollower) {
                    mTextFollowMe.setVisibility(View.VISIBLE);
                } else {
                    mTextFollowMe.setVisibility(View.GONE);
                }
                if (mIsFollowing) {
                    mButtonFollow.setText(R.string.following);
                }

                // Isi
                mTotalFollower = userDetail.getTotal_follower();
                mTextFullName.setText(userDetail.getFull_name());
                mTextUsername.setText(String.format("@%s", userDetail.getUsername()));
                mTextTotalReview.setText(String.valueOf(userDetail.getTotal_review()));
                mTextTotalFavorite.setText(String.valueOf(userDetail.getTotal_favorite()));
                mTextTotalWatchlist.setText(String.valueOf(userDetail.getTotal_watchlist()));
                mTextTotalFollower.setText(String.valueOf(mTotalFollower));
                mTextTotalFollowing.setText(String.valueOf(userDetail.getTotal_following()));
                Glide.with(mContext).load(userDetail.getProfile_picture()).into(mImageProfile);
                mProgressBar.setVisibility(View.GONE);
                mTextMessage.setVisibility(View.GONE);
                mScrollView.setVisibility(View.VISIBLE);
                mIsLoadFirstTimeSuccess = true;
            }

            @Override
            public void onHasRecentFavorite(List<RecentFavorite> recentFavoriteList) {
                mRecentFavoriteList = new ArrayList<>();
                mRecentFavoriteList.addAll(recentFavoriteList);
                RecentFavoriteAdapter recentFavoriteAdapter = new RecentFavoriteAdapter(mContext, mRecentFavoriteList, mOnRecentFavoriteClickListener);
                mRecyclerRecentFavorite.setAdapter(recentFavoriteAdapter);
                mRecyclerRecentFavorite.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false));
                mRecyclerRecentFavorite.setHasFixedSize(true);
                mRecyclerRecentFavorite.setVisibility(View.VISIBLE);
                mImageEmptyRecentFavorite.setVisibility(View.GONE);
            }

            @Override
            public void onHasRecentReview(List<RecentReview> recentReviewList) {
                mRecentReviewList = new ArrayList<>();
                mRecentReviewList.addAll(recentReviewList);
                RecentReviewAdapter recentReviewAdapter = new RecentReviewAdapter(mContext, mRecentReviewList, mOnRecentReviewClickListener);
                mRecyclerRecentReview.setAdapter(recentReviewAdapter);
                mRecyclerRecentReview.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false));
                mRecyclerRecentReview.setHasFixedSize(true);
                mRecyclerRecentReview.setVisibility(View.VISIBLE);
                mImageEmptyRecentReview.setVisibility(View.GONE);
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

    private void gotoReviewDetail(int id) {
        Intent intent = new Intent(mContext, ReviewActivity.class);
        intent.putExtra(Popularin.REVIEW_ID, id);
        intent.putExtra(Popularin.IS_SELF, mIsSelf);
        startActivity(intent);
    }

    private void showFilmModal(int id, String title, String year, String poster) {
        FilmModal filmModal = new FilmModal(id, title, year, poster);
        filmModal.show(getSupportFragmentManager(), Popularin.FILM_MODAL);
    }

    private void gotoUserReview(int id) {
        Intent intent = new Intent(mContext, UserReviewActivity.class);
        intent.putExtra(Popularin.USER_ID, id);
        startActivity(intent);
    }

    private void gotoUserFavorite(int id) {
        Intent intent = new Intent(mContext, UserFavoriteActivity.class);
        intent.putExtra(Popularin.USER_ID, id);
        startActivity(intent);
    }

    private void gotoUserWatchlist(int id) {
        Intent intent = new Intent(mContext, UserWatchlistActivity.class);
        intent.putExtra(Popularin.USER_ID, id);
        startActivity(intent);
    }

    private void gotoUserSocial(int id, int viewPagerIndex) {
        Intent intent = new Intent(mContext, SocialActivity.class);
        intent.putExtra(Popularin.USER_ID, id);
        intent.putExtra(Popularin.VIEW_PAGER_INDEX, viewPagerIndex);
        startActivity(intent);
    }

    private void gotoEditProfile() {
        Intent intent = new Intent(mContext, EditProfileActivity.class);
        startActivity(intent);
    }

    private void gotoEmptyAccount() {
        Intent intent = new Intent(mContext, EmptyAccountActivity.class);
        startActivity(intent);
    }

    private enum FollowingState {
        FOLLOWING,
        NOT_FOLLOWING,
        LOADING
    }

    private void setFollowingState(boolean state) {
        mIsFollowing = state;
        if (mIsFollowing) {
            mTotalFollower++;
        } else {
            mTotalFollower--;
        }
        mTextTotalFollower.setText(String.valueOf(mTotalFollower));
    }

    private void setFollowButtonState(boolean state, Enum<FollowingState> followingStateEnum) {
        mButtonFollow.setEnabled(state);
        if (followingStateEnum == FollowingState.FOLLOWING) {
            mButtonFollow.setText(R.string.following);
        } else if (followingStateEnum == FollowingState.NOT_FOLLOWING) {
            mButtonFollow.setText(R.string.follow);
        } else {
            mButtonFollow.setText(R.string.loading);
        }
    }

    private void followUser(int id) {
        FollowUserRequest followUserRequest = new FollowUserRequest(mContext, id);
        followUserRequest.sendRequest(new FollowUserRequest.Callback() {
            @Override
            public void onSuccess() {
                setFollowingState(true);
                setFollowButtonState(true, FollowingState.FOLLOWING);
            }

            @Override
            public void onError(String message) {
                setFollowButtonState(true, FollowingState.NOT_FOLLOWING);
                Snackbar.make(mAnchorLayout, message, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void unfollowUser(int id) {
        UnfollowUserRequest unfollowUserRequest = new UnfollowUserRequest(mContext, id);
        unfollowUserRequest.sendRequest(new UnfollowUserRequest.Callback() {
            @Override
            public void onSuccess() {
                setFollowingState(false);
                setFollowButtonState(true, FollowingState.NOT_FOLLOWING);
            }

            @Override
            public void onError(String message) {
                setFollowButtonState(true, FollowingState.FOLLOWING);
                Snackbar.make(mAnchorLayout, message, Snackbar.LENGTH_LONG).show();
            }
        });
    }
}
