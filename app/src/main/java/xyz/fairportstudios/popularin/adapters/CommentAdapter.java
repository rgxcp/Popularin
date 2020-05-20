package xyz.fairportstudios.popularin.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.activities.UserDetailActivity;
import xyz.fairportstudios.popularin.apis.popularin.delete.DeleteCommentRequest;
import xyz.fairportstudios.popularin.models.Comment;
import xyz.fairportstudios.popularin.preferences.Auth;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private Context context;
    private List<Comment> commentList;

    public CommentAdapter(Context context, List<Comment> commentList) {
        this.context = context;
        this.commentList = commentList;
    }

    private Integer dpToPx() {
        float px = 16 * context.getResources().getDisplayMetrics().density;
        return (int) px;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CommentViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_comment, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final CommentViewHolder holder, final int position) {
        // Comment ID
        final String commentID = String.valueOf(commentList.get(position).getId());

        // User ID
        final String userID = String.valueOf(commentList.get(position).getUser_id());

        // Auth
        final String authID = new Auth(context).getAuthID();

        if (userID.equals(authID)) {
            holder.buttonDelete.setVisibility(View.VISIBLE);
        }

        // Request gambar
        RequestOptions requestOptions = new RequestOptions().centerCrop().placeholder(R.color.colorPrimary).error(R.color.colorPrimary);

        // Mengisi data
        holder.userFirstName.setText(commentList.get(position).getFirst_name());
        holder.commentDate.setText(commentList.get(position).getTimestamp());
        holder.commentDetail.setText(commentList.get(position).getComment_text());
        Glide.with(context).load(commentList.get(position).getProfile_picture()).apply(requestOptions).into(holder.userProfile);

        // Margin
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) holder.itemView.getLayoutParams();
        if (position == commentList.size() - 1) {
            layoutParams.bottomMargin = dpToPx();
            holder.border.setVisibility(View.GONE);
        }
        holder.itemView.setLayoutParams(layoutParams);

        // Activity
        holder.userProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoUserDetail = new Intent(context, UserDetailActivity.class);
                gotoUserDetail.putExtra("USER_ID", userID);
                context.startActivity(gotoUserDetail);
            }
        });

        holder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // DELETE
                DeleteCommentRequest deleteCommentRequest = new DeleteCommentRequest(position, context, commentList, commentID);
                deleteCommentRequest.sendRequest(new DeleteCommentRequest.APICallback() {
                    @Override
                    public void onSuccess() {
                        Snackbar.make(holder.layout, R.string.comment_deleted, Snackbar.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError() {
                        Snackbar.make(holder.layout, R.string.delete_comment_error, Snackbar.LENGTH_LONG).show();
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
        Button buttonDelete;
        ImageView userProfile;
        LinearLayout layout;
        TextView userFirstName;
        TextView commentDate;
        TextView commentDetail;
        View border;

        CommentViewHolder(@NonNull View itemView) {
            super(itemView);

            buttonDelete = itemView.findViewById(R.id.button_rc_delete);
            userProfile = itemView.findViewById(R.id.image_rc_profile);
            layout = itemView.findViewById(R.id.layout_rc_anchor);
            userFirstName = itemView.findViewById(R.id.text_rc_first_name);
            commentDate = itemView.findViewById(R.id.text_rc_date);
            commentDetail = itemView.findViewById(R.id.text_rc_comment);
            border = itemView.findViewById(R.id.border_rc_layout);
        }
    }
}
