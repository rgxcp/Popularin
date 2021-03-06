package xyz.fairportstudios.popularin.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import xyz.fairportstudios.popularin.R;
import xyz.fairportstudios.popularin.fragments.EditProfileFragment;

public class EditProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_aep_container, new EditProfileFragment())
                .commit();
    }
}
