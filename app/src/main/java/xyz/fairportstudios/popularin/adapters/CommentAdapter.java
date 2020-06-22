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
import xyz.fairportstudios.popularin.models.Comment;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private Context mContext;
    private int mAuthID;
    private List<Comment> mCommentList;
    private OnClickListener mOnClickListener;

    public CommentAdapter(Context context, int authID, List<Comment> commentList, OnClickListener onClickListener) {
        mContext = context;
        mAuthID = authID;
        mCommentList = commentList;
        mOnClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onProfileClick(int position);

        void onDeleteClick(int position);
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CommentViewHolder(LayoutInflater.from(mContext).inflate(R.layout.recycler_comment, parent, false), mOnClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        // Posisi
        Comment currentItem = mCommentList.get(position);

        // Auth
        int userID = currentItem.getUser_id();
        boolean isSelf = userID == mAuthID;
        if (isSelf) {
            holder.mImageDelete.setVisibility(View.VISIBLE);
        } else {
            holder.mImageDelete.setVisibility(View.GONE);
        }

        // Isi
        holder.mTextUsername.setText(currentItem.getUsername());
        holder.mTextCommentTimestamp.setText(currentItem.getTimestamp());
        holder.mTextCommentDetail.setText(currentItem.getComment_detail());
        Glide.with(mContext).load(currentItem.getProfile_picture()).into(holder.mImageProfile);
    }

    @Override
    public int getItemCount() {
        return mCommentList.size();
    }

    static class CommentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mImageProfile;
        private ImageView mImageDelete;
        private TextView mTextUsername;
        private TextView mTextCommentTimestamp;
        private TextView mTextCommentDetail;
        private OnClickListener mOnClickListener;

        CommentViewHolder(@NonNull View itemView, OnClickListener onClickListener) {
            super(itemView);

            mImageProfile = itemView.findViewById(R.id.image_rc_profile);
            mImageDelete = itemView.findViewById(R.id.image_rc_delete);
            mTextUsername = itemView.findViewById(R.id.text_rc_username);
            mTextCommentTimestamp = itemView.findViewById(R.id.text_rc_timestamp);
            mTextCommentDetail = itemView.findViewById(R.id.text_rc_comment);
            mOnClickListener = onClickListener;

            mImageProfile.setOnClickListener(this);
            mImageDelete.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v == mImageProfile) {
                mOnClickListener.onProfileClick(getAdapterPosition());
            } else if (v == mImageDelete) {
                mOnClickListener.onDeleteClick(getAdapterPosition());
            }
        }
    }
}
