package xyz.fairportstudios.popularin.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.adapters.PagerAdapter;
import xyz.fairportstudios.popularin.adapters.UserAdapter;
import xyz.fairportstudios.popularin.apis.popularin.get.WatchlistFromAllRequest;
import xyz.fairportstudios.popularin.fragments.WatchlistFromAllFragment;
import xyz.fairportstudios.popularin.fragments.WatchlistFromFollowingFragment;
import xyz.fairportstudios.popularin.models.User;
import xyz.fairportstudios.popularin.preferences.Auth;
import xyz.fairportstudios.popularin.statics.Popularin;

public class WatchlistedByActivity extends AppCompatActivity {
    // Variable untuk fitur load more
    private Boolean isLoadFirstTime = true;
    private Boolean isLoading = true;
    private Integer currentPage = 1;
    private Integer totalPage;

    // Variable member
    private List<User> userList;
    private ProgressBar progressBar;
    private RecyclerView recyclerUser;
    private RelativeLayout anchorLayout;
    private SwipeRefreshLayout swipeRefresh;
    private TextView textMessage;
    private UserAdapter userAdapter;
    private WatchlistFromAllRequest watchlistFromAllRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Context
        final Context context = WatchlistedByActivity.this;

        // Extra
        Intent intent = getIntent();
        final Integer filmID = intent.getIntExtra(Popularin.FILM_ID, 0);

        // Auth
        final boolean isAuth = new Auth(context).isAuth();

        // Menampilkan layout berdasarkan kondisi
        if (isAuth) {
            setContentView(R.layout.reusable_toolbar_pager);

            // Binding
            TabLayout tabLayout = findViewById(R.id.tab_rtp_layout);
            Toolbar toolbar = findViewById(R.id.toolbar_rtp_layout);
            ViewPager viewPager = findViewById(R.id.pager_rtp_layout);

            // Toolbar
            toolbar.setTitle(R.string.watchlisted_by);

            // Tab pager
            PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
            pagerAdapter.addFragment(new WatchlistFromAllFragment(filmID), getString(R.string.all));
            pagerAdapter.addFragment(new WatchlistFromFollowingFragment(filmID), getString(R.string.following));
            viewPager.setAdapter(pagerAdapter);
            tabLayout.setupWithViewPager(viewPager);

            // Activity
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
        } else {
            setContentView(R.layout.reusable_toolbar_recycler);

            // Binding
            progressBar = findViewById(R.id.pbr_rtr_layout);
            recyclerUser = findViewById(R.id.recycler_rtr_layout);
            anchorLayout = findViewById(R.id.anchor_rtr_layout);
            swipeRefresh = findViewById(R.id.swipe_refresh_rtr_layout);
            textMessage = findViewById(R.id.text_aud_message);
            Toolbar toolbar = findViewById(R.id.toolbar_rtr_layout);

            // Toolbar
            toolbar.setTitle(R.string.watchlisted_by);

            // Mendapatkan data awal
            userList = new ArrayList<>();
            watchlistFromAllRequest = new WatchlistFromAllRequest(context, filmID);
            getWatchlistFromAll(context, currentPage);

            // Activity
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });

            recyclerUser.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                        if (!isLoading && currentPage <= totalPage) {
                            isLoading = true;
                            getWatchlistFromAll(context, currentPage);
                            swipeRefresh.setRefreshing(true);
                        }
                    }
                }
            });

            swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    if (isLoadFirstTime) {
                        getWatchlistFromAll(context, currentPage);
                    }
                    swipeRefresh.setRefreshing(false);
                }
            });
        }
    }

    private void getWatchlistFromAll(final Context context, Integer page) {
        // Menghilangkan pesan setiap kali method dijalankan
        textMessage.setVisibility(View.GONE);

        watchlistFromAllRequest.sendRequest(page, new WatchlistFromAllRequest.Callback() {
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
                } else {
                    int insertIndex = userList.size();
                    userList.addAll(insertIndex, users);
                    userAdapter.notifyItemRangeInserted(insertIndex, users.size());
                    recyclerUser.scrollToPosition(insertIndex);
                    swipeRefresh.setRefreshing(false);
                }
                currentPage++;
                isLoading = false;
            }

            @Override
            public void onNotFound() {
                progressBar.setVisibility(View.GONE);
                textMessage.setVisibility(View.VISIBLE);
                textMessage.setText(R.string.empty_film_watchlist);
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
