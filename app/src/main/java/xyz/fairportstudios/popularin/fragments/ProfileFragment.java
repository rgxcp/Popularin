package xyz.fairportstudios.popularin.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.activities.EditProfileActivity;
import xyz.fairportstudios.popularin.activities.FavoriteActivity;
import xyz.fairportstudios.popularin.activities.UserReviewActivity;
import xyz.fairportstudios.popularin.apis.popularin.get.UserDetail;
import xyz.fairportstudios.popularin.models.LatestFavorite;
import xyz.fairportstudios.popularin.models.LatestReview;
import xyz.fairportstudios.popularin.preferences.Auth;

public class ProfileFragment extends Fragment {
    private Context context;
    private ImageView profilePicture;
    private ProgressBar progressBar;
    private RecyclerView recyclerViewLatestFavorite;
    private RecyclerView recyclerViewLatestReview;
    private ScrollView scrollView;
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Binding
        context = getActivity();
        profilePicture = view.findViewById(R.id.image_fp_profile);
        progressBar = view.findViewById(R.id.progress_bar_fp_layout);
        recyclerViewLatestFavorite = view.findViewById(R.id.recycler_view_top_favorite_fp_layout);
        recyclerViewLatestReview = view.findViewById(R.id.recycler_view_latest_review_fp_layout);
        scrollView = view.findViewById(R.id.scroll_view_fp_layout);
        fullName = view.findViewById(R.id.text_fp_full_name);
        username = view.findViewById(R.id.text_fp_username);
        totalReview = view.findViewById(R.id.text_fp_total_review);
        totalFollower = view.findViewById(R.id.text_fp_total_follower);
        totalFollowing = view.findViewById(R.id.text_fp_total_following);
        totalFavorite = view.findViewById(R.id.text_fp_total_favorite);
        totalWatchlist = view.findViewById(R.id.text_fp_total_watchlist);
        rate05 = view.findViewById(R.id.text_fp_rate_05);
        rate10 = view.findViewById(R.id.text_fp_rate_10);
        rate15 = view.findViewById(R.id.text_fp_rate_15);
        rate20 = view.findViewById(R.id.text_fp_rate_20);
        rate25 = view.findViewById(R.id.text_fp_rate_25);
        rate30 = view.findViewById(R.id.text_fp_rate_30);
        rate35 = view.findViewById(R.id.text_fp_rate_35);
        rate40 = view.findViewById(R.id.text_fp_rate_40);
        rate45 = view.findViewById(R.id.text_fp_rate_45);
        rate50 = view.findViewById(R.id.text_fp_rate_50);
        emptyLatestFavorite = view.findViewById(R.id.text_fp_empty_top_favorite);
        emptyLatestReview = view.findViewById(R.id.text_fp_empty_latest_review);
        Button buttonEditProfile = view.findViewById(R.id.button_fp_edit_profile);

        // Mengecek apakah sign in
        boolean isAuth = new Auth(context).isAuth();

        if (!isAuth) {
            Objects.requireNonNull(getFragmentManager())
                    .beginTransaction()
                    .replace(R.id.fragment_am_container, new EmptyUserFragment())
                    .commit();
        }

        // Set-up list
        List<LatestFavorite> latestFavoriteList = new ArrayList<>();
        List<LatestReview> latestReviewList = new ArrayList<>();

        // Mendapatkan data
        UserDetail userDetail = new UserDetail(
                new Auth(context).getAuthID(),
                context,
                latestFavoriteList,
                latestReviewList,
                recyclerViewLatestFavorite,
                recyclerViewLatestReview
        );

        userDetail.sendRequest(new UserDetail.JSONCallback() {
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
        totalReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoReviewList = new Intent(context, UserReviewActivity.class);
                gotoReviewList.putExtra("USER_ID", new Auth(context).getAuthID());
                startActivity(gotoReviewList);
            }
        });

        totalFollower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        totalFollowing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        totalFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoFavoriteList = new Intent(context, FavoriteActivity.class);
                gotoFavoriteList.putExtra("USER_ID", new Auth(context).getAuthID());
                startActivity(gotoFavoriteList);
            }
        });

        totalWatchlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        buttonEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoEditProfile = new Intent(context, EditProfileActivity.class);
                startActivity(gotoEditProfile);
            }
        });

        return view;
    }
}
