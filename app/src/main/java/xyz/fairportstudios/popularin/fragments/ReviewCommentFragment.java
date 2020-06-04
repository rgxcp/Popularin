package xyz.fairportstudios.popularin.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import androidx.recyclerview.widget.LinearLayoutManager;
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
    // Untuk fitur onResume
    private Boolean firstTime = true;

    // Untuk fitur comment
    private Integer page = 1;
    private CommentAdapter commentAdapter;
    private List<Comment> commentList = new ArrayList<>();

    // Member variable
    private Context context;
    private EditText inputComment;
    private ImageView imageSend;
    private ProgressBar progressBar;
    private RecyclerView recyclerComment;
    private RelativeLayout anchorLayout;
    private String comment;
    private TextView textEmptyResult;

    // Constructor variable
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
        imageSend = view.findViewById(R.id.image_fr_send);
        anchorLayout = view.findViewById(R.id.anchor_frc_layout);
        progressBar = view.findViewById(R.id.pbr_frc_layout);
        recyclerComment = view.findViewById(R.id.recycler_frc_layout);
        textEmptyResult = view.findViewById(R.id.text_frc_empty_result);

        // Auth
        final boolean isAuth = new Auth(context).isAuth();

        // Text watcher
        inputComment.addTextChangedListener(commentWatcher);

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

        /*
        recyclerComment.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    getComment(page);
                }
            }
        });
         */

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (firstTime) {
            getComment(page);
            firstTime = false;
            page++;
        }
    }

    private TextWatcher commentWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // Tidak digunakan
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            comment = inputComment.getText().toString();
            if (!comment.isEmpty()) {
                imageSend.setVisibility(View.VISIBLE);
            } else {
                imageSend.setVisibility(View.GONE);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            // Tidak digunakan
        }
    };

    private void getComment(Integer page) {
        CommentRequest commentRequest = new CommentRequest(context, Integer.valueOf(reviewID), commentList);
        commentRequest.sendRequest(page, new CommentRequest.APICallback() {
            @Override
            public void onSuccess(List<Comment> comments) {
                commentAdapter = new CommentAdapter(context, comments);
                recyclerComment.setAdapter(commentAdapter);
                recyclerComment.setLayoutManager(new LinearLayoutManager(context));
                recyclerComment.setVisibility(View.VISIBLE);
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
                Snackbar.make(anchorLayout, R.string.network_error, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void addComment() {
        AddCommentRequest addCommentRequest = new AddCommentRequest(context, reviewID, comment);
        addCommentRequest.sendRequest(new AddCommentRequest.APICallback() {
            @Override
            public void onSuccess(Comment comment) {
                int index = commentAdapter.getItemCount();
                commentList.add(index, comment);
                commentAdapter.notifyItemInserted(index);
                recyclerComment.scrollToPosition(index - 1);
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
