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
import xyz.fairportstudios.popularin.services.Popularin;

public class AccountFragment extends Fragment {
    private Button buttonSignOut;
    private Context context;
    private CoordinatorLayout layout;
    private ImageView imageProfile;
    private ImageView imageEmptyRecentFavorite;
    private ImageView imageEmptyRecentReview;
    private LinearLayout layoutNotFound;
    private ProgressBar progressBar;
    private ScrollView scrollView;
    private TextView textFullName;
    private TextView textUsername;
    private TextView textTotalReview;
    private TextView textTotalFavorite;
    private TextView textTotalWatchlist;
    private TextView textTotalFollower;
    private TextView textTotalFollowing;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        // Binding
        context = getActivity();
        buttonSignOut = view.findViewById(R.id.button_fa_sign_out);
        layout = view.findViewById(R.id.layout_fa_anchor);
        imageProfile = view.findViewById(R.id.image_fa_profile);
        imageEmptyRecentFavorite = view.findViewById(R.id.image_fa_empty_recent_favorite);
        imageEmptyRecentReview = view.findViewById(R.id.image_fa_empty_recent_review);
        layoutNotFound = view.findViewById(R.id.layout_fa_not_found);
        progressBar = view.findViewById(R.id.pbr_fa_layout);
        scrollView = view.findViewById(R.id.scroll_fa_layout);
        textFullName = view.findViewById(R.id.text_fa_full_name);
        textUsername = view.findViewById(R.id.text_fa_username);
        textTotalReview = view.findViewById(R.id.text_fa_total_review);
        textTotalFavorite = view.findViewById(R.id.text_fa_total_favorite);
        textTotalWatchlist = view.findViewById(R.id.text_fa_total_watchlist);
        textTotalFollower = view.findViewById(R.id.text_fa_total_follower);
        textTotalFollowing = view.findViewById(R.id.text_fa_total_following);
        Button buttonEditProfile = view.findViewById(R.id.button_fa_edit_profile);
        RecyclerView recyclerRecentFavorite = view.findViewById(R.id.recycler_fa_recent_favorite);
        RecyclerView recyclerRecentReview = view.findViewById(R.id.recycler_fa_recent_review);

        // Auth
        final String authID = new Auth(context).getAuthID();

        // List
        List<RecentFavorite> recentFavoriteList = new ArrayList<>();
        List<RecentReview> recentReviewList = new ArrayList<>();

        // Membuat objek
        AccountDetailRequest accountDetailRequest = new AccountDetailRequest(
                context,
                authID,
                recentFavoriteList,
                recentReviewList,
                recyclerRecentFavorite,
                recyclerRecentReview
        );

        // Mengirim request
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
            public void onEmptyFavorite() {
                progressBar.setVisibility(View.GONE);
                imageEmptyRecentFavorite.setVisibility(View.VISIBLE);
            }

            @Override
            public void onEmptyReview() {
                progressBar.setVisibility(View.GONE);
                imageEmptyRecentReview.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError() {
                progressBar.setVisibility(View.GONE);
                layoutNotFound.setVisibility(View.VISIBLE);
                Snackbar.make(layout, R.string.network_error, Snackbar.LENGTH_LONG).show();
            }
        });

        // Activity
        textTotalReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UserReviewActivity.class);
                intent.putExtra(Popularin.USER_ID, authID);
                startActivity(intent);
            }
        });

        textTotalFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UserFavoriteActivity.class);
                intent.putExtra(Popularin.USER_ID, authID);
                startActivity(intent);
            }
        });

        textTotalWatchlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UserWatchlistActivity.class);
                intent.putExtra(Popularin.USER_ID, authID);
                startActivity(intent);
            }
        });

        textTotalFollower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SocialActivity.class);
                intent.putExtra(Popularin.USER_ID, authID);
                intent.putExtra(Popularin.IS_SELF, true);
                startActivity(intent);
            }
        });

        textTotalFollowing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SocialActivity.class);
                intent.putExtra(Popularin.USER_ID, authID);
                intent.putExtra(Popularin.IS_SELF, true);
                startActivity(intent);
            }
        });

        buttonEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EditProfileActivity.class);
                startActivity(intent);
            }
        });

        buttonSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonSignOut.setEnabled(false);
                buttonSignOut.setText(R.string.loading);
                signOut();
            }
        });

        return view;
    }

    private void signOut() {
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
                Snackbar.make(layout, R.string.sign_out_error, Snackbar.LENGTH_LONG).show();
            }
        });
    }
}
