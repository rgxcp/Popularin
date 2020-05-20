package xyz.fairportstudios.popularin.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import xyz.fairportstudios.popularin.models.ReviewDetail;
import xyz.fairportstudios.popularin.preferences.Auth;
import xyz.fairportstudios.popularin.services.ParseDate;
import xyz.fairportstudios.popularin.services.ParseImage;
import xyz.fairportstudios.popularin.services.ParseStar;
import xyz.fairportstudios.popularin.services.Popularin;

public class ReviewDetailFragment extends Fragment {
    // Member
    private Boolean isAuth;
    private Boolean isLiked;
    private Context context;
    private ImageView imageUserProfile;
    private ImageView imageFilmPoster;
    private ImageView imageReviewStar;
    private ImageView imageLike;
    private Integer rating;
    private Integer currentLike;
    private Integer totalLike;
    private LinearLayout notFoundLayout;
    private ProgressBar progressBar;
    private RelativeLayout anchorLayot;
    private ScrollView scrollView;
    private String userID;
    private String filmID;
    private String filmTitle;
    private String filmYear;
    private String filmPoster;
    private TextView textUserFullName;
    private TextView textFilmTitle;
    private TextView textFilmYear;
    private TextView textReviewDate;
    private TextView textReviewDetail;
    private TextView textLikeStatus;
    private TextView textTotalLike;

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
        imageLike = view.findViewById(R.id.icon_frd_like);
        notFoundLayout = view.findViewById(R.id.layout_frd_not_found);
        progressBar = view.findViewById(R.id.pbr_frd_layout);
        anchorLayot = view.findViewById(R.id.layout_frd_anchor);
        scrollView = view.findViewById(R.id.scroll_frd_anchor);
        textUserFullName = view.findViewById(R.id.text_frd_full_name);
        textFilmTitle = view.findViewById(R.id.text_frd_title);
        textFilmYear = view.findViewById(R.id.text_frd_year);
        textReviewDate = view.findViewById(R.id.text_frd_date);
        textReviewDetail = view.findViewById(R.id.text_frd_detail);
        textLikeStatus = view.findViewById(R.id.text_frd_like_status);
        textTotalLike = view.findViewById(R.id.text_frd_total_like);

        // Auth
        isAuth = new Auth(context).isAuth();

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
                showFilmStatusModal();
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
                gotoUserList();
            }
        });

        return view;
    }

    private void getReviewDetail() {
        ReviewDetailRequest reviewDetailRequest = new ReviewDetailRequest(context, reviewID);
        reviewDetailRequest.sendRequest(new ReviewDetailRequest.APICallback() {
            @Override
            public void onSuccess(ReviewDetail reviewDetail) {
                // ID
                filmID = String.valueOf(reviewDetail.getFilm_id());
                userID = String.valueOf(reviewDetail.getUser_id());

                // Parsing
                filmYear = new ParseDate().getYear(reviewDetail.getRelease_date());
                filmPoster = new ParseImage().getImage(reviewDetail.getPoster());
                rating = new ParseStar().getStar(reviewDetail.getRating());
                String reviewDate = new ParseDate().getDate(reviewDetail.getReview_date());

                // Getter
                isLiked = reviewDetail.getIs_liked();
                totalLike = reviewDetail.getTotal_like();
                filmTitle = reviewDetail.getTitle();

                // Status
                if (isLiked) {
                    imageLike.setImageResource(R.drawable.ic_favorite_fill);
                    textLikeStatus.setText(R.string.liked);
                }

                // Setter
                textUserFullName.setText(reviewDetail.getFull_name());
                textFilmTitle.setText(filmTitle);
                textFilmYear.setText(filmYear);
                textReviewDate.setText(reviewDate);
                textReviewDetail.setText(reviewDetail.getReview_detail());
                textTotalLike.setText(String.format("Total %s", String.valueOf(totalLike)));
                imageReviewStar.setImageResource(rating);
                Glide.with(context).load(filmPoster).into(imageFilmPoster);
                Glide.with(context).load(reviewDetail.getProfile_picture()).into(imageUserProfile);
                progressBar.setVisibility(View.GONE);
                scrollView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError() {
                progressBar.setVisibility(View.GONE);
                notFoundLayout.setVisibility(View.VISIBLE);
                Snackbar.make(anchorLayot, R.string.get_error, Snackbar.LENGTH_LONG).show();
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

    private void showFilmStatusModal() {
        FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        FilmStatusModal filmStatusModal = new FilmStatusModal(filmID, filmTitle, filmYear, filmPoster);
        filmStatusModal.show(fragmentManager, Popularin.FILM_STATUS_MODAL);
    }

    private void likeReview() {
        LikeReviewRequest likeReviewRequest = new LikeReviewRequest(context, reviewID);
        likeReviewRequest.sendRequest(new LikeReviewRequest.APICallback() {
            @Override
            public void onSuccess() {
                imageLike.setImageResource(R.drawable.ic_favorite_fill);
                currentLike = totalLike + 1;
                textTotalLike.setText(String.format("Total %s", String.valueOf(currentLike)));
                isLiked = true;
                totalLike++;
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
                imageLike.setImageResource(R.drawable.ic_favorite_outline);
                currentLike = totalLike - 1;
                textTotalLike.setText(String.format("Total %s", String.valueOf(currentLike)));
                isLiked = false;
                totalLike--;
                Snackbar.make(anchorLayot, R.string.review_unliked, Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onError() {
                Snackbar.make(anchorLayot, R.string.failed_unlike_review, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void gotoEmptyAccount() {
        Intent intent = new Intent(context, EmptyAccountActivity.class);
        context.startActivity(intent);
    }

    private void gotoUserList() {
        Intent intent = new Intent(context, LikedByActivity.class);
        intent.putExtra(Popularin.REVIEW_ID, reviewID);
        context.startActivity(intent);
    }
}
