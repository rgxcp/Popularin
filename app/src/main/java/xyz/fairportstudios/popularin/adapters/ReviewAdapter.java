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
import xyz.fairportstudios.popularin.activities.ReviewDetailActivity;
import xyz.fairportstudios.popularin.activities.UserDetailActivity;
import xyz.fairportstudios.popularin.fragments.FilmStatusModal;
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
        final String reviewID = String.valueOf(reviewList.get(position).getId());
        final String userID = String.valueOf(reviewList.get(position).getUser_id());
        final String filmID = String.valueOf(reviewList.get(position).getTmdb_id());

        // Auth
        final String authID = new Auth(context).getAuthID();
        final boolean isSelf = userID.equals(authID);

        // Request gambar
        RequestOptions requestOptions = new RequestOptions()
                .centerCrop()
                .placeholder(R.color.colorPrimary)
                .error(R.color.colorPrimary);

        // Parsing
        final String title = reviewList.get(position).getTitle();
        final String year = new ParseDate().getYear(reviewList.get(position).getRelease_date());
        final String poster = new ParseImage().getImage(reviewList.get(position).getPoster());
        Integer star = new ParseStar().getStar(reviewList.get(position).getRating());

        // Mengisi data
        holder.filmTitle.setText(title);
        holder.filmYear.setText(year);
        holder.userFirstName.setText(reviewList.get(position).getFirst_name());
        holder.reviewDetail.setText(reviewList.get(position).getReview_detail());
        holder.reviewTimestamp.setText(reviewList.get(position).getTimestamp());
        holder.reviewStar.setImageResource(star);
        Glide.with(context).load(reviewList.get(position).getProfile_picture()).apply(requestOptions).into(holder.userProfile);
        Glide.with(context).load(poster).apply(requestOptions).into(holder.filmPoster);

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
                Intent intent = new Intent(context, ReviewDetailActivity.class);
                intent.putExtra(Popularin.REVIEW_ID, reviewID);
                intent.putExtra(Popularin.IS_SELF, isSelf);
                context.startActivity(intent);
            }
        });

        holder.userProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UserDetailActivity.class);
                intent.putExtra(Popularin.USER_ID, userID);
                context.startActivity(intent);
            }
        });

        holder.filmPoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, FilmDetailActivity.class);
                intent.putExtra(Popularin.FILM_ID, filmID);
                context.startActivity(intent);
            }
        });

        holder.filmPoster.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
                FilmStatusModal filmStatusModal = new FilmStatusModal(filmID, title, year, poster);
                filmStatusModal.show(fragmentManager, Popularin.FILM_STATUS_MODAL);
                return true;
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
        TextView reviewTimestamp;
        View border;

        ReviewViewHolder(@NonNull View itemView) {
            super(itemView);

            userProfile = itemView.findViewById(R.id.image_rr_profile);
            reviewStar = itemView.findViewById(R.id.image_rr_star);
            filmPoster = itemView.findViewById(R.id.image_rr_poster);
            filmTitle = itemView.findViewById(R.id.text_rr_title);
            filmYear = itemView.findViewById(R.id.text_rr_year);
            userFirstName = itemView.findViewById(R.id.text_rr_first_name);
            reviewDetail = itemView.findViewById(R.id.text_rr_review);
            reviewTimestamp = itemView.findViewById(R.id.text_rr_timestamp);
            border = itemView.findViewById(R.id.border_rr_layout);
        }
    }
}
