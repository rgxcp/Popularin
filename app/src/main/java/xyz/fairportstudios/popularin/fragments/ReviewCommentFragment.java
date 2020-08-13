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
import xyz.fairportstudios.popularin.activities.UserDetailActivity;
import xyz.fairportstudios.popularin.adapters.CommentAdapter;
import xyz.fairportstudios.popularin.apis.popularin.delete.DeleteCommentRequest;
import xyz.fairportstudios.popularin.apis.popularin.get.CommentRequest;
import xyz.fairportstudios.popularin.apis.popularin.post.AddCommentRequest;
import xyz.fairportstudios.popularin.models.Comment;
import xyz.fairportstudios.popularin.preferences.Auth;
import xyz.fairportstudios.popularin.statics.Popularin;

public class ReviewCommentFragment extends Fragment implements CommentAdapter.OnClickListener {
    // Variable untuk fitur onResume
    private boolean mIsResumeFirstTime = true;

    // Variable untuk fitur load more
    private boolean mIsLoading = true;
    private boolean mIsLoadFirstTimeSuccess = false;
    private int mStartPage = 1;
    private int mCurrentPage = 1;
    private int mTotalPage;

    // Variable member
    private int mAuthID;
    private Context mContext;
    private CommentAdapter mCommentAdapter;
    private CommentAdapter.OnClickListener mOnClickListener;
    private CommentRequest mCommentRequest;
    private EditText mInputComment;
    private ImageView mImageSend;
    private List<Comment> mCommentList;
    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerComment;
    private String mComment;
    private SwipeRefreshLayout mSwipeRefresh;
    private TextView mTextMessage;

    // Variable constructor
    private int mReviewID;

    public ReviewCommentFragment(int reviewID) {
        mReviewID = reviewID;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_review_comment, container, false);

        // Context
        mContext = getActivity();

        // Binding
        mInputComment = view.findViewById(R.id.input_frc_comment);
        mImageSend = view.findViewById(R.id.image_frc_send);
        mProgressBar = view.findViewById(R.id.pbr_rr_layout);
        mRecyclerComment = view.findViewById(R.id.recycler_rr_layout);
        mSwipeRefresh = view.findViewById(R.id.swipe_refresh_rr_layout);
        mTextMessage = view.findViewById(R.id.text_rr_message);

        // Auth
        Auth auth = new Auth(mContext);
        mAuthID = auth.getAuthID();
        final boolean isAuth = auth.isAuth();

        // Text watcher
        mInputComment.addTextChangedListener(commentWatcher);

        // Activity
        mImageSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAuth && !mIsLoading) {
                    mIsLoading = true;
                    addComment();
                } else {
                    gotoEmptyAccount();
                }
            }
        });

        mRecyclerComment.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (!mIsLoading && mCurrentPage <= mTotalPage) {
                        mIsLoading = true;
                        mSwipeRefresh.setRefreshing(true);
                        getComment(mCurrentPage, false);
                    }
                }
            }
        });

        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mIsLoading = true;
                mSwipeRefresh.setRefreshing(true);
                getComment(mStartPage, true);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mIsResumeFirstTime) {
            // Mendapatkan data awal
            mIsResumeFirstTime = false;
            mOnClickListener = this;
            mCommentList = new ArrayList<>();
            mCommentRequest = new CommentRequest(mContext, mReviewID);
            getComment(mStartPage, false);
        }
    }

    @Override
    public void onCommentProfileClick(int position) {
        Comment currentItem = mCommentList.get(position);
        int id = currentItem.getUser_id();
        gotoUserDetail(id);
    }

    @Override
    public void onCommentDeleteClick(int position) {
        Comment currentItem = mCommentList.get(position);
        int id = currentItem.getId();
        if (!mIsLoading) {
            mIsLoading = true;
            deleteComment(id, position);
        }
    }

    private TextWatcher commentWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // Tidak digunakan
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            mComment = mInputComment.getText().toString();
            if (!mComment.isEmpty()) {
                mImageSend.setVisibility(View.VISIBLE);
            } else {
                mImageSend.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            // Tidak digunakan
        }
    };

    private void createAdapter() {
        mCommentAdapter = new CommentAdapter(mContext, mAuthID, mCommentList, mOnClickListener);
        mRecyclerComment.setAdapter(mCommentAdapter);
        mRecyclerComment.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerComment.setVisibility(View.VISIBLE);
    }

    private void getComment(int page, final boolean refreshPage) {
        mCommentRequest.sendRequest(page, new CommentRequest.Callback() {
            @Override
            public void onSuccess(int totalPage, List<Comment> commentList) {
                if (!mIsLoadFirstTimeSuccess) {
                    int insertIndex = mCommentList.size();
                    mCommentList.addAll(insertIndex, commentList);
                    createAdapter();
                    mProgressBar.setVisibility(View.GONE);
                    mTotalPage = totalPage;
                    mIsLoadFirstTimeSuccess = true;
                } else {
                    if (refreshPage) {
                        mCurrentPage = 1;
                        mCommentList.clear();
                        mCommentAdapter.notifyDataSetChanged();
                    }
                    int insertIndex = mCommentList.size();
                    mCommentList.addAll(insertIndex, commentList);
                    mCommentAdapter.notifyItemRangeInserted(insertIndex, commentList.size());
                    mRecyclerComment.scrollToPosition(insertIndex);
                }
                mTextMessage.setVisibility(View.GONE);
                mCurrentPage++;
            }

            @Override
            public void onNotFound() {
                if (!mIsLoadFirstTimeSuccess) {
                    mProgressBar.setVisibility(View.GONE);
                } else {
                    mCurrentPage = 1;
                    mCommentList.clear();
                    mCommentAdapter.notifyDataSetChanged();
                }
                mTextMessage.setVisibility(View.VISIBLE);
                mTextMessage.setText(R.string.empty_comment);
            }

            @Override
            public void onError(String message) {
                if (!mIsLoadFirstTimeSuccess) {
                    mProgressBar.setVisibility(View.GONE);
                    mTextMessage.setVisibility(View.VISIBLE);
                    mTextMessage.setText(message);
                } else {
                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Memberhentikan loading
        mIsLoading = false;
        mSwipeRefresh.setRefreshing(false);
    }

    private void addComment() {
        AddCommentRequest addCommentRequest = new AddCommentRequest(mContext, mReviewID, mComment);
        addCommentRequest.sendRequest(new AddCommentRequest.Callback() {
            @Override
            public void onSuccess(Comment comment) {
                int insertIndex = mCommentList.size();
                mCommentList.add(insertIndex, comment);
                if (!mIsLoadFirstTimeSuccess) {
                    createAdapter();
                    mIsLoadFirstTimeSuccess = true;
                }
                mCommentAdapter.notifyItemInserted(insertIndex);
                mRecyclerComment.scrollToPosition(insertIndex);
                mTextMessage.setVisibility(View.GONE);
                mInputComment.getText().clear();
            }

            @Override
            public void onFailed(String message) {
                Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
            }
        });

        // Memberhentikan loading
        mIsLoading = false;
    }

    private void deleteComment(final int id, final int position) {
        DeleteCommentRequest deleteCommentRequest = new DeleteCommentRequest(mContext, id);
        deleteCommentRequest.sendRequest(new DeleteCommentRequest.Callback() {
            @Override
            public void onSuccess() {
                mCommentList.remove(position);
                mCommentAdapter.notifyItemRemoved(position);
                if (mCommentList.isEmpty()) {
                    mTextMessage.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onError(String message) {
                Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
            }
        });

        // Memberhentikan loading
        mIsLoading = false;
    }

    private void gotoUserDetail(int id) {
        Intent intent = new Intent(mContext, UserDetailActivity.class);
        intent.putExtra(Popularin.USER_ID, id);
        startActivity(intent);
    }

    private void gotoEmptyAccount() {
        Intent intent = new Intent(mContext, EmptyAccountActivity.class);
        mContext.startActivity(intent);
    }
}
