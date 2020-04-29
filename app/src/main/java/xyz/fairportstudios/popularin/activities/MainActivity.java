package xyz.fairportstudios.popularin.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.apis.popularin.SignOutRequest;
import xyz.fairportstudios.popularin.preferences.Auth;

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
                Intent gotoSignUp = new Intent(this, SignUpActivity.class);
                startActivity(gotoSignUp);
                break;
            case R.id.menu_nd_signin:
                Intent gotoSignIn = new Intent(this, SignInActivity.class);
                startActivity(gotoSignIn);
                break;
            case R.id.menu_nd_signout:
                // Menyimpan data
                Auth auth = new Auth(this);
                String id = auth.getAuthID();
                String token = auth.getAuthToken();

                // Mengirim data
                SignOutRequest signOutRequest = new SignOutRequest(
                        this,
                        id,
                        token);

                // Mendapatkan hasil
                signOutRequest.sendRequest(new SignOutRequest.JSONCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        try {
                            int status = response.getInt("status");

                            if (status == 525) {
                                Auth auth = new Auth(MainActivity.this);
                                auth.delAuth();
                            } else if (status == 616) {
                                String errorMessage = response.getString("message");
                                Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainActivity.this, "Ada kesalahan dalam database", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException error) {
                            error.printStackTrace();
                        }
                    }
                });

                drawer.closeDrawer(GravityCompat.START);
                break;
        }

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
