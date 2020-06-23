package xyz.fairportstudios.popularin.activities;

import android.content.Context;
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
import xyz.fairportstudios.popularin.fragments.FollowerFragment;
import xyz.fairportstudios.popularin.fragments.FollowingFragment;
import xyz.fairportstudios.popularin.fragments.MutualFragment;
import xyz.fairportstudios.popularin.preferences.Auth;
import xyz.fairportstudios.popularin.statics.Popularin;

public class SocialActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reusable_toolbar_pager);

        // Context
        Context context = SocialActivity.this;

        // Binding
        TabLayout tabLayout = findViewById(R.id.tab_rtp_layout);
        Toolbar toolbar = findViewById(R.id.toolbar_rtp_layout);
        ViewPager viewPager = findViewById(R.id.pager_rtp_layout);

        // Extra
        Intent intent = getIntent();
        int userID = intent.getIntExtra(Popularin.USER_ID, 0);
        int viewPagerIndex = intent.getIntExtra(Popularin.VIEW_PAGER_INDEX, 0);

        // Auth
        Auth auth = new Auth(context);
        boolean isAuth = auth.isAuth();
        boolean isSelf = userID == auth.getAuthID();

        // Toolbar
        toolbar.setTitle(R.string.social);

        // Limit page yang akan ditampilkan
        int screenPageLimit;
        if (isAuth && !isSelf) {
            screenPageLimit = 3;
        } else {
            screenPageLimit = 2;
        }

        // Tab pager
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        pagerAdapter.addFragment(new FollowerFragment(userID), getString(R.string.follower));
        pagerAdapter.addFragment(new FollowingFragment(userID), getString(R.string.following));
        if (isAuth && !isSelf) {
            pagerAdapter.addFragment(new MutualFragment(userID), getString(R.string.mutual));
        }
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOffscreenPageLimit(screenPageLimit);
        viewPager.setCurrentItem(viewPagerIndex);
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
