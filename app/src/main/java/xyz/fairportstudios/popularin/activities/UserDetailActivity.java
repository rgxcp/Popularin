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

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.apis.popularin.delete.UnfollowUserRequest;
import xyz.fairportstudios.popularin.apis.popularin.get.UserDetailRequest;
import xyz.fairportstudios.popularin.apis.popularin.post.FollowUserRequest;
import xyz.fairportstudios.popularin.models.RecentFavorite;
import xyz.fairportstudios.popularin.models.RecentReview;
import xyz.fairportstudios.popularin.models.UserDetail;
import xyz.fairportstudios.popularin.preferences.Auth;
import xyz.fairportstudios.popularin.services.Popularin;

public class UserDetailActivity extends AppCompatActivity {
    private Boolean isFollowing;
    private Button buttonFollow;
    private Context context;
    private ImageView imageProfile;
    private ImageView imageEmptyRecentFavorite;
    private ImageView imageEmptyRecentReview;
    private Integer currentFollower;
    private Integer totalFollower;
    private LinearLayout notFoundLayout;
    private ProgressBar progressBar;
    private RecyclerView recyclerRecentFavorite;
    private RecyclerView recyclerRecentReview;
    private RelativeLayout anchorLayout;
    private ScrollView scrollView;
    private String userID;
    private TextView textFullName;
    private TextView textUsername;
    private TextView textTotalReview;
    private TextView textTotalFavorite;
    private TextView textTotalWatchlist;
    private TextView textTotalFollower;
    private TextView textTotalFollowing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        // Binding
        context = UserDetailActivity.this;
        buttonFollow = findViewById(R.id.button_aud_follow);
        imageProfile = findViewById(R.id.image_aud_profile);
        imageEmptyRecentFavorite = findViewById(R.id.image_aud_empty_recent_favorite);
        imageEmptyRecentReview = findViewById(R.id.image_aud_empty_recent_review);
        notFoundLayout = findViewById(R.id.layout_aud_not_found);
        progressBar = findViewById(R.id.pbr_aud_layout);
        recyclerRecentFavorite = findViewById(R.id.recycler_aud_recent_favorite);
        recyclerRecentReview = findViewById(R.id.recycler_aud_recent_review);
        anchorLayout = findViewById(R.id.layout_aud_anchor);
        scrollView = findViewById(R.id.scroll_aud_layout);
        textFullName = findViewById(R.id.text_aud_full_name);
        textUsername = findViewById(R.id.text_aud_username);
        textTotalReview = findViewById(R.id.text_aud_total_review);
        textTotalFavorite = findViewById(R.id.text_aud_total_favorite);
        textTotalWatchlist = findViewById(R.id.text_aud_total_watchlist);
        textTotalFollower = findViewById(R.id.text_aud_total_follower);
        textTotalFollowing = findViewById(R.id.text_aud_total_following);
        LinearLayout totalReviewLayout = findViewById(R.id.layout_aud_total_review);
        LinearLayout totalFavoriteLayout = findViewById(R.id.layout_aud_total_favorite);
        LinearLayout totalWatchlistLayout = findViewById(R.id.layout_aud_total_watchlist);
        LinearLayout totalFollowerLayout = findViewById(R.id.layout_aud_total_follower);
        LinearLayout totalFollowingLayout = findViewById(R.id.layout_aud_total_following);
        Toolbar toolbar = findViewById(R.id.toolbar_aud_layout);

        // Extra
        Intent intent = getIntent();
        userID = intent.getStringExtra(Popularin.USER_ID);

        // Auth
        Auth auth = new Auth(context);
        final String authID = auth.getAuthID();
        final boolean isAuth = auth.isAuth();
        final boolean isSelf = Objects.requireNonNull(userID).equals(authID);
        if (isSelf) {
            buttonFollow.setText(R.string.edit_profile);
        }

        // Request
        getUserDetail();

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
                getUserReview();
            }
        });

        totalFavoriteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUserFavorite();
            }
        });

        totalWatchlistLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUserWatchlist();
            }
        });

        totalFollowerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUserFollower();
            }
        });

        totalFollowingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUserFollowing();
            }
        });

        buttonFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAuth && isSelf) {
                    gotoEditProfile();
                } else if (isAuth) {
                    if (!isFollowing) {
                        followUser();
                    } else {
                        unfollowUser();
                    }
                } else {
                    gotoEmptyAccount();
                }
            }
        });
    }

    private void getUserDetail() {
        List<RecentFavorite> recentFavoriteList = new ArrayList<>();
        List<RecentReview> recentReviewList = new ArrayList<>();

        UserDetailRequest userDetailRequest = new UserDetailRequest(
                context,
                userID,
                recentFavoriteList,
                recentReviewList,
                recyclerRecentFavorite,
                recyclerRecentReview
        );

        userDetailRequest.sendRequest(new UserDetailRequest.APICallback() {
            @Override
            public void onSuccess(UserDetail userDetail) {
                isFollowing = userDetail.getIs_following();
                textTotalReview.setText(String.valueOf(userDetail.getTotal_review()));
                textTotalFavorite.setText(String.valueOf(userDetail.getTotal_favorite()));
                textTotalWatchlist.setText(String.valueOf(userDetail.getTotal_watchlist()));
                textTotalFollower.setText(String.valueOf(userDetail.getTotal_follower()));
                textTotalFollowing.setText(String.valueOf(userDetail.getTotal_following()));
                textFullName.setText(userDetail.getFull_name());
                textUsername.setText(String.format("@%s", userDetail.getUsername()));
                Glide.with(context).load(userDetail.getProfile_picture()).into(imageProfile);
                progressBar.setVisibility(View.GONE);
                scrollView.setVisibility(View.VISIBLE);

                // Following
                currentFollower = userDetail.getTotal_follower();
                if (isFollowing) {
                    buttonFollow.setText(R.string.following);
                }
            }

            @Override
            public void onEmptyFavorite() {
                imageEmptyRecentFavorite.setVisibility(View.VISIBLE);
            }

            @Override
            public void onEmptyReview() {
                imageEmptyRecentReview.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError() {
                progressBar.setVisibility(View.GONE);
                notFoundLayout.setVisibility(View.VISIBLE);
                Snackbar.make(anchorLayout, R.string.network_error, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void getUserReview() {
        Intent intent = new Intent(context, UserReviewActivity.class);
        intent.putExtra(Popularin.USER_ID, userID);
        startActivity(intent);
    }

    private void getUserFavorite() {
        Intent intent = new Intent(context, UserFavoriteActivity.class);
        intent.putExtra(Popularin.USER_ID, userID);
        startActivity(intent);
    }

    private void getUserWatchlist() {
        Intent intent = new Intent(context, UserWatchlistActivity.class);
        intent.putExtra(Popularin.USER_ID, userID);
        startActivity(intent);
    }

    private void getUserFollower() {
        Intent intent = new Intent(context, SocialActivity.class);
        intent.putExtra(Popularin.USER_ID, userID);
        startActivity(intent);
    }

    private void getUserFollowing() {
        Intent intent = new Intent(context, SocialActivity.class);
        intent.putExtra(Popularin.USER_ID, userID);
        startActivity(intent);
    }

    private void followUser() {
        buttonFollow.setEnabled(false);
        FollowUserRequest followUserRequest = new FollowUserRequest(context, userID);
        followUserRequest.sendRequest(new FollowUserRequest.APICallback() {
            @Override
            public void onSuccess() {
                isFollowing = true;
                totalFollower = currentFollower + 1;
                currentFollower++;
                buttonFollow.setEnabled(true);
                buttonFollow.setText(R.string.following);
                textTotalFollower.setText(String.valueOf(totalFollower));
                Snackbar.make(anchorLayout, R.string.user_followed, Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onError() {
                buttonFollow.setEnabled(true);
                Snackbar.make(anchorLayout, R.string.failed_follow_user, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void unfollowUser() {
        buttonFollow.setEnabled(false);
        UnfollowUserRequest unfollowUserRequest = new UnfollowUserRequest(context, userID);
        unfollowUserRequest.sendRequest(new UnfollowUserRequest.APICallback() {
            @Override
            public void onSuccess() {
                isFollowing = false;
                totalFollower = currentFollower - 1;
                currentFollower--;
                buttonFollow.setEnabled(true);
                buttonFollow.setText(R.string.follow);
                textTotalFollower.setText(String.valueOf(totalFollower));
                Snackbar.make(anchorLayout, R.string.user_unfollowed, Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onError() {
                buttonFollow.setEnabled(true);
                Snackbar.make(anchorLayout, R.string.failed_unfollow_user, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void gotoEditProfile() {
        Intent intent = new Intent(context, EditProfileActivity.class);
        startActivity(intent);
    }

    private void gotoEmptyAccount() {
        Intent intent = new Intent(context, EmptyAccountActivity.class);
        startActivity(intent);
    }
}
