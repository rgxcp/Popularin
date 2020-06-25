package xyz.fairportstudios.popularin.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.activities.EmptyAccountActivity;
import xyz.fairportstudios.popularin.activities.FilmDetailActivity;
import xyz.fairportstudios.popularin.activities.LikedByActivity;
import xyz.fairportstudios.popularin.activities.UserDetailActivity;
import xyz.fairportstudios.popularin.apis.popularin.delete.UnlikeReviewRequest;
import xyz.fairportstudios.popularin.apis.popularin.get.ReviewDetailRequest;
import xyz.fairportstudios.popularin.apis.popularin.post.LikeReviewRequest;
import xyz.fairportstudios.popularin.modals.FilmModal;
import xyz.fairportstudios.popularin.models.ReviewDetail;
import xyz.fairportstudios.popularin.preferences.Auth;
import xyz.fairportstudios.popularin.services.ConvertRating;
import xyz.fairportstudios.popularin.services.ParseDate;
import xyz.fairportstudios.popularin.statics.Popularin;
import xyz.fairportstudios.popularin.statics.TMDbAPI;

public class ReviewDetailFragment extends Fragment {
    // Variable untuk fitur onResume
    private boolean mIsResumeFirstTime = true;

    // Variable untuk fitur load
    private boolean mIsLoading = true;
    private boolean mIsLoadFirstTimeSuccess = false;

    // Variable member
    private boolean mIsLiked;
    private int mUserID;
    private int mFilmID;
    private int mTotalLike;
    private Context mContext;
    private ImageView mImageUserProfile;
    private ImageView mImageFilmPoster;
    private ImageView mImageReviewStar;
    private ImageView mImageLike;
    private ProgressBar mProgressBar;
    private RelativeLayout mAnchorLayout;
    private ScrollView mScrollView;
    private String mFilmTitle;
    private String mFilmYear;
    private String mFilmPoster;
    private TextView mTextUsername;
    private TextView mTextFilmTitleYear;
    private TextView mTextReviewDate;
    private TextView mTextReviewDetail;
    private TextView mTextLikeStatus;
    private TextView mTextTotalLike;
    private TextView mTextMessage;
    private SwipeRefreshLayout mSwipeRefresh;

    // Variable constructor
    private int mReviewID;

    public ReviewDetailFragment(int reviewID) {
        mReviewID = reviewID;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_review_detail, container, false);

        // Context
        mContext = getActivity();

        // Binding
        mImageUserProfile = view.findViewById(R.id.image_frd_profile);
        mImageFilmPoster = view.findViewById(R.id.image_frd_poster);
        mImageReviewStar = view.findViewById(R.id.image_frd_star);
        mImageLike = view.findViewById(R.id.image_frd_like);
        mProgressBar = view.findViewById(R.id.pbr_frd_layout);
        mAnchorLayout = view.findViewById(R.id.anchor_frd_layout);
        mScrollView = view.findViewById(R.id.scroll_frd_layout);
        mTextUsername = view.findViewById(R.id.text_frd_username);
        mTextFilmTitleYear = view.findViewById(R.id.text_frd_title_year);
        mTextReviewDate = view.findViewById(R.id.text_frd_date);
        mTextReviewDetail = view.findViewById(R.id.text_frd_review);
        mTextLikeStatus = view.findViewById(R.id.text_frd_like_status);
        mTextTotalLike = view.findViewById(R.id.text_frd_total_like);
        mTextMessage = view.findViewById(R.id.text_frd_message);
        mSwipeRefresh = view.findViewById(R.id.swipe_refresh_frd_layout);

        // Auth
        final boolean isAuth = new Auth(mContext).isAuth();

        // Activity
        mImageUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoUserDetail();
            }
        });

        mImageFilmPoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoFilmDetail();
            }
        });

        mImageFilmPoster.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showFilmModal();
                return true;
            }
        });

        mImageLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAuth && !mIsLoading) {
                    mIsLoading = true;
                    if (!mIsLiked) {
                        likeReview();
                    } else {
                        unlikeReview();
                    }
                } else {
                    gotoEmptyAccount();
                }
            }
        });

        mTextTotalLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoLikedBy();
            }
        });

        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mIsLoading = true;
                mSwipeRefresh.setRefreshing(true);
                getReviewDetail();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mIsResumeFirstTime) {
            // Mendapatkan data
            mIsResumeFirstTime = false;
            getReviewDetail();
        }
    }

    private void getReviewDetail() {
        ReviewDetailRequest reviewDetailRequest = new ReviewDetailRequest(mContext, mReviewID);
        reviewDetailRequest.sendRequest(new ReviewDetailRequest.Callback() {
            @Override
            public void onSuccess(ReviewDetail reviewDetail) {
                // Like status
                mIsLiked = reviewDetail.getIs_liked();
                if (mIsLiked) {
                    mTextLikeStatus.setText(R.string.liked);
                    mImageLike.setImageResource(R.drawable.ic_fill_heart);
                }

                // Parsing
                mUserID = reviewDetail.getUser_id();
                mFilmID = reviewDetail.getTmdb_id();
                mTotalLike = reviewDetail.getTotal_like();
                mFilmTitle = reviewDetail.getTitle();
                mFilmYear = new ParseDate().getYear(reviewDetail.getRelease_date());
                mFilmPoster = reviewDetail.getPoster();
                int reviewStar = new ConvertRating().getStar(reviewDetail.getRating());
                String reviewDate = new ParseDate().getDateForHumans(reviewDetail.getReview_date());

                // Isi
                mTextUsername.setText(reviewDetail.getUsername());
                mTextFilmTitleYear.setText(String.format("%s (%s)", mFilmTitle, mFilmYear));
                mTextReviewDate.setText(reviewDate);
                mTextReviewDetail.setText(reviewDetail.getReview_detail());
                mTextTotalLike.setText(String.format("Total %s", mTotalLike));
                mImageReviewStar.setImageResource(reviewStar);
                Glide.with(mContext).load(reviewDetail.getProfile_picture()).into(mImageUserProfile);
                Glide.with(mContext).load(TMDbAPI.BASE_SMALL_IMAGE_URL + mFilmPoster).into(mImageFilmPoster);
                mTextMessage.setVisibility(View.GONE);
                mProgressBar.setVisibility(View.GONE);
                mScrollView.setVisibility(View.VISIBLE);
                mIsLoadFirstTimeSuccess = true;
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
        mIsLoading = false;
        mSwipeRefresh.setRefreshing(false);
    }

    private void gotoUserDetail() {
        Intent intent = new Intent(mContext, UserDetailActivity.class);
        intent.putExtra(Popularin.USER_ID, mUserID);
        mContext.startActivity(intent);
    }

    private void gotoFilmDetail() {
        Intent intent = new Intent(mContext, FilmDetailActivity.class);
        intent.putExtra(Popularin.FILM_ID, mFilmID);
        mContext.startActivity(intent);
    }

    private void gotoLikedBy() {
        Intent intent = new Intent(mContext, LikedByActivity.class);
        intent.putExtra(Popularin.REVIEW_ID, mReviewID);
        mContext.startActivity(intent);
    }

    private void gotoEmptyAccount() {
        Intent intent = new Intent(mContext, EmptyAccountActivity.class);
        mContext.startActivity(intent);
    }

    private void showFilmModal() {
        FragmentManager fragmentManager = ((FragmentActivity) mContext).getSupportFragmentManager();
        FilmModal filmModal = new FilmModal(mFilmID, mFilmTitle, mFilmYear, mFilmPoster);
        filmModal.show(fragmentManager, Popularin.FILM_MODAL);
    }

    private void setLikeState(boolean isLiked) {
        mIsLiked = isLiked;
        if (mIsLiked) {
            mTotalLike++;
            mTextLikeStatus.setText(R.string.liked);
            mImageLike.setImageResource(R.drawable.ic_fill_heart);
        } else {
            mTotalLike--;
            mTextLikeStatus.setText(R.string.like);
            mImageLike.setImageResource(R.drawable.ic_outline_heart);
        }
        mTextTotalLike.setText(String.format("Total %s", mTotalLike));
    }

    private void likeReview() {
        LikeReviewRequest likeReviewRequest = new LikeReviewRequest(mContext, mReviewID);
        likeReviewRequest.sendRequest(new LikeReviewRequest.Callback() {
            @Override
            public void onSuccess() {
                setLikeState(true);
            }

            @Override
            public void onError(String message) {
                Snackbar.make(mAnchorLayout, message, Snackbar.LENGTH_LONG).show();
            }
        });

        // Memberhentikan loading
        mIsLoading = false;
    }

    private void unlikeReview() {
        UnlikeReviewRequest unlikeReviewRequest = new UnlikeReviewRequest(mContext, mReviewID);
        unlikeReviewRequest.sendRequest(new UnlikeReviewRequest.Callback() {
            @Override
            public void onSuccess() {
                setLikeState(false);
            }

            @Override
            public void onError(String message) {
                Snackbar.make(mAnchorLayout, message, Snackbar.LENGTH_LONG).show();
            }
        });

        // Memberhentikan loading
        mIsLoading = false;
    }
}
