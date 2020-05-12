package xyz.fairportstudios.popularin.activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.adapters.PagerAdapter;
import xyz.fairportstudios.popularin.fragments.LikedReviewFragment;
import xyz.fairportstudios.popularin.fragments.ReviewFromAllFragment;
import xyz.fairportstudios.popularin.fragments.ReviewFromFollowingFragment;
import xyz.fairportstudios.popularin.fragments.SelfReviewFragment;

public class FilmReviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.global_toolbar_pager);

        // Binding
        TabLayout tabLayout = findViewById(R.id.tab_gtp_layout);
        Toolbar toolbar = findViewById(R.id.toolbar_gtp_layout);
        ViewPager viewPager = findViewById(R.id.pager_gtp_layout);

        // Bundle
        Bundle bundle = getIntent().getExtras();
        String filmID = Objects.requireNonNull(bundle).getString("FILM_ID");

        // Toolbar
        toolbar.setTitle("Ulasan");

        // Tab
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), 0);
        pagerAdapter.addFragment(new ReviewFromAllFragment(filmID), "SEMUA");
        pagerAdapter.addFragment(new ReviewFromFollowingFragment(filmID), "MENGIKUTI");
        pagerAdapter.addFragment(new LikedReviewFragment(filmID), "DISUKAI");
        pagerAdapter.addFragment(new SelfReviewFragment(filmID), "SAYA");
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
