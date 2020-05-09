package xyz.fairportstudios.popularin.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.activities.EmptyUserActivity;
import xyz.fairportstudios.popularin.apis.popularin.get.CommentRequest;
import xyz.fairportstudios.popularin.apis.popularin.post.AddCommentRequest;
import xyz.fairportstudios.popularin.models.Comment;
import xyz.fairportstudios.popularin.preferences.Auth;

public class ReviewCommentFragment extends Fragment {
    private Boolean isAuth;
    private Context context;
    private RelativeLayout layout;
    private List<Comment> commentList;
    private ProgressBar progressBar;
    private TextInputEditText inputComment;
    private TextView emptyComment;
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
        layout = view.findViewById(R.id.layout_frc_anchor);
        progressBar = view.findViewById(R.id.pbr_frc_layout);
        inputComment = view.findViewById(R.id.text_frc_comment);
        emptyComment = view.findViewById(R.id.text_frc_empty);
        Button buttonComment = view.findViewById(R.id.button_frc_comment);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_frc_layout);

        // Auth
        isAuth = new Auth(context).isAuth();

        // List
        commentList = new ArrayList<>();

        // GET
        CommentRequest commentRequest = new CommentRequest(context, commentList, recyclerView);
        String requestURL = commentRequest.getRequestURL(reviewID);
        commentRequest.sendRequest(requestURL, new CommentRequest.APICallback() {
            @Override
            public void onSuccess() {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onEmpty() {
                progressBar.setVisibility(View.GONE);
                emptyComment.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError() {
                progressBar.setVisibility(View.GONE);
                emptyComment.setVisibility(View.VISIBLE);
                Snackbar.make(layout, R.string.get_error, Snackbar.LENGTH_SHORT).show();
            }
        });

        // Activity
        buttonComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAuth) {
                    // POST
                    String comment = Objects.requireNonNull(inputComment.getText()).toString();
                    AddCommentRequest addCommentRequest = new AddCommentRequest(context, commentList, reviewID, comment);
                    addCommentRequest.sendRequest(new AddCommentRequest.APICallback() {
                        @Override
                        public void onSuccess() {
                            Snackbar.make(layout, R.string.comment_sent, Snackbar.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailed(String message) {
                            Snackbar.make(layout, message, Snackbar.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError() {
                            Snackbar.make(layout, R.string.post_comment_error, Snackbar.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Intent gotoEmptyUser = new Intent(context, EmptyUserActivity.class);
                    startActivity(gotoEmptyUser);
                }
            }
        });

        return view;
    }
}
