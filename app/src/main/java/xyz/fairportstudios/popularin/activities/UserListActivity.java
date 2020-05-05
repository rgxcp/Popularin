package xyz.fairportstudios.popularin.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.adapters.PagerAdapter;
import xyz.fairportstudios.popularin.fragments.FromAllFragment;
import xyz.fairportstudios.popularin.fragments.FromFollowingFragment;

public class UserListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        // Binding
        TabLayout tabLayout = findViewById(R.id.tab_aul_layout);
        Toolbar toolbar = findViewById(R.id.toolbar_aul_layout);
        ViewPager viewPager = findViewById(R.id.pager_aul_layout);

        // Bundle
        Bundle bundle = getIntent().getExtras();
        String reviewID = Objects.requireNonNull(bundle).getString("REVIEW_ID");

        // Tabbed
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), 0);
        pagerAdapter.addFragment(new FromAllFragment(reviewID), "SEMUA");
        pagerAdapter.addFragment(new FromFollowingFragment(reviewID), "MENGIKUTI");
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
