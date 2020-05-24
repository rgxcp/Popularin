package xyz.fairportstudios.popularin.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.adapters.PagerAdapter;
import xyz.fairportstudios.popularin.fragments.LikeFromAllFragment;
import xyz.fairportstudios.popularin.fragments.LikeFromFollowingFragment;
import xyz.fairportstudios.popularin.services.Popularin;

public class LikedByActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reusable_toolbar_pager);

        // Binding
        TabLayout tabLayout = findViewById(R.id.tab_rtp_layout);
        Toolbar toolbar = findViewById(R.id.toolbar_rtp_layout);
        ViewPager viewPager = findViewById(R.id.pager_rtp_layout);

        // Extra
        Intent intent = getIntent();
        String reviewID = intent.getStringExtra(Popularin.REVIEW_ID);

        // Toolbar
        toolbar.setTitle(R.string.liked_by);

        // Tab Pager
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
    }
}
