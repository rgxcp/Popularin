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
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.apis.popularin.delete.UnfollowUserRequest;
import xyz.fairportstudios.popularin.apis.popularin.get.UserDetailRequest;
import xyz.fairportstudios.popularin.apis.popularin.post.FollowUserRequest;
import xyz.fairportstudios.popularin.models.UserDetail;
import xyz.fairportstudios.popularin.preferences.Auth;
import xyz.fairportstudios.popularin.statics.Popularin;

public class UserDetailActivity extends AppCompatActivity {
    // Variable untuk fitur swipe refresh
    private Boolean isLoadFirstTime = true;

    // Variable member
    private Boolean isFollower;
    private Boolean isFollowing;
    private Button buttonFollow;
    private ImageView imageProfile;
    private ImageView imageEmptyRecentFavorite;
    private ImageView imageEmptyRecentReview;
    private Integer totalFollower;
    private ProgressBar progressBar;
    private RecyclerView recyclerRecentFavorite;
    private RecyclerView recyclerRecentReview;
    private RelativeLayout anchorLayout;
    private ScrollView scrollView;
    private TextView textFullName;
    private TextView textUsername;
    private TextView textFollowMe;
    private TextView textTotalReview;
    private TextView textTotalFavorite;
    private TextView textTotalWatchlist;
    private TextView textTotalFollower;
    private TextView textTotalFollowing;
    private TextView textMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        // Context
        final Context context = UserDetailActivity.this;

        // Binding
        buttonFollow = findViewById(R.id.button_aud_follow);
        imageProfile = findViewById(R.id.image_aud_profile);
        imageEmptyRecentFavorite = findViewById(R.id.image_aud_empty_recent_favorite);
        imageEmptyRecentReview = findViewById(R.id.image_aud_empty_recent_review);
        progressBar = findViewById(R.id.pbr_aud_layout);
        recyclerRecentFavorite = findViewById(R.id.recycler_aud_recent_favorite);
        recyclerRecentReview = findViewById(R.id.recycler_aud_recent_review);
        anchorLayout = findViewById(R.id.anchor_aud_layout);
        scrollView = findViewById(R.id.scroll_aud_layout);
        textFullName = findViewById(R.id.text_aud_full_name);
        textUsername = findViewById(R.id.text_aud_username);
        textFollowMe = findViewById(R.id.text_aud_follow_me);
        textTotalReview = findViewById(R.id.text_aud_total_review);
        textTotalFavorite = findViewById(R.id.text_aud_total_favorite);
        textTotalWatchlist = findViewById(R.id.text_aud_total_watchlist);
        textTotalFollower = findViewById(R.id.text_aud_total_follower);
        textTotalFollowing = findViewById(R.id.text_aud_total_following);
        textMessage = findViewById(R.id.text_aud_message);
        LinearLayout totalReviewLayout = findViewById(R.id.layout_aud_total_review);
        LinearLayout totalFavoriteLayout = findViewById(R.id.layout_aud_total_favorite);
        LinearLayout totalWatchlistLayout = findViewById(R.id.layout_aud_total_watchlist);
        LinearLayout totalFollowerLayout = findViewById(R.id.layout_aud_total_follower);
        LinearLayout totalFollowingLayout = findViewById(R.id.layout_aud_total_following);
        Toolbar toolbar = findViewById(R.id.toolbar_aud_layout);
        final SwipeRefreshLayout swipeRefresh = findViewById(R.id.swipe_refresh_aud_layout);

        // Extra
        Intent intent = getIntent();
        final int userID = intent.getIntExtra(Popularin.USER_ID, 0);

        // Auth
        final Auth auth = new Auth(context);
        final Boolean isAuth = auth.isAuth();
        final boolean isSelf = userID == auth.getAuthID();
        if (isSelf) {
            buttonFollow.setText(R.string.edit_profile);
        }

        // Mendapatkan informasi pengguna
        getUserDetail(context, userID);

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
                gotoUserReview(context, userID);
            }
        });

        totalFavoriteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoUserFavorite(context, userID);
            }
        });

        totalWatchlistLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoUserWatchlist(context, userID);
            }
        });

        totalFollowerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoUserSocial(context, userID, 1, isSelf);
            }
        });

        totalFollowingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoUserSocial(context, userID, 2, isSelf);
            }
        });

        buttonFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAuth && isSelf) {
                    gotoEditProfile(context);
                } else if (isAuth) {
                    if (!isFollowing) {
                        setFollowButtonState(false, FollowingState.LOADING);
                        followUser(context, userID);
                    } else {
                        setFollowButtonState(false, FollowingState.LOADING);
                        unfollowUser(context, userID);
                    }
                } else {
                    gotoEmptyAccount(context);
                }
            }
        });

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getUserDetail(context, userID);
                swipeRefresh.setRefreshing(false);
            }
        });
    }

    private void getUserDetail(final Context context, Integer userID) {
        // Menghilangkan pesan setiap kali method dijalankan
        textMessage.setVisibility(View.GONE);

        final UserDetailRequest userDetailRequest = new UserDetailRequest(context, userID);
        userDetailRequest.sendRequest(new UserDetailRequest.Callback() {
            @Override
            public void onSuccess(UserDetail userDetail) {
                // Following status
                totalFollower = userDetail.getTotal_follower();
                isFollower = userDetail.getIs_follower();
                isFollowing = userDetail.getIs_following();
                if (isFollower) {
                    textFollowMe.setVisibility(View.VISIBLE);
                }
                if (isFollowing) {
                    buttonFollow.setText(R.string.following);
                }

                // Request gambar
                RequestOptions requestOptions = new RequestOptions()
                        .centerCrop()
                        .placeholder(R.color.colorSurface)
                        .error(R.color.colorSurface);

                textFullName.setText(userDetail.getFull_name());
                textUsername.setText(String.format("@%s", userDetail.getUsername()));
                textTotalReview.setText(String.valueOf(userDetail.getTotal_review()));
                textTotalFavorite.setText(String.valueOf(userDetail.getTotal_favorite()));
                textTotalWatchlist.setText(String.valueOf(userDetail.getTotal_watchlist()));
                textTotalFollower.setText(String.valueOf(totalFollower));
                textTotalFollowing.setText(String.valueOf(userDetail.getTotal_following()));
                Glide.with(context).load(userDetail.getProfile_picture()).apply(requestOptions).into(imageProfile);
                progressBar.setVisibility(View.GONE);
                scrollView.setVisibility(View.VISIBLE);
                isLoadFirstTime = false;
            }

            @Override
            public void onHasFavorite(JSONArray recentFavoriteArray) {
                userDetailRequest.getRecentFavorites(recentFavoriteArray, recyclerRecentFavorite);
                imageEmptyRecentFavorite.setVisibility(View.GONE);
            }

            @Override
            public void onHasReview(JSONArray recentReviewArray) {
                userDetailRequest.getRecentReviews(recentReviewArray, recyclerRecentReview);
                imageEmptyRecentReview.setVisibility(View.GONE);
            }

            @Override
            public void onError(String message) {
                if (isLoadFirstTime) {
                    progressBar.setVisibility(View.GONE);
                    textMessage.setVisibility(View.VISIBLE);
                    textMessage.setText(message);
                } else {
                    Snackbar.make(anchorLayout, message, Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    private void gotoUserReview(Context context, Integer userID) {
        Intent intent = new Intent(context, UserReviewActivity.class);
        intent.putExtra(Popularin.USER_ID, userID);
        startActivity(intent);
    }

    private void gotoUserFavorite(Context context, Integer userID) {
        Intent intent = new Intent(context, UserFavoriteActivity.class);
        intent.putExtra(Popularin.USER_ID, userID);
        startActivity(intent);
    }

    private void gotoUserWatchlist(Context context, Integer userID) {
        Intent intent = new Intent(context, UserWatchlistActivity.class);
        intent.putExtra(Popularin.USER_ID, userID);
        startActivity(intent);
    }

    private void gotoUserSocial(Context context, Integer userID, Integer viewPagerIndex, Boolean isSelf) {
        Intent intent = new Intent(context, SocialActivity.class);
        intent.putExtra(Popularin.USER_ID, userID);
        intent.putExtra(Popularin.VIEW_PAGER_INDEX, viewPagerIndex);
        intent.putExtra(Popularin.IS_SELF, isSelf);
        startActivity(intent);
    }

    private void gotoEditProfile(Context context) {
        Intent intent = new Intent(context, EditProfileActivity.class);
        startActivity(intent);
    }

    private void gotoEmptyAccount(Context context) {
        Intent intent = new Intent(context, EmptyAccountActivity.class);
        startActivity(intent);
    }

    private enum FollowingState {
        FOLLOWING,
        NOT_FOLLOWING,
        LOADING
    }

    private void setFollowingState(Boolean state) {
        isFollowing = state;
        if (state) {
            totalFollower++;
        } else {
            totalFollower--;
        }
        textTotalFollower.setText(String.valueOf(totalFollower));
    }

    private void setFollowButtonState(Boolean state, Enum<FollowingState> followingStateEnum) {
        buttonFollow.setEnabled(state);
        if (followingStateEnum == FollowingState.FOLLOWING) {
            buttonFollow.setText(R.string.following);
        } else if (followingStateEnum == FollowingState.NOT_FOLLOWING) {
            buttonFollow.setText(R.string.follow);
        } else {
            buttonFollow.setText(R.string.loading);
        }
    }

    private void followUser(Context context, Integer userID) {
        FollowUserRequest followUserRequest = new FollowUserRequest(context, userID);
        followUserRequest.sendRequest(new FollowUserRequest.Callback() {
            @Override
            public void onSuccess() {
                setFollowingState(true);
                setFollowButtonState(true, FollowingState.FOLLOWING);
            }

            @Override
            public void onError(String message) {
                setFollowButtonState(true, FollowingState.NOT_FOLLOWING);
                Snackbar.make(anchorLayout, message, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void unfollowUser(Context context, Integer userID) {
        UnfollowUserRequest unfollowUserRequest = new UnfollowUserRequest(context, userID);
        unfollowUserRequest.sendRequest(new UnfollowUserRequest.Callback() {
            @Override
            public void onSuccess() {
                setFollowingState(false);
                setFollowButtonState(true, FollowingState.NOT_FOLLOWING);
            }

            @Override
            public void onError(String message) {
                setFollowButtonState(true, FollowingState.FOLLOWING);
                Snackbar.make(anchorLayout, message, Snackbar.LENGTH_LONG).show();
            }
        });
    }
}
