package xyz.fairportstudios.popularin.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.activities.EmptyAccountActivity;
import xyz.fairportstudios.popularin.adapters.CommentAdapter;
import xyz.fairportstudios.popularin.apis.popularin.get.CommentRequest;
import xyz.fairportstudios.popularin.apis.popularin.post.AddCommentRequest;
import xyz.fairportstudios.popularin.models.Comment;
import xyz.fairportstudios.popularin.preferences.Auth;

public class ReviewCommentFragment extends Fragment {
    private Context context;
    private EditText inputComment;
    private List<Comment> commentList;
    private ProgressBar progressBar;
    private RecyclerView recyclerComment;
    private RelativeLayout anchorLayout;
    private TextView textEmptyResult;

    // Constructor
    private String reviewID;

    public ReviewCommentFragment(String reviewID) {
        this.reviewID = reviewID;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_review_comment, container, false);

        // Binding
        context = getActivity();
        inputComment = view.findViewById(R.id.input_frc_comment);
        anchorLayout = view.findViewById(R.id.anchor_frc_layout);
        progressBar = view.findViewById(R.id.pbr_frc_layout);
        recyclerComment = view.findViewById(R.id.recycler_frc_layout);
        textEmptyResult = view.findViewById(R.id.text_frc_empty_result);
        ImageView imageSend = view.findViewById(R.id.image_fr_send);

        // Auth
        final boolean isAuth = new Auth(context).isAuth();

        // List
        commentList = new ArrayList<>();

        // Activity
        imageSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAuth) {
                    addComment();
                } else {
                    gotoEmptyAccount();
                }
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getComment();
    }

    private void getComment() {
        CommentRequest commentRequest = new CommentRequest(context, commentList, recyclerComment);
        String requestURL = commentRequest.getRequestURL(reviewID, 1);
        commentRequest.sendRequest(requestURL, new CommentRequest.APICallback() {
            @Override
            public void onSuccess() {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onEmpty() {
                progressBar.setVisibility(View.GONE);
                textEmptyResult.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError() {
                progressBar.setVisibility(View.GONE);
                textEmptyResult.setVisibility(View.VISIBLE);
                textEmptyResult.setText(R.string.not_found);
                Snackbar.make(anchorLayout, R.string.get_error, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void addComment() {
        String comment = inputComment.getText().toString();
        AddCommentRequest addCommentRequest = new AddCommentRequest(context, reviewID, comment);
        addCommentRequest.sendRequest(new AddCommentRequest.APICallback() {
            @Override
            public void onSuccess(Comment comment) {
                CommentAdapter commentAdapter = new CommentAdapter(context, commentList);
                int position = commentList.size();
                commentList.add(position, comment);
                commentAdapter.notifyItemInserted(position);
                Snackbar.make(anchorLayout, R.string.comment_added, Snackbar.LENGTH_SHORT).show();
            }

            @Override
            public void onFailed(String message) {
                Snackbar.make(anchorLayout, message, Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onError() {
                Snackbar.make(anchorLayout, R.string.failed_add_comment, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void gotoEmptyAccount() {
        Intent intent = new Intent(context, EmptyAccountActivity.class);
        context.startActivity(intent);
    }
}
