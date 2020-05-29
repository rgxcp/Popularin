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
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.adapters.PagerAdapter;
import xyz.fairportstudios.popularin.apis.popularin.get.ReviewFromAllRequest;
import xyz.fairportstudios.popularin.fragments.LikedReviewFragment;
import xyz.fairportstudios.popularin.fragments.ReviewFromAllFragment;
import xyz.fairportstudios.popularin.fragments.ReviewFromFollowingFragment;
import xyz.fairportstudios.popularin.fragments.SelfReviewFragment;
import xyz.fairportstudios.popularin.models.FilmReview;
import xyz.fairportstudios.popularin.preferences.Auth;
import xyz.fairportstudios.popularin.services.Popularin;

public class FilmReviewActivity extends AppCompatActivity {
    // Variable untuk fitur load more
    private Integer currentPage = 1;
    private Integer totalPage = 1;

    // Variable member
    private ProgressBar progressBar;
    private RelativeLayout anchorLayout;
    private ReviewFromAllRequest reviewFromAllRequest;
    private TextView textMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Binding
        final Context context = FilmReviewActivity.this;

        // Extra
        Intent intent = getIntent();
        final String filmID = intent.getStringExtra(Popularin.FILM_ID);

        // Auth
        final boolean isAuth = new Auth(context).isAuth();

        // Tampilan
        if (isAuth) {
            setContentView(R.layout.reusable_toolbar_pager);

            // Binding
            final TabLayout tabLayout = findViewById(R.id.tab_rtp_layout);
            final Toolbar toolbar = findViewById(R.id.toolbar_rtp_layout);
            final ViewPager viewPager = findViewById(R.id.pager_rtp_layout);

            // Toolbar
            toolbar.setTitle(R.string.review);

            // Tab pager
            PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
            pagerAdapter.addFragment(new ReviewFromAllFragment(filmID), getString(R.string.all));
            pagerAdapter.addFragment(new ReviewFromFollowingFragment(filmID), getString(R.string.following));
            pagerAdapter.addFragment(new LikedReviewFragment(filmID), getString(R.string.liked));
            pagerAdapter.addFragment(new SelfReviewFragment(filmID), getString(R.string.self));
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
            anchorLayout = findViewById(R.id.anchor_rtr_layout);
            textMessage = findViewById(R.id.text_rtr_empty_result);
            final RecyclerView recyclerFilmReview = findViewById(R.id.recycler_rtr_layout);
            final Toolbar toolbar = findViewById(R.id.toolbar_rtr_layout);

            // Toolbar
            toolbar.setTitle(R.string.review);

            // Request
            List<FilmReview> filmReviewList = new ArrayList<>();
            reviewFromAllRequest = new ReviewFromAllRequest(context, filmID, filmReviewList, recyclerFilmReview);
            getAllFilmReview(currentPage);

            // Activity
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });

            recyclerFilmReview.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                        if (currentPage < totalPage) {
                            getAllFilmReview(currentPage);
                        }
                    }
                }
            });
        }
    }

    private void getAllFilmReview(Integer page) {
        reviewFromAllRequest.sendRequest(page, new ReviewFromAllRequest.APICallback() {
            @Override
            public void onSuccess(Integer lastPage) {
                totalPage = lastPage;
                currentPage++;
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onEmpty() {
                progressBar.setVisibility(View.GONE);
                textMessage.setVisibility(View.VISIBLE);
                textMessage.setText(R.string.empty_film_review);
            }

            @Override
            public void onError() {
                progressBar.setVisibility(View.GONE);
                textMessage.setVisibility(View.VISIBLE);
                textMessage.setText(R.string.not_found);
                Snackbar.make(anchorLayout, R.string.network_error, Snackbar.LENGTH_LONG).show();
            }
        });
    }
}
