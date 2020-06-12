package xyz.fairportstudios.popularin.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.adapters.UserAdapter;
import xyz.fairportstudios.popularin.apis.popularin.get.LikeFromFollowingRequest;
import xyz.fairportstudios.popularin.models.User;

public class LikeFromFollowingFragment extends Fragment {
    // Variable untuk fitur onResume
    private Boolean isResumeFirsTime = true;

    // Variable untuk fitur load more
    private Boolean isLoadFirstTime = true;
    private Boolean isLoading = true;
    private Integer currentPage = 1;
    private Integer totalPage;

    // Variable member
    private Context context;
    private CoordinatorLayout anchorLayout;
    private LikeFromFollowingRequest likeFromFollowingRequest;
    private List<User> userList;
    private ProgressBar progressBar;
    private RecyclerView recyclerUser;
    private SwipeRefreshLayout swipeRefresh;
    private TextView textMessage;
    private UserAdapter userAdapter;

    // Variable constructor
    private Integer reviewID;

    public LikeFromFollowingFragment(Integer reviewID) {
        this.reviewID = reviewID;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reusable_recycler, container, false);

        // Context
        context = getActivity();

        // Binding
        anchorLayout = view.findViewById(R.id.anchor_rr_layout);
        progressBar = view.findViewById(R.id.pbr_rr_layout);
        recyclerUser = view.findViewById(R.id.recycler_rr_layout);
        swipeRefresh = view.findViewById(R.id.swipe_refresh_rr_layout);
        textMessage = view.findViewById(R.id.text_rr_message);

        // Activity
        recyclerUser.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (!isLoading && currentPage <= totalPage) {
                        isLoading = true;
                        getLikeFromFollowing(context, currentPage);
                        swipeRefresh.setRefreshing(true);
                    }
                }
            }
        });

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isLoadFirstTime) {
                    getLikeFromFollowing(context, currentPage);
                }
                swipeRefresh.setRefreshing(false);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isResumeFirsTime) {
            // Mendapatkan data awal
            userList = new ArrayList<>();
            likeFromFollowingRequest = new LikeFromFollowingRequest(context, reviewID);
            getLikeFromFollowing(context, currentPage);
        }
    }

    private void getLikeFromFollowing(final Context context, Integer page) {
        // Menghilangkan pesan setiap kali method dijalankan
        textMessage.setVisibility(View.GONE);

        likeFromFollowingRequest.sendRequest(page, new LikeFromFollowingRequest.Callback() {
            @Override
            public void onSuccess(Integer pages, List<User> users) {
                if (isLoadFirstTime) {
                    int insertIndex = userList.size();
                    userList.addAll(insertIndex, users);
                    userAdapter = new UserAdapter(context, userList);
                    recyclerUser.setAdapter(userAdapter);
                    recyclerUser.setLayoutManager(new LinearLayoutManager(context));
                    recyclerUser.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    totalPage = pages;
                    isLoadFirstTime = false;
                    isResumeFirsTime = false;
                } else {
                    int insertIndex = userList.size();
                    userList.addAll(insertIndex, users);
                    userAdapter.notifyItemRangeInserted(insertIndex, users.size());
                    recyclerUser.scrollToPosition(insertIndex);
                    swipeRefresh.setRefreshing(false);
                }
                currentPage++;
                isLoadFirstTime = false;
            }

            @Override
            public void onNotFound() {
                progressBar.setVisibility(View.GONE);
                textMessage.setVisibility(View.VISIBLE);
                textMessage.setText(R.string.empty_review_like_from_following);
            }

            @Override
            public void onError(String message) {
                if (isLoadFirstTime) {
                    progressBar.setVisibility(View.GONE);
                    textMessage.setVisibility(View.VISIBLE);
                    textMessage.setText(message);
                } else {
                    Snackbar.make(anchorLayout, message, Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }
}
