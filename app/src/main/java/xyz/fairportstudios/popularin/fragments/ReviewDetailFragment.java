package xyz.fairportstudios.popularin.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.activities.EmptyUserActivity;
import xyz.fairportstudios.popularin.activities.FilmDetailActivity;
import xyz.fairportstudios.popularin.activities.LikedByActivity;
import xyz.fairportstudios.popularin.activities.UserDetailActivity;
import xyz.fairportstudios.popularin.apis.popularin.delete.UnlikeReviewRequest;
import xyz.fairportstudios.popularin.apis.popularin.get.ReviewDetailRequest;
import xyz.fairportstudios.popularin.apis.popularin.post.LikeReviewRequest;
import xyz.fairportstudios.popularin.models.ReviewDetail;
import xyz.fairportstudios.popularin.preferences.Auth;

public class ReviewDetailFragment extends Fragment {
    private Boolean isAuth;
    private Boolean isLiked;
    private Context context;
    private CoordinatorLayout layout;
    private ImageView userProfile;
    private ImageView filmPoster;
    private ImageView star;
    private ImageView like;
    private Integer currentLikes;
    private Integer likes;
    private ProgressBar progressBar;
    private ScrollView scrollView;
    private String userID;
    private String filmID;
    private String reviewID;
    private String title;
    private String year;
    private TextView userFirstName;
    private TextView filmTitle;
    private TextView filmYear;
    private TextView reviewDate;
    private TextView reviewText;
    private TextView likeStatus;
    private TextView totalLike;
    private TextView emptyResult;

    public ReviewDetailFragment(String reviewID) {
        this.reviewID = reviewID;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_review_detail, container, false);

        // Binding
        context = getActivity();
        layout = view.findViewById(R.id.layout_frd_anchor);
        userProfile = view.findViewById(R.id.image_frd_profile);
        filmPoster = view.findViewById(R.id.image_frd_poster);
        star = view.findViewById(R.id.image_frd_star);
        like = view.findViewById(R.id.icon_frd_like);
        progressBar = view.findViewById(R.id.pbr_frd_layout);
        scrollView = view.findViewById(R.id.scroll_frd_anchor);
        userFirstName = view.findViewById(R.id.text_frd_first_name);
        filmTitle = view.findViewById(R.id.text_frd_title);
        filmYear = view.findViewById(R.id.text_frd_year);
        reviewDate = view.findViewById(R.id.text_frd_date);
        reviewText = view.findViewById(R.id.text_frd_detail);
        likeStatus = view.findViewById(R.id.text_frd_like_status);
        totalLike = view.findViewById(R.id.text_frd_total_like);
        emptyResult = view.findViewById(R.id.text_frd_empty);

        // Auth
        isAuth = new Auth(context).isAuth();

        // GET
        ReviewDetailRequest reviewDetailRequest = new ReviewDetailRequest(context, reviewID);
        reviewDetailRequest.sendRequest(new ReviewDetailRequest.APICallback() {
            @Override
            public void onSuccess(ReviewDetail reviewDetail) {
                // Like status
                isLiked = reviewDetail.getLikeStatus();

                if (isLiked) {
                    like.setImageResource(R.drawable.ic_favorite_filled);
                    likeStatus.setText(R.string.liked);
                }

                // Parsing
                title = reviewDetail.getFilmTitle();
                year = reviewDetail.getFilmYear();

                // Request gambar
                RequestOptions requestOptions = new RequestOptions().centerCrop().placeholder(R.color.colorPrimary).error(R.color.colorPrimary);

                // Mengisi data
                likes = reviewDetail.getLike();
                filmID = String.valueOf(reviewDetail.getFilmID());
                userID = reviewDetail.getUserID();
                userFirstName.setText(reviewDetail.getUserFirstName());
                filmTitle.setText(title);
                filmYear.setText(year);
                reviewDate.setText(reviewDetail.getReviewDate());
                reviewText.setText(reviewDetail.getReviewText());
                totalLike.setText(String.format("Total %s", String.valueOf(likes)));
                star.setImageResource(reviewDetail.getStar());
                Glide.with(Objects.requireNonNull(context)).load(reviewDetail.getUserProfilePicture()).apply(requestOptions).into(userProfile);
                Glide.with(Objects.requireNonNull(context)).load(reviewDetail.getFilmPoster()).apply(requestOptions).into(filmPoster);

                progressBar.setVisibility(View.GONE);
                scrollView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError() {
                progressBar.setVisibility(View.GONE);
                emptyResult.setVisibility(View.VISIBLE);
                Snackbar.make(layout, R.string.get_error, Snackbar.LENGTH_LONG).show();
            }
        });

        // Activity
        userProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoUserDetail = new Intent(context, UserDetailActivity.class);
                gotoUserDetail.putExtra("USER_ID", userID);
                context.startActivity(gotoUserDetail);
            }
        });

        filmPoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoFilmDetail = new Intent(context, FilmDetailActivity.class);
                gotoFilmDetail.putExtra("FILM_ID", filmID);
                context.startActivity(gotoFilmDetail);
            }
        });

        filmPoster.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
                FilmStatusModal filmStatusModal = new FilmStatusModal(filmID, title, year);
                filmStatusModal.show(fragmentManager, "FILM_STATUS_MODAL");
                return true;
            }
        });

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAuth) {
                    if (!isLiked) {
                        // POST
                        LikeReviewRequest likeReviewRequest = new LikeReviewRequest(context, reviewID);
                        likeReviewRequest.sendRequest(new LikeReviewRequest.APICallback() {
                            @Override
                            public void onSuccess() {
                                like.setImageResource(R.drawable.ic_favorite_filled);
                                currentLikes = likes + 1;
                                totalLike.setText(String.format("Total %s", String.valueOf(currentLikes)));
                                isLiked = true;
                                likes++;
                                Snackbar.make(layout, R.string.review_liked, Snackbar.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError() {
                                Snackbar.make(layout, R.string.like_error, Snackbar.LENGTH_LONG).show();
                            }
                        });
                    } else {
                        // DELETE
                        UnlikeReviewRequest unlikeReviewRequest = new UnlikeReviewRequest(context, reviewID);
                        unlikeReviewRequest.sendRequest(new UnlikeReviewRequest.APICallback() {
                            @Override
                            public void onSuccess() {
                                like.setImageResource(R.drawable.ic_favorite_blank);
                                currentLikes = likes - 1;
                                totalLike.setText(String.format("Total %s", String.valueOf(currentLikes)));
                                isLiked = false;
                                likes--;
                                Snackbar.make(layout, R.string.review_unliked, Snackbar.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError() {
                                Snackbar.make(layout, R.string.unlike_error, Snackbar.LENGTH_LONG).show();
                            }
                        });
                    }
                } else {
                    Intent gotoEmptyUser = new Intent(context, EmptyUserActivity.class);
                    startActivity(gotoEmptyUser);
                }
            }
        });

        totalLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoUserList = new Intent(context, LikedByActivity.class);
                gotoUserList.putExtra("REVIEW_ID", reviewID);
                startActivity(gotoUserList);
            }
        });

        return view;
    }
}
