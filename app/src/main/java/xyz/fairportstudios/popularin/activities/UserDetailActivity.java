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

import org.json.JSONArray;

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
import xyz.fairportstudios.popularin.statics.Popularin;

public class UserDetailActivity extends AppCompatActivity {
    private Boolean isAuth;
    private Boolean isSelf;
    private Boolean isFollowing;
    private Boolean isFollower;
    private Button buttonFollow;
    private Context context;
    private ImageView imageProfile;
    private ImageView imageEmptyRecentFavorite;
    private ImageView imageEmptyRecentReview;
    private Integer currentFollower;
    private Integer totalFollower;
    private ProgressBar progressBar;
    private RecyclerView recyclerRecentFavorite;
    private RecyclerView recyclerRecentReview;
    private RelativeLayout anchorLayout;
    private ScrollView scrollView;
    private String userID;
    private TextView textFullName;
    private TextView textUsername;
    private TextView textFollower;
    private TextView textTotalReview;
    private TextView textTotalFavorite;
    private TextView textTotalWatchlist;
    private TextView textTotalFollower;
    private TextView textTotalFollowing;
    private TextView textNetworkError;

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
        progressBar = findViewById(R.id.pbr_aud_layout);
        recyclerRecentFavorite = findViewById(R.id.recycler_aud_recent_favorite);
        recyclerRecentReview = findViewById(R.id.recycler_aud_recent_review);
        anchorLayout = findViewById(R.id.anchor_aud_layout);
        scrollView = findViewById(R.id.scroll_aud_layout);
        textFullName = findViewById(R.id.text_aud_full_name);
        textUsername = findViewById(R.id.text_aud_username);
        textFollower = findViewById(R.id.text_aud_follower);
        textTotalReview = findViewById(R.id.text_aud_total_review);
        textTotalFavorite = findViewById(R.id.text_aud_total_favorite);
        textTotalWatchlist = findViewById(R.id.text_aud_total_watchlist);
        textTotalFollower = findViewById(R.id.text_aud_total_follower);
        textTotalFollowing = findViewById(R.id.text_aud_total_following);
        textNetworkError = findViewById(R.id.text_aud_network_error);
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
        isAuth = auth.isAuth();
        isSelf = Objects.requireNonNull(userID).equals(auth.getAuthID());
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
                gotoUserReview();
            }
        });

        totalFavoriteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoUserFavorite();
            }
        });

        totalWatchlistLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoUserWatchlist();
            }
        });

        totalFollowerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoSocial();
            }
        });

        totalFollowingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoSocial();
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
        final UserDetailRequest userDetailRequest = new UserDetailRequest(context, userID);
        userDetailRequest.sendRequest(new UserDetailRequest.APICallback() {
            @Override
            public void onSuccess(UserDetail userDetail) {
                // Social status
                isFollowing = userDetail.getIs_following();
                isFollower = userDetail.getIs_follower();
                currentFollower = userDetail.getTotal_follower();
                if (isFollowing) {
                    buttonFollow.setText(R.string.following);
                }
                if (isFollower) {
                    textFollower.setVisibility(View.VISIBLE);
                }

                textFullName.setText(userDetail.getFull_name());
                textUsername.setText(String.format("@%s", userDetail.getUsername()));
                textTotalReview.setText(String.valueOf(userDetail.getTotal_review()));
                textTotalFavorite.setText(String.valueOf(userDetail.getTotal_favorite()));
                textTotalWatchlist.setText(String.valueOf(userDetail.getTotal_watchlist()));
                textTotalFollower.setText(String.valueOf(currentFollower));
                textTotalFollowing.setText(String.valueOf(userDetail.getTotal_following()));
                Glide.with(context).load(userDetail.getProfile_picture()).into(imageProfile);
                progressBar.setVisibility(View.GONE);
                scrollView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onHasFavorite(JSONArray recentFavorites) {
                List<RecentFavorite> recentFavoriteList = new ArrayList<>();
                userDetailRequest.getRecentFavorites(recentFavorites, recentFavoriteList, recyclerRecentFavorite);
                imageEmptyRecentFavorite.setVisibility(View.GONE);
            }

            @Override
            public void onHasReview(JSONArray recentReviews) {
                List<RecentReview> recentReviewList = new ArrayList<>();
                userDetailRequest.getRecentReviews(recentReviews, recentReviewList, recyclerRecentReview);
                imageEmptyRecentReview.setVisibility(View.GONE);
            }

            @Override
            public void onError() {
                progressBar.setVisibility(View.GONE);
                textNetworkError.setVisibility(View.VISIBLE);
                Snackbar.make(anchorLayout, R.string.network_error, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void gotoUserReview() {
        Intent intent = new Intent(context, UserReviewActivity.class);
        intent.putExtra(Popularin.USER_ID, userID);
        startActivity(intent);
    }

    private void gotoUserFavorite() {
        Intent intent = new Intent(context, UserFavoriteActivity.class);
        intent.putExtra(Popularin.USER_ID, userID);
        startActivity(intent);
    }

    private void gotoUserWatchlist() {
        Intent intent = new Intent(context, UserWatchlistActivity.class);
        intent.putExtra(Popularin.USER_ID, userID);
        startActivity(intent);
    }

    private void gotoSocial() {
        Intent intent = new Intent(context, SocialActivity.class);
        intent.putExtra(Popularin.USER_ID, userID);
        intent.putExtra(Popularin.IS_SELF, isSelf);
        startActivity(intent);
    }

    private void gotoEditProfile() {
        Intent intent = new Intent(context, EditProfileActivity.class);
        startActivity(intent);
    }

    private void gotoEmptyAccount() {
        Intent intent = new Intent(context, EmptyAccountActivity.class);
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
}
