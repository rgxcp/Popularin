package xyz.fairportstudios.popularin.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.models.Review;
import xyz.fairportstudios.popularin.services.ParseDate;
import xyz.fairportstudios.popularin.services.ConvertRating;
import xyz.fairportstudios.popularin.statics.TMDbAPI;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    private Context mContext;
    private List<Review> mReviewList;
    private OnClickListener mOnClickListener;

    public ReviewAdapter(Context mContext, List<Review> mReviewList, OnClickListener mOnClickListener) {
        this.mContext = mContext;
        this.mReviewList = mReviewList;
        this.mOnClickListener = mOnClickListener;
    }

    public interface OnClickListener {
        void onItemClick(int position);

        void onUserProfileClicked(int position);

        void onFilmPosterClick(int position);

        void onFilmPosterLongClick(int position);

        void onLikeClick(int position);

        void onCommentClick(int position);
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ReviewViewHolder(LayoutInflater.from(mContext).inflate(R.layout.recycler_review, parent, false), mOnClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        // Posisi
        Review currentItem = mReviewList.get(position);

        // Like status
        if (currentItem.getIs_liked()) {
            holder.mImageLike.setImageResource(R.drawable.ic_fill_heart);
        } else {
            holder.mImageLike.setImageResource(R.drawable.ic_outline_heart);
        }

        // Border
        if (position == getItemCount() - 1) {
            holder.mBorder.setVisibility(View.INVISIBLE);
        } else {
            holder.mBorder.setVisibility(View.VISIBLE);
        }

        // Parsing
        final Integer reviewStar = new ConvertRating().getStar(currentItem.getRating());
        final String filmYear = new ParseDate().getYear(currentItem.getRelease_date());
        final String filmPoster = TMDbAPI.BASE_SMALL_IMAGE_URL + currentItem.getPoster();

        // Isi
        holder.mTextFilmTitleYear.setText(String.format("%s (%s)", currentItem.getTitle(), filmYear));
        holder.mTextUsername.setText(currentItem.getUsername());
        holder.mTextReviewDetail.setText(currentItem.getReview_detail());
        holder.mTextTotalLike.setText(String.valueOf(currentItem.getTotal_like()));
        holder.mTextTotalComment.setText(String.valueOf(currentItem.getTotal_comment()));
        holder.mTextReviewTimestamp.setText(currentItem.getTimestamp());
        holder.mImageReviewStar.setImageResource(reviewStar);
        Glide.with(mContext).load(currentItem.getProfile_picture()).into(holder.mImageUserProfile);
        Glide.with(mContext).load(filmPoster).into(holder.mImageFilmPoster);
    }

    @Override
    public int getItemCount() {
        return mReviewList.size();
    }

    static class ReviewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private ImageView mImageUserProfile;
        private ImageView mImageReviewStar;
        private ImageView mImageFilmPoster;
        private ImageView mImageLike;
        private ImageView mImageComment;
        private TextView mTextFilmTitleYear;
        private TextView mTextUsername;
        private TextView mTextReviewDetail;
        private TextView mTextTotalLike;
        private TextView mTextTotalComment;
        private TextView mTextReviewTimestamp;
        private View mBorder;
        private OnClickListener mOnClickListener;

        ReviewViewHolder(@NonNull View itemView, OnClickListener onClickListener) {
            super(itemView);

            mImageUserProfile = itemView.findViewById(R.id.image_rr_profile);
            mImageReviewStar = itemView.findViewById(R.id.image_rr_star);
            mImageFilmPoster = itemView.findViewById(R.id.image_rr_poster);
            mImageLike = itemView.findViewById(R.id.image_rr_like);
            mImageComment = itemView.findViewById(R.id.image_rr_comment);
            mTextFilmTitleYear = itemView.findViewById(R.id.text_rr_title_year);
            mTextUsername = itemView.findViewById(R.id.text_rr_username);
            mTextReviewDetail = itemView.findViewById(R.id.text_rr_review);
            mTextTotalLike = itemView.findViewById(R.id.text_rr_total_like);
            mTextTotalComment = itemView.findViewById(R.id.text_rr_total_comment);
            mTextReviewTimestamp = itemView.findViewById(R.id.text_rr_timestamp);
            mBorder = itemView.findViewById(R.id.border_rr_layout);
            mOnClickListener = onClickListener;

            itemView.setOnClickListener(this);
            mImageUserProfile.setOnClickListener(this);
            mImageFilmPoster.setOnClickListener(this);
            mImageFilmPoster.setOnLongClickListener(this);
            mImageLike.setOnClickListener(this);
            mImageComment.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v == itemView) {
                mOnClickListener.onItemClick(getAdapterPosition());
            } else if (v == mImageUserProfile) {
                mOnClickListener.onUserProfileClicked(getAdapterPosition());
            } else if (v == mImageFilmPoster) {
                mOnClickListener.onFilmPosterClick(getAdapterPosition());
            } else if (v == mImageLike) {
                mOnClickListener.onLikeClick(getAdapterPosition());
            } else if (v == mImageComment) {
                mOnClickListener.onCommentClick(getAdapterPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (v == mImageFilmPoster) {
                mOnClickListener.onFilmPosterLongClick(getAdapterPosition());
            }
            return true;
        }
    }
}
