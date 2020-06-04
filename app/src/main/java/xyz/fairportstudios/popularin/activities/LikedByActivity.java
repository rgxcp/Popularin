package xyz.fairportstudios.popularin.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import xyz.fairportstudios.popularin.apis.popularin.get.LikeFromAllRequest;
import xyz.fairportstudios.popularin.fragments.LikeFromAllFragment;
import xyz.fairportstudios.popularin.fragments.LikeFromFollowingFragment;
import xyz.fairportstudios.popularin.models.User;
import xyz.fairportstudios.popularin.preferences.Auth;
import xyz.fairportstudios.popularin.statics.Popularin;

public class LikedByActivity extends AppCompatActivity {
    private LikeFromAllRequest likeFromAllRequest;
    private ProgressBar progressBar;
    private RelativeLayout anchorLayout;
    private TextView textMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Binding
        final Context context = LikedByActivity.this;

        // Extra
        Intent intent = getIntent();
        final String reviewID = intent.getStringExtra(Popularin.REVIEW_ID);

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
            toolbar.setTitle(R.string.liked_by);

            // Tab pager
            PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
            pagerAdapter.addFragment(new LikeFromAllFragment(reviewID), getString(R.string.all));
            pagerAdapter.addFragment(new LikeFromFollowingFragment(reviewID), getString(R.string.following));
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
            final RecyclerView recyclerUser = findViewById(R.id.recycler_rtr_layout);
            final Toolbar toolbar = findViewById(R.id.toolbar_rtr_layout);

            // Toolbar
            toolbar.setTitle(R.string.liked_by);

            // Request
            List<User> userList = new ArrayList<>();
            likeFromAllRequest = new LikeFromAllRequest(context, userList, recyclerUser);
            getLikeFromAll(reviewID);

            // Activity
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
    }

    private void getLikeFromAll(String reviewID) {
        String requestURL = likeFromAllRequest.getRequestURL(reviewID, 1);
        likeFromAllRequest.sendRequest(requestURL, new LikeFromAllRequest.APICallback() {
            @Override
            public void onSuccess() {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onEmpty() {
                progressBar.setVisibility(View.GONE);
                textMessage.setVisibility(View.VISIBLE);
                textMessage.setText(R.string.empty_like);
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
