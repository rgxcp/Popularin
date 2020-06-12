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
import xyz.fairportstudios.popularin.services.ParseDate;
import xyz.fairportstudios.popularin.services.ParseImage;
import xyz.fairportstudios.popularin.services.ParseStar;
import xyz.fairportstudios.popularin.statics.Popularin;

public class ReviewDetailFragment extends Fragment {
    private Boolean isLiked;
    private Context context;
    private ImageView imageUserProfile;
    private ImageView imageFilmPoster;
    private ImageView imageReviewStar;
    private ImageView imageLike;
    private Integer currentLike;
    private Integer totalLike;
    private ProgressBar progressBar;
    private RelativeLayout anchorLayot;
    private ScrollView scrollView;
    private Integer userID;
    private String filmID;
    private String filmTitle;
    private String filmYear;
    private String filmPoster;
    private TextView textUserFullName;
    private TextView textFilmTitle;
    private TextView textFilmYear;
    private TextView textReviewDate;
    private TextView textReviewDetail;
    private TextView textLike;
    private TextView textTotalLike;
    private TextView textNetworkError;

    // Constructor
    private String reviewID;

    public ReviewDetailFragment(String reviewID) {
        this.reviewID = reviewID;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_review_detail, container, false);

        // Binding
        context = getActivity();
        imageUserProfile = view.findViewById(R.id.image_frd_profile);
        imageFilmPoster = view.findViewById(R.id.image_frd_poster);
        imageReviewStar = view.findViewById(R.id.image_frd_star);
        imageLike = view.findViewById(R.id.image_frd_like);
        progressBar = view.findViewById(R.id.pbr_frd_layout);
        anchorLayot = view.findViewById(R.id.anchor_frd_layout);
        scrollView = view.findViewById(R.id.scroll_frd_layout);
        textUserFullName = view.findViewById(R.id.text_frd_full_name);
        textFilmTitle = view.findViewById(R.id.text_frd_title);
        textFilmYear = view.findViewById(R.id.text_frd_year);
        textReviewDate = view.findViewById(R.id.text_frd_date);
        textReviewDetail = view.findViewById(R.id.text_frd_review);
        textLike = view.findViewById(R.id.text_frd_like);
        textTotalLike = view.findViewById(R.id.text_frd_total_like);
        textNetworkError = view.findViewById(R.id.text_frd_network_error);

        // Auth
        final boolean isAuth = new Auth(context).isAuth();

        // Mendapatkan data
        getReviewDetail();

        // Activity
        imageUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoUserDetail();
            }
        });

        imageFilmPoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoFilmDetail();
            }
        });

        imageFilmPoster.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showFilmModal();
                return true;
            }
        });

        imageLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAuth) {
                    if (!isLiked) {
                        likeReview();
                    } else {
                        unlikeReview();
                    }
                } else {
                    gotoEmptyAccount();
                }
            }
        });

        textTotalLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoLikedBy();
            }
        });

        return view;
    }

    private void getReviewDetail() {
        ReviewDetailRequest reviewDetailRequest = new ReviewDetailRequest(context, reviewID);
        reviewDetailRequest.sendRequest(new ReviewDetailRequest.APICallback() {
            @Override
            public void onSuccess(ReviewDetail reviewDetail) {
                filmID = String.valueOf(reviewDetail.getTmdb_id());
                userID = reviewDetail.getUser_id();
                isLiked = reviewDetail.getIs_liked();
                currentLike = reviewDetail.getTotal_like();
                filmTitle = reviewDetail.getTitle();

                // Parsing
                filmYear = new ParseDate().getYear(reviewDetail.getRelease_date());
                filmPoster = new ParseImage().getImage(reviewDetail.getPoster());
                Integer reviewStar = new ParseStar().getStar(reviewDetail.getRating());
                String reviewDate = new ParseDate().getDate(reviewDetail.getReview_date());

                // Like status
                if (isLiked) {
                    imageLike.setImageResource(R.drawable.ic_favorite_fill);
                    textLike.setText(R.string.liked);
                }

                // Isi
                textUserFullName.setText(reviewDetail.getFull_name());
                textFilmTitle.setText(filmTitle);
                textFilmYear.setText(filmYear);
                textReviewDate.setText(reviewDate);
                textReviewDetail.setText(reviewDetail.getReview_detail());
                textTotalLike.setText(String.format("Total %s", String.valueOf(currentLike)));
                imageReviewStar.setImageResource(reviewStar);
                Glide.with(context).load(filmPoster).into(imageFilmPoster);
                Glide.with(context).load(reviewDetail.getProfile_picture()).into(imageUserProfile);
                progressBar.setVisibility(View.GONE);
                scrollView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError() {
                progressBar.setVisibility(View.GONE);
                textNetworkError.setVisibility(View.VISIBLE);
                Snackbar.make(anchorLayot, R.string.network_error, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void showFilmModal() {
        FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        FilmModal filmModal = new FilmModal(Integer.parseInt(filmID), filmTitle, filmYear, filmPoster);
        filmModal.show(fragmentManager, Popularin.FILM_STATUS_MODAL);
    }

    private void likeReview() {
        LikeReviewRequest likeReviewRequest = new LikeReviewRequest(context, reviewID);
        likeReviewRequest.sendRequest(new LikeReviewRequest.APICallback() {
            @Override
            public void onSuccess() {
                isLiked = true;
                totalLike = currentLike + 1;
                currentLike++;
                imageLike.setImageResource(R.drawable.ic_favorite_fill);
                textLike.setText(R.string.liked);
                textTotalLike.setText(String.format("Total %s", String.valueOf(totalLike)));
                Snackbar.make(anchorLayot, R.string.review_liked, Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onError() {
                Snackbar.make(anchorLayot, R.string.failed_like_review, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void unlikeReview() {
        UnlikeReviewRequest unlikeReviewRequest = new UnlikeReviewRequest(context, reviewID);
        unlikeReviewRequest.sendRequest(new UnlikeReviewRequest.APICallback() {
            @Override
            public void onSuccess() {
                isLiked = false;
                totalLike = currentLike - 1;
                currentLike--;
                imageLike.setImageResource(R.drawable.ic_favorite_outline);
                textLike.setText(R.string.like);
                textTotalLike.setText(String.format("Total %s", String.valueOf(totalLike)));
                Snackbar.make(anchorLayot, R.string.review_unliked, Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onError() {
                Snackbar.make(anchorLayot, R.string.failed_unlike_review, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void gotoUserDetail() {
        Intent intent = new Intent(context, UserDetailActivity.class);
        intent.putExtra(Popularin.USER_ID, userID);
        context.startActivity(intent);
    }

    private void gotoFilmDetail() {
        Intent intent = new Intent(context, FilmDetailActivity.class);
        intent.putExtra(Popularin.FILM_ID, filmID);
        context.startActivity(intent);
    }

    private void gotoEmptyAccount() {
        Intent intent = new Intent(context, EmptyAccountActivity.class);
        context.startActivity(intent);
    }

    private void gotoLikedBy() {
        Intent intent = new Intent(context, LikedByActivity.class);
        intent.putExtra(Popularin.REVIEW_ID, reviewID);
        context.startActivity(intent);
    }
}
