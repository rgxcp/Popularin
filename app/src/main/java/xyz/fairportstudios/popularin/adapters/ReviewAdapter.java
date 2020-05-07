package xyz.fairportstudios.popularin.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.activities.ReviewActivity;
import xyz.fairportstudios.popularin.activities.UserDetailActivity;
import xyz.fairportstudios.popularin.models.Review;
import xyz.fairportstudios.popularin.services.ParseDate;
import xyz.fairportstudios.popularin.services.ParseImage;
import xyz.fairportstudios.popularin.services.ParseStar;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    private Context context;
    private List<Review> reviewList;

    public ReviewAdapter(Context context, List<Review> reviewList) {
        this.context = context;
        this.reviewList = reviewList;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ReviewViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_view_review, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        // Review ID
        final String reviewID = String.valueOf(reviewList.get(position).getId());

        // Film ID
        final String filmID = String.valueOf(reviewList.get(position).getTmdb_id());

        // User ID
        final String userID = String.valueOf(reviewList.get(position).getUser_id());

        // Request gambar
        RequestOptions requestOptions = new RequestOptions().centerCrop().placeholder(R.color.colorPrimary).error(R.color.colorPrimary);

        // Parsing
        Integer star = new ParseStar().getStar(reviewList.get(position).getRating());
        String year = new ParseDate().getYear(reviewList.get(position).getRelease_date());
        String poster = new ParseImage().getPoster(reviewList.get(position).getPoster());

        // Mengisi data
        holder.filmTitle.setText(reviewList.get(position).getTitle());
        holder.filmYear.setText(year);
        holder.userFirstName.setText(reviewList.get(position).getFirst_name());
        holder.reviewDetail.setText(reviewList.get(position).getReview_text());
        holder.reviewStar.setImageResource(star);
        Glide.with(context).load(reviewList.get(position).getProfile_picture()).apply(requestOptions).into(holder.userProfile);
        Glide.with(context).load(poster).apply(requestOptions).into(holder.filmPoster);

        // Activity
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoReviewDetail = new Intent(context, ReviewActivity.class);
                gotoReviewDetail.putExtra("REVIEW_ID", reviewID);
                context.startActivity(gotoReviewDetail);
            }
        });

        holder.userProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoUserDetail = new Intent(context, UserDetailActivity.class);
                gotoUserDetail.putExtra("USER_ID", userID);
                context.startActivity(gotoUserDetail);
            }
        });

        holder.filmPoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                Intent gotoFilmDetail = new Intent(context, FilmDetailActivity.class);
                gotoFilmDetail.putExtra("FILM_ID", filmID);
                context.startActivity(gotoFilmDetail);
                 */
            }
        });
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    static class ReviewViewHolder extends RecyclerView.ViewHolder {
        ImageView userProfile;
        ImageView reviewStar;
        ImageView filmPoster;
        TextView filmTitle;
        TextView filmYear;
        TextView userFirstName;
        TextView reviewDetail;

        ReviewViewHolder(@NonNull View itemView) {
            super(itemView);

            userProfile = itemView.findViewById(R.id.image_rvr_profile);
            reviewStar = itemView.findViewById(R.id.image_rvr_star);
            filmPoster = itemView.findViewById(R.id.image_rvr_poster);
            filmTitle = itemView.findViewById(R.id.text_rvr_title);
            filmYear = itemView.findViewById(R.id.text_rvr_year);
            userFirstName = itemView.findViewById(R.id.text_rvr_first_name);
            reviewDetail = itemView.findViewById(R.id.text_rvr_review);
        }
    }
}
