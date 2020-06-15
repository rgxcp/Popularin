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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
    // Variable untuk fitur onResume
    private Boolean isResumeFirsTime = true;

    // Variable untuk fitur load more
    private Boolean isLoadFirstTimeSuccess = false;
    private Boolean isLoading = true;
    private Integer startPage = 1;
    private Integer currentPage = 1;
    private Integer totalPage;

    // Variable member
    private Context context;
    private CommentAdapter commentAdapter;
    private CommentRequest commentRequest;
    private EditText inputComment;
    private ImageView imageSend;
    private List<Comment> commentList;
    private ProgressBar progressBar;
    private RecyclerView recyclerComment;
    private String comment;
    private SwipeRefreshLayout swipeRefresh;
    private TextView textMessage;

    // Variable constructor
    private Integer reviewID;

    public ReviewCommentFragment(Integer reviewID) {
        this.reviewID = reviewID;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_review_comment, container, false);

        // Context
        context = getActivity();

        // Binding
        inputComment = view.findViewById(R.id.input_frc_comment);
        imageSend = view.findViewById(R.id.image_frc_send);
        progressBar = view.findViewById(R.id.pbr_rr_layout);
        recyclerComment = view.findViewById(R.id.recycler_rr_layout);
        swipeRefresh = view.findViewById(R.id.swipe_refresh_rr_layout);
        textMessage = view.findViewById(R.id.text_rr_message);

        // Auth
        final Boolean isAuth = new Auth(context).isAuth();

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

        recyclerComment.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (!isLoading && currentPage <= totalPage) {
                        isLoading = true;
                        swipeRefresh.setRefreshing(true);
                        getComment(currentPage, false);
                    }
                }
            }
        });

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isLoading = true;
                swipeRefresh.setRefreshing(true);
                getComment(startPage, true);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isResumeFirsTime) {
            // Mendapatkan data awal
            commentList = new ArrayList<>();
            commentRequest = new CommentRequest(context, reviewID);
            getComment(startPage, false);
            isResumeFirsTime = false;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        resetState();
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

    private void getComment(Integer page, final Boolean refreshPage) {
        // Menghilangkan pesan setiap kali method dijalankan
        textMessage.setVisibility(View.GONE);

        commentRequest.sendRequest(page, new CommentRequest.Callback() {
            @Override
            public void onSuccess(Integer pages, List<Comment> comments) {
                if (!isLoadFirstTimeSuccess) {
                    int insertIndex = commentList.size();
                    commentList.addAll(insertIndex, comments);
                    createAdapter();
                    progressBar.setVisibility(View.GONE);
                    totalPage = pages;
                    isLoadFirstTimeSuccess = true;
                } else {
                    if (refreshPage) {
                        commentList.clear();
                        commentAdapter.notifyDataSetChanged();
                    }
                    int insertIndex = commentList.size();
                    commentList.addAll(insertIndex, comments);
                    commentAdapter.notifyItemRangeInserted(insertIndex, comments.size());
                    recyclerComment.scrollToPosition(insertIndex);
                }
                currentPage++;
            }

            @Override
            public void onNotFound() {
                progressBar.setVisibility(View.GONE);
                textMessage.setVisibility(View.VISIBLE);
                textMessage.setText(R.string.empty_comment);
            }

            @Override
            public void onError(String message) {
                if (!isLoadFirstTimeSuccess) {
                    progressBar.setVisibility(View.GONE);
                    textMessage.setVisibility(View.VISIBLE);
                    textMessage.setText(message);
                } else {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                }
            }
        });
        isLoading = false;
        swipeRefresh.setRefreshing(false);
    }

    private void gotoEmptyAccount() {
        Intent intent = new Intent(context, EmptyAccountActivity.class);
        context.startActivity(intent);
    }

    private void addComment() {
        AddCommentRequest addCommentRequest = new AddCommentRequest(context, reviewID, comment);
        addCommentRequest.sendRequest(new AddCommentRequest.Callback() {
            @Override
            public void onSuccess(Comment comment) {
                if (!isLoadFirstTimeSuccess) {
                    createAdapter();
                    textMessage.setVisibility(View.GONE);
                }
                int insertIndex = commentList.size();
                commentList.add(insertIndex, comment);
                commentAdapter.notifyItemInserted(insertIndex);
                recyclerComment.scrollToPosition(insertIndex);
                inputComment.getText().clear();
            }

            @Override
            public void onFailed(String message) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createAdapter() {
        commentAdapter = new CommentAdapter(context, commentList);
        recyclerComment.setAdapter(commentAdapter);
        recyclerComment.setLayoutManager(new LinearLayoutManager(context));
        recyclerComment.setVisibility(View.VISIBLE);
    }

    private void resetState() {
        isResumeFirsTime = true;
        isLoadFirstTimeSuccess = false;
        isLoading = true;
        currentPage = 1;
    }
}
