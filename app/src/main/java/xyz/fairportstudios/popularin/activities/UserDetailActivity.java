package xyz.fairportstudios.popularin.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.apis.popularin.UserDetailRequest;
import xyz.fairportstudios.popularin.models.LatestFavorite;
import xyz.fairportstudios.popularin.models.LatestReview;

public class UserDetailActivity extends AppCompatActivity {
    private Boolean isFollowing;
    private Boolean isFollower;
    private Button buttonFollow;
    private Context context;
    private ImageView profilePicture;
    private ProgressBar progressBar;
    private RecyclerView recyclerViewLatestFavorite;
    private RecyclerView recyclerViewLatestReview;
    private ScrollView scrollView;
    private TextView followerStatus;
    private TextView fullName;
    private TextView username;
    private TextView totalReview;
    private TextView totalFollower;
    private TextView totalFollowing;
    private TextView totalFavorite;
    private TextView totalWatchlist;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        // Binding
        buttonFollow = findViewById(R.id.button_aud_follow);
        context = UserDetailActivity.this;
        profilePicture = findViewById(R.id.image_aud_profile);
        progressBar = findViewById(R.id.progress_bar_aud_layout);
        recyclerViewLatestFavorite = findViewById(R.id.recycler_view_top_favorite_aud_layout);
        recyclerViewLatestReview = findViewById(R.id.recycler_view_latest_review_aud_layout);
        scrollView = findViewById(R.id.scroll_view_aud_layout);
        followerStatus = findViewById(R.id.text_aud_follower);
        fullName = findViewById(R.id.text_aud_full_name);
        username = findViewById(R.id.text_aud_username);
        totalReview = findViewById(R.id.text_aud_total_review);
        totalFollower = findViewById(R.id.text_aud_total_follower);
        totalFollowing = findViewById(R.id.text_aud_total_following);
        totalFavorite = findViewById(R.id.text_aud_total_favorite);
        totalWatchlist = findViewById(R.id.text_aud_total_watchlist);
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
        emptyLatestFavorite = findViewById(R.id.text_aud_empty_top_favorite);
        emptyLatestReview = findViewById(R.id.text_aud_empty_latest_review);

        // Set-up list
        List<LatestFavorite> latestFavoriteList = new ArrayList<>();
        List<LatestReview> latestReviewList = new ArrayList<>();

        // Bundle
        Bundle bundle = getIntent().getExtras();
        String userID = Objects.requireNonNull(bundle).getString("USER_ID");

        // Mendapatkan data
        UserDetailRequest userDetailRequest = new UserDetailRequest(
                context,
                userID,
                latestFavoriteList,
                latestReviewList,
                recyclerViewLatestFavorite,
                recyclerViewLatestReview
        );

        userDetailRequest.getUserDetail(new UserDetailRequest.JSONCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    JSONObject jsonObjectResult = response.getJSONObject("result");
                    JSONObject jsonObjectUser = jsonObjectResult.getJSONObject("user");
                    JSONObject jsonObjectMetadata = jsonObjectResult.getJSONObject("metadata");

                    fullName.setText(jsonObjectUser.getString("full_name"));
                    username.setText(String.format("@%s", jsonObjectUser.getString("username")));
                    totalReview.setText(String.valueOf(jsonObjectMetadata.getInt("reviews")));
                    totalFollower.setText(String.valueOf(jsonObjectMetadata.getInt("followers")));
                    totalFollowing.setText(String.valueOf(jsonObjectMetadata.getInt("followings")));
                    totalFavorite.setText(String.valueOf(jsonObjectMetadata.getInt("favorites")));
                    totalWatchlist.setText(String.valueOf(jsonObjectMetadata.getInt("watchlists")));
                    rate05.setText(String.valueOf(jsonObjectMetadata.getInt("rate_0.5")));
                    rate10.setText(String.valueOf(jsonObjectMetadata.getInt("rate_1.0")));
                    rate15.setText(String.valueOf(jsonObjectMetadata.getInt("rate_1.5")));
                    rate20.setText(String.valueOf(jsonObjectMetadata.getInt("rate_2.0")));
                    rate25.setText(String.valueOf(jsonObjectMetadata.getInt("rate_2.5")));
                    rate30.setText(String.valueOf(jsonObjectMetadata.getInt("rate_3.0")));
                    rate35.setText(String.valueOf(jsonObjectMetadata.getInt("rate_3.5")));
                    rate40.setText(String.valueOf(jsonObjectMetadata.getInt("rate_4.0")));
                    rate45.setText(String.valueOf(jsonObjectMetadata.getInt("rate_4.5")));
                    rate50.setText(String.valueOf(jsonObjectMetadata.getInt("rate_5.0")));

                    // Following follower status
                    isFollowing = jsonObjectMetadata.getBoolean("is_following");
                    isFollower = jsonObjectMetadata.getBoolean("is_follower");

                    if (isFollowing) {
                        buttonFollow.setText("MENGIKUTI");
                    }

                    if (isFollower) {
                        followerStatus.setVisibility(View.VISIBLE);
                    }

                    RequestOptions requestOptions = new RequestOptions()
                            .centerCrop()
                            .placeholder(R.color.colorPrimary)
                            .error(R.color.colorPrimary);

                    Glide.with(context)
                            .load(jsonObjectUser.getString("profile_picture"))
                            .apply(requestOptions)
                            .into(profilePicture);
                } catch (JSONException error) {
                    error.printStackTrace();
                } finally {
                    progressBar.setVisibility(View.GONE);
                    scrollView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onEmptyFavorite(Integer favorite) {
                if (favorite == 0) {
                    emptyLatestFavorite.setVisibility(View.VISIBLE);
                    recyclerViewLatestFavorite.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onEmptyReview(Integer review) {
                if (review == 0) {
                    emptyLatestReview.setVisibility(View.VISIBLE);
                    recyclerViewLatestReview.setVisibility(View.INVISIBLE);
                }
            }
        });

        // Activity
        buttonFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFollowing) {
                    // Unfollow
                } else {
                    // Follow
                }
            }
        });
    }
}
