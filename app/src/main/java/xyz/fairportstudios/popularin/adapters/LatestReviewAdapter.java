package xyz.fairportstudios.popularin.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.activities.ReviewActivity;
import xyz.fairportstudios.popularin.models.LatestReview;
import xyz.fairportstudios.popularin.services.ParseImage;
import xyz.fairportstudios.popularin.services.ParseStar;

public class LatestReviewAdapter extends RecyclerView.Adapter<LatestReviewAdapter.LatestReviewViewHolder> {
    private Context context;
    private List<LatestReview> latestReviewList;

    public LatestReviewAdapter(Context context, List<LatestReview> latestReviewList) {
        this.context = context;
        this.latestReviewList = latestReviewList;
    }

    @NonNull
    @Override
    public LatestReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LatestReviewViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_view_latest_review, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LatestReviewViewHolder holder, int position) {
        // Review ID
        final String reviewID = String.valueOf(latestReviewList.get(position).getId());

        // Request gambar
        RequestOptions requestOptions = new RequestOptions().centerCrop().placeholder(R.color.colorPrimary).error(R.color.colorPrimary);

        // Parsing
        Integer star = new ParseStar().getStar(latestReviewList.get(position).getRating());
        String poster = new ParseImage().getPoster(latestReviewList.get(position).getPoster());

        // Mengisi data
        holder.reviewStar.setImageResource(star);
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

        holder.filmPoster.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return latestReviewList.size();
    }

    static class LatestReviewViewHolder extends RecyclerView.ViewHolder {
        ImageView filmPoster;
        ImageView reviewStar;

        LatestReviewViewHolder(@NonNull View itemView) {
            super(itemView);

            filmPoster = itemView.findViewById(R.id.image_rvlr_poster);
            reviewStar = itemView.findViewById(R.id.image_rvlr_star);
        }
    }
}
