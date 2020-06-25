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
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.activities.EditProfileActivity;
import xyz.fairportstudios.popularin.activities.FilmDetailActivity;
import xyz.fairportstudios.popularin.activities.MainActivity;
import xyz.fairportstudios.popularin.activities.ReviewActivity;
import xyz.fairportstudios.popularin.activities.SocialActivity;
import xyz.fairportstudios.popularin.activities.UserFavoriteActivity;
import xyz.fairportstudios.popularin.activities.UserReviewActivity;
import xyz.fairportstudios.popularin.activities.UserWatchlistActivity;
import xyz.fairportstudios.popularin.adapters.RecentFavoriteAdapter;
import xyz.fairportstudios.popularin.adapters.RecentReviewAdapter;
import xyz.fairportstudios.popularin.apis.popularin.get.AccountDetailRequest;
import xyz.fairportstudios.popularin.modals.FilmModal;
import xyz.fairportstudios.popularin.models.AccountDetail;
import xyz.fairportstudios.popularin.models.RecentFavorite;
import xyz.fairportstudios.popularin.models.RecentReview;
import xyz.fairportstudios.popularin.preferences.Auth;
import xyz.fairportstudios.popularin.services.ParseDate;
import xyz.fairportstudios.popularin.statics.Popularin;

public class AccountFragment extends Fragment implements RecentFavoriteAdapter.OnClickListener, RecentReviewAdapter.OnClickListener {
    // Variable untuk fitur load
    private boolean mIsLoadFirstTimeSuccess = false;

    // Variable member
    private Context mContext;
    private CoordinatorLayout mAnchorLayout;
    private ImageView mImageProfile;
    private ImageView mImageEmptyRecentFavorite;
    private ImageView mImageEmptyRecentReview;
    private List<RecentFavorite> mRecentFavoriteList;
    private List<RecentReview> mRecentReviewList;
    private ProgressBar mProgressBar;
    private RecentFavoriteAdapter.OnClickListener mOnRecentFavoriteClickListener;
    private RecentReviewAdapter.OnClickListener mOnRecentReviewClickListener;
    private RecyclerView mRecyclerRecentFavorite;
    private RecyclerView mRecyclerRecentReview;
    private ScrollView mScrollView;
    private SwipeRefreshLayout mSwipeRefresh;
    private TextView mTextFullName;
    private TextView mTextUsername;
    private TextView mTextTotalReview;
    private TextView mTextTotalFavorite;
    private TextView mTextTotalWatchlist;
    private TextView mTextTotalFollower;
    private TextView mTextTotalFollowing;
    private TextView mTextMessage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        // Context
        mContext = getActivity();

        // Binding
        mAnchorLayout = view.findViewById(R.id.anchor_fa_layout);
        mImageProfile = view.findViewById(R.id.image_fa_profile);
        mImageEmptyRecentFavorite = view.findViewById(R.id.image_fa_empty_recent_favorite);
        mImageEmptyRecentReview = view.findViewById(R.id.image_fa_empty_recent_review);
        mProgressBar = view.findViewById(R.id.pbr_fa_layout);
        mRecyclerRecentFavorite = view.findViewById(R.id.recycler_fa_recent_favorite);
        mRecyclerRecentReview = view.findViewById(R.id.recycler_fa_recent_review);
        mScrollView = view.findViewById(R.id.scroll_fa_layout);
        mSwipeRefresh = view.findViewById(R.id.swipe_refresh_fa_layout);
        mTextFullName = view.findViewById(R.id.text_fa_full_name);
        mTextUsername = view.findViewById(R.id.text_fa_username);
        mTextTotalReview = view.findViewById(R.id.text_fa_total_review);
        mTextTotalFavorite = view.findViewById(R.id.text_fa_total_favorite);
        mTextTotalWatchlist = view.findViewById(R.id.text_fa_total_watchlist);
        mTextTotalFollower = view.findViewById(R.id.text_fa_total_follower);
        mTextTotalFollowing = view.findViewById(R.id.text_fa_total_following);
        mTextMessage = view.findViewById(R.id.text_fa_message);
        Button buttonEditProfile = view.findViewById(R.id.button_fa_edit_profile);
        Button buttonSignOut = view.findViewById(R.id.button_fa_sign_out);
        LinearLayout totalReviewLayout = view.findViewById(R.id.layout_fa_total_review);
        LinearLayout totalFavoriteLayout = view.findViewById(R.id.layout_fa_total_favorite);
        LinearLayout totalWatchlistLayout = view.findViewById(R.id.layout_fa_total_watchlist);
        LinearLayout totalFollowerLayout = view.findViewById(R.id.layout_fa_total_follower);
        LinearLayout totalFollowingLayout = view.findViewById(R.id.layout_fa_total_following);

        // Auth
        final Auth auth = new Auth(mContext);
        final int authID = auth.getAuthID();

        // Mendapatkan data
        mOnRecentFavoriteClickListener = this;
        mOnRecentReviewClickListener = this;
        getAccountDetail(authID);

        // Activity
        totalReviewLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoAccountReview(authID);
            }
        });

        totalFavoriteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoAccountFavorite(authID);
            }
        });

        totalWatchlistLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoAccountWatchlist(authID);
            }
        });

        totalFollowerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoAccountSocial(authID, 0);
            }
        });

        totalFollowingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoAccountSocial(authID, 1);
            }
        });

        buttonEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoEditProfile();
            }
        });

        buttonSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.delAuth();
                signOut();
            }
        });

        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefresh.setRefreshing(true);
                getAccountDetail(authID);
            }
        });

        return view;
    }

    @Override
    public void onRecentFavoriteItemClick(int position) {
        RecentFavorite currentItem = mRecentFavoriteList.get(position);
        int id = currentItem.getTmdb_id();
        gotoFilmDetail(id);
    }

    @Override
    public void onRecentFavoriteItemLongClick(int position) {
        RecentFavorite currentItem = mRecentFavoriteList.get(position);
        int id = currentItem.getTmdb_id();
        String title = currentItem.getTitle();
        String year = new ParseDate().getYear(currentItem.getRelease_date());
        String poster = currentItem.getPoster();
        showFilmModal(id, title, year, poster);
    }

    @Override
    public void onRecentReviewItemClick(int position) {
        RecentReview currentItem = mRecentReviewList.get(position);
        int id = currentItem.getId();
        gotoReviewDetail(id);
    }

    @Override
    public void onRecentReviewItemLongClick(int position) {
        RecentReview currentItem = mRecentReviewList.get(position);
        int id = currentItem.getTmdb_id();
        String title = currentItem.getTitle();
        String year = new ParseDate().getYear(currentItem.getRelease_date());
        String poster = currentItem.getPoster();
        showFilmModal(id, title, year, poster);
    }

    private void getAccountDetail(int id) {
        final AccountDetailRequest accountDetailRequest = new AccountDetailRequest(mContext, id);
        accountDetailRequest.sendRequest(new AccountDetailRequest.Callback() {
            @Override
            public void onSuccess(AccountDetail accountDetail) {
                mTextFullName.setText(accountDetail.getFull_name());
                mTextUsername.setText(String.format("@%s", accountDetail.getUsername()));
                mTextTotalReview.setText(String.valueOf(accountDetail.getTotal_review()));
                mTextTotalFavorite.setText(String.valueOf(accountDetail.getTotal_favorite()));
                mTextTotalWatchlist.setText(String.valueOf(accountDetail.getTotal_watchlist()));
                mTextTotalFollower.setText(String.valueOf(accountDetail.getTotal_follower()));
                mTextTotalFollowing.setText(String.valueOf(accountDetail.getTotal_following()));
                Glide.with(mContext).load(accountDetail.getProfile_picture()).into(mImageProfile);
                mProgressBar.setVisibility(View.GONE);
                mTextMessage.setVisibility(View.GONE);
                mScrollView.setVisibility(View.VISIBLE);
                mIsLoadFirstTimeSuccess = true;
            }

            @Override
            public void onHasRecentFavorite(List<RecentFavorite> recentFavoriteList) {
                mRecentFavoriteList = new ArrayList<>();
                mRecentFavoriteList.addAll(recentFavoriteList);
                RecentFavoriteAdapter recentFavoriteAdapter = new RecentFavoriteAdapter(mContext, mRecentFavoriteList, mOnRecentFavoriteClickListener);
                mRecyclerRecentFavorite.setAdapter(recentFavoriteAdapter);
                mRecyclerRecentFavorite.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false));
                mRecyclerRecentFavorite.setHasFixedSize(true);
                mRecyclerRecentFavorite.setVisibility(View.VISIBLE);
                mImageEmptyRecentFavorite.setVisibility(View.GONE);
            }

            @Override
            public void onHasRecentReview(List<RecentReview> recentReviewList) {
                mRecentReviewList = new ArrayList<>();
                mRecentReviewList.addAll(recentReviewList);
                RecentReviewAdapter recentReviewAdapter = new RecentReviewAdapter(mContext, mRecentReviewList, mOnRecentReviewClickListener);
                mRecyclerRecentReview.setAdapter(recentReviewAdapter);
                mRecyclerRecentReview.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false));
                mRecyclerRecentReview.setHasFixedSize(true);
                mRecyclerRecentReview.setVisibility(View.VISIBLE);
                mImageEmptyRecentReview.setVisibility(View.GONE);
            }

            @Override
            public void onError(String message) {
                if (!mIsLoadFirstTimeSuccess) {
                    mProgressBar.setVisibility(View.GONE);
                    mTextMessage.setVisibility(View.VISIBLE);
                    mTextMessage.setText(message);
                } else {
                    Snackbar.make(mAnchorLayout, message, Snackbar.LENGTH_LONG).show();
                }
            }
        });

        // Memberhentikan loading
        mSwipeRefresh.setRefreshing(false);
    }

    private void gotoFilmDetail(int id) {
        Intent intent = new Intent(mContext, FilmDetailActivity.class);
        intent.putExtra(Popularin.FILM_ID, id);
        startActivity(intent);
    }

    private void gotoReviewDetail(int id) {
        Intent intent = new Intent(mContext, ReviewActivity.class);
        intent.putExtra(Popularin.REVIEW_ID, id);
        intent.putExtra(Popularin.IS_SELF, true);
        startActivity(intent);
    }

    private void showFilmModal(int id, String title, String year, String poster) {
        FragmentManager fragmentManager = ((FragmentActivity) mContext).getSupportFragmentManager();
        FilmModal filmModal = new FilmModal(id, title, year, poster);
        filmModal.show(fragmentManager, Popularin.FILM_MODAL);
    }

    private void gotoAccountReview(int id) {
        Intent intent = new Intent(mContext, UserReviewActivity.class);
        intent.putExtra(Popularin.USER_ID, id);
        startActivity(intent);
    }

    private void gotoAccountFavorite(int id) {
        Intent intent = new Intent(mContext, UserFavoriteActivity.class);
        intent.putExtra(Popularin.USER_ID, id);
        startActivity(intent);
    }

    private void gotoAccountWatchlist(int id) {
        Intent intent = new Intent(mContext, UserWatchlistActivity.class);
        intent.putExtra(Popularin.USER_ID, id);
        startActivity(intent);
    }

    private void gotoAccountSocial(int id, int viewPagerIndex) {
        Intent intent = new Intent(mContext, SocialActivity.class);
        intent.putExtra(Popularin.USER_ID, id);
        intent.putExtra(Popularin.VIEW_PAGER_INDEX, viewPagerIndex);
        startActivity(intent);
    }

    private void gotoEditProfile() {
        Intent intent = new Intent(mContext, EditProfileActivity.class);
        startActivity(intent);
    }

    private void signOut() {
        Intent intent = new Intent(mContext, MainActivity.class);
        startActivity(intent);
        Objects.requireNonNull(getActivity()).finish();
    }
}
