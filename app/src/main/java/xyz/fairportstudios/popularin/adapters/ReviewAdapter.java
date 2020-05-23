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
import xyz.fairportstudios.popularin.activities.UserDetailActivity;
import xyz.fairportstudios.popularin.modals.FilmModal;
import xyz.fairportstudios.popularin.models.Review;
import xyz.fairportstudios.popularin.preferences.Auth;
import xyz.fairportstudios.popularin.services.ParseDate;
import xyz.fairportstudios.popularin.services.ParseImage;
import xyz.fairportstudios.popularin.services.ParseStar;
import xyz.fairportstudios.popularin.services.Popularin;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    private Context context;
    private List<Review> reviewList;

    public ReviewAdapter(Context context, List<Review> reviewList) {
        this.context = context;
        this.reviewList = reviewList;
    }

    private Integer dpToPx() {
        float px = 16 * context.getResources().getDisplayMetrics().density;
        return (int) px;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ReviewViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_review, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        // ID
        final String reviewID = String.valueOf(reviewList.get(position).getReview_id());
        final String filmID = String.valueOf(reviewList.get(position).getTmdb_id());
        final String userID = String.valueOf(reviewList.get(position).getUser_id());

        // Auth
        final boolean isSelf = userID.equals(new Auth(context).getAuthID());

        // Parsing
        final String filmTitle = reviewList.get(position).getTitle();
        final String filmYear = new ParseDate().getYear(reviewList.get(position).getRelease_date());
        final String filmPoster = new ParseImage().getImage(reviewList.get(position).getPoster());
        final Integer reviewStar = new ParseStar().getStar(reviewList.get(position).getRating());

        // Request gambar
        RequestOptions requestOptions = new RequestOptions()
                .centerCrop()
                .placeholder(R.color.colorSurface)
                .error(R.color.colorSurface);

        // Isi
        holder.textFilmTitle.setText(filmTitle);
        holder.textFilmYear.setText(filmYear);
        holder.textUsername.setText(reviewList.get(position).getUsername());
        holder.textReviewDetail.setText(reviewList.get(position).getReview_detail());
        holder.textReviewTimestamp.setText(reviewList.get(position).getTimestamp());
        holder.imageReviewStar.setImageResource(reviewStar);
        Glide.with(context).load(reviewList.get(position).getProfile_picture()).apply(requestOptions).into(holder.imageUserProfile);
        Glide.with(context).load(filmPoster).apply(requestOptions).into(holder.imageFilmPoster);

        // Margin
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) holder.itemView.getLayoutParams();
        if (position == reviewList.size() - 1) {
            layoutParams.bottomMargin = dpToPx();
            holder.border.setVisibility(View.GONE);
        }
        holder.itemView.setLayoutParams(layoutParams);

        // Activity
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ReviewActivity.class);
                intent.putExtra(Popularin.REVIEW_ID, reviewID);
                intent.putExtra(Popularin.IS_SELF, isSelf);
                context.startActivity(intent);
            }
        });

        holder.imageUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UserDetailActivity.class);
                intent.putExtra(Popularin.USER_ID, userID);
                context.startActivity(intent);
            }
        });

        holder.imageFilmPoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, FilmDetailActivity.class);
                intent.putExtra(Popularin.FILM_ID, filmID);
                context.startActivity(intent);
            }
        });

        holder.imageFilmPoster.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
                FilmModal filmModal = new FilmModal(filmID, filmTitle, filmYear, filmPoster);
                filmModal.show(fragmentManager, Popularin.FILM_STATUS_MODAL);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    static class ReviewViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageUserProfile;
        private ImageView imageReviewStar;
        private ImageView imageFilmPoster;
        private TextView textFilmTitle;
        private TextView textFilmYear;
        private TextView textUsername;
        private TextView textReviewDetail;
        private TextView textReviewTimestamp;
        private View border;

        ReviewViewHolder(@NonNull View itemView) {
            super(itemView);

            imageUserProfile = itemView.findViewById(R.id.image_rr_profile);
            imageReviewStar = itemView.findViewById(R.id.image_rr_star);
            imageFilmPoster = itemView.findViewById(R.id.image_rr_poster);
            textFilmTitle = itemView.findViewById(R.id.text_rr_title);
            textFilmYear = itemView.findViewById(R.id.text_rr_year);
            textUsername = itemView.findViewById(R.id.text_rr_username);
            textReviewDetail = itemView.findViewById(R.id.text_rr_review);
            textReviewTimestamp = itemView.findViewById(R.id.text_rr_timestamp);
            border = itemView.findViewById(R.id.layout_rr_border);
        }
    }
}
