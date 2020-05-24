package xyz.fairportstudios.popularin.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.activities.FilmDetailActivity;
import xyz.fairportstudios.popularin.activities.ReviewActivity;
import xyz.fairportstudios.popularin.modals.FilmModal;
import xyz.fairportstudios.popularin.models.UserReview;
import xyz.fairportstudios.popularin.preferences.Auth;
import xyz.fairportstudios.popularin.services.ParseDate;
import xyz.fairportstudios.popularin.services.ParseImage;
import xyz.fairportstudios.popularin.services.ParseStar;

public class UserReviewAdapter extends RecyclerView.Adapter<UserReviewAdapter.UserReviewViewHolder> {
    private Context context;
    private String userID;
    private List<UserReview> userReviewList;

    public UserReviewAdapter(Context context, String userID, List<UserReview> userReviewList) {
        this.context = context;
        this.userID = userID;
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

        // Auth
        final String authID = new Auth(context).getAuthID();
        final boolean isSelf = userID.equals(authID);

        // Request gambar
        RequestOptions requestOptions = new RequestOptions().centerCrop().placeholder(R.color.colorPrimary).error(R.color.colorPrimary);

        // Parsing
        final String title = userReviewList.get(position).getTitle();
        final String year = new ParseDate().getYear(userReviewList.get(position).getRelease_date());
        final String poster = new ParseImage().getImage(userReviewList.get(position).getPoster());
        Integer star = new ParseStar().getStar(userReviewList.get(position).getRating());
        String date = new ParseDate().getDate(userReviewList.get(position).getReview_date());

        // Mengisi data
        holder.filmTitle.setText(title);
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
                Intent gotoReviewDetail = new Intent(context, ReviewActivity.class);
                gotoReviewDetail.putExtra("REVIEW_ID", reviewID);
                gotoReviewDetail.putExtra("IS_SELF", isSelf);
                context.startActivity(gotoReviewDetail);
            }
        });

        holder.filmPoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoFilmDetail = new Intent(context, FilmDetailActivity.class);
                gotoFilmDetail.putExtra("FILM_ID", filmID);
                context.startActivity(gotoFilmDetail);
            }
        });

        holder.filmPoster.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
                FilmModal filmModal = new FilmModal(filmID, title, year, poster);
                filmModal.show(fragmentManager, "FILM_STATUS_MODAL");
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
