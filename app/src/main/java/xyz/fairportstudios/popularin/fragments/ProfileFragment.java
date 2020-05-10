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
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.activities.FavoriteListActivity;
import xyz.fairportstudios.popularin.activities.UserReviewListActivity;
import xyz.fairportstudios.popularin.activities.WatchListActivity;
import xyz.fairportstudios.popularin.apis.popularin.get.ProfileDetailRequest;
import xyz.fairportstudios.popularin.models.LatestFavorite;
import xyz.fairportstudios.popularin.models.LatestReview;
import xyz.fairportstudios.popularin.models.ProfileDetail;
import xyz.fairportstudios.popularin.preferences.Auth;

public class ProfileFragment extends Fragment {
    private Auth auth;
    private Context context;
    private ImageView profilePicture;
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Binding
        context = getActivity();
        profilePicture = view.findViewById(R.id.image_fp_profile);
        progressBar = view.findViewById(R.id.pbr_fp_layout);
        layout = view.findViewById(R.id.layout_fp_anchor);
        scrollView = view.findViewById(R.id.scroll_fp_layout);
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
        emptyLatestFavorite = view.findViewById(R.id.text_empty_latest_favorite_fp_layout);
        emptyLatestReview = view.findViewById(R.id.text_empty_latest_review_fp_layout);
        userDeleted = view.findViewById(R.id.text_fp_empty);
        Button buttonEditProfile = view.findViewById(R.id.button_fp_edit_profile);
        RecyclerView recyclerViewLatestFavorite = view.findViewById(R.id.recycler_latest_favorite_fp_layout);
        RecyclerView recyclerViewLatestReview = view.findViewById(R.id.recycler_latest_review_fp_layout);

        // Auth
        auth = new Auth(context);
        boolean isAuth = auth.isAuth();

        if (!isAuth) {
            Objects.requireNonNull(getFragmentManager())
                    .beginTransaction()
                    .replace(R.id.fragment_am_container, new EmptyUserFragment())
                    .commit();
        } else {
            // List
            List<LatestFavorite> latestFavoriteList = new ArrayList<>();
            List<LatestReview> latestReviewList = new ArrayList<>();

            // GET
            ProfileDetailRequest profileDetailRequest = new ProfileDetailRequest(context, latestFavoriteList, latestReviewList, recyclerViewLatestFavorite, recyclerViewLatestReview, auth.getAuthID());
            profileDetailRequest.sendRequest(new ProfileDetailRequest.APICallback() {
                @Override
                public void onSuccess(ProfileDetail profileDetail) {
                    fullName.setText(profileDetail.getFullName());
                    rate05.setText(profileDetail.getRate05());
                    rate10.setText(profileDetail.getRate10());
                    rate15.setText(profileDetail.getRate15());
                    rate20.setText(profileDetail.getRate20());
                    rate25.setText(profileDetail.getRate25());
                    rate30.setText(profileDetail.getRate30());
                    rate35.setText(profileDetail.getRate35());
                    rate40.setText(profileDetail.getRate40());
                    rate45.setText(profileDetail.getRate45());
                    rate50.setText(profileDetail.getRate50());
                    totalFavorite.setText(profileDetail.getTotalFavorite());
                    totalFollower.setText(profileDetail.getTotalFollower());
                    totalFollowing.setText(profileDetail.getTotalFollowing());
                    totalReview.setText(profileDetail.getTotalReview());
                    totalWatchlist.setText(profileDetail.getTotalWatchlist());
                    username.setText(profileDetail.getUsername());
                    Glide.with(context).load(profileDetail.getProfilePicture()).apply(new RequestOptions().centerCrop().placeholder(R.color.colorPrimary).error(R.color.colorPrimary)).into(profilePicture);

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
                public void onError() {
                    progressBar.setVisibility(View.GONE);
                    userDeleted.setVisibility(View.VISIBLE);
                    Snackbar.make(layout, R.string.get_error, Snackbar.LENGTH_LONG).show();
                }
            });

            // Activity
            totalReview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent gotoReviewList = new Intent(context, UserReviewListActivity.class);
                    gotoReviewList.putExtra("USER_ID", auth.getAuthID());
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
                    Intent gotoFavoriteList = new Intent(context, FavoriteListActivity.class);
                    gotoFavoriteList.putExtra("USER_ID", auth.getAuthID());
                    startActivity(gotoFavoriteList);
                }
            });

            totalWatchlist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent gotoWatchList = new Intent(context, WatchListActivity.class);
                    gotoWatchList.putExtra("USER_ID", auth.getAuthID());
                    startActivity(gotoWatchList);
                }
            });

            buttonEditProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (getFragmentManager() != null) {
                        getFragmentManager().beginTransaction()
                                .replace(R.id.fragment_am_container, new EditProfileFragment())
                                .addToBackStack(null)
                                .commit();
                    }
                }
            });
        }

        return view;
    }
}
