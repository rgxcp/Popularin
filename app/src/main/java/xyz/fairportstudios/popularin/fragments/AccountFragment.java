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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;

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
import xyz.fairportstudios.popularin.preferences.Auth;
import xyz.fairportstudios.popularin.statics.Popularin;

public class AccountFragment extends Fragment {
    // Variable untuk fitur swipe refresh
    private Boolean isLoadFirstTime = true;

    // Variable member
    private Button buttonSignOut;
    private CoordinatorLayout anchorLayout;
    private ImageView imageProfile;
    private ImageView imageEmptyRecentFavorite;
    private ImageView imageEmptyRecentReview;
    private ProgressBar progressBar;
    private RecyclerView recyclerRecentFavorite;
    private RecyclerView recyclerRecentReview;
    private ScrollView scrollView;
    private TextView textFullName;
    private TextView textUsername;
    private TextView textTotalReview;
    private TextView textTotalFavorite;
    private TextView textTotalWatchlist;
    private TextView textTotalFollower;
    private TextView textTotalFollowing;
    private TextView textMessage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        // Context
        final Context context = getActivity();

        // Binding
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
        textMessage = view.findViewById(R.id.text_fa_message);
        Button buttonEditProfile = view.findViewById(R.id.button_fa_edit_profile);
        LinearLayout totalReviewLayout = view.findViewById(R.id.layout_fa_total_review);
        LinearLayout totalFavoriteLayout = view.findViewById(R.id.layout_fa_total_favorite);
        LinearLayout totalWatchlistLayout = view.findViewById(R.id.layout_fa_total_watchlist);
        LinearLayout totalFollowerLayout = view.findViewById(R.id.layout_fa_total_follower);
        LinearLayout totalFollowingLayout = view.findViewById(R.id.layout_fa_total_following);
        final SwipeRefreshLayout swipeRefresh = view.findViewById(R.id.swipe_refresh_fa_layout);

        // Auth
        final Auth auth = new Auth(context);
        final Integer authID = auth.getAuthID();

        // Mendapatkan informasi diri sendiri
        getAccountDetail(context, authID);

        // Activity
        totalReviewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoAccountReview(context, authID);
            }
        });

        totalFavoriteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoAccountFavorite(context, authID);
            }
        });

        totalWatchlistLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoAccountWatchlist(context, authID);
            }
        });

        totalFollowerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoAccountSocial(context, authID, 0);
            }
        });

        totalFollowingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoAccountSocial(context, authID, 1);
            }
        });

        buttonEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editProfile(context);
            }
        });

        buttonSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSignOutButtonState(false);
                signOut(context, auth);
            }
        });

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAccountDetail(context, authID);
                swipeRefresh.setRefreshing(false);
            }
        });

        return view;
    }

    private void getAccountDetail(final Context context, Integer authID) {
        // Menghilangkan pesan setiap kali method dijalankan
        textMessage.setVisibility(View.GONE);

        final AccountDetailRequest accountDetailRequest = new AccountDetailRequest(context, authID);
        accountDetailRequest.sendRequest(new AccountDetailRequest.Callback() {
            @Override
            public void onSuccess(AccountDetail accountDetail) {
                // Request gambar
                RequestOptions requestOptions = new RequestOptions()
                        .centerCrop()
                        .placeholder(R.color.colorSurface)
                        .error(R.color.colorSurface);

                textFullName.setText(accountDetail.getFull_name());
                textUsername.setText(String.format("@%s", accountDetail.getUsername()));
                textTotalReview.setText(String.valueOf(accountDetail.getTotal_review()));
                textTotalFavorite.setText(String.valueOf(accountDetail.getTotal_favorite()));
                textTotalWatchlist.setText(String.valueOf(accountDetail.getTotal_watchlist()));
                textTotalFollower.setText(String.valueOf(accountDetail.getTotal_follower()));
                textTotalFollowing.setText(String.valueOf(accountDetail.getTotal_following()));
                Glide.with(context).load(accountDetail.getProfile_picture()).apply(requestOptions).into(imageProfile);
                progressBar.setVisibility(View.GONE);
                scrollView.setVisibility(View.VISIBLE);
                isLoadFirstTime = false;
            }

            @Override
            public void onHasFavorite(JSONArray recentFavoriteArray) {
                accountDetailRequest.getRecentFavorites(recentFavoriteArray, recyclerRecentFavorite);
                imageEmptyRecentFavorite.setVisibility(View.GONE);
            }

            @Override
            public void onHasReview(JSONArray recentReviewArray) {
                accountDetailRequest.getRecentReviews(recentReviewArray, recyclerRecentReview);
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

    private void gotoAccountReview(Context context, Integer authID) {
        Intent intent = new Intent(context, UserReviewActivity.class);
        intent.putExtra(Popularin.USER_ID, authID);
        startActivity(intent);
    }

    private void gotoAccountFavorite(Context context, Integer authID) {
        Intent intent = new Intent(context, UserFavoriteActivity.class);
        intent.putExtra(Popularin.USER_ID, authID);
        startActivity(intent);
    }

    private void gotoAccountWatchlist(Context context, Integer authID) {
        Intent intent = new Intent(context, UserWatchlistActivity.class);
        intent.putExtra(Popularin.USER_ID, authID);
        startActivity(intent);
    }

    private void gotoAccountSocial(Context context, Integer authID, Integer viewPagerIndex) {
        Intent intent = new Intent(context, SocialActivity.class);
        intent.putExtra(Popularin.USER_ID, authID);
        intent.putExtra(Popularin.VIEW_PAGER_INDEX, viewPagerIndex);
        intent.putExtra(Popularin.IS_SELF, true);
        startActivity(intent);
    }

    private void editProfile(Context context) {
        Intent intent = new Intent(context, EditProfileActivity.class);
        startActivity(intent);
    }

    private void setSignOutButtonState(Boolean state) {
        buttonSignOut.setEnabled(state);
        if (state) {
            buttonSignOut.setText(R.string.sign_out);
        } else {
            buttonSignOut.setText(R.string.loading);
        }
    }

    private void signOut(final Context context, final Auth auth) {
        SignOutRequest signOutRequest = new SignOutRequest(context);
        signOutRequest.sendRequest(new SignOutRequest.Callback() {
            @Override
            public void onSuccess() {
                auth.delAuth();

                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
                Objects.requireNonNull(getActivity()).finish();
            }

            @Override
            public void onError(String message) {
                setSignOutButtonState(true);
                Snackbar.make(anchorLayout, message, Snackbar.LENGTH_LONG).show();
            }
        });
    }
}
