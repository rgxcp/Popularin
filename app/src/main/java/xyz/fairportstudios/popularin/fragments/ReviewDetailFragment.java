package xyz.fairportstudios.popularin.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.activities.EmptyUserActivity;
import xyz.fairportstudios.popularin.activities.UserDetailActivity;
import xyz.fairportstudios.popularin.activities.UserListActivity;
import xyz.fairportstudios.popularin.apis.popularin.delete.UnlikeComment;
import xyz.fairportstudios.popularin.apis.popularin.get.ReviewDetail;
import xyz.fairportstudios.popularin.apis.popularin.post.LikeComment;
import xyz.fairportstudios.popularin.preferences.Auth;
import xyz.fairportstudios.popularin.services.ParseDate;
import xyz.fairportstudios.popularin.services.ParseImage;
import xyz.fairportstudios.popularin.services.ParseStar;

public class ReviewDetailFragment extends Fragment {
    private Boolean isAuth;
    private Boolean isLiked;
    private Context context;
    private ImageView userProfile, filmPoster, reviewStar, iconLike;
    private Integer currentLikes;
    private Integer likes;
    private String userID, filmID, reviewID;
    private TextView userFirstName, filmTitle, filmYear, reviewDate, reviewText, likeStatus, totalLike;

    public ReviewDetailFragment(String reviewID) {
        this.reviewID = reviewID;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_review_detail, container, false);

        // Binding
        context = getActivity();
        userProfile = view.findViewById(R.id.image_frd_profile);
        filmPoster = view.findViewById(R.id.image_frd_poster);
        reviewStar = view.findViewById(R.id.image_frd_star);
        iconLike = view.findViewById(R.id.icon_frd_like);
        userFirstName = view.findViewById(R.id.text_frd_first_name);
        filmTitle = view.findViewById(R.id.text_frd_film_title);
        filmYear = view.findViewById(R.id.text_frd_film_year);
        reviewDate = view.findViewById(R.id.text_frd_date);
        reviewText = view.findViewById(R.id.text_frd_detail);
        likeStatus = view.findViewById(R.id.text_frd_like_status);
        totalLike = view.findViewById(R.id.text_frd_total_like);

        // Mengecek apakah sign in
        isAuth = new Auth(context).isAuth();

        // Mengirim data
        ReviewDetail reviewDetail = new ReviewDetail(reviewID, context);

        // Mendapatkan hasil
        reviewDetail.sendRequest(new ReviewDetail.JSONCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    int status = response.getInt("status");

                    if (status == 101) {
                        // JSON
                        JSONObject jsonObjectResult = response.getJSONObject("result");
                        JSONObject jsonObjectReview = jsonObjectResult.getJSONObject("review");
                        JSONObject jsonObjectMetadata = jsonObjectResult.getJSONObject("metadata");
                        JSONObject jsonObjectFilm = jsonObjectReview.getJSONObject("film");
                        JSONObject jsonObjectUser = jsonObjectReview.getJSONObject("user");

                        // Request gambar
                        RequestOptions requestOptions = new RequestOptions().centerCrop().placeholder(R.color.colorPrimary).error(R.color.colorPrimary);

                        // Parsing
                        Integer star = new ParseStar().getStar(jsonObjectReview.getDouble("rating"));
                        String year = new ParseDate().getYear(jsonObjectFilm.getString("release_date"));
                        String date = new ParseDate().getDate(jsonObjectReview.getString("review_date"));

                        // Like status
                        isLiked = jsonObjectMetadata.getBoolean("liked");

                        if (isLiked) {
                            iconLike.setImageResource(R.drawable.ic_favorite_filled);
                            likeStatus.setText(R.string.liked);
                        }

                        // Parsing
                        String poster = new ParseImage().getPoster(jsonObjectFilm.getString("poster"));

                        // Detail
                        likes = jsonObjectMetadata.getInt("likes");
                        userID = jsonObjectUser.getString("id");
                        userFirstName.setText(jsonObjectUser.getString("first_name"));
                        filmTitle.setText(jsonObjectFilm.getString("title"));
                        filmYear.setText(year);
                        reviewDate.setText(date);
                        reviewText.setText(jsonObjectReview.getString("review_text"));
                        totalLike.setText(String.format("Total %s", String.valueOf(likes)));
                        reviewStar.setImageResource(star);
                        Glide.with(Objects.requireNonNull(context)).load(jsonObjectUser.getString("profile_picture")).apply(requestOptions).into(userProfile);
                        Glide.with(Objects.requireNonNull(context)).load(poster).apply(requestOptions).into(filmPoster);
                    } else {
                        Toast.makeText(getActivity(), "Ada kesalahan dalam database.", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
                /*
                Intent gotoFilmDetail = new Intent(context, FilmDetailActivity.class);
                gotoFilmDetail.putExtra("FILM_ID", filmID);
                context.startActivity(gotoFilmDetail);
                 */
            }
        });

        iconLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAuth) {
                    if (isLiked) {
                        UnlikeComment unlikeComment = new UnlikeComment(context, reviewID);
                        unlikeComment.sendRequest(new UnlikeComment.JSONCallback() {
                            @Override
                            public void onSuccess(Integer status) {
                                if (status == 404) {
                                    iconLike.setImageResource(R.drawable.ic_favorite_blank);
                                    currentLikes = likes - 1;
                                    totalLike.setText(String.format("Total %s", String.valueOf(currentLikes)));
                                    isLiked = false;
                                    likes--;
                                    Toast.makeText(context, "Ulasan tidak disukai.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, "Ada kesalahan dalam database.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        LikeComment likeComment = new LikeComment(context, reviewID);
                        likeComment.sendRequest(new LikeComment.JSONCallback() {
                            @Override
                            public void onSuccess(Integer status) {
                                if (status == 202) {
                                    iconLike.setImageResource(R.drawable.ic_favorite_filled);
                                    currentLikes = likes + 1;
                                    totalLike.setText(String.format("Total %s", String.valueOf(currentLikes)));
                                    isLiked = true;
                                    likes++;
                                    Toast.makeText(context, "Ulasan disukai.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, "Ada kesalahan dalam database.", Toast.LENGTH_SHORT).show();
                                }
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
                Intent gotoUserList = new Intent(context, UserListActivity.class);
                gotoUserList.putExtra("REVIEW_ID", reviewID);
                startActivity(gotoUserList);
            }
        });

        return view;
    }
}
