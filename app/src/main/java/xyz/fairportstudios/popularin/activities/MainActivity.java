package xyz.fairportstudios.popularin.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.fragments.AddFragment;
import xyz.fairportstudios.popularin.fragments.HomeFragment;
import xyz.fairportstudios.popularin.fragments.ProfileFragment;
import xyz.fairportstudios.popularin.fragments.ReviewFragment;
import xyz.fairportstudios.popularin.fragments.SearchFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView mBNV = findViewById(R.id.bnm_am_main);
        mBNV.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frm_am_main, new HomeFragment())
                .commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment mSelectedFragment = null;

            switch (item.getItemId()) {
                case R.id.bnm_home:
                    mSelectedFragment = new HomeFragment();
                    break;
                case  R.id.bnm_search:
                    mSelectedFragment = new SearchFragment();
                    break;
                case  R.id.bnm_add:
                    mSelectedFragment = new AddFragment();
                    break;
                case R.id.bnm_review:
                    mSelectedFragment = new ReviewFragment();
                    break;
                case R.id.bnm_profile:
                    mSelectedFragment = new ProfileFragment();
                    break;
            }

            if (mSelectedFragment != null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frm_am_main, mSelectedFragment)
                        .commit();
            }

            return true;
        }
    };
}
