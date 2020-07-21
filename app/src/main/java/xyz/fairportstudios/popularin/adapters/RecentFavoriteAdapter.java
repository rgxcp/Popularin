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
import xyz.fairportstudios.popularin.models.RecentFavorite;
import xyz.fairportstudios.popularin.statics.TMDbAPI;

public class RecentFavoriteAdapter extends RecyclerView.Adapter<RecentFavoriteAdapter.RecentFavoriteViewHolder> {
    private Context mContext;
    private List<RecentFavorite> mRecentFavoriteList;
    private OnClickListener mOnClickListener;

    public RecentFavoriteAdapter(Context context, List<RecentFavorite> recentFavoriteList, OnClickListener onClickListener) {
        mContext = context;
        mRecentFavoriteList = recentFavoriteList;
        mOnClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onRecentFavoriteItemClick(int position);

        void onRecentFavoriteItemLongClick(int position);
    }

    private int getDensity(int px) {
        float dp = px * mContext.getResources().getDisplayMetrics().density;
        return (int) dp;
    }

    @NonNull
    @Override
    public RecentFavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecentFavoriteViewHolder(LayoutInflater.from(mContext).inflate(R.layout.recycler_recent_favorite, parent, false), mOnClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecentFavoriteViewHolder holder, int position) {
        // Posisi
        RecentFavorite currentItem = mRecentFavoriteList.get(position);

        // Parsing
        String poster = TMDbAPI.BASE_SMALL_IMAGE_URL + currentItem.getPoster();

        // Isi
        Glide.with(mContext).load(poster).into(holder.mImagePoster);

        // Margin
        int left = getDensity(4);
        int right = getDensity(4);

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
        return mRecentFavoriteList.size();
    }

    static class RecentFavoriteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private ImageView mImagePoster;
        private OnClickListener mOnClickListener;

        RecentFavoriteViewHolder(@NonNull View itemView, OnClickListener onClickListener) {
            super(itemView);

            mImagePoster = itemView.findViewById(R.id.image_rrf_poster);
            mOnClickListener = onClickListener;

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v == itemView) {
                mOnClickListener.onRecentFavoriteItemClick(getAdapterPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (v == itemView) {
                mOnClickListener.onRecentFavoriteItemLongClick(getAdapterPosition());
            }
            return true;
        }
    }
}
