package xyz.fairportstudios.popularin.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.adapters.ReviewPagerAdapter;
import xyz.fairportstudios.popularin.fragments.ReviewCommentFragment;
import xyz.fairportstudios.popularin.fragments.ReviewDetailFragment;

public class ReviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        // Binding
        TabLayout tabLayout = findViewById(R.id.tab_ar_layout);
        Toolbar toolbar = findViewById(R.id.toolbar_ar_layout);
        ViewPager viewPager = findViewById(R.id.pager_ar_layout);

        // Bundle
        Bundle bundle = getIntent().getExtras();
        String reviewID = Objects.requireNonNull(bundle).getString("REVIEW_ID");

        // Tabbed
        ReviewPagerAdapter adapter = new ReviewPagerAdapter(getSupportFragmentManager(), 0);
        adapter.addFragment(new ReviewDetailFragment(reviewID), "Detail");
        adapter.addFragment(new ReviewCommentFragment(reviewID), "Komen");
        viewPager.setAdapter(adapter);
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
