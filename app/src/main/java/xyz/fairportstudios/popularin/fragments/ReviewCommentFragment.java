package xyz.fairportstudios.popularin.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.activities.EmptyAccountActivity;
import xyz.fairportstudios.popularin.apis.popularin.get.CommentRequest;
import xyz.fairportstudios.popularin.models.Comment;
import xyz.fairportstudios.popularin.preferences.Auth;

public class ReviewCommentFragment extends Fragment {
    // Member
    private Boolean isAuth;
    private Button buttonComment;
    private Context context;
    private EditText inputComment;
    private LinearLayout notFoundLayout;
    private RelativeLayout anchorLayout;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;

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
        buttonComment = view.findViewById(R.id.button_frc_comment);
        inputComment = view.findViewById(R.id.input_frc_comment);
        notFoundLayout = view.findViewById(R.id.layout_frc_not_found);
        anchorLayout = view.findViewById(R.id.layout_frc_anchor);
        progressBar = view.findViewById(R.id.pbr_frc_layout);
        recyclerView = view.findViewById(R.id.recycler_frc_layout);

        // Auth
        isAuth = new Auth(context).isAuth();

        // Mendapatkan data
        getComment();

        // Activity
        buttonComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAuth) {
                    buttonComment.setEnabled(false);
                    String comment = inputComment.getText().toString();
                    addComment(comment);
                } else {
                    gotoEmptyAccount();
                }
            }
        });

        /*
        // GET
        CommentRequest commentRequest = new CommentRequest(context, commentList, recyclerView);
        String requestURL = commentRequest.getRequestURL(reviewID, 1);
        commentRequest.sendRequest(requestURL, new CommentRequest.APICallback() {
            @Override
            public void onSuccess() {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onEmpty() {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError() {
                progressBar.setVisibility(View.GONE);
                Snackbar.make(layout, R.string.get_error, Snackbar.LENGTH_LONG).show();
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
                            Snackbar.make(layout, message, Snackbar.LENGTH_LONG).show();
                        }

                        @Override
                        public void onError() {
                            Snackbar.make(layout, R.string.post_comment_error, Snackbar.LENGTH_LONG).show();
                        }
                    });
                } else {
                    Intent gotoEmptyUser = new Intent(context, EmptyAccountActivity.class);
                    startActivity(gotoEmptyUser);
                }
            }
        });
         */

        return view;
    }

    private void getComment() {
        List<Comment> commentList = new ArrayList<>();

        CommentRequest commentRequest = new CommentRequest(context, commentList, recyclerView);
        String requestURL = commentRequest.getRequestURL(reviewID, 1);
        commentRequest.sendRequest(requestURL, new CommentRequest.APICallback() {
            @Override
            public void onSuccess() {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onEmpty() {
                notFoundLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError() {
                progressBar.setVisibility(View.GONE);
                notFoundLayout.setVisibility(View.VISIBLE);
                Snackbar.make(anchorLayout, R.string.get_error, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void addComment(String comment) {

    }

    private void gotoEmptyAccount() {
        Intent intent = new Intent(context, EmptyAccountActivity.class);
        context.startActivity(intent);
    }
}
