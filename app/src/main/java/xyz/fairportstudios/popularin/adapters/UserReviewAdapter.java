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
import xyz.fairportstudios.popularin.activities.ReviewDetailActivity;
import xyz.fairportstudios.popularin.models.UserReview;
import xyz.fairportstudios.popularin.services.ParseDate;
import xyz.fairportstudios.popularin.services.ParseImage;
import xyz.fairportstudios.popularin.services.ParseStar;

public class UserReviewAdapter extends RecyclerView.Adapter<UserReviewAdapter.UserReviewViewHolder> {
    private Context context;
    private List<UserReview> userReviewList;

    public UserReviewAdapter(Context context, List<UserReview> userReviewList) {
        this.context = context;
        this.userReviewList = userReviewList;
    }

    private Integer dpToPx() {
        float px = 16 * context.getResources().getDisplayMetrics().density;
        return (int) px;
    }

    @NonNull
    @Override
    public UserReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserReviewViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_user_review, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserReviewViewHolder holder, int position) {
        // Review ID
        final String reviewID = String.valueOf(userReviewList.get(position).getId());

        // Film ID
        final String filmID = String.valueOf(userReviewList.get(position).getTmdb_id());

        // Request gambar
        RequestOptions requestOptions = new RequestOptions().centerCrop().placeholder(R.color.colorPrimary).error(R.color.colorPrimary);

        // Parsing
        Integer star = new ParseStar().getStar(userReviewList.get(position).getRating());
        String year = new ParseDate().getYear(userReviewList.get(position).getRelease_date());
        String date = new ParseDate().getDate(userReviewList.get(position).getReview_date());
        String poster = new ParseImage().getPoster(userReviewList.get(position).getPoster());

        // Mengisi data
        holder.filmTitle.setText(userReviewList.get(position).getTitle());
        holder.filmYear.setText(year);
        holder.reviewDate.setText(date);
        holder.reviewDetail.setText(userReviewList.get(position).getReview_text());
        holder.reviewStar.setImageResource(star);
        Glide.with(context).load(poster).apply(requestOptions).into(holder.filmPoster);

        // Margin
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) holder.itemView.getLayoutParams();
        if (position == userReviewList.size() - 1) {
            layoutParams.bottomMargin = dpToPx();
            holder.border.setVisibility(View.GONE);
        }
        holder.itemView.setLayoutParams(layoutParams);

        // Activity
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoReviewDetail = new Intent(context, ReviewDetailActivity.class);
                gotoReviewDetail.putExtra("REVIEW_ID", reviewID);
                context.startActivity(gotoReviewDetail);
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

        holder.filmPoster.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return userReviewList.size();
    }

    static class UserReviewViewHolder extends RecyclerView.ViewHolder {
        ImageView reviewStar;
        ImageView filmPoster;
        TextView filmTitle;
        TextView filmYear;
        TextView reviewDate;
        TextView reviewDetail;
        View border;

        UserReviewViewHolder(@NonNull View itemView) {
            super(itemView);

            reviewStar = itemView.findViewById(R.id.image_rur_star);
            filmPoster = itemView.findViewById(R.id.image_rur_poster);
            filmTitle = itemView.findViewById(R.id.text_rur_title);
            filmYear = itemView.findViewById(R.id.text_rur_year);
            reviewDate = itemView.findViewById(R.id.text_rur_date);
            reviewDetail = itemView.findViewById(R.id.text_rur_review);
            border = itemView.findViewById(R.id.border_rur_layout);
        }
    }
}
