package xyz.fairportstudios.popularin.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.models.RecentReview;
import xyz.fairportstudios.popularin.services.ConvertRating;
import xyz.fairportstudios.popularin.statics.TMDbAPI;

public class RecentReviewAdapter extends RecyclerView.Adapter<RecentReviewAdapter.RecentReviewViewHolder> {
    private Context mContext;
    private List<RecentReview> mRecentReviewList;
    private OnClickListener mOnClickListener;

    public RecentReviewAdapter(Context context, List<RecentReview> recentReviewList, OnClickListener onClickListener) {
        mContext = context;
        mRecentReviewList = recentReviewList;
        mOnClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onRecentReviewItemClick(int position);

        void onRecentReviewPosterLongClick(int position);
    }

    public int getDensity(int px) {
        float dp = px * mContext.getResources().getDisplayMetrics().density;
        return (int) dp;
    }

    @NonNull
    @Override
    public RecentReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecentReviewViewHolder(LayoutInflater.from(mContext).inflate(R.layout.recycler_recent_review, parent, false), mOnClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecentReviewViewHolder holder, int position) {
        // Posisi
        RecentReview currentItem = mRecentReviewList.get(position);

        // Parsing
        int reviewStar = new ConvertRating().getStar(currentItem.getRating());
        String filmPoster = TMDbAPI.BASE_SMALL_IMAGE_URL + currentItem.getPoster();

        // Isi
        holder.mImageReviewStar.setImageResource(reviewStar);
        Glide.with(mContext).load(filmPoster).into(holder.mImageFilmPoster);

        // Margin
        int left = getDensity(8);
        int right = getDensity(8);

        boolean isEdgeLeft = position == 0;
        boolean isEdgeRight = position == getItemCount() - 1;

        if (isEdgeLeft) {
            left = getDensity(16);
        }
        if (isEdgeRight) {
            right = getDensity(16);
        }

        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) holder.itemView.getLayoutParams();
        layoutParams.setMarginStart(left);
        layoutParams.setMarginEnd(right);
        holder.itemView.setLayoutParams(layoutParams);
    }

    @Override
    public int getItemCount() {
        return mRecentReviewList.size();
    }

    static class RecentReviewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private ImageView mImageFilmPoster;
        private ImageView mImageReviewStar;
        private OnClickListener mOnClickListener;

        RecentReviewViewHolder(@NonNull View itemView, OnClickListener onClickListener) {
            super(itemView);

            mImageFilmPoster = itemView.findViewById(R.id.image_rrr_poster);
            mImageReviewStar = itemView.findViewById(R.id.image_rrr_star);
            mOnClickListener = onClickListener;

            itemView.setOnClickListener(this);
            mImageFilmPoster.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v == itemView) {
                mOnClickListener.onRecentReviewItemClick(getAdapterPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (v == mImageFilmPoster) {
                mOnClickListener.onRecentReviewPosterLongClick(getAdapterPosition());
            }
            return true;
        }
    }
}
