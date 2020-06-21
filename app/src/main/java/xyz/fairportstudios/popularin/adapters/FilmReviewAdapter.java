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
import xyz.fairportstudios.popularin.models.FilmReview;
import xyz.fairportstudios.popularin.services.ConvertRating;

public class FilmReviewAdapter extends RecyclerView.Adapter<FilmReviewAdapter.FilmReviewViewHolder> {
    private Context mContext;
    private List<FilmReview> mFilmReviewList;
    private OnClickListener mOnClickListener;

    public FilmReviewAdapter(Context context, List<FilmReview> filmReviewList, OnClickListener onClickListener) {
        mContext = context;
        mFilmReviewList = filmReviewList;
        mOnClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onItemClick(int position);

        void onUserProfileClick(int position);

        void onLikeClick(int position);

        void onCommentClick(int position);
    }

    @NonNull
    @Override
    public FilmReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FilmReviewViewHolder(LayoutInflater.from(mContext).inflate(R.layout.recycler_film_review, parent, false), mOnClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull FilmReviewViewHolder holder, int position) {
        // Posisi
        FilmReview currentItem = mFilmReviewList.get(position);

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
        int reviewStar = new ConvertRating().getStar(currentItem.getRating());

        // Isi
        holder.mTextUsername.setText(currentItem.getUsername());
        holder.mTextReviewDetail.setText(currentItem.getReview_detail());
        holder.mTextTotalLike.setText(String.valueOf(currentItem.getTotal_like()));
        holder.mTextTotalComment.setText(String.valueOf(currentItem.getTotal_comment()));
        holder.mTextReviewTimestamp.setText(currentItem.getTimestamp());
        holder.mImageReviewStar.setImageResource(reviewStar);
        Glide.with(mContext).load(currentItem.getProfile_picture()).into(holder.mImageUserProfile);
    }

    @Override
    public int getItemCount() {
        return mFilmReviewList.size();
    }

    static class FilmReviewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mImageUserProfile;
        private ImageView mImageReviewStar;
        private ImageView mImageLike;
        private ImageView mImageComment;
        private TextView mTextUsername;
        private TextView mTextReviewDetail;
        private TextView mTextTotalLike;
        private TextView mTextTotalComment;
        private TextView mTextReviewTimestamp;
        private View mBorder;
        private OnClickListener mOnClickListener;

        FilmReviewViewHolder(@NonNull View itemView, OnClickListener onClickListener) {
            super(itemView);

            mImageUserProfile = itemView.findViewById(R.id.image_rfr_profile);
            mImageReviewStar = itemView.findViewById(R.id.image_rfr_star);
            mImageLike = itemView.findViewById(R.id.image_rfr_like);
            mImageComment = itemView.findViewById(R.id.image_rfr_comment);
            mTextUsername = itemView.findViewById(R.id.text_rfr_username);
            mTextReviewDetail = itemView.findViewById(R.id.text_rfr_review);
            mTextTotalLike = itemView.findViewById(R.id.text_rfr_total_like);
            mTextTotalComment = itemView.findViewById(R.id.text_rfr_total_comment);
            mTextReviewTimestamp = itemView.findViewById(R.id.text_rfr_timestamp);
            mBorder = itemView.findViewById(R.id.border_rfr_layout);
            mOnClickListener = onClickListener;

            itemView.setOnClickListener(this);
            mImageUserProfile.setOnClickListener(this);
            mImageLike.setOnClickListener(this);
            mImageComment.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v == itemView) {
                mOnClickListener.onItemClick(getAdapterPosition());
            } else if (v == mImageUserProfile) {
                mOnClickListener.onUserProfileClick(getAdapterPosition());
            } else if (v == mImageLike) {
                mOnClickListener.onLikeClick(getAdapterPosition());
            } else if (v == mImageComment) {
                mOnClickListener.onCommentClick(getAdapterPosition());
            }
        }
    }
}
