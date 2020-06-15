package xyz.fairportstudios.popularin.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.activities.UserDetailActivity;
import xyz.fairportstudios.popularin.apis.popularin.delete.DeleteCommentRequest;
import xyz.fairportstudios.popularin.models.Comment;
import xyz.fairportstudios.popularin.preferences.Auth;
import xyz.fairportstudios.popularin.statics.Popularin;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private Context context;
    private List<Comment> commentList;

    public CommentAdapter(Context context, List<Comment> commentList) {
        this.context = context;
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CommentViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_comment, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        // Posisi
        Comment currentItem = commentList.get(position);
        final int currentPosition = position;

        // Extra
        final Integer commentID = currentItem.getId();
        final Integer userID = currentItem.getUser_id();

        // Auth
        final boolean isSelf = userID.equals(new Auth(context).getAuthID());
        if (isSelf) {
            holder.imageDelete.setVisibility(View.VISIBLE);
        }

        // Request gambar
        RequestOptions requestOptions = new RequestOptions()
                .centerCrop()
                .placeholder(R.color.colorSurface)
                .error(R.color.colorSurface);

        // Isi
        holder.textUsername.setText(currentItem.getUsername());
        holder.textCommentTimestamp.setText(currentItem.getTimestamp());
        holder.textCommentDetail.setText(currentItem.getComment_detail());
        Glide.with(context).load(currentItem.getProfile_picture()).apply(requestOptions).into(holder.imageUserProfile);

        // Activity
        holder.imageUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UserDetailActivity.class);
                intent.putExtra(Popularin.USER_ID, userID);
                context.startActivity(intent);
            }
        });

        holder.imageDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteCommentRequest deleteCommentRequest = new DeleteCommentRequest(context, commentID);
                deleteCommentRequest.sendRequest(new DeleteCommentRequest.Callback() {
                    @Override
                    public void onSuccess() {
                        commentList.remove(currentPosition);
                        notifyItemRemoved(currentPosition);
                        notifyItemRangeChanged(currentPosition, getItemCount());
                    }

                    @Override
                    public void onError(String message) {
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    static class CommentViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageUserProfile;
        private ImageView imageDelete;
        private TextView textUsername;
        private TextView textCommentTimestamp;
        private TextView textCommentDetail;

        CommentViewHolder(@NonNull View itemView) {
            super(itemView);

            imageUserProfile = itemView.findViewById(R.id.image_rc_profile);
            imageDelete = itemView.findViewById(R.id.button_rc_delete);
            textUsername = itemView.findViewById(R.id.text_rc_username);
            textCommentTimestamp = itemView.findViewById(R.id.text_rc_timestamp);
            textCommentDetail = itemView.findViewById(R.id.text_rc_comment);
        }
    }
}
