package xyz.fairportstudios.popularin.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.fragments.AccountFragment;
import xyz.fairportstudios.popularin.fragments.AiringFragment;
import xyz.fairportstudios.popularin.fragments.EmptyAccountFragment;
import xyz.fairportstudios.popularin.fragments.GenreFragment;
import xyz.fairportstudios.popularin.fragments.ReviewFragment;
import xyz.fairportstudios.popularin.fragments.SearchFragment;
import xyz.fairportstudios.popularin.fragments.TimelineFragment;
import xyz.fairportstudios.popularin.preferences.Auth;

public class MainActivity extends AppCompatActivity {
    // Variable untuk fitur double tap to exit
    private static final int TIME_INTERVAL = 2000;
    private static long sTimeBackPressed;

    // Variable member
    private boolean mIsAuth;
    private Context mContext;
    private Fragment mSelectedFragment;
    private final Fragment mAccountFragment = new AccountFragment();
    private final Fragment mAiringFragment = new AiringFragment();
    private final Fragment mEmptyAccountFragment = new EmptyAccountFragment();
    private final Fragment mGenreFragment = new GenreFragment();
    private final Fragment mReviewFragment = new ReviewFragment();
    private final Fragment mSearchFragment = new SearchFragment();
    private final Fragment mTimelineFragment = new TimelineFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Context
        mContext = MainActivity.this;

        // Auth
        mIsAuth = new Auth(mContext).isAuth();

        // Bottom navigation
        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation_am_layout);
        bottomNavigation.setOnNavigationItemSelectedListener(listener);

        // Menampilkan fragment otomatis sesuai kondisi
        if (mIsAuth) {
            mSelectedFragment = mTimelineFragment;
        } else {
            mSelectedFragment = mGenreFragment;
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_am_container, mSelectedFragment)
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (TIME_INTERVAL + sTimeBackPressed > System.currentTimeMillis()) {
            super.onBackPressed();
        } else {
            Toast.makeText(mContext, R.string.press_once_more_to_exit, Toast.LENGTH_SHORT).show();
        }
        sTimeBackPressed = System.currentTimeMillis();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener listener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_bn_home:
                    if (mIsAuth) {
                        mSelectedFragment = mTimelineFragment;
                    } else {
                        mSelectedFragment = mGenreFragment;
                    }
                    break;
                case R.id.menu_bn_airing:
                    mSelectedFragment = mAiringFragment;
                    break;
                case R.id.menu_bn_review:
                    mSelectedFragment = mReviewFragment;
                    break;
                case R.id.menu_bn_search:
                    mSelectedFragment = mSearchFragment;
                    break;
                case R.id.menu_bn_account:
                    if (mIsAuth) {
                        mSelectedFragment = mAccountFragment;
                    } else {
                        mSelectedFragment = mEmptyAccountFragment;
                    }
                    break;
            }

            if (mSelectedFragment != null) {
                // Menampilkan fragment sesuai pilihan
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_am_container, mSelectedFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
            }

            return true;
        }
    };
}
