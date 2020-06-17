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
import xyz.fairportstudios.popularin.services.ConvertPixel;
import xyz.fairportstudios.popularin.services.ParseDate;
import xyz.fairportstudios.popularin.services.ConvertRating;
import xyz.fairportstudios.popularin.statics.Popularin;
import xyz.fairportstudios.popularin.statics.TMDbAPI;

public class RecentReviewAdapter extends RecyclerView.Adapter<RecentReviewAdapter.RecentReviewViewHolder> {
    private Context context;
    private List<RecentReview> recentReviewList;
    private Boolean isSelf;

    public RecentReviewAdapter(Context context, List<RecentReview> recentReviewList, Boolean isSelf) {
        this.context = context;
        this.recentReviewList = recentReviewList;
        this.isSelf = isSelf;
    }

    @NonNull
    @Override
    public RecentReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecentReviewViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_recent_review, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecentReviewViewHolder holder, int position) {
        // Posisi
        RecentReview currentItem = recentReviewList.get(position);

        // Extra
        final Integer reviewID = currentItem.getId();
        final Integer filmID = currentItem.getTmdb_id();

        // Parsing
        final String filmTitle = currentItem.getTitle();
        final String filmYear = new ParseDate().getYear(currentItem.getRelease_date());
        final String filmPoster = TMDbAPI.IMAGE + currentItem.getPoster();
        final Integer reviewStar = new ConvertRating().getStar(currentItem.getRating());

        // Request gambar
        RequestOptions requestOptions = new RequestOptions()
                .centerCrop()
                .placeholder(R.color.colorSurface)
                .error(R.color.colorSurface);

        // Isi
        holder.imageReviewStar.setImageResource(reviewStar);
        Glide.with(context).load(filmPoster).apply(requestOptions).into(holder.imageFilmPoster);

        // Margin
        ConvertPixel convertPixel = new ConvertPixel(context);

        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) holder.itemView.getLayoutParams();
        if (position == 0) {
            layoutParams.leftMargin = convertPixel.getDensity(16);
            layoutParams.rightMargin = convertPixel.getDensity(8);
        } else if (position == getItemCount() - 1) {
            layoutParams.rightMargin = convertPixel.getDensity(16);
        } else {
            layoutParams.rightMargin = convertPixel.getDensity(8);
        }
        holder.itemView.setLayoutParams(layoutParams);

        // Activity
        holder.imageFilmPoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ReviewActivity.class);
                intent.putExtra(Popularin.REVIEW_ID, reviewID);
                intent.putExtra(Popularin.IS_SELF, isSelf);
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
        return recentReviewList.size();
    }

    static class RecentReviewViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageFilmPoster;
        private ImageView imageReviewStar;

        RecentReviewViewHolder(@NonNull View itemView) {
            super(itemView);

            imageFilmPoster = itemView.findViewById(R.id.image_rrr_poster);
            imageReviewStar = itemView.findViewById(R.id.image_rrr_star);
        }
    }
}
