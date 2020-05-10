package xyz.fairportstudios.popularin.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.apis.popularin.delete.UnfollowUserRequest;
import xyz.fairportstudios.popularin.apis.popularin.get.UserDetailRequest;
import xyz.fairportstudios.popularin.apis.popularin.post.FollowUserRequest;
import xyz.fairportstudios.popularin.models.LatestFavorite;
import xyz.fairportstudios.popularin.models.LatestReview;
import xyz.fairportstudios.popularin.models.UserDetail;
import xyz.fairportstudios.popularin.preferences.Auth;

public class UserDetailActivity extends AppCompatActivity {
    private Boolean isAuth;
    private Boolean isSelf;
    private Boolean isFollowing;
    private Boolean isFollower;
    private Button buttonFollow;
    private Context context;
    private ImageView profilePicture;
    private Integer currentFollowers;
    private Integer followers;
    private ProgressBar progressBar;
    private RelativeLayout layout;
    private ScrollView scrollView;
    private TextView fullName;
    private TextView username;
    private TextView totalReview;
    private TextView totalFollower;
    private TextView totalFollowing;
    private TextView totalFavorite;
    private TextView totalWatchlist;
    private TextView followMe;
    private TextView rate05;
    private TextView rate10;
    private TextView rate15;
    private TextView rate20;
    private TextView rate25;
    private TextView rate30;
    private TextView rate35;
    private TextView rate40;
    private TextView rate45;
    private TextView rate50;
    private TextView emptyLatestFavorite;
    private TextView emptyLatestReview;
    private TextView userDeleted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        // Binding
        context = UserDetailActivity.this;
        buttonFollow = findViewById(R.id.button_aud_follow);
        profilePicture = findViewById(R.id.image_aud_profile);
        progressBar = findViewById(R.id.pbr_aud_layout);
        layout = findViewById(R.id.layout_aud_anchor);
        scrollView = findViewById(R.id.scroll_aud_layout);
        fullName = findViewById(R.id.text_aud_full_name);
        username = findViewById(R.id.text_aud_username);
        totalReview = findViewById(R.id.text_aud_total_review);
        totalFollower = findViewById(R.id.text_aud_total_follower);
        totalFollowing = findViewById(R.id.text_aud_total_following);
        totalFavorite = findViewById(R.id.text_aud_total_favorite);
        totalWatchlist = findViewById(R.id.text_aud_total_watchlist);
        followMe = findViewById(R.id.text_aud_follow_me);
        rate05 = findViewById(R.id.text_aud_rate_05);
        rate10 = findViewById(R.id.text_aud_rate_10);
        rate15 = findViewById(R.id.text_aud_rate_15);
        rate20 = findViewById(R.id.text_aud_rate_20);
        rate25 = findViewById(R.id.text_aud_rate_25);
        rate30 = findViewById(R.id.text_aud_rate_30);
        rate35 = findViewById(R.id.text_aud_rate_35);
        rate40 = findViewById(R.id.text_aud_rate_40);
        rate45 = findViewById(R.id.text_aud_rate_45);
        rate50 = findViewById(R.id.text_aud_rate_50);
        emptyLatestFavorite = findViewById(R.id.text_empty_latest_favorite_aud_layout);
        emptyLatestReview = findViewById(R.id.text_empty_latest_review_aud_layout);
        userDeleted = findViewById(R.id.text_aud_empty);
        RecyclerView recyclerViewLatestFavorite = findViewById(R.id.recycler_latest_favorite_aud_layout);
        RecyclerView recyclerViewLatestReview = findViewById(R.id.recycler_latest_review_aud_layout);
        Toolbar toolbar = findViewById(R.id.toolbar_aud_layout);

        // Bundle
        Bundle bundle = getIntent().getExtras();
        final String userID = Objects.requireNonNull(bundle).getString("USER_ID");

        // Auth
        Auth auth = new Auth(context);
        isAuth = auth.isAuth();
        isSelf = Objects.requireNonNull(userID).equals(auth.getAuthID());

        // List
        List<LatestFavorite> latestFavoriteList = new ArrayList<>();
        List<LatestReview> latestReviewList = new ArrayList<>();

        // GET
        UserDetailRequest userDetailRequest = new UserDetailRequest(context, latestFavoriteList, latestReviewList, recyclerViewLatestFavorite, recyclerViewLatestReview, userID);
        userDetailRequest.sendRequest(new UserDetailRequest.APICallback() {
            @Override
            public void onSuccess(UserDetail userDetail) {
                fullName.setText(userDetail.getFullName());
                rate05.setText(userDetail.getRate05());
                rate10.setText(userDetail.getRate10());
                rate15.setText(userDetail.getRate15());
                rate20.setText(userDetail.getRate20());
                rate25.setText(userDetail.getRate25());
                rate30.setText(userDetail.getRate30());
                rate35.setText(userDetail.getRate35());
                rate40.setText(userDetail.getRate40());
                rate45.setText(userDetail.getRate45());
                rate50.setText(userDetail.getRate50());
                totalFavorite.setText(userDetail.getTotalFavorite());
                totalFollower.setText(userDetail.getTotalFollower());
                totalFollowing.setText(userDetail.getTotalFollowing());
                totalReview.setText(userDetail.getTotalReview());
                totalWatchlist.setText(userDetail.getTotalWatchlist());
                username.setText(userDetail.getUsername());
                Glide.with(context).load(userDetail.getProfilePicture()).apply(new RequestOptions().centerCrop().placeholder(R.color.colorPrimary).error(R.color.colorPrimary)).into(profilePicture);

                // Follow Status
                followers = Integer.valueOf(userDetail.getTotalFollower());
                isFollowing = userDetail.getFollowingStatus();
                isFollower = userDetail.getFollowerStatus();

                if (isFollowing) {
                    buttonFollow.setText(R.string.followed);
                }

                if (isFollower) {
                    followMe.setVisibility(View.VISIBLE);
                }

                progressBar.setVisibility(View.GONE);
                scrollView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onEmptyFavorite() {
                emptyLatestFavorite.setVisibility(View.VISIBLE);
            }

            @Override
            public void onEmptyReview() {
                emptyLatestReview.setVisibility(View.VISIBLE);
            }

            @Override
            public void onDeleted() {
                progressBar.setVisibility(View.GONE);
                userDeleted.setVisibility(View.VISIBLE);
                Snackbar.make(layout, R.string.user_deleted, Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onError() {
                progressBar.setVisibility(View.GONE);
                userDeleted.setVisibility(View.VISIBLE);
                Snackbar.make(layout, R.string.get_error, Snackbar.LENGTH_LONG).show();
            }
        });

        // Activity
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        totalReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoReviewList = new Intent(context, UserReviewListActivity.class);
                gotoReviewList.putExtra("USER_ID", userID);
                startActivity(gotoReviewList);
            }
        });

        totalFollower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoSocial = new Intent(context, SocialActivity.class);
                gotoSocial.putExtra("USER_ID", userID);
                if (isSelf) {
                    gotoSocial.putExtra("IS_SELF", true);
                } else {
                    gotoSocial.putExtra("IS_SELF", false);
                }
                startActivity(gotoSocial);
            }
        });

        totalFollowing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoSocial = new Intent(context, SocialActivity.class);
                gotoSocial.putExtra("USER_ID", userID);
                if (isSelf) {
                    gotoSocial.putExtra("IS_SELF", true);
                } else {
                    gotoSocial.putExtra("IS_SELF", false);
                }
                startActivity(gotoSocial);
            }
        });

        totalFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoFavoriteList = new Intent(context, FavoriteListActivity.class);
                gotoFavoriteList.putExtra("USER_ID", userID);
                startActivity(gotoFavoriteList);
            }
        });

        totalWatchlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoWatchList = new Intent(context, WatchListActivity.class);
                gotoWatchList.putExtra("USER_ID", userID);
                startActivity(gotoWatchList);
            }
        });

        buttonFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAuth) {
                    if (!isFollowing) {
                        // POST
                        FollowUserRequest followUserRequest = new FollowUserRequest(context, userID);
                        followUserRequest.sendRequest(new FollowUserRequest.APICallback() {
                            @Override
                            public void onSuccess() {
                                buttonFollow.setText(R.string.followed);
                                currentFollowers = followers + 1;
                                totalFollower.setText(String.valueOf(currentFollowers));
                                isFollowing = true;
                                followers++;
                                Snackbar.make(layout, R.string.user_followed, Snackbar.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError() {
                                Snackbar.make(layout, R.string.follow_error, Snackbar.LENGTH_LONG).show();
                            }
                        });
                    } else {
                        // DELETE
                        UnfollowUserRequest unfollowUserRequest = new UnfollowUserRequest(context, userID);
                        unfollowUserRequest.sendRequest(new UnfollowUserRequest.APICallback() {
                            @Override
                            public void onSuccess() {
                                buttonFollow.setText(R.string.follow);
                                currentFollowers = followers - 1;
                                totalFollower.setText(String.valueOf(currentFollowers));
                                isFollowing = false;
                                followers--;
                                Snackbar.make(layout, R.string.user_unfollowed, Snackbar.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError() {
                                Snackbar.make(layout, R.string.unfollow_error, Snackbar.LENGTH_LONG).show();
                            }
                        });
                    }
                } else {
                    Intent gotoEmptyUser = new Intent(context, EmptyUserActivity.class);
                    startActivity(gotoEmptyUser);
                }
            }
        });
    }
}
