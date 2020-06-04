package xyz.fairportstudios.popularin.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.activities.EditProfileActivity;
import xyz.fairportstudios.popularin.activities.MainActivity;
import xyz.fairportstudios.popularin.activities.SocialActivity;
import xyz.fairportstudios.popularin.activities.UserFavoriteActivity;
import xyz.fairportstudios.popularin.activities.UserReviewActivity;
import xyz.fairportstudios.popularin.activities.UserWatchlistActivity;
import xyz.fairportstudios.popularin.apis.popularin.get.AccountDetailRequest;
import xyz.fairportstudios.popularin.apis.popularin.post.SignOutRequest;
import xyz.fairportstudios.popularin.models.AccountDetail;
import xyz.fairportstudios.popularin.models.RecentFavorite;
import xyz.fairportstudios.popularin.models.RecentReview;
import xyz.fairportstudios.popularin.preferences.Auth;
import xyz.fairportstudios.popularin.statics.Popularin;

public class AccountFragment extends Fragment {
    private Button buttonSignOut;
    private Context context;
    private CoordinatorLayout anchorLayout;
    private ImageView imageProfile;
    private ImageView imageEmptyRecentFavorite;
    private ImageView imageEmptyRecentReview;
    private ProgressBar progressBar;
    private RecyclerView recyclerRecentFavorite;
    private RecyclerView recyclerRecentReview;
    private ScrollView scrollView;
    private String authID;
    private TextView textFullName;
    private TextView textUsername;
    private TextView textTotalReview;
    private TextView textTotalFavorite;
    private TextView textTotalWatchlist;
    private TextView textTotalFollower;
    private TextView textTotalFollowing;
    private TextView textNetworkError;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        // Binding
        context = getActivity();
        buttonSignOut = view.findViewById(R.id.button_fa_sign_out);
        anchorLayout = view.findViewById(R.id.anchor_fa_layout);
        imageProfile = view.findViewById(R.id.image_fa_profile);
        imageEmptyRecentFavorite = view.findViewById(R.id.image_fa_empty_recent_favorite);
        imageEmptyRecentReview = view.findViewById(R.id.image_fa_empty_recent_review);
        progressBar = view.findViewById(R.id.pbr_fa_layout);
        recyclerRecentFavorite = view.findViewById(R.id.recycler_fa_recent_favorite);
        recyclerRecentReview = view.findViewById(R.id.recycler_fa_recent_review);
        scrollView = view.findViewById(R.id.scroll_fa_layout);
        textFullName = view.findViewById(R.id.text_fa_full_name);
        textUsername = view.findViewById(R.id.text_fa_username);
        textTotalReview = view.findViewById(R.id.text_fa_total_review);
        textTotalFavorite = view.findViewById(R.id.text_fa_total_favorite);
        textTotalWatchlist = view.findViewById(R.id.text_fa_total_watchlist);
        textTotalFollower = view.findViewById(R.id.text_fa_total_follower);
        textTotalFollowing = view.findViewById(R.id.text_fa_total_following);
        textNetworkError = view.findViewById(R.id.text_fa_network_error);
        Button buttonEditProfile = view.findViewById(R.id.button_fa_edit_profile);
        LinearLayout totalReviewLayout = view.findViewById(R.id.layout_fa_total_review);
        LinearLayout totalFavoriteLayout = view.findViewById(R.id.layout_fa_total_favorite);
        LinearLayout totalWacthlistLayout = view.findViewById(R.id.layout_fa_total_watchlist);
        LinearLayout totalFollowerLayout = view.findViewById(R.id.layout_fa_total_follower);
        LinearLayout totalFollowingLayout = view.findViewById(R.id.layout_fa_total_following);

        // Auth
        authID = new Auth(context).getAuthID();

        // Request
        getAccountDetail();

        // Activity
        totalReviewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoAccountReview();
            }
        });

        totalFavoriteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoAccountFavorite();
            }
        });

        totalWacthlistLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoAccountWatchlist();
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

        buttonEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editProfile();
            }
        });

        buttonSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

        return view;
    }

    private void getAccountDetail() {
        final AccountDetailRequest accountDetailRequest = new AccountDetailRequest(context, authID);
        accountDetailRequest.sendRequest(new AccountDetailRequest.APICallback() {
            @Override
            public void onSuccess(AccountDetail accountDetail) {
                textFullName.setText(accountDetail.getFull_name());
                textUsername.setText(String.format("@%s", accountDetail.getUsername()));
                textTotalReview.setText(String.valueOf(accountDetail.getTotal_review()));
                textTotalFavorite.setText(String.valueOf(accountDetail.getTotal_favorite()));
                textTotalWatchlist.setText(String.valueOf(accountDetail.getTotal_watchlist()));
                textTotalFollower.setText(String.valueOf(accountDetail.getTotal_follower()));
                textTotalFollowing.setText(String.valueOf(accountDetail.getTotal_following()));
                Glide.with(context).load(accountDetail.getProfile_picture()).into(imageProfile);
                progressBar.setVisibility(View.GONE);
                scrollView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onHasFavorite(JSONArray recentFavorites) {
                List<RecentFavorite> recentFavoriteList = new ArrayList<>();
                accountDetailRequest.getRecentFavorites(recentFavorites, recentFavoriteList, recyclerRecentFavorite);
                imageEmptyRecentFavorite.setVisibility(View.GONE);
            }

            @Override
            public void onHasReview(JSONArray recentReviews) {
                List<RecentReview> recentReviewList = new ArrayList<>();
                accountDetailRequest.getRecentReviews(recentReviews, recentReviewList, recyclerRecentReview);
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

    private void gotoAccountReview() {
        Intent intent = new Intent(context, UserReviewActivity.class);
        intent.putExtra(Popularin.USER_ID, authID);
        startActivity(intent);
    }

    private void gotoAccountFavorite() {
        Intent intent = new Intent(context, UserFavoriteActivity.class);
        intent.putExtra(Popularin.USER_ID, authID);
        startActivity(intent);
    }

    private void gotoAccountWatchlist() {
        Intent intent = new Intent(context, UserWatchlistActivity.class);
        intent.putExtra(Popularin.USER_ID, authID);
        startActivity(intent);
    }

    private void gotoSocial() {
        Intent intent = new Intent(context, SocialActivity.class);
        intent.putExtra(Popularin.USER_ID, authID);
        intent.putExtra(Popularin.IS_SELF, true);
        startActivity(intent);
    }

    private void editProfile() {
        Intent intent = new Intent(context, EditProfileActivity.class);
        startActivity(intent);
    }

    private void signOut() {
        buttonSignOut.setEnabled(false);
        buttonSignOut.setText(R.string.loading);

        SignOutRequest signOutRequest = new SignOutRequest(context);
        signOutRequest.sendRequest(new SignOutRequest.APICallback() {
            @Override
            public void onSuccess() {
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
                Objects.requireNonNull(getActivity()).finish();
            }

            @Override
            public void onError() {
                buttonSignOut.setEnabled(true);
                buttonSignOut.setText(R.string.sign_out);
                Snackbar.make(anchorLayout, R.string.failed_sign_out, Snackbar.LENGTH_LONG).show();
            }
        });
    }
}
