package xyz.fairportstudios.popularin.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import xyz.fairportstudios.popularin.apis.popularin.ReviewDetailRequest;
import xyz.fairportstudios.popularin.services.ParseDate;
import xyz.fairportstudios.popularin.services.ParseStar;

public class ReviewDetailFragment extends Fragment {
    private Boolean isLiked;
    private Context context;
    private ImageView userProfile, filmPoster, reviewStar, iconLike;
    private String userID, filmID, reviewID;
    private TextView userFirstName, filmTitle, filmYear, reviewDate, reviewDetail, likeStatus, totalLike;

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
        reviewDetail = view.findViewById(R.id.text_frd_detail);
        likeStatus = view.findViewById(R.id.text_frd_like_status);
        totalLike = view.findViewById(R.id.text_frd_total_like);

        // Mengirim data
        ReviewDetailRequest reviewDetailRequest = new ReviewDetailRequest(context, reviewID);

        // Mendapatkan hasil
        reviewDetailRequest.sendRequest(new ReviewDetailRequest.JSONCallback() {
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

                        // Detail
                        userFirstName.setText(jsonObjectUser.getString("first_name"));
                        filmTitle.setText(jsonObjectFilm.getString("title"));
                        filmYear.setText(year);
                        reviewDate.setText(date);
                        reviewDetail.setText(jsonObjectReview.getString("review_text"));
                        totalLike.setText(String.format("Total %s", String.valueOf(jsonObjectMetadata.getInt("likes"))));
                        reviewStar.setImageResource(star);
                        Glide.with(Objects.requireNonNull(context)).load(jsonObjectUser.getString("profile_picture")).apply(requestOptions).into(userProfile);
                        Glide.with(Objects.requireNonNull(context)).load(jsonObjectFilm.getString("poster")).apply(requestOptions).into(filmPoster);
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
                /*
                Intent gotoUserDetail = new Intent(context, UserDetailActivity.class);
                gotoUserDetail.putExtra("USER_ID", userID);
                context.startActivity(gotoUserDetail);
                 */
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
                /*
                if (isLiked) {
                    // Like
                    // Like + 1
                } else {
                    // Remove Like
                    // Like - 1
                }
                 */
            }
        });

        return view;
    }
}
