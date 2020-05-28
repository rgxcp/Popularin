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
import xyz.fairportstudios.popularin.activities.ReviewActivity;
import xyz.fairportstudios.popularin.modals.FilmModal;
import xyz.fairportstudios.popularin.models.RecentReview;
import xyz.fairportstudios.popularin.preferences.Auth;
import xyz.fairportstudios.popularin.services.ParseDate;
import xyz.fairportstudios.popularin.services.ParseImage;
import xyz.fairportstudios.popularin.services.ParseStar;
import xyz.fairportstudios.popularin.services.Popularin;

public class RecentReviewAdapter extends RecyclerView.Adapter<RecentReviewAdapter.RecentReviewViewHolder> {
    private Context context;
    private List<RecentReview> recentReviewList;
    private String userID;

    public RecentReviewAdapter(Context context, List<RecentReview> recentReviewList, String userID) {
        this.context = context;
        this.recentReviewList = recentReviewList;
        this.userID = userID;
    }

    private Integer pxToDp(Integer px) {
        float dp = px * context.getResources().getDisplayMetrics().density;
        return (int) dp;
    }

    @NonNull
    @Override
    public RecentReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecentReviewViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_recent_review, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecentReviewViewHolder holder, int position) {
        // ID
        final String reviewID = String.valueOf(recentReviewList.get(position).getId());
        final String filmID = String.valueOf(recentReviewList.get(position).getTmdb_id());

        // Auth
        final boolean isSelf = userID.equals(new Auth(context).getAuthID());

        // Parsing
        final String filmTitle = recentReviewList.get(position).getTitle();
        final String filmYear = new ParseDate().getYear(recentReviewList.get(position).getRelease_date());
        final String filmPoster = new ParseImage().getImage(recentReviewList.get(position).getPoster());
        final Integer reviewStar = new ParseStar().getStar(recentReviewList.get(position).getRating());

        // Request gambar
        RequestOptions requestOptions = new RequestOptions()
                .centerCrop()
                .placeholder(R.color.colorSurface)
                .error(R.color.colorSurface);

        // Isi
        holder.imageReviewStar.setImageResource(reviewStar);
        Glide.with(context).load(filmPoster).apply(requestOptions).into(holder.imageFilmPoster);

        // Margin
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) holder.itemView.getLayoutParams();
        if (position == 0) {
            layoutParams.leftMargin = pxToDp(16);
            layoutParams.rightMargin = pxToDp(8);
        } else if (position == getItemCount() - 1) {
            layoutParams.rightMargin = pxToDp(16);
        } else {
            layoutParams.rightMargin = pxToDp(8);
        }
        holder.itemView.setLayoutParams(layoutParams);

        // Activity
        holder.imageFilmPoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoReviewDetail = new Intent(context, ReviewActivity.class);
                gotoReviewDetail.putExtra(Popularin.REVIEW_ID, reviewID);
                gotoReviewDetail.putExtra(Popularin.IS_SELF, isSelf);
                context.startActivity(gotoReviewDetail);
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
        return recentReviewList.size();
    }

    static class RecentReviewViewHolder extends RecyclerView.ViewHolder {
        ImageView imageFilmPoster;
        ImageView imageReviewStar;

        RecentReviewViewHolder(@NonNull View itemView) {
            super(itemView);

            imageFilmPoster = itemView.findViewById(R.id.image_rrr_poster);
            imageReviewStar = itemView.findViewById(R.id.image_rrr_star);
        }
    }
}
