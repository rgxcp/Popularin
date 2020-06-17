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
import xyz.fairportstudios.popularin.models.UserReview;
import xyz.fairportstudios.popularin.services.ParseDate;
import xyz.fairportstudios.popularin.services.ConvertRating;
import xyz.fairportstudios.popularin.statics.TMDbAPI;

public class UserReviewAdapter extends RecyclerView.Adapter<UserReviewAdapter.UserReviewViewHolder> {
    private Context mContext;
    private List<UserReview> mUserReviewList;
    private OnClickListener mOnClickListener;

    public UserReviewAdapter(Context mContext, List<UserReview> mUserReviewList, OnClickListener mOnClickListener) {
        this.mContext = mContext;
        this.mUserReviewList = mUserReviewList;
        this.mOnClickListener = mOnClickListener;
    }

    public interface OnClickListener {
        void onItemClick(int position);

        void onFilmPosterClick(int position);

        void onFilmPosterLongClick(int position);

        void onLikeClick(int position);

        void onCommentClick(int position);
    }

    @NonNull
    @Override
    public UserReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserReviewViewHolder(LayoutInflater.from(mContext).inflate(R.layout.recycler_user_review, parent, false), mOnClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull UserReviewViewHolder holder, int position) {
        // Posisi
        UserReview currentItem = mUserReviewList.get(position);

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
        final String filmPoster = TMDbAPI.IMAGE + currentItem.getPoster();

        // Isi
        holder.mTextFilmTitleYear.setText(String.format("%s (%s)", currentItem.getTitle(), filmYear));
        holder.mTextReviewDetail.setText(currentItem.getReview_detail());
        holder.mTextTotalLike.setText(String.valueOf(currentItem.getTotal_like()));
        holder.mTextTotalComment.setText(String.valueOf(currentItem.getTotal_comment()));
        holder.mTextReviewTimestamp.setText(currentItem.getTimestamp());
        holder.mImageReviewStar.setImageResource(reviewStar);
        Glide.with(mContext).load(filmPoster).into(holder.mImageFilmPoster);
    }

    @Override
    public int getItemCount() {
        return mUserReviewList.size();
    }

    static class UserReviewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private ImageView mImageReviewStar;
        private ImageView mImageFilmPoster;
        private ImageView mImageLike;
        private ImageView mImageComment;
        private TextView mTextFilmTitleYear;
        private TextView mTextReviewDetail;
        private TextView mTextTotalLike;
        private TextView mTextTotalComment;
        private TextView mTextReviewTimestamp;
        private View mBorder;
        private OnClickListener mOnClickListener;

        UserReviewViewHolder(@NonNull View itemView, OnClickListener onClickListener) {
            super(itemView);

            mImageReviewStar = itemView.findViewById(R.id.image_rur_star);
            mImageFilmPoster = itemView.findViewById(R.id.image_rur_poster);
            mImageLike = itemView.findViewById(R.id.image_rur_like);
            mImageComment = itemView.findViewById(R.id.image_rur_comment);
            mTextFilmTitleYear = itemView.findViewById(R.id.text_rur_title_year);
            mTextReviewDetail = itemView.findViewById(R.id.text_rur_review);
            mTextTotalLike = itemView.findViewById(R.id.text_rur_total_like);
            mTextTotalComment = itemView.findViewById(R.id.text_rur_total_comment);
            mTextReviewTimestamp = itemView.findViewById(R.id.text_rur_timestamp);
            mBorder = itemView.findViewById(R.id.border_rur_layout);
            mOnClickListener = onClickListener;

            itemView.setOnClickListener(this);
            mImageFilmPoster.setOnClickListener(this);
            mImageFilmPoster.setOnLongClickListener(this);
            mImageLike.setOnClickListener(this);
            mImageComment.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v == itemView) {
                mOnClickListener.onItemClick(getAdapterPosition());
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
