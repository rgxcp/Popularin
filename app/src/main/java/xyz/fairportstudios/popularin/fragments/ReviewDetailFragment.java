package xyz.fairportstudios.popularin.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
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
import com.bumptech.glide.request.RequestOptions;
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
import xyz.fairportstudios.popularin.services.ConvertPixel;
import xyz.fairportstudios.popularin.services.ParseDate;
import xyz.fairportstudios.popularin.services.ParseStar;
import xyz.fairportstudios.popularin.statics.Popularin;
import xyz.fairportstudios.popularin.statics.TMDbAPI;

public class ReviewDetailFragment extends Fragment {
    // Variable untuk fitur onResume
    private Boolean isResumeFirsTime = true;

    // Variable member
    private Context context;
    private Boolean isLiked;
    private ImageView imageUserProfile;
    private ImageView imageFilmPoster;
    private ImageView imageReviewStar;
    private ImageView imageLike;
    private Integer userID;
    private Integer filmID;
    private Integer totalLike;
    private ProgressBar progressBar;
    private RelativeLayout anchorLayout;
    private ScrollView scrollView;
    private String filmTitle;
    private String filmYear;
    private String filmPoster;
    private TextView textUsername;
    private TextView textFilmTitleYear;
    private TextView textReviewDate;
    private TextView textReviewDetail;
    private TextView textLikeStatus;
    private TextView textTotalLike;
    private TextView textMessage;

    // Variable constructor
    private Integer reviewID;

    public ReviewDetailFragment(Integer reviewID) {
        this.reviewID = reviewID;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_review_detail, container, false);

        // Context
        context = getActivity();

        // Binding
        imageUserProfile = view.findViewById(R.id.image_frd_profile);
        imageFilmPoster = view.findViewById(R.id.image_frd_poster);
        imageReviewStar = view.findViewById(R.id.image_frd_star);
        imageLike = view.findViewById(R.id.image_frd_like);
        progressBar = view.findViewById(R.id.pbr_frd_layout);
        anchorLayout = view.findViewById(R.id.anchor_frd_layout);
        scrollView = view.findViewById(R.id.scroll_frd_layout);
        textUsername = view.findViewById(R.id.text_frd_username);
        textFilmTitleYear = view.findViewById(R.id.text_frd_title_year);
        textReviewDate = view.findViewById(R.id.text_frd_date);
        textReviewDetail = view.findViewById(R.id.text_frd_review);
        textLikeStatus = view.findViewById(R.id.text_frd_like_status);
        textTotalLike = view.findViewById(R.id.text_frd_total_like);
        textMessage = view.findViewById(R.id.text_frd_message);

        // Auth
        final Boolean isAuth = new Auth(context).isAuth();

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

    @Override
    public void onResume() {
        super.onResume();
        if (isResumeFirsTime) {
            // Mendapatkan informasi ulasan
            getReviewDetail();
            isResumeFirsTime = false;
        }
    }

    private SpannableString spannableFilmTitleYear() {
        String filmTitleYear = filmTitle + " " + filmYear;
        int startIndex = filmTitleYear.length() - 4;
        int endIndex = filmTitleYear.length();
        ConvertPixel convertPixel = new ConvertPixel(context);
        SpannableString spannableString = new SpannableString(filmTitleYear);
        AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(convertPixel.getDensity(13));
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(getResources().getColor(R.color.colorOnSurface));
        spannableString.setSpan(absoluteSizeSpan, startIndex, endIndex, 0);
        spannableString.setSpan(foregroundColorSpan, startIndex, endIndex, 0);
        return spannableString;
    }

    private void getReviewDetail() {
        ReviewDetailRequest reviewDetailRequest = new ReviewDetailRequest(context, reviewID);
        reviewDetailRequest.sendRequest(new ReviewDetailRequest.Callback() {
            @Override
            public void onSuccess(ReviewDetail reviewDetail) {
                // Like status
                isLiked = reviewDetail.getIs_liked();
                if (isLiked) {
                    textLikeStatus.setText(R.string.liked);
                    imageLike.setImageResource(R.drawable.ic_fill_heart);
                }

                // Parsing
                userID = reviewDetail.getUser_id();
                filmID = reviewDetail.getTmdb_id();
                totalLike = reviewDetail.getTotal_like();
                filmTitle = reviewDetail.getTitle();
                filmYear = new ParseDate().getYear(reviewDetail.getRelease_date());
                filmPoster = TMDbAPI.IMAGE + reviewDetail.getPoster();
                Integer reviewStar = new ParseStar().getStar(reviewDetail.getRating());
                String reviewDate = new ParseDate().getDateForHumans(reviewDetail.getReview_date());

                // Request gambar
                RequestOptions requestOptions = new RequestOptions()
                        .centerCrop()
                        .placeholder(R.color.colorSurface)
                        .error(R.color.colorSurface);

                // Isi
                textUsername.setText(reviewDetail.getUsername());
                textFilmTitleYear.setText(spannableFilmTitleYear());
                textReviewDate.setText(reviewDate);
                textReviewDetail.setText(reviewDetail.getReview_detail());
                textTotalLike.setText(String.format("Total %s", totalLike));
                imageReviewStar.setImageResource(reviewStar);
                Glide.with(context).load(reviewDetail.getProfile_picture()).apply(requestOptions).into(imageUserProfile);
                Glide.with(context).load(filmPoster).apply(requestOptions).into(imageFilmPoster);
                progressBar.setVisibility(View.GONE);
                scrollView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(String message) {
                progressBar.setVisibility(View.GONE);
                textMessage.setVisibility(View.VISIBLE);
                textMessage.setText(message);
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

    private void showFilmModal() {
        FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
        FilmModal filmModal = new FilmModal(filmID, filmTitle, filmYear, filmPoster);
        filmModal.show(fragmentManager, Popularin.FILM_STATUS_MODAL);
    }

    private void setLikeState(Boolean state) {
        isLiked = state;
        if (state) {
            totalLike++;
            textLikeStatus.setText(R.string.liked);
            imageLike.setImageResource(R.drawable.ic_fill_heart);
        } else {
            totalLike--;
            textLikeStatus.setText(R.string.like);
            imageLike.setImageResource(R.drawable.ic_outline_heart);
        }
        textTotalLike.setText(String.format("Total %s", totalLike));
    }

    private void likeReview() {
        LikeReviewRequest likeReviewRequest = new LikeReviewRequest(context, reviewID);
        likeReviewRequest.sendRequest(new LikeReviewRequest.Callback() {
            @Override
            public void onSuccess() {
                setLikeState(true);
            }

            @Override
            public void onError(String message) {
                Snackbar.make(anchorLayout, message, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void unlikeReview() {
        UnlikeReviewRequest unlikeReviewRequest = new UnlikeReviewRequest(context, reviewID);
        unlikeReviewRequest.sendRequest(new UnlikeReviewRequest.Callback() {
            @Override
            public void onSuccess() {
                setLikeState(false);
            }

            @Override
            public void onError(String message) {
                Snackbar.make(anchorLayout, message, Snackbar.LENGTH_LONG).show();
            }
        });
    }
}
