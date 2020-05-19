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
import xyz.fairportstudios.popularin.services.Popularin;

public class RecentReviewAdapter extends RecyclerView.Adapter<RecentReviewAdapter.RecentReviewViewHolder> {
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
    public RecentReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecentReviewViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_recent_review, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecentReviewViewHolder holder, int position) {
        // Review ID
        final String reviewID = String.valueOf(recentReviewList.get(position).getId());

        // Film ID
        final String filmID = String.valueOf(recentReviewList.get(position).getTmdb_id());

        // Auth
        final String authID = new Auth(context).getAuthID();
        final boolean isSelf = userID.equals(authID);

        // Request gambar
        RequestOptions requestOptions = new RequestOptions()
                .centerCrop()
                .placeholder(R.color.colorPrimary)
                .error(R.color.colorPrimary);

        // Mendapatkan data
        final String title = recentReviewList.get(position).getTitle();
        final String year = new ParseDate().getYear(recentReviewList.get(position).getRelease_date());
        final String poster = new ParseImage().getImage(recentReviewList.get(position).getPoster());
        Integer star = new ParseStar().getStar(recentReviewList.get(position).getRating());
        holder.reviewStar.setImageResource(star);
        Glide.with(context).load(poster).apply(requestOptions).into(holder.filmPoster);

        // Margin
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) holder.itemView.getLayoutParams();
        if (position == 0) {
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
                gotoReviewDetail.putExtra(Popularin.REVIEW_ID, reviewID);
                gotoReviewDetail.putExtra(Popularin.IS_SELF, isSelf);
                context.startActivity(gotoReviewDetail);
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
        return recentReviewList.size();
    }

    static class RecentReviewViewHolder extends RecyclerView.ViewHolder {
        ImageView filmPoster;
        ImageView reviewStar;

        RecentReviewViewHolder(@NonNull View itemView) {
            super(itemView);

            filmPoster = itemView.findViewById(R.id.image_rrr_poster);
            reviewStar = itemView.findViewById(R.id.image_rrr_star);
        }
    }
}
