package xyz.fairportstudios.popularin.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import xyz.fairportstudios.popularin.R;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Binding
        drawer = findViewById(R.id.drawer_am_layout);
        NavigationView navigationView = findViewById(R.id.nav_view_am_layout);
        Toolbar toolbar = findViewById(R.id.toolbar_am_layout);

        // Toolbar
        setSupportActionBar(toolbar);

        // Navigation drawer icon
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer,
                toolbar,
                R.string.open_navigation_drawer,
                R.string.close_navigation_drawer);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Navigation view
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_nd_genre:
                Log.i("TAG", "Genre Clicked");
                break;
            case R.id.menu_nd_profile:
                Log.i("TAG", "Profile Clicked");
                break;
            case R.id.menu_nd_signup:
                Log.i("TAG", "Sign Up Clicked");
                break;
            case R.id.menu_nd_signin:
                Log.i("TAG", "Sign In Clicked");
                break;
            case R.id.menu_nd_signout:
                Log.i("TAG", "Sign Out Clicked");
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
