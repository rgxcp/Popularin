package xyz.fairportstudios.popularin.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.adapters.PagerAdapter;
import xyz.fairportstudios.popularin.fragments.FollowerFragment;
import xyz.fairportstudios.popularin.fragments.FollowingFragment;
import xyz.fairportstudios.popularin.fragments.MutualFragment;

public class SocialActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.global_toolbar_pager);

        // Binding
        TabLayout tabLayout = findViewById(R.id.tab_gtp_layout);
        Toolbar toolbar = findViewById(R.id.toolbar_gtp_layout);
        ViewPager viewPager = findViewById(R.id.pager_gtp_layout);

        // Extra
        Intent intent = getIntent();
        String userID = intent.getStringExtra("USER_ID");
        boolean isSelf = intent.getBooleanExtra("IS_SELF", false);

        // Toolbar
        toolbar.setTitle("Social");

        // Tab
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), 0);
        if (isSelf) {
            pagerAdapter.addFragment(new FollowingFragment(userID), "MENGIKUTI");
            pagerAdapter.addFragment(new FollowerFragment(userID), "PENGIKUT");
        } else {
            pagerAdapter.addFragment(new MutualFragment(userID), "MUTUAL");
            pagerAdapter.addFragment(new FollowingFragment(userID), "MENGIKUTI");
            pagerAdapter.addFragment(new FollowerFragment(userID), "PENGIKUT");
        }
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
