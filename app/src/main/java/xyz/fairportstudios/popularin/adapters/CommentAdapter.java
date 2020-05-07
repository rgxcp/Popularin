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
import xyz.fairportstudios.popularin.apis.popularin.delete.DeleteComment;
import xyz.fairportstudios.popularin.models.Comment;
import xyz.fairportstudios.popularin.preferences.Auth;
import xyz.fairportstudios.popularin.services.ParseDate;

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
        return new CommentViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_view_comment, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        // Comment ID
        final String commentID = String.valueOf(commentList.get(position).getId());

        // User ID
        final String userID = String.valueOf(commentList.get(position).getUser_id());

        // Auth ID
        final String authID = new Auth(context).getAuthID();

        // Request gambar
        RequestOptions requestOptions = new RequestOptions().centerCrop().placeholder(R.color.colorPrimary).error(R.color.colorPrimary);

        // Parsing
        String date = new ParseDate().getDate(commentList.get(position).getComment_date());

        // Mengisi data
        holder.userFirstName.setText(commentList.get(position).getFirst_name());
        holder.commentDate.setText(date);
        holder.commentDetail.setText(commentList.get(position).getComment_text());
        Glide.with(context).load(commentList.get(position).getProfile_picture()).apply(requestOptions).into(holder.userProfile);

        // Activity
        holder.userProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotoUserDetail = new Intent(context, UserDetailActivity.class);
                gotoUserDetail.putExtra("USER_ID", userID);
                context.startActivity(gotoUserDetail);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (userID.equals(authID)) {
                    DeleteComment deleteComment = new DeleteComment(commentID, context);
                    deleteComment.sendRequest(new DeleteComment.JSONCallback() {
                        @Override
                        public void onSuccess(Integer status) {
                            if (status == 404) {
                                Toast.makeText(context, "Komen dihapus.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "Ada kesalahan dalam database.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    static class CommentViewHolder extends RecyclerView.ViewHolder {
        ImageView userProfile;
        TextView userFirstName;
        TextView commentDate;
        TextView commentDetail;

        CommentViewHolder(@NonNull View itemView) {
            super(itemView);

            userProfile = itemView.findViewById(R.id.image_rvc_profile);
            userFirstName = itemView.findViewById(R.id.text_rvc_first_name);
            commentDate = itemView.findViewById(R.id.text_rvc_date);
            commentDetail = itemView.findViewById(R.id.text_rvc_comment);
        }
    }
}
