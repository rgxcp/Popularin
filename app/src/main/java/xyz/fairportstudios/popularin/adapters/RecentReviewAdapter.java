package xyz.fairportstudios.popularin.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.activities.ReviewDetailActivity;
import xyz.fairportstudios.popularin.fragments.FilmStatusModal;
import xyz.fairportstudios.popularin.models.RecentReview;
import xyz.fairportstudios.popularin.preferences.Auth;
import xyz.fairportstudios.popularin.services.ParseDate;
import xyz.fairportstudios.popularin.services.ParseImage;
import xyz.fairportstudios.popularin.services.ParseStar;

public class RecentReviewAdapter extends RecyclerView.Adapter<RecentReviewAdapter.LatestReviewViewHolder> {
    private Context context;
    private String userID;
    private List<RecentReview> recentReviewList;

    public RecentReviewAdapter(Context context, String userID, List<RecentReview> recentReviewList) {
        this.context = context;
        this.userID = userID;
        this.recentReviewList = recentReviewList;
    }

    private Integer dpToPx(Integer dp) {
        float px = dp * context.getResources().getDisplayMetrics().density;
        return (int) px;
    }

    @NonNull
    @Override
    public LatestReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LatestReviewViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_latest_review, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LatestReviewViewHolder holder, int position) {
        // Review ID
        final String reviewID = String.valueOf(recentReviewList.get(position).getId());

        // Auth
        final String authID = new Auth(context).getAuthID();
        final boolean isSelf = userID.equals(authID);

        // Request gambar
        RequestOptions requestOptions = new RequestOptions().centerCrop().placeholder(R.color.colorPrimary).error(R.color.colorPrimary);

        // Parsing
        final String filmID = String.valueOf(recentReviewList.get(position).getTmdb_id());
        final String title = recentReviewList.get(position).getTitle();
        final String year = new ParseDate().getYear(recentReviewList.get(position).getRelease_date());
        final String poster = new ParseImage().getImage(recentReviewList.get(position).getPoster());
        Integer star = new ParseStar().getStar(recentReviewList.get(position).getRating());

        // Mengisi data
        holder.reviewStar.setImageResource(star);
        Glide.with(context).load(poster).apply(requestOptions).into(holder.filmPoster);

        // Margin
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) holder.itemView.getLayoutParams();
        if (position == 0 ) {
            layoutParams.leftMargin = dpToPx(16);
            layoutParams.rightMargin = dpToPx(8);
        } else if (position == recentReviewList.size() - 1) {
            layoutParams.rightMargin = dpToPx(16);
        } else {
            layoutParams.rightMargin = dpToPx(8);
        }
        holder.itemView.setLayoutParams(layoutParams);

        // Activity
        holder.filmPoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoReviewDetail = new Intent(context, ReviewDetailActivity.class);
                gotoReviewDetail.putExtra("REVIEW_ID", reviewID);
                gotoReviewDetail.putExtra("IS_SELF", isSelf);
                context.startActivity(gotoReviewDetail);
            }
        });

        holder.filmPoster.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
                FilmStatusModal filmStatusModal = new FilmStatusModal(filmID, title, year, poster);
                filmStatusModal.show(fragmentManager, "FILM_STATUS_MODAL");
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return recentReviewList.size();
    }

    static class LatestReviewViewHolder extends RecyclerView.ViewHolder {
        ImageView filmPoster;
        ImageView reviewStar;

        LatestReviewViewHolder(@NonNull View itemView) {
            super(itemView);

            filmPoster = itemView.findViewById(R.id.image_rlr_poster);
            reviewStar = itemView.findViewById(R.id.image_rlr_star);
        }
    }
}
